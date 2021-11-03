/**
 * Created At 2014-2-24 下午03:55:48
 */
package yundeploy.cache;

import com.undao.cache.MasterRole;
import com.undao.database.AbstractMaster;
import com.undao.database.CommonSet;
import com.undao.database.DBUtils;

import java.util.HashMap;

/**
 * @author Administrator
 *
 */
public class YunMasterRole extends MasterRole {

	private HashMap<String,String> mapDisplay = new HashMap<String,String>();
	private final static String QUERY_SQL = "SELECT cloud_id,role,ne_zh,href_index FROM tbl_role ORDER BY cloud_id, sort_tag ASC";

	private static YunMasterRole instance = null;
	private YunMasterRole( ) {
		super();
	}
	public static YunMasterRole getInstance( ) {
		if ( instance == null ) {
			synchronized( YunMasterRole.class ) {
				if ( instance == null )  instance = new YunMasterRole( );
			}
		}
		return instance;
	}

	@Override
	public void fixSingletonObject( ) {
		mapDisplay.clear( );
		mapHref.clear( );

		CommonSet dataList = DBUtils.executeQuery( getDataSource(), QUERY_SQL,false );
		String preCloudID = SQL_ZERO;
		StringBuilder bufOptions = new StringBuilder( );
		for( int j=0; j<dataList.getRowCount(); j++ ) {
			String curCloudID = (String)dataList.getValue(j,"cloud_id");
			if ( !curCloudID.equals( preCloudID ) ) {
				if ( !preCloudID.equals( SQL_ZERO ) ) {
					mapDisplay.put( preCloudID, bufOptions.toString() );
				}
				bufOptions.delete(0, bufOptions.length() );
				preCloudID = curCloudID;
			}
			bufOptions.append( "<options value=\"" ).append(((Long)dataList.getValue(j,"role")).toString()).append( "\"").append( (String)dataList.getValue(j,"ne_zh") ).append( "</options" );
			mapHref.put( ((Long)dataList.getValue(j,"role")).toString(), (String)dataList.getValue(j,"href_index") );
		}
		if ( dataList.getRowCount() > 0 ) {
			mapDisplay.put( preCloudID, bufOptions.toString() );
		}

	}

	@Override
	public String getSelectOptions( String cloudID ) {
		return mapDisplay.get( cloudID );
	}
	
}
