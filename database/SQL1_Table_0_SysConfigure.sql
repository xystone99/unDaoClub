USE unDaoDB

#------------------------------------------------------------------------------------------------------------------------------------------------------
#	系统表(4)
#	stc_counter  stc_variables  stc_split_table  stc_update_log
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `stc_counter`;				#全局计数器表
CREATE TABLE stc_counter(
	cnt_name				VARCHAR(30) PRIMARY KEY,
	cnt_count				INT	UNSIGNED NOT NULL
);


DROP TABLE IF EXISTS `stc_variables`;			#系统变量表
CREATE TABLE stc_variables(
	cloud_id				VARCHAR(20) NOT NULL,						#All表示全部
	var_name				VARCHAR(30) PRIMARY KEY,
	var_value				VARCHAR(300) NOT NULL DEFAULT ''
);


DROP TABLE IF EXISTS `stc_split_table`;			#分表控制面板
CREATE TABLE stc_split_table(
	tb_name					VARCHAR(50) PRIMARY KEY,					#表名称
	tb_kind					ENUM('GEO','Other') NOT NULL,				#分表类别
	split_tag				VARCHAR(5) NOT NULL,						#分表标志
	cnt_record				INT	UNSIGNED NOT NULL,						#总记录数
	calc_date				DATETIME NOT NULL,							#统计时间
	region_s				INT UNSIGNED NOT NULL, 						#UserID区间开始
	region_e				INT UNSIGNED NOT NULL DEFAULT 999999999,	#UserID区间截至
	input_date				DATETIME NOT NULL,							#创建时间
	last_update				DATETIME NOT NULL,							#最后更新时间
	sys_flg					ENUM('Recent','Past') NOT NULL				#系统标记:当前分表;历史分表
);


DROP TABLE IF EXISTS `stc_update_log`;			#维护日志
CREATE TABLE stc_update_log(
	update_l				INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#日志ID
	log_kind				ENUM('NewTable','Calc','Partition') NOT NULL,						
																		#日志类别:创建新表;统计记录数;更新表分区.
	tb_kind					ENUM('GEO','Other') NOT NULL,				#分表类别
	remark					VARCHAR(2000),								#操作描述
	input_date				DATETIME NOT NULL							#创建时间
);


#------------------------------------------------------------------------------------------------------------------------------------------------------
#	用户表(2)
#	tbl_user  trn_payment_record
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `tbl_user`;				#用户表
CREATE TABLE tbl_user(
	user_i					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#用户ID
	we_id					VARCHAR(50) NOT NULL,						#禁止为空
	we_nick					VARCHAR(50) NOT NULL,						#微信昵称					
	split_tag				VARCHAR(10) NOT NULL DEFAULT 'ud',			#分表标记
	avatar_url				VARCHAR(300) NOT NULL DEFAULT '',			#微信头像
	expire_time				DATETIME NOT NULL DEFAULT '2021-01-01',		#取经纬度的截止时间
	cur_longitude			CHAR(17) NOT NULL DEFAULT '',				#最近一次记录的经度
	cur_latitude 			CHAR(17) NOT NULL DEFAULT '',				#最近一次记录的纬度
	effect_time				DATETIME NOT NULL DEFAULT '2021-01-01',		#最近一次记录其经纬度的时间(同GEO表的input_date)
	if_walker_update		ENUM('N','Y') NOT NULL DEFAULT 'N',			#行走者列表已更新需要刷新;未更新无须刷新;以便利用客户端缓存.
	total_amount			DECIMAL(8,2) NOT NULL DEFAULT 0,			#付款总额
	input_date				DATETIME NOT NULL,							#登记时间
	last_update				DATETIME NOT NULL							#最后更新时间			
);
ALTER TABLE tbl_user ADD UNIQUE INDEX idx_user(we_id);


DROP TABLE IF EXISTS `trn_payment_record`;		#支付记录表
CREATE TABLE trn_payment_record(
	pay_r					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#付款ID
	user_i					INT UNSIGNED NOT NULL,						#用户ID
	tb_kind					ENUM('GEO','Other') NOT NULL,				#分表类别
	amount					DECIMAL(7,2) NOT NULL,						#付款金额
	pay_date				DATE NOT NULL,								#付款日期
	remark					VARCHAR(80) NOT NULL DEFAULT '',			#付款说明
	input_date				DATETIME NOT NULL							#创建时间			
);
ALTER TABLE trn_payment_record ADD INDEX idx_payment_record(user_i);


