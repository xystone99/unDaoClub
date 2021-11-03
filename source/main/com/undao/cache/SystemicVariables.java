/**
 * SystemicVariables.java 
 *
 * Created at 2009-7-8, 上午10:44:15
 */
package com.undao.cache;

import java.util.*;

import com.undao.database.*;
import com.undao.utils.DateUtils;

/**
 * @author X.Stone
 *
 */
public class SystemicVariables extends AbstractDatabase {

	/**
	 * 系统参数（存储在数据库中）
	 */
	public static final String IF_ACCEPT_LOGIN = "9101";						//允许登陆系统?
	public static final String IF_ACCEPT_UPDATE = "9102";						//允许系统更新?
	public static final String CUR_PUBLIC_IP = "9103";							//当前公网IP
	public static final String CUR_PERFORM_MONTH = "9201";						//当前考核月份
	public static final String PERFORM_CLOSE_MONTH = "9202";					//绩效考核结账月份
	public static final String CUR_SALARY_MONTH = "9203";						//当前发放工资月份
	public static final String SALARY_CLOSE_MONTH = "9204";						//工资结账月份
	public static final String SALARY_UPDATE = "9205";							//是否允许修改工资(Accept,Refused)

	/**
	 * 初始化
	 */
	protected final static String[][] arrData = {		//部分数据结构(编号/排序标记/中文名称)
			{ IF_ACCEPT_LOGIN,		"Y" },
			{ IF_ACCEPT_UPDATE,		"Y" },
			{ CUR_PUBLIC_IP, 		"116.224.53.248" },
			{ CUR_PERFORM_MONTH,	"202101" },
			{ PERFORM_CLOSE_MONTH,	"202012" },
			{ CUR_SALARY_MONTH,		"202101" },
			{ SALARY_CLOSE_MONTH,	"202012" },
			{ SALARY_UPDATE,		"R" }
	};

	/**
	 * 生成初始化的SQL
	 */
	public final static void createInitialSQL( ) {
		StringBuilder bufSQL = new StringBuilder( );
		for ( int j=0; j<arrData.length; j++ ) {
			if (bufSQL.length() > 0) bufSQL.delete( 0, bufSQL.length() );
			bufSQL.append( "INSERT INTO stc_variables(cloud_id,var_name,var_value)VALUES('XYZABC','" ).append( arrData[j][0] ).append( "','" ).append(arrData[j][1] ).append( "');");
			System.out.println( bufSQL.toString() );
		}
	}

	protected final static String[] arrKey = new String[arrData.length];
	protected HashMap<String,String> mapVars = new HashMap<String,String>();
	
	/**
	 * 单例模式
	 */
	private String private_cloud_id = null;
	private static SystemicVariables instance;
	public SystemicVariables( ) {
	}
	public static SystemicVariables getInstance( ) {
		if ( instance == null ) {
			synchronized( SystemicVariables.class ) {
				if ( instance == null ) instance = new SystemicVariables( );
			}
		}
		return instance;
	}

	/**
	 * 系统启动时调入内存
	 */
	public void fixSingletonObject( ) {
		StringBuilder bufKey = new StringBuilder( "0" );
		CommonSet dataList = DBUtils.executeQuery( getDataSource(), "SELECT cloud_id,var_name,var_value FROM stc_variables", false );
		for( int j=0; j<dataList.getRowCount(); j++ ) {
			bufKey.delete( 0, bufKey.length() );
			bufKey.append( (String)dataList.getValue(j,"cloud_id") ).append( (String)dataList.getValue(j,"var_name") );
			mapVars.put( bufKey.toString(), (String)dataList.getValue(j,"var_value") );
		}
	}

	/**
	 * 是否允许登录
	 */
	public boolean isAcceptLogin( String cloudID ) {
		StringBuilder bufKey = new StringBuilder();
		bufKey.append( cloudID ).append( IF_ACCEPT_LOGIN );
		return mapVars.get( bufKey.toString() ).equals( "Y" );
	}

	/**
	 * 是否允许更新
	 */
	public boolean isAcceptUpdate( String cloudID ) {
		StringBuilder bufKey = new StringBuilder();
		bufKey.append( cloudID ).append( IF_ACCEPT_UPDATE );
		return mapVars.get( bufKey.toString() ).equals( "Y" );
	}

	/**
	 * 私有CloudID
	 */
	@Deprecated
	public String getPrivateCloudID( ) {
		return private_cloud_id;
	}

	@Deprecated
	public void setPrivateCloudID( String cloudID ) {
		this.private_cloud_id = cloudID;
	}

	public boolean isGlobalAcceptLogin( ) {
		return true;
	}

	public boolean isGlobalAcceptUpdate( ) {
		return true;
	}

	public String getCurrentPublicIP( String cloudID ) {
		return CUR_PUBLIC_IP;
	}

	public void setCurrentPublicID( String cloudID, String publicIP ) {
		return;
	}

	/**
	 * main test
	 */
	public static void main( String[] args ) {
		SystemicVariables.createInitialSQL( );
	}
}
