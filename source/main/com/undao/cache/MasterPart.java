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
public class MasterPart extends AbstractDatabase {

	private StringBuilder bufOptions = new StringBuilder( );
	private final static String QUERY_SQL = "SELECT part,ne_zh,sys_flg FROM mst_part ORDER BY sort_tag ASC";

	protected HashMap<String,String> mapPart = new HashMap<String,String>();

	private static MasterPart instance = null;
	public MasterPart( ) {
	}
	public static MasterPart getInstance( ) {
		if ( instance == null ) {
			synchronized( MasterPart.class ) {
				if ( instance == null )  instance = new MasterPart( );
			}
		}
		return instance;
	}

	public void fixSingletonObject( ) {
		bufOptions.delete(0, bufOptions.length()-1 );
		mapPart.clear( );

		CommonSet dataList = DBUtils.executeQuery( getDataSource(), QUERY_SQL,false );
		for( int j=0; j<dataList.getRowCount(); j++ ) {
			if ( dataList.getValue(j,"sys_flg").equals( SQL_NORMAL ) ) {
				bufOptions.append( "<options value=\"" ).append(((Long)dataList.getValue(j,"part")).toString()).append( "\"").append( (String)dataList.getValue(j,"ne_zh") ).append( "</options" );
			}
			mapPart.put( ((Long)dataList.getValue(j,"part")).toString(), (String)dataList.getValue(j,"ne_zh") );
		}
		
	}
	
	public String getCompanyName( String company ) {
    	return mapPart.get( company );
	}

	public String getSelectOptions( String placeID ) {
		return bufOptions.toString( );
	}


}
