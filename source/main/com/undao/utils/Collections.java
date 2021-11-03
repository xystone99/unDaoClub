/*
 * Collections.java
 *
 * Created on 2004年4月1日, 下午4:06
 */

package com.undao.utils;

/**
 * @author X.Stone
 *
 */
import java.io.*;
import java.util.*;

public class Collections {
    
	/**
	 * 得到数组的下标值 
	 */
	public final static int indexOfArray( Object[] arrObj, Object obj ) {
		for ( int j=0; j<arrObj.length; j++ ) {
			if ( obj.equals(arrObj[j]) ) {
				return j;
			}
		}
		return -1;
	}
	
	/**
	 * 相应网站字典映射为本地字典 
	 * 返回数组0对应的字典值
	 */
	public static String refPrivateDictionary( String[][] arrDictionary, String localTag_1 ) {
		for (int j=0; j<arrDictionary[1].length; j++) {
			if (arrDictionary[1][j].equals( localTag_1 ) ) {
				return arrDictionary[0][j];
			}
		}
		return arrDictionary[0][arrDictionary[0].length/2];
	}
	
	/**
	 * 本地字典映射为相应网站字典
	 * 返回数组1对应的字典值 
	 */
	public static String refLocalDictionary( String[][] arrDictionary, String privateTag_0 ) {
		for (int j=0; j<arrDictionary[0].length; j++) {
			if (arrDictionary[0][j].equals( privateTag_0 ) ) {
				return arrDictionary[1][j];
			}
		}
		return arrDictionary[1][arrDictionary[1].length/2];
	}
	
    /**
     * About Configuration file
     */     
    public final static Properties loadProperties( File file ) {        
        FileInputStream inputStream = null;
        Properties properties = new Properties( );
        try {            
            inputStream = new FileInputStream( file );
            properties.load( inputStream );
        } catch( FileNotFoundException fnfe ) {
            System.out.println( "Collections.loadProperties(File)>> " + fnfe.getMessage() );
        } catch( IOException ioe ) {
            System.out.println( "Collections.loadProperties(File)>> " + ioe.getMessage() );
        } finally {
            try { if ( inputStream != null )  inputStream.close( );   } catch( IOException ioe1 ) {              }
        }
        return properties;
    }
    
    public final static Properties loadProperties( String path ) {        
        FileInputStream inputStream = null;
        Properties properties = new Properties( );
        try {            
            inputStream = new FileInputStream( path );
            properties.load( inputStream );
        } catch( FileNotFoundException fnfe ) {
        	System.out.println( "Collections.loadProperties(String)>> " + fnfe.getMessage() );
        } catch( IOException ioe ) {
        	System.out.println( "Collections.loadProperties(String)>> " + ioe.getMessage() );
        } finally {
            try { if ( inputStream != null )  inputStream.close( );   } catch( IOException ioe1 ) {              }
        }
        return properties;
    }
    
    public final static void savePropertiesToFile( Properties properties, String path ) {        
        FileOutputStream outputStream = null;
        try {            
            outputStream = new FileOutputStream( path );
            if ( properties == null ) {
            	properties.store( outputStream, null );
            }
        } catch( FileNotFoundException fnfe ) {
        	System.out.println( "Collections.savePropertiesToFile(Properties,String)>> " + fnfe.getMessage() );
        } catch( IOException ioe ) {
        	System.out.println( "Collections.savePropertiesToFile(Properties,String)>> " + ioe.getMessage() );
        } finally {
            try { if ( outputStream != null )  outputStream.close( );  }  catch(  IOException ioe1 ) {            }
        }
    }

}
