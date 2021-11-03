
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
public class CanViewFetcher extends AbstractDaemon {
			
	private static final long serialVersionUID = 1L;
	private static final String FETCH_SQL = "SELECT lover_r,user_i_w,we_id, we_nick,avatar_url,split_tag,cur_longitude,cur_latitude,effect_time,dt_start,dt_end, RIGHT(acode_file_name,3) AS check_code FROM view_can_view_list WHERE user_i_v=? AND we_id_v=? AND dt_end>DATE_ADD(NOW(),INTERVAL -3 DAY)";

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) {

		String weID = request.getParameter( "ID1" );
    	String userID = request.getParameter( "ID2" );
    	CommonSet dataSet = DBUtils.prepareQuery( XmsInitial.getDataSource(), FETCH_SQL, userID, weID );
    	    	
    	StringBuilder buf = new StringBuilder( );
    	buf.append( "[" );
    	for ( int j = 0; j < dataSet.getRowCount(); j ++ ) {
    		buf.append( "{" );
    		buf.append( makeJsonItem("LoverR", DecimalUtils.formatLong(dataSet.getValue(j,"lover_r")) ) ).append( ", " );
    		buf.append( makeJsonItem("CheckCode", dataSet.getValue(j,"check_code") ) ).append( ", " );
    		buf.append( makeJsonItem("UserID", dataSet.getValue(j,"user_i_w") ) ).append( ", " );
    		buf.append( makeJsonItem("WeNick", dataSet.getValue(j,"we_nick") ) ).append( ", " );
    		buf.append( makeJsonItem("AvatarURL", dataSet.getValue(j,"avatar_url") ) ).append( ", " );
    		buf.append( makeJsonItem("SplitTag", dataSet.getValue(j,"split_tag") ) ).append( ", " );
    		buf.append( makeJsonItem("CurLongitude", dataSet.getValue(j,"cur_longitude") ) ).append( ", " );
    		buf.append( makeJsonItem("CurLatitude", dataSet.getValue(j,"cur_latitude") ) ).append( ", " );
    		buf.append( makeJsonItem("EffectTime", DateUtils.formatDateTime2(dataSet.getValue(j,"effect_time"))) ).append( ", " );
    		buf.append( makeJsonItem("DateStart", DateUtils.formatDateTime2(dataSet.getValue(j,"dt_start"))) ).append( "," );
    		buf.append( makeJsonItem("DateEnd", DateUtils.formatDateTime2(dataSet.getValue(j,"dt_end"))) ).append( " " );
    		buf.append( "}," );
    	}
    	buf.deleteCharAt( buf.length() -1 );
    	buf.append( "]" );
    	
    	outputToClient(request, response, buf.toString() );

    }       
       
}