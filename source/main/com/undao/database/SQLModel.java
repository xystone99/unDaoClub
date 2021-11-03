/**
 * SQLModel.java 
 *
 * Created at 2008-8-25, 下午01:27:57
 */
package com.undao.database;

/**
 * @author X.Stone
 *
 */
import java.io.Serializable;

public class SQLModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String sql = null;
	private String query_str = null;
	private String query_condition = null;
	private ParamLayer param_layer = null;
	
	/**
	 * Constructor
	 */
	public SQLModel( String sql, ParamLayer paramLayer ) {
		this.sql = sql;		
		this.param_layer = paramLayer;
	}
	
	public SQLModel( String sql, String queryString, String queryCondition ) {
		this.sql = sql;
		this.query_str = queryString;
		this.query_condition = queryCondition;
	}
	
	public SQLModel( String sql, String queryString, String queryCondition, ParamLayer paramLayer ) {
		this.sql = sql;
		this.query_str = queryString;
		this.query_condition = queryCondition;
		this.param_layer = paramLayer;
	}
	
	
	public String getSQL( ) {
		return sql;
	}
	
	public String getQueryString( ) {
		return query_str;
	}
	
	public String getQueryCondition( ) {
		return query_condition;
	}
	
	public void setQueryCondition( String queryCondition ) {
		this.query_condition = queryCondition;
	}
	
	public ParamLayer getParamLayer( ) {
		return param_layer;		
	}
	
	public boolean isPrepareQuery( ) {
		if ( param_layer == null ) {
			return false;
		}
		return param_layer.getSize() > 0;
	}

}