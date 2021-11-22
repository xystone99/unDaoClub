package xms.queries.wareHouse;

import com.undao.database.AbstractQuery;
import com.undao.database.CommonSet;
import com.undao.database.DBUtils;
import com.undao.utils.DateUtils;

import javax.sql.DataSource;
import java.util.HashMap;

public class TruckIdleList extends AbstractQuery {

	private final static int DEFAULT_PAGE_SIZE = 20;
	private final static String MAIN_SQL = "SELECT SQL_CALC_FOUND_ROWS truck_i,idle_k,cur_company,company_zh,truck,plate_number,driver,tel_driver,start_date,end_date,remark,user_a,user_a_zh,input_date FROM view_truck_idle_list ";

	public final static String QP_SORT_TAG = "QpSortTag";
	public final static String COMPANY_ASC = "CompanyAsc";
	public final static String IDLE_DATE_ASC = "IdleDateAsc";
	public final static String USER_ASC = "UserAsc";

	public final static String QP_COMPANY = "fCompany";						//所属公司
	public final static String QP_IDLE_K = "fIdleK";						//闲置类型
	public final static String QP_PLATE = "fPlate";							//车牌
	public final static String QP_DATE1 = "fDate1";							//使用日期1
	public final static String QP_DATE2 = "fDate2";							//使用日期1
	public final static String QP_USER = "fUser";							//登记人

	private final static String[] arr_param_serial = {
			QP_SORT_TAG, QP_COMPANY, QP_IDLE_K, QP_PLATE, QP_DATE1, QP_DATE2, QP_USER
	};

	/**
	 * Constructor
	 */
	public TruckIdleList(DataSource dataSource ) {
		super(dataSource);
		parameterMap = new HashMap<String,String>(arr_param_serial.length);
	}

	@Override
	public void setDefaultParameterValue( ) {
		parameterMap.put( QP_SORT_TAG, IDLE_DATE_ASC );
		parameterMap.put( QP_COMPANY, SQL_ALL );
		parameterMap.put( QP_IDLE_K, SQL_ALL );
		parameterMap.put( QP_PLATE, SQL_EMPTY );
		parameterMap.put( QP_DATE1, DateUtils.formatCurrentDate() );
		parameterMap.put( QP_DATE2, DateUtils.formatDateStepDays( 7 ) );
		parameterMap.put( QP_USER, SQL_EMPTY );
		this.setPageSize( DEFAULT_PAGE_SIZE );
		this.setCurrentPage( 1 );
	}

	@Override
	public String[] getParamSerial() {
		return arr_param_serial;
	}
	
	@Override
	public void executeQuery() {
		StringBuilder bufMainSQL = new StringBuilder( );
		bufMainSQL.append( MAIN_SQL ).append( "WHERE cloud_id='" ).append( getCloudID() ).append( "' ");
		bufMainSQL.append( parseEqualsWhere( "company", parameterMap.get(QP_COMPANY), false ) );
		bufMainSQL.append( parseEqualsWhere( "idle_k", parameterMap.get(QP_IDLE_K), false ) );
		bufMainSQL.append( parseLikeWhere( "plate_number", parameterMap.get(QP_PLATE), SQL_LIKE_BOTH ) );
		bufMainSQL.append( parseLikeWhere( "user_a_zh", parameterMap.get(QP_USER), SQL_LIKE_BOTH ) );

		String date1 = parameterMap.get(QP_DATE1);
		String date2 = parameterMap.get(QP_DATE2);
		bufMainSQL.append( "AND ( (start_date>='" ).append( date1 ).append( "' AND start_date<='" ).append( date2 ).append( "') OR (end_date>='" ).append( date1 ).append( "' AND end_date<='" ).append( date2 ).append( "') ) " );

		String sortTag = parameterMap.get(QP_SORT_TAG);
		if ( sortTag.equals( COMPANY_ASC ) ) {
			bufMainSQL.append( "ORDER BY cur_company ASC, start_date ASC " );
		} else if ( sortTag.equals( IDLE_DATE_ASC ) ) {
			bufMainSQL.append( "ORDER BY start_date ASC " );
		} else if ( sortTag.equals( USER_ASC ) ) {
			bufMainSQL.append( "ORDER BY user_a_zh ASC, start_date ASC " );
		}

		int base = ( getCurrentPage() - 1 ) * getPageSize();
		bufMainSQL.append( "LIMIT " ).append( base ).append( "," ).append( getPageSize() );

		querySQL = bufMainSQL.toString();
		setBaseIndex( base+1 );
		commonSet = DBUtils.executeQuery(getDataSource(), bufMainSQL.toString(), true);
	}

}
