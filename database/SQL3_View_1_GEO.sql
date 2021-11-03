
#------------------------------------------------------------------------------------------------------------------------
# 我能看的好友列表
#------------------------------------------------------------------------------------------------------------------------
CREATE OR REPLACE VIEW view_can_view_list AS
SELECT
	r.lover_r						lover_r,
	r.user_i_v						user_i_v,
	r.user_i_w						user_i_w,
	u1.we_id						we_id_v,
	u2.we_id						we_id,
	r.user_nick_w					we_nick,
	u2.avatar_url					avatar_url,
	u2.split_tag					split_tag,
	u2.cur_longitude				cur_longitude,
	u2.cur_latitude					cur_latitude,
	u2.effect_time					effect_time,
	r.dt_start						dt_start,
	r.dt_end						dt_end,
	r.dt_start_h					dt_start_h,
	r.dt_end_h						dt_end_h,
	r.acode_file_name				acode_file_name,
	r.input_date					input_date,
	r.last_update					last_update
FROM
	tbl_user_relation r
	LEFT JOIN tbl_user u1 ON r.user_i_v = u1.user_i
	LEFT JOIN tbl_user u2 ON r.user_i_w = u2.user_i;


#------------------------------------------------------------------------------------------------------------------------
# 能看我的好友列表
#------------------------------------------------------------------------------------------------------------------------
CREATE OR REPLACE VIEW view_be_seen_list AS
SELECT
	r.lover_r						lover_r,
	r.user_i_v						user_i_v,
	r.user_i_w						user_i_w,
	u2.we_id						we_id_w,
	u1.we_id						we_id,
	r.user_nick_v					we_nick,
	u1.avatar_url					avatar_url,
	r.dt_start						dt_start,
	r.dt_end						dt_end,
	r.dt_start_h					dt_start_h,
	r.dt_end_h						dt_end_h,
	r.acode_file_name				acode_file_name,
	r.input_date					input_date,
	r.last_update					last_update
FROM
	tbl_user_relation r
	LEFT JOIN tbl_user u1 ON r.user_i_v = u1.user_i
	LEFT JOIN tbl_user u2 ON r.user_i_w = u2.user_i;
	

#------------------------------------------------------------------------------------------------------------------------
# 活动轨迹记录
#------------------------------------------------------------------------------------------------------------------------
CREATE OR REPLACE VIEW view_activity_track_list AS
SELECT
	r.user_i						user_i,
	r.longitude						longitude,
	r.latitude						latitude,
	r.input_date					input_date
FROM
	trn_geo_record_ud r;


