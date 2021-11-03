/**
 * 
 */
package com.undao.database;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

/**
 * @author Administrator
 *
 */
public abstract class AbstractProcedure extends AbstractDatabase {
	
	private String str_procedure = null;			//Procedure语句
	private int cnt_return_values = 0;				//Procedure的返回值个数
	private String[] arr_param_serial = null;		//QueryParameter中传递的变量名称

	private ParamLayer pLayer = new ParamLayer();	//执行Procedure所需要的参数
	private String[] arr_returnValues = null;		//Procedure的执行结果
	
	/**
	 * Constructor 
	 */
	public AbstractProcedure(DataSource dataSource ) {
		super( dataSource );
	}
	
	/**
	 * Procedure字符串
	 */
	public String getProcedureString( ) { return str_procedure;		}
	public void setProcedureString(String strProcedure) { str_procedure = strProcedure; 	}
	
	/**
	 * Procedure输入参数列表,以数组方式保存
	 */
	public String[] getParamSerial( ) { return arr_param_serial; 	}
	public void setParamSerial(String[] arrParamSerial) { arr_param_serial = arrParamSerial; 	}

	/**
	 * 设置存储过程的一个参数
	 */
	@Override
	public void setParameterValue(String paramTag, String paramValue) {
		pLayer.addParam( PARAM_STRING, paramValue );
		super.setParameterValue(paramTag, paramValue);
	}

	/**
	 * 以数组形式批量设置存储过程的参数
	 */
	public void setParameterValue( String[] arrParamValue ) {
		for( int j=0; j<arr_param_serial.length; j++ ) {
			pLayer.addParam( PARAM_STRING, arrParamValue[j] );
			super.setParameterValue( arr_param_serial[j], arrParamValue[j] );
		}
	}
	
	/**
	 * Procedure输出参数的个数 
	 */
	public int getCountReturnValues( ) { return cnt_return_values; 	}
	public void setCountReturnValues(int cntReturnValues) { cnt_return_values = cntReturnValues; 	}
	
	/**
	 * 执行Procedure
	 */
	public void executeCall( ) {
		arr_returnValues = DBUtils.executeCall( getDataSource(), str_procedure, pLayer, cnt_return_values );
	}
	
	/**
	 * Procedure执行结果, 保存在数组中 
	 */
	public String[] getReturnValues( ) { return arr_returnValues; 	}

	public String getResult( ) { return getReturnValues()[getCountReturnValues()-1]; 	}

	/**
	 * 返回执行的存储过程字符串
	 */
	public String getCallString( ) {
		return DBUtils.generateCallString( str_procedure, pLayer, getCountReturnValues() );
	}

	/**
	 * 返回到客户端的执行结果描述
	 */
	public abstract String getResultDisplay( );

	/**
	 * 存储过程执行是否成功
	 */
	public abstract boolean isSuccess( );
	
}