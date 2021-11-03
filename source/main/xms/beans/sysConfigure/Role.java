package xms.beans.sysConfigure;

import javax.sql.DataSource;
import com.undao.database.*;

import java.util.HashMap;

public class Role extends AbstractBean{

	private final static String SELECT_SQL = "SELECT * FROM tbl_role WHERE role=? AND cloud_id=?";
	private final static String INSERT_SQL = "{CALL proc_role_new(?,?,?,?,?,?,  ?,?  ) }";
	private final static String UPDATE_SQL = "{CALL proc_role_update(?,?,?,?,?,?,?,  ?  ) }";
	private final static String DELETE_SQL = "{CALL proc_role_delete(?,?,  ? ) }";


	public final static String QP_ROLE = "ID";        					//编号
	public final static String QP_NAME_ZH = "fNameZh";   				//中文名称
	public final static String QP_SORT_TAG = "fSortTag";   				//排序标记
	public final static String QP_HREF_INDEX = "fHrefIndex";   			//角色首页
	public final static String QP_ASTRICTS = "fAstricts";				//权限列表
	public final static String QP_REMARK = "fRemark";					//备注

	private final static String[] i_param_serial = { QP_NAME_ZH, QP_SORT_TAG, QP_HREF_INDEX, QP_ASTRICTS, QP_REMARK		};
	private final static String[] u_param_serial = { QP_ROLE, QP_NAME_ZH, QP_SORT_TAG, QP_HREF_INDEX, QP_ASTRICTS, QP_REMARK	};
	private final static String[] d_param_serial = { QP_ROLE	};
	private final static String[] q_param_serial = { QP_ROLE	};

	private final static HashMap<String,String> mapResult = new HashMap<String,String>();
	static {
		mapResult.put( R_NEW_SUCCESS, "角色添加成功." );
		mapResult.put( R_UPDATE_SUCCESS, "角色已修改." );
		mapResult.put( R_DELETE_SUCCESS, "角色已删除." );
		mapResult.put( R_EXISTS, "新增失败！角色名称已存在." );
		mapResult.put( R_IN_USE, "删除失败！角色已使用." );
	}
	
	public Role( DataSource dataSource, int beanType ) {
		super( dataSource, beanType );
		if ( beanType == BTYPE_INSERT ) {
			setProcedureString( INSERT_SQL );
			setCountReturnValues( 2 );
			setParamSerial( i_param_serial );
		} else if ( beanType == BTYPE_UPDATE ) {
			setProcedureString( UPDATE_SQL );
			setCountReturnValues( 1 );
			setParamSerial( u_param_serial );
		} else if ( beanType == BTYPE_DELETE ) {
			setProcedureString( DELETE_SQL );
			setCountReturnValues( 1 );
			setParamSerial( d_param_serial );
		} else if ( beanType == BTYPE_DETAIL ) {
			setDetailSQL( SELECT_SQL );
			setParamSerial( q_param_serial );
		}
	}

	@Override
	public String getResultDisplay( ) {
		String result = getReturnValues()[getCountReturnValues()-1];
		if ( mapResult.containsKey( result ) ) {
			return mapResult.get( result );
		}
		return super.getResultDisplay( result );
	}
	
	/**
	 * @return : 返回该角色拥有的权限列表
	 */
	public String getAstricts( ) {
		if ( getBeanType() != BTYPE_DETAIL ) {
			return null;
		}
		StringBuilder bufAstricts = new StringBuilder( );
		StringBuilder astrictSQL = new StringBuilder( );
		astrictSQL.append( "SELECT astrict FROM stc_role_astricts WHERE role=" ).append( getParameterValue(QP_ROLE) );
		
		CommonSet commonSet = DBUtils.executeQuery( getDataSource(), astrictSQL.toString() );
		for ( int j=0; j<commonSet.getRowCount(); j++ ) {
			bufAstricts.append( commonSet.getValue( j,"astrict" ) ).append( "-" );
		}
		if ( bufAstricts.length() <= 0 ) {
			return bufAstricts.toString();
		} 
		return bufAstricts.deleteCharAt( bufAstricts.length()-1 ).toString( );
	}
	
}
