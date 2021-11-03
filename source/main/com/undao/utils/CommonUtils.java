package com.undao.utils;

import java.net.InetAddress;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;
import java.sql.Blob;
import java.sql.SQLException;

public class CommonUtils {	    	        
	
	/**
	 * 隐藏姓名，只显示姓，名字以*代替 
	 */
	public static String hideName( String name ) {
		if ( name == null ) return "";
		StringBuilder bufName = new StringBuilder();
		bufName.append( name.substring(0, 1) ).append( name.substring( 1 ).replaceAll(".", "*") );
		return bufName.toString( );
	}

	/** 
	 * JSON字符串特殊字符处理，比如：“\A1;1300” 
	 * @param s 
	 * @return String 
	 */  
	public static String formatJSON(String s) {        
		StringBuilder buf = new StringBuilder();
		for (int i=0; i<s.length(); i++) {
			char c = s.charAt(i);    
			switch (c){  
	             case '\"': buf.append("\\\"");	break;
	             case '\\': buf.append("\\\\");	break;
	             case '/': 	buf.append("\\/");	break;
	             case '\b': buf.append("\\b");	break;
	             case '\f': buf.append("\\f");	break;
	             case '\n': buf.append("\\n");	break;
	             case '\r': buf.append("\\r");	break;
	             case '\t': buf.append("\\t");	break;
	             default: 	buf.append(c);
			}
		}      
		return buf.toString();
	}  
	
	/**
	 * 格式化Request中的变量值，主要处理NULL值
	 */	
	public static final String patchString( String val ) {
		return ( val == null ) || val.equals( "null" ) ? "" : val;
	}
	
	public static final int patchIntegerKey( String val ) {
		if ( val == null || val.equals( "null" ) || val.length( )==0 ) {
			return 0;	
		}
		return Integer.parseInt( val );
	}
	
	public static final String patchStringKey( String val ) {		
		if ( val == null || val.equals( "null" )) {
			return null;	
		}
		return val;
	}
		
	public static final BigDecimal patchDecimal( String bVal ) {		
		return patchDecimal( bVal, false );
	}	
	
	public static final BigDecimal patchDecimal(String bVal, boolean fitZero ) {
		if ( bVal == null || bVal.equals( "null" ) || bVal.length( ) == 0 ) {			
			return fitZero ? new BigDecimal( 0 ) : null;
		}
		return new BigDecimal( bVal );
	}
	
	/**
	 * TextToHtml格式化输出
	 */
	public static final String formatTextToHtml( Object text ) {
		return ((String)text).replaceAll("<br />", "&#13;&#10;");
	} 
	
	/**
     * Convert Between Unicode and Local Charset
     */
    public static final String getUnicodeFromString( String unicode ) {
        return new StringBuffer( unicode ).toString( );
    }
    
    public static final String geStringFromUnicode( String change ) {
        String swap;
        StringBuffer buffer = new StringBuffer( );
        char[ ] array = change.toCharArray( );
        for ( int index = 0; index < change.length( ); index++ ) {
            short ch = ( short ) array[ index ];
            swap = Integer.toHexString( ch ).toUpperCase( );
            buffer.append( "\\u" );
            buffer.append( swap );
        }
        return buffer.toString( );
    }

	/**
	 * 获取文件的扩展名
	 */
	public final static String getExtension( String fileName ) {		//获取扩展名
        String extension;
        int index = fileName.lastIndexOf( "." );
        if ( index > 0 && index < fileName.length( ) ) {
            extension = fileName.substring( index + 1).toLowerCase( );
        } else {
            extension = null;
        }
        return ( extension == null ) ? "" : extension.trim( );
    }

	/**
	 * 生成随机验证码
	 */
	public final static String buildValidCode( int length ) {
		Random random = new Random( );
		StringBuilder buffer = new StringBuilder( );
		for( int index = 0; index < length; index++ ) {
			buffer.append( String.valueOf(random.nextInt(10)) );			
		}
		return buffer.toString( );
	}

	/**
	 * 生成随机密码
	 */
    public final static String createPassword( int seed ) {
		 String t = Long.toString( (long)seed * (long)31415926 );
		 int pos = t.length() / 2;
		 return t.substring( pos-2, pos+4 ).replace("4", "8");
	}
    
    public final static String createPassword( ) {						//生成随机密码
		 return createPassword( (int)System.currentTimeMillis() );
	}

	/**
	 * 生成smallInt和bigInt之间的一个随机数
	 */
    public final static int randomInt( int smallInt, int bigInt ) {
    	return (int)(System.currentTimeMillis() % (bigInt-smallInt) ) + smallInt;
    }
    
	/**
	 * 重新排列base字符串, length表示生成字符串的长度
	 */
    public static String getRandomString(String base, int length) {
        Random random = new Random();   
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < length; i++) {   
            int number = random.nextInt(base.length());   
            buf.append(base.charAt(number));
        }   
        return buf.toString();
    }

	/**
	 * 按字典顺序重排字符序列
	 */
	public final static String sort( String args ) {
		 char ss[] = args.toCharArray();  
		 Arrays.sort( ss );
		 StringBuilder buf = new StringBuilder( );
		 for ( int i=0; i < ss.length; i++ ) {
			 buf.append( ss[i] );
		 }
		 return buf.toString();
	 }
    
    /**
     * JSP显示变量值 
     */
    public static final String displayDate( String date, boolean displayBorder ) {	//特殊日期显示为空
    	if ( !displayBorder ) {
    		if ( date.equals("2001-01-01") || date.equals("2049-12-31") ) {
    			return "";
    		}
    	}
    	return date.trim();
    }
    
    public static final String displayHtmlMemo( String memo ) {					//在HTML中显示TEXT类型
		return memo.replace( "\r\n", "<br>").replace( " ", "&nbsp;" );	
	}
	
    public static final String displayToXLS( String memo ) {					//在XLS中显示转换过的Text类型
		return memo.replace( "<br />", "\r\n").replace( "&nbsp;", " " );	
	} 
    
	public static final String cleanHtmlCode( String tmpStr ) {					//清除字符串中的HTML标记
    	return tmpStr.replaceAll( "</?[a-zA-Z]{1,10}>", " " );
	}
	
    public static final String formatString( Object val ) {						//显示字符串，不显示空值
    	return val == null ? "" : val.toString( );
    }
    
    public static final String formatString( Object val, String replace ) {		//显示字符串，用默认字符串代替空值
    	return val == null ? replace : val.toString( );
    }
	 
    public static String convertBlobToString( Blob blob ) {						//将Blob转换为字符串
		 StringBuilder bufResult = new StringBuilder( );
		 try {
			 ByteArrayInputStream msgContent = (ByteArrayInputStream)blob.getBinaryStream();
			 byte[] byte_data = new byte[msgContent.available()];
			 msgContent.read(byte_data, 0,byte_data.length);
			 bufResult.append( byte_data );
		 } catch( SQLException sqle ) {
			 System.out.println( "CommonUtils.buildRequestURL(pURL, requestPath)>> " + sqle.getMessage() );
		 }
		 return bufResult.toString( );
	 }
	
	 /**
	  * 网络相关 
	  */
	 public final static URL buildRequestURL( URL pURL, String requestPath ) {	//生成完整的URL地址
		StringBuffer buf = new StringBuffer( );
		buf.append( pURL.getProtocol( ) ).append( "://" ).append( pURL.getHost( ) ).append( "/" ).append( requestPath );
		try {
			return new URL( buf.toString( ) );
		} catch( MalformedURLException mue ) {	
			System.out.println( "CommonUtils.buildRequestURL(pURL, requestPath)>> " + mue.getMessage() );
		}
		return null;
	}
	 
	 public final static String getLocalName( ) {				//获取当前主机名
		 InetAddress a;
		 try {
			 a = InetAddress.getLocalHost();
			 return a.getHostName();
		 } catch (UnknownHostException e) {
			 System.out.println( "CommonUtils.getLocalName()>> " + e.getMessage() );
		 }
		 return null;
	 } 
	 
	 public final static String getLocalAddress( ) {			//获取当前主机IP地址
		 InetAddress a;
		 try {
			 a = InetAddress.getLocalHost();
			 return a.getHostAddress( );
		 } catch (UnknownHostException e) {
			 System.out.println( "CommonUtils.getLocalAddress()>> " + e.getMessage() );
		 }
		 return null;
	 }

	/**
	 * main test
	 * @param args
	 */
	public static void main( String[] args ) {
		 System.out.println( createPassword() );
	 }
	
}