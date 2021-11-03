/**
 * Created At 2014-2-24 下午03:55:48
 */
package com.undao.cache;

import java.util.*;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import com.undao.database.*;

/**
 * @author Administrator
 *
 */
public class MasterRole extends AbstractDatabase {

	private StringBuilder bufOptions = new StringBuilder( );
	private final static String QUERY_SQL = "SELECT role,ne_zh,href_index FROM tbl_role ORDER BY cloud_id, sort_tag ASC";

	protected HashMap<String,String> mapHref = new HashMap<String,String>();

	private static MasterRole instance = null;
	public MasterRole( ) {
	}
	public static MasterRole getInstance( ) {
		if ( instance == null ) {
			synchronized( MasterRole.class ) {
				if ( instance == null )  instance = new MasterRole( );
			}
		}
		return instance;
	}

	public void fixSingletonObject( ) {
		bufOptions.delete(0, bufOptions.length()-1 );
		mapHref.clear( );

		CommonSet dataList = DBUtils.executeQuery( getDataSource(), QUERY_SQL,false );
		for( int j=0; j<dataList.getRowCount(); j++ ) {
			bufOptions.append( "<options value=\"" ).append(((Integer)dataList.getValue(j,"role")).toString()).append( "\"").append( (String)dataList.getValue(j,"ne_zh") ).append( "</options" );
			mapHref.put( ((Long)dataList.getValue(j,"role")).toString(), (String)dataList.getValue(j,"href_index") );
		}
		
	}
	
	public String getHrefByID( String roleID ) {
    	return mapHref.get( roleID );
	}

	public String getSelectOptions( String cloudID ) {
		return bufOptions.toString( );
	}


}
