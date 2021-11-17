package xms.queries.customerService;

import com.undao.database.AbstractQuery;
import com.undao.database.DBUtils;
import com.undao.utils.DateUtils;

import javax.sql.DataSource;
import java.util.HashMap;

public class TransPlanAlert extends AbstractQuery {

	private final static int DEFAULT_PAGE_SIZE = 20;
	private final static String QUERY_SQL = "SELECT SQL_CALC_FOUND_ROWS trans_p,plan_k,plan_date,obj_short,time_level,ne_recycle,ne_zh1,window_1,remark_1,ne_zh2,window_2,remark_2,qty_w,qty_v,qty_meter,qty_meter_r,route_zh,dispatch_remark,wh_remark,user_zh,input_date FROM view_trans_plan_list ";

	public final static String QP_SORT_TAG = "QpSortTag";
	public final static String INPUT_DATE_ASC = "InputDateAsc";
	public final static String INPUT_DATE_DESC = "InputDateDesc";

	public final static String QP_IF_HIDE = "ifHide";						//隐藏已安排计划
	public final static String QP_DATE1 = "fDate1";							//发货日期1
	public final static String QP_DATE2 = "fDate2";							//发货日期2
	public final static String QP_PLAN_K = "fPlanK";						//计划类型
	public final static String QP_OBJECT_P = "fObjectP";					//客户简称
	public final static String QP_USER_ZH = "fUserZh";						//登记人姓名
	public final static String QP_LOAD_NAME = "fLoadName";					//装卸地名称

	private final static String[] arr_param_serial = {
			QP_SORT_TAG, QP_IF_HIDE, QP_PLAN_K, QP_OBJECT_P, QP_DATE1, QP_DATE2, QP_LOAD_NAME, QP_USER_ZH
	};

	/**
	 * Constructor
	 */
	public TransPlanAlert(DataSource dataSource ) {
		super(dataSource);
		parameterMap = new HashMap<String,String>(arr_param_serial.length);
	}

	@Override
	public void setDefaultParameterValue( ) {
		parameterMap.put( QP_SORT_TAG, INPUT_DATE_ASC );
		parameterMap.put( QP_IF_HIDE, SQL_YES );
		parameterMap.put( QP_PLAN_K, SQL_ALL );
		parameterMap.put( QP_OBJECT_P, SQL_EMPTY );
		parameterMap.put( QP_DATE1, DateUtils.formatDateStepDays(1) );
		parameterMap.put( QP_DATE2, DateUtils.formatDateStepDays(1) );
		parameterMap.put( QP_LOAD_NAME, SQL_EMPTY );
		parameterMap.put( QP_USER_ZH, SQL_EMPTY );
		this.setPageSize( DEFAULT_PAGE_SIZE );
		this.setCurrentPage( 1 );
	}

	@Override
	public String[] getParamSerial() {
		return arr_param_serial;
	}
	
	@Override
	public void executeQuery() {
		StringBuilder bufSQL = new StringBuilder( );
		bufSQL.append( QUERY_SQL ).append( "WHERE cloud_id='" ).append( getCloudID() ).append( "' ");
		bufSQL.append( parseEqualsWhere( "plan_k", parameterMap.get(QP_PLAN_K), false ) );
		bufSQL.append( parseLikeWhere( "obj_short", parameterMap.get(QP_OBJECT_P), SQL_LIKE_BOTH ) );
		bufSQL.append( parseRangeWhere( "plan_date", parameterMap.get(QP_DATE1), parameterMap.get(QP_DATE2), false ) );
		bufSQL.append( parseLikeWhere( "user_zh", parameterMap.get(QP_USER_ZH), SQL_LIKE_BOTH ) );
		String load_name = parameterMap.get(QP_LOAD_NAME);
		if ( load_name.length() > 0 ) {
			bufSQL.append( " AND (ne_zh1 LIKE '%" ).append( load_name ).append( "%' OR ne_zh2 LIKE '%" ).append( load_name ).append( "%' OR ne_recycle LIKE '%" ).append( load_name ).append( "%') " );
		}
		if ( parameterMap.get(QP_IF_HIDE).equals( SQL_YES ) ) {
			bufSQL.append( "AND dispatch_remark='' " );
		}

		String sortTag = parameterMap.get(QP_SORT_TAG);
		if ( sortTag.equals( INPUT_DATE_ASC ) ) {
			bufSQL.append( "ORDER BY input_date ASC " );
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
