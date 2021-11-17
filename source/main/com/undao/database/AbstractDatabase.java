package com.undao.database;

import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.DataSource;

import com.undao.control.CtrlConstants;

public abstract class AbstractDatabase implements DatabaseConstants, CtrlConstants {
	
	protected boolean DEBUG = true;											//DEBUG开关
	private DataSource dataSource;
	private String cloudID = null;
	protected HashMap<String,String> parameterMap = new HashMap<String,String>();		//保存QueryParameters

	private final static HashMap<String,String> mapResult = new HashMap<String,String>();
	static {
		mapResult.put( R_SQL_CONSTRAINT_ERROR, "编号已经存在，请更换！" );
		mapResult.put( R_SQL_NOTFOUND, "编号不存在，请检查！" );
		mapResult.put( R_SQL_WARNING, "数据库报告警告信息，请联系系统管理员！" );
		mapResult.put( R_SQL_EXCEPTION, "数据库报告异常信息，请联系系统管理员！" );
	}

	/**
	 * Constructor 
	 */
	public AbstractDatabase( ) {  }

	public AbstractDatabase( DataSource ds ) {
		this.dataSource = ds;
	}
	
	public DataSource getDataSource( ) {
		return dataSource;
	}

	public void setDataSource( DataSource ds ) {
		this.dataSource = ds;
	}

	public String getCloudID( ) {
		return cloudID;
	}

	public void setCloudID( String cloudID ) {
		this.cloudID = cloudID;
	}

	public void setDEBUG( boolean ifDebug ) {
		this.DEBUG = ifDebug;
	}
	
	/**
	 * 设置QueryParameters
	 */
	public void setParameterValue( String paramTag, String paramValue ) {
		if ( paramValue != null ) {
			parameterMap.put( paramTag, paramValue.trim() );
		}
	}
	
	public String getParameterValue( String paramTag ) {
		if ( parameterMap.containsKey( paramTag ) ) {
			return parameterMap.get( paramTag );
		}
		return SQL_EMPTY;
	}
	
	
	/**
	 * 判断存储过程的返回结果 
	 */
	protected String getResultDisplay( String result ) {
		if ( mapResult.containsKey( result) ) {
			return mapResult.get( result );
		}
		return RD_UNKNOWN;
	} 
	
}
