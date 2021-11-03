/**
 * SmtpHandler.java
 *
 * Created on 20060303, 9:10 A.M
 */

package com.undao.mail;

/**
 * @author X.Stone
 *
 */
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;

import com.sun.mail.smtp.*;

public class SmtpHandler extends AbstractMail {
    
    private final static boolean DEBUG = false;
    
    /**
     * Creates a new instance of DefaultSMTP
     */
    private URLName urlName = null;
    private boolean needAuthen = false;
    
    private String priority = null;
    private InternetAddress from = null;
    private InternetAddress[ ] to = null;
    private InternetAddress[ ] cc = null;
    private InternetAddress[ ] bcc = null;
    private String subject = null;
    private boolean isHTML = false;
    private String content = "";
    private String  charSet = CHARSET_GB;
    private ArrayList attachFiles = null;
    private ArrayList pics = null;
        
    public SmtpHandler( String protocol, String smtpServer, int port ) {
        super( );
        needAuthen = false;
        urlName = new URLName( protocol, smtpServer, port, null, null, null );
    }
    
    public SmtpHandler( String smtpServer, String name, String password ) {
        this( PROTOCOL_SMTP, smtpServer, SMTP_PORT, name, password );
    }
    
    public SmtpHandler( String protocol, String smtpServer, int port, String name, String password ) {
        super( );
        needAuthen = true;
        urlName = new URLName( protocol, smtpServer, port, null, name, password );
    }
    
    public void setPriority( String priority ) { this.priority = priority;    }
    public void setFROM( String fromAddress ) { this.from = createInternetAddress( fromAddress );    }
    public void setTO( String toAddress ) { this.to = createInternetAddressList( toAddress );     }
    public void setCC( String ccAddress ) { this.cc = createInternetAddressList( ccAddress );    }
    public void setBCC( String bccAddress ) { this.bcc = createInternetAddressList( bccAddress );      }
    public void setCharSet( String set ) { this.charSet = set;    }
    public void setSubject( String subject ) { this.subject = getEncodeString( subject );   }
    public void setHTML( ) { this.isHTML = true;     }
    public void setContent( String content ) { this.content = content;     }
    public void setHtmlPics( ArrayList picList ) { this.pics = picList;    }
    public void setAttachFiles( ArrayList attachList ) { this.attachFiles = attachList;    }
    
    public void addHtmlPic( String path ) {
        if ( pics == null ) { pics = new ArrayList( );    }
        pics.add( path );
    }
    
    public void addAttachFile( String path ) {
        if ( attachFiles == null ) { attachFiles = new ArrayList( );     }
        attachFiles.add( path );
    }
    
    public synchronized boolean sendMessage( String a_to, String subject, String a_content  ) {
    	this.subject = subject;
    	return sendMessage( a_to, a_content );
    }
    
    public synchronized boolean sendMessage( String a_to, String a_content  ) {
        Properties properties = System.getProperties( );
        properties.put( "mail.smtp.host", urlName.getHost( ) );
        if ( needAuthen ) {
            properties.put( "mail.smtp.auth", "true" );
        }
        
        SMTPTransport transport = null;
        try {
        	Session session = Session.getInstance( properties, needAuthen ? new MyAuthenticator( urlName.getUsername( ), urlName.getPassword( ) ) : null );
            session.setDebug( DEBUG );
            
        	MimeMessage message = new MimeMessage( session );
        	message.setFrom( from );
        	message.setRecipients( Message.RecipientType.TO, createInternetAddressList(a_to) );            
        	message.setRecipients( Message.RecipientType.BCC, bcc );
        	message.setSubject( subject );
        	message.setSentDate( new Date( ) );
                
        	MimeMultipart mp = new MimeMultipart( );
        	MimeBodyPart mbp = new MimeBodyPart( );
        	mbp.setContent( a_content, "text/html;charset=" + charSet );            
        	mp.addBodyPart( mbp );            
        	message.setContent( mp );
                            
            transport = new SMTPTransport( session, urlName );
            transport.connect( );
            transport.send( message );

            return true;
        } catch( NoSuchProviderException nspe ) {
        	if(DEBUG) System.out.println( "SmtpHandler>>>SendMessage>>>with NoSuchProviderException" );
        } catch( MessagingException me ) {
        	if(DEBUG) System.out.println( "SmtpHandler>>>SendMessage>>>with MessagingException" );
        } catch( SecurityException se ) {
        	if(DEBUG) System.out.println( "SmtpHandler>>>SendMessage >>>with SecurityException" );
        } catch( Exception e ) {
        	if(DEBUG) System.out.println( "SmtpHandler>>>SendMessage>>>with UnknowException" );
        } finally {
            try { if ( transport != null )  transport.close( );  } catch( MessagingException me1 ) {      }
        }
        return false;
    }
       
    /**
     * private methods
     */
    private MimeMessage createMimeMessage( Session session ) {
        MimeMessage message = new MimeMessage( session );
        try {
            message.setFrom( from );
            message.setRecipients( Message.RecipientType.TO, to );            
            message.setSubject( subject );
            message.setSentDate( new Date( ) );
            
            MimeMultipart mp = new MimeMultipart( );
            MimeBodyPart mbp = new MimeBodyPart( );
            mbp.setContent( content, "text/html;charset=" + charSet );            
            mp.addBodyPart( mbp );            
            message.setContent( mp );
            return message;
        } catch( MessagingException me ) {
            if(DEBUG) System.out.println( "SmtpHandler>>>createMimeMessage>>>with MessagingException" );
        } catch( Exception e ) {
        	if(DEBUG) System.out.println( "SmtpHandler>>>createMimeMessage>>>with Exception" );
        }
        return null;
    }
    
    /**
     * main methods
     */
    public static void main( String[ ] args ) {
        JFrame frame = new JFrame( "SMTP Test" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setBounds( 0, 0, 800, 600 );
        frame.setVisible( true );
        SmtpHandler smtp = new SmtpHandler( "smtp.126.com", "xmsclient@126.com", "123456" );
        smtp.setFROM( "xmsclient@126.com" );
        smtp.setTO( "xmsinfo@126.com" );
        smtp.setSubject( "Hello, world" );
        smtp.setContent( "Hello, Stone. I am from iguchi" );
        smtp.setCharSet( CHARSET_GB );
        StringBuilder buf = new StringBuilder( );
        buf.append( "公司名称：" ).append( "A" ).append( "\r\n" );
        buf.append( "联系地址：" ).append( "B" ).append( "\r\n" );
        buf.append( "邮政编码：" ).append( "C" ).append( "\r\n" );
        buf.append( "联系电话：" ).append( "D" ).append( "\r\n" );
        buf.append( "客户服务：" ).append( "支持" ).append( "\r\n" );
        
        System.out.println( smtp.sendMessage( "xmsinfo@126.com", buf.toString() ) ? "Send Message success" : "Send Message faile" );
    }
    
}