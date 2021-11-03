
package xms.daemon.fetcher;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.undao.control.AbstractDaemon;
import com.undao.cache.*;

/**
 * @author X.Stone
 *
 */
@WebServlet(asyncSupported = false, value = "/fetchtransline", loadOnStartup = 5 )
public class TransLineFetcher extends AbstractDaemon {
			
	private static final long serialVersionUID = 1L;

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) {
		if ( checkSessionExpired( request ) ) {
			return;
		}
		String cusID = request.getParameter( "CusID" );
		String matchTag = request.getParameter("term").trim();
		TransLineGeometry tlGeometry = TransLineGeometry.getInstance();

		String json = tlGeometry.searchPattern( cusID, matchTag.toUpperCase() );
		outputToClient(request, response, json );

    } 
  
}