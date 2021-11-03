
package xms.daemon.logical;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.undao.control.AbstractDaemon;
import com.undao.database.DBUtils;
import com.undao.database.ParamLayer;

import xms.XmsInitial;

/**
 * @author X.Stone
 *
 */
@WebServlet(asyncSupported = false, value = "/checkaccount", loadOnStartup = 7 )
public class CheckAccountLogical extends AbstractDaemon {
			
	private static final long serialVersionUID = 1L;
	private static final String ADD_SQL = "{CALL proc_check_add_fees(?,?,?,  ?)}";
	private static final String REMOVE_SQL = "{CALL proc_check_remove_fees(?,?,?,  ?)}";
	
	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) {
		if ( checkSessionExpired( request ) ) {
			return;
		}
		String action = request.getParameter( "Action" );
		StringBuilder bufJSON = new StringBuilder( );
		bufJSON.append( "{" );
		
		if ( action.equals( "AddFees" ) ) {
			ParamLayer paramLayer = new ParamLayer( );
			paramLayer.addParam( ParamLayer.PARAM_STRING, request.getParameter("ID") );
			paramLayer.addParam( ParamLayer.PARAM_STRING, request.getParameter("FeeIDs") );
			paramLayer.addParam( ParamLayer.PARAM_STRING, AbstractDaemon.getLanderID(request) );
			
			String result = DBUtils.executeCall(XmsInitial.getDataSource(), ADD_SQL, paramLayer);
			
			if ( result.equals("AddFeesSuccess") ) {
				bufJSON.append( makeJsonItem("Result", "Success") );
			} else if ( result.equals("FailedForInvalid") ) {
				bufJSON.append( makeJsonItem("Result", "Invalid") );
			} else {
				bufJSON.append( makeJsonItem("Result", "Unknown") );
			}
		
		} else if ( action.equals( "RemoveFees" ) ) {
			ParamLayer paramLayer = new ParamLayer( );
			paramLayer.addParam( ParamLayer.PARAM_STRING, request.getParameter("ID") );
			paramLayer.addParam( ParamLayer.PARAM_STRING, request.getParameter("FeeIDs") );
			paramLayer.addParam( ParamLayer.PARAM_STRING, AbstractDaemon.getLanderID(request) );
			
			String result = DBUtils.executeCall(XmsInitial.getDataSource(), REMOVE_SQL, paramLayer);
			
			if ( result.equals("RemoveFeesSuccess") ) {
				bufJSON.append( makeJsonItem("Result", "Success") );
			} else if ( result.equals("FailedForInvalid") ) {
				bufJSON.append( makeJsonItem("Result", "Invalid") );
			} else {
				bufJSON.append( makeJsonItem("Result", "Unknown") );
			}	
			
		}
		
		bufJSON.append( "}" );
		outputToClient(request, response, bufJSON.toString( ) );
    }
       
}