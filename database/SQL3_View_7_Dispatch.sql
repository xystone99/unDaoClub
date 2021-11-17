
#------------------------------------------------------------------------------------------------------------------------
# Dispatch
#------------------------------------------------------------------------------------------------------------------------
CREATE OR REPLACE VIEW view_trans_plan_list AS
SELECT
	r.trans_p						trans_p,
	r.plan_k						plan_k,
	r.plan_date						plan_date,
	r.obj_p							obj_p,
	p.ne_short						obj_short,
	r.trans_l						trans_l,
	l.line_tag						line_tag,
	r.time_level					time_level,
	r.ne_recycle					ne_recycle,
	r.ne_zh1						ne_zh1,
	r.address_1						address_1,
	r.linkman_1						linkman_1,
	r.window_1						window_1,
	r.remark_1						remark_1,
	r.ne_zh2						ne_zh2,
	r.address_2						address_2,
	r.linkman_2						linkman_2,
	r.window_2						window_2,
	r.remark_2						remark_2,
	r.qty_w							qty_w,
	r.qty_v							qty_v,
	r.qty_meter						qty_meter,
	r.qty_meter_r					qty_meter_r,
	r.route_zh						route_zh,
	r.dispatch_remark				dispatch_remark,
	r.user_zh_d						user_zh_d,
	r.input_date_d					input_date_d,
	r.wh_remark						wh_remark,
	r.user_zh_w						user_zh_w,
	r.input_date_w					input_date_w,
	r.user_a						user_a,
	u.ne_zh							user_zh,
	r.input_date					input_date,
	r.cloud_id						cloud_id
FROM
	trn_trans_plan r
	LEFT JOIN tbl_payment_object p ON r.obj_p = p.obj_p
	LEFT JOIN tbl_user_account u ON r.user_a = u.user_a
	LEFT JOIN tbl_trans_line l ON r.trans_l = l.trans_l;

	
CREATE OR REPLACE VIEW view_dispatch_main AS 
SELECT
	d.dispt							dispt,
	d.dispt_serial					dispt_serial,
	d.depart_date					depart_date,
	d.truck							truck,
	d.plate_number					plate_number,
	d.driver						driver,
	d.tel_driver					tel_driver,
	d.sub_driver					sub_driver,
	u.ne_zh							sub_driver_zh,
	d.remark						remark,
	d.user_a						user_a,
	d.input_date					input_date,
	d.wh_remark_g					wh_remark_g,
	d.user_zh_wg					user_zh_wg,
	d.input_date_wg					input_date_wg,
	d.cloud_id						cloud_id
FROM
	trn_dispatch d
	LEFT JOIN tbl_user_account u ON d.sub_driver = u.user_a;
	

CREATE OR REPLACE VIEW view_dispatch_detail AS 
SELECT
	d.dispt							dispt,
	d.dispt_serial					dispt_serial,
	d.depart_date					depart_date,
	d.truck							truck,
	d.plate_number					plate_number,
	d.driver						driver,
	d.tel_driver					tel_driver,
	d.sub_driver					sub_driver,
	u.ne_zh							sub_driver_zh,
	d.remark						remark,
	d.user_a						user_a,
	d.input_date					input_date,
	d.wh_remark_g					wh_remark_g,
	d.user_zh_wg					user_zh_wg,
	d.input_date_wg					input_date_wg,
	p.trans_p						trans_p,
	p.plan_k						plan_k,
	p.obj_p							obj_p,
	o.ne_short						obj_short,
	p.time_level					time_level,
	p.ne_recycle					ne_recycle,
	p.ne_zh1						ne_zh1,
	p.address_1						address_1,
	p.linkman_1						linkman_1,
	p.window_1						window_1,
	p.remark_1						remark_1,
	p.ne_zh2						ne_zh2,
	p.address_2						address_2,
	p.linkman_2						linkman_2,
	p.window_2						window_2,
	p.remark_2						remark_2,
	p.qty_w							qty_w,
	p.qty_v							qty_v,
	p.qty_meter						qty_meter,
	p.qty_meter_r					qty_meter_r,
	p.route_zh						route_zh,
	p.dispatch_remark				dispatch_remark,
	p.user_zh_d						user_zh_d,
	p.input_date_d					input_date_d,
	p.wh_remark						wh_remark,
	p.user_zh_w						user_zh_w,
	p.input_date_w					input_date_w,
	d.cloud_id						cloud_id
FROM
	trn_dispatch d
	LEFT JOIN trn_dispatch_record r ON d.dispt = r.dispt
	LEFT JOIN trn_trans_plan p ON r.trans_p = p.trans_p
	LEFT JOIN tbl_payment_object o ON p.obj_p = o.obj_p
	LEFT JOIN tbl_user_account u ON d.sub_driver = u.user_a;

	
CREATE OR REPLACE VIEW view_truck_idle_list AS 
SELECT
	i.truck_i						truck_i,
	i.idle_k						idle_k,
	i.cur_company					cur_company,
	c.ne_zh							company_zh,
	i.truck							truck,
	i.plate_number					plate_number,
	i.driver						driver,
	i.tel_driver					tel_driver,
	i.start_date					start_date,
	i.end_date						end_date,
	i.remark						remark,
	i.user_a						user_a,
	u.ne_zh							user_a_zh,
	i.input_date					input_date,
	i.cloud_id						cloud_id
FROM
	tbl_truck_idle i
	JOIN mst_company c ON i.cur_company = c.company
	JOIN tbl_user_account u ON i.user_a = u.user_a;


	