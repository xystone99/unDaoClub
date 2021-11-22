
package yundeploy.daemon;

import com.undao.cache.DriverGeometry;
import com.undao.cache.SystemicVariables;
import com.undao.control.AbstractDaemon;
import xms.XmsInitial;
import yundeploy.cache.YunSystemicVariables;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author X.Stone
 * http://localhost:3721/unDao/yunmanage?Action=InitSystemicVariables&CloudID=XYZABC
 * http://localhost:3721/unDao/yunmanage?Action=RefreshSystemicVariables&CloudID=XYZABC
 */
@WebServlet(asyncSupported = false, value = "/yunmanage", loadOnStartup = 1 )
public class YunManager extends AbstractDaemon {
			
	private static final long serialVersionUID = 1L;

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) {
		if ( checkSessionExpired( request ) ) {
			return;
		}
		String actionTag = request.getParameter( "Action" );
		String cloudID = request.getParameter( "CloudID" );
		YunSystemicVariables stcVariables = (YunSystemicVariables)XmsInitial.getXmsContainer().getSystemicVariables();

		StringBuilder bufJSON = new StringBuilder( );
		bufJSON.append( "{" );

    	if ( actionTag.equals( "InitSystemicVariables" ) ) {
			stcVariables.initialVariables( cloudID );
			bufJSON.append( makeJsonItem("Result", "InitSuccess") );

		} else if ( actionTag.equals( "RefreshSystemicVariables" ) ) {
			stcVariables.refreshSingletonObject( cloudID );
			bufJSON.append( makeJsonItem("Result", "RefreshSuccess") );

    	}

		bufJSON.append( "}" );
		outputToClient(request, response, bufJSON.toString( ) );
    }       
       
}