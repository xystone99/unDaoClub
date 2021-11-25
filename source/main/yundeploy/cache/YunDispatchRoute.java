/**
 * Created At 2014-2-24 下午03:55:48
 */
package yundeploy.cache;

import com.undao.cache.DispatchRoute;
import com.undao.database.CommonSet;
import com.undao.database.DBUtils;
import com.undao.utils.DateUtils;

import java.util.HashMap;

/**
 * @author Administrator
 *
 */
public class YunDispatchRoute extends DispatchRoute {

	private final static String QUERY_SQL = "SELECT cloud_id, route_zh FROM tbl_dispatch_route ORDER BY cloud_id ASC, sort_tag ASC";
	private final static String QUERY_SQL_WITH_CLOUD = "SELECT route_zh FROM tbl_dispatch_route WHERE cloud_id = ? ORDER BY sort_tag ASC";

	private HashMap<String,String> mapSelectOptions = new HashMap<String,String>();

	private static YunDispatchRoute instance = null;
	private YunDispatchRoute( ) {
		super();
	}
	public static YunDispatchRoute getInstance( ) {
		if ( instance == null ) {
			synchronized( YunDispatchRoute.class ) {
				if ( instance == null )  instance = new YunDispatchRoute( );
			}
		}
		return instance;
	}

	@Override
	public void fixSingletonObject( ) {
		mapSelectOptions.clear( );

		CommonSet dataList = DBUtils.executeQuery( getDataSource(), QUERY_SQL,false );
		if ( dataList.getRowCount() <= 0 ) {
			return;
		}

		String preCloudID = (String)dataList.getValue(0,"cloud_id");
		String routeName = (String)dataList.getValue("route_zh");

		StringBuilder bufOptions = new StringBuilder( );
		bufOptions.append( "<option value=\"" ).append( routeName ).append( "\">").append( routeName ).append( "</option>" );

		for( int j=1; j<dataList.getRowCount(); j++ ) {
			String curCloudID = (String)dataList.getValue(j,"cloud_id");

			if ( !curCloudID.equals( preCloudID ) ) {
				mapSelectOptions.put( preCloudID, bufOptions.toString() );
				bufOptions.delete(0, bufOptions.length() );
				preCloudID = curCloudID;
			}
			bufOptions.append( "<option value=\"" ).append( (String)dataList.getValue(j,"route_zh") ).append( "\">").append( (String)dataList.getValue(j,"route_zh") ).append( "</option>" );
		}

		mapSelectOptions.put( preCloudID, bufOptions.toString() );
		System.out.println( "YunDispatchRoute Fixed At " + DateUtils.formatCurrentDateTime() );
	}

	/**
	 * 重置指定CloudID的公司缓存
	 */
	public void fixSingletonObject( String cloudID ) {
		CommonSet dataList = DBUtils.prepareQuery( getDataSource(), QUERY_SQL_WITH_CLOUD, cloudID );
		StringBuilder bufOptions = new StringBuilder( );

		for( int j=1; j<dataList.getRowCount(); j++ ) {
			bufOptions.append( "<option value=\"" ).append( (String)dataList.getValue(j,"route_zh") ).append( "\">").append( (String)dataList.getValue(j,"route_zh") ).append( "</option>" );
		}

		mapSelectOptions.put( cloudID, bufOptions.toString() );
	}

	@Override
	public String getSelectOptions( String cloudID ) {
		return mapSelectOptions.get( cloudID );
	}
	
}
