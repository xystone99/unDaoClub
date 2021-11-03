
package com.undao.security;

import java.awt.Color;  
import java.awt.image.BufferedImage;  
import java.io.File;  
import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  
  
import javax.imageio.ImageIO;   

/**
 * 识别IE房电话号码
 * @author Administrator
 * http://blog.csdn.net/problc/article/details/5794460
 */
public class ImageRecognizer {
	
	public static boolean IS_OUTPUT = false;							//是否输出解析后的值
	public static final String TRAIN_DIR = "D:\\Temporary\\Train";		//对照字符所在位置
	public static final String OUTPUT_DIR = "D:\\Temporary\\OutPut\\";	//解析后输出位置
	public static final String ERROR_RTN = "0123456789";				//解析失败后的返回值
	
	/**
	 * 设定阀值：区分黑白
	 * @param colorInt	: 前三个字节表示RGB,最高字节表示透明度
	 * @return
	 */
    private static int isWhite(int colorInt) {  
        Color color = new Color(colorInt);  
        if (color.getRed() + color.getGreen() + color.getBlue() > 150) {  
            return 1;  
        }  
        return 0;  
    }  
    
    /**
	 * 设定阀值：区分黑白
	 * @param colorInt	: 前三个字节表示RGB,最高字节表示透明度
	 * @return
	 */
    private static int isBlack(int colorInt) {  
        Color color = new Color(colorInt);  
        if (color.getRed() + color.getGreen() + color.getBlue() <= 150) {  
            return 1;  
        }  
        return 0;  
    }  
    
    /**
	 * 1、根据亮度设定阀值，对图片进行预处理
	 * @return
	 */
    private static BufferedImage removeBackgroud(String picFile) throws Exception {  
        BufferedImage img = ImageIO.read(new File(picFile));  
        if ( img == null ) {
        	return null;
        }
        int width = img.getWidth();  
        int height = img.getHeight();  
        for (int x = 0; x < width; ++x) {  
            for (int y = 0; y < height; ++y) {  
                if (isWhite(img.getRGB(x, y)) == 1) {  
                    img.setRGB(x, y, Color.WHITE.getRGB());  
                } else {  
                    img.setRGB(x, y, Color.BLACK.getRGB());  
                }  
            }  
        }  
        return img;  
    }
    
    /**
     * 2、对图片进行分割 
     * @return
     */
    private static List<BufferedImage> splitImage(BufferedImage img) throws Exception {  
        List<BufferedImage> subImgs = new ArrayList<BufferedImage>();  
        subImgs.add(img.getSubimage(3, 0, 8, 23));  
        subImgs.add(img.getSubimage(11, 0, 8, 23));  
        subImgs.add(img.getSubimage(19, 0, 8, 23));  
        subImgs.add(img.getSubimage(27, 0, 8, 23));
        subImgs.add(img.getSubimage(35, 0, 8, 23));
        subImgs.add(img.getSubimage(43, 0, 8, 23));
        subImgs.add(img.getSubimage(51, 0, 8, 23));
        if ( img.getWidth() > 67 ) {
        	subImgs.add(img.getSubimage(59, 0, 8, 23));
        }
        if ( img.getWidth() > 75 ) {
        	subImgs.add(img.getSubimage(67, 0, 8, 23));
        }
        if ( img.getWidth() > 83 ) {
        	subImgs.add(img.getSubimage(75, 0, 8, 23));
        }
        if ( img.getWidth() > 91 ) {
        	subImgs.add(img.getSubimage(83, 0, 8, 23));
        }	
        return subImgs;  
    }  
    
    /**
     * 3、训练(图片与字母数字的对照库)
     * @return
     */
    private static Map<BufferedImage, String> loadTrainData() throws Exception {  
        Map<BufferedImage, String> map = new HashMap<BufferedImage, String>();  
        File dir = new File( TRAIN_DIR );  
        File[] files = dir.listFiles();  
        for (File file : files) {  
            map.put(ImageIO.read(file), String.valueOf(file.getName().charAt(0)) );  
        }  
        return map;  
    }  
    
    /**
     * 4、识别(从对照库中选出相似度最大的图片及其对应的值)
     * @param img	: 待识别的图片
     * @param map	: 对照库
     * @return
     */
    private static String getSingleCharOcr(BufferedImage img, Map<BufferedImage, String> map) {  
        String result = "";  
        int width = img.getWidth();  
        int height = img.getHeight();  
        int min = width * height;  
        for (BufferedImage bi : map.keySet()) {  
            int count = 0;  
            Label1: for (int x = 0; x < width; ++x) {  
                for (int y = 0; y < height; ++y) {  
                    if (isWhite(img.getRGB(x, y)) != isWhite(bi.getRGB(x, y))) {  
                        count++;  
                        if (count >= min) break Label1;  
                    }  
                }  
            }  
            if (count < min) {  
                min = count;  
                result = map.get(bi);  
            }  
        }  
        return result;  
    }  
    
    /**
     * 分解图片，得到对应的字符串
     * @param filePath	: 待识别的图片路径
     * @return		: 相应的字符串
     * @throws Exception
     */
    public static String getAllOcr( String filePath ) throws Exception {  
        BufferedImage img = removeBackgroud( filePath );  
        if ( img == null ) {
        	return ERROR_RTN;				//解析失败
        }
        List<BufferedImage> listImg = splitImage(img);  
        Map<BufferedImage, String> map = loadTrainData();  
        StringBuilder bufResult = new StringBuilder();  
        for (BufferedImage bi : listImg) {  
        	bufResult.append( getSingleCharOcr(bi, map) );
        }  
        if ( IS_OUTPUT ) {
        	StringBuilder bufOutPath = new StringBuilder();
        	ImageIO.write(img, "JPG", new File( bufOutPath.append(OUTPUT_DIR).append( bufOutPath.toString() ).append(".jpg").toString()) );
        }
        return bufResult.toString();  
    }
  
    /** 
     * main test
     */  
    public static void main(String[] args) throws Exception {   
    	String text = getAllOcr("D:/Temporary/898637a3-e82d-4af2-b998-ebd4543dfbcd.bmp");  
    	System.out.println(text);  
    }
    
}  
