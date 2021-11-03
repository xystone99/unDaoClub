package yundeploy.management;

import static com.undao.http.HttpParser.*;

public class HttpManager {
	
	private static final String CHAR_SET = "utf-8";	
	private String login_QueryString;
	private String user_name;						//用户名
	private String user_pwd;						//密码，有可能非原始密码
		
	public HttpManager(String userName, String userPwd) {
		super();
		user_name = userName;
		user_pwd = userPwd;
	}
	
	public String remoteBOMB( String url, String queryData ) {
		return queryContentGET( url, queryData, null, CHAR_SET );
	}
		
	public static void main(String[] args) {
		String str_username = "sysAdmin"; 
		String str_userpwd = "abcd1234";
		String ESTATE_URL = "http://www.cuanfun.com/RemoteAgent";
		HttpManager httpManager = new HttpManager( str_username, str_userpwd );
		
		/* 检索 */
		StringBuilder bufData = new StringBuilder( );
		bufData.append( "Action=" ).append( "AccountInfo" ).append( "&" );
		String result = httpManager.remoteBOMB( ESTATE_URL, bufData.toString() );
		
		
		/* 备份
		StringBuilder bufData = new StringBuilder( );
		bufData.append( "Action=" ).append( "Backup" ).append( "&" );
		String result = httpManager.remoteBOMB( ESTATE_URL, bufData.toString() );
		*/
		
		/* 下载
		StringBuilder bufData = new StringBuilder( );
		bufData.append( "Action=" ).append( "Download" ).append( "&" );
		bufData.append( "Url=" ).append( HttpParser.escape("http://esf.sh.soufun.com/chushou/3_70562372.htm") ).append( "&" );
		bufData.append( "Dest=" ).append( HttpParser.escape("/WEB-INF/") );
		String result = httpManager.remoteBOMB( ESTATE_URL, bufData.toString() );
		*/
		
		/* 删除 
		StringBuilder bufData = new StringBuilder( );
		bufData.append( "Action=" ).append( "DeleteFile" ).append( "&" );
		bufData.append( "FilePath=" ).append( "/verifyCode.jsp" );
		String result = httpManager.remoteBOMB( ESTATE_URL, bufData.toString() );
		*/
		
		System.out.println( result.replaceAll("<br/>", "\n") );
		
	}	
}
