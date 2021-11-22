
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

		StringBuilder buf = new StringBuilder( );
		buf.append( "[" );

    	if ( actionTag.equals( "Driver" ) ) {
			buf.append(driverGeometry.searchPattern(matchTag.toUpperCase()));

			buf.append(",{");
			buf.append(AbstractDaemon.makeJsonItem("ID", "0")).append(",");
			buf.append(AbstractDaemon.makeJsonItem("Mobile", "")).append(",");
			buf.append(AbstractDaemon.makeJsonItem("Name", "")).append(",");
			buf.append(AbstractDaemon.makeJsonItem("value", "社会司机")).append(",");
			buf.append(AbstractDaemon.makeJsonItem("label", "社会司机..."));
			buf.append("}");

		} else if ( actionTag.equals( "TruckIdle" ) ) {
			buf.append(driverGeometry.searchPattern(matchTag.toUpperCase()));

		} else if ( actionTag.equals( "SubDriver" ) ) {
			buf.append( driverGeometry.searchPatternForSubDriver( matchTag.toUpperCase() ) );

		}

		buf.append( "]" );
		outputToClient(request, response, buf.toString() );

    }       
       
}