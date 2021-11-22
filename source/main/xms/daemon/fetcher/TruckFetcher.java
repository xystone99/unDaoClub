
package xms.daemon.fetcher;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.undao.cache.*;
import com.undao.control.AbstractDaemon;

/**
 * @author X.Stone
 *
 */
@WebServlet(asyncSupported = false, value = "/fetchtruck", loadOnStartup = 5 )
public class TruckFetcher extends AbstractDaemon {
			
	private static final long serialVersionUID = 1L;

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) {
		if ( checkSessionExpired( request ) ) {
			return;
		}
		String matchTag = request.getParameter("term").trim();
    	TruckGeometry truckGeometry = TruckGeometry.getInstance();

		StringBuilder buf = new StringBuilder( );
		buf.append( "[" );


    	String action = request.getParameter( "Action" );
    	if ( action.equals("Normal") ) {
			buf.append( truckGeometry.searchTruck( matchTag.toUpperCase() ) );

			buf.append( ",{" );
			buf.append( AbstractDaemon.makeJsonItem("ID", "0" ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("PlateNumber", "" ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("value", "社会车辆" ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("label", "社会车辆..." ) );
			buf.append( "}" );

    	} else if ( action.equals( "TruckIdle" ) ) {
			buf.append( truckGeometry.searchTruck( matchTag.toUpperCase() ) );
		}

		buf.append( "]" );
		outputToClient(request, response, buf.toString() );
    	
    }       
       
}