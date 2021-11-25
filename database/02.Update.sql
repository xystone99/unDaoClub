

INSERT INTO tbl_dispatch_route(route_zh,sort_tag,cloud_id)VALUES('安亭线','ABC','XYZABC');
INSERT INTO tbl_dispatch_route(route_zh,sort_tag,cloud_id)VALUES('浦东线','ABC','XYZABC');
INSERT INTO tbl_dispatch_route(route_zh,sort_tag,cloud_id)VALUES('嘉定线','ABC','XYZABC');
INSERT INTO tbl_dispatch_route(route_zh,sort_tag,cloud_id)VALUES('青浦线','ABC','XYZABC');
INSERT INTO tbl_dispatch_route(route_zh,sort_tag,cloud_id)VALUES('昆山线','ABC','XYZABC');
INSERT INTO tbl_dispatch_route(route_zh,sort_tag,cloud_id)VALUES('南京线','ABC','XYZABC');
INSERT INTO tbl_dispatch_route(route_zh,sort_tag,cloud_id)VALUES('仪征线','ABC','XYZABC');
INSERT INTO tbl_dispatch_route(route_zh,sort_tag,cloud_id)VALUES('平湖线','ABC','XYZABC');
INSERT INTO tbl_dispatch_route(route_zh,sort_tag,cloud_id)VALUES('宁波线','ABC','XYZABC');


INSERT INTO mst_company(company,ne_zh,sort_tag,input_date,last_update,sys_flg,cloud_id)VALUES(10001,'上海锂电池仓库','ABC',NOW(),NOW(),'Normal','XYZABC');
INSERT INTO mst_company(company,ne_zh,sort_tag,input_date,last_update,sys_flg,cloud_id)VALUES(10002,'上海线束仓库','ABC',NOW(),NOW(),'Normal','XYZABC');
INSERT INTO mst_company(company,ne_zh,sort_tag,input_date,last_update,sys_flg,cloud_id)VALUES(10003,'上海宝安公路仓库','ABC',NOW(),NOW(),'Normal','XYZABC');
INSERT INTO mst_company(company,ne_zh,sort_tag,input_date,last_update,sys_flg,cloud_id)VALUES(10004,'上海长泾仓库','ABC',NOW(),NOW(),'Normal','XYZABC');
INSERT INTO mst_company(company,ne_zh,sort_tag,input_date,last_update,sys_flg,cloud_id)VALUES(10005,'上海浦东仓库','ABC',NOW(),NOW(),'Normal','XYZABC');


INSERT INTO tbl_role(role,ne_zh,sort_tag,href_index,input_date,cloud_id)VALUES(11,'计划','ABC','CustomerService/transPlanList.jsp',NOW(),'XYZABC');
INSERT INTO tbl_role(role,ne_zh,sort_tag,href_index,input_date,cloud_id)VALUES(12,'调度','ABC','ShortDispatch/waitDispatchList.jsp',NOW(),'XYZABC');
INSERT INTO tbl_role(role,ne_zh,sort_tag,href_index,input_date,cloud_id)VALUES(13,'仓库','ABC','WareHouse/waitInOutList.jsp',NOW(),'XYZABC');


INSERT INTO tbl_user_account(user_a,ne_zh,login_name,login_pwd,can_login,role,cur_company,available_companys,sys_flg,cloud_id)VALUES(10011,'夏芸','xiayun',MD5('abcd1234'),'Y',11,0,'10001,10002,10003','Normal','XYZABC');
INSERT INTO tbl_user_account(user_a,ne_zh,login_name,login_pwd,can_login,role,cur_company,available_companys,sys_flg,cloud_id)VALUES(10012,'黄华','huanghua',MD5('abcd1234'),'Y',12,10002,'10001,10002,10003,10004,10005','Normal','XYZABC');
INSERT INTO tbl_user_account(user_a,ne_zh,login_name,login_pwd,can_login,role,cur_company,available_companys,sys_flg,cloud_id)VALUES(10013,'吴方舟','wufangzhou',MD5('abcd1234'),'Y',13,10001,'10001','Normal','XYZABC');


INSERT INTO tbl_payment_object(obj_p,ne_zh,ne_short,init_spell,if_cus,if_provider,cloud_id,sys_flg)VALUES(10001,'电池包国内陆路运输','电池包国内陆路运输','DCB','Y','N','XYZABC','Normal');
INSERT INTO tbl_payment_object(obj_p,ne_zh,ne_short,init_spell,if_cus,if_provider,cloud_id,sys_flg)VALUES(10002,'华美工程塑料（常熟）有限公司','华美工程塑料（常熟）有限公司','HMGC','Y','N','XYZABC','Normal');
INSERT INTO tbl_payment_object(obj_p,ne_zh,ne_short,init_spell,if_cus,if_provider,cloud_id,sys_flg)VALUES(10003,'柯锐世','柯锐世','KRS','Y','N','XYZABC','Normal');
INSERT INTO tbl_payment_object(obj_p,ne_zh,ne_short,init_spell,if_cus,if_provider,cloud_id,sys_flg)VALUES(10004,'平湖科世科','平湖科世科','KSK','Y','N','XYZABC','Normal');
INSERT INTO tbl_payment_object(obj_p,ne_zh,ne_short,init_spell,if_cus,if_provider,cloud_id,sys_flg)VALUES(10005,'上海马勒热有限公司','上海马勒热有限公司','MLR','Y','N','XYZABC','Normal');
INSERT INTO tbl_payment_object(obj_p,ne_zh,ne_short,init_spell,if_cus,if_provider,cloud_id,sys_flg)VALUES(10006,'上海纳铁福康桥工厂','上海纳铁福康桥工厂','NTF','Y','N','XYZABC','Normal');
INSERT INTO tbl_payment_object(obj_p,ne_zh,ne_short,init_spell,if_cus,if_provider,cloud_id,sys_flg)VALUES(10007,'上汽大众动力总成有限公司','上汽大众动力总成有限公司','DZ','Y','N','XYZABC','Normal');
INSERT INTO tbl_payment_object(obj_p,ne_zh,ne_short,init_spell,if_cus,if_provider,cloud_id,sys_flg)VALUES(10008,'苏州波特尼','苏州波特尼','BTN','Y','N','XYZABC','Normal');



INSERT INTO tbl_trans_line(line_tag,obj_p,plan_k,time_level,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,cloud_id)VALUES('',10005,'单程提货','','上海浦东陇桥路','上海浦东陇桥路355号','张沙38522634','','','宝安公路5065号','上海嘉定区安亭镇宝安公路5065号','严志刚15000830510','','单据及时提交仓库','XYZABC');
INSERT INTO tbl_trans_line(line_tag,obj_p,plan_k,time_level,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,cloud_id)VALUES('',10004,'往返运输','准时','平湖市昌盛路','平湖市昌盛路559号 西门进','李深15757882502','','','塔山路1382号/宝安公路5065号','上海嘉定区安亭镇塔山路1382号/宝安公路5065号','付海顺18717977161/严志刚15000830510','','派车单及时提交计划员','XYZABC');
INSERT INTO tbl_trans_line(line_tag,obj_p,plan_k,time_level,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,cloud_id)VALUES('',10004,'单程送货','','宝安公路5065号','上海嘉定区安亭镇宝安公路5065号','严志刚15000830510','','','昆山市张浦镇沪光路','江苏省昆山市张浦镇沪光路388号','李莉132 9510 0835','','单据及时提交计划员','XYZABC');
INSERT INTO tbl_trans_line(line_tag,obj_p,plan_k,time_level,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,cloud_id)VALUES('',10008,'往返运输','','苏州吴江区芦墟大道','苏州吴江区芦墟大道2299号','屠文峰13776106748','','','塔山路1382号','上海嘉定区安亭镇塔山路1382号','李秀丽15713271502/刘璐','','单据及时提交计划员','XYZABC');
INSERT INTO tbl_trans_line(line_tag,obj_p,plan_k,time_level,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,cloud_id)VALUES('',10003,'单程送货','一般','宝安公路5065','宝安公路5065','','','','两港大道2999','两港大道2999','','','7-1道口','XYZABC');
INSERT INTO tbl_trans_line(line_tag,obj_p,plan_k,time_level,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,cloud_id)VALUES('',10003,'单程送货','一般','宝安公路5065','宝安公路5065','','','','倚天路388号','倚天路388号','','','','XYZABC');
INSERT INTO tbl_trans_line(line_tag,obj_p,plan_k,time_level,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,cloud_id)VALUES('',10003,'往返送货','一般','宝安公路5065','宝安公路5065','','','','安研路201','安研路201','','','','XYZABC');
INSERT INTO tbl_trans_line(line_tag,obj_p,plan_k,time_level,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,cloud_id)VALUES('',10003,'单程送货','一般','宝安公路5065','宝安公路5065','','','','春浓路955-2','春浓路955-2','','','','XYZABC');
INSERT INTO tbl_trans_line(line_tag,obj_p,plan_k,time_level,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,cloud_id)VALUES('',10003,'单程送货','一般','宝安公路5065','宝安公路5065','','','','鹿吉路303','鹿吉路303','周宇+13641856606','','危险品车','XYZABC');
INSERT INTO tbl_trans_line(line_tag,obj_p,plan_k,time_level,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,cloud_id)VALUES('',10003,'单程送货','可调','宝安公路5065','宝安公路5065','','','','新丹路555','新丹路555','薛浩然+15296710758','','','XYZABC');
INSERT INTO tbl_trans_line(line_tag,obj_p,plan_k,time_level,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,cloud_id)VALUES('',10003,'单程提货','准时','浦东新区金穗路','浦东新区金穗路1055','','','带木托盘、缠绕膜；上午10点到达提货仓库','宝安公路5065','宝安公路5065','','','','XYZABC');
INSERT INTO tbl_trans_line(line_tag,obj_p,plan_k,time_level,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,cloud_id)VALUES('',10003,'单程送货','可调','宝安公路5065','宝安公路5065','','','','申霞路335','申霞路335','','','','XYZABC');
INSERT INTO tbl_trans_line(line_tag,obj_p,plan_k,time_level,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,cloud_id)VALUES('',10003,'单程送货','可调','宝安公路5065','宝安公路5065','','','','敬学路101','敬学路101','','','','XYZABC');
INSERT INTO tbl_trans_line(line_tag,obj_p,plan_k,time_level,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,cloud_id)VALUES('',10003,'单程提货','可调','丰功路888','丰功路888','','','','宝安公路5065','宝安公路5065','','','','XYZABC');
INSERT INTO tbl_trans_line(line_tag,obj_p,plan_k,time_level,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,cloud_id)VALUES('',10003,'往返送货','可调','宝安公路5065','宝安公路5065','','','','湖州长兴县','湖州长兴县和平镇圣远供应链','闫宇+17357239557','','','XYZABC');
INSERT INTO tbl_trans_line(line_tag,obj_p,plan_k,time_level,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,cloud_id)VALUES('',10003,'往返送货','一般','宝安公路5065','宝安公路5065','','','','金山区金山工业区林拓路','金山区金山工业区林拓路255号','','','','XYZABC');
INSERT INTO tbl_trans_line(line_tag,obj_p,plan_k,time_level,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,cloud_id)VALUES('',10003,'单程提货','一般','浦东新区顾徐路8号','浦东新区顾徐路8号','','','','宝安公路5065','宝安公路5065','','','','XYZABC');
INSERT INTO tbl_trans_line(line_tag,obj_p,plan_k,time_level,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,cloud_id)VALUES('',10003,'往返送货','可调','奉贤区平安大道红庄村1179号','奉贤区平安大道红庄村1179号','','','','宝安公路5065','宝安公路5065','','','','XYZABC');
INSERT INTO tbl_trans_line(line_tag,obj_p,plan_k,time_level,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,cloud_id)VALUES('',10006,'返空提货','可调','上海市浦东新区康桥路','上海市浦东新区康桥路950号','陆剑-02158122412','','数量确认，单据签收','外冈镇长泾村','上海市外冈镇长泾村鸡场路37号','仓库-02139556839','','数量确认，单据签收','XYZABC');
INSERT INTO tbl_trans_line(line_tag,obj_p,plan_k,time_level,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,cloud_id)VALUES('',10007,'单程提货','可调','上海市嘉定区城北路','上海市嘉定区城北路3598号','赵健-13501893478','','数量确认，单据签收','外冈镇长泾村鸡场路','上海市外冈镇长泾村鸡场路37号','仓库-02139556839','','数量确认，单据签收','XYZABC');
INSERT INTO tbl_trans_line(line_tag,obj_p,plan_k,time_level,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,cloud_id)VALUES('',10002,'单程提货','可调','江苏常熟海虞镇新材料产业园','中国江苏常熟海虞镇新材料产业园海宁路16号','陈柳营-13776231920','','数量确认，单据签收','外冈镇长泾村鸡场路','上海市外冈镇长泾村鸡场路37号','仓库-02139556839','','数量确认，单据签收','XYZABC');


UPDATE tbl_trans_line SET line_tag=CONCAT(ne_zh1,'-->',ne_zh2);


INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('张细毛','15800638806','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('殷志昂','18721955715','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('张东风','18256150181','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('王太平','13661568767','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('顾启亚','13817068464','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('张庆宏','18321329523','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('刘敬礼','18502193823','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('顾海威','18021818981','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('苏芳辉','18338262615','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('刘飞','15012353470','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('张帅','13155212188','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('张永辉','13852236468','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('宛银春','15821416327','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('王鹏程','13869450321','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('刘志华','17521531289','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('梅占林','15866998508','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('刘清龙','18217096718','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('朱泽华','15827796662','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('徐丹','13997669266','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('吴全刚','13701920475','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('张兆明','18055252522','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('项开寨','18818118013','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('王昌东','13606752285','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('储云','13797513211','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('李留西','13501994597','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('苏彬','13605223698','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('蒋兵','13661611005','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('赵战欣','15027171545','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('凡跃云','15062096939','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('朱坚坚','13834716458','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('朱其峰','13655506025','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('江兴明','19921495191','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('王孔','18738227141','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('张斌','13358054496','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('沈德洋','15900711349','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('李荣','13331998351','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('周龚','13917015396','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('凡友军','18321223070','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('凡士春','18352222099','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('张泊','18739455560','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('王强','14763246581','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('程雪峰','18521327072','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('徐咸福','15800967861','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('喻明安','13167191578','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('王利明','15900545597','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('叶春林','18016099952','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('张华','13621758971','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('卢友领','15821405972','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('张建国','13962639232','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('黄军','13554588251','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('杨洋','15051671659','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('桑佰停','15137442155','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('吕全','18621697849','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('李连官','13987339436','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('张凯强','18221579690','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('夏天','15618273141','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('曹广富','18763216733','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('马跃文','15987339389','Y','N','XYZABC','Normal');
INSERT INTO tbl_user_account(ne_zh,tel,if_driver,can_login,cloud_id,sys_flg)VALUES('段新生','13837859868','Y','N','XYZABC','Normal');


UPDATE tbl_user_account SET init_spell=LEFT(getInitSpell(ne_zh),5);


INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DK7348','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FE5587','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FH6311','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪EH3558','危险品单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪D61853','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪EP8957','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DR8248','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DJ7923','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DF0302','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DF0329','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DF0337','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DG1841','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DE5571','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DF6683','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DF6570','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FF5879','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DJ8665','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DF0356','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DF4095','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DE4405','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DG1606','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DG0146','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DF2667','危险品单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪D68576','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪D68669','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪D73625','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪D75735','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪D80165','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪EQ5268','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('皖S37200','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('鲁H7M627','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('浙B0T662','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪EP1261','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('浙B0K556','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DE3271','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DR7976','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪BR3631','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('湘AD6547','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('皖S19689','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DA7230','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪D73593','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('湘AF9962','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪EE0962','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FE2312','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FF2260','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FH9892','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FF3939','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FE6337','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FE3379','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FF8606','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪EC0175','危险品单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪EJ3156','危险品单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DT0788','危险品单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪ES3036','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DB0533','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FF9026','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FH2026','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DL4457','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DL4806','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DL9371','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DP1006','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DP1025','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DP1098','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DL6476','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DP1053','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DP2607','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FE5519','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DH4819','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FF9151','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FH5778','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪ED3210','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪EQ9191','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪D32733','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DJ7933','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DJ7810','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FF9853','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FG5195','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FE1359','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FG8097','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FF0035','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DL6287','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪EJ3267','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪EH3785','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪EL6916','危险品车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪ER2530','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪EP0955','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪EP0967','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪EK9116','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪ER0101','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DP1099','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪ES9018','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DS8603','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FG1153','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪EQ9871','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪EA6353','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪D44875','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪BS6423','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪BL5409','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FH7566','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FF9129','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪FE5807','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪BS6462','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DC3656','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪DC3703','普货单车','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪D99767','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪D90563','普货车头','XYZABC','Normal');
INSERT INTO tbl_truck(plate_number,truck_type,cloud_id,sys_flg)VALUES('沪D44738','普货单车','XYZABC','Normal');




