DELIMITER $$


#------------------------------------------------------------------------------------------------------------------------
# 生成员工编号可变部分
# 员工编号格式：公司代码(职能部门代码)+职位代码+入职序号。
# 入职序号一旦生成，不再变化。
# SELECT getPrefixOfJobID(1001,'SH',2001);
#------------------------------------------------------------------------------------------------------------------------
DROP FUNCTION IF EXISTS `getPrefixOfJobID` $$
CREATE FUNCTION `getPrefixOfJobID`(
	inPart		VARCHAR(10),
	inCompany	VARCHAR(10),
	inPost		VARCHAR(10) ) RETURNS VARCHAR(10) CHARSET utf8
BEGIN
	DECLARE preJobID VARCHAR(10);
	
	IF (inPart = '1001')     THEN SET preJobID = 'CS';
	ELSEIF (inPart = '1002') THEN SET preJobID = 'FB';
	ELSEIF (inPart = '1003') THEN SET preJobID = 'HR';
	ELSEIF (inPart = '1004') THEN SET preJobID = 'ST';
	ELSEIF (inPart = '1005') THEN SET preJobID = 'FA';
	ELSEIF (inPart = '4001') THEN SET preJobID = 'NY';
	ELSEIF (inPart = '5001') THEN SET preJobID = 'NYWL';
	ELSE					 
		SET preJobID = inCompany;
	END IF;
	
	IF (inPost = '8052')     THEN SET preJobID = CONCAT(preJobID, 'GL');
	ELSEIF (inPost = '8101') THEN SET preJobID = CONCAT(preJobID, 'FM');
	ELSEIF (inPost = '8201') THEN SET preJobID = CONCAT(preJobID, 'M');
	ELSEIF (inPost = '8301') THEN SET preJobID = CONCAT(preJobID, 'SM');
	ELSEIF (inPost = '8401') THEN SET preJobID = CONCAT(preJobID, 'D');
	ELSEIF (inPost = '8501') THEN SET preJobID = CONCAT(preJobID, 'D');
	ELSEIF (inPost = '8601') THEN SET preJobID = CONCAT(preJobID, 'SD');
	ELSEIF (inPost = '9001') THEN SET preJobID = CONCAT(preJobID, '');
	ELSE
		SET preJobID = CONCAT(preJobID, 'T');
	END IF;
	
	RETURN preJobID;
END$$


#------------------------------------------------------------------------------------------------------------------------
# 社保计算
# SHOW VARIABLES LIKE 'log_bin_trust_function_creators';
# SET GLOBAL log_bin_trust_function_creators=1;
#
# 编号/排序标记/社保基数/公积金基数/养老/医疗/失业/工伤/生育/中文名称
# { "SH5",  "A", "3850", "3500", "0.20", "0.08", "0.10", "0.02", "0.01",  "0.005", "0.0165",  "0", "0.01",  "0", "上海五险(能运)" },	//去掉小数
# { "BZ5",  "A", "3850", "3500", "0.20", "0.08", "0.10", "0.02", "0.01",  "0.005", "0.0165",  "0", "0.01",  "0", "上海五险(本质)" },	//去掉小数
# { "DL5A", "A", "3470", "1530", "0.18", "0.08", "0.08", "0.02", "0.005", "0.005", "0.014",   "0", "0.012", "0", "大连五险(农业)" },	//精确到分
# { "DL5B", "A", "3470", "1530", "0.18", "0.08", "0.08", "0.02", "0.005", "0.005", "0.014",   "0", "0.012", "0", "大连五险(城镇)" },	//精确到分
# { "DL2",  "A", "3470", "1530", "0",    "0",    "0.02", "0",    "0",     "0",     "0.014",   "0", "0",     "0", "大连两险" },			//精确到分
# { "TJ5",  "A", "2966", "1950", "0.19", "0.08", "0.11", "0.02", "0.01",  "0.005", "0.011",   "0", "0.005", "0", "天津五险" },			//精确到分(公积金到元)
# { "TJ3",  "A", "2472", "1950", "0.19", "0.08", "0.08", "0",    "0",     "0",     "0.011",   "0", "0",     "0", "天津三险" },			//精确到分(公积金到元)
# { "YT5",  "A", "2910", "1710", "0.18", "0.08", "0.07", "0.02", "0.01",  "0.005", "0.0132",  "0", "0.005", "0", "烟台五险" }			//精确到分
#------------------------------------------------------------------------------------------------------------------------
DROP FUNCTION IF EXISTS `calcFundsC` $$		#公积金公司承担
CREATE FUNCTION `calcFundsC`(
	socialPlace		VARCHAR(10),
	socialBasic		VARCHAR(10) ) RETURNS DECIMAL(7,2)
BEGIN
	IF (socialPlace = 'SH5') THEN		RETURN FLOOR(socialBasic*0.07);
	ELSEIF (socialPlace = 'BZ5')  THEN	RETURN FLOOR(socialBasic*0.07);
	ELSEIF (socialPlace = 'DL5A') THEN	RETURN ROUND(socialBasic*0.11,2);
	ELSEIF (socialPlace = 'DL5B') THEN	RETURN ROUND(socialBasic*0.11,2);
	ELSEIF (socialPlace = 'DL2')  THEN	RETURN ROUND(socialBasic*0.11,2);
	ELSEIF (socialPlace = 'TJ5')  THEN	RETURN ROUND(socialBasic*0.05);
	ELSEIF (socialPlace = 'TJ3')  THEN	RETURN ROUND(socialBasic*0.05);
	ELSEIF (socialPlace = 'YT5')  THEN	RETURN ROUND(socialBasic*0.06,2);
	ELSEIF (socialPlace = 'SZ5')  THEN	RETURN 0;
	END IF;
	RETURN 0;
END$$


DROP FUNCTION IF EXISTS `calcFundsE` $$		#公积金个人承担
CREATE FUNCTION `calcFundsE`(
	socialPlace		VARCHAR(10),
	socialBasic		VARCHAR(10) ) RETURNS DECIMAL(7,2)
BEGIN
	IF (socialPlace = 'SH5') THEN		RETURN FLOOR(socialBasic*0.07);
	ELSEIF (socialPlace = 'BZ5')  THEN	RETURN FLOOR(socialBasic*0.07);
	ELSEIF (socialPlace = 'DL5A') THEN	RETURN ROUND(socialBasic*0.07,2);
	ELSEIF (socialPlace = 'DL5B') THEN	RETURN ROUND(socialBasic*0.07,2);
	ELSEIF (socialPlace = 'DL2')  THEN	RETURN ROUND(socialBasic*0.07,2);
	ELSEIF (socialPlace = 'TJ5')  THEN	RETURN ROUND(socialBasic*0.05);
	ELSEIF (socialPlace = 'TJ3')  THEN	RETURN ROUND(socialBasic*0.05);
	ELSEIF (socialPlace = 'YT5')  THEN	RETURN ROUND(socialBasic*0.06,2);
	ELSEIF (socialPlace = 'SZ5')  THEN	RETURN 0;
	END IF;
	RETURN 0;
END$$


DROP FUNCTION IF EXISTS `calcYangC` $$		#养老公司承担
CREATE FUNCTION `calcYangC`(
	socialPlace		VARCHAR(10),
	socialBasic		VARCHAR(10) ) RETURNS DECIMAL(7,2)
BEGIN
	IF (socialPlace = 'SH5') THEN		RETURN ROUND(socialBasic*0.2, 1);
	ELSEIF (socialPlace = 'BZ5')  THEN	RETURN ROUND(socialBasic*0.2, 1);
	ELSEIF (socialPlace = 'DL5A') THEN	RETURN ROUND(socialBasic*0.18,2);
	ELSEIF (socialPlace = 'DL5B') THEN	RETURN ROUND(socialBasic*0.18,2);
	ELSEIF (socialPlace = 'DL2')  THEN	RETURN 0;
	ELSEIF (socialPlace = 'TJ5')  THEN	RETURN ROUND(socialBasic*0.19,2);
	ELSEIF (socialPlace = 'TJ3')  THEN	RETURN ROUND(socialBasic*0.19,2);
	ELSEIF (socialPlace = 'YT5')  THEN	RETURN ROUND(socialBasic*0.18,2);
	ELSEIF (socialPlace = 'SZ5')  THEN	RETURN 536.82;
	END IF;
	RETURN 0;
END$$


DROP FUNCTION IF EXISTS `calcYangE` $$		#养老个人承担
CREATE FUNCTION `calcYangE`(
	socialPlace		VARCHAR(10),
	socialBasic		VARCHAR(10) ) RETURNS DECIMAL(7,2)
BEGIN
	IF (socialPlace = 'SH5') THEN		RETURN ROUND(socialBasic*0.08, 1);
	ELSEIF (socialPlace = 'BZ5')  THEN	RETURN ROUND(socialBasic*0.08, 1);
	ELSEIF (socialPlace = 'DL5A') THEN	RETURN ROUND(socialBasic*0.08, 2);
	ELSEIF (socialPlace = 'DL5B') THEN	RETURN ROUND(socialBasic*0.08, 2);
	ELSEIF (socialPlace = 'DL2')  THEN	RETURN 0;
	ELSEIF (socialPlace = 'TJ5')  THEN	RETURN ROUND(socialBasic*0.08, 2);
	ELSEIF (socialPlace = 'TJ3')  THEN	RETURN ROUND(socialBasic*0.08, 2);
	ELSEIF (socialPlace = 'YT5')  THEN	RETURN ROUND(socialBasic*0.08, 2);
	ELSEIF (socialPlace = 'SZ5')  THEN	RETURN 0;
	END IF;
	RETURN 0;
END$$


DROP FUNCTION IF EXISTS `calcLiaoC` $$		#医疗公司承担
CREATE FUNCTION `calcLiaoC`(
	socialPlace		VARCHAR(10),
	socialBasic		VARCHAR(10) ) RETURNS DECIMAL(7,2)
BEGIN
	IF (socialPlace = 'SH5') THEN		RETURN ROUND(socialBasic*0.1, 1);
	ELSEIF (socialPlace = 'BZ5')  THEN	RETURN ROUND(socialBasic*0.1, 1);
	ELSEIF (socialPlace = 'DL5A') THEN	RETURN ROUND(socialBasic*0.08,2);
	ELSEIF (socialPlace = 'DL5B') THEN	RETURN ROUND(socialBasic*0.08,2);
	ELSEIF (socialPlace = 'DL2')  THEN	RETURN ROUND(socialBasic*0.02,2);
	ELSEIF (socialPlace = 'TJ5')  THEN	RETURN ROUND(socialBasic*0.11,2);
	ELSEIF (socialPlace = 'TJ3')  THEN	RETURN ROUND(socialBasic*0.08,2);
	ELSEIF (socialPlace = 'YT5')  THEN	RETURN ROUND(socialBasic*0.07,2);
	ELSEIF (socialPlace = 'SZ5')  THEN	RETURN 37.14;
	END IF;
	RETURN 0;
END$$


DROP FUNCTION IF EXISTS `calcLiaoE` $$		#医疗个人承担
CREATE FUNCTION `calcLiaoE`(
	socialPlace		VARCHAR(10),
	socialBasic		VARCHAR(10) ) RETURNS DECIMAL(7,2)
BEGIN
	IF (socialPlace = 'SH5') THEN		RETURN ROUND(socialBasic*0.02, 1);
	ELSEIF (socialPlace = 'BZ5')  THEN	RETURN ROUND(socialBasic*0.02, 1);
	ELSEIF (socialPlace = 'DL5A') THEN	RETURN ROUND(socialBasic*0.02, 2);
	ELSEIF (socialPlace = 'DL5B') THEN	RETURN ROUND(socialBasic*0.02, 2);
	ELSEIF (socialPlace = 'DL2')  THEN	RETURN 0;
	ELSEIF (socialPlace = 'TJ5')  THEN	RETURN ROUND(socialBasic*0.02, 2);
	ELSEIF (socialPlace = 'TJ3')  THEN	RETURN 0;
	ELSEIF (socialPlace = 'YT5')  THEN	RETURN ROUND(socialBasic*0.02, 2);
	ELSEIF (socialPlace = 'SZ5')  THEN	RETURN 0;
	END IF;
	RETURN 0;
END$$


DROP FUNCTION IF EXISTS `calcShiC` $$		#失业公司承担
CREATE FUNCTION `calcShiC`(
	socialPlace		VARCHAR(10),
	socialBasic		VARCHAR(10) ) RETURNS DECIMAL(7,2)
BEGIN
	IF (socialPlace = 'SH5') THEN		RETURN ROUND(socialBasic*0.01, 1);
	ELSEIF (socialPlace = 'BZ5')  THEN	RETURN ROUND(socialBasic*0.01, 1);
	ELSEIF (socialPlace = 'DL5A') THEN	RETURN ROUND(socialBasic*0.005,2);
	ELSEIF (socialPlace = 'DL5B') THEN	RETURN ROUND(socialBasic*0.005,2);
	ELSEIF (socialPlace = 'DL2')  THEN	RETURN 0;
	ELSEIF (socialPlace = 'TJ5')  THEN	RETURN ROUND(socialBasic*0.01, 2);
	ELSEIF (socialPlace = 'TJ3')  THEN	RETURN 0;
#	ELSEIF (socialPlace = 'YT5')  THEN	RETURN ROUND(socialBasic*0.007,2);
	ELSEIF (socialPlace = 'SZ5')  THEN	RETURN 30.45;
	END IF;
	RETURN 0;
END$$


DROP FUNCTION IF EXISTS `calcShiE` $$		#失业个人承担
CREATE FUNCTION `calcShiE`(
	socialPlace		VARCHAR(10),
	socialBasic		VARCHAR(10) ) RETURNS DECIMAL(7,2)
BEGIN
	IF (socialPlace = 'SH5') THEN		RETURN ROUND(socialBasic*0.005, 1);
	ELSEIF (socialPlace = 'BZ5')  THEN	RETURN ROUND(socialBasic*0.005, 1);
	ELSEIF (socialPlace = 'DL5A') THEN	RETURN 0;
	ELSEIF (socialPlace = 'DL5B') THEN	RETURN ROUND(socialBasic*0.005, 2);
	ELSEIF (socialPlace = 'DL2')  THEN	RETURN 0;
	ELSEIF (socialPlace = 'TJ5')  THEN	RETURN ROUND(socialBasic*0.005, 2);
	ELSEIF (socialPlace = 'TJ3')  THEN	RETURN 0;
#	ELSEIF (socialPlace = 'YT5')  THEN	RETURN ROUND(socialBasic*0.003, 2);
	ELSEIF (socialPlace = 'SZ5')  THEN	RETURN 0;
	END IF;
	RETURN 0;
END$$


DROP FUNCTION IF EXISTS `calcGongC` $$		#工伤公司承担(个人承担0)
CREATE FUNCTION `calcGongC`(
	socialPlace		VARCHAR(10),
	socialBasic		VARCHAR(10) ) RETURNS DECIMAL(7,2)
BEGIN
	IF (socialPlace = 'SH5') THEN		RETURN ROUND(socialBasic*0.0132,1);
	ELSEIF (socialPlace = 'BZ5')  THEN	RETURN ROUND(socialBasic*0.0132,1);
	ELSEIF (socialPlace = 'DL5A') THEN	RETURN ROUND(socialBasic*0.014, 2)+9;
	ELSEIF (socialPlace = 'DL5B') THEN	RETURN ROUND(socialBasic*0.014, 2)+9;
	ELSEIF (socialPlace = 'DL2')  THEN	RETURN ROUND(socialBasic*0.014, 2)+9;
	ELSEIF (socialPlace = 'TJ5')  THEN	RETURN ROUND(socialBasic*0.011, 2);
	ELSEIF (socialPlace = 'TJ3')  THEN	RETURN ROUND(socialBasic*0.011, 2);
	ELSEIF (socialPlace = 'YT5')  THEN	RETURN ROUND(socialBasic*0.0132,2);
	ELSEIF (socialPlace = 'SZ5')  THEN	RETURN 5.68;
	END IF;
	RETURN 0;
END$$


DROP FUNCTION IF EXISTS `calcShengC` $$		#生育公司承担(个人承担0)
CREATE FUNCTION `calcShengC`(
	socialPlace		VARCHAR(10),
	socialBasic		VARCHAR(10) ) RETURNS DECIMAL(7,2)
BEGIN
	IF (socialPlace = 'SH5') THEN		RETURN ROUND(socialBasic*0.01, 1);
	ELSEIF (socialPlace = 'BZ5')  THEN	RETURN ROUND(socialBasic*0.01, 1);
	ELSEIF (socialPlace = 'DL5A') THEN	RETURN ROUND(socialBasic*0.012,2);
	ELSEIF (socialPlace = 'DL5B') THEN	RETURN ROUND(socialBasic*0.012,2);
	ELSEIF (socialPlace = 'DL2')  THEN	RETURN 0;
	ELSEIF (socialPlace = 'TJ5')  THEN	RETURN ROUND(socialBasic*0.005,2);
	ELSEIF (socialPlace = 'TJ3')  THEN	RETURN 0;
	ELSEIF (socialPlace = 'YT5')  THEN	RETURN ROUND(socialBasic*0.005, 2);
	ELSEIF (socialPlace = 'SZ5')  THEN	RETURN 10.15;
	END IF;
	RETURN 0;
END$$


#------------------------------------------------------------------------------------------------------------------------
# 工龄工资计算
# 最高不能超过200元
# SELECT calcAge('201612','2013-10-27');
# SELECT calcAgePay('201612','2013-10-27',30);
#------------------------------------------------------------------------------------------------------------------------
DROP FUNCTION IF EXISTS `calcAge` $$			#工龄
CREATE FUNCTION `calcAge`(
	wageMonth		VARCHAR(10),
	joinDate		VARCHAR(10) ) RETURNS INT
BEGIN
	DECLARE firstDate VARCHAR(10);
	DECLARE countYear INT;
	
	SET firstDate = CONCAT(LEFT(wageMonth,4),'-',RIGHT(wageMonth,2),'-1');
	SET countYear = TIMESTAMPDIFF(YEAR,joinDate,firstDate);

	RETURN countYear;
END$$


DROP FUNCTION IF EXISTS `calcAgePay` $$			#工龄工资
CREATE FUNCTION `calcAgePay`(
	wageMonth		VARCHAR(10),
	joinDate		VARCHAR(10),
	agePayYear		VARCHAR(10) ) RETURNS DECIMAL(7,2)
BEGIN
	DECLARE firstDate VARCHAR(10);
	DECLARE countYear INT;
	DECLARE totalPay DECIMAL;
	
	SET firstDate = CONCAT(LEFT(wageMonth,4),'-',RIGHT(wageMonth,2),'-1');
	SET countYear = TIMESTAMPDIFF(YEAR,joinDate,firstDate);
	SET totalPay = agePayYear*countYear;
	
	IF ( totalPay > 200 ) THEN
		return 200;
	END IF;
	RETURN totalPay;
END$$


#------------------------------------------------------------------------------------------------------------------------
# 个税计算
# SELECT calcWageTax('BD',8000);
#------------------------------------------------------------------------------------------------------------------------
DROP FUNCTION IF EXISTS `calcWageTax` $$		#个税计算
CREATE FUNCTION `calcWageTax`(
	workCompany		VARCHAR(10),
	wage			VARCHAR(10) ) RETURNS DECIMAL(7,2)
BEGIN
	DECLARE calcWage DECIMAL(7,2);
	DECLARE rTAX DECIMAL(7,2) DEFAULT 0;
	
	IF ( (workCompany='DL') || (workCompany='CC') || (workCompany='YT') || (workCompany='TJ') || (workCompany='BD') ) THEN
		SET rTAX = 0;
	ELSE
		SET calcWage = wage - 3500;
		IF ( calcWage <= 0 ) THEN 								SET rTAX = 0;
		ELSEIF ( (calcWage>0 )    AND (calcWage<=1500)  ) THEN	SET rTAX = ROUND(calcWage*0.03,2);
		ELSEIF ( (calcWage>1500 ) AND (calcWage<=4500)  ) THEN	SET rTAX = ROUND(calcWage*0.10-105,  2);
		ELSEIF ( (calcWage>4500 ) AND (calcWage<=9000)  ) THEN	SET rTAX = ROUND(calcWage*0.20-555,  2);
		ELSEIF ( (calcWage>9000 ) AND (calcWage<=35000) ) THEN	SET rTAX = ROUND(calcWage*0.25-1005, 2);
		ELSEIF ( (calcWage>35000) AND (calcWage<=55000) ) THEN	SET rTAX = ROUND(calcWage*0.30-2755, 2);
		ELSEIF ( (calcWage>55000) AND (calcWage<=80000) ) THEN	SET rTAX = ROUND(calcWage*0.35-5505, 2);
		ELSEIF ( calcWage > 80000 ) 					  THEN	SET rTAX = ROUND(calcWage*0.45-13505,2);	
		END IF;
	END IF;
	RETURN rTAX;
END$$


#------------------------------------------------------------------------------------------------------------------------
# 考勤扣款计算
# 分两部分扣：一部分计入考勤扣款(当地最低工资标准计算)、剩余部分从职务工资中扣除
# SELECT calcAbsenceCut('NC','2.35'); 
#------------------------------------------------------------------------------------------------------------------------
DROP FUNCTION IF EXISTS `calcAbsenceCut` $$		#考勤扣款计算
CREATE FUNCTION `calcAbsenceCut`(
	curCompany		VARCHAR(10),
	curWage			VARCHAR(10),	#剔除了提成工资
	payDuty			VARCHAR(10),	#职务工资
	qtyAbsence		VARCHAR(10) ) RETURNS DECIMAL(7,2)
BEGIN
	DECLARE rCutAbsence DECIMAL(7,2);
	
	IF ( payDuty <= 0 ) THEN
		SET rCutAbsence = ROUND((curWage/21.75)*qtyAbsence,2);
	ELSE
		IF ( curCompany = 'ZB' ) THEN 		SET rCutAbsence = ROUND((2190/21.75)*qtyAbsence,2);	#--总部
		ELSEIF ( curCompany = 'SH' ) THEN	SET rCutAbsence = ROUND((2190/21.75)*qtyAbsence,2);	#--上海
		ELSEIF ( curCompany = 'TJ' ) THEN	SET rCutAbsence = ROUND((1950/21.75)*qtyAbsence,2);	#--天津
		ELSEIF ( curCompany = 'DL' ) THEN	SET rCutAbsence = ROUND((1530/21.75)*qtyAbsence,2);	#--大连
		ELSEIF ( curCompany = 'CC' ) THEN	SET rCutAbsence = ROUND((1300/21.75)*qtyAbsence,2);	#--长春
		ELSEIF ( curCompany = 'BD' ) THEN	SET rCutAbsence = ROUND((1650/21.75)*qtyAbsence,2);	#--保定
		ELSEIF ( curCompany = 'YT' ) THEN	SET rCutAbsence = ROUND((1710/21.75)*qtyAbsence,2);	#--烟台
		ELSEIF ( curCompany = 'WH' ) THEN	SET rCutAbsence = ROUND((1350/21.75)*qtyAbsence,2);	#--芜湖
		ELSEIF ( curCompany = 'CZ' ) THEN	SET rCutAbsence = ROUND((1770/21.75)*qtyAbsence,2);	#--常州
		ELSEIF ( curCompany = 'NB' ) THEN	SET rCutAbsence = ROUND((1660/21.75)*qtyAbsence,2);	#--宁波
		ELSEIF ( curCompany = 'NC' ) THEN	SET rCutAbsence = ROUND((1530/21.75)*qtyAbsence,2);	#--南昌
		ELSE			  					SET rCutAbsence = ROUND((2190/21.75)*qtyAbsence,2);	#--其它
		END IF;
	END IF;
		
	RETURN rCutAbsence;
END$$


#------------------------------------------------------------------------------------------------------------------------
# 外出餐费
# SELECT getMealFee('SH','Breakfast');
#------------------------------------------------------------------------------------------------------------------------
DROP FUNCTION IF EXISTS `getMealFee` $$
CREATE FUNCTION `getMealFee`(
	company			VARCHAR(10),
	meal_k			ENUM('Breakfast','Lunch','Dinner') ) RETURNS DECIMAL(5,2)
BEGIN
	IF ( company = 'SH' ) THEN
		IF ( meal_k = 'Breakfast' )	 THEN	RETURN 5;
		ELSEIF ( meal_k = 'Lunch' )  THEN	RETURN 10;
		ELSEIF ( meal_k = 'Dinner' ) THEN	RETURN 12;
		END IF;
	ELSE
		IF ( meal_k = 'Breakfast' )	 THEN	RETURN 5;
		ELSEIF ( meal_k = 'Lunch' )  THEN	RETURN 8;
		ELSEIF ( meal_k = 'Dinner' ) THEN	RETURN 10;
		END IF;
	END IF;
END$$



DELIMITER ;