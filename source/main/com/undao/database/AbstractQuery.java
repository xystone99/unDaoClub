/**
 * Created At 2014-2-23 上午11:21:28
 */
package com.undao.database;

import javax.sql.DataSource;

/**
 * @author Administrator
 *
 */
public abstract class AbstractQuery extends AbstractDatabase {
		
	/**
	 * 生成SQL语句条件部分：使用等号
	 * @param colName	: 列名称
	 * @param eqValue	: 等于的值
	 * @param decimal	: 是否为数值型
	 * @return
	 */
	public final static String parseEqualsWhere( String colName, String eqValue, boolean decimal ) {
		if ( eqValue.equals(SQL_ALL) || eqValue.equals(SQL_EMPTY) ) {
			return SQL_EMPTY;
		}
		StringBuilder bufCondition = new StringBuilder();
		if ( decimal ) {
			bufCondition.append( "AND " ).append( colName ).append( "=" ).append( eqValue ).append( " " );;
		} else {
			bufCondition.append( "AND " ).append( colName ).append( "='" ).append( eqValue ).append( "' " );
		}
		return bufCondition.toString();
	}

	/**
	 * 生成SQL语句条件部分：使用LIKE关键字
	 * @param colName		: 列名称
	 * @param eqValue		: 模糊匹配的值
	 * @param likeMode		: 是否左匹配(%置于匹配值后面)
	 * @return
	 */
	public final static String parseLikeWhere( String colName, String eqValue, int likeMode  ) {
		if ( eqValue.equals(SQL_ALL) || eqValue.equals(SQL_EMPTY) ) {
			return SQL_EMPTY;
		}
		StringBuilder bufCondition = new StringBuilder();
		if ( likeMode == SQL_LIKE_LEFT ) {
			bufCondition.append( "AND " ).append( colName ).append( " LIKE '" ).append( eqValue ).append( "%' " );;
		} else if ( likeMode == SQL_LIKE_RIGHT ) {
			bufCondition.append( "AND " ).append( colName ).append( " LIKE '%" ).append( eqValue ).append( "' " );
		} else if ( likeMode == SQL_LIKE_BOTH ) {
			bufCondition.append( "AND " ).append( colName ).append( " LIKE '%" ).append( eqValue ).append( "%' " );
		}
		return bufCondition.toString();
	}
	
	/**
	 * 生成SQL语句条件部分：使用大于号
	 * @param colName		: 列名称 
	 * @param moreValue		: 大于的值
	 * @param decimal		: 是否为数值型
	 * @param containEquals	: 是否包含等于
	 * @return
	 */
	public final static String parseMoreWhere( String colName, String moreValue, boolean decimal, boolean containEquals ) {
		if ( moreValue.equals(SQL_EMPTY) || moreValue.equals(SQL_ALL) ) {
			return SQL_EMPTY;
		}
		StringBuilder bufCondition = new StringBuilder();
		if ( decimal ) {
			bufCondition.append( "AND " ).append( colName ).append( ">" ).append( containEquals ? "=" : "" ).append( moreValue ).append( " " );;
		} else {
			bufCondition.append( "AND " ).append( colName ).append( ">" ).append( containEquals ? "='" : "'" ).append( moreValue ).append( "' " );
		}
		return bufCondition.toString();
	}
	
	/**
	 * 生成SQL语句条件部分：使用小于号
	 * @param colName		: 列名称 
	 * @param lessValue		: 小于的值
	 * @param decimal		: 是否为数值型
	 * @param containEquals	: 是否包含等于
	 * @return
	 */
	public final static String parseLessWhere( String colName, String lessValue, boolean decimal, boolean containEquals ) {
		if ( lessValue.equals(SQL_ALL) || lessValue.equals(SQL_EMPTY) ) {
			return SQL_EMPTY;
		}
		StringBuilder bufCondition = new StringBuilder();
		if ( decimal ) {
			bufCondition.append( "AND " ).append( colName ).append( "<" ).append( containEquals ? "=" : "" ).append( lessValue ).append( " " );;
		} else {
			bufCondition.append( "AND " ).append( colName ).append( "<" ).append( containEquals ? "='" : "'" ).append( lessValue ).append( "' " );
		}
		return bufCondition.toString();
	}
	
	/**
	 * 生成SQL语句条件部分：范围查询
	 * @param colName		: 列名称
	 * @param leftEdge		: 范围域左侧的值
	 * @param rightEdge		: 范围域右侧的值
	 * @param decimal		: 是否为数值型
	 * @return
	 */
	public final static String parseRangeWhere( String colName, String leftEdge, String rightEdge, boolean decimal ) {
		StringBuilder bufCondition = new StringBuilder();
		if ( decimal ) {
			bufCondition.append( "AND " ).append( colName ).append( ">=" ).append( leftEdge ).append( " " );
			bufCondition.append( "AND " ).append( colName ).append( "<=" ).append( rightEdge ).append( " " );;
		} else {
			bufCondition.append( "AND " ).append( colName ).append( ">='" ).append( leftEdge ).append( "' " );
			bufCondition.append( "AND " ).append( colName ).append( "<='" ).append( rightEdge ).append( "' " );
		}
		return bufCondition.toString( );
		
	}
	
	/**
	 * 生成SQL语句条件部分：使用IN关键字
	 * @param colName	: 列名称
	 * @param inValue	: 匹配的列表
	 * @return
	 */
	public final static String parseInWhere( String colName, String inValue ) {
		if ( inValue.equals(SQL_ALL) || inValue.equals(SQL_EMPTY) ) {
			return SQL_EMPTY;
		}
		StringBuilder bufCondition = new StringBuilder();
		bufCondition.append( "AND " ).append( colName ).append( " IN(" ).append( inValue ).append( ") " );
		return bufCondition.toString( );
	}
	
	/**
	 * 生成SQL语句条件部分：范围查询,为日期补足时间部分
	 * @param colName		: 列名称
	 * @param leftEdge		: 范围域的左侧日期值
	 * @param rightEdge		: 范围域的右侧日期值
	 * @return
	 */
	public final static String parseDateTimeWhere( String colName, String leftEdge, String rightEdge ) {
		StringBuilder bufCondition = new StringBuilder();
		bufCondition.append( "AND " ).append( colName ).append( ">='" ).append( leftEdge ).append( " 00:00:01' " );
		bufCondition.append( "AND " ).append( colName ).append( "<='" ).append( rightEdge ).append( " 23:59:59' " );
		return bufCondition.toString( );
	}
	
	private int pageSize;								//每页记录数
	private int currentPage;							//当前页码
	private int baseIndex;								//当前页码的开始序号
	private int numColumns = 1;							//记录列数,默认为1列
	private boolean calcFoundRows = true;				//是否执行SQL_CALC_FOUND_ROWS

	protected CommonSet commonSet = null;				//保存查询结果
	protected String querySQL = null;					//查询SQL语句
	protected StringBuilder bufQueryConditions = null;	//查询条件
	protected String currentLanderID = null;			//当前登录者ID
	protected boolean isSystemAdministrator = false;	//是否根管理员
	protected String rangeCompanies = null;				//查询公司的范围

	/**
	 * Constructor
	 */
	public AbstractQuery( DataSource dataSource ) {
		super( dataSource );
		bufQueryConditions = new StringBuilder();
	}
	
	@Override
	public void setParameterValue(String paramTag, String paramValue) {
		if ( paramValue == null ) {
			return;
		}
		if ( !paramTag.equals(QP_CUR_PAGE) ) {
			bufQueryConditions.append( paramTag ).append( "=" ).append( paramValue ).append( "&" );
		}
		super.setParameterValue(paramTag, paramValue);
	}
	
	/**
	 * 设置当前登录者ID
	 */
	public void setCurrentLanderID( String landerID ) { this.currentLanderID = landerID; 	}
	
	/**
	 * 设置公司级别的查询范围
	 */
	public void setRangeCompanies( String rangeCompanies ) { this.rangeCompanies = rangeCompanies; 		}
	
	/**
	 * 设置是否根管理员
	 */
	public void setIfSystemAdministrator( boolean ifSystemAdministrator ) {	this.isSystemAdministrator = ifSystemAdministrator; 	}
	
	/**
	 * 设置每页记录数
	 */
	public int getPageSize( ) { return pageSize; 	}
	public void setPageSize( int pageSize ) { this.pageSize = pageSize; 	}
	
	/**
	 * 设置当前页码
	 */
	public int getCurrentPage( ) { return currentPage; 		}
	public void setCurrentPage( int currentPage ) { this.currentPage = currentPage; 	}
	
	/**
	 * 当前页码的开始序号 
	 */
	public int getBaseIndex( ) { return baseIndex; 		}
	public void setBaseIndex( int baseIndex ) { this.baseIndex = baseIndex; 		}
	
	/**
	 * 是否同时执行 SQL_CALC_FOUND_ROWS 查询
	 */
	public boolean isCalcFoundRows() { return calcFoundRows; 		}
	public void setCalcFoundRows(boolean calcFoundRows) { this.calcFoundRows = calcFoundRows; 		}
	
	/**
	 * 设置查询结果的列数
	 */
	public int getNumColumns() { return numColumns; 	}
	public void setNumColumns(int numColumns) { this.numColumns = numColumns; 	}

	/**
	 * 返回生成的查询语句,必须先执行executeQuery方法。 
	 */
	public String getQuerySQL( ) { return querySQL;		}

	/**
	 * 返回查询记录集，应先执行executeQuery()方法
	 */
	public CommonSet getQueryResult( ) { return commonSet; 		}
	
	/**
	 * 计算总页数,必须先执行executeQuery方法
	 */
	public int numOfPages( ) {
		if ( commonSet == null ) {
			return 0;
		}
		int m = commonSet.getTotalCount() % (getPageSize() * getNumColumns());
		int d = commonSet.getTotalCount() / (getPageSize() * getNumColumns());
		int numPages = m > 0 ? d + 1 : d;
		return numPages > 100 ? 100 : numPages;
	}

	/**
	 * 设置默认的查询参数
	 */
	public abstract void setDefaultParameterValue( );

	/**
	 * 执行查询
	 */
	public abstract void executeQuery( );

	/**
	 * 需要从Request中取得的参数列表
	 */
	public abstract String[] getParamSerial( );

}
