USE unDAODB

#------------------------------------------------------------------------------------------------------------------------------------------------------
#	系统(2)
#	stc_variables  tbl_user_training
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `stc_variables`;				#系统变量表
CREATE TABLE stc_variables(
	var_name				VARCHAR(30) PRIMARY KEY,
	var_value				VARCHAR(300) NOT NULL DEFAULT ''
);


DROP TABLE IF EXISTS `stc_split_table`;
CREATE TABLE stc_split_table(
	split_r					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#分表标志ID
	split_kind				ENUM('Train','Inquiry','Exam') NOT NULL,	#分表类别:培训,调查,考试
	table_tag				VARCHAR(5) NOT NULL,						#分表标志(UD)
	cnt_record				INT UNSIGNED NOT NULL DEFAULT 0,			#表的最高纪录数(非实时)
	calc_date				DATETIME NOT NULL,							#统计时间
	if_valid				ENUM('N','Y') NOT NULL,						#有效标志
	input_date				DATETIME NOT NULL,							#登记时间
	last_update				TIMESTAMP NOT NULL							#最后更新时间
);
INSERT INTO stc_split_table(split_kind,table_tag,calc_date,if_valid,input_date)VALUES('Train','ud',NOW(),'Y',NOW());
INSERT INTO stc_split_table(split_kind,table_tag,calc_date,if_valid,input_date)VALUES('Inquiry','ud',NOW(),'Y',NOW());
INSERT INTO stc_split_table(split_kind,table_tag,calc_date,if_valid,input_date)VALUES('Exam','ud',NOW(),'Y',NOW());




#------------------------------------------------------------------------------------------------------------------------------------------------------
#	培训(4)
#	tbl_training_ud  tbl_train_question_ud  trn_trainee_ud  trn_trainee_testing_ud
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `tbl_training_ud`;				#培训表
CREATE TABLE tbl_training_ud(
	train_i					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#培训ID
	user_i					INT UNSIGNED NOT NULL,						#用户ID
	title					VARCHAR(50) NOT NULL,						#标题
	remark					VARCHAR(600) NOT NULL DEFAULT '',			#备注
	train_date				DATE NOT NULL,								#培训日期
	cnt_trainee				SMALLINT NOT NULL DEFAULT 0,				#受训人数
	cnt_clock_in			SMALLINT NOT NULL DEFAULT 0,				#要求打卡次数
	input_date				DATETIME NOT NULL,							#登记时间
	last_update				TIMESTAMP NOT NULL							#最后更新时间			
);
ALTER TABLE tbl_training_ud ADD INDEX idx_user_i(user_i);


DROP TABLE IF EXISTS `tbl_train_question_ud`;		#培训题目表
CREATE TABLE tbl_train_question_ud(
	quest_r					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#题目ID
	train_i					INT UNSIGNED NOT NULL,						#培训ID
	question				VARCHAR(80) NOT NULL DEFAULT '',			#问题
	items					VARCHAR(300) NOT NULL DEFAULT '',			#选项列表
	answer					VARCHAR(10) NOT NULL DEFAULT '',			#正确答案
	input_date				DATETIME NOT NULL,							#登记时间
	last_update				TIMESTAMP NOT NULL							#最后更新时间			
);
ALTER TABLE tbl_train_question_ud ADD INDEX idx_train_i(train_i);


DROP TABLE IF EXISTS `trn_trainee_ud`;				#受训人表
CREATE TABLE trn_trainee_ud(
	trainee_i				INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#受训人ID
	train_i					INT UNSIGNED NOT NULL,						#培训ID
	we_id					VARCHAR(20) NOT NULL DEFAULT '0',			#
	dt_id					VARCHAR(20) NOT NULL DEFAULT '0',			#DingTalk
	ne_nick					VARCHAR(50) NOT NULL,						#
	cnt_clock_in_f			SMALLINT NOT NULL DEFAULT 0,				#1.实际打卡次数(35分)
	if_doubt				ENUM('N','Y','0') NOT NULL DEFAULT '0',		#2.是否有疑问(15分)
	doubt_remark			VARCHAR(600) NOT NULL DEFAULT '',			#2.疑问
	if_suggest				ENUM('N','Y','0') NOT NULL DEFAULT '0',		#2.是否有建议(15分)
	suggest					VARCHAR(600) NOT NULL DEFAULT '',			#2.建议
	cnt_right				SMALLINT NOT NULL DEFAULT 0,				#3.答题正确数(35分)
	f_score					SMALLINT NOT NULL DEFAULT 0,				#最终得分
	input_date				DATETIME NOT NULL,							#登记时间
	last_update				TIMESTAMP NOT NULL							#最后更新时间			
);
ALTER TABLE trn_trainee_ud ADD INDEX idx_train_i(train_i);


DROP TABLE IF EXISTS `trn_trainee_testing_ud`;		#培训测试表
CREATE TABLE trn_trainee_testing_ud(
	test_r					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#记录行惟一标记
	trainee					INT UNSIGNED NOT NULL,						#参与人ID
	quest_r					INT UNSIGNED NOT NULL,						#题目ID
	reply					VARCHAR(10) NOT NULL DEFAULT '',			#回答 
	if_right				ENUM('N','Y','0') NOT NULL DEFAULT '0',		#是否正确
	input_date				DATETIME NOT NULL,							#登记时间
	last_update				TIMESTAMP NOT NULL							#最后更新时间			
);
ALTER TABLE trn_trainee_testing_ud ADD INDEX idx_trainee(trainee);


#------------------------------------------------------------------------------------------------------------------------------------------------------
#	调查(3)
#	tbl_inquiry_ud  tbl_inquiry_item_ud  trn_interviewee_ud
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `tbl_inquiry_ud`;				#调查表
CREATE TABLE tbl_inquiry_ud(
	inquiry_i				INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#调查ID
	user_i					INT UNSIGNED NOT NULL,						#用户ID
	title					VARCHAR(50) NOT NULL,						#标题
	remark					VARCHAR(600) NOT NULL DEFAULT '',			#备注
	start_date				DATETIME NOT NULL,							#开始时间
	end_date				DATETIME NOT NULL,							#截止时间
	if_radio				ENUM('N','Y') NOT NULL DEFAULT 'Y',			#是否单选
	if_anonymous			ENUM('N','Y') NOT NULL DEFAULT 'N',			#是否匿名(记名投票限制人数9999)
	accept_other			ENUM('N','Y') NOT NULL DEFAULT 'N',			#允许个人意见
	cnt_participant			INT NOT NULL DEFAULT 0,						#参加人数
	input_date				DATETIME NOT NULL,							#登记时间
	last_update				TIMESTAMP NOT NULL							#最后更新时间			
);
ALTER TABLE tbl_inquiry_ud ADD INDEX idx_user_i(user_i);


DROP TABLE IF EXISTS `tbl_inquiry_item_ud`;			#调查选项表(调查表限制9个选项)
CREATE TABLE tbl_inquiry_item_ud(
	item_r					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#选项ID
	inquiry_i				INT UNSIGNED NOT NULL,						#培训ID
	item_zh					VARCHAR(50) NOT NULL DEFAULT '',			#选项名称
	remark					VARCHAR(300) NOT NULL DEFAULT '',			#选项说明
	cnt_select				INT NOT NULL DEFAULT 0,						#选择人数
	input_date				DATETIME NOT NULL,							#登记时间
	last_update				TIMESTAMP NOT NULL							#最后更新时间			
);
ALTER TABLE tbl_inquiry_item_ud ADD INDEX idx_inquiry_i(inquiry_i);


DROP TABLE IF EXISTS `trn_interviewee_ud`;			#受访者表
CREATE TABLE trn_interviewee_ud(
	intvee_i				INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#受访者ID
	inquiry_i				INT UNSIGNED NOT NULL,						#培训ID
	we_id					VARCHAR(20) NOT NULL DEFAULT '0',			#
	dt_id					VARCHAR(20) NOT NULL DEFAULT '0',			#DingTalk
	ne_nick					VARCHAR(50) NOT NULL,						#
	item_r_select			VARCHAR(100) NOT NULL DEFAULT '',			#以逗号隔开
	other_remark			VARCHAR(300) NOT NULL DEFAULT '',			#其它选择说明
	input_date				DATETIME NOT NULL,							#登记时间
	last_update				TIMESTAMP NOT NULL							#最后更新时间			
);
ALTER TABLE trn_interviewee_ud ADD INDEX idx_inquiry_i(inquiry_i);


#------------------------------------------------------------------------------------------------------------------------------------------------------
#	考试(4)
#	tbl_paper_ud  tbl_paper_item_ud  trn_exam_ud  trn_exam_record_ud
#------------------------------------------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `tbl_paper_ud`;				#试卷表
CREATE TABLE tbl_paper_ud(
	paper_i					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#试卷ID
	user_i					INT UNSIGNED NOT NULL,						#用户ID
	title					VARCHAR(50) NOT NULL,						#标题
	cnt_items				SMALLINT UNSIGNED NOT NULL DEFAULT 0,		#题目数量
	cnt_select				SMALLINT UNSIGNED NOT NULL DEFAULT 0,		#可选题目数量
	start_date				DATETIME NOT NULL,							#允许答题开始时间
	end_date				DATETIME NOT NULL,							#允许答题结束时间
	cnt_examinee			SMALLINT UNSIGNED NOT NULL DEFAULT 0,		#参与人数
	highest_score			SMALLINT UNSIGNED NOT NULL DEFAULT 0,		#最高得分
	lowest_score			SMALLINT UNSIGNED NOT NULL DEFAULT 0,		#最低得分	
	first_date				DATETIME NOT NULL DEFAULT '2049-12-31 23:59:59',	#最早交卷
	last_date				DATETIME NOT NULL DEFAULT '2049-12-31 23:59:59',	#最晚交卷
	remark					VARCHAR(300) NOT NULL DEFAULT '',			#备注
	input_date				DATETIME NOT NULL,							#登记时间
	last_update				TIMESTAMP NOT NULL,							#最后更新时间
	sys_flg					ENUM('Normal','Cancel') NOT NULL DEFAULT 'Normal'
);
ALTER TABLE tbl_paper_ud ADD INDEX idx_user_i(user_i);


DROP TABLE IF EXISTS `tbl_paper_question_ud`;			#试卷题目表
CREATE TABLE tbl_paper_question_ud(
	quest_r					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#记录行惟一标记
	paper_i					INT UNSIGNED NOT NULL,						#试卷ID,
	item_stem				VARCHAR(300) NOT NULL,						#题干
	options					VARCHAR(300) NOT NULL,						#可选项
	answer					VARCHAR(20) NOT NULL,						#正确答案
	analysis				VARCHAR(300) NOT NULL DEFAULT '',			#答案解析
	score					SMALLINT UNSIGNED NOT NULL DEFAULT 0,		#分数
	cnt_exam				SMALLINT UNSIGNED NOT NULL DEFAULT 0,		#答题次数
	cnt_right				SMALLINT UNSIGNED NOT NULL DEFAULT 0,		#正确数量
	input_date				DATETIME NOT NULL,							#登记时间
	last_update				TIMESTAMP NOT NULL,							#最后更新时间
	sys_flg					ENUM('Normal','Cancel') NOT NULL DEFAULT 'Normal'
);
ALTER TABLE tbl_paper_question_ud ADD INDEX idx_paper_i(paper_i);


DROP TABLE IF EXISTS `trn_exam_ud`;				#考试主表
CREATE TABLE trn_exam_ud(
	exam_i					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#考试ID
	paper_i					INT UNSIGNED NOT NULL,						#试卷ID,
	we_id					VARCHAR(20) NOT NULL DEFAULT '0',			#
	dt_id					VARCHAR(20) NOT NULL DEFAULT '0',			#DingTalk
	ne_nick					VARCHAR(50) NOT NULL,						#
	if_finish				ENUM('N','Y') NOT NULL DEFAULT 'N',			#是否交卷
	cnt_select				SMALLINT UNSIGNED NOT NULL DEFAULT 0,		#题目总数
	cnt_right				SMALLINT UNSIGNED NOT NULL DEFAULT 0,		#正确数量
	total_score				SMALLINT UNSIGNED NOT NULL DEFAULT 0,		#题目总分数
	f_score					SMALLINT UNSIGNED NOT NULL DEFAULT 0,		#最终得分(换算为百分制)
	input_date				DATETIME NOT NULL,							#登记时间
	last_update				TIMESTAMP NOT NULL							#最后更新时间	
);
ALTER TABLE trn_exam_ud ADD INDEX idx_paper_i(paper_i);


DROP TABLE IF EXISTS `trn_exam_record_ud`;		#考试记录表
CREATE TABLE trn_exam_record_ud(
	exam_r					INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,	#记录行惟一标记
	exam_i					INT UNSIGNED NOT NULL,						#考试ID
	paper_r					INT UNSIGNED NOT NULL,						#题目ID
	cur_answer				VARCHAR(20) NOT NULL DEFAULT '',			#当前回答		
	if_right				ENUM('N','Y','0') NOT NULL DEFAULT '0',		#是否正确
	score					SMALLINT UNSIGNED NOT NULL DEFAULT 0,		#本题得分
	input_date				DATETIME NOT NULL,							#登记时间
	last_update				TIMESTAMP NOT NULL							#最后更新时间	
);
ALTER TABLE trn_exam_record_ud ADD INDEX idx_exam_i(exam_i);

