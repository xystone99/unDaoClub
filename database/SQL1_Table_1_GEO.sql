USE unDaoDB

#------------------------------------------------------------------------------------------------------------------------------------------------------
#	GEO
#	tbl_user_relation  trn_geo_record_XX
#	SELECT * FROM trn_geo_record_XX PARTITION (PW1);
#	ALTER TABLE trn_geo_record_XX TRUNCATE PARTITION PW1;
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `tbl_user_relation`;		#关注用户表
CREATE TABLE tbl_user_relation(
	lover_r					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#关注ID
	user_i_v				INT UNSIGNED NOT NULL DEFAULT 0,			#观察者
	user_i_w				INT UNSIGNED NOT NULL,						#行走者
	user_nick_v				VARCHAR(50) NOT NULL DEFAULT '',			#默认同昵称,允许行走者修改.
	user_nick_w				VARCHAR(50) NOT NULL,						#默认同昵称,允许观察者修改.
	dt_start				DATETIME NOT NULL,							#普通关注开始时间
	dt_end					DATETIME NOT NULL,							#普通关注结束时间
	dt_start_h				DATETIME NOT NULL,							#高频关注开始时间
	dt_end_h				DATETIME NOT NULL,							#高频关注结束时间(此时间<=普通关注结束时间)
	acode_file_name			VARCHAR(20) NOT NULL DEFAULT '',			#对应的二维码图片名字(System.currentTimeMillis())
	input_date				DATETIME NOT NULL,							#登记时间
	last_update				DATETIME NOT NULL							#最后更新时间
);
ALTER TABLE tbl_user_relation ADD INDEX idx_user_v(user_i_v);
ALTER TABLE tbl_user_relation ADD INDEX idx_user_w(user_i_w);


DROP TABLE IF EXISTS `trn_geo_record_ud`;			#GEO记录表
CREATE TABLE trn_geo_record_ud(
	user_i					INT UNSIGNED NOT NULL,						#用户ID
	longitude				CHAR(17) NOT NULL DEFAULT '',				#经度
	latitude 				CHAR(17) NOT NULL DEFAULT '',				#纬度
	duration				SMALLINT NOT NULL DEFAULT 0,				#持续时长(分钟数)
	input_date				DATETIME NOT NULL							#登记时间
);
ALTER TABLE trn_geo_record_ud ADD UNIQUE INDEX idx_geo_record_ud(user_i, input_date);
ALTER TABLE trn_geo_record_ud PARTITION BY RANGE( DAYOFWEEK(input_date) ) ( 
	PARTITION PW1 VALUES LESS THAN (2),	PARTITION PW2 VALUES LESS THAN (3), PARTITION PW3 VALUES LESS THAN (4), 
	PARTITION PW4 VALUES LESS THAN (5), PARTITION PW5 VALUES LESS THAN (6), PARTITION PW6 VALUES LESS THAN (7), PARTITION PW7 VALUES LESS THAN (8)
);
INSERT INTO stc_split_table(tb_name,tb_kind,split_tag,cnt_record,calc_date,region_s,input_date,last_update,sys_flg)VALUES('trn_geo_record_ud','GEO','ud',0,NOW(),1,NOW(),NOW(),'Recent');

