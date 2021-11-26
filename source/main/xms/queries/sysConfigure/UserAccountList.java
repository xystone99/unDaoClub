package xms.queries.sysConfigure;

import com.undao.database.AbstractQuery;
import com.undao.database.DBUtils;
import com.undao.enumeration.EnumConstants;
import com.undao.utils.DateUtils;

import javax.sql.DataSource;
import java.util.HashMap;

public class UserAccountList extends AbstractQuery {

	private final static int DEFAULT_PAGE_SIZE = 20;
	private final static String QUERY_SQL = "SELECT SQL_CALC_FOUND_ROWS user_a,emp_rely,ne_zh,tel,init_spell,if_driver,login_name,can_login,role,astrict_level,cur_company,available_companys,sys_flg FROM tbl_user_account ";

	public final static String QP_NAME = "fName";						//用户姓名
	public final static String QP_COMPANY = "fCompany";					//默认分公司
	public final static String QP_IF_DRIVER = "ifDriver";				//是否司机
	public final static String QP_EMP_RELY = "empRely";					//依赖用户表
	public final static String QP_CAN_LOGIN = "canLogin";				//允许登录
	public final static String QP_ROLE = "fRole";						//所属角色
	public final static String QP_ASTRICT_LEVEL = "aLevel";				//权限级别
	public final static String QP_AVALIABLE_COMPANYS = "aCompanys";		//可访问分公司列表
	public final static String QP_SYS_FLG = "sysFlg";					//用户状态

	private final static String[] arr_param_serial = {
			QP_NAME, QP_COMPANY, QP_IF_DRIVER, QP_EMP_RELY, QP_CAN_LOGIN, QP_ROLE, QP_ASTRICT_LEVEL, QP_AVALIABLE_COMPANYS, QP_SYS_FLG
	};

	/**
	 * Constructor
	 */
	public UserAccountList(DataSource dataSource ) {
		super(dataSource);
		parameterMap = new HashMap<String,String>(arr_param_serial.length);
	}

	@Override
	public void setDefaultParameterValue( ) {
		parameterMap.put( QP_NAME, SQL_EMPTY );
		parameterMap.put( QP_COMPANY, SQL_ALL );
		parameterMap.put( QP_IF_DRIVER, SQL_ALL );
		parameterMap.put( QP_EMP_RELY, SQL_ALL );
		parameterMap.put( QP_CAN_LOGIN, SQL_YES );
		parameterMap.put( QP_ROLE, SQL_ALL );
		parameterMap.put( QP_ASTRICT_LEVEL, SQL_ALL );
		parameterMap.put( QP_AVALIABLE_COMPANYS, SQL_ALL );
		parameterMap.put( QP_SYS_FLG, SQL_NORMAL );
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
		bufSQL.append( parseLikeWhere( "ne_zh", parameterMap.get(QP_NAME), SQL_LIKE_BOTH ) );
		bufSQL.append( parseEqualsWhere( "cur_company", parameterMap.get(QP_COMPANY), false ) );
		bufSQL.append( parseEqualsWhere( "if_driver", parameterMap.get(QP_IF_DRIVER), false ) );
		String empRely = parameterMap.get( QP_EMP_RELY );
		if ( !empRely.equals( SQL_ALL) ) {
			bufSQL.append( "AND emp_rely" ).append( empRely );
		}
		bufSQL.append( parseEqualsWhere( "can_login", parameterMap.get(QP_CAN_LOGIN), false ) );
		bufSQL.append( parseEqualsWhere( "role", parameterMap.get(QP_ROLE), false ) );
		bufSQL.append( parseEqualsWhere( "astrict_level", parameterMap.get(QP_ASTRICT_LEVEL), false ) );
		bufSQL.append( parseLikeWhere( "available_companys", parameterMap.get(QP_AVALIABLE_COMPANYS), SQL_LIKE_BOTH ) );
		bufSQL.append( parseLikeWhere( "sys_flg", parameterMap.get(QP_SYS_FLG), SQL_LIKE_BOTH ) );

		bufSQL.append( "ORDER BY cur_company ASC, init_spell ASC " );

		int base = ( getCurrentPage() - 1 ) * getPageSize();
		bufSQL.append( "LIMIT " ).append( base ).append( "," ).append( getPageSize() );
		
		querySQL = bufSQL.toString();
		setBaseIndex( base+1 );
		commonSet = DBUtils.executeQuery(getDataSource(), bufSQL.toString(), true);
	}

}
