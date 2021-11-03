/**
 * Created At 2014-4-29 上午09:44:10
 */
package com.undao.http;

import java.util.ArrayList;
import java.util.HashMap;

import com.undao.database.ParamLayer;

/**
 * @author Administrator
 *
 */
public class GraspHouseList implements HttpConstants {
	
	public static final String COLLECT_SQL = "INSERT INTO trn_collect_record(web_flg,trade_type,district,block,estate_zh,title,count_f,at_floor,total_floor,area,price,intro,url,input_date)VALUES(?,?,?,?,?,?,?,?,?,?, ?,?,?, NOW())";
	public static final String PERSONAL_SQL = "INSERT INTO trn_personal_record(web_flg,trade_type,district,block,estate_zh,title,count_f,at_floor,total_floor,area,price,url,linkman,mobile,input_date,last_update)VALUES(?,?,?,?,?,?,?,?,?,?, ?,?,?,?, NOW(),NOW())";
	
	private ArrayList<HashMap<String,String>> arrayList = new ArrayList<HashMap<String,String>>();
	private String webFlg = null;
	
	/**
	 * Constructor
	 */
	public GraspHouseList( ) {	
	}
	
	public GraspHouseList( String webFlg ) {
		this.webFlg = webFlg;
	}
	
	/**
	 * 类属性
	 */
	public String getWebFlg ( ) {
		return webFlg;
	}
		
	/**
	 * 房源设置
	 */
	public void addHouse( HashMap<String,String> house ) {
		arrayList.add( house );
	}
	
	public HashMap<String,String> getHouse( int index ) {
		if ( index >= arrayList.size() ) {
			return null;
		}
		return arrayList.get( index );
	}
	
	/**
	 * 返回房源数目
	 */
	public int size( ) {
		return arrayList.size( );
	}
	
	/**
	 * 返回字段值
	 */
	public String getWebFlg( int index ) {
		return arrayList.get(index).get( WEB_FLG );
	}
	
	public String getTradeType( int index ) {
		return arrayList.get(index).get( TRADE_TYPE );
	}
	
	public String getDistrict( int index ) {
		return arrayList.get(index).get( DISTRICT );
	}
	
	public String getBlock( int index ) {
		return arrayList.get(index).get( BLOCK );
	}
	
	public String getEstateZh( int index ) {
		return arrayList.get(index).get( ESTATE_ZH );
	}
	
	public String getTitle( int index ) {
		return arrayList.get(index).get( TITLE );
	}
	
	public String getCountF( int index ) {
		return arrayList.get(index).get( COUNT_F );
	}
	
	public String getAtFloor( int index ) {
		return arrayList.get(index).get( AT_FLOOR );
	}
	
	public String getTotalFloor( int index ) {
		return arrayList.get(index).get( TOTAL_FLOOR );
	}
	
	public String getArea( int index ) {
		return arrayList.get(index).get( AREA );
	}
	
	public String getPrice( int index ) {
		return arrayList.get(index).get( PRICE );
	}
	
	public String getIntro( int index ) {
		return arrayList.get(index).get( INTRO );
	}
	
	public String getURL( int index ) {
		return arrayList.get(index).get( URL );
	}
	
	public String getLinkMan( int index ) {
		return arrayList.get(index).get( LINK_MAN );
	}
	
	public String getMobile( int index ) {
		return arrayList.get(index).get( MOBILE );
	}
	
	/**
	 * 返回参数配置列表，供保存到数据库中(公司房源)
	 */
	public ArrayList<ParamLayer> getParamLayerList( ) {
		ArrayList<ParamLayer> list = new ArrayList<ParamLayer>();
		for ( int j=0; j<arrayList.size(); j++ ) {
			ParamLayer layer = new ParamLayer();
			layer.addParam(ParamLayer.PARAM_STRING, getWebFlg() );
			layer.addParam(ParamLayer.PARAM_STRING, getTradeType(j) );
			layer.addParam(ParamLayer.PARAM_STRING, getDistrict(j) );
			layer.addParam(ParamLayer.PARAM_STRING, getBlock(j) );
			layer.addParam(ParamLayer.PARAM_STRING, getEstateZh(j) );
			layer.addParam(ParamLayer.PARAM_STRING, getTitle(j) );
			layer.addParam(ParamLayer.PARAM_STRING, getCountF(j) );
			layer.addParam(ParamLayer.PARAM_STRING, getAtFloor(j) );
			layer.addParam(ParamLayer.PARAM_STRING, getTotalFloor(j) );
			layer.addParam(ParamLayer.PARAM_STRING, getArea(j) );
			layer.addParam(ParamLayer.PARAM_STRING, getPrice(j) );
			layer.addParam(ParamLayer.PARAM_STRING, getIntro(j) );
			layer.addParam(ParamLayer.PARAM_STRING, getURL(j) );
			list.add( layer );
		}
		return list;
	}
	
	public ParamLayer getParamLayer( int index ) {
		ParamLayer layer = new ParamLayer();
		layer.addParam(ParamLayer.PARAM_STRING, getWebFlg() );
		layer.addParam(ParamLayer.PARAM_STRING, getTradeType(index) );
		layer.addParam(ParamLayer.PARAM_STRING, getDistrict(index) );
		layer.addParam(ParamLayer.PARAM_STRING, getBlock(index) );
		layer.addParam(ParamLayer.PARAM_STRING, getEstateZh(index) );
		layer.addParam(ParamLayer.PARAM_STRING, getTitle(index) );
		layer.addParam(ParamLayer.PARAM_STRING, getCountF(index) );
		layer.addParam(ParamLayer.PARAM_STRING, getAtFloor(index) );
		layer.addParam(ParamLayer.PARAM_STRING, getTotalFloor(index) );
		layer.addParam(ParamLayer.PARAM_STRING, getArea(index) );
		layer.addParam(ParamLayer.PARAM_STRING, getPrice(index) );
		layer.addParam(ParamLayer.PARAM_STRING, getIntro(index) );
		layer.addParam(ParamLayer.PARAM_STRING, getURL(index) );
		return layer;
	}
	
	/**
	 * 返回参数配置列表，供保存到数据库中(个人房源)
	 */
	public ArrayList<ParamLayer> getPersonalParamLayerList( ) {
		ArrayList<ParamLayer> list = new ArrayList<ParamLayer>();
		for ( int j=0; j<arrayList.size(); j++ ) {
			ParamLayer layer = new ParamLayer();
			layer.addParam(ParamLayer.PARAM_STRING, getWebFlg() );
			layer.addParam(ParamLayer.PARAM_STRING, getTradeType(j) );
			layer.addParam(ParamLayer.PARAM_STRING, getDistrict(j) );
			layer.addParam(ParamLayer.PARAM_STRING, getBlock(j) );
			layer.addParam(ParamLayer.PARAM_STRING, getEstateZh(j) );
			layer.addParam(ParamLayer.PARAM_STRING, getTitle(j) );
			layer.addParam(ParamLayer.PARAM_STRING, getCountF(j) );
			layer.addParam(ParamLayer.PARAM_STRING, getAtFloor(j) );
			layer.addParam(ParamLayer.PARAM_STRING, getTotalFloor(j) );
			layer.addParam(ParamLayer.PARAM_STRING, getArea(j) );
			layer.addParam(ParamLayer.PARAM_STRING, getPrice(j) );
			layer.addParam(ParamLayer.PARAM_STRING, getURL(j) );
			layer.addParam(ParamLayer.PARAM_STRING, getLinkMan(j) );
			layer.addParam(ParamLayer.PARAM_STRING, getMobile(j) );
			list.add( layer );
		}
		return list;
	}
	
	public ParamLayer getPersonalParamLayer( int index ) {
		ParamLayer layer = new ParamLayer();
		layer.addParam(ParamLayer.PARAM_STRING, getWebFlg() );
		layer.addParam(ParamLayer.PARAM_STRING, getTradeType(index) );
		layer.addParam(ParamLayer.PARAM_STRING, getDistrict(index) );
		layer.addParam(ParamLayer.PARAM_STRING, getBlock(index) );
		layer.addParam(ParamLayer.PARAM_STRING, getEstateZh(index) );
		layer.addParam(ParamLayer.PARAM_STRING, getTitle(index) );
		layer.addParam(ParamLayer.PARAM_STRING, getCountF(index) );
		layer.addParam(ParamLayer.PARAM_STRING, getAtFloor(index) );
		layer.addParam(ParamLayer.PARAM_STRING, getTotalFloor(index) );
		layer.addParam(ParamLayer.PARAM_STRING, getArea(index) );
		layer.addParam(ParamLayer.PARAM_STRING, getPrice(index) );
		layer.addParam(ParamLayer.PARAM_STRING, getURL(index) );
		layer.addParam(ParamLayer.PARAM_STRING, getLinkMan(index) );
		layer.addParam(ParamLayer.PARAM_STRING, getMobile(index) );
		return layer;
	}
	
	/**
	 * Main Test
	 */
	public static void main( String[] args ) {
		String url = "http://esf.sh.soufun.com/house-a028-b01663/c2100-d2200-i39-l3100/";
		System.out.println( url );
		System.out.println( url.replaceFirst("-i3[\\d]{1,5}", "-i3"+120));
	}
	
}
