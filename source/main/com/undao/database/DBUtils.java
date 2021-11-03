/**
 * DBUtils.java
 *
 * Created on 20080312, 10:03 A.M
 */
package com.undao.database;

/**
 * @author X.Stone
 *
 */
import com.mysql.cj.x.protobuf.MysqlxDatatypes;

import java.io.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.math.BigDecimal;

import javax.sql.DataSource;   
   
public class DBUtils implements DatabaseConstants {
  	
	public static boolean DEBUG = true;
	
	/**
	 * 检索一个表或视图的总记录数
	 */
	public static final int fetchTotalCount( DataSource dataSource, String tableName ) {
		StringBuilder bufSQL = new StringBuilder( );
		bufSQL.append( "SELECT COUNT(*) AS count_rows FROM " ).append( tableName );
		Connection conn = null;   
		Statement stmt = null;
		ResultSet rs = null;
		try {    
        	conn = dataSource.getConnection( );
            stmt = conn.createStatement( );
            rs = stmt.executeQuery( bufSQL.toString() );
            if ( rs.next() ) {
            	return rs.getInt( "count_rows" );
            }
		} catch( SQLException sqle ){
        	if(DEBUG) System.err.println( "DBUtils>>fetchTotalCount(" + bufSQL.toString() + ") Error:" + sqle.getMessage() );
        } finally {
        	close( conn, stmt, rs );
		} 
        return 0;
	}
	
	/**
	 * SQL Query Statement
	 */   
	public static final CommonSet executeQuery( DataSource dataSource, String strSQL ) {
		return executeQuery( dataSource, strSQL, false, false );
	}
	
	public static final CommonSet executeQuery( DataSource dataSource, String strSQL, boolean queryTotalCount ) {
		return executeQuery( dataSource, strSQL, queryTotalCount, false );
	}
	
	public static final CommonSet executeQuery( DataSource dataSource, String strSQL, boolean queryTotalCount, boolean useAlias ) {
		Connection conn = null;   
		Statement stmt = null, stmt_count = null;
		ResultSet rs = null, rs_count = null;
    	CommonSet data_list = new CommonSet( );
    	
        try {    
        	conn = dataSource.getConnection( );
            stmt = conn.createStatement( );
            rs = stmt.executeQuery( strSQL );
            
            if ( queryTotalCount ) {
            	stmt_count = conn.createStatement( ); 
                rs_count = stmt_count.executeQuery( "SELECT FOUND_ROWS() count_rows" );
                rs_count.next( );
                data_list.setTotalCount( rs_count.getInt("count_rows") );
            }
			fillCommonSet( data_list, rs, useAlias );
        } catch( SQLException sqle ){
        	if(DEBUG) System.err.println( "DBUtils>>executeQuery(" + strSQL + ") Error:" + sqle.getMessage() );
        } finally {
        	if ( queryTotalCount ) close( null, stmt_count, rs_count );
        	close( conn, stmt, rs );
		}  
        return data_list;
    }  
	
	public static final HashMap<String,HashMap<String,Object>> executeQueryMap( DataSource dataSource, String strSQL, String[] keys ) {
		Connection conn = null;   
		Statement stmt = null;
		ResultSet rs = null;
    	HashMap<String,HashMap<String,Object>> qResult = new HashMap<String,HashMap<String,Object>>();
    	
        try {    
        	conn = dataSource.getConnection( );
            stmt = conn.createStatement( );
            rs = stmt.executeQuery( strSQL );
            
            ResultSetMetaData md = rs.getMetaData( );
            int cols = md.getColumnCount( );
            
            while( rs.next( ) ) {
                HashMap<String,Object> row = new HashMap<String,Object>( );
                for( int i = 1; i <= cols; i++ ) {
                    row.put( md.getColumnName(i), rs.getObject(i) );
                }
                StringBuilder bufKey = new StringBuilder();
                for( int j=0; j<keys.length; j++ ) {
                	bufKey.append( rs.getObject(keys[j]) );
                }
                qResult.put( bufKey.toString(), row );
            }                       	
        } catch( SQLException sqle ){
        	if(DEBUG) System.err.println( "DBUtils>>executeQueryMap(" + strSQL + ") Error:" + sqle.getMessage() );
        } finally {
        	close( conn, stmt, rs );
		}  
        return qResult;
    }
	  
    /**
     * SQL Prepare Query Statement 
     */
	public static final CommonSet prepareQuery( DataSource dataSource, String strSQL, String id ) {						//BLOB NOT SUPPORTED
		String[] arrParams = { id };
		return prepareQuery( dataSource, strSQL, arrParams );
	}
    public static final CommonSet prepareQuery( DataSource dataSource, String strSQL, String param1, String param2 ) {	//BLOB NOT SUPPORTED
    	String[] arrParams = { param1, param2 };
    	return prepareQuery( dataSource, strSQL, arrParams );
    }
    
    public static final CommonSet prepareQuery( DataSource dataSource, String strSQL, String[] arrParams ) {			//BLOB NOT SUPPORTED
    	Connection conn = null; 
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	CommonSet data_list = new CommonSet( );
    	try {
    		conn = dataSource.getConnection( );	
    		pstmt = conn.prepareStatement( strSQL );
    		for ( int j=0; j<arrParams.length; j++ ) {
    			pstmt.setString( j+1, arrParams[j] );
    		}
    		  		
    		rs = pstmt.executeQuery( );
			fillCommonSet( data_list, rs, false );
    	} catch(SQLException sqle) {   
			if(DEBUG) System.out.println( "DBUtils>>prepareQuery(" + strSQL + ") Error:" + sqle.getMessage() );   
		} finally {
			close( conn, pstmt, rs );
		} 
		return data_list;
    }
    
    public static final CommonSet prepareQuery( DataSource dataSource, String strSQL, ParamLayer paramLayer ) {
    	return prepareQuery( dataSource, strSQL, paramLayer, false );
    }
       
    public static final CommonSet prepareQuery( DataSource dataSource, String strSQL, ParamLayer paramLayer, boolean queryTotalCount ) { //BLOB NOT SUPPORTED
    	Connection conn = null; 
    	PreparedStatement pstmt = null;
    	Statement stmt_count = null;
    	ResultSet rs = null, rs_count = null;
    	CommonSet data_list = new CommonSet( );
    	try {
    		conn = dataSource.getConnection( );	
    		pstmt = conn.prepareStatement( strSQL );
    		
    		int size = ( paramLayer == null ) ? 0 : paramLayer.getSize( );
    		for ( int index = 0; index < size; index ++ ) {
    			int param_k = paramLayer.getParamType( index );
    			if ( param_k == PARAM_SHORT ) {
    				pstmt.setShort( index+1, ((Short)paramLayer.getParam(index)).shortValue() );	
    			} else if ( param_k == PARAM_INTEGER ) {
    				pstmt.setInt( index+1, ((Integer)paramLayer.getParam(index)).intValue() );
    			} else if ( param_k == PARAM_STRING ) {
    				pstmt.setString( index+1, (String)paramLayer.getParam(index) );
    			} else if ( param_k == PARAM_DATE ) {
    				pstmt.setString( index+1, (String)paramLayer.getParam(index) );
    			} else if ( param_k == PARAM_DECIMAL ) {
    				pstmt.setBigDecimal( index+1, (BigDecimal)paramLayer.getParam(index) ); 
    			}
    		}
    		rs = pstmt.executeQuery( );
    		if ( queryTotalCount ) {
	    		stmt_count = conn.createStatement( );
	    		rs_count = stmt_count.executeQuery( "SELECT FOUND_ROWS() count_rows" );
	            rs_count.next( );
	            data_list.setTotalCount( rs_count.getInt("count_rows") );
    		}
    		fillCommonSet( data_list, rs, false );
    	} catch(SQLException sqle) {   
			if(DEBUG) System.out.println( "DBUtils>>prepareQuery(" + strSQL + ") Error:" + sqle.getMessage() );   
		} finally {
			if (queryTotalCount) close( null, stmt_count, rs_count );
			close( conn, pstmt, rs );
		} 
		return data_list;
    }
    
	/**
     * SQL Update Statement 
     */
    public static final int executeUpdate( DataSource dataSource, String strSQL ) { 
    	int result = -1;
    	Connection conn = null; 
    	Statement stmt = null;      	
		try {     
			conn = dataSource.getConnection( );
			stmt = conn.createStatement( );
			result = stmt.executeUpdate( strSQL );   
		} catch(SQLException sqle) {   
			if(DEBUG) System.err.println( "DBUtils>>executeUpdate(" + strSQL + ") Error:" + sqle.getMessage() );     
		} finally {
			close( conn, stmt, null );
		}  
		return result;   
    }
    
    /**
     * SQL Prepare Update Statement 
     */
	public static final int prepareUpdate( DataSource dataSource, String strSQL, String param1 ) {
		int result = -1;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection( );
			pstmt = conn.prepareStatement( strSQL );
			pstmt.setString( 1, param1 );
			result = pstmt.executeUpdate( );
		} catch(SQLException sqle) {
			if(DEBUG) System.out.println( "DBUtils>>prepareUpdate(" + strSQL + ") Error:" + sqle.getMessage() );
		} finally {
			close( conn, pstmt, null );
		}
		return result;
	}

	public static final int prepareUpdate( DataSource dataSource, String strSQL, String param1, String param2 ) {
		int result = -1;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection( );
			pstmt = conn.prepareStatement( strSQL );
			pstmt.setString( 1, param1 );
			pstmt.setString( 2, param2 );
			result = pstmt.executeUpdate( );
		} catch(SQLException sqle) {
			if(DEBUG) System.out.println( "DBUtils>>prepareUpdate(" + strSQL + ") Error:" + sqle.getMessage() );
		} finally {
			close( conn, pstmt, null );
		}
		return result;
	}

    public static final int prepareUpdate( DataSource dataSource, String strSQL, String param1, String param2, String param3 ) {
		int result = -1;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection( );
			pstmt = conn.prepareStatement( strSQL );
			pstmt.setString( 1, param1 );
			pstmt.setString( 2, param2 );
			pstmt.setString( 3, param3 );
			result = pstmt.executeUpdate( );
		} catch(SQLException sqle) {
			if(DEBUG) System.out.println( "DBUtils>>prepareUpdate(" + strSQL + ") Error:" + sqle.getMessage() );
		} finally {
			close( conn, pstmt, null );
		}
		return result;
	}
    
    public static final int prepareUpdate( DataSource dataSource, String strSQL, ParamLayer paramLayer ) {
    	int result = -1;
    	Connection conn = null; 
    	PreparedStatement pstmt = null;    	
    	try {
    		conn = dataSource.getConnection( );	
    		pstmt = conn.prepareStatement( strSQL );
    		for ( int index = 0; index < paramLayer.getSize(); index ++ ) {
    			int param_k = paramLayer.getParamType( index );
    			if ( param_k == PARAM_SHORT ) {
    				pstmt.setShort( index+1, ((Short)paramLayer.getParam(index)).shortValue() );	
    			} else if ( param_k == PARAM_INTEGER ) {
    				pstmt.setInt( index+1, ((Integer)paramLayer.getParam(index)).intValue() );
    			} else if ( param_k == PARAM_STRING ) {
    				pstmt.setString( index+1, (String)paramLayer.getParam(index) );
    			} else if ( param_k == PARAM_DATE ) {
    				pstmt.setString( index+1, (String)paramLayer.getParam(index) );
    			} else if ( param_k == PARAM_DECIMAL ) {
    				pstmt.setBigDecimal( index+1, (BigDecimal)paramLayer.getParam(index) ); 
    			}
    		}
    		result = pstmt.executeUpdate( );    		
    	} catch(SQLException sqle) {   
			if(DEBUG) System.out.println( "DBUtils>>prepareUpdate(" + strSQL + ") Error:" + sqle.getMessage() );   
		} finally {
			close( conn, pstmt, null );
		} 
		return result;
    }
    
    public static final int prepareBatch( DataSource dataSource, String strSQL, ArrayList<ParamLayer> layerList ) {
    	Connection conn = null; 
    	PreparedStatement pstmt = null;    	
    	try {
    		conn = dataSource.getConnection( );	
    		pstmt = conn.prepareStatement( strSQL );
    		for ( int j = 0; j < layerList.size( ); j ++ ) {
    			ParamLayer paramLayer = layerList.get( j );	
    			for ( int index = 0; index < paramLayer.getSize( ); index ++ ) {
        			int param_k = paramLayer.getParamType( index );
        			if ( param_k == PARAM_SHORT ) {
        				pstmt.setShort( index+1, ((Short)paramLayer.getParam(index)).shortValue() );	
        			} else if ( param_k == PARAM_INTEGER ) {
        				pstmt.setInt( index+1, ((Integer)paramLayer.getParam(index)).intValue() );
        			} else if ( param_k == PARAM_STRING ) {
        				pstmt.setString( index+1, (String)paramLayer.getParam(index) );
        			} else if ( param_k == PARAM_DATE ) {
        				pstmt.setString( index+1, (String)paramLayer.getParam(index) );
        			} else if ( param_k == PARAM_DECIMAL ) {
        				pstmt.setBigDecimal( index+1, (BigDecimal)paramLayer.getParam(index) ); 
        			}
        		}
    			pstmt.addBatch( );
    		}    		    	  		
    		int[] arrResult = pstmt.executeBatch( ); 
    		int countSuccess = 0;
    		for( int j=0; j<arrResult.length; j++ ) {
    			if ( arrResult[j] > 0 ) {
    				countSuccess++;
    			}
    		}
    		return countSuccess++;
    	} catch(SQLException sqle) {   
			if(DEBUG) System.out.println( "DBUtils>>prepareBatch(" + strSQL + ") Error:" + sqle.getMessage() );
			return -1;
		} finally {
			close( conn, pstmt, null );
		} 
    }
    
    /**
     * SQL Call Statement 
     */
    public static final String generateCallString( String strProcedure, ParamLayer pLayer, int cntReturnValues ) {		//返回执行的存储过程字符串，主要用作测试
		StringBuilder bufCall = new StringBuilder( );
		bufCall.append( strProcedure.substring(1,strProcedure.indexOf("(")+1 ) );
		for ( int j=0; j<pLayer.getSize(); j++ ) {
			bufCall.append( "'" ).append( pLayer.getParam(j) ).append( "'," );
		}
		for ( int j=1; j<=cntReturnValues; j++ ) {
			bufCall.append( "@rValue" ).append( j ).append( "," );
		}
		bufCall.deleteCharAt( bufCall.length()-1 ).append( "); SELECT " );
		for ( int j=1; j<=cntReturnValues; j++ ) {
			bufCall.append( "@rValue" ).append( j ).append( "," );
		}
		return bufCall.deleteCharAt( bufCall.length()-1 ).append( ";" ).toString();
	}
	
    public static final String executeCall( DataSource dataSource, String callStr ) {	//一般用于取STC_COUNTER表
    	Connection conn = null; 
    	CallableStatement cstmt = null;
    	try {
    		conn = dataSource.getConnection( );	
    		cstmt = conn.prepareCall( callStr );
    		cstmt.registerOutParameter( 1, java.sql.Types.VARCHAR );  		
    		cstmt.executeUpdate( );
    		return cstmt.getString( 1 );
    	} catch(SQLException sqle) {   
			if(DEBUG) System.out.println( "DBUtils>>executeCall(" + callStr + ") Error:" + sqle.getMessage() );   
		} finally {
			close( conn, cstmt, null );
		} 
		return "NONE";
    }
    
    public static final String executeCall( DataSource dataSource, String callStr, ParamLayer paramLayer ) {
    	String[] a_result = executeCall( dataSource, callStr, paramLayer, 1 );
    	return a_result[0];
    }
    
    public static final String[] executeCall( DataSource dataSource, String callStr, ParamLayer paramLayer, int cntOut ) {
    	String[] result = new String[cntOut];
    	Connection conn = null; 
    	CallableStatement cstmt = null;
    	try {
    		conn = dataSource.getConnection( );	
    		cstmt = conn.prepareCall( callStr );
    		int size = paramLayer.getSize( );
    		for ( int index = 0; index < size; index ++ ) {
    			int param_k = paramLayer.getParamType( index );
    			if ( param_k == PARAM_SHORT ) {
    				cstmt.setShort( index+1, ((Short)paramLayer.getParam(index)).shortValue() );	
    			} else if ( param_k == PARAM_INTEGER ) {
    				cstmt.setInt( index+1, ((Integer)paramLayer.getParam(index)).intValue() );
    			} else if ( param_k == PARAM_STRING ) {
    				cstmt.setString( index+1, (String)paramLayer.getParam(index) );
    			} else if ( param_k == PARAM_DATE ) {
    				cstmt.setString( index+1, (String)paramLayer.getParam(index) );
    			} else if ( param_k == PARAM_DECIMAL ) {
    				cstmt.setBigDecimal( index+1, (BigDecimal)paramLayer.getParam(index) ); 
    			}
    		}
    		for ( int index = 1; index <= cntOut; index++ ) {
    			cstmt.registerOutParameter( size+index, java.sql.Types.VARCHAR );
    		}    		
    		cstmt.executeUpdate( );
    		
    	    for ( int index = 1; index <= cntOut; index++ ) {
    	    	result[index-1] = cstmt.getString( size + index ); 
    	    } 
    	} catch(SQLException sqle) {   
			if(DEBUG) System.out.println( "DBUtils>>" + generateCallString(callStr,paramLayer,cntOut) + " Error:" + sqle.getMessage() );   
			result[cntOut-1] = "Exception";
		} finally {
			close( conn, cstmt, null );
		} 
		return result;
    }     	
	
    /**
     * Backup && Recovery 
     */
    public static final String backup( String mysqlBinDir, String destDir, String dbUserName, String dbUserPwd, String dbName ) {
    	SimpleDateFormat fmt_datetime = new SimpleDateFormat( "yyyyMMdd_HHmmss_" );
    	StringBuilder outPath = new StringBuilder( );
    	outPath.append( destDir ); 
    	File backupPath = new File( outPath.toString() );
    	if ( !backupPath.exists( ) ) {
    		backupPath.mkdirs( );
    	}
    	outPath.append( fmt_datetime.format( new Date() ) ).append( dbName ).append( ".sql" );
    	
    	StringBuilder buf = new StringBuilder( );
    	buf.append( "\"" ).append( mysqlBinDir ).append( File.separator );
    	buf.append( "MYSQLDUMP\" -hlocalhost -u" ).append( dbUserName ).append( " -p" ).append( dbUserPwd );
    	buf.append( " --opt --lock-all-tables --flush-logs --master-data=2 --result-file=" ).append( outPath.toString() ).append( " " ).append( dbName );
    	
    	LineNumberReader input = null;
    	BufferedWriter writer = null;
        try {
            Process p = Runtime.getRuntime().exec( buf.toString() );
            input = new LineNumberReader( new InputStreamReader( p.getInputStream(), "utf8" ) );	//为了防止乱码,设置输出流编码为UTF8
            writer = new BufferedWriter( new FileWriter( new File( outPath.toString() ) ) );
            
            String line;
            while( (line = input.readLine()) != null ) {
                writer.write( line );
                writer.newLine( );
            }
            input.close();
            writer.close();
            return new File( outPath.toString() ).getName();
        } catch( IOException ioe ) {
            ioe.printStackTrace( );
        } finally {
        	try { input.close();  } catch (IOException e1) {	}
			try { writer.close( );  } catch (IOException e) {	}

        }
        return "Failure>>" + outPath.toString( );
    }

	/**
	 * 依据ResultSet生成CommonSet
	 */
	private static final void fillCommonSet(CommonSet dataList, ResultSet rs, boolean useAlias ) throws SQLException {
		ResultSetMetaData md = rs.getMetaData( );
		int cols = md.getColumnCount( );
		dataList.setMetaData( md );

		if ( useAlias ) {
			while (rs.next()) {
				HashMap<String, Object> row = new HashMap<String, Object>();
				for (int j = 1; j <= cols; j++) {
					row.put(md.getColumnLabel(j), rs.getObject(j));
				}
				dataList.addRow(row);
			}
		} else {
			while (rs.next()) {
				HashMap<String, Object> row = new HashMap<String, Object>();
				for (int j = 1; j <= cols; j++) {
					row.put(md.getColumnName(j), rs.getObject(j));
				}
				dataList.addRow(row);
			}
		}
	}

    /**
	 * Close Database 
	 */
	public static final void close( Connection conn, Statement stmt, ResultSet rs ) {
		if ( rs != null ) {
			try { rs.close( ); }catch( SQLException sqle ) {  } 
		}
		if ( stmt != null ) {
			try { stmt.close( ); }catch( SQLException sqle ) {  }
		}
		if ( conn != null ) {
			try { conn.close( ); }catch( SQLException sqle ) {  }
		}
	}
	
}