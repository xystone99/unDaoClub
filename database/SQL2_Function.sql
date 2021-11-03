DELIMITER $$


#------------------------------------------------------------------------------------------------------------------------
# 获得快速查询代码
# SHOW VARIABLES LIKE 'log_bin_trust_function_creators';
# SET GLOBAL log_bin_trust_function_creators=1;
#------------------------------------------------------------------------------------------------------------------------
DROP FUNCTION IF EXISTS `getInitSpell` $$
CREATE FUNCTION `getInitSpell`(
	in_string VARCHAR(50) ) RETURNS VARCHAR(50) CHARSET utf8
BEGIN
	DECLARE tmp_str VARCHAR(50) CHARSET gbk DEFAULT '';		
	DECLARE tmp_len SMALLINT DEFAULT 0;
	DECLARE tmp_loc SMALLINT DEFAULT 0;							
	DECLARE tmp_char VARCHAR(2) CHARSET gbk DEFAULT '';			
	DECLARE tmp_rs VARCHAR(50)CHARSET gbk DEFAULT '';		
	DECLARE tmp_cc VARCHAR(2) CHARSET gbk DEFAULT '';			

	SET tmp_str = in_string;									
	SET tmp_len = LENGTH(tmp_str);								
	WHILE tmp_len > 0 DO										
		SET tmp_char = LEFT(tmp_str,1);							
		SET tmp_cc = tmp_char;									
		SET tmp_loc=INTERVAL(CONV(HEX(tmp_char),16,10),0xB0A1,0xB0C5,0xB2C1,0xB4EE,0xB6EA,0xB7A2,0xB8C1,0xB9FE,0xBBF7,0xBFA6,0xC0AC,0xC2E8,0xC4C3,0xC5B6,0xC5BE,0xC6DA,0xC8BB,0xC8F6,0xCBFA,0xCDDA ,0xCEF4,0xD1B9,0xD4D1);
		IF (LENGTH(tmp_char)>1 AND tmp_loc>0 AND tmp_loc<24) THEN
			SELECT ELT(tmp_loc,'A','B','C','D','E','F','G','H','J','K','L','M','N','O','P','Q','R','S','T','W','X','Y','Z') INTO tmp_cc;	#获得汉字拼音首字符 	
		END IF;
		SET tmp_rs = CONCAT(tmp_rs,tmp_cc);						
		SET tmp_str = SUBSTRING(tmp_str,2);						
		SET tmp_len = LENGTH(tmp_str);							
	END WHILE;
	RETURN tmp_rs;
END$$


#------------------------------------------------------------------------------------------------------------------------
# 两个日期跨月时,在哪个月份天数多,即返回该月份
# SELECT trendMonth('2017-06-25','2017-07-18');
#------------------------------------------------------------------------------------------------------------------------
DROP FUNCTION IF EXISTS `trendMonth` $$
CREATE FUNCTION `trendMonth`(
	date1			VARCHAR(10),
	date2			VARCHAR(10) ) RETURNS VARCHAR(7) CHARSET utf8
BEGIN
	DECLARE tMonth, tMonth1, tMonth2 VARCHAR(7);
	
	SET tMonth1 = LEFT(date1,7);
	SET tMonth2 = LEFT(date2,7);
	IF (tMonth1 = tMonth2) THEN
		RETURN tMonth1;
	END IF;
	
	IF ( MONTH(date2)-MONTH(date1) >= 2 ) THEN
		RETURN DATE_FORMAT( DATE_ADD(LAST_DAY(date1),INTERVAL 1 DAY), '%Y-%m');
	ELSEIF ( DATEDIFF(LAST_DAY(date1),date1) >= DAY(date2) ) THEN
		RETURN tMonth1;
	ELSE
		RETURN tMonth2;
	END IF;
END$$



DELIMITER ;