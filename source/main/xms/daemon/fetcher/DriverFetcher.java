
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
@WebServlet(asyncSupported = false, value = "/fetchdriver", loadOnStartup = 5 )
public class DriverFetcher extends AbstractDaemon {
			
	private static final long serialVersionUID = 1L;

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) {
		if ( checkSessionExpired( request ) ) {
			return;
		}
		String actionTag = request.getParameter( "Action" );
		String matchTag = request.getParameter("term").trim();
    	DriverGeometry driverGeometry = DriverGeometry.getInstance();

    	if ( actionTag.equals( "Driver" ) ) {
			String json = driverGeometry.searchPattern( matchTag.toUpperCase() );
			outputToClient(request, response, json );

		} else if ( actionTag.equals( "SubDriver" ) ) {
			String json = driverGeometry.searchPatternForSubDriver( matchTag.toUpperCase() );
			outputToClient(request, response, json );

		}
    }       
       
}