/**
 * Created At 2014-2-24 下午03:55:48
 */
package com.undao.cache;

import java.util.*;
import java.math.BigDecimal;

import com.undao.control.AbstractDaemon;
import com.undao.database.*;
import com.undao.enumeration.*;

/**
 * @author Administrator
 *
 */
public class TruckGeometry extends AbstractDatabase {

	private static String FETCH_SQL = "SELECT truck,plate_number,truck_type FROM tbl_truck WHERE 1=1 ORDER BY plate_number ASC";
	
	private static TruckGeometry instance = null;
	private TruckGeometry() {
		super();
	}
	public static TruckGeometry getInstance( ) {
		if ( instance == null ) {
			synchronized( TruckGeometry.class ) {
				if ( instance == null )  instance = new TruckGeometry( );
			}
		}
		return instance;
	}
		
	private String[] arrID = null;
	private String[] arrType = null;
	private String[] arrPlate = null;
	
	protected HashMap<String,String> mapPlate = new HashMap<String,String>();

	public void fixSingletonObject( ) {				
		mapPlate.clear( );
		CommonSet dataList = DBUtils.executeQuery( getDataSource(), FETCH_SQL, false );
		int rowCount = dataList.getRowCount();
		arrID = new String[rowCount];
		arrType = new String[rowCount];
		arrPlate = new String[rowCount];
		
		for( int j=0; j<dataList.getRowCount(); j++ ) {
			String key = ((Long)dataList.getValue(j,"truck")).toString( );
			arrID[j] = key;
			arrType[j] = (String)dataList.getValue(j,"truck_type");
			arrPlate[j] = (String)dataList.getValue(j,"plate_number");
			mapPlate.put( key, (String)dataList.getValue(j,"plate_number") );
		}
	}
	
	public int size( ) {
    	return arrID.length;
    }
    
    public String getID( int index ) {
    	return arrID[index];
    }
    
    public String getDisplay( int index ) {
    	return mapPlate.get( arrID[index] );
    }
    
    public String getDisplayByID( Long truckID ) {
    	return mapPlate.get( truckID.toString() );
    }
    
    public String getDisplayOfID( String truckID ) {
    	return mapPlate.get( truckID );
    }
    
    public String getDisplayOfID( int truckID ) {
    	return mapPlate.get( Integer.toString( truckID ) );
    }    
    
    /**
	 * 根据快速检索代码匹配
	 * @param term	: AutoComplete输入字符串
	 * @return		：包含项目信息的JSON字符串
	 */
	public String searchTruck( String term ) {
		int counter = 0;
		StringBuilder buf = new StringBuilder( );
		StringBuilder bufLabel = new StringBuilder( );
		for ( int j=0; j<arrPlate.length; j++ ) {
			if ( !arrPlate[j].contains(term) ) continue;
			bufLabel.delete(0, bufLabel.length() );
			bufLabel.append( arrPlate[j] ).append( "[" ).append( arrType[j] ).append( "]" );
    		buf.append( "{" );
    		buf.append( AbstractDaemon.makeJsonItem("ID", arrID[j] ) ).append( "," );
    		buf.append( AbstractDaemon.makeJsonItem("PlateNumber", arrPlate[j] ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("value", "自有车辆" ) ).append( "," );
    		buf.append( AbstractDaemon.makeJsonItem("label", bufLabel.toString() ) );
    		buf.append( "}," );
    		counter++;
    		if ( counter >= 12 ) break;
		}

		buf.deleteCharAt( buf.length() -1 );
		return buf.toString( );
	}

}
