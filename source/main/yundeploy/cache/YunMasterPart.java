/**
 * Created At 2014-2-24 下午03:55:48
 */
package yundeploy.cache;

import com.undao.cache.MasterPart;
import com.undao.database.CommonSet;
import com.undao.database.DBUtils;
import com.undao.utils.DateUtils;

import java.util.HashMap;

/**
 * @author Administrator
 *
 */
public class YunMasterPart extends MasterPart {

	private final static String QUERY_SQL = "SELECT cloud_id, part, ne_zh, sys_flg FROM mst_part ORDER BY cloud_id ASC, sort_tag ASC";
	private final static String QUERY_SQL_WITH_CLOUD = "SELECT part, ne_zh, sys_flg FROM mst_part WHERE cloud_id = ? ORDER BY sort_tag ASC";

	private HashMap<String,String> mapSelectOptions = new HashMap<String,String>();

	private static YunMasterPart instance = null;
	private YunMasterPart( ) {
		super();
	}
	public static YunMasterPart getInstance( ) {
		if ( instance == null ) {
			synchronized( YunMasterPart.class ) {
				if ( instance == null )  instance = new YunMasterPart( );
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
		Long partID = (Long)dataList.getValue(0,"part");
		String partName = (String)dataList.getValue(0,"ne_zh");

		StringBuilder bufOptions = new StringBuilder( );
		bufOptions.append( "<option value=\"" ).append( partID.toString() ).append( "\">").append( partName ).append( "</option>" );
		mapDisplay.put( partID.toString(), partName );

		for( int j=1; j<dataList.getRowCount(); j++ ) {
			String curCloudID = (String)dataList.getValue(j,"cloud_id");
			partID = (Long)dataList.getValue(j,"part");
			mapDisplay.put( partID.toString(), (String)dataList.getValue(j,"ne_zh") );

			if ( !curCloudID.equals( preCloudID ) ) {
				mapSelectOptions.put( preCloudID, bufOptions.toString() );
				bufOptions.delete(0, bufOptions.length() );
				preCloudID = curCloudID;
			}
			if ( dataList.getValue(j,"sys_flg").equals( SQL_NORMAL ) ) {
				bufOptions.append( "<option value=\"" ).append( partID.toString() ).append( "\">").append( (String)dataList.getValue(j,"ne_zh") ).append( "</option>" );
			}
		}

		mapSelectOptions.put( preCloudID, bufOptions.toString() );
		System.out.println( "YunMasterPart Fixed At " + DateUtils.formatCurrentDateTime() );
	}

    /**
     * 重置指定CloudID的部门缓存
	 */
	public void fixSingletonObject( String cloudID ) {
		CommonSet dataList = DBUtils.prepareQuery( getDataSource(), QUERY_SQL_WITH_CLOUD, cloudID );
		StringBuilder bufOptions = new StringBuilder( );

		for( int j=1; j<dataList.getRowCount(); j++ ) {
			Long partID = (Long)dataList.getValue(j,"part");
			mapDisplay.put( partID.toString(), (String)dataList.getValue(j,"ne_zh") );
			if ( dataList.getValue(j,"sys_flg").equals( SQL_NORMAL ) ) {
				bufOptions.append( "<option value=\"" ).append( partID.toString() ).append( "\">").append( (String)dataList.getValue(j,"ne_zh") ).append( "</option>" );
			}
		}

		mapSelectOptions.put( cloudID, bufOptions.toString() );
	}

	@Override
	public String getSelectOptions( String cloudID ) {
		return mapSelectOptions.get( cloudID );
	}
	
}
