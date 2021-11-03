
package undaoclub.daemon;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xms.XmsInitial;

import com.alibaba.fastjson.JSONObject;
import com.undao.cache.WeChatAccessToken;
import com.undao.control.AbstractDaemon;
import com.undao.database.DBUtils;
import com.undao.database.ParamLayer;
import com.undao.http.WeChatUtils;

/**
 * @author X.Stone
 *
 */
public class WeChatGround extends AbstractDaemon {
			
	private static final long serialVersionUID = 1L;
	private final static String SQL_NEW_USER = "{CALL proc_user_new(?,?,?,  ?,?,?,?  ) }";

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) {
		String action = request.getParameter( "Action" );
		StringBuilder buf = new StringBuilder( );
		
		if ( action.equalsIgnoreCase( "AccessToken" ) ) {
			String accessToken = WeChatAccessToken.getInstance().getAccessToken();
			buf.append( "{" );
			buf.append( "\"AccessToken\":\"" ).append( accessToken ).append( "\"," );
			buf.append( "\"Other\":\"" ).append( System.currentTimeMillis() ).append( "\"" );
			buf.append( "}" );
			
		} else if ( action.equalsIgnoreCase( "OpenID" ) ) {			//获取OpenID和UserID
			String jsCode = request.getParameter( "JSCode" );
			String nickName = request.getParameter( "NickName" );
			String avatarURL = request.getParameter( "AvatarURL" );
			JSONObject openIDAbout = WeChatUtils.getOpenIDAbout( jsCode );
			buf.append( "{" );
			if ( openIDAbout == null || !openIDAbout.containsKey( "openid" ) ) {
				buf.append( "\"OpenID\":\"" ).append( openIDAbout.toJSONString() ).append( "\"," );
				buf.append( "\"UserID\":\"0\"," );
				buf.append( "\"ExpireTime\":\"2001-01-01 01:01:01\"," );
				buf.append( "\"SplitTag\":\"00\"," );
				buf.append( "\"SessionKey\":\"" ).append( System.currentTimeMillis() ).append( "\"" );
			} else {
				String openID = openIDAbout.getString( "openid" );
				ParamLayer paramLayer = new ParamLayer();
				paramLayer.addParam(ParamLayer.PARAM_STRING, openID );
				paramLayer.addParam(ParamLayer.PARAM_STRING, nickName );
				paramLayer.addParam(ParamLayer.PARAM_STRING, avatarURL );
				String[] arrResult = DBUtils.executeCall( XmsInitial.getDataSource(), SQL_NEW_USER, paramLayer, 4);
				buf.append( "\"OpenID\":\"" ).append( openID ).append( "\"," );
				buf.append( "\"UserID\":\"" ).append( arrResult[0] ).append( "\"," );
				buf.append( "\"ExpireTime\":\"" ).append( arrResult[1] ).append( "\"," );
				buf.append( "\"SplitTag\":\"" ).append( arrResult[2] ).append( "\"," );
				buf.append( "\"SessionKey\":\"" ).append( openIDAbout.getString( "session_key" ) ).append( "\"" );
			}
			buf.append( "}" );
			
		} else if ( action.equalsIgnoreCase( "ACode" ) ) {
			String accessToken = WeChatAccessToken.getInstance().getAccessToken();
			StringBuilder bufFile = new StringBuilder( );
			bufFile.append( XmsInitial.getHomeDir() ).append( "Photos" ).append( File.separator ).append( System.currentTimeMillis() ).append( ".jpg" );
			File aCodeFile = new File( bufFile.toString() );
			
			WeChatUtils.createWxacodeUnlimit( accessToken, "123456789012345678901234567890", "pages/issuedRequest/issuedRequest", aCodeFile );
			buf.append( "{" );
			buf.append( "\"aCodeFile\":\"" ).append( aCodeFile.getName() ).append( "\"," );
			buf.append( "\"Other\":\"" ).append( System.currentTimeMillis() ).append( "\"" );
			buf.append( "}" );
			
		} else if ( action.equalsIgnoreCase( "AQRCode" ) ) {
			String fileName = request.getParameter( "FName" );
			String accessToken = WeChatAccessToken.getInstance().getAccessToken();
			StringBuilder bufFile = new StringBuilder( );
			bufFile.append( XmsInitial.getHomeDir() ).append( "Photos" ).append( File.separator ).append( fileName ).append( ".jpg" );
			File aCodeFile = new File( bufFile.toString() );
			
			WeChatUtils.createWxaqrCode(accessToken, "pages/aboutKite/aboutKite", 430, aCodeFile);
			buf.append( "{" );
			buf.append( "\"aqrCodeFile\":\"" ).append( aCodeFile.getName() ).append( "\"," );
			buf.append( "\"Other\":\"" ).append( System.currentTimeMillis() ).append( "\"" );
			buf.append( "}" );
		} 

		outputToClient(request, response, buf.toString() );

    } 
  
}