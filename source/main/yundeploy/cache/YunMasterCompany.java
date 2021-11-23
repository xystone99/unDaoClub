/**
 * Created At 2014-2-24 下午03:55:48
 */
package yundeploy.cache;

import com.undao.cache.MasterCompany;
import com.undao.database.CommonSet;
import com.undao.database.DBUtils;

import java.util.HashMap;

/**
 * @author Administrator
 *
 */
public class YunMasterCompany extends MasterCompany {

	private final static String QUERY_SQL = "SELECT cloud_id, company, ne_zh, sys_flg FROM mst_company ORDER BY cloud_id ASC, sort_tag ASC";
	private final static String QUERY_SQL_WITH_CLOUD = "SELECT company, ne_zh, sys_flg FROM mst_company WHERE cloud_id = ? ORDER BY sort_tag ASC";

	private HashMap<String,String> mapSelectOptions = new HashMap<String,String>();

	private static YunMasterCompany instance = null;
	private YunMasterCompany( ) {
		super();
	}
	public static YunMasterCompany getInstance( ) {
		if ( instance == null ) {
			synchronized( YunMasterCompany.class ) {
				if ( instance == null )  instance = new YunMasterCompany( );
			}
		}
		return instance;
	}

	@Override
	public void fixSingletonObject( ) {
		mapDisplay.clear( );
		mapSelectOptions.clear( );

		CommonSet dataList = DBUtils.executeQuery( getDataSource(), QUERY_SQL,false );
		if ( dataList.getRowCount() <= 0 ) {
			return;
		}

		String preCloudID = (String)dataList.getValue(0,"cloud_id");
		Long companyID = (Long)dataList.getValue(0,"company");
		String companyName = (String)dataList.getValue(0,"ne_zh");

		StringBuilder bufOptions = new StringBuilder( );
		bufOptions.append( "<option value=\"" ).append( companyID.toString() ).append( "\">").append( companyName ).append( "</option>" );
		mapDisplay.put( companyID.toString(), companyName );

		for( int j=1; j<dataList.getRowCount(); j++ ) {
			String curCloudID = (String)dataList.getValue(j,"cloud_id");
			companyID = (Long)dataList.getValue(j,"company");
			mapDisplay.put( companyID.toString(), (String)dataList.getValue(j,"ne_zh") );

			if ( !curCloudID.equals( preCloudID ) ) {
				mapSelectOptions.put( preCloudID, bufOptions.toString() );
				bufOptions.delete(0, bufOptions.length() );
				preCloudID = curCloudID;
			}
			if ( dataList.getValue(j,"sys_flg").equals( SQL_NORMAL ) ) {
				bufOptions.append( "<option value=\"" ).append( companyID.toString() ).append( "\">").append( (String)dataList.getValue(j,"ne_zh") ).append( "</option>" );
			}
		}

		mapSelectOptions.put( preCloudID, bufOptions.toString() );
	}

	/**
	 * 重置指定CloudID的公司缓存
	 */
	public void fixSingletonObject( String cloudID ) {
		CommonSet dataList = DBUtils.prepareQuery( getDataSource(), QUERY_SQL_WITH_CLOUD, cloudID );
		StringBuilder bufOptions = new StringBuilder( );

		for( int j=1; j<dataList.getRowCount(); j++ ) {
			Long companyID = (Long)dataList.getValue(j,"company");
			mapDisplay.put( companyID.toString(), (String)dataList.getValue(j,"ne_zh") );
			if ( dataList.getValue(j,"sys_flg").equals( SQL_NORMAL ) ) {
				bufOptions.append( "<option value=\"" ).append( companyID.toString() ).append( "\">").append( (String)dataList.getValue(j,"ne_zh") ).append( "</option>" );
			}
		}

		mapSelectOptions.put( cloudID, bufOptions.toString() );
	}

	@Override
	public String getSelectOptions( String cloudID ) {
		return mapSelectOptions.get( cloudID );
	}
	
}
