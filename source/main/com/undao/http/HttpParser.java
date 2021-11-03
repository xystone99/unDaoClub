/**
 * HttpParser.java
 *
 * Created on 20080305, 11:31 A.M
 */

package com.undao.http;

/**
 * @author X.Stone
 *
 */
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.*;

import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.json.*;

public class HttpParser implements HttpConstants {
	
	public static boolean DEBUG = false;
	
	public static final HttpURLConnection getCONN( String strURL ) {
		try {
			URL url = new URL( strURL );
			URLConnection urlConn = url.openConnection( );
			//此处的urlConn对象实际上是根据URL的 请求协议(此处是http)生成的URLConnection类的子类HttpURLConnection,
			//故此处最好将其转化为HttpURLConnection类型的对象,以便用到HttpURLConnection更多的API.
			HttpURLConnection httpUrlConn = (HttpURLConnection)urlConn;
			
			httpUrlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
			httpUrlConn.setRequestProperty("Accept", "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-powerpoint, application/vnd.ms-excel, application/msword, *//*");
			httpUrlConn.setRequestProperty("Accept-Language", "zh-cn");
			httpUrlConn.setRequestProperty("UA-CPU", "x86");
			httpUrlConn.setRequestProperty("Accept-Encoding", "gzip");//为什么没有deflate呢
			httpUrlConn.setRequestProperty("Content-type", "text/html");
			httpUrlConn.setRequestProperty("Connection", "close"); //keep-Alive，有什么用呢，你不是在访问网站，你是在采集。嘿嘿。减轻别人的压力，也是减轻自己。 
			httpUrlConn.setUseCaches(false);//不要用cache，用了也没有什么用，因为我们不会经常对一个链接频繁访问。（针对程序）
			httpUrlConn.setConnectTimeout(6 * 1000);
			httpUrlConn.setReadTimeout(6*1000);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true); 
			
			//设定传送的内容类型是可序列化的java对象 (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)   
			httpUrlConn.setRequestProperty("Content-type", "application/x-java-serialized-object");   
			// 设定请求的方法为"POST"，默认是GET   
			httpUrlConn.setRequestMethod( "POST" );
			return httpUrlConn;
		} catch( MalformedURLException urle ) {
		} catch( IOException ioe ) {			
		}
		return null;
	}
	
	public static final String requestPath( HttpServletRequest request ) { 
		StringBuffer buf = request.getRequestURL( );
		return buf.substring( 0, buf.lastIndexOf("/") );
	}

	public static final String requestBasePath( HttpServletRequest request ) {
		StringBuffer buf = request.getRequestURL( );
		return buf.substring( 0, buf.indexOf("/", 7) );
	}
	
	public static final String extractRootPath( String strURL ) {
		StringBuilder bufPath = new StringBuilder( );
		try {
			URL url = new URL( strURL );
			bufPath.append( url.getProtocol() ).append( "://" ).append( url.getHost() ).append( ":" ).append( url.getPort() );
		} catch (MalformedURLException mue) {
			if(DEBUG) System.out.println( "HttpParser.extractRootPath()>>" + mue.getMessage() );
		}
		return bufPath.toString( );
	}
	
	public static final String encodeUnicodeString( Object uncodeStr ) {
		try {
			return URLEncoder.encode( (String)uncodeStr, "UTF-8" );
		} catch( UnsupportedEncodingException uee ) {
			if(DEBUG) System.out.println( "HttpParser.encodeUnicodeString()>>" + uee.getMessage() );
		}
		return null;
	}
	
	public static final String escape( String strURL ) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer( );
		tmp.ensureCapacity( strURL.length() * 6 );
		for( i = 0; i < strURL.length(); i++ ) {
			j = strURL.charAt(i);
			if ( Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j) ) {
				tmp.append(j);
			} else if ( j < 256 ) {
				tmp.append( "%" );
				if ( j < 16 ) {
					tmp.append( "0" );
				}
				tmp.append( Integer.toString(j, 16) );
			} else {
				tmp.append( "%u" );
				tmp.append( Integer.toString(j, 16) );
			}
		}
		return tmp.toString( );
	}

	public static final String unescape( String strURL ) {
		StringBuffer tmp = new StringBuffer( );
		tmp.ensureCapacity( strURL.length() );
		int lastPos = 0, pos = 0;
		char ch;
		while( lastPos < strURL.length() ) {
			pos = strURL.indexOf( "%", lastPos );
			if ( pos == lastPos ) {
				if ( strURL.charAt(pos + 1) == 'u' ) {
					ch = (char)Integer.parseInt( strURL.substring(pos+2, pos+6), 16 );
					tmp.append( ch );
					lastPos = pos + 6;
				} else {
					ch = (char)Integer.parseInt( strURL.substring(pos+1, pos+3), 16 );
					tmp.append( ch );
					lastPos = pos + 3;
				}
			} else {
				if ( pos == -1 ) {
					tmp.append( strURL.substring(lastPos) );
					lastPos = strURL.length( );
				} else {
					tmp.append( strURL.substring(lastPos, pos) );
					lastPos = pos;
				}
			}
		}
		return tmp.toString( );
	}
	
	public static synchronized void buildHtml(String httpUrl, String targetFilePath, boolean replace) {
		BufferedInputStream in = null;
		FileOutputStream out = null;
		long start_time = System.currentTimeMillis();
		try {
			URL url = new java.net.URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("User-Agent","Mozilla/4.0");
			connection.connect();
			in = new BufferedInputStream( connection.getInputStream() );
			
			File targetFile = new File( targetFilePath );
			if ( !targetFile.exists( ) ) {
				targetFile.createNewFile( );
			} else if ( replace ) {
				targetFile.delete( );
				targetFile.createNewFile( );
			} else {
				return;
			}
			out = new FileOutputStream( targetFile );
            
			int count;
            byte data[ ] = new byte[ BUFFER_SIZE ];            
            while ( ( count = in.read( data, 0, BUFFER_SIZE ) ) != -1 ) {
                out.write( data, 0, count );
            }
            in.close( );
            out.close( );
        } catch( IOException ioe ) {
            ioe.printStackTrace( );
        } finally {
            if (out != null) try{out.close( );  } catch(IOException ioe) {   }
            if (in != null) try{in.close();  } catch(IOException ioe){  } 
        }
		long span_time = System.currentTimeMillis() - start_time;
        if(DEBUG) System.out.println( "HttpParser.buildHtml()>>执行时间:" + span_time + "毫秒" );
	}	
	
	public static final File downloadFile( String strURL, String destDIR ) {
		File tempFile = null;
		String extName = strURL.substring( strURL.lastIndexOf(".") );
		try {
			URL url = new URL( strURL );  
	        URLConnection con = url.openConnection( );  
	        con.setConnectTimeout( 30*1000 );		//设置请求超时为30S,做新浪时，设置为5S,本机正常，服务器异常。 
	        InputStream is = con.getInputStream( );  
	        
	        
	        byte[] bs = new byte[1024];				//1K的数据缓冲 
	        int len;								// 读取到的数据长度
	        tempFile = File.createTempFile("AAA", extName, new File(destDIR) );
	        OutputStream os = new FileOutputStream( tempFile );  
	        while ((len = is.read(bs)) != -1) {  
	        	os.write(bs, 0, len);  
	        }  
	        os.close( );  
	        is.close( ); 
		} catch( IOException ioe ) {
			if(DEBUG) System.out.println( "HttpParser.downloadFile()---Exception: " + ioe.getMessage() );
		}
		return tempFile;
	}
	
	public static File downloadFile(String strURL, String fileName, String destDir, String cookieList) {  
		File sf = new File( destDir );						//输出的文件流   
		if ( !sf.exists() ) {  
			sf.mkdirs( );  
		}  
		File dFile = new File( sf.getPath() + File.separator + fileName );
		try {
			URL url = new URL( strURL );  					//构造URL 
			URLConnection conn = url.openConnection();  	//打开连接    
			conn.setRequestProperty( "Cookie", cookieList );
			conn.setConnectTimeout(5*1000);  				//设置请求超时为5S 
			InputStream is = conn.getInputStream();  		//输入流 
			
			byte[] bs = new byte[1024];  					//1K的数据缓冲  
			int len;										//读取到的数据长度    
			OutputStream os = new FileOutputStream( dFile );  
			while ((len = is.read(bs)) != -1) {  
				os.write(bs, 0, len);  
			}  
			os.close();  
			is.close();  
		} catch( IOException ioe ) {
			if(DEBUG) System.out.println( "HttpParser.downloadFile()---Exception: " + ioe.getMessage() );
		}
		return dFile;
    } 
	
	/**
	 * 使用HttpURLConnection爬取页面内容
	 * @param strURL			: 爬取页面的URL地址
	 * @param acceptEncoding	: 使用的字符集
	 * @return
	 */
	public static final String queryContentPOST( String strURL, String acceptEncoding ) {		
		return queryContentPOST( strURL, "", "", acceptEncoding, null );
	}
	
	public static final String queryContentPOST( String strURL, String cookieList, String acceptEncoding ) {		
		return queryContentPOST( strURL, "", cookieList, acceptEncoding, null );
	}
	
	public static final String queryContentPOST( String strURL, String queryString, String cookieList, String acceptEncoding ) {		
		return queryContentPOST( strURL, queryString, cookieList, acceptEncoding, null );
	}
	
	public static final String queryContentPOST( String strURL, String queryString, String cookieList, String acceptEncoding, String referer ) {
		if ( !strURL.toLowerCase().startsWith("http://") && !strURL.toLowerCase().startsWith( "https://" ) ) {  
            strURL = "http://" + strURL;  
        }  
		try{
			URL url = new URL( strURL );
			HttpURLConnection httpUrlConn = (HttpURLConnection)url.openConnection( );
			HttpURLConnection.setFollowRedirects( false );
			if ( cookieList != null ) {
				httpUrlConn.setRequestProperty( "Cookie", cookieList );
			}	
			if ( referer != null ) {
				httpUrlConn.setRequestProperty( "Referer", referer ); 
			}
			httpUrlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
			httpUrlConn.setRequestMethod( "POST" );
			httpUrlConn.setDoOutput( true );
			OutputStreamWriter wr = new OutputStreamWriter( httpUrlConn.getOutputStream() );
			wr.write( queryString );
			wr.flush( );
			
			StringBuilder bufResult = new StringBuilder( );
			BufferedReader rd = new BufferedReader( new InputStreamReader(httpUrlConn.getInputStream(),acceptEncoding) );
			String ss;
			while ( (ss=rd.readLine())!=null ) {
				bufResult.append( ss );
			} 
			wr.close( );
			rd.close( );
			return bufResult.toString( );
		} catch( IOException ioe ) {
			return ioe.getMessage( );
		} 
	}
	
	public static final String queryJsonPOST( String strURL, JSONObject jsonOBJ, String acceptEncoding ) {
        try {
            URL url = new URL( strURL );
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);									//设置允许输出
            conn.setDoInput(true);

            conn.setUseCaches(false);								//设置不用缓存
            conn.setRequestMethod("POST");							//设置传递方式
            conn.setRequestProperty("Connection", "Keep-Alive");	//设置维持长连接
            conn.setRequestProperty("Charset", "UTF-8");			//设置文件字符集:
            
            byte[] data = (jsonOBJ.toString()).getBytes();				//转换为字节数组
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));		//设置文件长度
            conn.setRequestProperty("contentType", "application/json");		//设置文件类型:
            
            conn.connect();		//开始连接请求
            OutputStream  out = conn.getOutputStream();     
            
            out.write((jsonOBJ.toString()).getBytes());		//写入请求的字符串
            out.flush();
            out.close();
            
            if (conn.getResponseCode() == 200) {			//请求返回的状态
            	StringBuilder bufResult = new StringBuilder( );
    			BufferedReader rd = new BufferedReader( new InputStreamReader(conn.getInputStream(),acceptEncoding) );
    			String ss;
    			while ( (ss=rd.readLine())!=null ) {
    				bufResult.append( ss );
    			} 
    			rd.close( );
    			return bufResult.toString();
            } else {
            }
        } catch (Exception e) {
        }
        return null;
	}
	
	public static final String queryContentGET( String strURL, String cookieList, String acceptEncoding, String referer ) {		
		if ( !strURL.toLowerCase().startsWith("http://") && !strURL.toLowerCase().startsWith( "https://" ) ) {  
            strURL = "http://" + strURL;  
        }  
		try{
			URL url = new URL( strURL );
			HttpURLConnection httpUrlConn = (HttpURLConnection)url.openConnection( );
			HttpURLConnection.setFollowRedirects( false );
			if ( cookieList != null ) {
				httpUrlConn.setRequestProperty( "Cookie", cookieList );
			}	
			if ( referer != null ) {
				httpUrlConn.setRequestProperty( "Referer", referer ); 
			}
			httpUrlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
			httpUrlConn.setRequestMethod( "GET" );
			httpUrlConn.setDoInput( true );
			
			StringBuilder bufResult = new StringBuilder( );
			BufferedReader rd = new BufferedReader( new InputStreamReader(httpUrlConn.getInputStream(),acceptEncoding) );
			String ss;
			while ( (ss=rd.readLine())!=null ) {
				bufResult.append( ss );
			} 
			rd.close( );
			return bufResult.toString( );
		} catch( IOException ioe ) {
			return ioe.getMessage( );
		} 
	}
	
	/**
	 * 使用HtmlParser爬取页面内容
	 * @param url				: 爬取页面的URL地址
	 * @param acceptEncoding	: 使用的字符集
	 * @return
	 */
	public static final String queryHtmlContent( String url, String acceptEncoding ) {
		try {
			Parser parser = new Parser( url );
			parser.setEncoding( acceptEncoding );
			NodeList rt = parser.parse( null );
			return rt.toHtml( ).replaceAll("[\\t\\n\\r\\f]", "");		//替换掉无用字符
		} catch (ParserException pe) {
			if(DEBUG) System.out.println("HttpParser.queryHtmlContent>>" + pe.getMessage() );
			return "";
		}				
	}
	
	public static final String queryHttpsContent( String strURL, String queryString, String cookie, String acceptEncoding ) {		
		if ( !strURL.toLowerCase().startsWith( "https://" ) ) {  
            strURL = "https://" + strURL;  
        }  
		try{
			URL url = new URL( strURL );
			HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
			if ( cookie != null ) {
				conn.setRequestProperty( "Cookie", cookie );
			}
			
	        InputStreamReader insr = new InputStreamReader(conn.getInputStream(), acceptEncoding);	// 取得该连接的输入流，以读取响应内容
	        StringBuilder bufResult = new StringBuilder( );
	        int respInt = insr.read( );
	        while( respInt != -1 ) {
	            bufResult.append( (char)respInt );
	            respInt = insr.read( );
	        }
			return bufResult.toString( );
		} catch( IOException ioe ) {
			return ioe.getMessage( );
		} 
	}
	
	public static final String queryLocation( String strURL, String postData, String cookie, String acceptEncoding ) {		
		if ( !strURL.toLowerCase().startsWith( "http://" ) ) {  
            strURL = "http://" + strURL;  
        }  
		try{
			URL url = new URL( strURL );
			HttpURLConnection.setFollowRedirects( false );
			URLConnection conn = url.openConnection( );
			if ( cookie != null ) {
				conn.setRequestProperty( "Cookie", cookie );
			}
			conn.setDoOutput( true );
			OutputStreamWriter wr = new OutputStreamWriter( conn.getOutputStream() );
			wr.write( postData );
			wr.flush( );
			return conn.getHeaderField("Location");
			
		} catch( IOException ioe ) {
			return ioe.getMessage( );
		} 
	}

	public static final String queryCookie( String strURL, String postData ) {		//get cookie
		return queryCookie( strURL, postData, null, null, null );
	}
	
	public static final String queryCookie( String strURL, String postData, String cookieList ) {	//get cookie
		return queryCookie( strURL, postData, cookieList, null, null ); 
	}
	
	public static final String queryCookie( String strURL, String postData, String cookieList, String origin, String referer ) {	//get cookie
		try{
			URL url = new URL( strURL );
			HttpURLConnection.setFollowRedirects( false );
			HttpURLConnection conn = (HttpURLConnection)url.openConnection( );
			if ( cookieList != null ) {
				conn.setRequestProperty( "Cookie", cookieList );
			}
			conn.setRequestMethod( "POST" ); 
			conn.setDoOutput( true );
			if ( origin != null ) {
				conn.setRequestProperty( "Origin", origin ); 
			}
			if ( referer != null ) {
				conn.setRequestProperty( "Referer", referer ); 
			}
			OutputStreamWriter wr = new OutputStreamWriter( conn.getOutputStream() );
			wr.write( postData );
			wr.flush( );
			
			StringBuilder bufCookie = new StringBuilder( "" );
			List<String> cookies = conn.getHeaderFields().get( "Set-Cookie" );
			if ( cookies != null ) {
				for ( String cookie : cookies ) {
					bufCookie.append( cookie.substring(0, cookie.indexOf(";")+1 ) );
				}
			}
			wr.close( );
			return bufCookie.toString( );
		} catch( IOException ioe ) {
			System.out.println( "HttpParser.queryCookie>>" + ioe.getMessage() );
			return RTN_SPACE; 
		} 
	}
	
	public static final String queryHttpsCookie( String strURL, String postData, String cookieList ) {	//get cookie
		try{
			URL url = new URL( strURL );
			HttpsURLConnection.setFollowRedirects( false );
			HttpsURLConnection conn = (HttpsURLConnection)url.openConnection( );
			if ( cookieList != null ) {
				conn.setRequestProperty( "Cookie", cookieList );
			}
			conn.setDoOutput( true );
			OutputStreamWriter wr = new OutputStreamWriter( conn.getOutputStream() );
			wr.write( postData );
			wr.flush( );
			wr.close( );
			
			StringBuilder bufCookie = new StringBuilder( "" );
			List<String> cookies = conn.getHeaderFields().get( "Set-Cookie" );
			if ( cookies != null ) {
				for ( String cookie : cookies ) {
					bufCookie.append( cookie.substring(0, cookie.indexOf(";")+1 ) );
				}
			}
			return bufCookie.toString( );
		} catch( IOException ioe ) {
			return ioe.getMessage( ); 
		} 
	}
	
	public static final String readNormalParam( String paramName, String paramValue, String boundary ) {
		StringBuilder buf = new StringBuilder( );
		buf.append( "--" ).append( boundary ).append( CRLF );
	    buf.append( "Content-Disposition: form-data; name=\"" ).append( paramName ).append("\"").append( CRLF );                    
	    buf.append( CRLF );                                  
	    buf.append( paramValue ).append( CRLF );
	    
	    System.out.println( buf.toString() );
		return buf.toString( );
	}
	
	public static final String readTextFile( String paramName, File textFile, String charSet, String boundary ) {
		StringBuilder buf = new StringBuilder( );
		buf.append( "--" ).append( boundary ).append( CRLF );
	    buf.append( "Content-Disposition: form-data; name=\"" ).append( paramName ).append( "\"; filename=\"" ).append( textFile.getName() ).append( "\"" ).append( CRLF );                                                                
	    buf.append( "Content-Type: text/plain; charset=" ).append( charSet ).append( CRLF );                                                             
	    buf.append( CRLF );
	                                                                    
	    BufferedReader reader = null;	                                                                    
	    try {                                   
	        reader = new BufferedReader( new InputStreamReader(new FileInputStream(textFile), charSet) );
	        for ( String line; (line = reader.readLine()) != null; ) {     
	            buf.append( line ).append( CRLF );                        
	        }                      
	    } catch( IOException ioe ) {
	    } finally {                                                            
	        if ( reader != null ) try { reader.close(); } catch ( IOException logOrIgnore ) {	}
	    }
		return buf.toString( );
	}
	
	
	public static void main( String[] args ) {
//		String result = HttpParser.getHtmlContent("http://my.anjuke.com/user/broker/ppc/sale/step1", "UTF-8", null);
//		System.out.println( result );
		
//		HttpParser.buildHtml("http://agent.anjuke.com/login", "D:\\aa.thml", true);
		
		System.out.println( unescape("http://sh.81812727.com/User/Houses.aspx?type=3&query=%7B%22City%22%3A%222%22%2C%22Beds%22%3A%221%22%2C%22MinArea%22%3A%220%22%2C%22MaxArea%22%3A%2230%22%2C%22PageIndex%22%3A1%2C%22PageSize%22%3A%2220%22%7D&_=1406257539652") );
				  
	}

}
