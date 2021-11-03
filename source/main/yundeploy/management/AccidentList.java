/**
 * Created At 2019-5-10 下午20:40:12
 */
package yundeploy.management;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import org.apache.poi.xssf.usermodel.*;

import com.undao.utils.*;

/**
 * @author Administrator
 *
 */
public class AccidentList {
	
	public final static int K_PROVIDER = 1011;		//供应商
	public final static int K_DUTY_0 = 1021;		//主责
	public final static int K_DUTY_1 = 1022;		//次责
	public final static int K_DUTY_2 = 1023;		//管理责
	public final static int K_LEAVE = 1031;			//离职员工
	
	public final static String T_ID = "审批编号";
	public final static String TOTAL_DEDUCTION = "1.罚款总额";
	public final static String T_INSURE = "1-1.保险理赔";
	public final static String T_COMPANY = "1-2.公司承担";
	public final static String T_PROVIDER_1 = "责任供应商";
	public final static String T_PROVIDER_2 = "1-3.责任供应商";
	public final static String T_EMPLOYEE = "1-4 个人承担金额汇总";
	public final static String T_DUTY_0 = "主责任人";
	public final static String T_DUTY_1 = "次责任人";
	public final static String T_DUTY_2 = "管理责任人";
	public final static String T_LEAVE = "离职员工";
	
	public int COL_INDEX_ID = 0;
	public int COL_INDEX_PROVIDER_1 = 33;
	public int COL_INDEX_PROVIDER_2 = 37;
	public int COL_INDEX_DUTY_0 = 40;
	public int COL_INDEX_DUTY_1 = 43;
	public int COL_INDEX_DUTY_2 = 46;
	public int COL_INDEX_LEAVE = 49;
	
	final static String[] ARR_TITLE_TAG = {
		"审批编号", "标题", "审批状态", "审批结果", "发起时间", "完成时间", "耗时(时:分:秒)", "发起人工号", "发起人UserID", "发起人姓名",	
		"发起人部门", "历史审批人姓名", "审批记录", "当前处理人姓名", "运单号", "报告日期", "事发地点", "项目名称", "运输线路", "事故概述", 
		"事故相关附件", "事故类型", "事故类型说明", "异常阶段", "实际损失", "预估损失", "事故等级", "事故责任方", "事故主责分公司", "事故主责部门", 
		"改善措施", "处理决定", "责任界定日期", "责任供应商", "1.罚款总额", "1-1.保险理赔", "1-2.公司承担", "1-3.责任供应商", "1-4 个人承担金额汇总",
		};

	final static ArrayList<String> ARR_LIST_TITLE_TAG = new ArrayList<String>(Arrays.asList(ARR_TITLE_TAG));
	final static int[] ARR_COL_INDEX = new int[ARR_TITLE_TAG.length];
	
	class Deduction {
		int kind;
		String name;
		BigDecimal amount;
		public Deduction( int dKind, String dName, String dAmount ) {
			this.kind = dKind;
			this.name = dName;
			this.amount = new BigDecimal( dAmount );
		}
		public int getKind() { return kind;		}
		public String getName() { return name;		}
		public BigDecimal getAmount() { return amount;		}
	}
	
	class Accident {
		String accID;
		HashMap<Integer, String> map = new HashMap<Integer,String>();
		ArrayList<Deduction> arrayList = new ArrayList<Deduction>();
		public Accident( String id ) {
			this.accID = id;
		}
		public String getID( ) {
			return accID;
		}
		public String getItemValue( int itemTag ) { 
			return map.get( new Integer( itemTag ) );
		}
		public String getItemValue( String itemTag ) {
			return map.get( new Integer(ARR_LIST_TITLE_TAG.indexOf(itemTag)) );
		}
		public void setTitleItem( int itemTag, String itemValue ) {
			map.put( new Integer( itemTag ), itemValue );
		}
		public void addDeduction( int dKind, String dName, String dAmount ) { 
			try {
				if ( new Float(dAmount).floatValue() >= 0.01 ) {
					arrayList.add( new Deduction(dKind, dName, dAmount) );
				}
			} catch( Exception e ) {
				System.out.println( "AddDeduction Error>>" + dKind + "=" + dName + "=" + dAmount );
			}
		}
		public int getDeductionSize( ) {
			return arrayList.size( );
		}
		public Deduction getDeduction( int index ) {
			return arrayList.get( index );
		}
		public int deductionError( ) {
			String sumTotal = getItemValue( TOTAL_DEDUCTION );
			String sumInsure = getItemValue( T_INSURE );
			String sumCompany = getItemValue( T_COMPANY );
			String sumProvider = getItemValue( T_PROVIDER_2 );
			String sumEmployee = getItemValue( T_EMPLOYEE );
			
			BigDecimal totalDeduct = new BigDecimal( sumTotal.length()>0 ? sumTotal : "0" );
			BigDecimal tInsure = new BigDecimal( sumInsure.length()>0 ? sumInsure : "0" );
			BigDecimal tCompany = new BigDecimal( sumCompany.length()>0 ? sumCompany : "0" );
			BigDecimal tProvider = new BigDecimal( sumProvider.length()>0 ? sumProvider : "0" );
			BigDecimal tEmployee = new BigDecimal( sumEmployee.length()>0 ? sumEmployee : "0" );
			
			BigDecimal calcTotal = new BigDecimal(0);
			calcTotal = calcTotal.add(tInsure).add( tCompany ).add( tProvider ).add( tEmployee );
			
			BigDecimal calcEmployee = new BigDecimal(0);
			for ( int j=0; j<arrayList.size(); j++ ) {
				calcEmployee = calcEmployee.add( arrayList.get(j).getAmount() );
			}
			
			if ( totalDeduct.compareTo(calcTotal)==0 && tEmployee.compareTo(calcEmployee)==0 ) {
				return 0;
			} else if ( totalDeduct.compareTo(calcTotal)!=0 && tEmployee.compareTo(calcEmployee)!=0 ) {
				System.out.println( accID + ">>" + totalDeduct + "<>" + tInsure + "+" + tCompany + "+" + tProvider + "+" + tEmployee + "&&" + calcEmployee );
				return 3;
			} else if ( totalDeduct.compareTo(calcTotal)!=0 ) {
				System.out.println( accID + ">>" + totalDeduct + "<>" + tInsure + "+" + tCompany + "+" + tProvider + "+" + tEmployee );
				return 1;
			} else if ( tEmployee.compareTo(calcEmployee)!=0 ) {
				System.out.println( accID + ">>" + tEmployee + "<>" + calcEmployee );
				return 2;
			}
			return 9;
		}
	}
	
	private File xlsFile;
	
	
	private ArrayList<String> accList = new ArrayList<String>( );
	private ArrayList<Accident> arrListAccident = new ArrayList<Accident>( );
	
	public AccidentList( String xlsPath) {
		this.xlsFile = new File( xlsPath );
	}
	
	public void queryIDByName( String name, int posName, int posID ) {	//依据发起人姓名，打印出审批单号
		if ( !xlsFile.exists() ) {
			System.out.println( "xms.bill.AccidentList>>XLSX File Not Exists" );
			return;
		}
		
		XSSFWorkbook wb = null;
		XSSFSheet sheet = null;
		XSSFRow row = null;
		try {    
			FileInputStream excelFileInputStream = new FileInputStream( xlsFile );
			wb = new XSSFWorkbook( excelFileInputStream );
			for ( int iSheet=0; iSheet<wb.getNumberOfSheets(); iSheet++ ) {
				sheet = wb.getSheetAt( iSheet );  
				for ( int j=1; j<=sheet.getLastRowNum(); j++ ) {
	            	row = sheet.getRow( j );
	            	String tName = row.getCell(posName).getStringCellValue().trim();
	            	if ( tName.indexOf( name ) >= 0 ) {
	            		System.out.println( row.getCell(posID).getStringCellValue().trim() );
	            	}
				}
			}
			excelFileInputStream.close( );
			System.out.println( "Finished" );
		} catch( IOException ioe ) {
			System.out.println( "xms.bill.AccidentList>>" + ioe.getMessage() );
			ioe.printStackTrace( );
		} catch( Exception e ) {
			System.out.println( "xms.bill.AccidentList>>" + e.getMessage() );
			e.printStackTrace( );
		} finally {
		}
	}
	
	public void analyseAccidentXLS( ) {
		if ( !xlsFile.exists() ) {
			System.out.println( "xms.bill.AccidentList>>XLSX File Not Exists" );
			return;
		}
		
		XSSFWorkbook wb = null;
		XSSFSheet sheet = null;
		try {    
			FileInputStream excelFileInputStream = new FileInputStream( xlsFile );
			wb = new XSSFWorkbook( excelFileInputStream );
			for ( int iSheet=0; iSheet<wb.getNumberOfSheets(); iSheet++ ) {
				sheet = wb.getSheetAt( iSheet );  
				XSSFRow row = sheet.getRow( 0 );
				for ( int j=0; j<row.getPhysicalNumberOfCells(); j++ ) {
					String title = row.getCell( j ).getStringCellValue().trim();
					int index = ARR_LIST_TITLE_TAG.indexOf( title );
					if ( index >= 0 ) {
						ARR_COL_INDEX[index] = j;
					} else if ( title.equals( T_DUTY_0 ) ) {
						COL_INDEX_DUTY_0 = j;
					} else if ( title.equals( T_DUTY_1 ) ) {
						COL_INDEX_DUTY_1 = j;
					} else if ( title.equals( T_DUTY_2 ) ) {
						COL_INDEX_DUTY_2 = j;
					} else if ( title.equals( T_LEAVE ) ) {
						COL_INDEX_LEAVE = j;
					}
					if ( title.equals( T_ID ) ) {
						COL_INDEX_ID = j;
					}
					if ( title.equals( T_PROVIDER_1 ) ) {
						COL_INDEX_PROVIDER_1 = j;
					}
					if ( title.equals( T_PROVIDER_2 ) ) {
						COL_INDEX_PROVIDER_2 = j;
					}
				}
				System.out.println( COL_INDEX_ID + "---" + COL_INDEX_PROVIDER_1 + "---" + COL_INDEX_PROVIDER_2 + "---" + COL_INDEX_DUTY_0 + "---" + COL_INDEX_DUTY_1 + "---" + COL_INDEX_DUTY_2 + "---" + COL_INDEX_LEAVE );
				
				String cell_1, cell_2;
	            for ( int j=1; j<=sheet.getLastRowNum(); j++ ) {
	            	row = sheet.getRow( j );
	            	String accdID = row.getCell(COL_INDEX_ID).getStringCellValue().trim();
	            	Accident accd = new Accident( accdID );
	            	int index = accList.indexOf( accdID );
	            	if ( index >= 0 ) {
	            		accd = arrListAccident.get( index );
	            	} else {
	            		for ( int h=0; h<ARR_COL_INDEX.length; h++ ) {
	                		XSSFCell cell = row.getCell( ARR_COL_INDEX[h] );
	                		accd.setTitleItem(h, PoiUtils.getCellString( cell ) );
	                	}
	            		accList.add( accdID );
	            		arrListAccident.add( accd );
	            		
	            		cell_1 = PoiUtils.getCellString( row.getCell( COL_INDEX_PROVIDER_1 ) );
	                	cell_2 = PoiUtils.getCellString( row.getCell( COL_INDEX_PROVIDER_2 ) );
	                	if ( cell_2.length() > 0) {
	                		accd.addDeduction( K_PROVIDER, cell_1, cell_2 );
	                	}
	            	}
	            	
	            	cell_1 = PoiUtils.getCellString( row.getCell( COL_INDEX_DUTY_0 ) );
	            	cell_2 = PoiUtils.getCellString( row.getCell( COL_INDEX_DUTY_0 + 1 ) );
	            	if ( cell_2.length() > 0) {
                		accd.addDeduction( K_DUTY_0, cell_1, cell_2 );
                	}
	            	
	            	cell_1 = PoiUtils.getCellString( row.getCell( COL_INDEX_DUTY_1 ) );
	            	cell_2 = PoiUtils.getCellString( row.getCell( COL_INDEX_DUTY_1 + 1 ) );
	            	if ( cell_2.length() > 0) {
                		accd.addDeduction( K_DUTY_1, cell_1, cell_2 );
                	}
	            	
	            	cell_1 = PoiUtils.getCellString( row.getCell( COL_INDEX_DUTY_2 ) );
	            	cell_2 = PoiUtils.getCellString( row.getCell( COL_INDEX_DUTY_2 + 1 ) );
	            	if ( cell_2.length() > 0) {
                		accd.addDeduction( K_DUTY_2, cell_1, cell_2 );
                	}
	            	
	            	cell_1 = PoiUtils.getCellString( row.getCell( COL_INDEX_LEAVE ) );
	            	cell_2 = PoiUtils.getCellString( row.getCell( COL_INDEX_LEAVE + 1 ) );
	            	if ( cell_2.length() > 0) {
                		accd.addDeduction( K_LEAVE, cell_1, cell_2 );
                	}
	            }
			}
    		excelFileInputStream.close( );
		} catch( IOException ioe ) {
			System.out.println( "xms.bill.AccidentList>>" + ioe.getMessage() );
			ioe.printStackTrace( );
		} catch( Exception e ) {
			System.out.println( "xms.bill.AccidentList>>" + e.getMessage() );
			e.printStackTrace( );
		} finally {
		}
	}
	
	public void printDeduction( ) {
		for ( int j=0; j<arrListAccident.size(); j++ ) {
			Accident accd = arrListAccident.get( j );
			StringBuilder bufDeduction = new StringBuilder( );
			bufDeduction.append( j ).append( "=" ).append( accd.getItemValue( T_ID ) ).append( ">>");
			for ( int h=0; h<accd.getDeductionSize(); h++ ) {
				Deduction d = accd.getDeduction( h );
				bufDeduction.append( d.getKind() ).append( "=").append( d.getName() ).append( "=" ).append( d.getAmount().floatValue() ).append( "&" );
			}
			System.out.println( bufDeduction.toString() );
		}
		System.out.println( "Deduction Finished" );
	}
	
	public void printErrorDeduction( ) {
		for ( int j=0; j<arrListAccident.size(); j++ ) {
			Accident accd = arrListAccident.get( j );
			accd.deductionError( );
		}
		System.out.println( "DeductionError Finished" );
	}
	
	public BigDecimal getColumnSum( int kindTag ) {
		BigDecimal decimal = new BigDecimal( 0 );
		for ( int j=0; j<arrListAccident.size(); j++ ) {
			Accident accd = arrListAccident.get( j );
			for ( int h=0; h<accd.getDeductionSize(); h++ ) {
				Deduction d = accd.getDeduction( h );
				if ( d.getKind( ) == kindTag ) {
					decimal = decimal.add( d.getAmount() );
				}
			}
		}
		return decimal;
	} 
	
	public void exportXLS( String xlsPath ) { 
		XSSFWorkbook wb = null;                     	
		XSSFSheet sheet = null;
		XSSFRow row = null;
		XSSFCell cell = null;
		
		int[] COL_WIDTH_1 = { 20, 60, 90, 200, 300, 90	};
		int[] COL_WIDTH_2 = { 20, 60, 200, 100, 100, 100, 100, 100 };
		String[] COL_TAG_1 = { "序号", "扣款类别", "事故报告编号", "被扣款人", "扣款金额"	};
		String[] COL_TAG_2 = { "序号", "事故报告编号", TOTAL_DEDUCTION, T_INSURE, T_COMPANY, T_PROVIDER_2, T_EMPLOYEE	};
		
		try { 
			wb = new XSSFWorkbook( );			
			sheet = wb.createSheet( );
			wb.setSheetName( 0, "扣款明细" ); 
			int pos_y = 0;
			
			for ( int j=0; j<COL_WIDTH_1.length; j++ ) {
				sheet.setColumnWidth((short)j, (short)35.7*COL_WIDTH_1[j] );
			}
			
			row = sheet.createRow( pos_y++ );
			row.setHeight( (short)(15.625*25) );
			
			for ( int j=0; j<COL_TAG_1.length; j++ ) {
				cell = row.createCell( j+1 );
				cell.setCellStyle( PoiUtils.getTitleStyle(wb,false,false) );
				cell.setCellValue( new XSSFRichTextString( COL_TAG_1[j] ) );
			}
			
			for ( int j=0; j<arrListAccident.size(); j++ ) {
				Accident accd = arrListAccident.get( j );
				for ( int h=0; h<accd.getDeductionSize(); h++ ) {
					Deduction d = accd.getDeduction( h );
					row = sheet.createRow( pos_y++ );
					row.setHeight( (short)(15.625*25) );
					
					cell = row.createCell( 1 );
					cell.setCellStyle( PoiUtils.getNormalCenterStyle(wb) );
					cell.setCellValue( pos_y-1 );
					
					cell = row.createCell( 2 );
					cell.setCellStyle( PoiUtils.getNormalCenterStyle(wb) );
					cell.setCellValue( d.getKind() );
					
					cell = row.createCell( 3 );
					cell.setCellStyle( PoiUtils.getNormalCenterStyle(wb) );
					cell.setCellValue( accd.accID );
					
					cell = row.createCell( 4 );
					cell.setCellStyle( PoiUtils.getNormalCenterStyle(wb) );
					cell.setCellValue( d.getName() );
					
					cell = row.createCell( 5 );
					cell.setCellStyle( PoiUtils.getNormalRightStyle(wb) );
					cell.setCellValue( DecimalUtils.formatQty(d.getAmount()) );
				}
			}
            
			sheet = wb.createSheet( );
			wb.setSheetName( 1, "扣款汇总" ); 
			pos_y = 0;
			
			for ( int j=0; j<COL_WIDTH_2.length; j++ ) {
				sheet.setColumnWidth((short)j, (short)35.7*COL_WIDTH_2[j] );
			}
			
			row = sheet.createRow( pos_y++ );
			row.setHeight( (short)(15.625*25) );
			
			for ( int j=0; j<COL_TAG_2.length; j++ ) {
				cell = row.createCell( j+1 );
				cell.setCellStyle( PoiUtils.getTitleStyle(wb,false,false) );
				cell.setCellValue( new XSSFRichTextString( COL_TAG_2[j] ) );
			}
			
			for ( int j=0; j<arrListAccident.size(); j++ ) {
				row = sheet.createRow( pos_y++ );
				row.setHeight( (short)(15.625*25) );
				Accident accd = arrListAccident.get( j );
					
				cell = row.createCell( 1 );
				cell.setCellStyle( PoiUtils.getNormalCenterStyle(wb) );
				cell.setCellValue( pos_y-1 );
				
				cell = row.createCell( 2 );
				cell.setCellStyle( PoiUtils.getNormalCenterStyle(wb) );
				cell.setCellValue( accd.getID() );
				
				for ( int h=2; h<COL_TAG_2.length; h++ ) {
					cell = row.createCell( h+1 );
					cell.setCellStyle( PoiUtils.getNormalRightStyle(wb) );
					cell.setCellValue( accd.getItemValue( COL_TAG_2[h] ) );
				}
			}
			
    		FileOutputStream excelFileOutPutStream = new FileOutputStream( new File(xlsPath) );
    		excelFileOutPutStream.flush();
    		wb.write( excelFileOutPutStream );
    		excelFileOutPutStream.close();
		} catch( IOException ioe ) {
			System.out.println( ioe.getMessage() );
			ioe.printStackTrace( );
		} catch( Exception e ) {
			System.out.println( e.getMessage() );
			e.printStackTrace( );
		} finally {
		}
	}
	
	/**
	 * Main Test
	 */
	public static void main(String[] args) {
		AccidentList accidentList = new AccidentList( "D:\\Temporary\\20210322115800039.xlsx" );

//		财务黄孝梅分析事故报告
//		accidentList.analyseAccidentXLS( );
//		//accidentList.printErrorDeduction();
//		System.out.println( accidentList.getColumnSum( K_PROVIDER ) );
//		System.out.println( accidentList.getColumnSum( K_DUTY_0 ) );
//		System.out.println( accidentList.getColumnSum( K_DUTY_1 ) );
//		System.out.println( accidentList.getColumnSum( K_DUTY_2 ) );
//		System.out.println( accidentList.getColumnSum( K_LEAVE ) );
//		accidentList.exportXLS( "D:\\Temporary\\202007-202012_Deduction.xlsx" );
		
//		搜索离职人员发起的事故报告单号
		accidentList.queryIDByName( "贺", 9, 0);

	}
	
}
