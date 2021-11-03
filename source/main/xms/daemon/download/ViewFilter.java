package xms.daemon.download;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Servlet implementation class TestServlet
*/
@WebServlet(asyncSupported = false, value = "/viewfilter", loadOnStartup = 9 )
public class ViewFilter extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	public ViewFilter( ) {
       //System.out.println( "endtering servlet..." );
    }
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("entering doPost...");
		response.setCharacterEncoding("ISO-8859-1");      
		request.setCharacterEncoding("ISO-8859-1");      
		
		OutputStream os = null;      
		FileInputStream fis = null;  
		try {    
			String file = "E:/AA.pptx";
			System.out.println("Check if got file:"+file);    
			System.out.println("File path is:"+file);      
			os = response.getOutputStream();	//video output stream
//			response.setContentType("video/x-msvideo");
//			response.setContentType("APPLICATION/OCTET-STREAM");
//			response.setHeader("Content-Type","video/x-msvideo");
//			response.setHeader("content-disposition", "attachment;filename=" + filename);      
			response.setContentType("application/vnd.ms-powerpoint");   
			byte temp[] = new byte[1024]; 
			System.out.println("No error 1");
			fis = new FileInputStream(file);//Open and connect to the file input stream      
			System.out.println("No error 2");
			int n = 0;      
			while ((n = fis.read(temp)) != -1) { 
				int len=temp.length;
				System.out.println("temp length:"+len);
			} 
			System.out.println("End Read...");
		} catch (Exception e) {      
			System.out.println("Exception!");      
		} finally {      
			if ( os != null ) {
				os.close();
			}      
			if (fis != null) {
				fis.close();      
			}      
		}         

	}

}