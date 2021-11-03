package xms.procedures;

import com.undao.database.AbstractProcedure;

import javax.sql.DataSource;
import java.util.HashMap;

public class UpdatePassword extends AbstractProcedure implements ProcedureConstants {

	static final String SQL_PROC = "{CALL proc_password_update(?,?,?,?,?,  ? ) }";

	public final static String QP_USER_A = "fUserA";
	public final static String QP_LOGIN_PWD = "fLoginPwd";
	public final static String QP_LOGIN_NAME_NEW = "fLoginNameNew";
	public final static String QP_LOGIN_PWD_NEW = "fLoginPwdNew";

	private final static String[] p_param_serial = { QP_USER_A, QP_LOGIN_PWD, QP_LOGIN_NAME_NEW, QP_LOGIN_PWD_NEW	};

	public final static String R_OLD_ERROR = "OldError";
	private final static HashMap<String,String> mapResult = new HashMap<String,String>();
	static {
		mapResult.put( R_OLD_ERROR, "登录成功." );
		mapResult.put( R_UPDATE_SUCCESS, "密码修改成功！" );
	}

	public UpdatePassword(DataSource dataSource ) {
		super( dataSource );
		setProcedureString( SQL_PROC );
		setCountReturnValues( 8 );
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
		return getReturnValues()[getCountReturnValues()-1].equalsIgnoreCase( R_UPDATE_SUCCESS );
	}


}
