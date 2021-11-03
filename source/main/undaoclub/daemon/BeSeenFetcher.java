
package undaoclub.daemon;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.undao.control.AbstractDaemon;
import com.undao.database.*;
import com.undao.utils.DateUtils;
import com.undao.utils.DecimalUtils;

import xms.XmsInitial;

/**
 * @author HaiZhou
 * 我能看到的用户列表
 */
public class BeSeenFetcher extends AbstractDaemon {
			
	private static final long serialVersionUID = 1L;
	private static final String FETCH_SQL = "SELECT lover_r,we_id,we_nick,avatar_url,dt_start, dt_end,RIGHT(acode_file_name,3) AS check_code FROM view_be_seen_list WHERE user_i_w=? AND we_id_w=? AND user_i_v<>0 AND dt_end>DATE_ADD(NOW(),INTERVAL -3 DAY)";

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) {
		String weID = request.getParameter( "OpenID" );
    	String userID = request.getParameter( "UserID" );
    	CommonSet dataSet = DBUtils.prepareQuery( XmsInitial.getDataSource(), FETCH_SQL, userID, weID );
    	System.out.println( userID + "===" + weID );
    	
    	StringBuilder buf = new StringBuilder( );
    	buf.append( "[" );
    	for ( int j = 0; j < dataSet.getRowCount(); j ++ ) {
    		buf.append( "{" );
    		buf.append( makeJsonItem("LoverR", DecimalUtils.formatLong(dataSet.getValue(j,"lover_r")) ) ).append( ", " );
    		buf.append( makeJsonItem("CheckCode", dataSet.getValue(j,"check_code") ) ).append( ", " );
    		buf.append( makeJsonItem("WeNick", dataSet.getValue(j,"we_nick") ) ).append( ", " );
    		buf.append( makeJsonItem("AvatarURL", dataSet.getValue(j,"avatar_url") ) ).append( ", " );
    		buf.append( makeJsonItem("DateStart", DateUtils.formatDateTime2(dataSet.getValue(j,"dt_start"))) ).append( "," );
    		buf.append( makeJsonItem("DateEnd", DateUtils.formatDateTime2(dataSet.getValue(j,"dt_end"))) ).append( " " );
    		buf.append( "}," );
    	}
    	buf.deleteCharAt( buf.length() -1 );
    	buf.append( "]" );
    	    	
    	outputToClient(request, response, buf.toString() );

    }       
       
}