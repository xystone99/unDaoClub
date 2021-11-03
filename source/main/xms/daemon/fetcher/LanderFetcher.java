
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
@WebServlet(asyncSupported = false, value = "/fetchlander", loadOnStartup = 5 )
public class LanderFetcher extends AbstractDaemon {
			
	private static final long serialVersionUID = 1L;

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) {
		if ( checkSessionExpired( request ) ) {
			return;
		}
		String matchTag = request.getParameter("term");
    	EmployeeGeometry empGeometry = EmployeeGeometry.getInstance();
    	
    	String json = empGeometry.searchLander( matchTag.toUpperCase() );
    	outputToClient(request, response, json );

    }       
       
}