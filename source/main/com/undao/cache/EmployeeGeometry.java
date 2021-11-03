/**
 * Created At 2014-2-24 下午03:55:48
 */
package com.undao.cache;

import java.util.*;

import com.undao.control.AbstractDaemon;
import com.undao.database.*;
import com.undao.utils.DateUtils;

/**
 * @author Administrator
 *
 */
public class EmployeeGeometry extends AbstractDatabase {
	
	private static final long serialVersionUID = 1L;
	private static String EMPLOYEE_SQL = "SELECT emp,ne_zh,init_spell,company,part,join_date,sys_flg FROM tbl_emp WHERE sys_flg IN('Normal','Inner','Dimission') ORDER BY company ASC, init_spell ASC";
	private static String EMPLOYEE_SQL_L = "SELECT emp,init_spell,m_companies,b_companies FROM tbl_emp WHERE can_login='Y' AND sys_flg IN('Normal','Inner','Dimission') ORDER BY company ASC, init_spell ASC";
	
	private static EmployeeGeometry instance = null;
	private EmployeeGeometry() {
		super();
	}
	public static EmployeeGeometry getInstance( ) {
		if ( instance == null ) {
			synchronized( EmployeeGeometry.class ) {
				if ( instance == null )  instance = new EmployeeGeometry( );
			}
		}
		return instance;
	}
	
	/**
	 * 员工属性
	 */
	protected String[] arrInitSpell = null;
	protected String[] arrID = null;
	
	protected HashMap<String,String> mapName = new HashMap<String,String>();    
	protected HashMap<String,String> mapCompany = new HashMap<String,String>();
	protected HashMap<String,String> mapPart = new HashMap<String,String>();
	protected HashMap<String,String> mapJoinDate = new HashMap<String,String>();
	protected HashMap<String,String> mapFlg = new HashMap<String,String>();
	
	protected String[] arrInitSpell_l = null;
	protected String[] arrID_l = null;
	
	protected HashMap<String,String> mapManageCompanies = new HashMap<String,String>();
	protected HashMap<String,String> mapBusinessCompanies = new HashMap<String,String>();
	
	public void fixSingletonObject( ) {
		mapName.clear( );
		mapCompany.clear( );
		mapPart.clear( );
		mapJoinDate.clear( );
		mapFlg.clear( );
		
		CommonSet dataList = DBUtils.executeQuery( getDataSource(), EMPLOYEE_SQL, false );
		int rowCount = dataList.getRowCount();
		arrInitSpell = new String[rowCount];
		arrID = new String[rowCount];
		
		for( int j=0; j<dataList.getRowCount(); j++ ) {
			String empID = ((Integer)dataList.getValue(j,"emp")).toString();
			arrID[j] = empID;
			arrInitSpell[j] = (String)dataList.getValue(j,"init_spell");
			
			mapName.put( empID, (String)dataList.getValue(j,"ne_zh") );
			mapCompany.put( empID, (String)dataList.getValue(j,"company") );
			mapPart.put( empID, ((Integer)dataList.getValue(j,"part")).toString() );
			mapJoinDate.put( empID, DateUtils.formatDate( dataList.getValue(j,"join_date") ) );
			mapFlg.put( empID, (String)dataList.getValue(j,"sys_flg") );
		}	
		
		mapManageCompanies.clear( );
		mapBusinessCompanies.clear( );
		
		dataList = DBUtils.executeQuery( getDataSource(), EMPLOYEE_SQL_L, false );
		rowCount = dataList.getRowCount();
		arrInitSpell_l = new String[rowCount];
		arrID_l = new String[rowCount];
		
		for( int j=0; j<dataList.getRowCount(); j++ ) {
			String empID = ((Integer)dataList.getValue(j,"emp")).toString();
			arrID_l[j] = empID;
			arrInitSpell_l[j] = (String)dataList.getValue(j,"init_spell");

		}
	}    
    
	/**
	 * 根据快速检索代码匹配
	 * @param term	: AutoComplete输入字符串
	 * @return		：包含项目信息的JSON字符串
	 */
	public String searchLander( String term ) {
		int counter = 0;
		StringBuilder buf = new StringBuilder( );
		StringBuilder bufLabel = new StringBuilder( );
		buf.append( "[" );
		for ( int j=0; j<arrInitSpell_l.length; j++ ) {
			if ( !arrInitSpell_l[j].contains(term) ) continue;
			String empID = arrID_l[j];
			bufLabel.delete(0, bufLabel.length() );
			bufLabel.append( "[" ).append( mapCompany.get(empID) ).append( "]" ).append( mapName.get(empID) );
    		buf.append( "{" );
    		buf.append( AbstractDaemon.makeJsonItem("LanderID", empID ) ).append( "," );
    		buf.append( AbstractDaemon.makeJsonItem("value", mapName.get(empID) ) ).append( "," );
    		buf.append( AbstractDaemon.makeJsonItem("label", bufLabel.toString() ) );
    		buf.append( "}," );
    		counter++;
    		if ( counter >= 12 ) break;
		}
		buf.deleteCharAt( buf.length() -1 );
		buf.append( "]" );
		return buf.toString( );
	}
	
	public String searchEmployee( String term ) {
		int counter = 0;
		StringBuilder buf = new StringBuilder( );
		StringBuilder bufLabel = new StringBuilder( );
		buf.append( "[" );
		for ( int j=0; j<arrInitSpell.length; j++ ) {
			if ( !arrInitSpell[j].contains(term) ) continue;
			String empID = arrID[j];
			bufLabel.delete(0, bufLabel.length() );
			bufLabel.append( "[" ).append( mapCompany.get(empID) ).append( "]" ).append( mapName.get(empID) );
    		buf.append( "{" );
    		buf.append( AbstractDaemon.makeJsonItem("Emp", empID ) ).append( "," );
    		buf.append( AbstractDaemon.makeJsonItem("NameZh", mapName.get(empID) ) ).append( "," );
    		buf.append( AbstractDaemon.makeJsonItem("Company", mapCompany.get(empID) ) ).append( "," );
    		buf.append( AbstractDaemon.makeJsonItem("Part", mapPart.get(empID) ) ).append( "," );
    		buf.append( AbstractDaemon.makeJsonItem("SysFlg", mapFlg.get(empID) ) ).append( "," );
    		buf.append( AbstractDaemon.makeJsonItem("value", mapName.get(empID) ) ).append( "," );
    		buf.append( AbstractDaemon.makeJsonItem("label", bufLabel.toString() ) );
    		buf.append( "}," );
    		counter++;
    		if ( counter >= 12 ) break;
		}
		buf.deleteCharAt( buf.length() -1 );
		buf.append( "]" );
		return buf.toString( );
	}
	
	/**
	 * 根据员工编号获取中文名字
	 */
	public String getName( String empID ) {
		if ( empID.equals( SQL_EMPTY ) ) {
			return SQL_EMPTY;
		} 
		return mapName.get( empID );
	}
	
	public String getName( Integer empID ) {
		if ( empID.intValue() == 0 ) {
			return SQL_EMPTY;
		}
		return mapName.get( empID.toString() );
	}
	
	public String getName( Object empID ) {
		if ( empID.equals( new Integer(0) ) ) {
			return SQL_EMPTY;
		} 
		return mapName.get( ((Integer)empID).toString() );
	}
	
	/**
	 * 根据员工编号获取登录者中文名字
	 */
	public String getLanderName( String empID ) {
		if ( empID.equals( SQL_EMPTY ) ) {
			return SQL_EMPTY;
		} 
		return mapName.get( empID );
	}
	
	public String getLanderName( Integer empID ) {
		if ( empID.intValue() == 0 ) {
			return SQL_EMPTY;
		}
		return mapName.get( empID.toString() );
	}
	
	public String getLanderName( Object empID ) {
		if ( empID.equals( new Integer(0) ) ) {
			return SQL_EMPTY;
		} 
		return mapName.get( ((Integer)empID).toString() );
	}
	
	/**
	 * 根据员工编号获取获取所属公司
	 */
	public String getCompany( String empID ) {
		return mapCompany.get( empID );
	}
	
	/**
	 * 根据员工编号获取获取所属部门
	 */
	public String getPart( String empID ) {
		return mapPart.get( empID );
	}
	
	/**
	 * 根据员工编号获取获取入职日期
	 */
	public String getJoinDate( String empID ) {
		return mapJoinDate.get( empID );
	}
	
	/**
	 * 根据员工编号获取系统 标记 
	 */
	public String getSysFlg( String empID ) {
		return mapFlg.get( empID );
	}
	
	/**
	 * 可检索分公司列表(管理类)
	 */
	public String getManageCompanies( String empID ) {
		return mapManageCompanies.get( empID );
	}
	
	public String setManageCompanies( String empID, String companies ) {
		return mapManageCompanies.put( empID, companies );
	}
	
	/**
	 * 可检索分公司列表(业务类)
	 */
	public String getBusinessCompanies( String empID ) {
		return mapBusinessCompanies.get( empID );
	}
	
	public String setBusinessCompanies( String empID, String companies ) {
		return mapBusinessCompanies.put( empID, companies );
	}
	
}
