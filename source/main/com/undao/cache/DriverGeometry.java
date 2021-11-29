/**
 * Created At 2014-2-24 下午03:55:48
 */
package com.undao.cache;

import java.util.*;

import com.undao.control.AbstractDaemon;
import com.undao.database.*;

/**
 * @author Administrator
 * 驾驶员
 */
public class DriverGeometry extends AbstractDatabase {

	private static String FETCH_SQL = "SELECT user_a,ne_zh,tel,init_spell,cur_company FROM tbl_user_account WHERE if_driver='Y' ORDER BY init_spell ASC";
	
	private static DriverGeometry instance = null;
	private DriverGeometry( ) {
	}
	public static DriverGeometry getInstance( ) {
		if ( instance == null ) {
			synchronized( DriverGeometry.class ) {
				if ( instance == null ) instance = new DriverGeometry( );
			}
		}
		return instance;
	}
	
	private String[] arrInitSpell = null;
	private String[] arrID = null;
	private String[] arrName = null;
	private String[] arrMobile = null;
	private int[] arrCurCompany = null;
	private HashMap<String,String> mapDriverName = new HashMap<String,String>(); 

	public void fixSingletonObject( ) {
		CommonSet dataList = DBUtils.executeQuery( getDataSource(), FETCH_SQL, false );
		int rowCount = dataList.getRowCount();
		arrInitSpell = new String[rowCount];
		arrID = new String[rowCount];
		arrName = new String[rowCount];
		arrCurCompany = new int[rowCount];
		arrMobile = new String[rowCount];
		
		for( int j=0; j<rowCount; j++ ) {
			arrInitSpell[j] = (String)dataList.getValue(j,"init_spell");
			arrID[j] = ((Long)dataList.getValue(j,"user_a")).toString();
			arrName[j] = (String)dataList.getValue(j,"ne_zh");
			arrMobile[j] = (String)dataList.getValue(j,"tel");
			arrCurCompany[j] = ((Long)dataList.getValue(j,"cur_company")).intValue();
			mapDriverName.put( arrID[j], arrName[j] );
		}	
	}
	
	public String getDisplayByID( Integer driverID ) {
    	return mapDriverName.get( driverID.toString() );
    }
    
    public String getDisplayOfID( String driverID ) {
    	return mapDriverName.get( driverID );
    }
    
    public String getDisplayOfID( int driverID ) {
    	return mapDriverName.get( Integer.toString( driverID ) );
    }
	
	/**
	 * 主驾驶员：根据快速检索代码匹配
	 * @param term	: AutoComplete输入字符串
	 * @return		：包含项目信息的JSON字符串
	 */
	public String searchPattern( String term, int companyID ) {
		int counter = 0;
		StringBuilder buf = new StringBuilder( );
		StringBuilder bufLabel = new StringBuilder( );
		for ( int j=0; j<arrInitSpell.length; j++ ) {
			if ( !arrInitSpell[j].contains(term) ) continue;
			if ( arrCurCompany[j] != companyID ) continue;
			bufLabel.delete(0, bufLabel.length() );
			bufLabel.append( arrName[j] ).append( "-" ).append( arrMobile[j] );
    		buf.append( "{" );
    		buf.append( AbstractDaemon.makeJsonItem("ID", arrID[j] ) ).append( "," );
    		buf.append( AbstractDaemon.makeJsonItem("Mobile", arrMobile[j] ) ).append( "," );
    		buf.append( AbstractDaemon.makeJsonItem("Name", bufLabel.toString() ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("value", "自有司机" ) ).append( "," );
    		buf.append( AbstractDaemon.makeJsonItem("label", bufLabel.toString() ) );
    		buf.append( "}," );
    		counter++;
    		if ( counter >= 12 ) break;
		}
		
		buf.deleteCharAt( buf.length() -1 );
		return buf.toString( );
	}

	public String searchPattern( String term, String companyList ) {
		int counter = 0;
		StringBuilder buf = new StringBuilder( );
		StringBuilder bufLabel = new StringBuilder( );
		for ( int j=0; j<arrInitSpell.length; j++ ) {
			if ( !arrInitSpell[j].contains(term) ) continue;
			if ( !companyList.contains( Integer.toString(arrCurCompany[j])) ) continue;
			bufLabel.delete(0, bufLabel.length() );
			bufLabel.append( arrName[j] ).append( "-" ).append( arrMobile[j] );
			buf.append( "{" );
			buf.append( AbstractDaemon.makeJsonItem("ID", arrID[j] ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("Mobile", arrMobile[j] ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("Name", bufLabel.toString() ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("value", "自有司机" ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("label", bufLabel.toString() ) );
			buf.append( "}," );
			counter++;
			if ( counter >= 12 ) break;
		}

		buf.deleteCharAt( buf.length() -1 );
		return buf.toString( );
	}
	
}
