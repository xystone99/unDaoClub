package xms.queries.shortDispatch;

import com.undao.database.AbstractQuery;
import com.undao.database.CommonSet;
import com.undao.database.DBUtils;
import com.undao.utils.CommonUtils;
import com.undao.utils.DateUtils;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.HashMap;

public class TruckIdleSummary extends AbstractQuery {

	private final static int DEFAULT_PAGE_SIZE = 20;
	private final static String SUB_SQL = "SELECT truck_i,idle_k,cur_company,company_zh,truck,plate_number,driver,tel_driver,start_date,end_date,remark,user_a,user_a_zh,input_date FROM view_truck_idle_list ";
	private final static String MAIN_SQL = "SELECT SQL_CALC_FOUND_ROWS cur_company,truck,GROUP_CONCAT(DISTINCT company_zh) AS company_zh,GROUP_CONCAT(DISTINCT plate_number) AS plate_number FROM view_truck_idle_list ";

	public final static String QP_SORT_TAG = "QpSortTag";
	public final static String COMPANY_ASC = "CompanyAsc";
	public final static String PLATE_ASC = "PlateAsc";

	public final static String QP_COMPANY = "fCompany";						//所属公司
	public final static String QP_IDLE_K = "fIdleK";						//闲置类型
	public final static String QP_PLATE = "fPlate";							//车牌
	public final static String QP_DATE1 = "fDate1";							//使用日期1
	public final static String QP_DATE2 = "fDate2";							//使用日期1

	private final static String[] arr_param_serial = {
			QP_SORT_TAG, QP_COMPANY, QP_IDLE_K, QP_PLATE, QP_DATE1, QP_DATE2
	};

	private HashMap<String,String> mapIdle = null;
	public String getIdleRemark( String idleKey ) {
		if ( mapIdle == null ) {
			return SQL_EMPTY;
		}
		if ( mapIdle.containsKey( idleKey ) ) {
			return mapIdle.get( idleKey );
		}
		return SQL_EMPTY;
	}

	/**
	 * Constructor
	 */
	public TruckIdleSummary(DataSource dataSource ) {
		super(dataSource);
		parameterMap = new HashMap<String,String>(arr_param_serial.length);
	}

	@Override
	public void setDefaultParameterValue( ) {
		parameterMap.put( QP_SORT_TAG, COMPANY_ASC );
		parameterMap.put( QP_COMPANY, SQL_ALL );
		parameterMap.put( QP_IDLE_K, SQL_ALL );
		parameterMap.put( QP_PLATE, SQL_EMPTY );
		parameterMap.put( QP_DATE1, DateUtils.formatDateStepDays( 1 ) );
		parameterMap.put( QP_DATE2, DateUtils.formatDateStepDays( 7 ) );
		this.setPageSize( DEFAULT_PAGE_SIZE );
		this.setCurrentPage( 1 );
	}

	@Override
	public String[] getParamSerial() {
		return arr_param_serial;
	}
	
	@Override
	public void executeQuery() {
		StringBuilder bufConditions = new StringBuilder( );
		bufConditions.append( "WHERE cloud_id='" ).append( getCloudID() ).append( "' ");
		bufConditions.append( parseEqualsWhere( "cur_company", parameterMap.get(QP_COMPANY), false ) );
		bufConditions.append( parseEqualsWhere( "idle_k", parameterMap.get(QP_IDLE_K), false ) );
		bufConditions.append( parseLikeWhere( "plate_number", parameterMap.get(QP_PLATE), SQL_LIKE_BOTH ) );

		String date1 = parameterMap.get(QP_DATE1);
		String date2 = parameterMap.get(QP_DATE2);
		bufConditions.append( "AND ( (start_date>='" ).append( date1 ).append( "' AND start_date<='" ).append( date2 ).append( "') OR (end_date>='" ).append( date1 ).append( "' AND end_date<='" ).append( date2 ).append( "') ) " );

		StringBuilder bufMainSQL = new StringBuilder( MAIN_SQL );
		bufMainSQL.append( bufConditions ).append( "GROUP BY cur_company, truck " );
		String sortTag = parameterMap.get(QP_SORT_TAG);
		if ( sortTag.equals( COMPANY_ASC ) ) {
			bufMainSQL.append( "ORDER BY cur_company ASC, truck ASC " );
		} else if ( sortTag.equals( PLATE_ASC ) ) {
			bufMainSQL.append( "ORDER BY truck ASC " );
		}
		int base = ( getCurrentPage() - 1 ) * getPageSize();
		bufMainSQL.append( "LIMIT " ).append( base ).append( "," ).append( getPageSize() );

		querySQL = bufMainSQL.toString();
		setBaseIndex( base+1 );
		commonSet = DBUtils.executeQuery(getDataSource(), bufMainSQL.toString(), true);

		if ( commonSet.getRowCount() > 0 ) {
			StringBuilder bufTrucks = new StringBuilder( );
			for ( int j=0; j<commonSet.getRowCount(); j++ ) {
				bufTrucks.append( ((Long)commonSet.getValue(j,"truck")).toString() ).append( "," );
			}

			StringBuilder bufSubSQL = new StringBuilder( SUB_SQL );
			bufSubSQL.append( bufConditions.toString() ).append( " AND truck IN (" ).append( bufTrucks.deleteCharAt(bufTrucks.length()-1 ).toString() ).append( ")" );
			CommonSet dataSet = DBUtils.executeQuery(getDataSource(), bufSubSQL.toString(), false);

			mapIdle = new HashMap<String,String>();
			StringBuilder bufKey = new StringBuilder( );
			StringBuilder bufRemark = new StringBuilder( );
			LocalDate fromDate;
			LocalDate toDate;

			for ( int j=0; j<dataSet.getRowCount(); j++ ) {
				bufRemark.delete(0, bufRemark.length() );
				int compID = ((Long)dataSet.getValue(j,"cur_company" )).intValue();
				int truckID = ((Long)dataSet.getValue(j,"truck" )).intValue();
				fromDate = ((java.sql.Date)dataSet.getValue(j,"start_date" )).toLocalDate();
				toDate = ((java.sql.Date)dataSet.getValue(j,"end_date" )).toLocalDate();

				if ( ((String)dataSet.getValue(j,"tel_driver")).length() > 0 ) {
					bufRemark.append( "&nbsp;" ).append( dataSet.getValue(j,"tel_driver") ).append( "<br/>" );
				}
				if ( ((String)dataSet.getValue(j,"remark")).length() > 0 ) {
					bufRemark.append( "&nbsp;" ).append( dataSet.getValue(j,"remark") ).append( "<br/>" );
				}
				bufRemark.append( "&nbsp;" ).append(dataSet.getValue(j,"user_a_zh")).append( SQL_SPACE ).append( DateUtils.formatDateTime3(dataSet.getValue(j,"input_date")));
				while( fromDate.isBefore(toDate) || fromDate.isEqual(toDate) ) {
					bufKey.delete(0, bufKey.length() );
					bufKey.append( compID ).append( SQL_HYPHEN ).append( truckID ).append( SQL_HYPHEN ).append( DateUtils.formatLocal_mmdd(fromDate) );
					mapIdle.put( bufKey.toString(), bufRemark.toString() );
					fromDate = fromDate.plusDays( 1 );
				}
			}
		}
	}

}
