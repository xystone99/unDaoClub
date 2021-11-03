/**
 * 网络探测器 2014.10.27
 */
package com.undao.security;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.undao.cache.SystemicVariables;
import com.undao.mail.SmtpHandler;
import com.undao.utils.DateUtils;

/**
 * @author Administrator
 *
 */
public class NetDetector implements Runnable {
	
	public static boolean DEBUG = false;		
	
	private static final int SMALL_INTERVAL = 1 * 60 * 1000;
	private static final int LONG_INTERVAL = 12 * 60 * 1000;
	
	private static final String DEFAULT_IP = "10.10.0.1";
	
	/**
	 * 发送变更后的IP
	 */
	public void run( ) {	
        while( true ) {
        	String curIP = detectPublicIP( );
			SystemicVariables stcVariables = SystemicVariables.getInstance();
        	if ( !stcVariables.getCurrentPublicIP(stcVariables.getPrivateCloudID()).equals( curIP ) ) {
        		if ( !curIP.equals( DEFAULT_IP ) ) { 
        			if ( waveXMS( curIP ) ) {
						stcVariables.setCurrentPublicID(stcVariables.getPrivateCloudID(), curIP );
                		System.out.println( "IP Address Changed:" + curIP );
            		} else {
            			System.out.println( "NetDetector Send Failure:" + curIP + " (" + DateUtils.formatCurrentDateTime() + ")" );
            		}
        		}
        	}
        	        	
            try {
            	Thread.sleep( DEBUG ? SMALL_INTERVAL : LONG_INTERVAL );
            } catch( InterruptedException ie ) {
                if(DEBUG) System.out.println( "NetDetector>>Sleep failure with InterruptedException" );
            }
        }
	}
	
	/**
	 * 探测当前网络的公网IP
	 */
	public static String detectPublicIP( ) {
		BufferedReader br = null;
		try {
			URL url = new URL( "http://1212.ip138.com/ic.asp" );
			URLConnection conn = url.openConnection( );
			conn.setRequestProperty( "User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2.15) Gecko/20110303 Firefox/3.6.15");
			conn.setRequestProperty( "Content-Type", "text/html" );
			conn.setRequestProperty( "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8" );
			InputStream is = conn.getInputStream();
			br = new BufferedReader(new InputStreamReader(is, "GB2312"));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.contains("您的IP是")) {
					int start = line.indexOf('[') + 1;
					int end = line.indexOf(']');
					return line.substring(start, end);
				}
			}
		} catch (MalformedURLException e) { 
			System.out.println( e.getMessage() );
		} catch( Exception e ) {
			System.out.println( e.getMessage() );
		} finally {
			try { br.close();  } catch(Exception e) {		}
		}
		return DEFAULT_IP;
		  
	}
	
	private SmtpHandler smtpHandler = null;				//邮件发送处理器
	private String mailList = "294115550@QQ.com;";		//收件列表
	
	/**
	 * Contructor
	 * @param smtpHandler : 邮件发送处理器
	 */
	public NetDetector( SmtpHandler smtpHandler ) {
		this.smtpHandler = smtpHandler;
	}
	
	public void setMailList( String mailList ) {
		this.mailList = mailList;
	}
	
	/**
	 * 发送更新后的IP地址
	 * @param curIP : 更新后的IP地址
	 * @return
	 */
	public boolean waveXMS( String curIP ) {
		smtpHandler.setFROM( "xmsclient_1@126.com" );
        smtpHandler.setBCC( "294115550@QQ.com" );
		StringBuilder bufSubject = new StringBuilder( );
        bufSubject.append( "Sorry. IP地址已更新，请做相应修改." );
        smtpHandler.setSubject( bufSubject.toString() );
        
        StringBuilder bufContent = new StringBuilder();
        bufContent.append( "Hello：<br /><br />" );
        bufContent.append( "当前的IP地址：&nbsp;" ).append( curIP ).append( "<br />" );
        bufContent.append( "用户名：&nbsp;nyremote" ).append( "<br />" );
        bufContent.append( "密码：&nbsp;nyr.123456" ).append( "<br /><br />" );
        bufContent.append( "能运物流&nbsp;&nbsp;许世科" ).append( "<br />" );
        bufContent.append( "Best Regards!!!" ).append( "<br />" );
        boolean is_send_success = smtpHandler.sendMessage( mailList, bufContent.toString() );
        return is_send_success;
	}
	
	/**
	 * main test
	 */
	public static void main(String[] args) throws Exception {
		SmtpHandler smtpHandler = new SmtpHandler( "smtp.126.com", "xmsclient_1", "www.126.com" );
		NetDetector detector = new NetDetector( smtpHandler );
		NetDetector.DEBUG = true;
		detector.setMailList( "294115550@QQ.com" );
		System.out.println( detector.waveXMS( detector.detectPublicIP() ) );
	}

}
