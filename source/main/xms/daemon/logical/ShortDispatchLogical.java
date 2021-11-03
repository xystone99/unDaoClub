
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
@WebServlet(asyncSupported = false, value = "/shortdispatch", loadOnStartup = 7 )
public class ShortDispatchLogical extends AbstractDaemon {
			
	private static final long serialVersionUID = 1L;
	private static final String ADD_WAYBILL_SQL = "{CALL proc_short_add_waybill(?,?,?,  ?)}";
	private static final String REMOVE_WAYBILL_SQL = "{CALL proc_short_remove_waybill(?,?,  ?)}";

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) {
		if ( checkSessionExpired( request ) ) {
			return;
		}
		String action = request.getParameter( "Action" );
		StringBuilder bufJSON = new StringBuilder( );
		bufJSON.append( "{" );
		
		if ( action.equals( "AddWayBill" ) ) {	
			String dispt = request.getParameter( "Dispt" );
			String disptK = request.getParameter( "DisptK" );
			String waybillQtys = request.getParameter( "WayBillQtys" );
			
			ParamLayer paramLayer = new ParamLayer( );
			paramLayer.addParam( ParamLayer.PARAM_STRING, dispt );
			paramLayer.addParam( ParamLayer.PARAM_STRING, disptK );
			paramLayer.addParam( ParamLayer.PARAM_STRING, waybillQtys );
			
			String result = DBUtils.executeCall(XmsInitial.getDataSource(), ADD_WAYBILL_SQL, paramLayer);
			if ( result.equals("AddSuccess") ) {
				bufJSON.append( makeJsonItem("Result", "Success") );
			} else if ( result.equals("FailedForInvalid") ) {
				bufJSON.append( makeJsonItem("Result", "Invalid") );
			} else {
				bufJSON.append( makeJsonItem("Result", "Unknown") );
			}
			
		} else if ( action.equals( "RemoveWayBill" ) ) {
			String dispt = request.getParameter( "Dispt" );
			String disptIdList = request.getParameter( "DisptIDList" );
			
			ParamLayer paramLayer = new ParamLayer( );
			paramLayer.addParam( ParamLayer.PARAM_STRING, dispt );
			paramLayer.addParam( ParamLayer.PARAM_STRING, disptIdList );
			
			String result = DBUtils.executeCall(XmsInitial.getDataSource(), REMOVE_WAYBILL_SQL, paramLayer);
			
			if ( result.equals("RemoveSuccess") ) {
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