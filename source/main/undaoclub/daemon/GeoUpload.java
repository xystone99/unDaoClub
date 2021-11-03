
package undaoclub.daemon;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xms.XmsInitial;

import com.undao.control.AbstractDaemon;
import com.undao.database.*;

/**
 * @author X.Stone
 *
 */
public class GeoUpload extends AbstractDaemon {
			
	private static final long serialVersionUID = 1L;
	private final static String SQL_GEO_RECORD_NEW = "{CALL proc_geo_record_new(?,?,?,?,?,?,  ?  ) }";
	
	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) {

		String openID = request.getParameter( "OpenID" );
		String userID = request.getParameter( "UserID" );
		String splitTag = request.getParameter( "SplitTag" );
		String longitude = request.getParameter( "Longitude" );
		String latitude = request.getParameter( "Latitude" );
		String duration = request.getParameter( "Duration" );
			
		ParamLayer paramLayer = new ParamLayer();
		paramLayer.addParam(ParamLayer.PARAM_STRING, openID );
		paramLayer.addParam(ParamLayer.PARAM_STRING, userID );
		paramLayer.addParam(ParamLayer.PARAM_STRING, splitTag );
		paramLayer.addParam(ParamLayer.PARAM_STRING, longitude );
		paramLayer.addParam(ParamLayer.PARAM_STRING, latitude );
		paramLayer.addParam(ParamLayer.PARAM_STRING, duration );
		String result = DBUtils.executeCall( XmsInitial.getDataSource(), SQL_GEO_RECORD_NEW, paramLayer );
		
		StringBuilder buf = new StringBuilder( );
		buf.append( "{" );
		buf.append( "\"Status\":\"" ).append( result ).append( "\"," );
		buf.append( "\"Other\":\"" ).append( System.currentTimeMillis() ).append( "\"" );
		buf.append( "}" );
		
		outputToClient(request, response, buf.toString() ); 

    } 
  
}