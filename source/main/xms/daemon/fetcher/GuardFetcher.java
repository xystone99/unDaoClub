
package xms.daemon.fetcher;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.undao.control.AbstractDaemon;
import com.undao.database.*;
import com.undao.utils.DecimalUtils;

import xms.XmsInitial;

/**
 * @author X.Stone
 *
 */
@WebServlet(asyncSupported = false, value = "/fetchguard", loadOnStartup = 5 )
public class GuardFetcher extends AbstractDaemon {
			
	private static final long serialVersionUID = 1L;

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) {
		if ( checkSessionExpired( request ) ) {
			return;
		}
		String truckID = request.getParameter( "TruckID" );
    	String matchTag = request.getParameter( "term" );
    	
    	StringBuilder bufSQL = new StringBuilder( );
    	bufSQL.append( "SELECT id, item_zh FROM view_guard_list WHERE sys_flg='Normal' " );
    	bufSQL.append( "AND truck='" ).append( truckID ).append( "' " );
    	bufSQL.append( "AND item_zh LIKE '%" ).append( matchTag.trim() ).append( "%' " );
    	bufSQL.append( "ORDER BY item_zh ASC LIMIT 12" );
    	CommonSet dataSet = DBUtils.executeQuery( XmsInitial.getDataSource(), bufSQL.toString() );
    	    	
    	StringBuilder buf = new StringBuilder( );
    	buf.append( "[" );
    	for ( int j = 0; j < dataSet.getRowCount(); j ++ ) {
    		buf.append( "{" );
    		buf.append( makeJsonItem("GuardID", DecimalUtils.formatLong(dataSet.getValue(j,"id")) ) ).append( "," );
    		buf.append( makeJsonItem("value", dataSet.getValue(j,"item_zh") ) ).append( "," );
    		buf.append( makeJsonItem("label", dataSet.getValue(j,"item_zh") ) );
    		buf.append( "}," );
    	}
    	buf.deleteCharAt( buf.length() -1 );
    	buf.append( "]" );
    	    	
    	outputToClient(request, response, buf.toString() );

    }       
       
}