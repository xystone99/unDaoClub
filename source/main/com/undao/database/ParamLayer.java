/**
 * ParamLayer.java
 *
 * Created on 20080312, 10:25 A.M
 */
package com.undao.database;

/**
 * @author X.Stone
 *
 */
import java.io.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.math.BigDecimal;

public class ParamLayer implements Serializable, DatabaseConstants {
	
	private static final long serialVersionUID = 1L;

	class ParamHolder implements Serializable {
		private static final long serialVersionUID = -7109988458520739470L;
		private int param_k;
		private Object param = null;
		public ParamHolder( int paramKind, Object object ) {
			this.param_k = paramKind;
			this.param = object;
		}
		public int getType( ) { return param_k;    }
		public Object getObject( ) { return param;    }
	}
	
	/**
	 * Constructor 
	 */
	private ArrayList<ParamHolder> param_list = new ArrayList<ParamHolder>( );
	
	public ParamLayer( ) {
		super( );
	}
	
	public void addParam( int paramKind, String param ) {
		Object object = param;
		if ( paramKind == PARAM_SHORT ) {
			object = Short.valueOf( param );	
		} else if ( paramKind == PARAM_INTEGER ) {
			object = Integer.valueOf( param );
		} else if ( paramKind == PARAM_DECIMAL ) {
			object = new BigDecimal( param );
		}
		param_list.add( new ParamHolder(paramKind, object) );
	}
	
	public void setParam( int index, int paramKind, String param ) {
		Object object = param;
		if ( paramKind == PARAM_SHORT ) {
			object = Short.valueOf( param );	
		} else if ( paramKind == PARAM_INTEGER ) {
			object = Integer.valueOf( param );
		} else if ( paramKind == PARAM_DECIMAL ) {
			object = new BigDecimal( param );
		}
		param_list.set(index, new ParamHolder(paramKind, object) );
	}
	
	public int getSize( ) {
		return param_list.size( );		
	}
	
	public int getParamType( int index ) {
		return param_list.get(index).getType( );
	}
	
	public Object getParam( int index ) {
		return param_list.get(index).getObject( );
	}
	
	public void clearParams( ) {
		param_list.clear( );		
	}
	
}