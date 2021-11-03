
package undaoclub.daemon;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xms.XmsInitial;

import com.undao.control.AbstractDaemon;
import com.undao.database.*;
import com.undao.utils.DateUtils;

/**
 * @author X.Stone
 *
 */
public class GeoDownload extends AbstractDaemon {
			
	private static final long serialVersionUID = 1L;
	private final static String SQL_GEO_RELATION = "SELECT dt_start, dt_end FROM tbl_user_relation WHERE lover_r=? AND user_i_w=? AND RIGHT(acode_file_name,3)=?";
	private final static String SQL_GEO_RECORD_1 = "SELECT longitude, latitude, duration, input_date FROM trn_geo_record_";
	private final static String SQL_GEO_RECORD_2 = " WHERE user_i=? AND input_date>=? AND input_date<=? ORDER BY input_date ASC";
	
	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) {

		String userID = request.getParameter( "UserID" );
		String loverR = request.getParameter( "LoverR" );
		String checkCode = request.getParameter( "CheckCode" );
		String splitTag = request.getParameter( "SplitTag" );
		String fetchDate = request.getParameter( "FetchDate" );
		
		String[] arrParams = { loverR, userID, checkCode };
		CommonSet dataSet = DBUtils.prepareQuery( XmsInitial.getDataSource(), SQL_GEO_RELATION, arrParams );
		
		StringBuilder buf = new StringBuilder( );
		buf.append( "[" );
		if ( dataSet.getRowCount() > 0 ) {
			StringBuilder bufSQL = new StringBuilder();
			bufSQL.append( SQL_GEO_RECORD_1 ).append( splitTag ).append( SQL_GEO_RECORD_2 );
			String date1 = fetchDate + " 00:00:01";
			String date2 = fetchDate + " 23:59:59";
			String dateStart = DateUtils.formatDateTime( dataSet.getValue( "dt_start" ) );
			String dateEnd = DateUtils.formatDateTime( dataSet.getValue( "dt_end" ) );
			if ( date1.compareTo( dateStart ) < 0 ) {
				date1 = dateStart;
			}
			if ( date2.compareTo( dateEnd ) > 0 ) {
				date2 = dateEnd;
			}
			String[] arrParams2 = { userID, date1, date2 };
			CommonSet geoSet = DBUtils.prepareQuery( XmsInitial.getDataSource(), bufSQL.toString(), arrParams2 );
			if ( geoSet.getRowCount() > 0 ) {
				for ( int j = 0; j < geoSet.getRowCount(); j ++ ) {
		    		buf.append( "{" );
		    		buf.append( makeJsonItem("longitude", geoSet.getValue(j,"longitude") ) ).append( ", " );
		    		buf.append( makeJsonItem("latitude", geoSet.getValue(j,"latitude") ) );
		    		buf.append( "}," );
		    	}
				buf.deleteCharAt( buf.length() -1 );
			} else {
				buf.append( "{ \"longitude\":\"113.322596\", \"latitude\":\"23.098596\" }" );
			}
		} else {
			buf.append( "{ \"longitude\":\"113.322595\", \"latitude\":\"23.098595\" }" );
		}
		buf.append( "]" );
		
		outputToClient(request, response, buf.toString() ); 

    } 
  
}