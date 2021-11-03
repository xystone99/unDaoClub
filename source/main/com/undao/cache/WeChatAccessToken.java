/**
 * Created At 2014-2-24 下午02:47:40
 */
package com.undao.cache;

import com.undao.database.AbstractDatabase;
import com.undao.http.*;
import com.undao.utils.*;

/**
 * @author Administrator
 *
 */
public class WeChatAccessToken extends AbstractDatabase implements Runnable {

	private static final long serialVersionUID = -8219129191531966588L;
	
	private static WeChatAccessToken instance = null;
	private WeChatAccessToken( ) {
	}
	public static WeChatAccessToken getInstance( ) {
		if ( instance == null ) {
			synchronized( WeChatAccessToken.class ) {
				if ( instance == null ) instance = new WeChatAccessToken( );
			}
		}
		return instance;
	}
	
	private String accessToken = null;
	private int stepSecondsForRefresh = 3600;

	public void fixSingletonObject( ) {
		accessToken = WeChatUtils.getAccessToken( );
		stepSecondsForRefresh = WeChatUtils.getSecondsOfRefreshAccessToken( );
	}

	public String getAccessToken( ) {
		return accessToken;
	}
	
	@Override
	public void run() {
		System.out.println( DateUtils.formatCurrentDateTime() + " WeChatAccessToken START UP ......" );
		while( true ) {				
			fixSingletonObject( );
			try {
            	Thread.sleep( stepSecondsForRefresh * 1000 );
            } catch( InterruptedException ie ) {
            	StringBuilder bufLog = new StringBuilder( DateUtils.formatCurrentDateTime() );
            	bufLog.append( "WeChatAccessToken.run() Exception---" ).append( ie.getMessage() );
                System.out.println( bufLog.toString() );
            }
		}
		
	}
	
}
