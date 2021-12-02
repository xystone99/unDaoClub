
package com.undao.cache;

import java.util.*;

import com.undao.control.*;
import com.undao.database.*;
import com.undao.utils.DecimalUtils;
import com.undao.database.AbstractDatabase;

/**
 * @author Administrator
 *
 */
public class CustomerGeometry extends AbstractDatabase {

	private static final long serialVersionUID = 1L;	
	private static String FETCH_SQL = "SELECT obj_p,ne_short,init_spell,ne_zh FROM tbl_payment_object WHERE sys_flg IN('Normal') AND if_cus='Y' ORDER BY init_spell ASC";
	
	private static CustomerGeometry instance = null;
	private CustomerGeometry( ) {
	}
	public static CustomerGeometry getInstance( ) {
		if ( instance == null ) {
			synchronized( CustomerGeometry.class ) {
				if ( instance == null ) instance = new CustomerGeometry( );
			}
		}
		return instance;
	}
	
	private String[] arrInitSpell = null;
	private String[] arrID = null;
	private String[] arrShort = null;
	private HashMap<String,String> mapCus = new HashMap<String,String>();

	public void fixSingletonObject( ) {
		CommonSet dataList = DBUtils.executeQuery( getDataSource(), FETCH_SQL, false );
		int rowCount = dataList.getRowCount();
		arrInitSpell = new String[rowCount];
		arrID = new String[rowCount];
		arrShort = new String[rowCount];
		
		for( int j=0; j<rowCount; j++ ) {
			Long objID = (Long)dataList.getValue(j,"obj_p");
			String ne_short = (String)dataList.getValue(j,"ne_short");
			arrInitSpell[j] = (String)dataList.getValue(j,"init_spell");
			arrID[j] = objID.toString();
			arrShort[j] = ne_short;
			mapCus.put( objID.toString(), ne_short );
		}
	}
	
	/**
	 * 根据快速检索代码匹配
	 * @param term	: AutoComplete输入字符串
	 * @return		:
	 */
	public String searchPattern( String term ) {
		int counter = 0;
		StringBuilder buf = new StringBuilder( );
		buf.append( "[" );
		for ( int j=0; j<arrInitSpell.length; j++ ) {
			if ( !arrInitSpell[j].contains(term) && !mapCus.get(arrID[j]).contains(term) ) continue;
    		buf.append( "{" );
    		buf.append( AbstractDaemon.makeJsonItem("ID", arrID[j] ) ).append( "," );
    		buf.append( AbstractDaemon.makeJsonItem("value", arrShort[j] ) ).append( "," );
    		buf.append( AbstractDaemon.makeJsonItem("label", arrShort[j] ) );
    		buf.append( "}," );
    		counter++;
    		if ( counter >= 12 ) break;
		}
		buf.deleteCharAt( buf.length() -1 );
		buf.append( "]" );
		return buf.toString( );
	}
	
	/**
	 * 根据客户ID精确匹配
	 * @param cusID	: 客户ID
	 * @return		：包含项目信息和费用模板信息的JSON字符串
	 */
	public String searchByCusID( String cusID ) {
		StringBuilder buf = new StringBuilder( );
		buf.append( "{" );
		buf.append( AbstractDaemon.makeJsonItem("ProjectList", "Building......" ) ).append( "," );
		buf.append( AbstractDaemon.makeJsonItem("FeeEngineList", "Building......" ) );
		buf.append( "}" );
		return buf.toString( );
	}
	
}
