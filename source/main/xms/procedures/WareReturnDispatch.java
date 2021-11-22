package xms.procedures;

import com.undao.database.AbstractProcedure;

import javax.sql.DataSource;
import java.util.HashMap;

public class WareReturnDispatch extends AbstractProcedure implements ProcedureConstants {

	private static final String SQL_PROC = "{CALL proc_ware_retrun_dispatch(?,?,?,  ?,?,  ?  ) }";

	public final static String QP_DISPT = "fDispt";        		//车次ID
	public final static String QP_TRANS_P = "fTransP";			//计划ID
	public final static String QP_REMARK = "fRemark";			//

	private final static String[] p_param_serial = { QP_DISPT, QP_TRANS_P, QP_REMARK	};

	public final static String R_RETURN_SUCCESS = "ReturnSuccess";
	private final static HashMap<String,String> mapResult = new HashMap<String,String>();
	static {
		mapResult.put( R_RETURN_SUCCESS, "回报成功." );
		mapResult.put( R_INVALID, "非法修改." );
		mapResult.put( R_IN_USE, "仅能回报三天内的计划." );
	}

	public WareReturnDispatch(DataSource dataSource ) {
		super( dataSource );
		setProcedureString( SQL_PROC );
		setCountReturnValues( 1 );
		setParamSerial( p_param_serial );
	}

	@Override
	public String getResultDisplay( ) {
		String result = getReturnValues()[getCountReturnValues()-1];
		if ( mapResult.containsKey( result ) ) {
			return mapResult.get( result );
		}
		return super.getResultDisplay( result );
	}

	public boolean isSuccess( ) {
		return getReturnValues()[getCountReturnValues()-1].equalsIgnoreCase( R_RETURN_SUCCESS );
	}


}
