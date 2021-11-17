package xms.queries.sysConfigure;

import java.util.HashMap;

import javax.sql.DataSource;

import com.undao.database.AbstractQuery;
import com.undao.database.DBUtils;

public class UppGroupList extends AbstractQuery {
	
	private final static int DEFAULT_PAGE_SIZE = 20; 
	public final static String TRANSFER_TAG = "UppGroupList";
	
	public final static String QP_NAME_ZH = "QpNameZh";
	public final static String QP_COMPANY = "QpCompany";
	public final static String QP_SYS_FLG = "QpSysFlg";
	
	private final static String[] arr_param_serial = { QP_NAME_ZH, QP_NAME_ZH, QP_SYS_FLG	};

	public UppGroupList(DataSource dataSource) {
		super(dataSource);
		parameterMap = new HashMap<String,String>(arr_param_serial.length);
	}

	@Override
	public void setDefaultParameterValue( ) {
		parameterMap.put( QP_NAME_ZH, SQL_EMPTY );
		parameterMap.put( QP_COMPANY, SQL_EMPTY );
		parameterMap.put( QP_SYS_FLG, "Normal" );
		this.setPageSize( DEFAULT_PAGE_SIZE );
		this.setCurrentPage( 1 );
	}

	
	@Override
	public String[] getParamSerial( ) {
		return arr_param_serial;
	}
	
	@Override
	public void executeQuery() {
		StringBuilder bufSQL = new StringBuilder( );
		bufSQL.append( "SELECT SQL_CALC_FOUND_ROWS " );
		bufSQL.append( "id,ne_zh,company,emp_list,emp,input_date,last_update,sys_flg FROM tbl_upp_group " );
		bufSQL.append( "WHERE 1=1 " );
		
		bufSQL.append( parseLikeWhere( "ne_zh", parameterMap.get(QP_NAME_ZH), SQL_LIKE_BOTH ) );
		bufSQL.append( parseLikeWhere( "company", parameterMap.get(QP_COMPANY), SQL_LIKE_BOTH ) );
		bufSQL.append( parseEqualsWhere( "sys_flg", parameterMap.get(QP_SYS_FLG), false ) );
		
		bufSQL.append( "ORDER BY last_update DESC " );
		
		int base = ( getCurrentPage() - 1 ) * getNumColumns() * getPageSize();
		bufSQL.append( "LIMIT " ).append( base ).append( "," ).append( getPageSize()*getNumColumns() );
		
		querySQL = bufSQL.toString();
		setBaseIndex( base+1 );
		commonSet = DBUtils.executeQuery(getDataSource(), bufSQL.toString(), true);
	}


}
