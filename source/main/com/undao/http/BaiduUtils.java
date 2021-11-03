package com.undao.http;

import java.io.*;
import java.net.*;

import com.alibaba.fastjson.JSONObject;

public class BaiduUtils {
	
	private final static String AK = "RKRR5fRuFrtqU7GaLuMG5Ra8mXU0WuIx";
	private final static String DRIVING = "http://api.map.baidu.com/routematrix/v2/driving?output=json&tactics=12&ak=";		//驾车时两点间距
	private final static String COORDINATE = "http://api.map.baidu.com/geocoder/v2/?output=json&ak=";						//根据地址求经纬度
	
	public static final String loadJSON( String url ) {
		StringBuilder json = new StringBuilder();
		try {
			URL oracle = new URL( url );
			URLConnection yc = oracle.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				json.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {} catch (IOException e) {}
		return json.toString();
    }
	
    /**
     * 调用百度地图API根据地址，根据地址获取坐标
     */
	public static final String[] getCoordinate(String address) {
		String[] arrLocation = new String[2];		
		if (address == null || address.length()<2) {
			return arrLocation;
		}
		address = address.replaceAll("\\s*", "").replace("#", "栋");
		StringBuilder bufURL = new StringBuilder( COORDINATE );
		bufURL.append( AK ).append( "&address=" ).append( address );
		String json = loadJSON( bufURL.toString() );
		if (json == null || json.length()<3) {
			return null;
		}
		JSONObject obj = JSONObject.parseObject(json);
		if ( obj.getString("status").equals("0") ) {
			double lng = obj.getJSONObject("result").getJSONObject("location").getDouble("lng"); //经度
			double lat = obj.getJSONObject("result").getJSONObject("location").getDouble("lat"); //纬度
			arrLocation[0] = Double.toString( lng );
			arrLocation[1] = Double.toString( lat );
			return arrLocation;
		} 
		return null;
    }
	
	/**
	 * 获取两点间直线距离
	 */
    public static final double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        //纬度
        double lat1 = (Math.PI / 180) * latitude1;
        double lat2 = (Math.PI / 180) * latitude2;

        //经度
        double lon1 = (Math.PI / 180) * longitude1;
        double lon2 = (Math.PI / 180) * longitude2;

        //地球半径(公里)
        double R = 6371;

        //两点间距离KM
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
        return d;
    }
    
    public static final double getDistance( String fromAddress, String toAddress ) {
    	String[] from = getCoordinate( fromAddress );
		String[] to = getCoordinate( toAddress );
		if ( from == null || to == null ) {
			return (double)0;
		}
		return getDistance(Double.parseDouble(from[0]), Double.parseDouble(from[1]), Double.parseDouble(to[0]), Double.parseDouble(to[1]) );
    }
    
	/**
	 * 依据百度地图获取两点间驾车距离 
	 */
	public static final String getDriveDistance(String fromAddress, String toAddress) {
		String[] from = getCoordinate( fromAddress );
		String[] to = getCoordinate( toAddress );
		StringBuilder bufURL = new StringBuilder( DRIVING );
		bufURL.append( AK );
		if ( from != null ) {
			bufURL.append( "&origins=" ).append( from[1] ).append( "," ).append( from[0] );
		}
		if ( to != null ) {
			bufURL.append( "&destinations=" ).append( to[1] ).append( "," ).append( to[0] );
		}
		
		StringBuilder bufJSON = new StringBuilder();
		try {
			URL url = new URL( bufURL.toString() );
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.connect();
			BufferedReader bReader = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "utf-8"));
			String str = null;
			while ((str = bReader.readLine()) != null) {
				bufJSON.append( str );
			}
			bReader.close();
			conn.disconnect();
		} catch (MalformedURLException e) {} catch (IOException e) {}
		return bufJSON.toString();
	}
	
    public static void main(String[] args) {
        //String[] arrLocation = getCoordinate( "凉山彝族自治州雷波县" );
        //System.out.println( "经纬度为：" + arrLocation[0] + "," + arrLocation[1] );
    	System.out.println( getDistance("上海市", "长春市" ) );
    }	

}
