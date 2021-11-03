/**
 * Created At 2014-4-21 上午10:19:47
 */
package com.undao.utils;

/**
 * @author Administrator
 * 
 * 缩略图类。将jpg、bmp、png、gif图片文件，进行等比或非等比的大小转换。 
 * 具体使用方法compressPic(大图片路径,生成小图片路径,大图片文件名,生成小图片文名,生成小图片宽度,生成小图片高度,是否等比缩放(默认为true))
 *
 */
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageUtils { 
	 
	public static boolean DEBUG = true;
	
	/**
	 * 获得图片大小
	 * @param filePath	: 图片文件的路径 
	 * @return			：图片大小	
	 */
	public static long getPicSize(String filePath) { 
		return new File(filePath).length(); 
	}
	
	/**
	 * 压缩单张图片
	 * @param inDir				：待压缩图片所在目录
	 * @param inFileName		：待压缩图片文件名
	 * @param outDir			：压缩后图片所在目录
	 * @param outFileName		：压缩后图片名字
	 * @param outWidth			：压缩后图片像素宽度
	 * @param outHeight			：压缩后图片像素高度
	 * @param equalProportion	：是否等比例压缩
	 * @return
	 */
	public static boolean compressFile(String inDir, String inFileName, String outDir, String outFileName, int outWidth, int outHeight, boolean equalProportion ) {
		try { 
			//获得源文件 
			File file = new File(inDir + File.separator + inFileName); 
			if ( !file.exists( ) ) { 
				return false; 
			} 
			Image img = ImageIO.read( file );
			if ( img == null ) {
				return false;
			}
			 
			// 判断图片格式是否正确 
			if ( img.getWidth(null) == -1 ) {
				return false; 
			} 
			if ( ((double)img.getWidth(null) < outWidth) && ((double)img.getHeight(null)<= outHeight) ) {
				return true;
			}
			
			int newWidth; int newHeight;
			// 判断是否是等比缩放 
			if ( equalProportion ) {	// 为等比缩放计算输出的图片宽度及高度 	
				double rate1 = ((double)img.getWidth(null)) / (double) outWidth + 0.1; 
				double rate2 = ((double)img.getHeight(null)) / (double) outHeight + 0.1; 
				double rate = rate1 > rate2 ? rate1 : rate2;	// 根据缩放比率大的进行缩放控制 
				newWidth = (int)(((double) img.getWidth(null)) / rate); 
				newHeight = (int)(((double) img.getHeight(null)) / rate); 
			} else { 
				newWidth = outWidth;	// 输出的图片宽度 
				newHeight = outHeight;	// 输出的图片高度 
			} 
			 
			BufferedImage tag = new BufferedImage((int) newWidth, (int) newHeight, BufferedImage.TYPE_INT_RGB); 
			//Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的优先级比速度高 生成的图片质量比较好,但速度慢
			tag.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);	
			FileOutputStream out = new FileOutputStream(outDir + File.separator + outFileName);
			// JPEGImageEncoder可适用于其他图片类型的转换 
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out); 
			encoder.encode(tag); 
			out.close(); 
		} catch( IOException ioe ) { 
			if(DEBUG) System.out.println( "ImageUtils.compress()??" + ioe.getMessage() ); 
			return false;
		} 
		return true; 
	} 
	
	/**
	 * 递归压缩fromDir目录下的图片到toDir目录下
	 * @param fromDir		：原始图片目录
	 * @param toDir			：压缩后的图片目录
	 * @param outWidth		：压缩后图片的像素宽度
	 * @param outHeight		：压缩后图片的像素高度
	 */
	public static void recursionCompress( File fromDir, File toDir, int outWidth, int outHeight ) {
		 File[] arrFile = fromDir.listFiles(); 
		 if ( arrFile == null ) {
			 return;
		 }
		 if ( !toDir.exists() ) {
			 toDir.mkdirs( );
		 }
		 for( int j=0; j<arrFile.length; j++ ) {
			 if ( arrFile[j].isFile() ) {
				 compressFile(fromDir.getAbsolutePath(),arrFile[j].getName(),toDir.getAbsolutePath(),arrFile[j].getName(),outWidth,outHeight,true );
			 }
			 if ( arrFile[j].isDirectory() ) {
				 File newDir = new File( toDir.getAbsolutePath() + File.separator + arrFile[j].getName() );
				 newDir.mkdir();
				 recursionCompress(arrFile[j],newDir,outWidth,outHeight);
			 }
		 }
	 }
 	
 	
	public static void main(String[] arg) { 
		//ImageUtils.compressFile( "d:\\", "r10.jpg", "d:\\", "r11.jpg", 1024, 768, true );
		
		/*		 
		System.out.println("输入的图片大小：" + ImageUtils.getPicSize("d:\\100_1541.jpg")/1024 + "KB"); 
		int count = 0; // 记录全部图片压缩所用时间
		for (int i = 0; i < 100; i++) { 
			int start = (int) System.currentTimeMillis();	// 开始时间 
			ImageUtils.compressFile("d:\\", "100_1541.jpg", "d:\\Temporary\\", "r1"+i+".jpg", 120, 120, true); 
			int end = (int) System.currentTimeMillis(); // 结束时间 
			int re = end-start; // 但图片生成处理时间 
			count += re; System.out.println("第" + (i+1) + "张图片压缩处理使用了: " + re + "毫秒"); 
			System.out.println("输出的图片大小：" + ImageUtils.getPicSize("d:\\Temporary\\r1"+i+".jpg")/1024 + "KB"); 
		}
		System.out.println("总共用了：" + count + "毫秒");
		*/
		
		ImageUtils.recursionCompress(new File("D:\\Temporary\\01"), new File("D:\\Temporary\\02"), 800, 600);	 
	 
	}
	
}

