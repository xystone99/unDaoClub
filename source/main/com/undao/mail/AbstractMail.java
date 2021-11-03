/**
 * AbstractMail.java
 *
 * Created on 200603029, 4:04 P.M
 */

package com.undao.mail;

/**
 * @author X.Stone
 *
 */
import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public abstract class AbstractMail implements MailConstants {
    
    public final static int BUFFER = 2048;
    
    /**
     * Class Authenticator
     */
    protected class MyAuthenticator extends Authenticator {
        String username = null;
        String password = null;
        public MyAuthenticator( String username, String password ) {
            this.username = username;
            this.password = password;
        }
        protected PasswordAuthentication getPasswordAuthentication( ) {
            return new PasswordAuthentication( username, password );
        }
    }
    
    /**
     * methods
     */
    protected final static String getEncodeString( String original ) {
        try {
            return  MimeUtility.encodeText( original );
        } catch( UnsupportedEncodingException uee ) {
            System.out.println( "==AbstractMail>>>getEncodeString>>>with UnsupportedEncodingException" );
        }
        return original;
    }
    
    protected final static String getValidString( String original ) {
        if ( original == null ) { return "";   }
        String valid;
        boolean isStand;
        try {
            valid = MimeUtility.decodeText( original );
        } catch( UnsupportedEncodingException uee ) {
            return original;
        }
        isStand = original.indexOf( "=?GB2312" ) != -1 || original.indexOf( "=?GBK" ) != -1 || original.indexOf( "=?utf-8" ) != -1 || original.indexOf( "=?gb2312" ) != -1 ? true : false;
        if ( valid.length( ) == 0 ) {
            valid = "";
            isStand = true;
        }
        if ( isStand ) { return valid;  }
        try {
            byte[ ] temp = valid.getBytes( "ISO8859-1" );
            return new String( temp );
        } catch( UnsupportedEncodingException uee ) {
            System.out.println( "==AbstractMail>>>getValidString>>>with UnsupportedEncodingException" );
            return "";
        }
    }
    
    protected  InternetAddress createInternetAddress( String address ) {
        try {
            return new InternetAddress( address );
        } catch( AddressException ae ) {
            System.out.println( "==AbstractMail>>>createInternetAddress>>>with AddressException" );
        }
        return null;
    }
    
    protected InternetAddress[ ] createInternetAddressList( String addressList ) {
        String[ ] tmpStr = addressList.split( ";" );
        InternetAddress address[ ] = new InternetAddress[ tmpStr.length ];
        for ( int index = 0; index < tmpStr.length; index ++ ) {
            address[ index ] = createInternetAddress( tmpStr[ index ] );
        }
        return address;
    }
    
    protected void fetchHTMLPics( Part part, ArrayList attachFiles ) {
        try {
            if ( part.isMimeType( "text/plain" ) ||  part.isMimeType( "text/html" ) || part.isMimeType( "multipart/alternative" ) ) {
                return;
            }
            
            if ( part.isMimeType( "multipart/related" ) ) {
                Multipart mp = ( Multipart )part.getContent( );
                BodyPart current = mp.getBodyPart( 0 );
                for ( int i = 1; i < mp.getCount(); i ++ ) {
                    MimeBodyPart mbp = ( MimeBodyPart )mp.getBodyPart( i );
                    String fileID = mbp.getContentID( );
                    String fileName = getValidString( getFileNameOfPart( mbp ) );
                    attachFiles.add( makeTmpFile( mbp ) );
                }
            } else if ( part.isMimeType( "multipart/mixed" ) ) {
                Multipart mp = ( Multipart )part.getContent( );
                BodyPart current = mp.getBodyPart( 0 );
                fetchHTMLPics( current, attachFiles );
            }
        } catch( IOException ioe ) {
            System.out.println( "==AbstractMail>>>fetchHTMLPics>>>with IOException" );
        } catch( MessagingException me ) {
            System.out.println( "==AbstractMail>>>fetchHTMLPics>>>with MessagingException" );
        }
    }
    
    protected ArrayList fetchAttachFiles( Part part ) {
        try {
            if ( ! ( part.isMimeType( "multipart/mixed" ) ) ) { return null;   }
            Multipart mp = ( Multipart )part.getContent( );
            MimeBodyPart mbp = null;
            ArrayList fileList = new ArrayList( );
            for( int index = 1; index < mp.getCount( ); index ++ ) {
                mbp = ( MimeBodyPart )mp.getBodyPart( index );
                String fileID = mbp.getContentID( );
                String fileName = getValidString( getFileNameOfPart( mbp ) );
                fileList.add( makeTmpFile( mbp ) );
            }
            return fileList; 
        } catch( IOException ioe ) {
            System.out.println( "==AbstractMail>>>fetchAttachFiles>>>with IOException" );
        } catch( MessagingException me ) {
            System.out.println( "==AbstractMail>>>fetchAttachFiles>>>with MessagingException" );
        }
        return null;
    }
    
    protected  String  getFileNameOfPart( Part  body ) {
        boolean flag = true;
        if ( body == null ) { return  null;  }
        String[ ] cdis;
        try {
            cdis=body.getHeader( "Content-Disposition" );
            flag = ( cdis == null ) ? false : true;
            if ( ! flag ) {
                cdis = body.getHeader( "Content-Type" );
            }
        } catch( MessagingException me ){
            System.out.println( "==AbstractMail>>>getFileNameOfPart>>>with MessagingException" );
            return null;
        }
        if ( cdis == null || cdis[ 0 ] == null ) { return  null;   }
        
        if ( flag ) {
            int  pos = cdis[ 0 ].indexOf( "filename=" );
            if ( pos < 0 ) { return  null;    }
            if ( cdis[ 0 ].charAt( cdis[ 0 ].length( ) - 1) == '"' ) {
                return  cdis[ 0 ].substring( pos + 10, cdis[ 0 ].length( ) - 1 );
            }
            return  cdis[ 0 ].substring( pos + 9, cdis[ 0 ].length( ) );
        } else {
            int  pos = cdis[ 0 ].indexOf( "name=" );
            if ( pos < 0 ) {
                return  null;
            }
            if ( cdis[ 0 ].charAt( cdis[ 0 ].length( ) - 1) == '"' ) {
                return  cdis[ 0 ].substring( pos + 6, cdis[ 0 ].length( ) - 1 );
            }
            return  cdis[ 0 ].substring( pos + 5, cdis[ 0 ].length( ) );
        }
    }
    
    protected String makeTmpFile( MimeBodyPart part ) {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            int count = 0;
            byte data[ ] = new byte[ BUFFER ];
            File tmpFile = File.createTempFile( "Synchro", ".tmp" );
            in = new BufferedInputStream( part.getInputStream( ) );
            out = new BufferedOutputStream( new FileOutputStream( tmpFile ) );
            while ( ( count = in.read( data, 0, BUFFER ) ) != -1 ) {
                out.write( data, 0, count );
            }
            in.close( );
            out.close( );
            tmpFile.deleteOnExit( );
            return tmpFile.getAbsolutePath( );
        } catch( MessagingException me ) {
            System.out.println( "==AbstractMail>>>makeTmpFile>>>with MessagingException" );
        } catch( IOException ioe ) {
            System.out.println( "==AbstractMail>>>makeTmpFile>>>with IOException" );
        } finally {
            if ( in != null )  try { in.close( );  } catch( IOException e ) {      }
            if ( out != null )  try { out.close( );  } catch( IOException e ) {      }
        }
        return null;
    }
    
}