/**
 * HttpParser.java
 *
 * Created on 20040401, 4:06 P.M
 */

package com.undao.utils;

/**
 * @author X.Stone
 *
 */
import java.io.*;
import java.util.*;

public class Files {
    
    /**
     * 获取指定目录下，将给定字符串做为文件名一部分的文件（不搜索子目录，不包括扩展名）
     */
    public static final Vector getAllFiles( String dirPath, String defaultName ) {
        File dir = new File( dirPath );
        File file = new File( defaultName );
        if ( ( ! dir.exists( ) ) || ( ! file.exists( ) ) ) { return null;  }
        
        Vector vFiles = new Vector( );
        String fileName = file.getName( );
        String priName = fileName.indexOf( "." ) == -1 ? fileName : fileName.substring( 0, fileName.indexOf( "." ) );
        File[ ] files = dir.listFiles( );
        
        for ( int i = 0; i < files.length; i ++ ) {
            fileName = files[ i ].getName( );
            if ( fileName.indexOf( priName ) == -1 ) { continue;  }
            vFiles.add( new File( dirPath + File.separator + fileName ) );
        }
        
        return vFiles;
    }
    
    /**
     * 递归删除目录中所有文件
     */
    public static boolean deleteDir(File dir) {           
    	if ( dir.isDirectory( ) ) {                 
    		String[] children = dir.list( );     //递归删除目录中的子目录下                 
    		for ( int i=0; i<children.length; i++ ) {       
    			boolean success = deleteDir( new File(dir, children[i]) );                     
    			if ( !success ) {                           
    				return false;                      
    			}                 
    		}           
    	}             
    	// 目录此时为空，可以删除           
    	return dir.delete( );       
    } 
    
}
