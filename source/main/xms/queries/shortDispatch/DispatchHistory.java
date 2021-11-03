package xms.queries.shortDispatch;

import com.undao.database.AbstractQuery;
import com.undao.database.CommonSet;
import com.undao.database.DBUtils;
import com.undao.utils.DateUtils;

import javax.sql.DataSource;
import java.util.HashMap;

public class DispatchHistory extends AbstractQuery {

	private final static int DEFAULT_PAGE_SIZE = 20;
	private final static String MAIN_SQL = "SELECT SQL_CALC_FOUND_ROWS dispt FROM view_dispatch_main ";
	private final static String DETAIL_SQL = "SELECT dispt,depart_date,truck,plate_number,driver,tel_driver,sub_driver,sub_driver_zh,remark,wh_remark,ne_zh1,ne_zh2,ne_recycle,qty_w,qty_v,qty_meter,qty_meter_r,user_a FROM view_dispatch_detail ";

	public final static String QP_SORT_TAG = "QpSortTag";
	public final static String INPUT_DATE_ASC = "InputDateAsc";
	public final static String INPUT_DATE_DESC = "InputDateDesc";

	public final static String QP_TRUCK = "fTruck";							//车牌号
	public final static String QP_DRIVER = "fDriver";						//司机
	public final static String QP_SUB_DRIVER = "fSubDriver";				//押运员
	public final static String QP_DATE1 = "fDate1";							//发车日期1
	public final static String QP_DATE2 = "fDate2";							//发车日期2

	private final static String[] arr_param_serial = {
			QP_SORT_TAG, QP_TRUCK, QP_DRIVER, QP_SUB_DRIVER, QP_DATE1, QP_DATE2
	};

	/**
	 * Constructor
	 */
	public DispatchHistory(DataSource dataSource ) {
		super(dataSource);
		parameterMap = new HashMap<String,String>(arr_param_serial.length);
		parameterMap.put( QP_SORT_TAG, INPUT_DATE_ASC );
		parameterMap.put( QP_TRUCK, SQL_EMPTY );
		parameterMap.put( QP_DRIVER, SQL_EMPTY );
		parameterMap.put( QP_SUB_DRIVER, SQL_EMPTY );
		parameterMap.put( QP_DATE1, DateUtils.formatCurrentDate() );
		parameterMap.put( QP_DATE2, DateUtils.formatCurrentDate() );
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
		bufMainSQL.append( parseLikeWhere( "plate_number", parameterMap.get(QP_TRUCK), SQL_LIKE_BOTH ) );
		bufMainSQL.append( parseLikeWhere( "tel_driver", parameterMap.get(QP_DRIVER), SQL_LIKE_BOTH ) );
		bufMainSQL.append( parseLikeWhere( "sub_driver_zh", parameterMap.get(QP_SUB_DRIVER), SQL_LIKE_BOTH ) );
		bufMainSQL.append( parseRangeWhere( "depart_date", parameterMap.get(QP_DATE1), parameterMap.get(QP_DATE2), false ) );

		String sortTag = parameterMap.get(QP_SORT_TAG);
		if ( sortTag.equals( INPUT_DATE_ASC ) ) {
			bufMainSQL.append( "ORDER BY dispt ASC, input_date ASC " );
		} else if ( sortTag.equals( INPUT_DATE_DESC ) ) {
			bufMainSQL.append( "ORDER BY dispt ASC, input_date DESC " );
		} else {
			bufMainSQL.append( "ORDER BY dispt ASC, input_date ASC " );
		}
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
		bufDetailSQL.append( parseLikeWhere( "plate_number", parameterMap.get(QP_TRUCK), SQL_LIKE_BOTH ) );
		bufDetailSQL.append( parseLikeWhere( "tel_driver", parameterMap.get(QP_DRIVER), SQL_LIKE_BOTH ) );
		bufDetailSQL.append( parseLikeWhere( "sub_driver_zh", parameterMap.get(QP_SUB_DRIVER), SQL_LIKE_BOTH ) );
		bufDetailSQL.append( parseRangeWhere( "depart_date", parameterMap.get(QP_DATE1), parameterMap.get(QP_DATE2), false ) );

		bufDetailSQL.append( "AND dispt>=" ).append( id_small ).append( " AND dispt<=" ).append( id_big ).append( SQL_SPACE );

		if ( sortTag.equals( INPUT_DATE_ASC ) ) {
			bufDetailSQL.append( "ORDER BY dispt ASC, input_date ASC " );
		} else if ( sortTag.equals( INPUT_DATE_DESC ) ) {
			bufDetailSQL.append( "ORDER BY dispt ASC, input_date DESC " );
		} else {
			bufDetailSQL.append( "ORDER BY dispt ASC, input_date ASC " );
		}

		querySQL = bufDetailSQL.toString();
		setBaseIndex( base+1 );
		commonSet = DBUtils.executeQuery(getDataSource(), bufDetailSQL.toString(), true );
		commonSet.setTotalCount( mainSet.getTotalCount() );
	}

}
