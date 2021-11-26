package xms.beans.sysConfigure;

import com.undao.database.AbstractBean;

import javax.sql.DataSource;
import java.util.HashMap;

public class UserAccount extends AbstractBean {

	public final static String QP_ID = "ID";							//ID
	public final static String QP_NAME = "fName";						//姓名
	public final static String QP_TEL = "fTel";   						//电话号码
	public final static String QP_INIT_SPELL = "fInitSpell";   			//速记码
	public final static String QP_IF_DRIVER = "ifDriver";   			//是否司机
	public final static String QP_LOGIN_NAME = "loginName";   			//登录名
	public final static String QP_CAN_LOGIN = "canLogin";   			//是否能登录
	public final static String QP_ROLE = "fRole";   					//所属角色
	public final static String QP_ASTRICT_LEVEL = "fAstrictLevel";		//权限级别
	public final static String QP_CUR_COMPANY = "curCompany";			//当前公司
	public final static String QP_AVALIABLE_COMPANYS = "aCompanys";		//可访问公司列表

	private final static String[] i_param_serial = {
			QP_ID, QP_NAME, QP_TEL, QP_INIT_SPELL, QP_IF_DRIVER, QP_LOGIN_NAME, QP_CAN_LOGIN, QP_ROLE, QP_ASTRICT_LEVEL, QP_CUR_COMPANY,
			QP_AVALIABLE_COMPANYS
	};
	private final static String[] d_param_serial = { QP_ID  };

	private final static HashMap<String,String> mapResult = new HashMap<String,String>();
	static {
		mapResult.put( R_NEW_SUCCESS, "用户已创建." );
		mapResult.put( R_UPDATE_SUCCESS, "用户已修改." );
		mapResult.put( R_DELETE_SUCCESS, "用户已删除." );
		mapResult.put( R_INVALID, "非法操作." );
	}

	public UserAccount(DataSource dataSource, int beanType) {
		super(dataSource, beanType);
		if ( beanType == BTYPE_UPDATE ) {
			setProcedureString( "{CALL proc_user_account_update(?,?,?,?,?,?,?,?,?,?,?,?,  ?  ) }" );
			setCountReturnValues( 1 );
			setParamSerial( i_param_serial );
		} else if ( beanType == BTYPE_DELETE ) {
			setProcedureString( "{CALL proc_user_account_delete(?,  ?,  ? ) }" );
			setCountReturnValues( 1 );
			setParamSerial( d_param_serial );
		}
	}

	@Override
	public String getResultDisplay() {
		String result = getReturnValues()[getCountReturnValues()-1];
		if ( mapResult.containsKey( result ) ) {
			return mapResult.get( result );
		}
		return super.getResultDisplay( result );
	}

}
