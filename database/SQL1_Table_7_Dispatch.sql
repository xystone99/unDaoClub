USE unDaoDB

#------------------------------------------------------------------------------------------------------------------------------------------------------
#	基础表(3)
#	tbl_payment_object  tbl_dispatch_route  tbl_trans_line  tbl_truck
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `tbl_payment_object`;			#收付对象表
CREATE TABLE tbl_payment_object(
	obj_p					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	ne_zh					VARCHAR(50) NOT NULL DEFAULT '',			#全称
	ne_short				VARCHAR(20) NOT NULL DEFAULT '',			#简称
	sort_tag				VARCHAR(20) NOT NULL DEFAULT 'ABC',			#排序标记
	init_spell				VARCHAR(20) NOT NULL DEFAULT '',			#快速查询代码
	kind					VARCHAR(20) NOT NULL DEFAULT '',			#类别
	if_cus					ENUM('N','Y') NOT NULL,						#是否客户?
	if_provider				ENUM('N','Y') NOT NULL,						#是否供应商?
	tel_linkman				VARCHAR(50) NOT NULL DEFAULT '',			#联系人
	address					VARCHAR(50) NOT NULL DEFAULT '',			#详细地址
	remark					VARCHAR(50) NOT NULL DEFAULT '',			#其它说明
	user_r					INT UNSIGNED NOT NULL DEFAULT 0,			#关联用户
	cloud_id				VARCHAR(20) NOT NULL,						#
	sys_flg					ENUM('Normal','Stop','Inner') NOT NULL		#内置,正常,中止,取消
);


DROP TABLE IF EXISTS `tbl_dispatch_route`;			#调度路径表
CREATE TABLE tbl_dispatch_route(
	id						INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#
	route_zh				VARCHAR(20) NOT NULL, 						#路径名称
	sort_tag				VARCHAR(20) NOT NULL DEFAULT 'ABC',			#排序标记
	remark					VARCHAR(50) NOT NULL DEFAULT '',			#其它说明
	cloud_id				VARCHAR(20) NOT NULL						#
);


DROP TABLE IF EXISTS `tbl_trans_line`;				#运输线路表
CREATE TABLE tbl_trans_line(
	trans_l					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#
	line_tag				VARCHAR(50) NOT NULL, 						#格式为name1-name2
	sort_tag				VARCHAR(20) NOT NULL DEFAULT 'ABC',			#排序标记
	obj_p					INT UNSIGNED NOT NULL DEFAULT 0,			#所属客户
	plan_k					VARCHAR(20) NOT NULL,						#计划类型:单程提货/返空提货/单程送货/返空送货/直提直送/往返运输
	time_level				VARCHAR(20) NOT NULL DEFAULT '',			#时效要求等级
	ne_zh1					VARCHAR(20) NOT NULL DEFAULT '',			#发货方名称	
	address_1				VARCHAR(50) NOT NULL DEFAULT '',			#详细地址1
	linkman_1				VARCHAR(50) NOT NULL DEFAULT '',			#联系人1
	window_1				VARCHAR(20) NOT NULL DEFAULT '',			#窗口时间1
	remark_1				VARCHAR(50) NOT NULL DEFAULT '',			#其它说明1(单据、道口、特殊要求等)
	ne_zh2					VARCHAR(20) NOT NULL DEFAULT '',			#收货方名称	
	address_2				VARCHAR(50) NOT NULL DEFAULT '',			#详细地址2
	linkman_2				VARCHAR(50) NOT NULL DEFAULT '',			#联系人2
	window_2				VARCHAR(20) NOT NULL DEFAULT '',			#窗口时间2
	remark_2				VARCHAR(50) NOT NULL DEFAULT '',			#其它说明2(单据、道口、特殊要求等)
	route_zh				VARCHAR(20) NOT NULL DEFAULT '',			#调度路径
	cloud_id				VARCHAR(20) NOT NULL						#
);


DROP TABLE IF EXISTS `tbl_truck`;					#车辆表(挂车另创建表)
CREATE TABLE tbl_truck(
	truck					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#ID,记录行惟一标记
	plate_number			VARCHAR(10) NOT NULL DEFAULT '',			#车牌号
	company					INT UNSIGNED NOT NULL DEFAULT 0,			#所属分公司(所有权)
	cur_company				INT UNSIGNED NOT NULL DEFAULT 0,			#当前分公司(使用权)
	truck_type				VARCHAR(20) NOT NULL DEFAULT '',			#类别:单车,挂车,危险品单车,危险品挂车
	truck_trailer			VARCHAR(10) NOT NULL DEFAULT '',			#对应挂车(不保存ID)
	truck_form				VARCHAR(20) NOT NULL DEFAULT '',			#车型(如厢式牵引车)
	truck_kind				VARCHAR(20) NOT NULL DEFAULT '',			#车型(如9.6/13.5)
	if_attach				ENUM('N','Y') NOT NULL DEFAULT 'N',			#自有/挂靠
	driver					INT UNSIGNED NOT NULL DEFAULT 0,			#司机ID
	tel_driver				VARChAR(50) NOT NULL DEFAULT '',			#驾驶员姓名-电话
	qty_km_c				INT UNSIGNED NOT NULL DEFAULT 0,			#当前里程表
	qty_km_o				INT UNSIGNED NOT NULL DEFAULT 0,			#上次加油时里程
	remark					VARCHAR(50) NOT NULL DEFAULT '',			#其它说明
	cloud_id				VARCHAR(20) NOT NULL,						#
	sys_flg					ENUM('Normal','Scrap','Sold') NOT NULL 		#正常,报废,已出售
);


#------------------------------------------------------------------------------------------------------------------------------------------------------
#	提送指令相关(1)
#	trn_trans_plan
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `trn_trans_plan`;				#运输计划表
CREATE TABLE trn_trans_plan(
	trans_p					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#
	plan_k					VARCHAR(20) NOT NULL,						#计划类型:单程提货/返空提货/单程送货/返空送货/直提直送/往返运输
	plan_date				DATE NOT NULL,								#计划日期
	obj_p					INT UNSIGNED NOT NULL DEFAULT 0,			#所属客户
	time_level				VARCHAR(20) NOT NULL DEFAULT '',			#时效要求等级
	ne_recycle				VARCHAR(20) NOT NULL DEFAULT '',			#返空仓库(可能多个)
	trans_l					INT UNSIGNED NOT NULL DEFAULT 0,			#运输线路
	ne_zh1					VARCHAR(20) NOT NULL DEFAULT '',			#发货方名称	
	address_1				VARCHAR(50) NOT NULL DEFAULT '',			#详细地址1
	linkman_1				VARCHAR(50) NOT NULL DEFAULT '',			#联系人1
	window_1				VARCHAR(20) NOT NULL DEFAULT '',			#窗口时间1
	remark_1				VARCHAR(50) NOT NULL DEFAULT '',			#其它说明1(单据、道口、特殊要求等)
	ne_zh2					VARCHAR(20) NOT NULL DEFAULT '',			#收货方名称	
	address_2				VARCHAR(50) NOT NULL DEFAULT '',			#详细地址2
	linkman_2				VARCHAR(50) NOT NULL DEFAULT '',			#联系人2
	window_2				VARCHAR(20) NOT NULL DEFAULT '',			#窗口时间2
	remark_2				VARCHAR(50) NOT NULL DEFAULT '',			#其它说明2(单据、道口、特殊要求等)
	qty_w					DECIMAL(7,3) NOT NULL DEFAULT 0,			#吨
	qty_v					DECIMAL(7,3) NOT NULL DEFAULT 0,			#方
	qty_meter				DECIMAL(7,3) NOT NULL DEFAULT 0,			#占车米数
	qty_meter_r				DECIMAL(7,3) NOT NULL DEFAULT 0,			#空箱占车米数
	route_zh				VARCHAR(20) NOT NULL DEFAULT '',			#调度路径
	dispatch_remark			VARCHAR(80) NOT NULL DEFAULT '',			#调度回报信息
	user_zh_d				VARCHAR(20) NOT NULL DEFAULT '',			#调度
	input_date_d			DATETIME DEFAULT NULL,						#调度登记时间
	wh_remark				VARCHAR(50) NOT NULL DEFAULT '',			#仓库回报信息
	user_zh_w				VARCHAR(20) NOT NULL DEFAULT '',			#仓库回报人
	input_date_w			DATETIME DEFAULT NULL,						#仓库登记时间
	user_a					INT UNSIGNED NOT NULL,						#录入人
	input_date				DATETIME NOT NULL,							#录入时间
	last_update				DATETIME NOT NULL,							#最后更新时间
	cloud_id				VARCHAR(20) NOT NULL						#
);


#------------------------------------------------------------------------------------------------------------------------------------------------------
#	车次相关(2)
#	trn_dispatch  trn_dispatch_record
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `trn_dispatch`;				#车次主表
CREATE TABLE trn_dispatch(
	dispt					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#
	dispt_serial			VARCHAR(20) NOT NULL,						#自定义车次号
	depart_date				DATE NOT NULL,								#出发日期
	truck					INT UNSIGNED NOT NULL DEFAULT 0,			#车辆ID(为0表示社会车辆)
	plate_number			VARCHAR(10) NOT NULL DEFAULT '',			#车牌号
	driver					INT UNSIGNED NOT NULL DEFAULT 0,			#驾驶员ID(为0表示非内部员工)
	tel_driver				VARChAR(20) NOT NULL DEFAULT '',			#驾驶员姓名-电话
	sub_driver				INT UNSIGNED NOT NULL DEFAULT 0,			#副驾或押运员(为0表示无押运员)
	trans_l					INT UNSIGNED NOT NULL DEFAULT 0,			#运输线路
	stop_zh_f				VARCHAR(20) NOT NULL DEFAULT '',			#始发地
	stop_zh_via				VARCHAR(200) NOT NULL DEFAULT '',			#途径地(最多9个)
	stop_zh_t				VARCHAR(20) NOT NULL DEFAULT '',			#目的地
	remark					VARCHAR(50) NOT NULL DEFAULT '',			#其它说明			
	user_a					INT UNSIGNED NOT NULL,						#调度登记人
	input_date				DATETIME NOT NULL,							#调度登记时间
	wh_remark_g				VARCHAR(200) NOT NULL DEFAULT '',			#仓库说明			
	user_zh_wg				VARCHAR(50) NOT NULL DEFAULT '',			#仓库登记人
	input_date_wg			DATETIME DEFAULT NULL,						#仓库登记时间
	cloud_id				VARCHAR(20) NOT NULL						#
);


DROP TABLE IF EXISTS `trn_dispatch_record`;			#车次从表
CREATE TABLE trn_dispatch_record(
	dispt_r					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#
	dispt					INT UNSIGNED NOT NULL,						#车次ID
	trans_p					INT UNSIGNED NOT NULL,						#运输计划ID
	trans_l					INT UNSIGNED NOT NULL DEFAULT 0,			#运输线路
	if_main					ENUM('Y','N') NOT NULL DEFAULT 'N'
);


#------------------------------------------------------------------------------------------------------------------------------------------------------
#	回报车辆闲置()
#	tbl_truck_idle
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `tbl_truck_idle`;				#车辆闲置表
CREATE TABLE tbl_truck_idle(
	truck_i					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#
	idle_k					ENUM('Idle','Lack') NOT NULl,				#闲置,缺少
	cur_company				INT UNSIGNED NOT NULL DEFAULT 0,			#当前分公司(使用权)
	truck					INT UNSIGNED NOT NULL DEFAULT 0,			#车辆ID(为0表示闲置)
	plate_number			VARCHAR(10) NOT NULL DEFAULT '',			#车牌号/车型
	driver					INT UNSIGNED NOT NULL DEFAULT 0,			#驾驶员ID(为0表示未设置)
	tel_driver				VARChAR(20) NOT NULL DEFAULT '',			#驾驶员姓名-电话
	start_date				DATE NOT NULL,								#开始日期
	end_date				DATE NOT NULL,								#结束日期
	remark					VARCHAR(50) NOT NULL DEFAULT '',			#其它说明			
	user_a					INT UNSIGNED NOT NULL,						#登记人
	input_date				DATETIME NOT NULL,							#登记时间
	cloud_id				VARCHAR(20) NOT NULL						#
);


