package com.undao.http;

import java.io.*;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.client.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.*;

import com.alibaba.fastjson.JSONObject;

public class WeChatUtils {
	
	final static String APP_ID = "wx6d564ec1cbd75e7a";
	final static String APP_SECRET = "e358ecce7c9c37224f08aacdada2899e";
	
	private static int EXPIRES_IN_OF_ACCESS_TOKEN = 3600;
	
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
				System.out.println("WeChatUtils>>request url failed, http code=" + response.getStatusLine().getStatusCode()+ ", url=" + url);
				return null;
			}
			HttpEntity entity = response.getEntity();						//reponse返回的数据在entity中
			if ( entity != null ) {
				String resultStr = EntityUtils.toString(entity,"utf-8");	//将数据转化为string格式
				JSONObject result = JSONObject.parseObject(resultStr);		//将结果转化为json格式
				return result;
			}
		} catch(ClientProtocolException e) {
			System.out.println( "WeChatUtils.httpGet>>request url=" + url + ", ClientProtocolException, msg=" + e.getMessage());
			e.printStackTrace( );
		} catch (Exception e) {
			System.out.println( "WeChatUtils.httpGet>>request url=" + url + ", Exception, msg=" + e.getMessage());
			e.printStackTrace( );
		} finally {
	        if ( response != null ) {
	        	try { response.close();  } catch(IOException e) { e.printStackTrace();	}
	        } 
	    }
	    return null;
	}
	
	/**
	 * 定义一个post请求的方法：
	 * url：需要Post请求的网址
	 * return: 返回请求时网页相应的数据，用json存储
	 */
	public final static JSONObject httpPost( String url, JSONObject jsonObject ) {
		CloseableHttpClient httpClient = HttpClients.createDefault();		//创建httpClient
		HttpPost httpPost = new HttpPost( url );							//生成一个请求
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();	//配置请求的一些属性
		httpPost.setConfig( requestConfig );	//为请求设置属性
		httpPost.setHeader( "Content-type", "text/plain; charset=UTF-8" );
		httpPost.setHeader( "User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)" );
		httpPost.setHeader( "IConnection", "close" );

		CloseableHttpResponse response = null;
		try {
			httpPost.setEntity( new StringEntity(jsonObject.toString()) );
			response = httpClient.execute( httpPost );
				
			//如果返回结果的code不等于200，说明出错了
			if (response.getStatusLine().getStatusCode() != 200) {
				System.out.println("WeChatUtils>>request url failed, http code=" + response.getStatusLine().getStatusCode()+ ", url=" + url);
				return null;
			}
			HttpEntity entity = response.getEntity();					//reponse返回的数据在entity中
			if ( entity != null ) {
				String resultStr = EntityUtils.toString(entity,"utf-8");	//将数据转化为string格式
				JSONObject result = JSONObject.parseObject(resultStr);		//将结果转化为json格式
				return result;
			}
		} catch(ClientProtocolException e) {
			System.out.println( "WeChatUtils.httpPost>>request url=" + url + ", ClientProtocolException, msg=" + e.getMessage());
			e.printStackTrace( );
		} catch (Exception e) {
			System.out.println( "WeChatUtils.httpPost>>request url=" + url + ", Exception, msg=" + e.getMessage());
			e.printStackTrace( );
		} finally {
	        if ( response != null ) {
	        	try { response.close();  } catch(IOException e) { e.printStackTrace();	}
	        } 
	    }
	    return null;
	}
	
	/**
	 * 获取AccessToken
	 */
	public final static String getAccessToken( ) {
		StringBuilder bufURL = new StringBuilder( "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" );
		bufURL.append( "&appid=" ).append( APP_ID ).append( "&secret=" ).append( APP_SECRET );
		JSONObject res = httpGet( bufURL.toString() );
		String access_token = null;
		if ( res != null ) {
			access_token = res.getString( "access_token" );
			EXPIRES_IN_OF_ACCESS_TOKEN = res.getIntValue( "expires_in" );
		} else {
			access_token = "CannotResolveAccessToken";
		}
		return access_token;
	}
	
	/**
	 * 获取OpenID等相关信息
	 */
	public final static JSONObject getOpenIDAbout( String jsCode ) {	
		StringBuilder bufURL = new StringBuilder( "https://api.weixin.qq.com/sns/jscode2session?grant_type=authorization_code" );
		bufURL.append( "&appid=" ).append( APP_ID ).append( "&secret=" ).append( APP_SECRET ).append( "&js_code=" ).append( jsCode );
		JSONObject res = httpGet( bufURL.toString() );
		return res;
	}
	
	/**
	 * 获得刷新时长
	 */
	public final static int getSecondsOfRefreshAccessToken( ) {
		return EXPIRES_IN_OF_ACCESS_TOKEN * 3 / 4;
	}
	
	/**
	 * 创建永久有效的二维码
	 */
	public final static void createWxaqrCode( String accessToken, String path, int width, File aCodeFile ) {
		String url = "https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?access_token=" + accessToken;
		JSONObject param = new JSONObject();
        param.put("path", path);
        param.put("width", width);
        
    	CloseableHttpClient httpClient = HttpClients.createDefault();		//创建httpClient
		HttpPost httpPost = new HttpPost( url );							//生成一个请求
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();	//配置请求的一些属性
		httpPost.setConfig( requestConfig );	//为请求设置属性
		httpPost.setHeader( "Content-type", "application/json" );
		
		InputStream inputStream = null;
        OutputStream outputStream = null;
		CloseableHttpResponse response = null;
		try {
			httpPost.setEntity( new StringEntity( param.toJSONString(), "utf-8" ) );
			response = httpClient.execute( httpPost );
			if (response.getStatusLine().getStatusCode() != 200) {
				System.out.println("WeChatUtils.createWxaqrCode>>request url failed, http code=" + response.getStatusLine());
			}
			inputStream = new BufferedInputStream(response.getEntity().getContent());
			if ( !aCodeFile.exists() ) {
				aCodeFile.createNewFile( );
			} 
            outputStream = new FileOutputStream( aCodeFile );
            int len = 0;
            byte[] buf = new byte[1024*1024];
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
		} catch( Exception e ) {
			System.out.println( "WeChatUtils.createWxaqrCode>>request url Exception, Exception, msg=" + e.getMessage());
			e.printStackTrace( );
		} finally {
	        if ( inputStream != null ) {
                try { inputStream.close();  } catch(IOException e) { e.printStackTrace();	}
            }
            if ( outputStream != null ) {
                try { outputStream.close(); } catch(IOException e) { e.printStackTrace();	}
            }
            if ( response != null ) {
	        	try { response.close();  } catch(IOException e) { e.printStackTrace();	}
	        } 
	    }
    }
	
	
	/**
	 * 创建无限制的二维码
	 * scene: 必须30位，可以包含-等字符
	 */
	public final static void createWxacodeUnlimit( String accessToken, String scene, String path, File aCodeFile ) {
		String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
		JSONObject param = new JSONObject();
		param.put("scene", scene);
        param.put("path", path);
        param.put("width", 430);
        param.put("auto_color", false);
        Map<String,Object> line_color = new HashMap<>();
        line_color.put("r", "0");
        line_color.put("g", "0");
        line_color.put("b", "0");
        param.put("line_color", line_color);
        
    	CloseableHttpClient httpClient = HttpClients.createDefault();		//创建httpClient
		HttpPost httpPost = new HttpPost( url );							//生成一个请求
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();	//配置请求的一些属性
		httpPost.setConfig( requestConfig );	//为请求设置属性
		httpPost.setHeader( "Content-type", "application/json" );
		
		InputStream inputStream = null;
        OutputStream outputStream = null;
		CloseableHttpResponse response = null;
		try {
			httpPost.setEntity( new StringEntity( param.toJSONString(), "utf-8" ) );
			response = httpClient.execute( httpPost );
			if (response.getStatusLine().getStatusCode() != 200) {
				System.out.println("WeChatUtils.createWxacodeUnlimit>>request url failed, http code=" + response.getStatusLine());
			}
			inputStream = new BufferedInputStream(response.getEntity().getContent());
			if ( !aCodeFile.exists() ) {
				aCodeFile.createNewFile( );
			} 
            outputStream = new FileOutputStream( aCodeFile );
            int len = 0;
            byte[] buf = new byte[1024*1024];
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
		} catch( Exception e ) {
			System.out.println( "WeChatUtils.createWxacodeUnlimit>>request url Exception, Exception, msg=" + e.getMessage());
			e.printStackTrace( );
		} finally {
	        if ( inputStream != null ) {
                try { inputStream.close();  } catch(IOException e) { e.printStackTrace();	}
            }
            if ( outputStream != null ) {
                try { outputStream.close(); } catch(IOException e) { e.printStackTrace();	}
            }
            if ( response != null ) {
	        	try { response.close();  } catch(IOException e) { e.printStackTrace();	}
	        } 
	    }
    }
	
	public static void  main( String[] args ) {
		System.out.println( getAccessToken() );
		System.out.println( getSecondsOfRefreshAccessToken() );
	}

}
