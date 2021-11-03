package yundeploy.procedures;

import javax.sql.DataSource;

import com.undao.database.*;

/**
 * 更新天气信息
 * @author SWK
 *
 */
public class WeatherUpdate extends AbstractProcedure {
	
	public final static String QP_ECL_L = "QpEclL";					//线路编号
	public final static String QP_CITY = "QpCity";					//城市
	public final static String QP_DATE = "QpDate";					//日期
	public final static String QP_DAY_TQ = "QpDayTQ";				//白天天气
	public final static String QP_NIGHT_TQ = "QpNightTQ";			//夜晚天气
	public final static String QP_QW1 = "QpQW1";					//气温1
	public final static String QP_QW2 = "QpQW2";					//气温2
	public final static String QP_FL = "QpFL";						//风力
	public final static String QP_REMARK = "QpRemark";				//天气信息
	
	private final static String[] arr_param_serial = { QP_ECL_L, QP_CITY, QP_DATE, QP_DAY_TQ, QP_NIGHT_TQ, QP_QW1, QP_QW2, QP_FL, QP_REMARK  };

	/**
	 * Constructor
	 */
	public WeatherUpdate(DataSource ds) {
		super( ds );
		setProcedureString( "{CALL proc_weather_update(?,?,?,?,?,?,?,?,?,  ?  ) }" );
		setCountReturnValues( 1 );
		setParamSerial( arr_param_serial );
	}

	@Override
	public String getResultDisplay() {
		if ( getReturnValues() == null ) {
			return "NullReturnValues";
		}
		String result = getReturnValues()[getCountReturnValues()-1];
		if ( result.equalsIgnoreCase( "WeatherUpdateSuccess" ) ) {
			return "天气信息更新成功......";
		}
		return super.getResultDisplay( result );
	}

	@Override
	public boolean isSuccess() {
		if ( getReturnValues() == null ) {
			return false;
		}
		String result = getReturnValues()[getCountReturnValues()-1];
		return result.equalsIgnoreCase( "WeatherUpdateSuccess" );
	}
	
}
