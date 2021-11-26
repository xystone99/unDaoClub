/**
 * Created At 2014-2-24 下午03:55:48
 */
package com.undao.cache;

import java.util.*;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import com.undao.database.*;
import com.undao.utils.DateUtils;

/**
 * @author Administrator
 *
 */
public class MasterRole extends AbstractDatabase {

	private final static String QUERY_SQL = "SELECT role, ne_zh, href_index FROM tbl_role ORDER BY sort_tag ASC";

	private StringBuilder bufOptions = new StringBuilder( );
	protected HashMap<String,String> mapDisplay = new HashMap<String,String>();
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
		mapDisplay.clear( );

		CommonSet dataList = DBUtils.executeQuery( getDataSource(), QUERY_SQL,false );
		for( int j=0; j<dataList.getRowCount(); j++ ) {
			Long roleID = (Long)dataList.getValue(j,"role");
			String roleName = (String)dataList.getValue(j,"ne_zh");
			bufOptions.append( "<option value=\"" ).append( roleID.toString() ).append( "\">").append( roleName ).append( "</option>" );
			mapDisplay.put( roleID.toString(), roleName );
			mapHref.put( roleID.toString(), (String)dataList.getValue(j,"href_index") );
		}
	}

	public String getDisplay( String roleID ) {
		return mapDisplay.get( roleID );
	}

	public String getDisplay( Long roleID ) {
		if ( roleID.intValue() == 0 ) {
			return SQL_EMPTY;
		}
		return mapDisplay.get( roleID.toString() );
	}

	public String getHrefByID( String roleID ) {
		return mapHref.get( roleID );
	}

	public String getSelectOptions( String placeID ) {
		return bufOptions.toString( );
	}


}
