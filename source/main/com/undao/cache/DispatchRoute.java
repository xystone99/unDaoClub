/**
 * Created At 2014-2-24 下午03:55:48
 */
package com.undao.cache;

import com.undao.database.AbstractDatabase;
import com.undao.database.CommonSet;
import com.undao.database.DBUtils;
import com.undao.utils.DateUtils;

import java.util.HashMap;

/**
 * @author Administrator
 *
 */
public class DispatchRoute extends AbstractDatabase {

	private final static String QUERY_SQL = "SELECT route_zh FROM tbl_dispatch_route ORDER BY sort_tag ASC";

	private StringBuilder bufOptions = new StringBuilder( );

	private static DispatchRoute instance = null;
	public DispatchRoute( ) {
	}
	public static DispatchRoute getInstance( ) {
		if ( instance == null ) {
			synchronized( DispatchRoute.class ) {
				if ( instance == null )  instance = new DispatchRoute( );
			}
		}
		return instance;
	}

	public void fixSingletonObject( ) {
		bufOptions.delete(0, bufOptions.length()-1 );

		CommonSet dataList = DBUtils.executeQuery( getDataSource(), QUERY_SQL,false );
		for( int j=0; j<dataList.getRowCount(); j++ ) {
			bufOptions.append( "<option value=\"" ).append((String)dataList.getValue(j,"route_zh")).append( "\">").append( (String)dataList.getValue(j,"route_zh") ).append( "</option>" );
		}
	}
	
	public String getSelectOptions( String placeID ) {
		return bufOptions.toString( );
	}

}
