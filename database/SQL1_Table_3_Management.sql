USE unDaoDB

#------------------------------------------------------------------------------------------------------------------------------------------------------
#	Management
#	tbl_user  tbl_user_relXX  stc_geo_geometry trn_geo_recXXX  trn_payment_record
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `tbl_user_management`;			#用户表-管理
CREATE TABLE tbl_user_management(
	user_i					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#用户ID
	we_id					VARCHAR(20) NOT NULL DEFAULT '0',			#
	dt_id					VARCHAR(20) NOT NULL DEFAULT '0',			#DingTalk
	ne_nick					VARCHAR(50) NOT NULL,						#
	t_serial_m				VARCHAR(10) NOT NULL DEFAULT 'ud',			#分表标记
	t_serial_h				VARCHAR(10) NOT NULL DEFAULT 'ud',			#分表标记(归档记录)
	total_amount			DECIMAL(8,2) NOT NULL DEFAULT 0,			#
	input_date				DATETIME NOT NULL,							#登记时间
	last_update				TIMESTAMP NOT NULL							#最后更新时间			
);


DROP TABLE IF EXISTS `tbl_user_rel01`;				#关联用户表(用户类别区分内部和外部)
CREATE TABLE tbl_user_rel01(
	rel_id					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#
	user_id					INT UNSIGNED NOT NULL,						#
	we_id					VARCHAR(20) NOT NULL,						#
	ne_nick					VARCHAR(50) NOT NULL DEFAULT '',			#
	ne_remark				VARCHAR(50) NOT NULL DEFAULT '',			#
	start_date				DATE NOT NULL,								#从哪一天开始记录
	end_date				DATE NOT NULL,								#到哪一天结束记录
	store_days				SMALLINT NOT NULL DEFAULT 3,				#存储时长(天数)
	geo_serial				VARCHAR(10) NOT NULL DEFAULT '001',			#GEO记录表序列号
	cur_longitude			CHAR(17) NOT NULL DEFAULT '',				#
	cur_latitude 			CHAR(17) NOT NULL DEFAULT '',				#
	effect_time				DATETIME NOT NULL DEFAULT '2001-01-01',		#
	input_date				DATETIME NOT NULL,							#登记时间
	last_update				TIMESTAMP NOT NULL,							#最后更新时间
	sys_flg					TINYINT NOT NULL DEFAULT 0					#0发出关注;1同意关注;2拒绝关注;3不再关注
);
ALTER TABLE tbl_user_rel01 ADD INDEX idx_user_id(user_id);


DROP TABLE IF EXISTS `trn_management_ud`;				#安排工作表
CREATE TABLE trn_management_ud(
	mangt_i					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#安排ID
	user_i					INT UNSIGNED NOT NULL,						#用户ID
	title					VARCHAR(50) NOT NULL,						#标题
	remark					VARCHAR(600) NOT NULL DEFAULT '',			#备注
	start_date				DATE NOT NULL,								#开始日期
	end_date				DATE NOT NULL,								#要求结束日期
	f_end_date				DATE NOT NULL DEFAULT '2049-12-31'			#实际结束日期	
	mangt_k					TINYINT NOT NULL,							#0主动发起;1上级安排	
	urgent_level			TINYINT NOT NULL,							#0常规;1一般;2紧急;3特急	
	ask_feedback			VARCHAR(20) NOT NULL DEFAULT '',			#反馈方式
	evaluation				VARCHAR(50) NOT NULL DEFAULT '',			#总体评价
	score					TINYINT NOT NULL DEFAULT 0,					#得分
	sys_flg					TINYINT NOT NULL,							#0发起;1进行中(下级收到或上级审核);2关闭
	input_date				DATETIME NOT NULL,							#登记时间
	last_update				TIMESTAMP NOT NULL							#最后更新时间			
);
ALTER TABLE trn_management_ud ADD INDEX idx_user_i(user_i);


DROP TABLE IF EXISTS `trn_management_feedback_ud`;		#工作反馈表
CREATE TABLE trn_management_feedback_ud(
	fback_r					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#题目ID
	mangt_i					INT UNSIGNED NOT NULL,						#培训ID
	remark					VARCHAR(300) NOT NULL DEFAULT '',			#下级反馈
	reply					VARCHAR(300) NOT NULL DEFAULT '',			#上级回复
	input_date				DATETIME NOT NULL,							#登记时间
	last_update				TIMESTAMP NOT NULL							#最后更新时间			
);
ALTER TABLE trn_management_feedback_ud ADD INDEX idx_management_i(mangt_i);

