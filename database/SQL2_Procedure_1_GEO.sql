DELIMITER $$


#------------------------------------------------------------------------------------------------------------------------------------------------------
# [创建分表]Produce SplitTable
# CALL proc_produce_split_table_for_geo( @result ); SELECT @result; 
# SELECT update_l, log_kind, tb_kind, remark, input_date FROM stc_update_log WHERE log_kind='NewTable' AND tb_kind='GEO' AND DATE_FORMAT(input_date,'%Y-%m-%d')=CURDATE();
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS `proc_produce_split_table_for_geo` $$
CREATE PROCEDURE `proc_produce_split_table_for_geo`(
	OUT result				VARCHAR(50)	)
BASIC_BLOCK:BEGIN
	DECLARE xSplitTag, ySplitTag, xTableName VARCHAR(50);
	DECLARE xCountUser, xRegionStart, xRegionEnd INT UNSIGNED;
	DECLARE jTomorrowDate DATE;
	DECLARE xRemark VARCHAR(300);
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
		SET xRemark = CONCAT('SplitTag=', xSplitTag, ' Exception;' );
		INSERT INTO stc_update_log(log_kind,tb_kind,remark,input_date)VALUES('NewTable','GEO',xRemark,NOW());
		SET result = 'SQLException';
	END;
	
	SELECT split_tag, region_s INTO xSplitTag, xRegionStart FROM stc_split_table WHERE tb_kind = 'GEO' AND sys_flg = 'Recent';
	SELECT COUNT(user_i) INTO xCountUser FROM tbl_user WHERE user_i >= xRegionStart AND split_tag = xSplitTag;
	
	IF ( xCountUser <= 10000 ) THEN 
		SET result = CONCAT('NotNecessaryFor ', xCountUser);
		LEAVE BASIC_BLOCK;
	END IF;
	
	START TRANSACTION;
	SET ySplitTag = getNextSplitTag( xSplitTag );
	SET xTableName = CONCAT( 'trn_geo_record_', ySplitTag );
	SET @yCreateSQL = CONCAT( 'CREATE TABLE ', xTableName, "(
		user_i					INT UNSIGNED NOT NULL,						#用户ID
		longitude				CHAR(17) NOT NULL DEFAULT '',				#经度
		latitude 				CHAR(17) NOT NULL DEFAULT '',				#纬度
		duration				SMALLINT NOT NULL DEFAULT 0,				#持续时长(分钟数)
		input_date				DATETIME NOT NULL							#登记时间		
		);");
	PREPARE stmt_create_table FROM @yCreateSQL;
	EXECUTE stmt_create_table;
	
	SET @yCreateSQL = CONCAT( 'ALTER TABLE ', xTableName, ' ADD UNIQUE INDEX idx_geo_record_', ySplitTag, '(user_i, input_date);' );
	PREPARE stmt_create_table FROM @yCreateSQL;
	EXECUTE stmt_create_table;
	
	SET jTomorrowDate = DATE_ADD( CURDATE(), INTERVAL 1 DAY);
	SET @yCreateSQL = CONCAT( 'ALTER TABLE ', xTableName, ' PARTITION BY RANGE( DAYOFWEEK(input_date) )', "(
		PARTITION PW1 VALUES LESS THAN (2),	PARTITION PW2 VALUES LESS THAN (3), PARTITION PW3 VALUES LESS THAN (4), 
		PARTITION PW4 VALUES LESS THAN (5), PARTITION PW5 VALUES LESS THAN (6), PARTITION PW6 VALUES LESS THAN (7), PARTITION PW7 VALUES LESS THAN (8)
		);");
	PREPARE stmt_create_table FROM @yCreateSQL;
	EXECUTE stmt_create_table;
	
	SELECT MAX(user_i) INTO xRegionEnd FROM tbl_user;
	INSERT INTO stc_split_table(tb_name,tb_kind,split_tag,cnt_record,calc_date,region_s,input_date,last_update,sys_flg)VALUES(xTableName,'GEO',ySplitTag,0,NOW(),xRegionEnd+1,NOW(),NOW(),'Recent');
	UPDATE stc_split_table SET region_e=xRegionEnd, sys_flg='Past' WHERE tb_kind = 'GEO' AND split_tag = xSplitTag;
	
	
	SET xRemark = CONCAT('SplitTag=', ySplitTag, ';RegionStart=', xRegionStart, ';RegionEnd=', xRegionEnd );
	INSERT INTO stc_update_log(log_kind,tb_kind,remark,input_date)VALUES('NewTable','GEO',xRemark,NOW());
	
	COMMIT;
	SET result = 'ProduceSplitTableSuccess';
END BASIC_BLOCK $$


#------------------------------------------------------------------------------------------------------------------------------------------------------
# [增加表分区(暂无用)]Update PARTITION For GEO
# CALL proc_update_partition_for_geo('unDaoDB', @result); SELECT @result; 
# SELECT TABLE_NAME,PARTITION_NAME,PARTITION_METHOD,PARTITION_EXPRESSION,PARTITION_DESCRIPTION,TABLE_ROWS FROM information_schema.PARTITIONS WHERE TABLE_SCHEMA=SCHEMA() AND TABLE_NAME LIKE 'trn_geo_record_%' ORDER BY TABLE_NAME ASC, PARTITION_NAME ASC;
# SELECT update_l, log_kind, tb_kind, remark, input_date FROM stc_update_log WHERE log_kind='Partition' AND tb_kind='GEO' AND DATE_FORMAT(input_date,'%Y-%m-%d')=CURDATE();
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS `proc_update_partition_for_geo` $$
CREATE PROCEDURE `proc_update_partition_for_geo` (
	IN	pSCHEMA_NAME			VARCHAR(50) charset utf8 COLLATE utf8_tolower_ci,
	
	OUT result					VARCHAR(50)	)
BASIC_BLOCK:BEGIN
    DECLARE xCountPartitions INT UNSIGNED;
	DECLARE qTableName, qSplitTag VARCHAR(50) charset utf8 COLLATE utf8_tolower_ci;
    DECLARE jTomorrowDate DATE;
    DECLARE jPartitionName, jTomorrowTag VARCHAR(50) charset utf8 COLLATE utf8_tolower_ci;
	DECLARE xRemark VARCHAR(300);
	DECLARE qPartitionName, xThreeDaysAgo VARCHAR(10);
	
	DECLARE cur_split_tag CURSOR FOR SELECT tb_name, split_tag FROM stc_split_table WHERE tb_kind = 'GEO' ORDER BY input_date ASC; 
	
	DECLARE CONTINUE HANDLER FOR NOT FOUND BEGIN
		SET qTableName = NULL;
	END;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
		SET xRemark = CONCAT( qTableName, ': ', jPartitionName, ' Exception;' );
		INSERT INTO stc_update_log(log_kind,tb_kind,remark,input_date)VALUES('Partition','GEO',xRemark,NOW());
		SET result = 'SQLException';
	END;
	
    SET jTomorrowDate = DATE_ADD( CURDATE(), INTERVAL 1 DAY);
    SET jPartitionName = DATE_FORMAT( jTomorrowDate, 'P%y%m%d' );
	SET jTomorrowTag = DATE_FORMAT( jTomorrowDate, '%Y-%m-%d' );
	
	#--新增表分区
	OPEN cur_split_tag;			
	CYC_BLOCK_OUTER:REPEAT
		FETCH cur_split_tag INTO qTableName, qSplitTag;
		IF ( qTableName IS NOT NULL ) THEN
			SELECT COUNT(partition_name) INTO xCountPartitions FROM information_schema.partitions WHERE table_schema=pSCHEMA_NAME AND table_name=qTableName AND partition_name=jPartitionName;
			IF (xCountPartitions = 0) THEN
				SET @xUpdateSQL = CONCAT( 'ALTER TABLE `', pSCHEMA_NAME, '`.`', qTableName, '` ADD PARTITION (PARTITION ', jPartitionName, " VALUES LESS THAN (", TO_DAYS(jTomorrowTag) ,"));" );
				PREPARE STMT FROM @xUpdateSQL;
				EXECUTE STMT;
				DEALLOCATE PREPARE STMT;
				SET xRemark = CONCAT( qTableName, ': ', jPartitionName, ' Created;' );
				INSERT INTO stc_update_log(log_kind,tb_kind,remark,input_date)VALUES('Partition','GEO',xRemark,NOW());
			END IF;
		END IF;
	UNTIL qTableName IS NULL
	END REPEAT CYC_BLOCK_OUTER;
	CLOSE cur_split_tag;
	
	#--删除过期的表分区(应调整为-4)
	BEGIN						
	DECLARE cur_partition CURSOR FOR SELECT partition_name, table_name FROM information_schema.partitions WHERE table_schema=pSCHEMA_NAME AND table_name LIKE 'trn_geo_record_%' AND create_time<DATE_ADD(CURDATE(),INTERVAL -4 DAY); 
	DECLARE CONTINUE HANDLER FOR NOT FOUND BEGIN
		SET qPartitionName = NULL;
	END;
	
	SET xThreeDaysAgo = DATE_FORMAT( DATE_ADD(CURDATE(), INTERVAL -3 DAY), '%y%m%d' );
	OPEN cur_partition;
	CYC_BLOCK_INNER:REPEAT
		FETCH cur_partition INTO qPartitionName, qTableName;
		IF ( qPartitionName IS NOT NULL ) THEN
			IF ( RIGHT(qPartitionName,6) < xThreeDaysAgo ) THEN
				SET @xUpdateSQL = CONCAT( 'ALTER TABLE ', qTableName, ' DROP PARTITION ', qPartitionName, ';' );
				PREPARE STMT FROM @xUpdateSQL;
				EXECUTE STMT;
				SET xRemark = CONCAT( qTableName, ': ', qPartitionName, ' Droped;' );
				INSERT INTO stc_update_log(log_kind,tb_kind,remark,input_date)VALUES('Partition','GEO',xRemark,NOW());
			END IF;
		END IF;
	UNTIL qPartitionName IS NULL
	END REPEAT CYC_BLOCK_INNER;
	CLOSE cur_partition;
	END;

	SET result = 'UpdatePartitionSuccess';
END BASIC_BLOCK $$


#------------------------------------------------------------------------------------------------------------------------------------------------------
# [计算分表的记录数]Calc SplitTable
# CALL proc_calc_split_table_for_geo( @result ); SELECT @result; 
# SELECT update_l, log_kind, tb_kind, remark, input_date FROM stc_update_log WHERE log_kind='Calc' AND tb_kind='GEO' AND DATE_FORMAT(input_date,'%Y-%m-%d')=CURDATE();
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS `proc_calc_split_table_for_geo` $$
CREATE PROCEDURE `proc_calc_split_table_for_geo`(
	OUT result				VARCHAR(50)	)
BASIC_BLOCK:BEGIN
	DECLARE qTableName, qSplitTag VARCHAR(50);
	DECLARE qCountRows, xCountRows INT UNSIGNED;
	DECLARE xRemark VARCHAR(300);
	
	DECLARE cur_split_tag CURSOR FOR SELECT tb_name, split_tag, cnt_record FROM stc_split_table WHERE tb_kind = 'GEO' ORDER BY input_date ASC; 
	
	DECLARE CONTINUE HANDLER FOR NOT FOUND BEGIN
		SET qTableName = NULL;
	END;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
		SET result = 'SQLException';
	END;
	
	OPEN cur_split_tag;
	CYC_BLOCK_OUTER:REPEAT
		FETCH cur_split_tag INTO qTableName, qSplitTag, qCountRows;
		IF ( qTableName IS NOT NULL ) THEN
			SET @querySQL = CONCAT( 'SELECT COUNT(*) INTO @countTableRows FROM ', qTableName );
			PREPARE stmt_query_table FROM @querySQL;
			EXECUTE stmt_query_table;
			SET xCountRows = @countTableRows;
		
			UPDATE stc_split_table SET cnt_record=xCountRows, calc_date=NOW() WHERE tb_name = qTableName;
			SET xRemark = CONCAT( qTableName, '=', qSplitTag, '=', qCountRows, '->', xCountRows, ';' );
			INSERT INTO stc_update_log(log_kind,tb_kind,remark,input_date)VALUES('Calc','GEO',xRemark,NOW());
		END IF;
	UNTIL qTableName IS NULL
	END REPEAT CYC_BLOCK_OUTER;
	CLOSE cur_split_tag;
	
	SET result = 'CalcSplitTableSuccess';
END BASIC_BLOCK $$


#------------------------------------------------------------------------------------------------------------------------------------------------------
# [自动执行机制]Auto Execute For GEO
# SELECT * FROM information_schema.events; 
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP EVENT IF EXISTS `auto_produce_split_table_for_geo` $$
CREATE EVENT `auto_produce_split_table_for_geo`
	ON SCHEDULE EVERY 1 DAY STARTS DATE_ADD(CURDATE(), INTERVAL 1*60+10 MINUTE) 
	ON COMPLETION PRESERVE
ENABLE
COMMENT 'Produce Split Tables'
DO BEGIN
	CALL proc_produce_split_table_for_geo( @result );
END$$


DROP EVENT IF EXISTS `auto_calc_split_table_for_geo` $$
CREATE EVENT `auto_calc_split_table_for_geo`
	ON SCHEDULE EVERY 1 DAY STARTS DATE_ADD(CURDATE(), INTERVAL 1*60+50 MINUTE) 
	ON COMPLETION PRESERVE
ENABLE
COMMENT 'Calc Split Tables'
DO BEGIN
	CALL proc_calc_split_table_for_geo( @result );
END$$


#------------------------------------------------------------------------------------------------------------------------------------------------------
# 发出请求
# CALL proc_send_relation('owCe05DPc7018WqYcIcOv34JvtTY','1','TestNickName','56789',@rID,@result); SELECT @rID,@result;
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS `proc_send_relation` $$
CREATE PROCEDURE `proc_send_relation`(
	IN	pWE_ID				VARCHAR(50),
	IN	pUSER_I				VARCHAR(10),
	IN	pNICK_NAME			VARCHAR(50),
	IN	pACODE_FILE_NAME	VARCHAR(20),
	
	OUT	r_id				VARCHAR(10),
	OUT	result				VARCHAR(50) )
BASIC_BLOCK:BEGIN
	DECLARE xCount, qLoverR INT;
	DECLARE qACodeName VARCHAR(20);
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
		ROLLBACK;
		SET result = 'SQLException';
	END;
	
	SELECT COUNT(user_i) INTO xCount FROM tbl_user WHERE user_i=pUSER_I AND we_id=pWE_ID;
	IF (xCount = 0) THEN
		SET r_id = '0';
		SET result = 'ValidUser';
	ELSE
		BEGIN
		DECLARE CONTINUE HANDLER FOR NOT FOUND BEGIN
			SET qLoverR = 0;
		END;
		SELECT lover_r, acode_file_name INTO qLoverR, qACodeName FROM tbl_user_relation WHERE user_i_w=pUSER_I AND user_i_v=0 LIMIT 1;
		IF (qLoverR = 0) THEN
			INSERT INTO tbl_user_relation(user_i_w,user_nick_w,dt_start,dt_end,dt_start_h,dt_end_h,acode_file_name,input_date,last_update)VALUES(pUSER_I,pNICK_NAME,NOW(),NOW(),NOW(),NOW(),pACODE_FILE_NAME,NOW(),NOW());
			SET r_id = LAST_INSERT_ID();
			SET result = 'SendRelationSuccess';
		ELSE
			SET r_id = qLoverR;
			SET result = qACodeName;
		END IF;
		END;
	END IF;
END BASIC_BLOCK $$ 


#------------------------------------------------------------------------------------------------------------------------------------------------------
# 接受请求
# CALL proc_accept_relation('owCe05DPc7018WqYcIcOv34JvtTY','1','TestNickName','1','789',@result); SELECT @result;
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS `proc_accept_relation` $$
CREATE PROCEDURE `proc_accept_relation`(
	IN	pWE_ID				VARCHAR(50),
	IN	pUSER_I				VARCHAR(10),
	IN	pNICK_NAME			VARCHAR(50),
	IN	pLOVER_R			VARCHAR(10),
	IN	pCHECK_CODE			VARCHAR(10),	#--3位校验码:即acode_file_name末三位
	
	OUT	result				VARCHAR(50) )
BASIC_BLOCK:BEGIN
	DECLARE xCount, qWalkerID INT;
	DECLARE qViewerNick, qWalkerNick VARCHAR(50);
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
		ROLLBACK;
		SET result = 'SQLException';
	END;
	
	SELECT COUNT(user_i) INTO xCount FROM tbl_user WHERE user_i=pUSER_I AND we_id=pWE_ID;
	IF (xCount = 0) THEN
		SET result = 'ValidUser';
	ELSE
		BEGIN
		DECLARE CONTINUE HANDLER FOR NOT FOUND BEGIN
			SET qWalkerID = 0;
			SET qViewerNick = '';
		END;
		SELECT user_i_w INTO qWalkerID FROM tbl_user_relation WHERE lover_r=pLOVER_R AND user_i_v=0 AND RIGHT(acode_file_name,3)=pCHECK_CODE;
		
		IF (qWalkerID = 0) THEN
			SET result = 'ValidUser';
		ELSEIF (qWalkerID = pUSER_I) THEN
			SET result = 'ValidSelfUser';
		ELSE
			SELECT user_nick_v, user_nick_w INTO qViewerNick, qWalkerNick FROM tbl_user_relation WHERE user_i_v=pUSER_I AND user_i_w=qWalkerID ORDER BY input_date DESC LIMIT 1;
			IF (qViewerNick = '') THEN
				UPDATE tbl_user_relation SET user_i_v=pUSER_I, user_nick_v=pNICK_NAME, last_update=NOW() WHERE lover_r=pLOVER_R;
			ELSE
				UPDATE tbl_user_relation SET user_i_v=pUSER_I, user_nick_v=qViewerNick, user_nick_w=qWalkerNick, last_update=NOW() WHERE lover_r=pLOVER_R;
			END IF;
			SET result = 'AcceptRelationSuccess';
		END IF;
		END;
	END IF;
END BASIC_BLOCK $$ 


#------------------------------------------------------------------------------------------------------------------------------------------------------
# 更改行走者昵称
# CALL proc_update_walker_nick('owCe05DPc7018WqYcIcOv34JvtTY','1','1','789','TestNickNam2222e',@result); SELECT @result;
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS `proc_update_walker_nick` $$
CREATE PROCEDURE `proc_update_walker_nick`(
	IN	pWE_ID				VARCHAR(50),
	IN	pUSER_I				VARCHAR(10),
	IN	pLOVER_R			VARCHAR(10),
	IN	pCHECK_CODE			VARCHAR(10),	#--3位校验码:即acode_file_name末三位
	IN	pWALKER_NICK		VARCHAR(50),
	
	OUT	result				VARCHAR(50) )
BASIC_BLOCK:BEGIN
	DECLARE xCount INT;
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
		ROLLBACK;
		SET result = 'SQLException';
	END;
	
	SELECT COUNT(user_i) INTO xCount FROM tbl_user WHERE user_i=pUSER_I AND we_id=pWE_ID;
	IF (xCount = 0) THEN
		SET result = 'ValidUser';
	ELSE
		UPDATE tbl_user_relation SET user_nick_w=pWALKER_NICK, last_update=NOW() WHERE lover_r=pLOVER_R AND user_i_v=pUSER_I AND RIGHT(acode_file_name,3)=pCHECK_CODE;
		SET result = 'UpdateWalkerNickSuccess';
	END IF;
END BASIC_BLOCK $$ 


#------------------------------------------------------------------------------------------------------------------------------------------------------
# 更改观察者昵称
# CALL proc_update_viewer_nick('owCe05DPc7018WqYcIcOv34JvtTY','1','1','789','TestNickNam3333e',@result); SELECT @result;
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS `proc_update_viewer_nick` $$
CREATE PROCEDURE `proc_update_viewer_nick`(
	IN	pWE_ID				VARCHAR(50),
	IN	pUSER_I				VARCHAR(10),
	IN	pLOVER_R			VARCHAR(10),
	IN	pCHECK_CODE			VARCHAR(10),	#--3位校验码:即acode_file_name末三位
	IN	pVIEWER_NICK		VARCHAR(50),
	
	OUT	result				VARCHAR(50) )
BASIC_BLOCK:BEGIN
	DECLARE xCount INT;
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
		ROLLBACK;
		SET result = 'SQLException';
	END;
	
	SELECT COUNT(user_i) INTO xCount FROM tbl_user WHERE user_i=pUSER_I AND we_id=pWE_ID;
	IF (xCount = 0) THEN
		SET result = 'ValidUser';
	ELSE
		UPDATE tbl_user_relation SET user_nick_v=pVIEWER_NICK, last_update=NOW() WHERE lover_r=pLOVER_R AND user_i_w=pUSER_I AND RIGHT(acode_file_name,3)=pCHECK_CODE;
		SET result = 'UpdateViewerNickSuccess';
	END IF;
END BASIC_BLOCK $$ 


#------------------------------------------------------------------------------------------------------------------------------------------------------
# 设置轨迹共享的截止时间
# CALL proc_update_viewer_expire('owCe05DPc7018WqYcIcOv34JvtTY','1','1','2021-06-30 10:30','TestNickNam3333e',@rExpireTime,@result); SELECT @result,@rExpireTime;
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS `proc_update_viewer_expire` $$
CREATE PROCEDURE `proc_update_viewer_expire`(
	IN	pWE_ID				VARCHAR(50),
	IN	pUSER_I				VARCHAR(10),
	IN	pLOVER_R			VARCHAR(10),
	IN	pCHECK_CODE			VARCHAR(10),	#--3位校验码:即acode_file_name末三位
	IN	pVIEWER_EXPIRE		VARCHAR(20),
	
	OUT rExpireTime			VARCHAR(20),
	OUT	result				VARCHAR(50) )
BASIC_BLOCK:BEGIN
	DECLARE xCount, xUserI INT;
	DECLARE maxExpireTime VARCHAR(20);
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
		ROLLBACK;
		SET result = 'SQLException';
	END;
	
	SELECT COUNT(user_i) INTO xCount FROM tbl_user WHERE user_i=pUSER_I AND we_id=pWE_ID;
	IF (xCount = 0) THEN
		SET rExpireTime = '2001-01-01 01:02:01';
		SET result = 'ValidUser';
	ELSE
		UPDATE tbl_user_relation SET dt_end=pVIEWER_EXPIRE, last_update=NOW() WHERE lover_r=pLOVER_R AND user_i_w=pUSER_I AND RIGHT(acode_file_name,3)=pCHECK_CODE;
		SELECT user_i_w INTO xUserI FROM tbl_user_relation WHERE lover_r=pLOVER_R;
		SELECT MAX(dt_end) INTO maxExpireTime FROM tbl_user_relation WHERE user_i_w = xUserI;
		UPDATE tbl_user SET expire_time=maxExpireTime WHERE user_i = xUserI;
		SET rExpireTime = maxExpireTime;
		SET result = 'UpdateViewerExpireSuccess';
	END IF;
END BASIC_BLOCK $$ 


#------------------------------------------------------------------------------------------------------------------------------------------------------
# 新增轨迹点-注意分区
# CALL proc_geo_record_new('owCe05DPc7018WqYcIcOv34JvtTY','1','ud','112.22','35.456','30',@result); SELECT @result;
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS `proc_geo_record_new` $$
CREATE PROCEDURE `proc_geo_record_new`(
	IN	pWE_ID				VARCHAR(50),
	IN	pUSER_I				VARCHAR(10),
	IN	pSPLIT_TAG			VARCHAR(10),
	IN	pLONGITUDE			VARCHAR(17),
	IN	pLATITUDE			VARCHAR(17),
	IN	pDURATION			VARCHAR(10),
	
	OUT	result				VARCHAR(50) )
BASIC_BLOCK:BEGIN
	DECLARE xCount, xUserI INT;
	DECLARE maxExpireTime VARCHAR(20);
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
		ROLLBACK;
		SET result = 'SQLException';
	END;
	
	SELECT COUNT(user_i) INTO xCount FROM tbl_user WHERE user_i=pUSER_I AND we_id=pWE_ID;
	SELECT xCount;
	IF (xCount = 0) THEN
		SET result = 'ValidUser';
	ELSE
		SET @InsertSQL = CONCAT( 'INSERT INTO trn_geo_record_', pSPLIT_TAG, '(user_i,longitude,latitude,duration,input_date)VALUES(', pUSER_I, ',\'', pLONGITUDE, '\',\'', pLATITUDE, '\',', pDURATION, ',NOW());' );
		PREPARE stmt_insert_table FROM @InsertSQL;
		EXECUTE stmt_insert_table;
		
		UPDATE tbl_user SET cur_longitude=pLONGITUDE, cur_latitude=pLATITUDE, effect_time=NOW() WHERE user_i = pUSER_I;
		SET result = 'GeoRecordNewSuccess';
	END IF;
END BASIC_BLOCK $$ 



DELIMITER ;
