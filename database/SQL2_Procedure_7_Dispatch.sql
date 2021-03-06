DELIMITER $$


/************************************************************************************************************************
 * 新增运输计划
 * CALL proc_trans_plan_new('PlanK','2021-10-27','10001','TimeLevel','NeRecycle','1000','Name1','Address1','Link1','Win1','Remark1','Name2','Address2','Link2','Win2','Remark2','1.11','2.22','3.33','4.44',10002,'XYZABC',@rID,@result); SELECT @rID,@result;
 * SELECT plan_k,plan_date,obj_p,time_level,ne_recycle,trans_l,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,qty_w,qty_v,qty_meter,qty_meter_r,user_a,cloud_id,input_date FROM trn_trans_plan WHERE trans_p=@rID;
 ************************************************************************************************************************/
DROP PROCEDURE IF EXISTS `proc_trans_plan_new` $$
CREATE PROCEDURE `proc_trans_plan_new`(
	IN	pPLAN_K					VARCHAR(20),
	IN	pPLAN_DATE				VARCHAR(10),
	IN	pOBJ_P					VARCHAR(10),
	IN	pTIME_LEVEL				VARCHAR(20),	
	IN	pNE_RECYCLE				VARCHAR(20),
	IN	pTRANS_L				VARCHAR(10),
	IN	pNE_ZH1					VARCHAR(20),
	IN	pADDRESS_1				VARCHAR(50),
	IN	pLINKMAN_1				VARCHAR(50),
	IN	pWINDOW_1				VARCHAR(20),
	IN	pREMARK_1				VARCHAR(50),	
	IN	pNE_ZH2					VARCHAR(20),
	IN	pADDRESS_2				VARCHAR(50),
	IN	pLINKMAN_2				VARCHAR(50),
	IN	pWINDOW_2				VARCHAR(20),
	IN	pREMARK_2				VARCHAR(50),
	IN	pQTY_W					VARCHAR(10),
	IN	pQTY_V					VARCHAR(10),
	IN	pQTY_METER				VARCHAR(10),
	IN	pQTY_METER_R			VARCHAR(10),
	IN	pDISPT_REMARK			VARCHAR(50),
	IN	pUSER_A					VARCHAR(10),
	IN	pCLOUD_ID				VARCHAR(20),
	
	OUT r_id					VARCHAR(10),
	OUT	result					VARCHAR(50) )
BASIC_BLOCK:BEGIN
	DECLARE qRouteZh, qUserZh VARCHAR(20);
	
	DECLARE EXIT HANDLER FOR SQLWARNING, NOT FOUND, SQLEXCEPTION BEGIN
		SET result = 'SQLException';
	END;
	
	SELECT route_zh INTO qRouteZh FROM tbl_trans_line WHERE trans_l = pTRANS_L;
	START TRANSACTION;
	INSERT INTO trn_trans_plan(plan_k,plan_date,obj_p,time_level,ne_recycle,trans_l,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,qty_w,qty_v,qty_meter,qty_meter_r,route_zh,user_a,cloud_id,input_date,last_update)
	VALUES(pPLAN_K,pPLAN_DATE,pOBJ_P,pTIME_LEVEL,pNE_RECYCLE,pTRANS_L,pNE_ZH1,pADDRESS_1,pLINKMAN_1,pWINDOW_1,pREMARK_1,pNE_ZH2,pADDRESS_2,pLINKMAN_2,pWINDOW_2,pREMARK_2,pQTY_W,pQTY_V,pQTY_METER,pQTY_METER_R,qRouteZh,pUSER_A,pCLOUD_ID,NOW(),NOW());
	
	SET r_id = LAST_INSERT_ID();
	IF (pPLAN_K = '其它运输') THEN
		SELECT ne_zh INTO qUserZh FROM tbl_user_account WHERE user_a = pUSER_A;
		UPDATE trn_trans_plan SET dispatch_remark=CONCAT('&nbsp;',pDISPT_REMARK), user_zh_d=qUserZh, input_date_d=NOW() WHERE trans_p=r_id;
	END IF;
	
	COMMIT;
	SET result = 'NewSuccess';
END BASIC_BLOCK $$


/************************************************************************************************************************
 * 修改运输计划
 * CALL proc_trans_plan_update(@rID,'PlanK2','2021-10-22','10002','TimeLevel','NeRecycle','1000','Name1','Address1','Link1','Win1','Remark1','Name2','Address2','Link2','Win2','Remark2','1.11','2.22','3.33','4.44',10002,'XYZABC',@result); SELECT @result;
 * SELECT plan_k,plan_date,obj_p,time_level,ne_recycle,trans_l,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,qty_w,qty_v,qty_meter,qty_meter_r,user_a,cloud_id,input_date,last_update FROM trn_trans_plan WHERE trans_p=@rID;
 ************************************************************************************************************************/
DROP PROCEDURE IF EXISTS `proc_trans_plan_update` $$
CREATE PROCEDURE `proc_trans_plan_update`(
	IN	pTRANS_P				VARCHAR(10),
	IN	pPLAN_K					VARCHAR(20),
	IN	pPLAN_DATE				VARCHAR(10),
	IN	pOBJ_P					VARCHAR(10),
	IN	pTIME_LEVEL				VARCHAR(20),	
	IN	pNE_RECYCLE				VARCHAR(20),
	IN	pTRANS_L				VARCHAR(10),
	IN	pNE_ZH1					VARCHAR(20),
	IN	pADDRESS_1				VARCHAR(50),
	IN	pLINKMAN_1				VARCHAR(50),
	IN	pWINDOW_1				VARCHAR(20),
	IN	pREMARK_1				VARCHAR(50),	
	IN	pNE_ZH2					VARCHAR(20),
	IN	pADDRESS_2				VARCHAR(50),
	IN	pLINKMAN_2				VARCHAR(50),
	IN	pWINDOW_2				VARCHAR(20),
	IN	pREMARK_2				VARCHAR(50),
	IN	pQTY_W					VARCHAR(10),
	IN	pQTY_V					VARCHAR(10),
	IN	pQTY_METER				VARCHAR(10),
	IN	pQTY_METER_R			VARCHAR(10),
	IN	pDISPT_REMARK			VARCHAR(50),
	IN	pUSER_A					VARCHAR(10),
	IN	pCLOUD_ID				VARCHAR(20),

	OUT	result					VARCHAR(50) )
BASIC_BLOCK:BEGIN
	DECLARE qUserA INTEGER DEFAULT 0;
	DECLARE qCloudID, qDispatchRemark, qWhRemark VARCHAR(50);
	DECLARE qRouteZh VARCHAR(20);
	
	DECLARE EXIT HANDLER FOR SQLWARNING, NOT FOUND, SQLEXCEPTION BEGIN
		SET result = 'SQLException';
	END;
	
	SELECT user_a, cloud_id, dispatch_remark, wh_remark INTO qUserA, qCloudID, qDispatchRemark, qWhRemark FROM trn_trans_plan WHERE trans_p=pTRANS_P;
	IF ((qUserA <> pUSER_A) OR (qCloudID <> pCLOUD_ID)) THEN
		SET result = 'Invalid';
		LEAVE BASIC_BLOCK;
	END IF;
	IF ( (qDispatchRemark <> '') OR (qWhRemark <> '') ) THEN  
		SET result = 'InUse';
		LEAVE BASIC_BLOCK;
	END IF;
	
	SELECT route_zh INTO qRouteZh FROM tbl_trans_line WHERE trans_l = pTRANS_L;
	START TRANSACTION;
	UPDATE trn_trans_plan SET
		plan_k=pPLAN_K, plan_date=pPLAN_DATE, obj_p=pOBJ_P, time_level=pTIME_LEVEL, ne_recycle=pNE_RECYCLE, trans_l=pTRANS_L,
		ne_zh1=pNE_ZH1, address_1=pADDRESS_1, linkman_1=pLINKMAN_1, window_1=pWINDOW_1, remark_1=pREMARK_1, ne_zh2=pNE_ZH2, address_2=pADDRESS_2, linkman_2=pLINKMAN_2, window_2=pWINDOW_2, remark_2=pREMARK_2, 
		qty_w=pQTY_W, qty_v=pQTY_V, qty_meter=pQTY_METER, qty_meter_r=pQTY_METER_R, dispatch_remark=pDISPT_REMARK, route_zh=qRouteZh, last_update=NOW()
	WHERE trans_p = pTRANS_P;
	
	COMMIT;
	SET result = 'UpdateSuccess';
END BASIC_BLOCK $$


/************************************************************************************************************************
 * 删除运输计划
 * CALL proc_trans_plan_delete(@rID,10002,'XYZABC',@result); SELECT @result;
  ************************************************************************************************************************/
DROP PROCEDURE IF EXISTS `proc_trans_plan_delete` $$
CREATE PROCEDURE `proc_trans_plan_delete`(
	IN	pTRANS_P				VARCHAR(10),
	IN	pUSER_A					VARCHAR(10),
	IN	pCLOUD_ID				VARCHAR(20),

	OUT	result					VARCHAR(50) )
BASIC_BLOCK:BEGIN
	DECLARE qUserA, qPassDays INTEGER DEFAULT 0;
	DECLARE qCloudID, qDispatchRemark, qWhRemark VARCHAR(50);
	DECLARE qPlanK VARCHAR(20);
	
	DECLARE EXIT HANDLER FOR SQLWARNING, NOT FOUND, SQLEXCEPTION BEGIN
		SET result = 'SQLException';
	END;
	
	SELECT user_a, cloud_id, plan_k, dispatch_remark, wh_remark, DATEDIFF(NOW(),input_date) INTO qUserA, qCloudID, qPlanK, qDispatchRemark, qWhRemark, qPassDays FROM trn_trans_plan WHERE trans_p=pTRANS_P;
	IF ((pUSER_A <> qUserA ) OR (pCLOUD_ID <> qCloudID)) THEN
		SET result = 'Invalid';
		LEAVE BASIC_BLOCK;
	END IF;
	
	IF ( ((qPlanK<>'其它运输') AND (qDispatchRemark<>'')) OR (qWhRemark <> '') OR (qPassDays>=3) ) THEN		
		SET result = 'InUse';
		LEAVE BASIC_BLOCK;
	END IF;
		
	START TRANSACTION;
	DELETE FROM trn_trans_plan WHERE trans_p=pTRANS_P;
	
	COMMIT;
	SET result = 'DeleteSuccess';
END BASIC_BLOCK $$


/************************************************************************************************************************
 * 新增车次
 * CALL proc_dispatch_new('First','2021-10-27','10001','CL198X','1222','ZS-18117259195','111','1TO2','123-234&22-11&','REMARK',10011,'XYZABC',@rID,@result); SELECT @rID,@result;
 * SELECT depart_date,truck,plate_number,driver,tel_driver,sub_driver,remark,user_a,input_date,cloud_id,input_date FROM trn_dispatch WHERE dispt=@rID;
 ************************************************************************************************************************/
DROP PROCEDURE IF EXISTS `proc_dispatch_new` $$
CREATE PROCEDURE `proc_dispatch_new`(
	IN	pDISPT_SERIAL			VARCHAR(20),
	IN	pDEPART_DATE			VARCHAR(10),
	IN	pTRUCK					VARCHAR(10),
	IN	pPLATE_NUMBER			VARCHAR(10),
	IN	pDRIVER					VARCHAR(10),
	IN	pTEL_DRIVER				VARCHAR(30),
	IN	pSUB_DRIVER				VARCHAR(10),
	IN	pTRANS_MODE				VARCHAR(20),
	IN	pTRANS_PLANS			VARCHAR(50),	
	IN	pREMARK					VARCHAR(50),
	IN	pUSER_A					VARCHAR(10),
	IN	pCLOUD_ID				VARCHAR(20),
	
	OUT r_id					VARCHAR(10),
	OUT	result					VARCHAR(50) )
BASIC_BLOCK:BEGIN
	DECLARE pos_index, xDispt, xCount INTEGER DEFAULT 1;
	DECLARE xTemp, xTempG, xTransP, xPlanSerial, xUserName VARCHAR(50);
	
	DECLARE EXIT HANDLER FOR SQLWARNING, NOT FOUND, SQLEXCEPTION BEGIN
		SET result = 'SQLException';
	END;
	
	SELECT COUNT(dispt) INTO xCount FROM trn_dispatch WHERE depart_date=pDEPART_DATE AND truck=pTRUCK; 
	IF ( pDISPT_SERIAL-xCount != 1 ) THEN
		SET result = 'Overflow';
		LEAVE BASIC_BLOCK;
	END IF;
	SELECT ne_zh INTO xUserName FROM tbl_user_account WHERE user_a = pUSER_A;

	START TRANSACTION;
	INSERT INTO trn_dispatch(dispt_serial,depart_date,truck,plate_number,driver,tel_driver,sub_driver,trans_mode,remark,user_a,input_date,cloud_id)VALUES(pDISPT_SERIAL,pDEPART_DATE,pTRUCK,pPLATE_NUMBER,pDRIVER,pTEL_DRIVER,pSUB_DRIVER,pTRANS_MODE,pREMARK,pUSER_A,NOW(),pCLOUD_ID);
	SET xDispt = LAST_INSERT_ID();
	
	REPEAT
		SET xTemp = SUBSTRING_INDEX(pTRANS_PLANS,'&',pos_index);
		SET xTempG = SUBSTRING_INDEX(xTemp,'&',-1);
		SET xTransP = SUBSTRING_INDEX(xTempG,'-',1);
		SET xPlanSerial = SUBSTRING_INDEX(xTempG,'-',-1);
		
		INSERT INTO trn_dispatch_record(dispt,p_serial,trans_p,trans_l,if_main)VALUES(xDispt,xPlanSerial,xTransP,0,'Y'); 
		UPDATE trn_trans_plan SET 
			dispatch_remark=CONCAT(dispatch_remark, IF(LENGTH(dispatch_remark)>1,'<br/>&nbsp;','&nbsp;'), '[', pDISPT_SERIAL, ']', pPLATE_NUMBER, ' ', pTEL_DRIVER, ';' ), 
			user_zh_d=xUserName, input_date_d=NOW() 
		WHERE trans_p=xTransP;
		SET pos_index = pos_index + 1;
	UNTIL ( LENGTH(xTemp)+1 = LENGTH(pTRANS_PLANS) ) 
	END REPEAT;
	IF ( pos_index > 2 ) THEN
		SELECT pos_index;
		#--设置trn_dispatch_record中非计费线路的标识为N
	END IF;
	
	COMMIT;
	SET r_id = xDispt;
	SET result = 'NewSuccess';
END BASIC_BLOCK $$


/************************************************************************************************************************
 * 删除车次
 * CALL proc_dispatch_delete(@rID,'10002','XYZABC',@result); SELECT @rID,@result;
 ************************************************************************************************************************/
DROP PROCEDURE IF EXISTS `proc_dispatch_delete` $$
CREATE PROCEDURE `proc_dispatch_delete`(
	IN	pDISPT					VARCHAR(10),
	IN	pUSER_A					VARCHAR(10),
	IN	pCLOUD_ID				VARCHAR(20),
	
	OUT	result					VARCHAR(50) )
BASIC_BLOCK:BEGIN
	DECLARE qUserA, qPassDays INTEGER DEFAULT 0;
	DECLARE qCloudID, qWhRemark, xWhRemark VARCHAR(50);
	
	DECLARE EXIT HANDLER FOR SQLWARNING, NOT FOUND, SQLEXCEPTION BEGIN
		SET result = 'SQLException';
	END;
	
	SELECT user_a, cloud_id, wh_remark_g, DATEDIFF(NOW(),input_date) INTO qUserA, qCloudID, qWhRemark, qPassDays FROM trn_dispatch WHERE dispt=pDISPT;
	IF ((pUSER_A <> qUserA ) OR (pCLOUD_ID <> qCloudID)) THEN
		SET result = 'Invalid';
		LEAVE BASIC_BLOCK;
	END IF;
	IF ( (qWhRemark <> '') OR ( qPassDays>=3) ) THEN
		SET result = 'InUse';
		LEAVE BASIC_BLOCK;
	END IF;
	
	START TRANSACTION;
	SELECT CONCAT('&nbsp;', '[', dispt_serial, ']', plate_number, ' ', tel_driver, ';') INTO xWhRemark FROM trn_dispatch WHERE dispt = pDISPT;
	UPDATE trn_dispatch_record r JOIN trn_trans_plan p ON r.trans_p=p.trans_p
		SET p.dispatch_remark=REPLACE(REPLACE(p.dispatch_remark, CONCAT('<br/>', xWhRemark), ''), xWhRemark, '')
	WHERE r.dispt = pDISPT;
	
	DELETE FROM trn_dispatch WHERE dispt = pDISPT;
	DELETE FROM trn_dispatch_record WHERE dispt = pDISPT;
	
	COMMIT;
	SET result = 'DeleteSuccess';
END BASIC_BLOCK $$


/************************************************************************************************************************
 * 仓库回报
 * CALL proc_ware_retrun_dispatch('13','3','R11','10002','XYZABC',@result); SELECT @result;
 ************************************************************************************************************************/
DROP PROCEDURE IF EXISTS `proc_ware_retrun_dispatch` $$
CREATE PROCEDURE `proc_ware_retrun_dispatch`(
	IN	pDISPT					VARCHAR(10),
	IN	pTRANS_P				VARCHAR(10),
	IN	pREMARK					VARCHAR(30),
	IN	pUSER_A					VARCHAR(10),
	IN	pCLOUD_ID				VARCHAR(20),
	
	OUT	result					VARCHAR(50) )
BASIC_BLOCK:BEGIN
	DECLARE qPassDays INTEGER DEFAULT 0;
	DECLARE qCloudID, xRemarkWG, xUserName, xUserNameWG VARCHAR(200);
	DECLARE xInputDateWG DATETIME;
	
	DECLARE EXIT HANDLER FOR SQLWARNING, NOT FOUND, SQLEXCEPTION BEGIN
		SET result = 'SQLException';
	END;
	
	SELECT cloud_id, DATEDIFF(NOW(),input_date) INTO qCloudID, qPassDays FROM trn_dispatch WHERE dispt=pDISPT;
	IF ( pCLOUD_ID <> qCloudID ) THEN
		SET result = 'Invalid';
		LEAVE BASIC_BLOCK;
	END IF;
	IF ( qPassDays >= 3 ) THEN
		SET result = 'InUse';
		LEAVE BASIC_BLOCK;
	END IF;
	
	SELECT ne_zh INTO xUserName FROM tbl_user_account WHERE user_a = pUSER_A;
	
	START TRANSACTION;
	UPDATE trn_trans_plan SET wh_remark = pREMARK, user_zh_w=xUserName, input_date_w=NOW() WHERE trans_p = pTRANS_P;
	SELECT GROUP_CONCAT(p.wh_remark SEPARATOR ' '), GROUP_CONCAT(p.user_zh_w), MAX(input_date_w) INTO xRemarkWG, xUserNameWG, xInputDateWG FROM trn_dispatch d JOIN trn_dispatch_record r ON d.dispt=r.dispt JOIN trn_trans_plan p ON r.trans_p=p.trans_p WHERE d.dispt = pDISPT ORDER BY dispt_r ASC;
	UPDATE trn_dispatch SET wh_remark_g=xRemarkWG, user_zh_wg=xUserNameWG, input_date_wg=xInputDateWG WHERE dispt = pDISPT;

	COMMIT;
	SET result = 'ReturnSuccess';
END BASIC_BLOCK $$


/************************************************************************************************************************
 * 回报车辆闲置情况
 * CALL proc_truck_idle_feedback('Idle','1000','沪DK7348','10002','张三-18125673567','2021-11-16','2021-11-18','Remark','10002','XYZABC',@result); SELECT @result;
 * CALL proc_truck_idle_delete('1','10002','XYZABC',@result); SELECT @result;
 ************************************************************************************************************************/
DROP PROCEDURE IF EXISTS `proc_truck_idle_feedback` $$
CREATE PROCEDURE `proc_truck_idle_feedback`(
	IN	pIDLE_K					VARCHAR(10),
	IN	pTRUCK					VARCHAR(10),
	IN	pPLATE_NUMBER			VARCHAR(10),
	IN	pDRIVER					VARCHAR(10),
	IN	pTEL_DRIVER				VARCHAR(30),
	IN	pSTART_DATE				VARCHAR(10),
	IN	pEND_DATE				VARCHAR(10),
	IN	pREMARK					VARCHAR(50),
	IN	pUSER_A					VARCHAR(10),
	IN	pCUR_COMPANY			VARCHAR(10),
	IN	pCLOUD_ID				VARCHAR(20),
	
	OUT	result					VARCHAR(50) )
BASIC_BLOCK:BEGIN
	DECLARE xCount INTEGER DEFAULT 0;
	DECLARE qCloudID VARCHAR(20);
	
	DECLARE EXIT HANDLER FOR SQLWARNING, NOT FOUND, SQLEXCEPTION BEGIN
		SET result = 'SQLException';
	END;
	
	IF ( pTRUCK <> '0' ) THEN
		SELECT cloud_id INTO qCloudID FROM tbl_truck WHERE truck = pTRUCK;
		IF ( pCLOUD_ID <> qCloudID ) THEN
			SET result = 'Invalid';
			LEAVE BASIC_BLOCK;
		END IF;
		SELECT COUNT(truck_i) INTO xCount FROM tbl_truck_idle WHERE truck=pTRUCK AND ((start_date<=pSTART_DATE AND end_date>=pSTART_DATE) OR (start_date<=pEND_DATE AND end_date>=pEND_DATE) ) LIMIT 1;
	ELSE 
		SELECT COUNT(truck_i) INTO xCount FROM tbl_truck_idle WHERE plate_number=pPLATE_NUMBER AND cur_company=pCUR_COMPANY AND ((start_date<=pSTART_DATE AND end_date>=pSTART_DATE) OR (start_date<=pEND_DATE AND end_date>=pEND_DATE) ) LIMIT 1;
	END IF;
	
	IF ( xCount > 0 ) THEN
		SET result = 'InUse';
		LEAVE BASIC_BLOCK;
	END IF;
		
	START TRANSACTION;
	INSERT INTO tbl_truck_idle(idle_k,cur_company,truck,plate_number,driver,tel_driver,start_date,end_date,remark,user_a,input_date,cloud_id)VALUES(pIDLE_K,pCUR_COMPANY,pTRUCK,pPLATE_NUMBER,pDRIVER,pTEL_DRIVER,pSTART_DATE,pEND_DATE,pREMARK,pUSER_A,NOW(),pCLOUD_ID);
	
	COMMIT;
	SET result = 'NewSuccess';
END BASIC_BLOCK $$


DROP PROCEDURE IF EXISTS `proc_truck_idle_delete` $$
CREATE PROCEDURE `proc_truck_idle_delete`(
	IN	pTRK_IDLE				VARCHAR(10),
	IN	pUSER_A					VARCHAR(10),
	IN	pCLOUD_ID				VARCHAR(20),
	
	OUT	result					VARCHAR(50) )
BASIC_BLOCK:BEGIN
	DECLARE qUserA INTEGER DEFAULT 0;
	DECLARE qCloudID VARCHAR(20);
	
	DECLARE EXIT HANDLER FOR SQLWARNING, NOT FOUND, SQLEXCEPTION BEGIN
		SET result = 'SQLException';
	END;
	
	SELECT user_a, cloud_id INTO qUserA, qCloudID FROM tbl_truck_idle WHERE truck_i = pTRK_IDLE;
	IF ( (qCloudID<>pCLOUD_ID) OR (qUserA<>pUSER_A) ) THEN
		SET result = 'Invalid';
		LEAVE BASIC_BLOCK;
	END IF;

	
	START TRANSACTION;
	DELETE FROM tbl_truck_idle WHERE truck_i = pTRK_IDLE;

	COMMIT;
	SET result = 'DeleteSuccess';
END BASIC_BLOCK $$



DELIMITER ;

