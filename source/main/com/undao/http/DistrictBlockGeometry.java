/**
 * Created At 2014-5-7 下午01:46:26
 */
package com.undao.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * @author Administrator
 *
 */
public class DistrictBlockGeometry {
	
	private String site_tag = null;
	private String[] arrDistricts = null;
	private HashMap<String,ArrayList<String>> mapBlock = new HashMap<String,ArrayList<String>>();	//District - BlockList
	private HashMap<String,String> mapDistrictURL = new HashMap<String,String>();
	private HashMap<String,String> mapBlockURL = new HashMap<String,String>();
	
	public DistrictBlockGeometry( String siteTag ) {
		this.site_tag = siteTag;
	}
	
	/**
	 * 区域设置
	 */
	public void setDistricts( String[] arrDistricts ) {
		this.arrDistricts = arrDistricts;
	}
	
	public String[] getDistricts( ) {
		return arrDistricts;
	}
	
	public int sizeOfDistrict( ) {
		return arrDistricts.length;
	}
	
	public String getDistrict( int index ) {
		return arrDistricts[index];
	}
	
	/**
	 * 板块设置
	 */
	public void addBlock( String district, String block ) {
		ArrayList<String> arrList = mapBlock.get( district );
		if ( arrList == null ) {
			ArrayList<String> arrNewList = new ArrayList<String>();
			arrNewList.add( block );
			mapBlock.put( district, arrNewList );
		} else if ( !arrList.contains( block ) ) {
			arrList.add( block );
		}
	}
	
	public ArrayList<String> getBlockList( String district ) {
		return mapBlock.get( district );
	}
	
	/**
	 * 区域URL设置 
	 */
	public void setDistrictURL( String district, String url ) {
		mapDistrictURL.put( district, url );
	}
	
	public String getDistrictURL( String district ) {
		return mapDistrictURL.get( district );
	}
	
	/**
	 * 板块URL设置 
	 */
	public void setBlockURL( String block, String url ) {
		mapBlockURL.put( block, url );
	}
	
	public String getBlockURL( String block ) {
		return mapBlockURL.get( block );
	}
	
	public String getBlock( String blockURL ) {
		Iterator<Entry<String,String>> iter = mapBlockURL.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String,String> entry = (Entry<String,String>)iter.next();
			String val = entry.getValue();
			if ( val.equals( blockURL ) ) {
				return entry.getKey();
			}
		}
		return "UnKnown";
	}
	
	/**
	 * 获得区域、板块对照表,以字符串形式来表达 
	 * 格式：区域=板块ID=板块Value&区域=板块ID=板块Value&区域=板块ID=板块Value
	 */
	public String getGeometry( ) {
		StringBuilder buf = new StringBuilder( );
		for ( int j=0; j<arrDistricts.length; j++ ) {
			ArrayList<String> arrList = mapBlock.get( arrDistricts[j] );
			if ( arrList == null ) {
				continue;
			}
			for ( int k=0; k< arrList.size(); k++ ) {
				buf.append( buf.length() > 0 ? "&" : "" );
				buf.append( arrDistricts[j] ).append( "=" ).append( arrList.get(k) ).append( "=" ).append( arrList.get(k) );
			}
		}		
		return buf.toString( );
		
	}
	
	/**
	 * 随机取得区域和板块
	 */
	public String getRadomDistrict( ) {
		if ( arrDistricts == null ) return "All";
		if ( arrDistricts.length == 0 ) return "All";
		return arrDistricts[(int)(Math.random()*arrDistricts.length)];
	}
	
	public String getRadomBlock( String district ) {
		ArrayList<String> arrBlock = mapBlock.get( district );
		if ( arrBlock == null ) return "All";
		return arrBlock.get( (int)(Math.random()*arrBlock.size()) );
	}
	
	public static void main( String[] args ) {
		System.out.println( (int)(10*0.99) );
	}


}
