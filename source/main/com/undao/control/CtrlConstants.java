
package com.undao.control;

/**
 * @author X.Stone
 *
 */
public interface CtrlConstants {
	
	/**
	 * 内置管理员帐户登录名 
	 */
	public final static String INNER_ADMIN = "sysAdmin";

	/**
	 * 内置的公司标记
	 */
	public final static String INNER_CLOUD_ID = "XYZABC";

	/**
	 * 常量定义
	 */
	public final static String CTRL_CONST_Y = "Y";
	public final static String CTRL_CONST_N = "N";

	/**
	 * 边界日期（一般代表空值）
	 */
	public final static String BORDER_DATE_1 = "2001-01-01";
	public final static String BORDER_DATE_2 = "2049-12-31";
	
	/**
	 * 权限级别
	 */
	public final static String ASTRICT_LEVEL_HIGHEST = "0";				//权限级别-最高
	public final static String ASTRICT_LEVEL_MIDDLE = "5";				//权限级别-一般
	public final static String ASTRICT_LEVEL_LOWEST = "9";				//权限级别-最低
	
	/**
	 * SESSION 常量
	 */
	public final static String SESS_VERIFY_CODE = "vCode";				//验证码
	public final static String SESS_VERIFY_TIME = "vTime";				//验证码生成的时间
	public final static String SESS_LANDER_ID = "ID";					//登录者的ID
	public final static String SESS_LANDER_NAME = "Name";				//登录者的名字
	public final static String SESS_LANDER_ROLE = "Role";				//登录者角色(针对内部用户)
	public final static String SESS_LOGIN_NAME = "LoginName";			//当前用户的登录名
	public final static String SESS_AVALIABLE_COMPANIES = "Companies";	//访问范围
	public final static String SESS_AVALIABLE_OPTIONS = "aOptions";		//访问范围OPTIONS列表
	public final static String SESS_CUR_COMPANY = "Company";			//当前用户的工作分公司
	public final static String SESS_CUR_COMPANY_NAME = "CompanyName";	//当前用户的工作分公司
	public final static String SESS_ASTRICT_LEVEL = "aLevel";			//权限级别
	public final static String SESS_CLOUD_ID = "CloudID";
	public final static String SESS_LANDER_FLG = "Flag";				//登录者系统标记
	public final static String SESS_ONLINE_SENSOR = "onSensor";			//SESSION传感器

	public final static String SESS_MODULE_CUS = "mCus";				//访问客户模块
	public final static String SESS_MODULE_DISPATCH = "mDispatch";		//访问调度模块
	public final static String SESS_MODULE_WH = "mWH";					//访问仓库模块

	/**
	 * COOKIE常量
	 */
	public final static String COKK_MEAL_COMPANY = "MealFeeCompany";	//餐补注册分公司
	public final static String COKK_OPERATOR_KEY = "OperatorKey";		//移动端标记

	/**
	 * Request请求中的附加变量
	 */
	public final static String QP_VERIFY_CODE = "vCode";				//验证码
	public final static String QP_CUR_PAGE = "curPage";					//当前页
	public final static String QP_PAGE_SIZE = "pgSize";					//每页记录数
	public final static String QP_WINDOW_MODE = "WM";					//请求页的窗口模式

	/**
	 * 请求页的窗口模式
	 * 请求被拒绝后,对当前窗口的处理模式(退回或关闭)
	 * 客户端发送请求需要包含的一个参数，确定页面跳转的目标
	 */
	public final static int WM_PARENT = 5101;							//退回上一页
	public final static int WM_CHILD = 5102;							//关闭
	public final static int WM_GRANDSON = 5103;							//关闭

	/**
	 * 对齐方式
	 */
	public final static int ALIGN_LEFT = 5201;							//左
	public final static int ALIGN_CENTER = 5202;						//居中
	public final static int ALIGN_RIGHT = 5203;							//右
	public final static int ALIGN_TOP_LEFT = 5204;						//顶部左对齐
	public final static int ALIGN_TOP = 5205;							//顶部居中
	public final static int ALIGN_TOP_RIGHT = 5206;						//顶部右对齐
	public final static int ALIGN_BOTTOM_LEFT = 5207;					//底部左对齐
	public final static int ALIGN_BOTTOM = 5208;						//底部居中
	public final static int ALIGN_BOTTOM_RIGHT = 5209;					//底部右对齐

	/**
	 * 对于xms.beans包的一般访问方式，特殊的操作在Bean中自行定义。
	 */
	public static final int BTYPE_INSERT = 6101;						//增
	public static final int BTYPE_UPDATE = 6102;						//改
	public static final int BTYPE_DELETE = 6103;						//删
	public static final int BTYPE_DETAIL = 6104;						//查,一般是根据ID检索一条记录

	/**
	 * 方法执行成功的返回值
	 */
	public final static String RET_BLANK = "";							//空白字符
	public final static String RET_SUCCESS = "1";						//成功
	public final static String RET_FAILED = "0";						//失败

	/**
	 * JSP页面标记,唯一标识该页面,主要用于对菜单项设置不同的外观
	 */
	public final static int PG_SELF_1 = 8101;							//个人办公-开始
	public final static int PG_MY_DOMAIN = 8111;						//我的主页(管理)
	public final static int PG_APPLY_LIST_SELF = 8121;					//我的申请
	public final static int PG_APPLY_LIST = 8122;						//分发
	public final static int PG_APPROVE_LIST_SELF = 8123;				//我的审批
	public final static int PG_APPROVE_LIST = 8124;						//一览
	public final static int PG_EXECUTE_LIST_SELF = 8125;				//我的执行
	public final static int PG_HANDOVER_IN_LIST = 8131;					//工作交接-我收到的
	public final static int PG_HANDOVER_OUT_LIST = 8132;				//工作交接-我发出的
	public final static int PG_SELF_2 = 8199;							//个人办公-结束
	
	public final static int PG_HUMAN_RESOURCE_1 = 8201;					//人力资源-开始
	public final static int PG_EMP_LIST = 8211;							//员工一览
	public final static int PG_ARCHIVE_LIST = 8212;						//档案一览
	public final static int PG_JOIN_LIST = 8213;						//入离职一览
	public final static int PG_TRACE_LIST = 8214;						//试用期跟踪
	public final static int PG_EVALUATE_DAILY = 8221;					//日常考核一览
	public final static int PG_UNDER_EMP_LIST = 8222;					//直接下属
	public final static int PG_KPI_EMP_LIST = 8223;						//关注指标
	public final static int PG_PERFORM_SUMMARY = 8224;					//每月报表
	public final static int PG_RECRUIT_SUMMARY = 8231;					//招聘概况
	public final static int PG_RECRUIT_LIST = 8232;						//明细记录
	public final static int PG_JOIN_GEOMETRY = 8233;					//入职分布
	public final static int PG_JOIN_GEOMETRY_COMPANY = 8234;			//分布汇总
	public final static int PG_JOB_CHANNEL_GEOMETRY = 8235;				//招聘渠道分布
	public final static int PG_RECRUIT_YEARLY = 8236;					//年度统计
	public final static int PG_HUMAN_RESOURCE_2 = 8299;					//人力资源-结束
	
	public final static int PG_ADMIN_1 = 8301;							//行政管理-开始
	public final static int PG_ACCIDENT_LIST = 8311;					//事故报告一览
	public final static int PG_ACCIDENT_GEOMETRY_KIND = 8312;			//按类别概览
	public final static int PG_ACCIDENT_GEOMETRY_MONTHLY = 8313;		//按月份概览
	public final static int PG_ATTEND_LIST = 8321;						//考勤明细
	public final static int PG_ATTEND_MONTHLY = 8322;					//考勤月报
	public final static int PG_ATTEND_YEARLY = 8323;					//考勤年报
	public final static int PG_MEALFEE_CHECKING1_LIST = 8331;			//餐补-待运营审核
	public final static int PG_MEALFEE_CHECKING2_LIST = 8332;			//餐补-待行政审核
	public final static int PG_MEALFEE_LIST = 8333;						//餐补明细
	public final static int PG_MEALFEE_MONTHLY = 8334;					//餐补月报
	public final static int PG_OPERATOR_LIST = 8341;					//非认证账户一览
	public final static int PG_REGISTER_MEAL_LIST = 8342;				//待审核1一览
	public final static int PG_REGISTER_MEALCHECK_LIST = 8343;			//待审核2一览
	public final static int PG_ADMIN_2 = 8399;							//行政管理-结束	
	
	public final static int PG_SALARY_1 = 8401;							//薪酬福利-开始
	public final static int PG_EMP_SALARY_LIST = 8411;					//员工一览(工资配置)
	public final static int PG_CUT_LIFE_LIST = 8421;					//水电扣款一览
	public final static int PG_CUT_ABSENCE_LIST = 8422;					//考勤扣款一览
	public final static int PG_PERCENTAGE_LIST = 8423;					//提成一览
	public final static int PG_CUT_OTHER_LIST = 8424;					//赔款和事故报告一览
	public final static int PG_ADJUST_PART_LIST = 8425;					//部门调整一览
	public final static int PG_ADJUST_LIST = 8426;						//总部调整一览
	public final static int PG_SALARY_MONTHLY = 8431;					//每月工资报表
	public final static int PG_BUT_MONTHLY = 8432;						//每月凭票补贴一览
	public final static int PG_SOCIAL_MONTHLY = 8433;					//每月社保一览
	public final static int PG_FUNDS_MONTHLY = 8434;					//每月公积金一览
	public final static int PG_SALARY_2 = 8499;							//薪酬福利-结束
		
	public final static int PG_ASSETS_PURCHASE_1 = 8601;				//资产采购-开始
	public final static int PG_PROVIDER_LIST = 8611;					//供应商信息
	public final static int PG_ASSETS_LIST = 8621;						//固定资产
	public final static int PG_SHIFT_LIST = 8622;						//转移记录
	public final static int PG_MAINTAIN_LIST = 8623;					//维修记录
	public final static int PG_CONSUMABLES_LIST = 8631;					//低值易耗品
	public final static int PG_CONSUMABLES_BALANCE = 8632;				//库存
	public final static int PG_PURCHASE_LIST = 8633;					//采购记录
	public final static int PG_ALLOT_LIST = 8634;						//调拨记录
	public final static int PG_EXPEND_LIST = 8635;						//领用记录
	public final static int PG_PAYMENT_OBJ_OUT_LIST = 8641;				//应付对象
	public final static int PG_CONTRACT_OUT_LIST = 8642;				//供应商合同一览
	public final static int PG_CONTRACT_PAYMENT_LIST = 8643;			//合同付款一览
	public final static int PG_ASSETS_PURCHASE_2 = 8699;				//资产采购-结束
	
	public final static int PG_SYS_CONFIGURE_1 = 8701;					//系统配置-开始
	public final static int PG_CONSOLE_PANEL = 8711;					//控制台
	public final static int PG_EMP_SECURITY = 8721;						//账号设定
	public final static int PG_ROLE_LIST = 8731;						//角色一览
	public final static int PG_EVALUATION_LIST = 8732;					//考核指标
	public final static int PG_PROJECT_LIST = 8741;						//项目一览
	public final static int PG_ADDRESS_LIST = 8742;						//收发货地址一览
	public final static int PG_FEE_ENGINE_LIST = 8743;					//费用模板一览
	public final static int PG_UPP_GROUP_LIST = 8744;					//工作班组一览
	public final static int PG_SYS_CONFIGURE_2 = 8799;					//系统配置-结束
	
	public final static int PG_SALE_MANAGE_1 = 9101;					//销售管理-开始
	public final static int PG_SALE_LIST = 9111;						//销售明细
	public final static int PG_SALE_MONTHLY = 9112;						//销售月报
	public final static int PG_WAYBILL_STATUS_ALERT = 9121;				//运单状态预警
	public final static int PG_WAIT_ACCOUNT_LIST = 9131;				//应收明细
	public final static int PG_WAIT_ACCOUNT_MONTHLY = 9132;				//应收月报
	public final static int PG_SALE_MANAGE_2 = 9199;					//销售管理-结束
	
	public final static int PG_CUSTOMER_SERVICE_1 = 9201;				//客户服务-开始
	public final static int PG_TRANS_PLAN_LIST = 9211;					//运输计划一览
	public final static int PG_TRANS_PLAN_ALERT = 9212;					//运输计划预警
	public final static int PG_CUSTOMER_SERVICE_2 = 9299;				//客户服务-结束
	
	public final static int PG_SHORT_DISPATCH_1 = 9301;					//短驳调度-开始
	public final static int PG_WAIT_DISPATCH_LIST = 9311;				//等待安排
	public final static int PG_DISPATCH_HISTORY = 9312;					//车次一览
	public final static int PG_TRUCK_IDLE_SUMMARY = 9315;				//车辆闲置情况概览
	public final static int PG_SHORT_DISPATCH_2 = 9399;					//短驳调度-结束
	
	public final static int PG_WARE_HOUSE_1 = 9401;						//仓库管理-开始
	public final static int PG_WAIT_INOUT_LIST = 9411;					//等待出入库一览
	public final static int PG_TRUCK_IDLE_LIST = 9412;					//车辆使用情况一览
	public final static int PG_WARE_HOUSE_2 = 9499;						//仓库管理-结束
	
	public final static int PG_LONG_DISPATCH_1 = 9501;					//干线配载-开始
	public final static int PG_WAIT_WAYBILL_LIST = 9511;				//待发运单
	public final static int PG_PREV_DISPATCH_LIST = 9512;				//预录派车单
	public final static int PG_LONG_DISPATCH_LIST = 9513;				//正式派车单
	public final static int PG_DISPATCH_COME_LIST = 9521;				//即将到站
	public final static int PG_DISPATCH_PAST_LIST = 9522;				//已到站
	public final static int PG_UPP_COST_LIST = 9531;					//装卸提成一览
	public final static int PG_LONG_DISPATCH_2 = 9599;					//干线配载-结束
	
	public final static int PG_THIRD_PARTY_1 = 9601;					//三方管理-开始
	public final static int PG_WAIT_THIRD_TH_LIST = 9611;				//待提货
	public final static int PG_WAIT_THIRD_SH_LIST = 9612;				//待送货
	public final static int PG_WAIT_THIRD_ZZ_LIST = 9613;				//待中转
	public final static int PG_CONTROL_THIRD_END_LIST = 9621;			//待关闭列表
	public final static int PG_THIRD_LIST = 9622;						//三方一览
	public final static int PG_THIRD_ACCOUNT_LIST = 9623;				//中转费
	public final static int PG_THIRD_PARTY_2 = 9699;					//三方管理-结束
	
	public final static int PG_FLEET_MANAGE_1 = 9701;					//车队管理-开始
	public final static int PG_TRUCK_LIST = 9711;						//车辆一览
	public final static int PG_ALLOCATE_LIST = 9712;					//调车记录
	public final static int PG_TRAVEL_LIST = 9721;						//出车记录
	public final static int PG_PERCENTAGE_MONTHLY = 9722;				//提成月报
	public final static int PG_OIL_LIST = 9731;							//加油记录
	public final static int PG_RATIO_LIST = 9732;						//司机油耗明细
	public final static int PG_RATIO_MONTHLY = 9733;					//油耗月报
	public final static int PG_OILCARD_LIST = 9736;						//油卡一览
	public final static int PG_OILCARD_INOUT = 9737;					//油卡出入记录
	public final static int PG_OILCARD_HISTORY = 9738;					//油卡历史状态
	public final static int PG_GUARD_LIST = 9741;						//维保项目
	public final static int PG_REPAIR_LIST = 9742;						//维修一览
	public final static int PG_INSURE_LIST = 9751;						//保险一览
	public final static int PG_TRUCK_FEE_LIST = 9752;					//车辆杂费
	public final static int PG_TRUCK_MONTHLY = 9761;					//车辆月报
	public final static int PG_FLEET_MANAGE_2 = 9799;					//车队管理-结束

	public final static int PG_FINANCE_MANAGE_1 = 9801;					//财务审计-开始
	public final static int PG_PAYMENT_OBJECT_LIST = 9811;				//收付对象
	public final static int PG_CONTRACT_LIST = 9812;					//合同一览
	public final static int PG_ALERT_RECEIVE = 9821;					//应收未销预警
	public final static int PG_ALERT_PAYMENT = 9822;					//应付未销预警
	public final static int PG_FEE_RECEIVE_LIST = 9831;					//应收费用一览
	public final static int PG_FEE_PAYMENT_LIST = 9832;					//应付费用一览
	public final static int PG_FEE_MANAGE_LIST = 9833;					//管理费用一览
	public final static int PG_CHECK_ACCOUNT_LIST = 9841;				//对账单一览
	public final static int PG_GROSS_WAYBILL_FF = 9851;					//运单毛利
	public final static int PG_GROSS_DISPATCH_FF = 9852;				//整车毛利
	public final static int PG_GROSS_OTHER_FF = 9853;					//其它毛利
	public final static int PG_FINANCE_MANAGE_2 = 9899;					//财务审计-结束
	
	public final static int PG_POLICY_ANALYSIS_1 = 9901;				//决策分析-开始
	public final static int PG_DISPT_RUNTIME = 9911;					//实时发车量
	public final static int PG_DISPT_MONTHLY = 9912;					//发车量历史汇总
	public final static int PG_GROSS_WAYBILL_PA = 9921;					//运单毛利
	public final static int PG_GROSS_DISPATCH_PA = 9922;				//整车毛利
	public final static int PG_GROSS_OTHER_PA = 9923;					//其它毛利
	public final static int PG_PURE_PROFIT_LIST = 9931;					//纯利
	public final static int PG_POLICY_ANALYSIS_2 = 9999;				//决策分析-结束
	
}
