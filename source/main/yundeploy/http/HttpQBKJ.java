package yundeploy.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import com.undao.http.HttpParser;
import com.undao.utils.DateUtils;
import netscape.javascript.JSObject;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator
 *
 */
public class HttpQBKJ {

	private static String LOGIN_URL = "http://http://116.236.0.202:30019//x5/UI2/v_6267_nol_zh_CNs_desktopd_pc/portal/base/login/login.j";
	private static String ACCOUNT_URL = "http://http://116.236.0.202:30019//BusinessServer/business-action?bsessionid=05329BB6EFEDB493CC26443B7FB28202&process=/JY/WarehouseManage/process/Inventory/inventoryProcess&activity=DayInventoryActivity";

	private CookieStore cookieStore;

	public HttpQBKJ( String sessionID ) {

	}

	public void login( ) {
		this.cookieStore = new BasicCookieStore();
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout( 300*1000).setConnectTimeout(300*1000).build();

		HttpPost httpPost = new HttpPost( LOGIN_URL );
		httpPost.setConfig( requestConfig );

//		httpPost.setHeader( "Host", "116.236.0.202:30019" );
//		httpPost.setHeader( "Connection", "keep-alive" );
//		httpPost.setHeader( "Content-Length", "109" );
//		httpPost.setHeader( "Accept", "application/json, text/javascript, */*; q=0.01" );
//		httpPost.setHeader( "X-Requested-With", "XMLHttpRequest" );
//		httpPost.setHeader( "User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36" );
		httpPost.setHeader( "Content-Type", "application/x-www-form-urlencoded; charset=UTF-8" );
		//httpPost.setHeader( "Origin", "http://116.236.0.202:30019" );
		//httpPost.setHeader( "Referer", "http://116.236.0.202:30019/x5/UI2/v_/portal/pc/index.w?device=pc" );
//		httpPost.setHeader( "Accept-Encoding", "gzip, deflate" );
//		httpPost.setHeader( "Accept-Language", "zh-CN,zh;q=0.9" );
//		httpPost.setHeader( "Cookie", "JSESSIONID=7309BED3AC5AF29F8F637CC0CF433DED; request-use-base64=false" );

		Map<String,Object> param = new HashMap<String,Object>();
		param.put( "syscode", "true" );
		param.put( "username", "zhangd" );
		param.put( "password", ":d3d027585c0a863747f0fac5517fa497" );
		param.put( "loginDate", "2021-11-16" );
		param.put( "language", "zh_CN" );

		StringEntity entity = new StringEntity( param.toString(),"utf-8" );
		httpPost.setEntity( entity );

		try {
			System.out.println( "A" );
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
			//System.out.println(EntityUtils.toString(httpResponse.getEntity()));
			System.out.println( "B" );

			int statusCode = httpResponse.getStatusLine().getStatusCode();
			System.out.println( statusCode );
			if (statusCode != HttpStatus.SC_OK) {
				System.out.println("Connection failed 错误代码：" + statusCode);
			} else {
				System.out.println( statusCode );
			}

			System.out.println( "C" );
			List<Cookie> cookieList = cookieStore.getCookies();
			for ( Cookie cookie : cookieList ) {
				System.out.println( cookie.getName() + "=" + cookie.getValue() );
			}
			System.out.println( "D" );
			httpClient.close();
		} catch ( ClientProtocolException cpe ) {
			System.out.println( "CPE" );
			System.out.print( cpe.getMessage() );
		} catch ( IOException ioe ) {
			System.out.println( "IOE" );
			System.out.print( ioe.getMessage() );
		}
	}

	public String updateAccount( String fromDate, String toDate ) {
		StringBuilder buf = new StringBuilder();
		buf.append( "{" );
		buf.append( "\"contentType\": \"json\"," );
		buf.append( "\"accept\": \"application/json\"," );
		buf.append( "\"process\": \"/JY/WarehouseManage/process/Inventory/inventoryProcess\"," );
		buf.append( "\"activity\": \"DayInventoryActivity\"," );
		buf.append( "\"actionFlag\": \"__action_0__\"," );
		buf.append( "\"executor\": \"\"," );
		buf.append( "\"executeContext\": \"\"," );
		buf.append( "\"action\": \"DayInventoryAction\"," );
		buf.append( "\"parameters\": {" );
		buf.append( "\"fDate\": \"[Date:" );
		buf.append( fromDate );
		buf.append( "]\"" );
		buf.append( "}," );
		buf.append( "}" );
		HttpPost httpPost = new HttpPost( ACCOUNT_URL );
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore( cookieStore ).build();
		try {
			BasicResponseHandler handler = new BasicResponseHandler();
			StringEntity entity = new StringEntity( buf.toString(), "utf-8");//解决中文乱码问题
			entity.setContentEncoding( "UTF-8" );
			entity.setContentType( "application/json" );
			httpPost.setEntity( entity );
			return httpClient.execute(httpPost, handler);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String fromDate = "2021-10-31";
		String toDate = "2021-11-01";
		new HttpQBKJ("2BD4FF650B709BF530AA7B074BB84EE9").login( );
		//String result = new HttpQBKJ("2BD4FF650B709BF530AA7B074BB84EE9").updateAccount( "2021-11-01", "2021-11-02" );
		//System.out.println( result );
	} 
	
}
