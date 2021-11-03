/**
 * AbstractMaster.java 
 *
 * Created at 2009-7-7, 上午09:23:19
 */
package com.undao.database;

/**
 * @author X.Stone
 *
 */
import java.util.*;

public class AbstractMaster extends AbstractDatabase {

	protected final static String ID_TAG = "masterid";
	protected final static String NAME_TAG = "mastername";

	protected ArrayList<Integer> id_list = new ArrayList<>();
	protected ArrayList<String> flg_list = new ArrayList<String>(); 
	protected HashMap<Integer,String> mapDisplay = new HashMap<Integer,String>();

	private String strSQL;
	protected AbstractMaster( String strSQL ) {
		this.strSQL = strSQL;
	}

	public void fixSingletonObject( ) {
		id_list.clear( );	
		flg_list.clear( );
		mapDisplay.clear( );

		StringBuilder bufAllOptions = new StringBuilder();
		StringBuilder bufNormalOptions = new StringBuilder();
		CommonSet dataList = DBUtils.executeQuery( getDataSource(), strSQL,false );

		for( int j=0; j<dataList.getRowCount(); j++ ) {
			Integer key = (Integer)dataList.getValue(j,ID_TAG);
			String flg = (String)dataList.getValue(j,"sys_flg");
			String display = (String)dataList.getValue(j,NAME_TAG);
			id_list.add( key );
			flg_list.add( flg );
			mapDisplay.put( key, display );

			bufAllOptions.append( "<option value=\"" ).append( key.toString() ).append( "\">" ).append( display ).append( "</option>" );
			if ( flg_list.get(j).equals( "Normal" ) ) {
				bufNormalOptions.append( "<option value=\"" ).append( key.toString() ).append( "\">" ).append( display ).append( "</option>" );
			}
		}
		ALL_OPTIONS = bufAllOptions.toString( );
		NORMAL_OPTIONS = bufNormalOptions.toString( );
	}

	public int size( ) {
    	return id_list.size( );
    }
    
    public String getID( int index ) {
    	return id_list.get( index ).toString();
    }
    
    public String getDisplay( int index ) {
    	return mapDisplay.get( id_list.get(index) );
    }
    
    public String getDisplayByID( Integer masterID ) {
    	return mapDisplay.get( masterID );
    }
    
    public String getDisplayOfID( String masterID ) {
    	return mapDisplay.get( new Integer(masterID) );
    }
    
    public String getDisplayOfID( int masterID ) {
    	return mapDisplay.get( new Integer( masterID ) );
    }    
    
    /**
     * 用户端快速访问
     */
	private String ALL_OPTIONS = "";
    private String NORMAL_OPTIONS = "";

	public String getAllOptions( ) {
		return ALL_OPTIONS;
	}

    public String getNormalOptions( ) {
    	return NORMAL_OPTIONS;
    }

    public void rebuildOptions( ) {
		StringBuilder bufAllOptions = new StringBuilder();
		StringBuilder bufNormalOptions = new StringBuilder();
    	for ( int j=0; j<id_list.size(); j++ ) {
			Integer key = id_list.get(j);
			String display = mapDisplay.get(key);
			bufAllOptions.append( "<option value=\"" ).append( key ).append( "\">" ).append( display ).append( "</option>" );
    		if ( flg_list.get(j).equals( "Normal" ) ) {
				bufNormalOptions.append( "<option value=\"" ).append( key ).append( "\">" ).append( display ).append( "</option>" );
 			}
    	}
    	ALL_OPTIONS = bufAllOptions.toString( );
    	NORMAL_OPTIONS = bufNormalOptions.toString( );
	}
    
    /**
     * 依据KEY数组生成SELECT组件的OPTION列表
     */
    public final static String createOptions( String[] arrKey, HashMap<String,String> map ) {
    	StringBuilder bufOptions = new StringBuilder();
		for (String key : arrKey) {
			bufOptions.append("<option value=\"").append(key).append("\">").append(map.get(key)).append("</option>");
		}
    	return bufOptions.toString();
    }
    
}
