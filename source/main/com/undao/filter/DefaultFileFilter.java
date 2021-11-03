/**
 * SimpletFileFilter.java
 *
 * Created at 20031211, 10:06 A.M.
 */

package com.undao.filter;

/**
 * @author X.Stone
 *
 */
import java.io.*;
import javax.swing.filechooser.FileFilter;

public class DefaultFileFilter extends FileFilter {
    
    /**
     * Constructor
     */
    private String ext;    
    
    public DefaultFileFilter(String ext) {
        this.ext = ext;
    }
    
    public boolean accept( File file ) {
        if ( file.isDirectory( ) ) {
            return true;
        }
        String fileName = file.getName( );
        int index = fileName.lastIndexOf( "." );
        if ( index > 0 && index < fileName.length( ) ) {
            String extension = fileName.substring( index + 1).toLowerCase( );
            if ( extension.equals( ext ) ) {
                return true;
            }
        }
        return false;
    }
    public String getDescription( ) {        
        String extension = ext.toLowerCase( );
        if ( extension.equals( "txt" ) ) {
            return "TEXT File  ( *.txt )";
        } else if ( extension.equals( "java" ) ) {
            return "JAVA Source File  ( *.java )";
        } else if ( extension.equals( "properties" ) ) {
            return "JAVA Property File ( *.properties )";
        } else if ( extension.equals( "xls" ) ) {
            return "Microsoft Excel File( *.xls )";
        }
        return "";
    }
    
}