/**
 * EnumConstants.java
 *
 * Created at 2009-8-13, 下午01:35:00
 */
package com.undao.enumeration;

import java.util.HashMap;

/**
 * 常数，供JSP文件直接调用
 * @author Administrator
 */
public class EnumConstants {

	private static HashMap<String,String> mapEnum = new HashMap<String,String>();
	static {
		mapEnum.put( "Normal", "正常" );
		mapEnum.put( "Cancel", "已取消" );
		mapEnum.put( "Idle", "车辆闲置" );
		mapEnum.put( "Lack", "车辆空缺" );
		mapEnum.put( "Dimission", "已离职" );
		mapEnum.put( "Inner", "内置用户" );
	}
	public final static String getDisplay( String enumKey ) {
		return mapEnum.get( enumKey );
	}

	//是否
	public static final String YES_NO_OPTIONS = "<option value=\"Y\">是</option><option value=\"N\">否</option>";

	//运输计划类别
	public static final String TRANS_PLAN_K_OPTIONS = "<option value=\"单程提货\">单程提货</option><option value=\"返空提货\">返空提货</option><option value=\"单程送货\">单程送货</option><option value=\"直提直送\">直提直送</option><option value=\"往返运输\">往返运输</option>";

	//运输模式
	public static final String TRANS_MODE_OPTIONS = "<option value=\"一装一卸\">一装一卸</option><option value=\"一装两卸\">一装两卸</option><option value=\"一装三卸\">一装三卸</option><option value=\"两装一卸\">两装一卸</option><option value=\"两装两卸\">两装两卸</option><option value=\"两装三卸\">两装三卸</option><option value=\"三装一卸\">三装一卸</option><option value=\"三装两卸\">三装两卸</option><option value=\"三装三卸\">三装三卸</option>";

	//时效要求等级
	public static final String TIME_LEVEL_OPTIONS = "<option value=\"可调\">可调</option><option value=\"一般\">一般</option><option value=\"准时\">准时</option>";

	//返空仓库列表
	public static final String WH_RECYCLE_OPTIONS = "<option>塔山路仓库</option><option>宝安公路仓库</option><option>长泾仓库</option>";

	//车辆闲置类型
	public static final String TRUCK_IDLE_K_OPTIONS = "<option value=\"Idle\">车辆闲置</option><option value=\"Lack\">车辆空缺</option>";

	//仓库回报信息列表
	public static final String WH_RETURN_DISPATCH_OPTIONS = "<option>空箱装车完毕;</option><option>派送货物装车完毕;</option><option>提货返回卸车完毕;</option><option>车辆发车;</option><option>车辆到达;</option>";


	//允许登录(员工表)
	public static final String CAN_LOGIN_OPTIONS = "<option value=\"Y\">允许</option><option value=\"N\">禁止</option>";

	//权限级别
	public static final String ASTRICT_LEVEL_OPTIONS = "<option value=\"0\">最低</option><option value=\"5\">一般</option><option value=\"9\">最高</option>";

	//用户状态
	public static final String USER_STATUS_OPTIONS = "<option value=\"Normal\">正常</option><option value=\"Inner\">内置用户</option><option value=\"Dimission\">已离职</option>";

	//员工状态
	public static final String EMP_STATUS_OPTIONS = "<option value=\"Normal\">普通员工</option><option value=\"Dimission\">已离职</option><option value=\"Cancel\">已取消</option>";

	//驾驶员状态
	public static final String DRIVER_STATUS_OPTIONS = "<option value=\"Normal\">正常</option><option value=\"Dimission\">离职</option><option value=\"Cancel\">待删除</option>";
	
	//车辆类别
	public static final String TRUCK_K_OPTIONS = "<option value=\"Normal\">普通车</option><option value=\"Head\">车头</option><option value=\"Trailer\">挂车</option>";
	
	//车辆状态
	public static final String TRUCK_STATUS_OPTIONS = "<option value=\"Normal\">正常</option><option value=\"Scrap\">报废</option><option value=\"Cancel\">已取消</option>";


}
