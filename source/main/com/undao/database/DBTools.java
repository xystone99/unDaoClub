/**
 * Created At 2014-3-6 下午08:33:13
 */
package com.undao.database;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * Title: 数据库工具类
 * 
 * Description: 将大部分的数据库操作放入这个类中, 包括数据库连接的建立, 自动释放等.
 * @author beansoft 日期: 2004年04月
 * @version 2.0
 */
public class DBTools implements DatabaseConstants {

	private Connection connection;

	/**
	 * All database resources created by this class, should be free after all operations, 
	 * holds: ResultSet, Statement, PreparedStatement, etc.
	 */
	private ArrayList<Object> resourcesList = new ArrayList<Object>(5);

	public DBTools(Connection connection) {
		this.connection = connection;
	}
	
	public DBTools(String dbName, String userName, String userPwd) {
		try {
			String jdbc_url = "jdbc:mysql://localhost:3306/" + dbName;
			Class.forName("com.mysql.jdbc.Driver");
			this.connection = DriverManager.getConnection(jdbc_url, userName, userPwd);
		} catch (ClassNotFoundException cnfe) {
			System.out.println( "DBTools>>" + cnfe.getMessage() );
		} catch (SQLException sqle) {
			System.out.println( "DBTools>>" + sqle.getMessage() );
		}
	}
	
	/**
	 * 返回当前数据库连接.
	 */
	public Connection getConnection() {
		return connection;

	}
	
	/**
	 * 连接新的数据库对象到这个工具类, 首先尝试关闭老连接.
	 */

	public void setConnection(Connection connection) {
		if ( this.connection != null) {			
			try {
				getConnection().close();
			} catch (SQLException sqle) {
				// TODO Auto-generated catch block
			}
		}
		this.connection = connection;
	}
	
	/**
	 * Return the resources list of this class.
	 * @return ArrayList the resources list
	 */
	public ArrayList<Object> getResourcesList() {
		return resourcesList;
	}

	/** 
	 * 关闭数据库连接并释放所有数据库资源
	 */
	public void close() {
		closeAllResources();
		close(getConnection());
	}

	/**
	 * Close given connection.
	 * @param connection
	 */

	public static void close(Connection connection) {
		try {
			connection.close();
		} catch (SQLException sqle) {
			System.err.println("Exception when close a connection: " + sqle.getMessage());
		}
	}

	/**
	 * Close all resources created by this class.
	 */
	public void closeAllResources() {
		for (int i = 0; i < this.getResourcesList().size(); i++) {
			closeJDBCResource(getResourcesList().get(i));
		}
	}

	/**
	 * Close a jdbc resource, such as ResultSet, Statement, Connection.... 
	 * All these objects must have a method signature is void close().
	 * @param resource
	 */

	public void closeJDBCResource(Object resource) {
		Class<? extends Object> clazz = resource.getClass();
		Method method;
		try {
			method = clazz.getMethod("close", new Class[0]);
			method.invoke(resource, new Object[]{});
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
		}
	}

	/**
	 * 执行 SELECT 等 SQL 语句并返回结果集.
	 * @param sql 需要发送到数据库 SQL 语句
	 * @return a ResultSet</code> object that contains the data produced by the given query; never null</code>
	 */

	public ResultSet executeQuery(String sql) {
		try {
			Statement statement = getStatement();
			ResultSet rs = statement.executeQuery(sql);
			this.getResourcesList().add(rs);
			this.getResourcesList().add(statement);	// BUG fix at 2006-04-29 by BeanSoft, added this to res list

			// MySql 数据库要求必需关闭 statement 对象, 否则释放不掉资源  - 此观点错误, 因为关闭此对象后有时数据无法读出
			// statement.close();
			return rs;
		} catch (Exception ex) {
			System.out.println("Error in executeQuery(\"" + sql + "\"):" + ex);
			return null;
		}
	}

	/**
	 * Executes the given SQL statement, which may be an INSERT,UPDATE, 
	 * or DELETE statement or an SQL statement that returns nothing, such as an SQL DDL statement. 
	 * 
	 * @param sql an SQL INSERT,UPDATE or DELETE statement or an SQL statement that returns nothing
	 * @return either the row count for INSERT,UPDATE or DELETE statements, or 0 for SQL statements that return nothing
	 */

	public int executeUpdate(String sql) {
		try {
			Statement statement = getStatement();
			return statement.executeUpdate(sql);
			// MySql 数据库要求必需关闭 statement 对象, 否则释放不掉资源 - 此观点错误, 因为关闭此对象后有时数据无法读出
			// statement.close();
		} catch (Exception ex) {
			System.out.println("Error in executeUpdate(): " + sql + " " + ex);
		}
		return -1;
	}

	/**
	 * 返回记录总数, 使用方法: getAllCount("SELECT count(ID) from tableName") 2004-06-09
	 * 可滚动的 Statement 不能执行 SELECT MAX(ID) 之类的查询语句(SQLServer 2000)
	 * 
	 * @param sql 需要执行的 SQL
	 * @return 记录总数
	 */
	public int getAllCount(String sql) {
		try {
			Statement statement = getConnection().createStatement();
			this.getResourcesList().add(statement);
			ResultSet rs = statement.executeQuery(sql);
			rs.next();
			int cnt = rs.getInt(1);
			rs.close();
			try {
				statement.close();
				this.getResourcesList().remove(statement);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return cnt;

		} catch (Exception ex) {
			System.out.println("Exception in DatabaseUtil.getAllCount(" + sql + "):" + ex);
			return 0;
		}
	}

	/**
	 * Create a common statement from the database connection and return it.
	 * @return Statement
	 */

	public Statement getStatement() {
		// 首先尝试获取可滚动的 Statement, 然后才是普通 Statement
		Statement updatableStmt = getUpdatableStatement();
		if (updatableStmt != null) {
			return updatableStmt;
		}
		try {
			Statement statement = getConnection().createStatement();
			this.getResourcesList().add(statement);
			return statement;
		} catch (Exception ex) {
			System.out.println("Error in getStatement(): " + ex);
		}
		return null;
	}

	/**
	 * Create a updatable and scrollable statement from the database connection and return it.
	 * @return Statement
	 */

	public Statement getUpdatableStatement() {
		try {
			Statement statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			this.getResourcesList().add(statement);
			return statement;
		} catch (Exception ex) {
			System.out.println("Error in getUpdatableStatement(): " + ex);
		}
		return null;
	}

	/**
	 * Create a prepared statement and return it.
	 * @param sql String SQL to prepare
	 * @throws SQLException any database exception
	 * @return PreparedStatement the prepared statement
	 */

	public PreparedStatement getPreparedStatement(String sql) throws SQLException {
		try {
			PreparedStatement preparedStatement = getConnection().prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			this.getResourcesList().add(preparedStatement);
			return preparedStatement;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public void prepareBatch( String preSQL, List<ParamLayer> list ) {
		PreparedStatement pstmt = null;    	
    	try {
    		pstmt = connection.prepareStatement( preSQL );
    		this.getResourcesList().add( pstmt );
    		for ( int index=0; index<list.size(); index++ ) {
    			ParamLayer pLayer = list.get(index);
    			for ( int j=0; j<pLayer.getSize(); j++ ) {
    				int param_k = pLayer.getParamType( j );
    				if ( param_k == PARAM_SHORT ) {
        				pstmt.setShort( j+1, ((Short)pLayer.getParam(j)).shortValue() );	
        			} else if ( param_k == PARAM_INTEGER ) {
        				pstmt.setInt( j+1, ((Integer)pLayer.getParam(j)).intValue() );
        			} else if ( param_k == PARAM_STRING ) {
        				pstmt.setString( j+1, (String)pLayer.getParam(j) );
        			} else if ( param_k == PARAM_DATE ) {
        				pstmt.setString( j+1, (String)pLayer.getParam(j) );
        			} else if ( param_k == PARAM_DECIMAL ) {
        				pstmt.setBigDecimal( j+1, (BigDecimal)pLayer.getParam(j) ); 
        			}
    			}
    			pstmt.addBatch( );
    		}  		
    		pstmt.executeBatch( );
    	} catch(SQLException sqle) {   
			System.out.println( "DBTools>>prepareUpdate(" + preSQL + ") Error:" + sqle.getMessage() );   
		} finally {
			this.closeAllResources( );
		}
	}

	

	/**
	 * Fetch a string from the result set, and avoid return a null string.
	 * @param rs the ResultSet
	 * @param columnName the column name
	 * @return the fetched string
	 */

	public static String getString(ResultSet rs, String columnName) {
		try {
			String result = rs.getString(columnName);
			if (result == null) {
				result = "";
			}
			return result;
		} catch (Exception ex) {
		}
		return "";
	}

	/**
	 * Get all the column labels
	 * @param resultSet ResultSet
	 * @return String[]
	 */

	public static String[] getColumns(ResultSet resultSet) {
		if (resultSet == null) {
			return null;
		}
		try {
			ResultSetMetaData metaData = resultSet.getMetaData();
			int numberOfColumns = metaData.getColumnCount();
			if (numberOfColumns <= 0) {
				return null;
			}
			String[] columns = new String[numberOfColumns];
			// System.err.println("numberOfColumns=" + numberOfColumns);
			// Get the column names
			for (int column = 0; column < numberOfColumns; column++) {
				// System.out.print(metaData.getColumnLabel(column + 1) + "\t");
				columns[column] = metaData.getColumnName(column + 1);
			}
			return columns;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Get the row count of the result set.
	 * @param resultset ResultSet
	 * @throws SQLException if a database access error occurs or the result set type is TYPE_FORWARD_ONLY
	 * @return int the row count
	 * @since 1.2
	 */

	public static int getRowCount(ResultSet resultset) throws SQLException {
		int row = 0;
		try {
			int currentRow = resultset.getRow(); // Remember old row position
			resultset.last();
			row = resultset.getRow();
			if (currentRow > 0) {
				resultset.absolute(row);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return row;
	}

	/**
	 * Get the column count of the result set.
	 * @param resultSet ResultSet
	 * @return int the column count
	 */
	public static int getColumnCount(ResultSet resultSet) {
		if (resultSet == null) {
			return 0;
		}
		try {
			ResultSetMetaData metaData = resultSet.getMetaData();
			int numberOfColumns = metaData.getColumnCount();
			return numberOfColumns;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}


	/** 
	 * Test this class. 
	 */
	public static void main(String[] args) throws Exception {
		DBTools util = new DBTools("OneBestDB","root","abcd1234");
		
	}

}
