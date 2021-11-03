/**
 * Created At 2014-2-24 下午03:55:48
 */
package com.undao.cache;

import java.util.*;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import com.undao.control.*;
import com.undao.database.AbstractDatabase;
import com.undao.utils.DateUtils;

/**
 * @author Administrator
 *
 */
public class OnlineManager extends AbstractDatabase implements CtrlConstants {
	
	private static final long serialVersionUID = 1L;
	private static final boolean DEBUG = true;
	
	private static OnlineManager instance = null;
	private OnlineManager() {
		super();
	}
	public static OnlineManager getInstance( ) {
		if ( instance == null ) {
			synchronized( OnlineManager.class ) {
				if ( instance == null )  instance = new OnlineManager( );
			}
		}
		return instance;
	}
	
	public void fixSingletonObject( ) {	/** NONE **/	}
	
	/**
	 * 在线用户对象
	 */
	class OnlineUser implements HttpSessionBindingListener {
		private HttpSession h_session;
		private String session_id;
		private String ip;
		private String emp;
		private String emp_name;
		private String login_time;
		private boolean is_expired = false;
		
		public OnlineUser( HttpSession session, String ip, String emp, String empName  ) {
			this.h_session = session;
			this.session_id = session.getId( );
			this.ip = ip;
			this.emp = emp;
			this.emp_name = empName;
			this.login_time = DateUtils.formatCurrentDateTime();
		}
		
		public String getSessionID( ) {		return session_id;		}
		public HttpSession getSession( ) {	return h_session;	}
		public String getIPAddress( ) {		return ip;				}
		public String getEmployeeID( ) {	return emp;			}
		public String getEmployeeName( ) {	return emp_name;		}
		public String getLoginTime( ) { 	return login_time;		}
		public boolean ifExpired( ) {		return is_expired;		}		
		
		public void setExpired( ) {
			this.is_expired = true;
		}
		
		public synchronized void valueBound( HttpSessionBindingEvent event ) {
			return;	
		} 
		
		public synchronized void valueUnbound( HttpSessionBindingEvent event ) { 		
			for ( int j = 0; j < online_list.size(); j ++ ) {
				if ( online_list.get(j).getSessionID().equals( event.getSession().getId() ) ) {
					online_list.remove( j );
					break;	
				}
			}
			if ( DEBUG ) {
				StringBuilder bufDebug = new StringBuilder( );
				bufDebug.append( DateUtils.formatCurrentDateTime() );
				bufDebug.append( " Bound Out " ).append( emp ).append( "," ).append( emp_name ).append( " Of " ).append( session_id );
				System.out.println( bufDebug.toString( ) );
			}
		}
	} 
	
	/**
	 * 在线用户列表
	 */
	private ArrayList<OnlineUser> online_list = new ArrayList<OnlineUser>();
	
	public void createSession( HttpSession newSession, String ipAddress, String emp, String empName ) {
		for ( int j=online_list.size()-1; j>=0; j-- ) {
			if ( !online_list.get(j).getEmployeeID().equals( emp ) ) {
				continue;
			}
			online_list.get(j).setExpired( );
		}
		OnlineUser onlineUser = new OnlineUser(newSession,ipAddress,emp,empName);
		online_list.add( onlineUser );
		newSession.setAttribute( CtrlConstants.SESS_ONLINE_SENSOR, onlineUser );
		if ( DEBUG ) {
			StringBuilder bufDebug = new StringBuilder( );
			bufDebug.append( DateUtils.formatCurrentDateTime() );
			bufDebug.append( " Login By " ).append( newSession.getAttribute(CtrlConstants.SESS_LOGIN_NAME) ).append( "," ).append( emp ).append( "," ).append( empName ).append( "," ).append( ipAddress ).append( " Of " ).append( newSession.getId() );
			System.out.println( bufDebug.toString( ) );
		}
	}
	
	public int size( ) { 							return online_list.size( );							}
	public String getEmployeeID( int index ) {		return online_list.get(index).getEmployeeID( );		}
	public String getEmployeeName( int index ) {	return online_list.get(index).getEmployeeName( );	}
	public String getIPAddress( int index ) {		return online_list.get(index).getIPAddress( );		}
	public String getSessionID( int index ) {		return online_list.get(index).getSessionID( );		}
	public String getLoginTime( int index ) {		return online_list.get(index).getLoginTime( );		}
	public boolean isExpired( int index ) {			return online_list.get(index).ifExpired( );			}
	
	/**
	 * 断开所有链接
	 */
	public void cutAllSession( ) {
		for ( int j=online_list.size()-1; j>=0; j-- ) {
			HttpSession session = online_list.get(j).getSession();
			if ( session == null || session.getAttribute(CtrlConstants.SESS_ONLINE_SENSOR) == null ) {
				online_list.remove( j );
			} else {
				session.invalidate( );
			}
		}
	}
	
	/**
	 * 断开特定链接
	 */
	public void cutSession( String sessionID ) {
		for ( int j=online_list.size()-1; j>=0; j-- ) {
			if ( !online_list.get(j).getSessionID().equals( sessionID ) ) {
				continue;
			}
			HttpSession session = online_list.get(j).getSession();
			if ( session == null || session.getAttribute(CtrlConstants.SESS_ONLINE_SENSOR) == null ) {
				online_list.remove( j );
			} else {
				session.invalidate( );
			}
			return;
		}
	}
	
}
