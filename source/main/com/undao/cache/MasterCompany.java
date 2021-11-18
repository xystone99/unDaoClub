/**
 * Created At 2014-2-24 下午03:55:48
 */
package com.undao.cache;

import com.undao.database.AbstractDatabase;
import com.undao.database.CommonSet;
import com.undao.database.DBUtils;

import java.util.HashMap;

/**
 * @author Administrator
 *
 */
public class MasterCompany extends AbstractDatabase {

	private final static String QUERY_SQL = "SELECT company,ne_zh,sys_flg FROM mst_company ORDER BY sort_tag ASC";

	private StringBuilder bufOptions = new StringBuilder( );
	protected HashMap<String,String> mapDisplay = new HashMap<String,String>();

	private static MasterCompany instance = null;
	public MasterCompany( ) {
	}
	public static MasterCompany getInstance( ) {
		if ( instance == null ) {
			synchronized( MasterCompany.class ) {
				if ( instance == null )  instance = new MasterCompany( );
			}
		}
		return instance;
	}

	public void fixSingletonObject( ) {
		bufOptions.delete(0, bufOptions.length()-1 );
		mapDisplay.clear( );

		CommonSet dataList = DBUtils.executeQuery( getDataSource(), QUERY_SQL,false );
		for( int j=0; j<dataList.getRowCount(); j++ ) {
			if ( dataList.getValue(j,"sys_flg").equals( SQL_NORMAL ) ) {
				bufOptions.append( "<options value=\"" ).append(((Long)dataList.getValue(j,"company")).toString()).append( "\"").append( (String)dataList.getValue(j,"ne_zh") ).append( "</options" );
			}
			mapDisplay.put( ((Long)dataList.getValue(j,"company")).toString(), (String)dataList.getValue(j,"ne_zh") );
		}
		
	}
	
	public String getDisplay( String companyID ) {
    	return mapDisplay.get( companyID );
	}

	public String getSelectOptions( String placeID ) {
		return bufOptions.toString( );
	}


}
