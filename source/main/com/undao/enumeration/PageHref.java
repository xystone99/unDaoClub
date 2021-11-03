/**
 * PageHref.java
 *
 * Created at 2009-8-13, 下午01:35:00
 */
package com.undao.enumeration;

import com.undao.utils.EnumUtils;

/**
 * 主页列表
 * @author Administrator
 */
public class PageHref {
	
	/**
	 * 主页列表
	 */
	public static final String PG_SYS_CONFIGURE = "SysConfigure/roleList.jsp";
	public static final String PG_CUSTOMER_SERVICE = "CustomerService/transPlanList.jsp";
	public static final String PG_SHORT_DISPATCH = "ShortDispatch/waitDispatchList.jsp";
	public static final String PG_WARE_HOUSE = "WareHouse/waitDispatchList.jsp";
	
	private static String[] arrPage = {
			PG_SYS_CONFIGURE, PG_CUSTOMER_SERVICE, PG_SHORT_DISPATCH, PG_WARE_HOUSE
	};
	private static String[] arrName = {
			"管理员主页", "客户服务", "车辆调度", "仓库管理"
	};
	
	public static int size( ) {
		return arrPage.length;
	}
	
	public static String getPage( int index ) {
		return arrPage[index];
	}
	
	public static String getDisplay( int index ) {
		return arrName[index];
	}

	/**
	 * 供JSP文件直接调用
	 */
	public static final String PURE_OPTIONS = "<option value=\"SysConfigure/roleList.jsp\">管理员主页</option><option value=\"CustomerService/transPlanList.jsp\">客户服务</option><option value=\"ShortDispatch/waitDispatchList.jsp\">车辆调度</option><option value=\"WareHouse/waitDispatchList.jsp\">仓库管理</option>";

	/**
	 * Test
	 */
	public static void main( String[] args ) {
		String selectOptions = EnumUtils.createSelectOptions( arrPage, arrName, true, "0", "角色主页");
		System.out.println( selectOptions );
	}

}