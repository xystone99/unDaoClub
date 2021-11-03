package xms.daemon.upload;

import java.io.*;
import java.util.regex.Pattern;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.undao.control.FileUploader;

import xms.XmsInitial;

@WebServlet(asyncSupported = false, value = "/uploadcontract", loadOnStartup = 9 )
public class ContractUploader extends FileUploader {
	
	private static final long serialVersionUID = -8169784428260420957L;
	
	private static String UPLOAD_DEST_DIR;
	private static String URL_DIR = "/AA_Upload/Contract/";
	
	private static String rAcceptFileType = "[.]jpg|png|jpeg|gif$";
	private static Pattern pAcceptFileType = Pattern.compile( rAcceptFileType, Pattern.CASE_INSENSITIVE );
	
	static {
		UPLOAD_DEST_DIR = XmsInitial.getHomeDir() + File.separator + "AA_Upload" + File.separator + "Contract";
	}
	
	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) {		
		setCompress( false );
		uploadFile(request, response, UPLOAD_DEST_DIR, URL_DIR, pAcceptFileType);
	}

}
