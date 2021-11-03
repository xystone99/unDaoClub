/**
 * Created At 2014-3-6 上午11:26:12
 */
package com.undao.utils;

import java.io.*;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xwpf.extractor.*;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.xmlbeans.XmlException;

/**
 * @author Administrator
 *
 */
public class PoiUtils {
	
	public final static boolean DEBUG = false;
	
	public final static String getCellString( XSSFCell cell ) {
		if ( cell == null ) {
			return "";
		}
		if ( cell.getCellTypeEnum()== CellType.STRING ) {
			return cell.getStringCellValue().trim( );
		} else if ( cell.getCellTypeEnum() == CellType.NUMERIC ) {
			return String.valueOf( cell.getNumericCellValue() ).trim( );
		} else if ( cell.getCellTypeEnum() == CellType.BLANK ) {
			return "";
		} else if ( cell.getCellTypeEnum() == CellType.ERROR ) {
			return cell.getErrorCellString();
		} else if ( cell.getCellTypeEnum() == CellType.BOOLEAN ) {
			return String.valueOf( cell.getBooleanCellValue() );
		} 
		return "";
	}
	
	/**
	 * 设置合并单元格之后的边框
	 */
	public static void setRegionBorder_None( XSSFWorkbook wb, XSSFSheet sheet, CellRangeAddress region ) {
		setRegionBorder( wb, sheet, region, BorderStyle.NONE );
	}
	public static void setRegionBorder_Thin( XSSFWorkbook wb, XSSFSheet sheet, CellRangeAddress region ) {
		setRegionBorder( wb, sheet, region, BorderStyle.THIN );
	}
	public static void setRegionBorder_Thick( XSSFWorkbook wb, XSSFSheet sheet, CellRangeAddress region ) {
		setRegionBorder( wb, sheet, region, BorderStyle.MEDIUM );
	}
	public static void setRegionBorder_Double( XSSFWorkbook wb, XSSFSheet sheet, CellRangeAddress region ) {
		setRegionBorder( wb, sheet, region, BorderStyle.DOUBLE );
	}
	
	public static void setRegionBorder(XSSFWorkbook wb, XSSFSheet sheet, CellRangeAddress region, BorderStyle borderStyle ) {
		setRegionBorder( wb, sheet, region, borderStyle, borderStyle, borderStyle, borderStyle );
	}
	public static void setRegionBorder( XSSFWorkbook wb, XSSFSheet sheet, CellRangeAddress region, BorderStyle leftStyle, BorderStyle bottomStyle, BorderStyle rightStyle, BorderStyle topStyle ){
		RegionUtil.setBorderLeft( leftStyle, region, sheet );
		RegionUtil.setBorderBottom( bottomStyle, region, sheet );
		RegionUtil.setBorderRight( rightStyle, region, sheet );
		RegionUtil.setBorderTop( topStyle, region, sheet );
	}
	
	/**
	 * 设置打印边距等
	 * @param landscape : 打印方向,true:横向;false:纵向.
	 */
	public static void setPrintFormat( XSSFSheet sheet, boolean landscape, double bMargin, double lMargin, double rMargin, double tMargin, boolean hCenter, boolean vCenter ) {
		XSSFPrintSetup ps = sheet.getPrintSetup();
		ps.setLandscape( landscape );	
        ps.setPaperSize(XSSFPrintSetup.A4_PAPERSIZE); 		//纸张
        sheet.setMargin(XSSFSheet.BottomMargin, bMargin);	//页边距（下）
        sheet.setMargin(XSSFSheet.LeftMargin, lMargin);		//页边距（左）
        sheet.setMargin(XSSFSheet.RightMargin, rMargin);	//页边距（右）
        sheet.setMargin(XSSFSheet.TopMargin, tMargin);		//页边距（上）
        sheet.setHorizontallyCenter( hCenter );				//设置打印页面为水平居中
        sheet.setVerticallyCenter( vCenter );				//设置打印页面为垂直居中
	}
	
	public static void setNormalPrintFormat( XSSFSheet sheet, boolean landscape ) {
		setPrintFormat( sheet, landscape, 0.3, 0.3, 0.3, 0.3, true, false );
	}
	
	/**
	 * 设置自动换行
	 */
	public static CellStyle getWrapStyle( XSSFWorkbook wb ) {
		CellStyle style = wb.createCellStyle( );    
		style.setWrapText( true );
		return style;
	}
	
	/**
	 * 创建字体
	 * @param wb			：工作簿
	 * @param fontName		：字体名称（宋体）
	 * @param isBold		：加粗
	 * @param size			：字体大小（(short)10）
	 * @param color			：字体颜色（IndexedColors.AUTOMATIC.getIndex()）
	 * @return
	 */
	public static Font getFont( XSSFWorkbook wb, String fontName, boolean isBold, short size, short color ) {
		Font font = wb.createFont( );	
		font.setFontName( fontName );
		font.setBold( isBold );
		font.setFontHeightInPoints( size );         
		font.setColor( color );		
		return font;
	}
	
	public static Font getNormalFont( XSSFWorkbook wb ) {						//正文字体
		return getNormalFont( wb, (short)10 );
	}
	
	public static Font getNormalFont( XSSFWorkbook wb, short fontSize ) {		//正文字体
		return getFont( wb, "微软雅黑", false, (short)fontSize, IndexedColors.AUTOMATIC.getIndex() );
	}
	
	public static Font getTitleFont( XSSFWorkbook wb ) {						//标题字体
		return getTitleFont( wb, (short)12 );
	}
	
	public static Font getTitleFont( XSSFWorkbook wb, short fontSize ) {		//标题字体
		return getFont( wb, "微软雅黑", true, fontSize, IndexedColors.AUTOMATIC.getIndex() );
	}
	
	/**
	 * 创建小数位数、千分位
	 */
	public static short getDataFormat( XSSFWorkbook wb, int digits, boolean ifThousands ) {
		XSSFDataFormat format = wb.createDataFormat();
		String strFmt = null;
		if ( digits == 1 ) {
			strFmt = ifThousands ? "#,#0.0" : "0.0";
		} else if ( digits == 2 ) {
			strFmt = ifThousands ? "#,#0.00" : "0.00";
		} else if ( digits == 3 ) {
			strFmt = ifThousands ? "#,#0.000" : "0.000";
		}
		return format.getFormat( strFmt );
	}
	
	/**
	 * 创建单元格格式
	 * @param wb			：工作簿
	 * @param font			：字体
	 * @param hAlignment	：水平对齐（XSSFCellStyle.ALIGN_CENTER）
	 * @param vAlignment	：垂直对齐（XSSFCellStyle.VERTICAL_CENTER）
	 * @param fColor		：背景颜色（HSSFColor.GREY_25_PERCENT.index）
	 * @param lBorder		：左边框（XSSFCellStyle.BORDER_THIN）
	 * @param bBorder		：下边框
	 * @param rBorder		：右边框
	 * @param tBorder		：上边框
	 * @param wrapText		: 是否自动换行
	 * @return 
	 */
	public static CellStyle getStyle( XSSFWorkbook wb, Font font,
									  HorizontalAlignment hAlignment, VerticalAlignment vAlignment, short fColor,
									  BorderStyle lBorder, BorderStyle bBorder, BorderStyle rBorder, BorderStyle tBorder, boolean wrapText ) {
		CellStyle style = wb.createCellStyle( );    
		style.setFont( font );
		style.setFillPattern( FillPatternType.SOLID_FOREGROUND );
		style.setFillForegroundColor( fColor );
		style.setAlignment( hAlignment );
		style.setVerticalAlignment( vAlignment );
		style.setBorderLeft( lBorder );
		style.setBorderBottom( bBorder );
		style.setBorderRight( rBorder );
		style.setBorderTop( tBorder );
		style.setWrapText( wrapText );
		return style;
	}
	
	/**
	 * 标题格式
	 */
	public static CellStyle getTitleStyle( XSSFWorkbook wb, boolean ifGray, boolean ifSmall ) {
		return getTitleStyle( wb, ifSmall?(short)10:(short)12, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, ifGray );
	}
	public static CellStyle getTitleLeftStyle( XSSFWorkbook wb, boolean ifGray, boolean ifSmall ) {
		return getTitleStyle( wb, ifSmall?(short)10:(short)12, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, ifGray );
	}
	public static CellStyle getTitleRightStyle( XSSFWorkbook wb, boolean ifGray, boolean ifSmall ) {
		return getTitleStyle( wb, ifSmall?(short)10:(short)12, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, ifGray );
	}
	public static CellStyle getTitleStyle( XSSFWorkbook wb, short fontSize, HorizontalAlignment alignType, VerticalAlignment verticalType, boolean ifGray ) {
		return getStyle( wb, getTitleFont(wb,fontSize), alignType, verticalType, 
				ifGray ? IndexedColors.GREY_25_PERCENT.index : IndexedColors.WHITE.index, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, false );
	}

	/**
	 * 无边框格式
	 */
	public static CellStyle getBlankStyle( XSSFWorkbook wb ) {
		return getBlankStyle( wb, (short)10 );
	}
	public static CellStyle getBlankStyle( XSSFWorkbook wb, short fontSize ) {
		return getBlankStyle(wb,fontSize, HorizontalAlignment.CENTER, VerticalAlignment.CENTER );
	}
	public static CellStyle getBlankRightStyle( XSSFWorkbook wb, short fontSize ) {
		return getBlankStyle(wb,fontSize, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER );
	}
	public static CellStyle getBlankLeftStyle( XSSFWorkbook wb, short fontSize ) {
		return getBlankStyle(wb,fontSize, HorizontalAlignment.LEFT, VerticalAlignment.CENTER );
	}
	public static CellStyle getBlankStyle( XSSFWorkbook wb, short fontSize, HorizontalAlignment hAlignment, VerticalAlignment vAlignment ) {
		return getStyle( wb, getNormalFont(wb,fontSize), hAlignment, vAlignment, IndexedColors.WHITE.index, BorderStyle.NONE, BorderStyle.NONE, BorderStyle.NONE, BorderStyle.NONE, false );
	}
	
	/**
	 * 正文格式(左对齐)
	 */
	public static CellStyle getNormalLeftStyle( XSSFWorkbook wb ) {
		return getNormalLeftStyle( wb, (short)10, VerticalAlignment.CENTER );
	}
	public static CellStyle getNormalTopLeftStyle( XSSFWorkbook wb ) {
		return getNormalLeftStyle( wb, (short)10, VerticalAlignment.TOP );
	}
	public static CellStyle getNormalBottomLeftStyle( XSSFWorkbook wb ) {
		return getNormalLeftStyle( wb, (short)10, VerticalAlignment.BOTTOM );
	}
	public static CellStyle getNormalLeftStyle( XSSFWorkbook wb, short fontSize ) {
		return getNormalLeftStyle( wb, fontSize, VerticalAlignment.CENTER );
	}
	public static CellStyle getNormalLeftStyle( XSSFWorkbook wb, short fontSize, VerticalAlignment vAlignment ) {
		return getStyle( wb, getNormalFont(wb,fontSize), HorizontalAlignment.LEFT, vAlignment,
				IndexedColors.WHITE.index, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, true );
	}
	
	/**
	 * 正文格式(右对齐)
	 */
	public static CellStyle getNormalRightStyle( XSSFWorkbook wb ) {
		return getNormalRightStyle( wb, (short)10, VerticalAlignment.CENTER );
	}
	public static CellStyle getNormalRightStyle( XSSFWorkbook wb, int digits ) {
		return getNormalRightStyle( wb, digits, false );
	}
	public static CellStyle getNormalRightStyle( XSSFWorkbook wb, int digits, boolean ifThousands ) {
		CellStyle tCellStyle = getNormalRightStyle( wb, (short)10, VerticalAlignment.CENTER );
		tCellStyle.setDataFormat( getDataFormat(wb,digits,ifThousands) );
		return tCellStyle;
	}
	public static CellStyle getNormalTopRightStyle( XSSFWorkbook wb ) {
		return getNormalRightStyle( wb, (short)10, VerticalAlignment.TOP );
	}
	public static CellStyle getNormalBottomRightStyle( XSSFWorkbook wb ) {
		return getNormalRightStyle( wb, (short)10, VerticalAlignment.BOTTOM );
	}
	public static CellStyle getNormalRightStyle( XSSFWorkbook wb, short fontSize ) {
		return getNormalRightStyle( wb, fontSize, VerticalAlignment.CENTER );
	}
	public static CellStyle getNormalRightStyle( XSSFWorkbook wb, short fontSize, VerticalAlignment vAlignment ) {
		return getStyle( wb, getNormalFont(wb,fontSize), HorizontalAlignment.RIGHT, vAlignment,
				IndexedColors.WHITE.index, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, false );
	}
	
	/**
	 * 正文格式(居中)
	 */
	public static CellStyle getNormalCenterStyle( XSSFWorkbook wb ) {
		return getNormalCenterStyle( wb, (short)10, VerticalAlignment.CENTER );
	}
	public static CellStyle getNormalTopCenterStyle( XSSFWorkbook wb ) {
		return getNormalCenterStyle( wb, (short)10, VerticalAlignment.TOP );
	}
	public static CellStyle getNormalBottomCenterStyle( XSSFWorkbook wb ) {
		return getNormalCenterStyle( wb, (short)10, VerticalAlignment.BOTTOM );
	}
	public static CellStyle getNormalCenterStyle( XSSFWorkbook wb, short fontSize ) {
		return getNormalCenterStyle( wb, fontSize, VerticalAlignment.CENTER );
	}
	public static CellStyle getNormalCenterStyle( XSSFWorkbook wb, short fontSize, VerticalAlignment vAlignment ) {
		return getStyle( wb, getNormalFont(wb,fontSize), HorizontalAlignment.CENTER, vAlignment,
				IndexedColors.WHITE.index, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, false );
	}
	
	/**
	 * @Method: extractTextFromDOCX
	 * @Description: 从Word2007文档中提取纯文本
	 */
	public static String extractTextFromDOCX( String fileName ) {
		POIXMLTextExtractor ex;
		try {
			OPCPackage opcPackage = POIXMLDocument.openPackage( fileName );
			ex = new XWPFWordExtractor( opcPackage );
			return ex.getText( );
		} catch (IOException ioe ) {
			return ioe.getMessage( );
		} catch ( XmlException xe ) {
			return xe.getMessage( );
		} catch ( OpenXML4JException oxe ) {
			return oxe.getMessage( );
		}
	}

	/**
	 * @Method: extractTextFromXLS
	 * @Description: 从Excell2003文档中提取纯文本
	 */
	@SuppressWarnings("deprecation")
	private static String extractTextFromXLS(InputStream is) throws IOException {
		StringBuffer content = new StringBuffer( );
		XSSFWorkbook workbook = new XSSFWorkbook( is );							//创建对Excel工作簿文件的引用

		for ( int numSheets=0; numSheets<workbook.getNumberOfSheets(); numSheets++ ) {
			if ( null != workbook.getSheetAt(numSheets) ) {
				XSSFSheet aSheet = workbook.getSheetAt(numSheets);				//获得一个sheet
				for ( int rowNumOfSheet=0; rowNumOfSheet<=aSheet.getLastRowNum(); rowNumOfSheet++ ) {
					if ( null != aSheet.getRow(rowNumOfSheet) ) {
						XSSFRow aRow = aSheet.getRow( rowNumOfSheet );			//获得一行
						for ( short cellNumOfRow=0; cellNumOfRow<=aRow.getLastCellNum(); cellNumOfRow++ ) {
							if ( null != aRow.getCell(cellNumOfRow) ) {
								XSSFCell aCell = aRow.getCell(cellNumOfRow);	// 获得列值
								if ( aCell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC ) {
									content.append( aCell.getNumericCellValue() );
								} else if ( aCell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN ) {
									content.append( aCell.getBooleanCellValue() );
								} else {
									content.append( aCell.getStringCellValue() );
								}
							}
						}
					}
				}
			}
		}
		return content.toString( );
	}

	/**
	 * @Method: extractTextFromXLS2007
	 * @Description: 从Excell2007文档中提取纯文本
	 */
	private static String extractTextFromXLSX( String fileName ) throws Exception {
		StringBuffer content = new StringBuffer( );
		FileInputStream fileInputStream = new FileInputStream( new File(fileName) );
		XSSFWorkbook xwb = new XSSFWorkbook( fileInputStream );								//构造 XSSFWorkbook 对象，strPath 传入文件路径

		for ( int numSheet=0; numSheet<xwb.getNumberOfSheets(); numSheet++ ) {			//循环工作表Sheet
			XSSFSheet xSheet = xwb.getSheetAt( numSheet );
			if ( xSheet == null ) {
				continue;
			}
			for ( int rowNum=0; rowNum<=xSheet.getLastRowNum(); rowNum++ ) {			//循环行Row
				XSSFRow xRow = xSheet.getRow( rowNum );
				if ( xRow == null ) {
					continue;
				}
				for ( int cellNum=0; cellNum<=xRow.getLastCellNum(); cellNum++ ) {		//循环列Cell
					XSSFCell xCell = xRow.getCell( cellNum );
					if ( xCell == null ) {
						continue;
					}
					if ( xCell.getCellTypeEnum() == CellType.BOOLEAN ) {
						content.append( xCell.getBooleanCellValue() );
					} else if ( xCell.getCellTypeEnum() == CellType.NUMERIC ) {
						content.append( xCell.getNumericCellValue() );
					} else {
						content.append( xCell.getStringCellValue() );
					}
				}
			}
		}
		return content.toString( );
	}
	
	/**
	 * Main Test
	 */
	public static void main(String[] args) {
		try {
			String wordFile = "D:/20121128.docx";
			String wordText2007 = PoiUtils.extractTextFromDOCX(wordFile);
			System.out.println("wordText2007=======" + wordText2007);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
