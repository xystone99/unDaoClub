package com.undao.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class AliUtils {
	
	final static String CORP_ID = "ding28d28855acf60c37";
	final static String CORP_SECRET = "HPK6PE99uu5qEIaiCdzjVZH0MY1WRuUv5FKByMGghTXngPa6-2TcphSGgCp96i_f";
	final static String NONCE_STR = "1688";
	final static String AGENT_ID = "46264922";
	
	/**
	 * 定义一个get请求的方法：
	 * url：需要Get请求的网址
	 * return: 返回请求时网页相应的数据，用json存储
	 */
	public final static JSONObject httpGet( String url ) {
		CloseableHttpClient httpClient=HttpClients.createDefault();		//创建httpClient
		HttpGet httpGet = new HttpGet( url );							//生成一个请求
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();	//配置请求的一些属性
		httpGet.setConfig( requestConfig );								//为请求设置属性
		
		CloseableHttpResponse response = null;
		try {
			response=httpClient.execute( httpGet );
				
			//如果返回结果的code不等于200，说明出错了
			if (response.getStatusLine().getStatusCode() != 200) {
				System.out.println("AliUtils>>request url failed, http code=" + response.getStatusLine().getStatusCode()+ ", url=" + url);
				return null;
			}
			HttpEntity entity = response.getEntity();					//reponse返回的数据在entity中
			if ( entity != null ) {
				String resultStr = EntityUtils.toString(entity,"utf-8");	//将数据转化为string格式
				JSONObject result = JSONObject.parseObject(resultStr);		//将结果转化为json格式
				if ( result.getInteger("errcode") == 0 ) {					//如果返回值得errcode值为0，则成功
					result.remove( "errcode" );							//移除一些没用的元素
					result.remove( "errmsg" );
					return result;										//返回有用的信息
				} else {												//返回结果出错了，则打印出来
					System.out.println( "AliUtils>>request url=" + url + ",return value=" );
					System.out.println(resultStr);
					int errCode = result.getInteger( "errcode" );
					String errMsg = result.getString( "errmsg" );
					throw new Exception( "ErrorCode:"+errCode+"ErrorMsg"+errMsg ); 
				}
			}
		} catch(ClientProtocolException e) {
			System.out.println( "AliUtils>>request url=" + url + ", exception, msg=" + e.getMessage());
			e.printStackTrace( );
		} catch (Exception e) {
			System.out.println( "AliUtils>>request url=" + url + ", exception, msg=" + e.getMessage());
			e.printStackTrace( );
		} finally {
	        if ( response != null ) {
	        	try { response.close();  } catch(IOException e) { e.printStackTrace();	}
	        } 
	    }
	    return null;
	}
	
	public final static String getAccessToken( ) {
		return getAccessToken( CORP_ID, CORP_SECRET );
	}
	
	public final static String getAccessToken( String corpID, String corpSecret ) {	
		String url = "https://oapi.dingtalk.com/gettoken?corpid=" + corpID + "&corpsecret=" + corpSecret;
		JSONObject res = httpGet( url );
		String access_token = "";
		if ( res != null ) {
			access_token = res.getString( "access_token" );
		} else {
			new Exception( "Cannot resolve field access_token from oapi resonpse" );
		}
		return access_token;
	}
	

	public final static String getTicket( String accessToken ) {
		String url = "https://oapi.dingtalk.com/get_jsapi_ticket?access_token=" + accessToken;
		JSONObject res = httpGet( url );
		String ticket = "";
		if ( res != null ) {
			ticket = res.getString( "ticket" );
		} else {
			new Exception( "Cannot resolve field ticket from oapi resonpse" );
		}
		return ticket;
	}
	
	/**
	 * 生成签名的函数
	 * ticket：签名数据
	 * nonceStr：签名用的随机字符串，从properties文件中读取
	 * timeStamp：生成签名用的时间戳
	 * url：当前请求的URL地址
	 */
	public final static String getSign(String ticket, String nonceStr, long timeStamp, String url) throws Exception {
		String plain = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + String.valueOf(timeStamp) + "&url=" + url;
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");		//安全hash算法
			sha1.reset( );
			sha1.update( plain.getBytes("UTF-8") );							//根据参数产生hash值
			return bytesToHex( sha1.digest() );
		} catch (NoSuchAlgorithmException e) {
			throw new Exception( e.getMessage() );
		} catch (UnsupportedEncodingException e) {
			throw new Exception( e.getMessage() );
		}
	}
	
	/**
	 * 将bytes类型的数据转化为16进制类型
	 */
	private final static String bytesToHex( byte[] hash ) {                    
		Formatter formatter = new Formatter( );
		for ( byte b : hash ) {
			formatter.format( "%02x", b );
		}
		String result = formatter.toString( );
		formatter.close( );
		return result;
	}
	
	/**
	 * 将所有需要传送到前端的参数进行打包，在前端会调用jsapi提供的dd.config接口进行签名的验证
	 * request：	在钉钉中点击微应用图标跳转的url地址
	 * return：	将需要的参数打包好，按json格式打包
	 * 
	 * 以http://localhost/test.do?a=b&c=d为例：request.getRequestURL的结果是http://localhost/test.do；request.getQueryString的返回值是a=b&c=d。
	 * 封装好所有需要的参数，并且传递到企业应用网址的前端H5中。需要的参数有corpId,agentId,ticket,signature,nonceStr,timeStamp,url.
	 * 其中nonceStr,timeStamp,url用来在服务器后台生成signatrue签名，然后将ticket,nonceStr,timeStamp和signatrue传送到前台，前台网页就会调用jsapi的dd.config函数重新生成signatrue,和传进的signatrue进行比较，来实现验证过程。
	 */
	public static String getConfig( HttpServletRequest request ) {
		String urlString = request.getRequestURL().toString();
		String queryString = request.getQueryString();
			
		String url = null;
		if ( queryString != null ) {
			url = urlString + queryString;
		} else {
			url = urlString;
		}
			
		String corpId = CORP_ID;        						//一些比较重要的不变得参数本人存储在properties文件中
		String corpSecret = CORP_SECRET;
		String nonceStr = NONCE_STR;							//随机串,自己定义
		String agentId = AGENT_ID;     							//应用的标识,编辑企业应用可以看到
		long timeStamp = System.currentTimeMillis()/1000;		//时间戳,当前时间,但是前端和服务端进行校验时候的值要一致
		String signedUrl = url;                                 //请求链接的参数，这个链接主要用来生成signatrue，并不需要传到前端
		String accessToken = null;                              //token参数
		String ticket = null;                                   //ticket参数
		String signature = null;                                //签名参数
				
		try {
			accessToken = getAccessToken( corpId, corpSecret );
			ticket = getTicket( accessToken );
			signature = getSign( ticket,nonceStr,timeStamp,signedUrl );
		} catch(Exception e) {
			e.printStackTrace( );
		}
		return "{\"ticket\":\"" + ticket + "\",\"signature\":\"" + signature + "\",\"nonceStr\":\"" + nonceStr + "\",\"timeStamp\":\"" + timeStamp + "\",\"corpId\":\"" + corpId + "\",\"agentId\":\"" + agentId + "\"}";
	}

	public final static String getUserID( String accessToken, String code ) {
		String url = "https://oapi.dingtalk.com/user/getuserinfo?access_token=" + accessToken + "&code=" + code;
		JSONObject res = httpGet( url );
		String userID = "";
		if ( res != null ) {
			userID = res.getString( "userid" );
		} else {
			new Exception( "Cannot resolve field userID from oapi resonpse" );
		}
		return userID;
	}	
	
	public final static String getUserName( String accessToken, String userID ) {
		String url = "https://oapi.dingtalk.com/user/get?access_token=" + accessToken + "&userid=" + userID;
			
		JSONObject res = httpGet( url );
		String userName = "";
		if ( res != null ) {
			userName = res.getString( "name" );
		} else {
			new Exception( "Cannot resolve field name from oapi resonpse" );
		}
		return userName;
	}

}
