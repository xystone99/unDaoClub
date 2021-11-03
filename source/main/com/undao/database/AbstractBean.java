/**
 * 
 */
package com.undao.database;

import javax.sql.DataSource;

/**
 * @author Administrator
 *
 */
public abstract class AbstractBean extends AbstractDatabase {
	
	private int beanType = 0;							//操作类型
	private String str_procedure = null;				//Procedure语句
	private String[] arr_param_serial = null;			//QueryParameter中传递的变量名称
	private int cnt_return_values = 0;					//Procedure的返回值个数

	private ParamLayer pLayer = new ParamLayer();		//执行Procedure所需要的参数
	private String detail_sql = null;					//PrepareSQL
	private CommonSet commonSet = null;					//保存查询结果
	private String[] arr_returnValues = null;			//Procedure的执行结果
	
	/**
	 * Constructor 
	 */
	public AbstractBean( DataSource dataSource, int beanType ) {
		super( dataSource );
		this.beanType = beanType;
	}
	
	/**
	 * Bean的类型：参考BTYPE_常量 
	 */
	public int getBeanType( ) {
		return beanType;
	}
	
	/**
	 * Procedure字符串 
	 */
	public String getProcedureString( ) { return str_procedure; 	}
	public void setProcedureString(String strProcedure) { str_procedure = strProcedure; 	}

	/**
	 * 针对BTYPE_QUERY_DETAIL设置查询语句
	 */
	public void setDetailSQL( String detailSQL ) { this.detail_sql = detailSQL; 	}
	
	public String getDetailSQL( ) {		//将PrepareSQL转化为完整的SQL
		StringBuilder bufSQL = new StringBuilder(detail_sql);
		for ( int j=0; j<pLayer.getSize(); j++ ) {
			int p = bufSQL.indexOf( "?" );
			bufSQL.replace(p, p+1, "'" + pLayer.getParam(j) + "'" );
		}
		return bufSQL.toString();
	}
	
	/**
	 * Procedure输入参数列表,保存在数组中
	 */
	public String[] getParamSerial( ) { return arr_param_serial; 	}
	public void setParamSerial(String[] arrParamSerial) { arr_param_serial = arrParamSerial; 	}
	
	@Override
	public void setParameterValue(String paramTag, String paramValue) {
		pLayer.addParam( PARAM_STRING, paramValue );
		super.setParameterValue(paramTag, paramValue);
	}

	/**
	 * Procedure输出参数的个数
	 */
	public int getCountReturnValues( ) { return cnt_return_values; 		}
	public void setCountReturnValues(int cntReturnValues) { cnt_return_values = cntReturnValues; 	}

	/**
	 * 执行查询：BTYPE_QUERY_DETAIL
	 */
	public void executeQuery( ) {
		commonSet = DBUtils.prepareQuery( getDataSource(), detail_sql, pLayer );
	}

	/**
	 * 返回查询结果：BTYPE_QUERY_DETAIL
	 */
	public CommonSet getQueryResult( ) { return commonSet; 		}


	/**
	 * 执行Procedure (前提条件：beanType 不是 BTYPE_DETAIL)
	 */
	public void executeCall( ) {
		arr_returnValues = DBUtils.executeCall( getDataSource(), str_procedure, pLayer, cnt_return_values );
	}
	
	/**
	 * 返回Procedure的执行结果 (前提条件：beanType 不是 BTYPE_DETAIL)
	 */
	public String[] getReturnValues( ) { return arr_returnValues; 	}
	
	public String getResult( ) { return getReturnValues()[getCountReturnValues()-1]; 	}
	
	public String getNewID( ) { return getReturnValues()[getCountReturnValues()-2]; 	}

	/**
	 * 返回执行的存储过程字符串
	 */
	public String getCallString() {
		return DBUtils.generateCallString( getProcedureString(), pLayer, getCountReturnValues() );
	}

	/**
	 * 返回到客户端的执行结果描述
	 */
	public abstract String getResultDisplay( );

	
}