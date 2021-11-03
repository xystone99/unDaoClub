package com.undao.http;

public abstract class AbstractJobSite {
	
	public abstract String login( );	//此方法通过getLoginCookies()来调用，不能直接调用
	public abstract boolean is_can_login( );
	public abstract void refreshSession( );
	public abstract void refreshBulletin( );
	
	protected boolean DEBUG = false;				//DEBUG开关
	private long session_length = 30*60*1000;		//当前网站Session时长
	private String user_name;						//用户名
	private String user_pwd;						//密码，有可能非原始密码
	private String login_QueryString;				//缓存登录的链接串，以便再次登录
	private long lastCheckInTime = 0;				//最近一次操作当前对象的时间
	private String login_cookies = null; 			//缓存连接的Cookie，避免高频率的重复获取该值
	
	public AbstractJobSite(String userName, String userPwd) {
		super();
		user_name = userName;
		user_pwd = userPwd;
		lastCheckInTime = System.currentTimeMillis();
	}
	
	public void setDEBUG( boolean ifDebug ) {
		this.DEBUG = ifDebug;
	}
	
	public String getUserName( ) {
		return user_name;
	}
	public String getUserPwd( ) {
		return user_pwd;
	}
	
	public String getLoginQueryString() {
		return login_QueryString;
	}
	public void setLoginQueryString(String loginQueryString) {
		login_QueryString = loginQueryString;
	}
	
	public long getLastCheckInTime() {
		return lastCheckInTime;
	}
	public void setLastCheckInTime(long lastCheckInTime) {
		this.lastCheckInTime = lastCheckInTime;
	}
	
	public void setSessionLength( long sessionLength ) {
		this.session_length = sessionLength;
	}
	public boolean isSessionExpired( ) {
		return System.currentTimeMillis() - lastCheckInTime > session_length;
	}

	public String getLoginCookies() {
		if ( login_cookies == null ) {
			login_cookies = login( );
		}
		return login_cookies;
	}
	public void setLoginCookies(String loginCookies) {
		login_cookies = loginCookies;
	}
	
}
