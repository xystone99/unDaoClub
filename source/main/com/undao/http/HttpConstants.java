package com.undao.http;

public interface HttpConstants {
	
	/**
	 * 换行符
	 * 生成multipart/form-data字符串时，需调用这个常量
	 */
	public static final String CRLF = "\r\n";
	
	/**
	 * 缓存区的长度
	 */
	public static final int BUFFER_SIZE = 2048;
	
	/**
	 * 交易类别(出售,出租)
	 */
	public static final String TRADE_SELL = "Sell";
	public static final String TRADE_RENT = "Rent";
	
	/**
	 * 封装trn_grasp_record,trn_personal_record,trn_grasp_record三表中的字段
	 */
	public static final String WEB_FLG = "web_flg";				//来源网站.当来源于同一网站时,使用类属性webFlg
	public static final String TRADE_TYPE = "trade_type";		//交易类别:出租,出售
	public static final String DISTRICT = "district";			//区域
	public static final String BLOCK = "block";					//板块
	public static final String ESTATE_ZH = "estate_zh";			//楼盘名称
	public static final String TITLE = "title";					//房源标题
	public static final String COUNT_F = "count_f";				//房型 
	public static final String AT_FLOOR = "at_floor";			//所在楼层
	public static final String TOTAL_FLOOR = "total_floor";		//总楼层数
	public static final String AREA = "area";					//面积
	public static final String PRICE = "price";					//价格
	public static final String INTRO = "intro";					//简介
	public static final String URL = "url";						//房源详细信息的URL	
	
	public static final String LINK_MAN = "linkman";			//联系人
	public static final String MOBILE = "mobile";				//联系电话
	
	public static final String HAVE_IMAGE = "have_image";		//有无关联图片
	public static final String FX_IMGS = "fx_imgs";				//房型图列表
	public static final String SN_IMGS = "sn_imgs";				//室内图列表
	public static final String COUNT_T = "count_t";				//客厅数
	public static final String COUNT_W = "count_w";				//卫生间数
	public static final String TOWARD = "toward";				//朝向
	public static final String HOUSE_TYPE = "house_type";		//房型
	public static final String FITMENT = "fitment";				//装修情况
	public static final String CREATE_TIME = "create_time";		//建造年代
	public static final String REMARK = "remark";				//描述
	
	/**
	 * 服务器返回值
	 */
	public static final String RTN_SUCCESS = "Success";			//表示对网站操作成功
	public static final String RTN_EXCEPTION = "服务器报告异常";	//出现异常
	public static final String RTN_SPACE = "";					//返回空值

}
