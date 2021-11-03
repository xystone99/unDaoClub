
package undaoclub.daemon;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xms.XmsInitial;

import com.undao.cache.WeChatAccessToken;
import com.undao.control.AbstractDaemon;
import com.undao.database.*;
import com.undao.http.WeChatUtils;

/**
 * @author X.Stone
 *
 */
public class GeoRelation extends AbstractDaemon {
			
	private static final long serialVersionUID = 1L;
	private final static String SQL_WHO_SHARE = "SELECT we_nick,avatar_url FROM view_can_view_list WHERE lover_r=? AND RIGHT(acode_file_name,3)=?";
	private final static String SQL_SEND_RELATION = "{CALL proc_send_relation(?,?,?,?,  ?,?  ) }";
	private final static String SQL_ACCEPT_RELATION = "{CALL proc_accept_relation(?,?,?,?,?,  ?  ) }";
	private final static String SQL_UPDATE_WALKER_NICK = "{CALL proc_update_walker_nick(?,?,?,?,?,  ?  ) }";
	private final static String SQL_UPDATE_VIEWER_NICK = "{CALL proc_update_viewer_nick(?,?,?,?,?,  ?  ) }";
	private final static String SQL_UPDATE_VIEWER_EXPIRE = "{CALL proc_update_viewer_expire(?,?,?,?,?,  ?,?  ) }";
	
	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) {
		String action = request.getParameter( "Action" );
		StringBuilder buf = new StringBuilder( );
		
		if ( action.equalsIgnoreCase( "SendRelation" ) ) {
			String openID = request.getParameter( "OpenID" );
			String userID = request.getParameter( "UserID" );
			String nickName = request.getParameter( "NickName" );
			String acodeName = Long.toString( System.currentTimeMillis() );
			
			ParamLayer paramLayer = new ParamLayer();
			paramLayer.addParam(ParamLayer.PARAM_STRING, openID );
			paramLayer.addParam(ParamLayer.PARAM_STRING, userID );
			paramLayer.addParam(ParamLayer.PARAM_STRING, nickName );
			paramLayer.addParam(ParamLayer.PARAM_STRING, acodeName );
			String[] arrResult = DBUtils.executeCall( XmsInitial.getDataSource(), SQL_SEND_RELATION, paramLayer, 2);
			
			buf.append( "{" );
			if ( arrResult[0].equals( "0" ) ) {
				buf.append( "\"LoverR\":\"0\"," );
				buf.append( "\"aCodeName\":\"000\"" );
			} else if ( arrResult[1].equals("SendRelationSuccess") ) {
				StringBuilder bufFile = new StringBuilder( );
				bufFile.append( XmsInitial.getHomeDir() ).append( "Photos" ).append( File.separator ).append( acodeName ).append( ".jpg" );
				File aCodeFile = new File( bufFile.toString() );
				
				String accessToken = WeChatAccessToken.getInstance().getAccessToken();
				StringBuilder bufParams = new StringBuilder("12345678901234567890123456789-");
				bufParams.append( acodeName.substring(acodeName.length()-3) ).append("-").append( arrResult[0] );
				WeChatUtils.createWxacodeUnlimit( accessToken, bufParams.substring(bufParams.length()-30), "/pages/receivedRequest/receivedRequest", aCodeFile );
				
				buf.append( "\"LoverR\":\"" ).append( arrResult[0] ).append( "\"," );
				buf.append( "\"aCodeName\":\"" ).append( acodeName ).append( "\"" );
			} else {
				buf.append( "\"LoverR\":\"" ).append( arrResult[0] ).append( "\"," );
				buf.append( "\"aCodeName\":\"" ).append( arrResult[1] ).append( "\"" );
			}
			buf.append( "}" );
		
		} else if ( action.equalsIgnoreCase( "WhoShare" ) ) {		//谁向我共享轨迹
			String loverR = request.getParameter( "LoverR" );
			String checkCode = request.getParameter( "CheckCode" );
			CommonSet dataSet = DBUtils.prepareQuery( XmsInitial.getDataSource(), SQL_WHO_SHARE, loverR, checkCode );
			
			buf.append( "{" );
			if ( dataSet.getRowCount() < 1 ) {
				buf.append( "\"NickName\":\"" ).append( "Unknown" ).append( "\"," );
				buf.append( "\"AvatarURL\":\"" ).append( "https://undao.club/Photos/SpreadCode.jpg" ).append( "\"" );
			} else {
				buf.append( "\"NickName\":\"" ).append( dataSet.getValue("we_nick") ).append( "\"," );
				buf.append( "\"AvatarURL\":\"" ).append( dataSet.getValue("avatar_url") ).append( "\"" );
			}
			buf.append( "}" );
		
		} else if ( action.equalsIgnoreCase( "AcceptRelation" ) ) {		//接受关注好友的活动轨迹
			String openID = request.getParameter( "OpenID" );
			String userID = request.getParameter( "UserID" );
			String nickName = request.getParameter( "NickName" );
			String loverR = request.getParameter( "LoverR" );
			String checkCode = request.getParameter( "CheckCode" );
			
			ParamLayer paramLayer = new ParamLayer();
			paramLayer.addParam(ParamLayer.PARAM_STRING, openID );
			paramLayer.addParam(ParamLayer.PARAM_STRING, userID );
			paramLayer.addParam(ParamLayer.PARAM_STRING, nickName );
			paramLayer.addParam(ParamLayer.PARAM_STRING, loverR );
			paramLayer.addParam(ParamLayer.PARAM_STRING, checkCode );
			String result = DBUtils.executeCall( XmsInitial.getDataSource(), SQL_ACCEPT_RELATION, paramLayer );
			
			buf.append( "{" );
			buf.append( "\"Status\":\"" ).append( result ).append( "\"," );
			buf.append( "\"Other\":\"" ).append( System.currentTimeMillis() ).append( "\"" );
			buf.append( "}" );
			
		} else if ( action.equalsIgnoreCase( "UpdateWalkerNick" ) ) {		//更新行走者的昵称
			String openID = request.getParameter( "OpenID" );
			String userID = request.getParameter( "UserID" );
			String loverR = request.getParameter( "LoverR" );
			String checkCode = request.getParameter( "CheckCode" );
			String walkerNick = request.getParameter( "WalkerNick" );
			
			ParamLayer paramLayer = new ParamLayer();
			paramLayer.addParam(ParamLayer.PARAM_STRING, openID );
			paramLayer.addParam(ParamLayer.PARAM_STRING, userID );
			paramLayer.addParam(ParamLayer.PARAM_STRING, loverR );
			paramLayer.addParam(ParamLayer.PARAM_STRING, checkCode );
			paramLayer.addParam(ParamLayer.PARAM_STRING, walkerNick );
			String result = DBUtils.executeCall( XmsInitial.getDataSource(), SQL_UPDATE_WALKER_NICK, paramLayer );
			
			buf.append( "{" );
			buf.append( "\"Status\":\"" ).append( result ).append( "\"," );
			buf.append( "\"Other\":\"" ).append( System.currentTimeMillis() ).append( "\"" );
			buf.append( "}" );
			
		} else if ( action.equalsIgnoreCase( "UpdateViewerNick" ) ) {		//更新观察者的昵称
			String openID = request.getParameter( "OpenID" );
			String userID = request.getParameter( "UserID" );
			String loverR = request.getParameter( "LoverR" );
			String checkCode = request.getParameter( "CheckCode" );
			String viewerNick = request.getParameter( "ViewerNick" );
			
			ParamLayer paramLayer = new ParamLayer();
			paramLayer.addParam(ParamLayer.PARAM_STRING, openID );
			paramLayer.addParam(ParamLayer.PARAM_STRING, userID );
			paramLayer.addParam(ParamLayer.PARAM_STRING, loverR );
			paramLayer.addParam(ParamLayer.PARAM_STRING, checkCode );
			paramLayer.addParam(ParamLayer.PARAM_STRING, viewerNick );
			String result = DBUtils.executeCall( XmsInitial.getDataSource(), SQL_UPDATE_VIEWER_NICK, paramLayer );
			
			buf.append( "{" );
			buf.append( "\"Status\":\"" ).append( result ).append( "\"," );
			buf.append( "\"Other\":\"" ).append( System.currentTimeMillis() ).append( "\"" );
			buf.append( "}" );
		
		} else if ( action.equalsIgnoreCase( "UpdateViewerExpire" ) ) {		//更新向好友分享活动轨迹的截止时间
			String openID = request.getParameter( "OpenID" );
			String userID = request.getParameter( "UserID" );
			String loverR = request.getParameter( "LoverR" );
			String checkCode = request.getParameter( "CheckCode" );
			String viewerExpire = request.getParameter( "ViewerExpire" );
			
			ParamLayer paramLayer = new ParamLayer();
			paramLayer.addParam(ParamLayer.PARAM_STRING, openID );
			paramLayer.addParam(ParamLayer.PARAM_STRING, userID );
			paramLayer.addParam(ParamLayer.PARAM_STRING, loverR );
			paramLayer.addParam(ParamLayer.PARAM_STRING, checkCode );
			paramLayer.addParam(ParamLayer.PARAM_STRING, viewerExpire );
			String[] result = DBUtils.executeCall( XmsInitial.getDataSource(), SQL_UPDATE_VIEWER_EXPIRE, paramLayer, 2 );
			
			buf.append( "{" );
			buf.append( "\"Status\":\"" ).append( result[1] ).append( "\"," );
			buf.append( "\"MaxExpireTime\":\"" ).append( result[0] ).append( "\"" );
			buf.append( "}" );
		
		} 

		outputToClient(request, response, buf.toString() ); 

    } 
  
}