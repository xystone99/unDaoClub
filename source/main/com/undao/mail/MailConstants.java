/**
 * MailConstants.java
 *
 * Created on 20060302, 2:23 P.M
 */

package com.undao.mail;

/**
 * @author X.Stone
 *
 */
public interface MailConstants {
        
    static final String PROTOCOL_POP3 = "pop3";
    static final String PROTOCOL_SMTP = "smtp";
    
    static final int POP3_PORT = 110;
    static final int SMTP_PORT = 25;
    
    static final String POP3_HOST = "pop3.126.com";
    static final String SMTP_HOST = "smtp.126.com";
            
    static final String POP3_IN_BOX = "INBOX";
    
    static final String FMT_MAIL_TEXT = "text/plain";
    static final String FMT_MAIL_HTML = "text/html";
    static final String FMT_MAIL_ALTERNATIVE = "multipart/alternative";
    static final String FMT_MAIL_RELEATED = "multipart/related";
    static final String FMT_MAIL_MIXED = "multipart/mixed";
    
    static final String CHARSET_JP = "shift_jis";
    static final String CHARSET_GB = "GB2312";                    
    
}