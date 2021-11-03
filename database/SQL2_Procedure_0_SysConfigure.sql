DELIMITER $$


#------------------------------------------------------------------------------------------------------------------------------------------------------
# Function getNextSplitTag
# SELECT getNextSplitTag('ud');
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP FUNCTION IF EXISTS `getNextSplitTag` $$
CREATE FUNCTION `getNextSplitTag`(
	pSPLIT_TAG	VARCHAR(10) ) RETURNS VARCHAR(10) CHARSET utf8
BEGIN
	DECLARE jLetterSerial1, jLetterSerial2 VARCHAR(50);
	DECLARE xChar1, xChar2, xTableTag VARCHAR(10);
	DECLARE xPos1, xPos2 INT;
	
	SET jLetterSerial1 = 'uabcdefghijklmnopqrstvwxyz';
	SET jLetterSerial2 = 'd0123456789abcefghijklmnopqrstuvwxyz';
	
	SET xChar1 = LEFT(pSPLIT_TAG,1);
	SET xPos1 = LOCATE( xChar1, jLetterSerial1 );
	SET xPos2 = LOCATE( RIGHT(pSPLIT_TAG,1), jLetterSerial2 );
	IF ( xPos2 >= 36 ) THEN
		SET xPos2 = 0;
		SET xPos1 = xPos1 + 1;
		SET xChar1 = SUBSTR( jLetterSerial1, xpos1, 1 );
		SET xChar2 = LEFT( jLetterSerial2, 1 );
	ELSE
		SET xChar2 = SUBSTR( jLetterSerial2, xpos2+1, 1 );
	END IF;
	
	SET xTableTag = CONCAT( xChar1, xChar2 );
	RETURN xTableTag;
END $$


#------------------------------------------------------------------------------------------------------------------------
# 创建用户
# CALL proc_user_new('open_id','TestNickName','url',@rID,@rExpireTime,@rSplitTag,@result); SELECT @rID,@result;
#------------------------------------------------------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS `proc_user_new` $$
CREATE PROCEDURE `proc_user_new`(
	IN	pOPEN_ID			VARCHAR(50),
	IN	pNICK_NAME			VARCHAR(50),
	IN	pAVATAR_URL			VARCHAR(300),
	
	OUT	r_id				VARCHAR(10),
	OUT r_expire_time		VARCHAR(20),
	OUT r_split_tag			VARCHAR(10),
	OUT	result				VARCHAR(50) )
BASIC_BLOCK:BEGIN
	DECLARE qUserID, qSplitTag VARCHAR(10);
	DECLARE qNickName VARCHAR(50);
	
	DECLARE CONTINUE HANDLER FOR NOT FOUND BEGIN
		SET qUserID = NULL;
	END;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
		ROLLBACK;
		SET result = 'SQLException';
	END;
	
	SELECT user_i, we_nick, expire_time, split_tag INTO qUserID, qNickName, r_expire_time, qSplitTag FROM tbl_user WHERE we_id = pOPEN_ID;
	IF ( qUserID IS NULL ) THEN
		SELECT split_tag INTO qSplitTag FROM stc_split_table WHERE tb_kind = 'GEO' AND sys_flg = 'Recent';
		INSERT INTO tbl_user(we_id,we_nick,avatar_url,split_tag,input_date,last_update)VALUES(pOPEN_ID,pNICK_NAME,pAVATAR_URL,qSplitTag,NOW(),NOW());
		SET r_id = LAST_INSERT_ID();
		SET r_expire_time = '2021-01-01 12:30:00';
	ELSE
		UPDATE tbl_user SET we_nick=pNICK_NAME, avatar_url=pAVATAR_URL WHERE user_i = qUserID;
		SET r_id = qUserID;
	END IF;
	
	SET r_split_tag = qSplitTag;
	SET result = 'NewSuccess';
END BASIC_BLOCK $$ 

DELIMITER ;
