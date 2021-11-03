/**
 * DaemonFetcher.java 
 *
 * Created at 2008-7-23, 上午11:12:45
 */
package yundeploy.management;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.undao.control.AbstractDaemon;
import com.undao.database.CommonSet;
import com.undao.database.DBUtils;
import com.undao.utils.Files;

import static xms.XmsInitial.*;

/**
 * @author X.Stone
 *
 */
public class RemoteAgent extends AbstractDaemon {
		
	private static final long serialVersionUID = 1L;
	
	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) {
    	String actionTag = request.getParameter("Action");
    	String result = "Success";
    	
    	if ( !isSystemAdministrator( request) ) {
    		StringBuilder bufResult = new StringBuilder();
    		bufResult.append( "<script language=\"javascript\">" );
    		bufResult.append( "alert(\"无效请求，面墙自省！！！\");" );
    		bufResult.append( "</script>" );
    		result = bufResult.toString( );
    	} else if ( actionTag.equals( "ClearTemporary" ) ) {
    		result = doClearTemporary( request );
    	} else if ( actionTag.equals( "DeleteFile" ) ) {
    		result = doDeleteFile( request );
    	} else if ( actionTag.equals( "DropTable" ) ) {
    		result = doDropTable( request );
    	} else if ( actionTag.equals( "Backup" ) ) {
    		String db_path = doBackup( request );
    		StringBuilder bufResult = new StringBuilder();
    		bufResult.append( "<a href=\"/" ).append( db_path ).append( "\">Download</a>" );
    		result = bufResult.toString();
    	} else if ( actionTag.equals( "AccountInfo" ) ) {
    		result = doQueryAccountInfo( request );
    	} else if ( actionTag.equals( "Download" ) ) {
    		result = doDownloadFile( request );
    	}
    	
    	try {
    		//设置编码，这句最重要
    		response.setContentType("text/html;charset=UTF-8");    
    		response.setCharacterEncoding("UTF-8"); 
    		
    		//设置浏览器不要缓存  
    		response.setHeader("Pragma", "No-cache");     
    		response.setHeader("Cache-Control", "no-cache");    
    		response.setDateHeader("Expires", 0);

    		response.getWriter().print( result );
    	} catch( IOException ioe ) {
    		System.out.println( "EstateFetcher IOException:" + ioe.getMessage() );
    	}
    }       
       
    private String doDownloadFile( HttpServletRequest request ) {
    	String strURL = request.getParameter( "Url" );
		String destDir = request.getParameter( "Dest" );
    	File destFile = null;
		String fileName = strURL.substring( strURL.lastIndexOf("/")+1 );
		try {
			URL url = new URL( strURL );  
	        URLConnection con = url.openConnection( );  
	        con.setConnectTimeout( 5*1000 );  //设置请求超时为5s 
	        InputStream is = con.getInputStream( );  
	        
	        byte[] bs = new byte[1024];  // 1K的数据缓冲 
	        int len;  // 读取到的数据长度
	        destFile = new File(getHomeDir()+destDir+fileName );
	        OutputStream os = new FileOutputStream( destFile );  
	        while ((len = is.read(bs)) != -1) {  
	        	os.write(bs, 0, len);  
	        }  
	        os.close();  
	        is.close(); 
		} catch( IOException ioe) {
			return ioe.toString( );
		}
		return destFile.getAbsolutePath( );
    }
    
    private String doClearTemporary( HttpServletRequest request ) {
    	File temp_dir = new File( getTemporaryDir() );
    	Files.deleteDir( temp_dir );
    	if ( !temp_dir.exists() ) {
    		temp_dir.mkdir( );
    	}
    	return "临时目录已清除";
    }
    
    private String doDeleteFile( HttpServletRequest request ) {
    	File temp_file = new File( getHomeDir() + File.separator + request.getParameter( "FilePath" ) );
    	if ( temp_file.exists() ) {
    		temp_file.delete( );
    		return temp_file.getAbsolutePath( );
    	}
    	return "Not Exist";
    }
    
    private String doDropTable( HttpServletRequest request ) {
    	return null;
    }
    
    private String doBackup( HttpServletRequest request ) {
    	String mysql_dir = "E:\\MySQL\\MySQL_5.5.28\\bin";
    	String xInfo_dir = getHomeDir();
    	String db_UserName = "root";
    	String db_UserPwd = "abcd1234";
    	String db_Name = "Info921WEBDB";
    	
    	return DBUtils.backup(mysql_dir, xInfo_dir, db_UserName, db_UserPwd, db_Name );
    }
    
    private String doQueryAccountInfo( HttpServletRequest request ) {
    	StringBuilder buf = new StringBuilder( );
    	CommonSet dataBinding = DBUtils.executeQuery( getDataSource(), "SELECT broker, web_flg,user_name,user_pwd_original,is_bound,cnt_post,input_date,last_update FROM tbl_account_binding ORDER BY broker ASC", false );
    	for(int j = 0; j <dataBinding.getRowCount(); j++ ) {
    		buf.append( dataBinding.getValue(j,"broker") ).append( "===" );
    		buf.append( dataBinding.getValue(j,"web_flg") ).append( "===" );
    		buf.append( dataBinding.getValue(j,"user_name") ).append( "===" );
    		buf.append( dataBinding.getValue(j,"user_pwd_original") ).append( "===" );
    		buf.append( dataBinding.getValue(j,"is_bound") ).append( "===" );
    		buf.append( dataBinding.getValue(j,"cnt_post") ).append( "===" );
    		buf.append( dataBinding.getValue(j,"input_date") ).append( "===" );
    		buf.append( dataBinding.getValue(j,"last_update") );
    		buf.append( "<br/>" );
    	}
    	
    	buf.append( "<br/>" ).append( "<br/>" ).append( "<br/>" );
    	CommonSet dataPost = DBUtils.executeQuery( getDataSource(), "SELECT broker,web_flg,url,input_date FROM trn_post_record ORDER BY broker ASC, web_flg, input_date ASC", false );
    	for(int j = 0; j <dataPost.getRowCount(); j++ ) {
    		buf.append( dataPost.getValue(j,"broker") ).append( "===" );
    		buf.append( dataPost.getValue(j,"web_flg") ).append( "===" );
    		buf.append( dataPost.getValue(j,"url") ).append( "===" );
    		buf.append( dataPost.getValue(j,"input_date") );
    		buf.append( "<br/>" );
    	}
    	
    	return buf.toString();
    }
    
}