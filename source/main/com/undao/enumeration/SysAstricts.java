/**
 * SysAstricts.java
 *
 * Created at 2009-8-13, 下午01:35:00
 */
package com.undao.enumeration;

import java.util.HashMap;

import com.undao.utils.EnumUtils;

/**
 * 系统权限列表
 * @author Administrator
 */
public class SysAstricts {
	
	/**
	 * 权限常量
	 */
	public static final String QX_SYS_CONFIGURE = "7011";				//系统配置
	public static final String QX_ASTRICT_CONFIGURE = "7012";			//权限设置
	public static final String QX_ACCESS_REFUSED = "7013";				//特殊权限(拒绝访问)

	public static final String QX_CUS_READ = "7111";					//浏览客户计划
	public static final String QX_CUS_UPDATE = "7112";					//创建、更新客户计划
	public static final String QX_CUS_EXCEPTION = "7113";				//处理客户计划异常操作

	public static final String QX_DISPATCH_READ = "7211";				//浏览调度信息
	public static final String QX_DISPATCH_UPDATE = "7212";				//创建、更新车次
	public static final String QX_DISPATCH_EXCEPTION = "7213";			//处理调度异常操作

	public static final String QX_WH_READ = "7311";						//浏览仓库信息
	public static final String QX_WH_UPDATE = "7312";					//创建、更新仓库操作
	public static final String QX_WH_EXCEPTION = "7313";				//处理仓库异常操作

	private static String[] arrKey = {
			QX_SYS_CONFIGURE, QX_ASTRICT_CONFIGURE,
			QX_CUS_READ, QX_CUS_UPDATE, QX_CUS_EXCEPTION,
			QX_DISPATCH_READ, QX_DISPATCH_UPDATE, QX_DISPATCH_EXCEPTION,
			QX_WH_READ, QX_WH_UPDATE, QX_WH_EXCEPTION
	};
	
	private static String[] arrName = {
			"系统配置", "权限设置",
			"浏览客户计划", "创建、更新客户计划", "处理客户计划异常操作",
			"浏览调度信息", "创建、更新车次", "处理调度异常操作",
			"浏览仓库信息", "创建、更新仓库操作", "处理仓库异常操作"
	};
	
	final static HashMap<String,String> mapData = new HashMap<String,String>();
	static {
		for ( int j=0; j<arrKey.length; j++ ) {
			mapData.put( arrKey[j], arrName[j] );
		}
	}
	
	/**
	 * 权限个数
	 */
	public static int size( ) {
		return arrKey.length;
	}
	
	/**
	 * 权限值（存储于数据库中）
	 */
	public static String getKey( int index ) {
		return arrKey[index];
	}

	/**
	 * 权限名称（在管理界面显示）
	 */
	public static String getDisplay( int index ) {
		return arrName[index];
	}
	
	/**
	 * 
	 * @param filter	: true表示过滤掉astricts中包含的权限, false表示保留astricts中包含的权限
	 * @param astricts	: 需要过滤的权限列表,使用-分隔开
	 * @return
	 */
	public static String getFilterOptions( String astricts, boolean filter ) {
		StringBuilder bufOptions = new StringBuilder();
		if ( filter ) {
			for ( int j=0; j<arrKey.length; j++ ) {
				if ( astricts.indexOf( arrKey[j] ) < 0 ) {
					bufOptions.append( "<option value=\"" ).append( arrKey[j] ).append( "\">" ).append( arrName[j] ).append( "</option>" );
				}
			}
		} else {
			for ( int j=0; j<arrKey.length; j++ ) {
				if ( astricts.indexOf( arrKey[j] ) >= 0 ) {
					bufOptions.append( "<option value=\"" ).append( arrKey[j] ).append( "\">" ).append( arrName[j] ).append( "</option>" );
				}
			}
		}
		return bufOptions.toString();
	}

	/**
	 * 供JSP文件直接调用
	 */
	public static final String PURE_OPTIONS = "<option value=\"7011\" selected>系统配置</option><option value=\"7012\">权限设置</option><option value=\"7111\">浏览客户计划</option><option value=\"7112\">创建、更新客户计划</option><option value=\"7113\">处理客户计划异常操作</option><option value=\"7211\">浏览调度信息</option><option value=\"7212\">创建、更新车次</option><option value=\"7213\">处理调度异常操作</option><option value=\"7311\">浏览仓库信息</option><option value=\"7312\">创建、更新仓库操作</option><option value=\"7313\">处理仓库异常操作</option>";
	
	/**
	 * Test
	 */
	public static void main( String[] args ) {
		String selectOptions = EnumUtils.createSelectOptions( arrKey, arrName, false, "All", "全部权限");
		System.out.println( selectOptions );
	}

}