package com.undao.control;

import java.io.*;
import java.util.regex.*;

import javax.servlet.http.*;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.undao.utils.ImageUtils;

public abstract class FileUploader extends AbstractDaemon {
	
	private static final long serialVersionUID = 6239542275135048820L;
	
	/**
	 * 图片压缩选项
	 */
	private boolean ifCompress = false;
	private int width = 1600;
	private int height = 1200;
	
	public void setCompress( boolean ifCompress ) {		//是否压缩
		this.ifCompress = ifCompress;
	}
	public void setWidth( int width ) {					//压缩后宽度
		this.width = width;
	}
	public void setHeight( int height ) {				//压缩后高度
		this.height = height;
	}

	/**
	 * 上传文件
	 * @param request			
	 * @param response			
	 * @param destDir			：保存文件的目录
	 * @param pAcceptFileType	：接收的文件类型(正则表达式)
	 * @param urlDir			: 相对于根目录的URL路径
	 */
	protected void uploadFile(HttpServletRequest request, HttpServletResponse response, String destDir, String urlDir, Pattern pAcceptFileType ) {		
		String status = "没有图片！";								//上传状态,返回到用户
		StringBuilder bufRealFileName = new StringBuilder( );		//随机文件名称.containPath为True时,则包含相对于根目录的路径
		
		try {
			File dir = new File( destDir );
			if ( !dir.isDirectory() ) {
				dir.mkdir();
			}
			if ( !ServletFileUpload.isMultipartContent(request) ) {
				return;
			}
			
			DiskFileItemFactory dff = new DiskFileItemFactory( );
			dff.setRepository( dir );
			dff.setSizeThreshold( 1024000 );
			ServletFileUpload sfu = new ServletFileUpload( dff );
			FileItemIterator fii = null;
			fii = sfu.getItemIterator( request );
			
			while ( fii.hasNext() ) {
				FileItemStream fis = fii.next( );
				if ( fis.isFormField() || fis.getName().length() <= 0 ) {
					continue;
				}
				String fileName = fis.getName();
				Matcher mFileType = pAcceptFileType.matcher( fileName );
				if ( !mFileType.find() ) {
					status = "请选择合适的文件类型！";
					break;
				}
				
				bufRealFileName.append( System.currentTimeMillis() ).append( fileName.substring(fileName.lastIndexOf("."), fileName.length()) );
				String realPath = destDir + File.separator + bufRealFileName.toString();

				BufferedInputStream in = new BufferedInputStream( fis.openStream() );	// 获得文件输入流
				FileOutputStream a = new FileOutputStream( new File(realPath) );
				BufferedOutputStream output = new BufferedOutputStream( a );
				Streams.copy(in, output, true);											// 开始把文件写到你指定的上传文件夹
				
				if ( ifCompress ) {
					ImageUtils.compressFile( destDir, bufRealFileName.toString(), destDir, bufRealFileName.toString(), width, height, true );
				}
			}
		} catch( Exception ee ) {
			ee.printStackTrace( );
			status = "暂不能上传文件，请稍候再试！";
		}
		StringBuilder bufResult = new StringBuilder( );
		if ( bufRealFileName.length() < 2 ) {
			bufResult.append( "{\"Error\": \"Failed\",\"FileName\": \"\",\"Status\": \"" ).append( status ).append( "\"}" );
//			bufResult.append( "0" );
		} else {
			response.setStatus( 200 );
			bufResult.append( "{" );
			bufResult.append( "\"Error\": \"\"," );
			bufResult.append( "\"FileName\": \"" ).append( urlDir + bufRealFileName.toString() ).append( "\"," );
			bufResult.append( "\"Status\": \"Success\"" );
			bufResult.append( "}" );
//			bufResult.append( urlDir ).append( bufRealFileName.toString() );
		}
		outputToClient( request, response, bufResult.toString() );
	}

}
