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
public class MasterPost extends AbstractDatabase {

	private StringBuilder bufOptions = new StringBuilder( );
	private final static String QUERY_SQL = "SELECT post, ne_zh, sys_flg FROM mst_post ORDER BY sort_tag ASC";

	protected HashMap<String,String> mapDisplay = new HashMap<String,String>();

	private static MasterPost instance = null;
	public MasterPost( ) {
	}
	public static MasterPost getInstance( ) {
		if ( instance == null ) {
			synchronized( MasterPost.class ) {
				if ( instance == null )  instance = new MasterPost( );
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
				bufOptions.append( "<option value=\"" ).append(((Long)dataList.getValue(j,"post")).toString()).append( "\">").append( (String)dataList.getValue(j,"ne_zh") ).append( "</option>" );
			}
			mapDisplay.put( ((Long)dataList.getValue(j,"post")).toString(), (String)dataList.getValue(j,"ne_zh") );
		}
	}
	
	public String getDisplay( String postID ) {
		return mapDisplay.get( postID );
	}

	public String getSelectOptions( String placeID ) {
		return bufOptions.toString( );
	}


}
