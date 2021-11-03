
package com.undao.cache;

import java.util.*;

import com.undao.control.*;
import com.undao.database.*;

/**
 * @author Administrator
 *
 */
public class TransLineGeometry extends AbstractDatabase {

	private static String FETCH_SQL = "SELECT trans_l,line_tag,obj_p,plan_k,time_level,ne_zh1,address_1,linkman_1,window_1,remark_1,ne_zh2,address_2,linkman_2,window_2,remark_2,route_zh FROM tbl_trans_line WHERE cloud_id='XYZABC' ORDER BY obj_p ASC, sort_tag ASC";
	
	private static TransLineGeometry instance = null;
	private TransLineGeometry( ) {
	}
	public static TransLineGeometry getInstance( ) {
		if ( instance == null ) {
			synchronized( TransLineGeometry.class ) {
				if ( instance == null ) instance = new TransLineGeometry( );
			}
		}
		return instance;
	}

	private class TransLine {
		public String ID;
		public String LineTag;
		public String PlanK;
		public String TimeLevel;
		public String Name1;
		public String Address1;
		public String Linkman1;
		public String Window1;
		public String Remark1;
		public String Name2;
		public String Address2;
		public String Linkman2;
		public String Window2;
		public String Remark2;
		public String RouteName;
		public TransLine( String id, String lineTag ) {
			this.ID = id;
			this.LineTag = lineTag;
		}
		public void setPlanK( String planK ) { this.PlanK = planK;		}
		public void setTimeLevel( String timeLevel ) { this.TimeLevel = timeLevel;		}
		public void setRouteName( String routeName ) { this.RouteName = routeName;		}
		public void setName1( String name1 ) { this.Name1 = name1;	 }
		public void setAddress1( String address1 ) { this.Address1 = address1;  }
		public void setLinkman1( String linkman1 ) { this.Linkman1 = linkman1;	 }
		public void setWindow1( String window1 ) { this.Window1 = window1;		}
		public void setRemark1( String remark1 ) { this.Remark1 = remark1;		 }
		public void setName2( String name2 ) { this.Name2 = name2;	 }
		public void setAddress2( String address2 ) { this.Address2 = address2;  }
		public void setLinkman2( String linkman2 ) { this.Linkman2 = linkman2;	 }
		public void setWindow2( String window2 ) { this.Window2 = window2;		}
		public void setRemark2( String remark2 ) { this.Remark2 = remark2;		 }
	}

	private HashMap<String,ArrayList<TransLine>> mapTransLine = new HashMap<String,ArrayList<TransLine>>();

	public void fixSingletonObject( ) {
		mapTransLine.clear();
		CommonSet dataList = DBUtils.executeQuery( getDataSource(), FETCH_SQL, false );

		for( int j=0; j<dataList.getRowCount(); j++ ) {
			ArrayList<TransLine> arrTransLine;
			String cusID = ((Long)dataList.getValue(j,"obj_p")).toString();
			TransLine transLine = new TransLine( ((Long)dataList.getValue(j,"trans_l")).toString(), (String)dataList.getValue(j,"line_tag") );
			transLine.setPlanK( (String)dataList.getValue(j,"plan_k") );
			transLine.setTimeLevel( (String)dataList.getValue(j,"time_level") );
			transLine.setRouteName( (String)dataList.getValue(j,"route_zh") );
			transLine.setName1( (String)dataList.getValue(j,"ne_zh1") );
			transLine.setAddress1( (String)dataList.getValue(j,"address_1") );
			transLine.setLinkman1( (String)dataList.getValue(j,"linkman_1") );
			transLine.setWindow1( (String)dataList.getValue(j,"window_1") );
			transLine.setRemark1( (String)dataList.getValue(j,"remark_1") );
			transLine.setName2( (String)dataList.getValue(j,"ne_zh2") );
			transLine.setAddress2( (String)dataList.getValue(j,"address_2") );
			transLine.setLinkman2( (String)dataList.getValue(j,"linkman_2") );
			transLine.setWindow2( (String)dataList.getValue(j,"window_2") );
			transLine.setRemark2( (String)dataList.getValue(j,"remark_2") );

			if ( mapTransLine.containsKey( cusID ) ) {
				arrTransLine = mapTransLine.get( cusID );
				arrTransLine.add( transLine );
			} else {
				arrTransLine = new ArrayList<TransLine>();
				arrTransLine.add( transLine );
				mapTransLine.put( cusID, arrTransLine );
			}
		}	
	}

	/**
	 * 根据快速检索代码匹配
	 * @param cusID	: AutoComplete输入字符串
	 * @return		：包含项目信息和费用模板信息的JSON字符串
	 */
	public String searchPattern( String cusID, String lineTag ) {
		ArrayList<TransLine> arrTransLine = mapTransLine.get( cusID );
		if ( arrTransLine == null ) {
			return SQL_ZERO;
		}
		int counter = 0;
		StringBuilder buf = new StringBuilder( );
		buf.append( "[" );
		for ( int j=0; j<arrTransLine.size(); j++ ) {
			TransLine transLine = arrTransLine.get( j );
			buf.append( "{" );
			buf.append( AbstractDaemon.makeJsonItem("ID", transLine.ID ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("PlanK", transLine.PlanK ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("TimeLevel", transLine.TimeLevel ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("RouteName", transLine.RouteName ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("Name1", transLine.Name1 ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("Address1", transLine.Address1 ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("Linkman1", transLine.Linkman1 ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("Window1", transLine.Window1 ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("Remark1", transLine.Remark1 ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("Name2", transLine.Name2 ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("Address2", transLine.Address2 ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("Linkman2", transLine.Linkman2 ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("Window2", transLine.Window2 ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("Remark2", transLine.Remark2 ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("value", transLine.LineTag ) ).append( "," );
			buf.append( AbstractDaemon.makeJsonItem("label", transLine.LineTag ) );
			buf.append( "}," );
			counter++;
			if ( counter >= 12 ) break;
		}
		buf.deleteCharAt( buf.length() -1 );
		buf.append( "]" );
		return buf.toString( );
	}
	
}
