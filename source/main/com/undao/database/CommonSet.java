/**
 *
 * Created on 20080312, 10:18 A.M
 */
package com.undao.database;

/**
 * @author X.Stone
 *
 */
import java.io.Serializable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class CommonSet implements Serializable, DatabaseConstants {
	
	private static final long serialVersionUID = 6350122972582012041L;
	
	private int total_record_count = 0;						//查询的总记录数(非记录列表中记录数)
	private ArrayList<HashMap<String,Object>> data_list;	//记录列表
	private ArrayList<String> header_list;					//列标题(即检索视图或表中的字段名)

	public CommonSet( ) {
		data_list = new ArrayList<HashMap<String,Object>>( );
		header_list = new ArrayList<String>( );
	}

	/**
	 * 构造器
	 * @param initialCapacity	: 分配初始内存空间
	 */
	public CommonSet( int initialCapacity ) {
		data_list = new ArrayList<HashMap<String,Object>>( initialCapacity );
		header_list = new ArrayList<String>( );
	}
	
	/**
	 * 设置列标题
	 * @param metaData	: ResultSet元数据
	 */
	public void setMetaData( ResultSetMetaData metaData ) {
		try {
			int cols = metaData.getColumnCount();
	        for ( int index = 1; index <= cols; index ++ ) {
	        	header_list.add( metaData.getColumnName( index ) );
	        }
		} catch( SQLException sqle ) {
			System.err.println( "CommonSet>>setMetaData Error:" + sqle.getMessage() );
		}		
	}
	
	public String getHeaderTag( int col ) {
		return header_list.get( col );
	}
	
	/**
	 * 查询的总记录数
	 */
	public void setTotalCount( int totalCount ) {
		this.total_record_count = totalCount;
	}
		
	public int getTotalCount( ) {
		return total_record_count;		
	}
	
	/**
	 * 向记录集中新增一条记录
	 */
	public void addRow( HashMap<String,Object> mapRow ) {
		data_list.add( mapRow );
	}
	
	/**
	 * 记录集的列数
	 */
	public int getColumnCount( ) {
		return header_list.size( );
	}
	
	/**
	 * 记录集的行数
	 */
	public int getRowCount( ) {
		return data_list.size( );
	}
		
	/**
	 * 给定XY详细坐标,从记录集中取值
	 */
	public Object getValue( int row, String tag ) {
		return data_list.get( row ).get( tag );
	}
	
	public Object getValue( int row, int col ) {
		return data_list.get( row ).get( header_list.get( col ) );
	}
	
	/**
	 * 给定Y坐标,从记录集的第一行取值
	 */
	public Object getValue( String tag ) {
		return data_list.size( ) > 0 ? data_list.get( 0 ).get( tag ) : null;
	}
	
	public Object getValue( int col ) {
		return data_list.size( ) > 0 ? data_list.get( 0 ).get( header_list.get( col ) ) : null;
	}
	
	/**
	 * 将数据集中的第一行转换为HashMap<String,String>类型
	 */
	public HashMap<String,String> convertStringMap( ) {
		if ( data_list.size( ) == 0 ) {
			return null;
		}
		HashMap<String,Object> map = data_list.get( 0 );
		HashMap<String,String> rowMap = new HashMap<String,String>();
		Iterator<String> list = map.keySet().iterator();
		while ( list.hasNext() ) {
			String key = list.next();
			rowMap.put(key, map.get(key).toString() );
		}
		return rowMap;
	} 
				
}