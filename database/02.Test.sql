DROP TABLE IF EXISTS `mst_company`;				#分公司表
CREATE TABLE mst_company(
	company					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	ne_zh					VARCHAR(20) NOT NULL,
	sort_tag				VARCHAR(20) NOT NULL DEFAULT 'ABC',
	input_date				DATETIME NOT NULL,
	last_update				DATETIME NOT NULL,
	sys_flg					ENUM('Normal','Disable') NOT NULL DEFAULT 'Normal',
	cloud_id				VARCHAR(20) NOT NULL
);


DROP TABLE IF EXISTS `mst_part`;				#部门表
CREATE TABLE mst_part(
	part					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	ne_zh					VARCHAR(20) NOT NULL,
	sort_tag				VARCHAR(20) NOT NULL DEFAULT 'ABC',
	input_date				DATETIME NOT NULL,
	last_update				DATETIME NOT NULL,
	sys_flg					ENUM('Normal','Disable') NOT NULL DEFAULT 'Normal',
	cloud_id				VARCHAR(20) NOT NULL
);


DROP TABLE IF EXISTS `mst_post`;				#岗位表
CREATE TABLE mst_post(
	post					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	ne_zh					VARCHAR(20) NOT NULL,
	sort_tag				VARCHAR(20) NOT NULL DEFAULT 'ABC',
	input_date				DATETIME NOT NULL,
	last_update				DATETIME NOT NULL,
	sys_flg					ENUM('Normal','Disable') NOT NULL DEFAULT 'Normal',
	cloud_id				VARCHAR(20) NOT NULL
);

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