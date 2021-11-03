/**
 * Created At 2014-2-23 上午11:22:45
 */
package xms.queries.sysConfigure;

import java.util.HashMap;

import javax.sql.DataSource;

import com.undao.database.AbstractQuery;
import com.undao.database.DBUtils;

/**
 * @author Administrator
 *
 */
public class RoleList extends AbstractQuery {
	
	private final static int DEFAULT_PAGE_SIZE = 20;
	private final static String QUERY_SQL = "SELECT SQL_CALC_FOUND_ROWS role,ne_zh,sort_tag,href_index,remark,input_date FROM tbl_role ";

	public final static String QP_SORT_TAG = "QpSortTag";
	public final static String SORT_TAG_ASC = "SortTagAsc";
	public final static String INPUT_DATE_DESC = "InputDateDesc";

	public final static String QP_NAME_ZH = "QpPostZh";
	
	private final static String[] arr_param_serial = { QP_SORT_TAG, QP_NAME_ZH	};
	
	/**
	 * Constructor
	 */
	public RoleList(DataSource ds) {
		super(ds);
		parameterMap = new HashMap<String,String>(arr_param_serial.length);
		parameterMap.put( QP_SORT_TAG, SORT_TAG_ASC );
		parameterMap.put( QP_NAME_ZH, "" );
		this.setPageSize( DEFAULT_PAGE_SIZE );
		this.setCurrentPage( 1 );
	}
	
	@Override
	public String[] getParamSerial( ) {
		return arr_param_serial;
	}
	
	@Override
	public void executeQuery() {
		StringBuilder bufSQL = new StringBuilder( );
		bufSQL.append( QUERY_SQL ).append( "WHERE cloud_id='" ).append( getCloudID() ).append( "' ");
		bufSQL.append( parseLikeWhere( "ne_zh", parameterMap.get(QP_NAME_ZH), SQL_LIKE_BOTH ) );

		String sortTag = parameterMap.get(QP_SORT_TAG);
		if ( sortTag.equals( SORT_TAG_ASC ) ) {
			bufSQL.append( "ORDER BY sort_tag ASC " );
		} else if ( sortTag.equals( INPUT_DATE_DESC ) ) {
			bufSQL.append( "ORDER BY input_date DESC " );
		}
		
		int base = ( getCurrentPage() - 1 ) * getPageSize();
		bufSQL.append( "LIMIT " ).append( base ).append( "," ).append( getPageSize() );
		
		querySQL = bufSQL.toString();
		setBaseIndex( base+1 );
		commonSet = DBUtils.executeQuery(getDataSource(), bufSQL.toString(), true);
	}
	
}
