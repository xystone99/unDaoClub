package xms.procedures;

import java.util.HashMap;
import javax.sql.DataSource;
import com.undao.database.AbstractProcedure;

public class LoginSystem extends AbstractProcedure implements ProcedureConstants {

	private static final String SQL_PROC = "{CALL proc_employee_login(?,?,?,  ?,?,?,?,?,?,?,?  ) }";

	public final static String QP_LOGIN_NAME = "tLoginName";        //用户名
	public final static String QP_LOGIN_PWD = "tLoginPwd";			//密码
	public final static String QP_CLOUD_ID = "fCloudID";

	private final static String[] p_param_serial = { QP_LOGIN_NAME, QP_LOGIN_PWD, QP_CLOUD_ID	};

	public final static String R_LOGIN_SUCCESS = "LoginSuccess";
	public final static String R_PASSWORD_ERROR = "PasswordError";
	public final static String R_CANT_LOGIN = "CantLogin";
	private final static HashMap<String,String> mapResult = new HashMap<String,String>();
	static {
		mapResult.put( R_LOGIN_SUCCESS, "登录成功." );
		mapResult.put( R_PASSWORD_ERROR, "密码错误, 登录失败！" );
		mapResult.put( R_CANT_LOGIN, "该帐户已被管理员禁止登录！" );
		mapResult.put( R_NOT_EXISTS, "用户名不存在, 登录失败！" );
	}

	public LoginSystem(DataSource dataSource ) {
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
		return getReturnValues()[getCountReturnValues()-1].equalsIgnoreCase( R_LOGIN_SUCCESS );
	}


}
