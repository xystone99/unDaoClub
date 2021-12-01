USE unDaoDB


#------------------------------------------------------------------------------------------------------------------------------------------------------
#	基础配置表
#	tbl_role  stc_role_astricts  mst_company  mst_part  mst_post
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `tbl_role`;				#角色表
CREATE TABLE tbl_role(
	role					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	ne_zh					VARCHAR(20) NOT NULL,
	sort_tag				VARCHAR(20) NOT NULL DEFAULT 'ABC',
	href_index				VARCHAR(50) NOT NULL,
	remark					VARCHAR(80) NOT NULL DEFAULT '',
	input_date				DATETIME NOT NULL,
	cloud_id				VARCHAR(20) NOT NULL
);


DROP TABLE IF EXISTS `stc_role_astricts`;		#角色权限关系表
CREATE TABLE stc_role_astricts(
	id						INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	role					INT UNSIGNED NOT NULL,
	astrict					VARCHAR(10) NOT NULL
);


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


#------------------------------------------------------------------------------------------------------------------------------------------------------
#	用户账户
#	tbl_user_account
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `tbl_user_account`;		#用户账户表
CREATE TABLE tbl_user_account(
	user_a					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	emp_rely				INT UNSIGNED NOT NULL DEFAULT 0,			#0表示不依赖员工表
	ne_zh					VARCHAR(20) NOT NULL,	
	tel						VARCHAR(20) NOT NULL DEFAULT '',			#手机号码
	init_spell				VARCHAR(20) NOT NULL DEFAULT '',			#快速查询代码
	if_driver				ENUM('N','Y') NOT NULL DEFAULT 'N',			#是否司机
	login_name				VARCHAR(20) NOT NULL DEFAULT '',			#登录名
	login_pwd				VARCHAR(50) NOT NULL DEFAULT '',			#登录密码
	can_login				ENUM('N','Y') NOT NULL DEFAULT 'N',			#是否允许登录
	role					INT UNSIGNED NOT NULL DEFAULT 0,			#角色编号
	astrict_level			TINYINT UNSIGNED NOT NULL DEFAULT 0,		#权限级别:9最高;5一般;0最低.
	cur_company				INT UNSIGNED NOT NULL DEFAULT 0,			#系统操作时默认分公司(company)
	available_companys		VARCHAR(200) NOT NULL DEFAULT '',			#可访问分公司列表(IN函数)(company)
	sys_flg					ENUM('Normal','Inner','Quit') NOT NULL,		#正常,内置用户,离职
	cloud_id				VARCHAR(20) NOT NULL
);
INSERT INTO tbl_user_account(user_a,ne_zh,login_name,login_pwd,can_login,cur_company,available_companys,sys_flg,cloud_id)VALUES(10001,'SysAdministrator','sysAdmin',MD5('369258'),'Y',12001,'11001,11002,11003,11004,11005,11006,12001,12002,12003','Inner','XYZABC');


#------------------------------------------------------------------------------------------------------------------------------------------------------
#	HR
#	tbl_emp
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `tbl_emp`;					#员工表
CREATE TABLE tbl_emp(
	emp						INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	ne_zh					VARCHAR(20) NOT NULL,						
	company					INT UNSIGNED NOT NULL DEFAULT 0,			#部门1(分公司)
	part					INT UNSIGNED NOT NULL DEFAULT 0,			#部门2
	sort_tag				VARCHAR(20) NOT NULL DEFAULT 'ABC',			#排序标记
	init_spell				VARCHAR(20) NOT NULL DEFAULT '',			#快速查询代码
	post					INT UNSIGNED NOT NULL DEFAULT 0,			#岗位
	join_date				DATE NOT NULL DEFAULT '2049-12-31',			#加入日期
	job_id					VARCHAR(20) NOT NULL DEFAULT '',			#工号
	birth					DATE NOT NULL DEFAULT '2049-12-31',			#出生日期
	sex						ENUM('M','F') NOT NULL DEFAULT 'M',			#性别.M-男
	marriage				ENUM('N','Y','Divorced','Bereft') NOT NULL DEFAULT 'Y',			
																		#婚姻状况:未婚,已婚,离异,丧偶
	nation					VARCHAR(20) NOT NULL DEFAULT '',			#民族
	native_place			VARCHAR(20) NOT NULL DEFAULT '',			#籍贯
	id_card					VARCHAR(20) NOT NULL DEFAULT '',			#身份证号
	census_type				VARCHAR(20) NOT NULL DEFAULT '',			#户籍类别
	census_address			VARCHAR(50) NOT NULL DEFAULT '',			#户籍地址
	address					VARCHAR(50) NOT NULL DEFAULT '',			#当前住址
	mobile					VARCHAR(20) NOT NULL DEFAULT '',			#手机号码
	tel_linkman				VARCHAR(50) NOT NULL DEFAULT '',			#紧急联系人,电话
	school					VARCHAR(20) NOT NULL DEFAULT '',			#毕业院校
	major					VARCHAR(20) NOT NULL DEFAULT '',			#专业
	education				VARCHAR(20) NOT NULL DEFAULT '',			#学历
	appraisal_date			DATE NOT NULL DEFAULT '2049-12-31',			#试用期测评日
	probation_end			DATE NOT NULL DEFAULT '2049-12-31',			#试用期结束日期
	contract_start			DATE NOT NULL DEFAULT '2049-12-31',			#最近一份合同开始日期
	contract_end			DATE NOT NULL DEFAULT '2049-12-31',			#最近一份合同结束日期
	remark					VARCHAR(80) NOT NULL DEFAULT '',			#其它说明
	can_eval				ENUM('N','Y') NOT NULL DEFAULT 'N',			#是否具备考核权限
	emp_eval				INT UNSIGNED NOT NULL DEFAULT 0,			#考核人
	pay_card				VARCHAR(30) NOT NULL DEFAULT '',			#工资卡号
	in_wage					SMALLINT UNSIGNED NOT NULL DEFAULT 0,		#入职工资
	cur_wage				SMALLINT UNSIGNED NOT NULL DEFAULT 0,		#当前签约工资
	social_place			VARCHAR(80) NOT NULL DEFAULT '',			#社保缴纳地
	social_basic			SMALLINT UNSIGNED NOT NULL DEFAULT 0,		#社保缴费基数
	funds_basic				SMALLINT UNSIGNED NOT NULL DEFAULT 0,		#公积金缴费基数
	quit_date				DATE NOT NULL DEFAULT '2049-12-31',			#离职日期
	user_a					INT UNSIGNED NOT NULL,						#录入人
	input_date				DATETIME NOT NULL,							#录入时间
	cloud_id				VARCHAR(20) NOT NULL
);

