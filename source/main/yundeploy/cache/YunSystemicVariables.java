/**
 * SystemicVariables.java 
 *
 * Created at 2009-7-8, 上午10:44:15
 */
package yundeploy.cache;

import com.undao.cache.SystemicVariables;
import com.undao.database.AbstractDatabase;
import com.undao.database.CommonSet;
import com.undao.database.DBUtils;
import com.undao.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * @author X.Stone
 */
public class YunSystemicVariables extends SystemicVariables {

	/**
	 * 全局参数配置（全局参数不保存数据库，内存中实时管理）
	 */
	private boolean if_global_accept_login = false;
	private boolean if_global_accept_update = false;

	@Override
	public boolean isGlobalAcceptLogin( ) {
		return if_global_accept_login;
	}

	public void setGlobalAcceptLogin( boolean ifAcceptLogin ) {
		if_global_accept_login = ifAcceptLogin;
	}

	@Override
	public boolean isGlobalAcceptUpdate( ) {
		return if_global_accept_update;
	}

	public void setGlobalAcceptUpdate( boolean ifAcceptUpdate ) {
		if_global_accept_update = ifAcceptUpdate;
	}

	/**
	 * 更改参数的SQL
	 */
	public static final String UPDATE_SQL = "UPDATE stc_variables SET var_value=? WHERE cloud=? AND var_name=?";

	/**
	 * 单例模式
	 */
	private static YunSystemicVariables instance;
	private YunSystemicVariables( ) {
	}
	public static YunSystemicVariables getInstance( ) {
		if ( instance == null ) {
			synchronized( YunSystemicVariables.class ) {
				if ( instance == null ) instance = new YunSystemicVariables( );
			}
		}
		return instance;
	}

	/**
	 * 初始化系统参数
	 */
	public void initialVariables( String cloudID ) {
		for ( int j=0; j<arrKey.length; j++ ) {
			StringBuilder bufSQL = new StringBuilder();
			bufSQL.append( "INSERT INTO stc_variables(cloud_id,var_name,var_value)VALUES('" ).append( cloudID ).append( "','" ).append( arrKey[j] ).append( "','" ).append( arrData[j][1] ).append( "');" );
			DBUtils.executeUpdate( getDataSource(), bufSQL.toString() );
		}
	}

	@Override
	public void fixSingletonObject( ) {
		super.fixSingletonObject( );
		setGlobalAcceptLogin( true );
		setGlobalAcceptUpdate( true );
	}

	/**
	 * 更新数据库参数后，需要重新加载。
	 */
	public void refreshSingletonObject( String cloudID) {
		StringBuilder bufSQL = new StringBuilder();
		bufSQL.append( "SELECT var_name,var_value FROM stc_variables WHERE cloudID='" ).append( cloudID ).append( "'" );
		CommonSet dataList = DBUtils.executeQuery( getDataSource(), bufSQL.toString(), false );
		for( int j=0; j<dataList.getRowCount(); j++ ) {
			mapVars.put( cloudID+(String)dataList.getValue(j,"var_name"), (String)dataList.getValue(j,"var_value") );
		}
	}

	/**
	 * 客户端更新系统参数（仅龚类内调用）
	 */
	private void updateVariables( String cloudID, String uKey, String uValue ) {
		DBUtils.prepareUpdate( getDataSource(), UPDATE_SQL, uValue, cloudID, uKey );
		StringBuilder bufKey = new StringBuilder();
		bufKey.append( cloudID ).append( uKey );
		mapVars.put( bufKey.toString(), uValue );
	}

	/**
	 * 允许登录设置
	 */
	@Override
	public boolean isAcceptLogin( String cloudID ) {
		StringBuilder bufKey = new StringBuilder();
		bufKey.append( cloudID ).append( IF_ACCEPT_LOGIN );
		if ( isGlobalAcceptLogin() && mapVars.containsKey( bufKey.toString() ) ) {
			return mapVars.get( bufKey.toString() ).equals( "Y" );
		}
		return false;
	}

	public void setAcceptLogin( String cloudID, String ifAcceptLogin ) {
		updateVariables( cloudID, IF_ACCEPT_LOGIN, ifAcceptLogin );
	}

	/**
	 * 允许更新设置
	 */
	@Override
	public boolean isAcceptUpdate( String cloudID ) {
		StringBuilder bufKey = new StringBuilder();
		bufKey.append( cloudID ).append( IF_ACCEPT_UPDATE );
		return isGlobalAcceptUpdate() && mapVars.get( bufKey.toString() ).equals( "Y" );
	}

	public void setAcceptUpdate( String cloudID, String ifAcceptUpdate ) {
		updateVariables(cloudID, IF_ACCEPT_UPDATE, ifAcceptUpdate );
	}

	/**
	 * 设置公共IP
	 */
	@Override
	public String getCurrentPublicIP( String cloudID ) {
		StringBuilder bufKey = new StringBuilder();
		bufKey.append( cloudID ).append( CUR_PUBLIC_IP );
		return mapVars.get( bufKey.toString() );
	}

	@Override
	public void setCurrentPublicID( String cloudID, String publicIP ) {
		updateVariables( cloudID, CUR_PUBLIC_IP, publicIP );
	}

	/**
	 * 获取月份列表
	 */
	public String getMonthOptions( ) {
		Calendar calendar = Calendar.getInstance( );   
	    calendar.setTime( new Date() );   
	    calendar.add( Calendar.MONTH, 2 );
	    calendar.set( Calendar.DATE, 1 );      
	    calendar.add( Calendar.DATE, -1 ); 
	    	
	    boolean continueCYC = true;
	    StringBuilder buf = new StringBuilder();
	    	
	    while ( continueCYC ) {
	    	String monthValue = DateUtils.formatMonth1( calendar );
	    	String monthTag = DateUtils.formatMonthZh( calendar );
	    	if ( monthValue.compareTo( "202101" ) >= 0 ) {
	    		buf.append( "<option value=\"" ).append( monthValue ).append( "\">" ).append( monthTag ).append( "</option>" );
	    		calendar.add( Calendar.MONTH, -1 );
	    	} else {
	    		continueCYC = false;
	    	}	
	    }
	    return buf.toString( );
	}
	
}
