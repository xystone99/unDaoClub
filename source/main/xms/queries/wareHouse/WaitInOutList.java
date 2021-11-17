package xms.queries.wareHouse;

import com.undao.database.AbstractQuery;
import com.undao.database.CommonSet;
import com.undao.database.DBUtils;
import com.undao.utils.DateUtils;

import javax.sql.DataSource;
import java.util.HashMap;

public class WaitInOutList extends AbstractQuery {

	private final static int DEFAULT_PAGE_SIZE = 20;
	private final static String MAIN_SQL = "SELECT SQL_CALC_FOUND_ROWS DISTINCT dispt FROM view_dispatch_detail ";
	private final static String DETAIL_SQL = "SELECT dispt,depart_date,truck,plate_number,driver,tel_driver,remark,user_a,input_date,trans_p,plan_k,obj_p,obj_short,time_level,ne_recycle,ne_zh1,linkman_1,window_1,remark_1,ne_zh2,linkman_2,window_2,remark_2,qty_w,qty_v,qty_meter,qty_meter_r,wh_remark,user_zh_w,input_date_w FROM view_dispatch_detail ";

	public final static String QP_SORT_TAG = "QpSortTag";
	public final static String DEPART_DATE_ASC = "DepartDateAsc";
	public final static String PLAN_K_ASC = "PlanKAsc";
	public final static String PLATE_NUMBER_ASC = "PlateNumberAsc";

	public final static String QP_PLAN_K = "fPlanK";						//计划类型
	public final static String QP_OBJECT_P = "fObjectP";					//客户简称
	public final static String QP_PLATE = "fPlate";							//车牌
	public final static String QP_DRIVER = "fDriver";						//司机
	public final static String QP_DATE1 = "fDate1";							//发车日期
	public final static String QP_DATE2 = "fDate2";							//发车日期
	public final static String QP_LOAD_NAME = "fLoadName";					//装卸地名称

	private final static String[] arr_param_serial = {
			QP_SORT_TAG, QP_PLAN_K, QP_OBJECT_P, QP_PLATE, QP_DRIVER, QP_DATE1, QP_DATE2, QP_LOAD_NAME
	};

	/**
	 * Constructor
	 */
	public WaitInOutList(DataSource dataSource ) {
		super(dataSource);
		parameterMap = new HashMap<String,String>(arr_param_serial.length);
	}

	@Override
	public void setDefaultParameterValue( ) {
		parameterMap.put( QP_SORT_TAG, PLAN_K_ASC );
		parameterMap.put( QP_PLAN_K, SQL_ALL );
		parameterMap.put( QP_OBJECT_P, SQL_EMPTY );
		parameterMap.put( QP_PLATE, SQL_EMPTY );
		parameterMap.put( QP_DRIVER, SQL_EMPTY );
		parameterMap.put( QP_DATE1, DateUtils.formatCurrentDate() );
		parameterMap.put( QP_DATE2, DateUtils.formatCurrentDate() );
		parameterMap.put( QP_LOAD_NAME, SQL_EMPTY );
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
		bufMainSQL.append( parseEqualsWhere( "plan_k", parameterMap.get(QP_PLAN_K), false ) );
		bufMainSQL.append( parseLikeWhere( "obj_short", parameterMap.get(QP_OBJECT_P), SQL_LIKE_BOTH ) );
		bufMainSQL.append( parseLikeWhere( "plate_number", parameterMap.get(QP_PLATE), SQL_LIKE_BOTH ) );
		bufMainSQL.append( parseLikeWhere( "driver", parameterMap.get(QP_DRIVER), SQL_LIKE_BOTH ) );
		bufMainSQL.append( parseRangeWhere("depart_date", parameterMap.get(QP_DATE1), parameterMap.get(QP_DATE2), false) );
		String load_name = parameterMap.get(QP_LOAD_NAME);
		if ( load_name.length() > 0 ) {
			bufMainSQL.append( "AND (ne_recycle LIKE '%" ).append( load_name ).append( "%' OR ne_zh1 LIKE '%" ).append( load_name ).append( "%' OR ne_zh2 LIKE '%" ).append( load_name ).append( "%') " );
		}
		bufMainSQL.append( "ORDER BY dispt ASC " );
		System.out.println( bufMainSQL.toString() );

		int base = ( getCurrentPage() - 1 ) * getPageSize();
		bufMainSQL.append( "LIMIT " ).append( base ).append( "," ).append( getPageSize() );
		CommonSet mainSet = DBUtils.executeQuery(getDataSource(), bufMainSQL.toString(), true );

		long id1=0 , id2=0, id_small=0, id_big=0;
		if ( mainSet.getRowCount() > 0 ) {
			id1 = ((Long)mainSet.getValue(0, "dispt" )).longValue();
			id2 = ((Long)mainSet.getValue( mainSet.getRowCount()-1, "dispt" )).longValue();
			id_small = id1<=id2 ? id1 : id2;
			id_big = id1>=id2 ? id1 : id2;
		}

		StringBuilder bufDetailSQL = new StringBuilder( );
		bufDetailSQL.append( DETAIL_SQL ).append( "WHERE cloud_id='" ).append( getCloudID() ).append( "' ");
		bufDetailSQL.append( parseEqualsWhere( "plan_k", parameterMap.get(QP_PLAN_K), false ) );
		bufDetailSQL.append( parseLikeWhere( "obj_short", parameterMap.get(QP_OBJECT_P), SQL_LIKE_BOTH ) );
		bufDetailSQL.append( parseLikeWhere( "plate_number", parameterMap.get(QP_PLATE), SQL_LIKE_BOTH ) );
		bufDetailSQL.append( parseLikeWhere( "driver", parameterMap.get(QP_DRIVER), SQL_LIKE_BOTH ) );
		bufDetailSQL.append( parseRangeWhere("depart_date", parameterMap.get(QP_DATE1), parameterMap.get(QP_DATE2), false) );
		if ( load_name.length() > 0 ) {
			bufDetailSQL.append( "AND (ne_recycle LIKE '%" ).append( load_name ).append( "%' OR ne_zh1 LIKE '%" ).append( load_name ).append( "%' OR ne_zh2 LIKE '%" ).append( load_name ).append( "%') " );
		}

		bufDetailSQL.append( "AND dispt>=" ).append( id_small ).append( " AND dispt<=" ).append( id_big ).append( SQL_SPACE );

		String sortTag = parameterMap.get(QP_SORT_TAG);
		if ( sortTag.equals( DEPART_DATE_ASC ) ) {
			bufDetailSQL.append( "ORDER BY dispt ASC, depart_date ASC " );
		} else if ( sortTag.equals( PLAN_K_ASC ) ) {
			bufDetailSQL.append( "ORDER BY dispt ASC, plan_k ASC " );
		} else if ( sortTag.equals( PLATE_NUMBER_ASC ) ) {
			bufDetailSQL.append( "ORDER BY dispt ASC, plate_number ASC " );
		}

		querySQL = bufDetailSQL.toString();
		setBaseIndex( base+1 );
		commonSet = DBUtils.executeQuery(getDataSource(), bufDetailSQL.toString(), true );
		commonSet.setTotalCount( mainSet.getTotalCount() );
	}

}
