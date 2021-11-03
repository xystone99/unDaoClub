
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
@WebServlet(asyncSupported = false, value = "/package", loadOnStartup = 7 )
public class PackageLogical extends AbstractDaemon {
			
	private static final long serialVersionUID = 1L;
	private static final String ADD_RECEIPT_SQL = "{CALL proc_package_receipt_add(?,?,  ?)}";
	private static final String REMOVE_RECEIPT_SQL = "{CALL proc_package_receipt_remove(?,?,  ?)}";

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) {
		if ( checkSessionExpired( request ) ) {
			return;
		}
		String action = request.getParameter( "Action" );
		StringBuilder bufJSON = new StringBuilder( );
		bufJSON.append( "{" );
		
		if ( action.equals( "AddReceipt" ) ) {	
			String packageID = request.getParameter( "PackageID" );
			String recptList = request.getParameter( "ReceiptList" );
			
			ParamLayer paramLayer = new ParamLayer( );
			paramLayer.addParam( ParamLayer.PARAM_STRING, packageID );
			paramLayer.addParam( ParamLayer.PARAM_STRING, recptList );
			
			String result = DBUtils.executeCall(XmsInitial.getDataSource(), ADD_RECEIPT_SQL, paramLayer);
			if ( result.equals("ReceiptAddSuccess") ) {
				bufJSON.append( makeJsonItem("Result", "Success") );
			} else if ( result.equals("FailedForInvalid") ) {
				bufJSON.append( makeJsonItem("Result", "Invalid") );
			} else {
				bufJSON.append( makeJsonItem("Result", "Unknown") );
			}
			
		} else if ( action.equals( "RemoveReceipt" ) ) {
			String packageID = request.getParameter( "PackageID" );
			String recptIdList = request.getParameter( "ReceiptIDList" );
			
			ParamLayer paramLayer = new ParamLayer( );
			paramLayer.addParam( ParamLayer.PARAM_STRING, packageID );
			paramLayer.addParam( ParamLayer.PARAM_STRING, recptIdList );
			
			String result = DBUtils.executeCall(XmsInitial.getDataSource(), REMOVE_RECEIPT_SQL, paramLayer);
			
			if ( result.equals("ReceiptRemoveSuccess") ) {
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