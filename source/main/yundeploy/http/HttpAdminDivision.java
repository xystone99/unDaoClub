package yundeploy.http;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.*;

import com.undao.http.HttpParser;

/**
 * @author Administrator
 *
 */
public class HttpAdminDivision {
	
	private static final String CHAR_SET = "GBK";
	private static HashMap<String, String> mapShortName = new HashMap<String,String>();
	
	/**
	 * 行政区划
	 */
	public static final String reg_Division = "<input type=\"hidden\" value='(.*?)' id=\"pyArr\" />";
	public static final Pattern p_Division = Pattern.compile( reg_Division, Pattern.CASE_INSENSITIVE );
	
	public static final String reg_Province = "var json = (.*?);";
	public static final Pattern p_Province = Pattern.compile( reg_Province, Pattern.CASE_INSENSITIVE );
	
	public final static ArrayList<String[]> getDivisionList( ) {
		ArrayList<String[]> arrayList = new ArrayList<String[]>();
		String url = "http://xzqh.mca.gov.cn/map";
		String q_Result = HttpParser.queryContentGET( url, null, CHAR_SET, null );
		Matcher m_Division = p_Division.matcher( q_Result );

		if ( !m_Division.find() ) {
			return null;
		}
		String strJSON = m_Division.group(1);
		try {
			JSONArray jsonArray = new JSONArray( strJSON );
			for ( int j=0; j<jsonArray.length(); j++ ) {
				String[] arrDivision = new String[5];
				arrDivision[0] = (String)jsonArray.getJSONObject(j).get( "cName" );
				arrDivision[1] = (String)jsonArray.getJSONObject(j).get( "code" );
				arrDivision[2] = (String)jsonArray.getJSONObject(j).get( "py" );
				arrDivision[3] = (String)jsonArray.getJSONObject(j).get( "jp" );
				arrDivision[4] = (String)jsonArray.getJSONObject(j).get( "qp" );
				arrayList.add( arrDivision );
			}
		} catch( Exception e ) {
			System.out.println( "HttpAdminDivision Failed......" );
		}
		return arrayList;
	}
	
	public final static ArrayList<String[]> getProvinceList( ) {
		ArrayList<String[]> arrayList = new ArrayList<String[]>();
		String url = "http://xzqh.mca.gov.cn/map";
		String q_Result = HttpParser.queryContentGET( url, null, CHAR_SET, null );
		Matcher m_Province = p_Province.matcher( q_Result );

		if ( !m_Province.find() ) {
			return null;
		}
		String strJSON = m_Province.group(1);
		try {
			JSONArray jsonArray = new JSONArray( strJSON );
			for ( int j=0; j<jsonArray.length(); j++ ) {
				String[] arrProvince = new String[3];
				String shengji = (String)jsonArray.getJSONObject(j).get( "shengji" );
				int p1 = shengji.indexOf( "(" );
				int p2 = shengji.lastIndexOf( ")" );
				arrProvince[0] = (String)jsonArray.getJSONObject(j).get( "quHuaDaiMa" );
				arrProvince[1] = shengji.substring(0, p1);
				arrProvince[2] = shengji.substring(p1+1, p2);
				arrayList.add( arrProvince );
				mapShortName.put( arrProvince[0], arrProvince[2] );
				System.out.println( arrProvince[0] + arrProvince[2] );
			}
		} catch( Exception e ) {
			System.out.println( "HttpAdminDivision Failed......" );
		}
		return arrayList;
	}
	
	public static HashMap<String, String> getShortMap( ) {
		return mapShortName;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<String[]> arrList = HttpAdminDivision.getProvinceList();
		for ( int j=0; j<arrList.size(); j++ ) {
			String[] arrProvince = arrList.get( j );
			StringBuilder bufProvince = new StringBuilder();
			bufProvince.append(j).append("=").append( arrProvince[0]).append("=").append( arrProvince[1]).append("=").append( arrProvince[2]);
			System.out.println( bufProvince.toString() );
		}
		
		System.out.println( "===========================================================================================" );
		/*
		arrList = HttpAdminDivision.getDivisionList();
		for ( int j=0; j<arrList.size(); j++ ) {
			String[] arrDivision = arrList.get( j );
			StringBuilder bufDivision = new StringBuilder();
			bufDivision.append(j).append("=").append( arrDivision[0]).append("=").append( arrDivision[1]).append("=").append( arrDivision[2]).append("=").append( arrDivision[3]).append("=").append( arrDivision[4]);
			System.out.println( bufDivision.toString() );
		}
		*/
	} 
	
}
