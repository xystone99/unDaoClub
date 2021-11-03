
package xms.daemon;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.undao.control.AbstractDaemon;
import com.undao.http.AliUtils;

/**
 * @author X.Stone
 *
 */
@WebServlet(asyncSupported = false, value = "/ddloginverify", loadOnStartup = 9 )
public class DingLoginVerify extends AbstractDaemon {
			
	private static final long serialVersionUID = 1L;

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) {
		String code = request.getParameter( "Code" );
		
		String token = AliUtils.getAccessToken();
		String userID = AliUtils.getUserID( token, code );
		String userName = AliUtils.getUserName( token, userID );
		
		StringBuilder buf = new StringBuilder( );
		buf.append( "{" );
		buf.append( "\"AliID\":\"" ).append( userID ).append( "\"," );
		buf.append( "\"AliName\":\"" ).append( userName ).append( "\"" );
		buf.append( "}" );
		
		outputToClient(request, response, buf.toString() );

    } 
  
}