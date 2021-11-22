/**
 * Created At 2014-2-24 下午02:47:40
 */
package com.undao.cache;

import java.util.*;

import com.undao.database.*;

/**
 * @author Administrator
 *
 */
public class RoleAstricts extends AbstractDatabase {

	private static final String GLOBAL_SQL = "SELECT role,astrict FROM stc_role_astricts";
	
	
	private static RoleAstricts instance = null;
	public RoleAstricts( ) {
	}
	public static RoleAstricts getInstance( ) {
		if ( instance == null ) {
			synchronized( RoleAstricts.class ) {
				if ( instance == null ) instance = new RoleAstricts( );
			}
		}
		return instance;
	}
	
	private HashMap<String,HashSet<String>> role_astricts = new HashMap<String,HashSet<String>>();

	public void fixSingletonObject( ) {
		role_astricts.clear( );		
		CommonSet dataList = DBUtils.executeQuery( getDataSource(), GLOBAL_SQL, false );
		for( int j=0; j<dataList.getRowCount(); j++ ) {
			Long role = (Long)dataList.getValue( j,"role" );
			if ( role_astricts.containsKey( role.toString() ) ) {
				role_astricts.get( role.toString() ).add( (String)dataList.getValue(j,"astrict") );
			} else {
				HashSet<String> astricts = new HashSet<String>();
				astricts.add( (String)dataList.getValue(j,"astrict") );
				role_astricts.put( role.toString(), astricts );
			}  
		}	
	}
	
	/**
	 * 重新加载权限列表
	 */
	public void reload( ) {
		fixSingletonObject( );
	}
	
	/**
	 * 验证某角色是否包含某个权限
	 * @param role			：待验证的角色
	 * @param needAstrict	：需要包含的权限
	 * @return
	 */
	public boolean checkPassport( String role, String needAstrict ) {
		if ( needAstrict == null ) {
			return true;
		}
		HashSet<String> astricts = role_astricts.get( role );
		if ( astricts == null ) {
			return false;
		}
		return astricts.contains( needAstrict );
	}
	
	/**
	 * 验证某角色是否包含权限列表中的某一个权限
	 * @param role			：待验证的角色
	 * @param needAstricts	：需要包含的权限列表
	 * @return
	 */
	public boolean checkPassport( String role, String[] needAstricts ) {
		System.out.println( "Need:" + Arrays.toString( needAstricts ) );
		System.out.println( "Have1:" + getAstrictsOfRole(role) );

		if ( needAstricts == null ) {
			return true;
		}
		HashSet<String> astricts = role_astricts.get( role );
		for ( String a : astricts ) {
			System.out.println( "Have2:" + a );
		}
		if ( astricts == null ) {
			return false;
		}
		for ( int index = 0; index < needAstricts.length; index ++ ) {
			if ( astricts.contains( needAstricts[index] ) ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 返回某角色包含的权限列表
	 * @param role	：角色代码
	 * @return
	 */
	public String getAstrictsOfRole( String role ) {
		if ( role == null ) {
			return "";
		}
		HashSet<String> list = role_astricts.get( role );
		if ( list == null ) {
			return "";
		}
		StringBuilder buf = new StringBuilder( );
		for( String astrit : list ) {
			buf.append( buf.length()==0 ? "" : "=" ).append( astrit );
		}
		return buf.toString( );
	}
	
}
