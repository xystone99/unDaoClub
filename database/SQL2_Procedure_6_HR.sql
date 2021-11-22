DELIMITER $$


/************************************************************************************************************************
 * SYSTEM: Login Check
 * CALL proc_employee_login('XYZABC','sysAdmin','3258',@rUserA,@rRole,@rName,@rAstrictLevel,@rCurCompany,@rCompanies,@rSysFlg,@result); SELECT @rUserA,@rRole,@rName,@rAstrictLevel,@rCurCompany,@rCompanies,@rSysFlg,@result;
 ************************************************************************************************************************/
DROP PROCEDURE IF EXISTS `proc_employee_login` $$
CREATE PROCEDURE `proc_employee_login`(
	IN	pLOGIN_NAME			VARCHAR(20),
	IN	pLOGIN_PWD			VARCHAR(20),
	IN	pCLOUD_ID			VARCHAR(20),
	
	OUT r_USER_A			VARCHAR(20),
	OUT r_NAME				VARCHAR(20),
	OUT r_ROLE				VARCHAR(20),
	OUT	r_ASTRICT_LEVEL		VARCHAR(20),
	OUT r_CUR_COMPANY		VARCHAR(20),
	OUT r_COMPANYS			VARCHAR(200),
	OUT r_SYS_FLG			VARCHAR(20),
	
	OUT result				VARCHAR(50)	)
BASIC_BLOCK:BEGIN
	DECLARE xPwd VARCHAR(50);
	DECLARE xCanLogin VARCHAR(50);
	
	DECLARE CONTINUE HANDLER FOR NOT FOUND BEGIN
		SET r_ROLE = NULL;
	END;
	DECLARE EXIT HANDLER FOR SQLWARNING, SQLEXCEPTION BEGIN
		ROLLBACK;
		SET result = 'SQLException';
	END;
	
	SELECT user_a,role,login_pwd,can_login,ne_zh,astrict_level,cur_company,available_companys,sys_flg INTO r_USER_A,r_ROLE,xPwd,xCanLogin,r_NAME,r_ASTRICT_LEVEL,r_CUR_COMPANY,r_COMPANYS,r_SYS_FLG FROM tbl_user_account WHERE cloud_id=pCLOUD_ID AND sys_flg IN('Normal','Inner') AND login_name=pLOGIN_NAME; 
	IF ( r_ROLE IS NULL ) THEN
		SET result = 'NotExists';
	ELSEIF ( xPwd <> MD5(pLOGIN_PWD) ) THEN
		SET result = 'PasswordError';
	ELSE
		IF ( xCanLogin = 'N' ) THEN
			SET result = 'CantLogin';
		ELSE
			SET result = 'LoginSuccess';
		END IF;
	END IF;
END BASIC_BLOCK $$


/************************************************************************************************************************
 * Password Update
 * CALL proc_password_update('JY','10001','369258','Administrator','369255',@result); SELECT @result;
 ************************************************************************************************************************/
DROP PROCEDURE IF EXISTS `proc_password_update` $$
CREATE PROCEDURE `proc_password_update`(
	IN	pLOGIN_PWD			VARCHAR(20),	
	IN	pLOGIN_NAME_NEW		VARCHAR(20),
	IN	pLOGIN_PWD_NEW		VARCHAR(20),
	IN	pUSER_A				VARCHAR(20),
	IN	pCLOUD_ID			VARCHAR(20),
	
	OUT	result				VARCHAR(50) )
BASIC_BLOCK:BEGIN
	DECLARE xPwd, xOldLoginName VARCHAR(50);
	
	DECLARE CONTINUE HANDLER FOR NOT FOUND BEGIN
		SET xPwd = NULL;
	END;
	DECLARE EXIT HANDLER FOR SQLWARNING, SQLEXCEPTION BEGIN
		ROLLBACK;
		SET result = 'SQLException';
	END;	
	
	SELECT login_name,login_pwd INTO xOldLoginName,xPwd FROM tbl_user_account WHERE cloud_id=pCLOUD_ID AND user_a=pUSER_A AND login_pwd = MD5(pLOGIN_PWD);
	IF ( (xPwd IS NULL) || (xPwd <> MD5(pLOGIN_PWD)) ) THEN
		SET result = 'OldError';
		LEAVE BASIC_BLOCK;
	END IF;
	
	START TRANSACTION;
	IF (pLOGIN_NAME_NEW <> 'sysAdmin') THEN
		UPDATE tbl_user_account SET login_name=pLOGIN_NAME_NEW, login_pwd=MD5(pLOGIN_PWD_NEW) WHERE user_a = pUSER_A;
	ELSE
		UPDATE tbl_user_account SET login_pwd=MD5(pLOGIN_PWD_NEW) WHERE user_a = pUSER_A;
	END IF;

	COMMIT;
	SET result = 'UpdateSuccess';
END BASIC_BLOCK $$


/************************************************************************************************************************
 * SECURITY - tbl_role stc_role_astricts
 ************************************************************************************************************************/
DROP PROCEDURE IF EXISTS `proc_role_new` $$
CREATE PROCEDURE `proc_role_new`(
	IN	pNE_ZH					VARCHAR(20),
	IN	pSORT_TAG				VARCHAR(20),
	IN	pHREF_INDEX				VARCHAR(50),
	IN	pASTRICTS				VARCHAR(300),		#--格式：1021-1022-1023-
	IN	pREMARK					VARCHAR(50),
	IN	pCLOUD_ID				VARCHAR(20),
	
	OUT r_id					VARCHAR(10),
	OUT	result					VARCHAR(50) )
BASIC_BLOCK:BEGIN
	DECLARE xCount, xID, pos_index INTEGER DEFAULT 1;
	DECLARE xTemp VARCHAR(300);
	DECLARE xAstrict VARCHAR(10);
	
	DECLARE EXIT HANDLER FOR SQLWARNING, NOT FOUND, SQLEXCEPTION BEGIN
		ROLLBACK;
		SET result = 'SQLException';
	END;

	SELECT COUNT(role) INTO xCount FROM tbl_role WHERE cloud_id=pCLOUD_ID AND ne_zh = pNE_ZH;
	IF ( xCount > 0 ) THEN
		SET result = "Exists";
		LEAVE BASIC_BLOCK;
	END IF;
	
	START TRANSACTION;
	INSERT INTO tbl_role(cloud_id,ne_zh,sort_tag,href_index,remark,input_date)VALUES(pCLOUD_ID,pNE_ZH,pSORT_TAG,pHREF_INDEX,pREMARK,NOW());
	SET xID = LAST_INSERT_ID();
	
	REPEAT
		SET xTemp = SUBSTRING_INDEX(pASTRICTS,'-',pos_index);
		SET xAstrict = SUBSTRING_INDEX(xTemp,'-',-1);
		INSERT INTO stc_role_astricts( role, astrict )VALUES( xID, xAstrict );
		SET pos_index = pos_index + 1;
	UNTIL ( LENGTH(pASTRICTS)-LENGTH(xTemp)<=1 ) 
	END REPEAT;
	
	COMMIT;
	SET r_id = xID;
	SET result = 'NewSuccess';
END BASIC_BLOCK $$


DROP PROCEDURE IF EXISTS `proc_role_update` $$
CREATE PROCEDURE `proc_role_update`(
	IN	pROLE					VARCHAR(10),
	IN	pNE_ZH					VARCHAR(20),
	IN	pSORT_TAG				VARCHAR(20),
	IN	pHREF_INDEX				VARCHAR(50),
	IN	pASTRICTS				VARCHAR(300),
	IN	pREMARK					VARCHAR(50),
	IN	pCLOUD_ID				VARCHAR(20),
	
	OUT	result					VARCHAR(50) )
BASIC_BLOCK:BEGIN
	DECLARE xCount, pos_index INTEGER DEFAULT 1;
	DECLARE xTemp VARCHAR(300);
	DECLARE xAstrict VARCHAR(10);
	
	DECLARE EXIT HANDLER FOR SQLWARNING, NOT FOUND, SQLEXCEPTION BEGIN
		ROLLBACK;
		SET result = 'SQLException';
	END;

	SELECT COUNT(role) INTO xCount FROM tbl_role WHERE role <> pROLE AND cloud_id=pCLOUD_ID AND ne_zh = pNE_ZH;
	IF ( xCount > 0 ) THEN
		SET result = "Exists";
		LEAVE BASIC_BLOCK;
	END IF;

	START TRANSACTION;
	UPDATE tbl_role SET ne_zh=pNE_ZH, sort_tag=pSORT_TAG, href_index=pHREF_INDEX, remark=pREMARK WHERE role = pROLE;
	DELETE FROM stc_role_astricts WHERE role = pROLE;

	REPEAT
		SET xTemp = SUBSTRING_INDEX(pASTRICTS,'-',pos_index);
		SET xAstrict = SUBSTRING_INDEX(xTemp,'-',-1);
		INSERT INTO stc_role_astricts( role, astrict )VALUES( pROLE, xAstrict );
		SET pos_index = pos_index + 1;
	UNTIL ( LENGTH(pASTRICTS)-LENGTH(xTemp)<=1 ) 
	END REPEAT;

	COMMIT;
	SET result = 'UpdateSuccess';
END BASIC_BLOCK $$


DROP PROCEDURE IF EXISTS `proc_role_delete` $$
CREATE PROCEDURE `proc_role_delete`(	
	IN	pROLE					VARCHAR(10),
	IN	pCLOUD_ID				VARCHAR(20),
	
	OUT	result					VARCHAR(50) )
BASIC_BLOCK:BEGIN
	DECLARE xCount INT;
	DECLARE EXIT HANDLER FOR SQLWARNING, NOT FOUND, SQLEXCEPTION BEGIN
		ROLLBACK;
		SET result = 'SQLException';
	END;

	SELECT COUNT(user_a) INTO xCount FROM tbl_user_account WHERE cloud_id=pCLOUD_ID AND role = pROLE;
	IF ( xCount > 0 ) THEN
		SET result = "InUse";
		LEAVE BASIC_BLOCK;
	END IF;

	START TRANSACTION;
	DELETE FROM tbl_role WHERE role = pROLE;
	DELETE FROM stc_role_astricts WHERE role = pROLE;
	
	COMMIT;
	SET result = 'DeleteSuccess';
END BASIC_BLOCK $$



DELIMITER ;
