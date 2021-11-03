package com.undao.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtils {

	public final static String BORDER_DATE_1 = "2001-01-01";
	public final static String BORDER_DATE_2 = "2049-12-31";
	
	private static SimpleDateFormat fmt_date_1 = new SimpleDateFormat( "yyyy-MM-dd" );
	private static SimpleDateFormat fmt_date_2 = new SimpleDateFormat( "MM/dd/yyyy" );
	private static SimpleDateFormat fmt_yyyymmdd = new SimpleDateFormat( "yyyyMMdd" );
	private static SimpleDateFormat fmt_mmdd = new SimpleDateFormat( "MM-dd" );

	private static DateTimeFormatter fmt_datetime = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );
	private static DateTimeFormatter fmt_datetime_2 = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm" );
	private static DateTimeFormatter fmt_datetime_3 = DateTimeFormatter.ofPattern( "MM-dd HH:mm" );

	
	private static SimpleDateFormat fmt_time = new SimpleDateFormat( "HH:mm:ss" );
	private static SimpleDateFormat fmt_time_2 = new SimpleDateFormat( "HH:mm" );
	private static SimpleDateFormat fmt_HHmmss = new SimpleDateFormat( "HHmmss" );

	private static SimpleDateFormat fmt_year = new SimpleDateFormat( "yyyy" );
	private static SimpleDateFormat fmt_year_zh = new SimpleDateFormat( "yyyy年");

	private static SimpleDateFormat fmt_month_1 = new SimpleDateFormat( "yyyyMM" );
	private static SimpleDateFormat fmt_month_2 = new SimpleDateFormat( "yyyy-MM" );
	private static SimpleDateFormat fmt_month_zh = new SimpleDateFormat( "yyyy年MM月" );

	private static SimpleDateFormat fmt_week_zh = new SimpleDateFormat( "EEEE" ); 	
	private static SimpleDateFormat fmt_week_en = new SimpleDateFormat( "EEE", Locale.ENGLISH ); 	
		   
    /**
     * About Date - Build 
     */
    public static final Date buildDate( String strDate ) {			//Convert 
        try {
        	if ( strDate.indexOf("-") >= 0 ) {
        		return fmt_date_1.parse( strDate );
        	} else if ( strDate.indexOf( "/" ) >= 0 ) {
        		return fmt_date_2.parse( strDate );
        	} else {
        		return fmt_yyyymmdd.parse( strDate );
        	}
        } catch( ParseException pe ) {
        	System.out.println( "DateUtils.buildDate(String)>> " + pe.getMessage() );
        }      
        return null;
    }
    
    public static final GregorianCalendar buildCalendar( String strDate ) {
    	try {   
    		GregorianCalendar calendar = new GregorianCalendar( );
    		if ( strDate.indexOf("-") >= 0 ) {
    			calendar.setTime( fmt_date_1.parse( strDate ) );
        	} else if ( strDate.indexOf( "/" ) >= 0 ) {
        		calendar.setTime( fmt_date_2.parse( strDate ) );
        	} else {
        		calendar.setTime( fmt_yyyymmdd.parse( strDate ) );
        	}
    		return calendar;
        } catch( ParseException pe ) { 
        	System.out.println( "DateUtils.buildCalendar(String)>> " + pe.getMessage() );
        }      
        return null;
    }
    
    public static final GregorianCalendar buildCalendar( Date date ) {
        GregorianCalendar calendar = new GregorianCalendar( );
        calendar.setTime( date );
        return calendar;
        
    }
    
    /**
     * About Date - Special 
     */
    public static final String formatFirstOfCurrentWeek( ) {		//本周周一
    	Calendar calendar = Calendar.getInstance( ); 
    	calendar.setTime( new Date() );
    	int step = 1 + 1 - calendar.get(Calendar.DAY_OF_WEEK);
    	calendar.add( Calendar.DATE, step );
    	return fmt_date_1.format( calendar.getTime() );
    }       
    
    public static final String formatFirstOfPreviousWeek( ) {		//上周周一
    	Calendar calendar = Calendar.getInstance( ); 
    	calendar.setTime( new Date() );
    	int step = 2 - 7 - calendar.get(Calendar.DAY_OF_WEEK);
    	calendar.add(Calendar.DATE, step );
    	return fmt_date_1.format( calendar.getTime() );
    }
    
    public static final String formatLastOfPreviousWeek( ) {  		//上周周末
    	Calendar calendar = Calendar.getInstance( );   
    	calendar.setTime( new Date() );       
    	int step = 1 - calendar.get(Calendar.DAY_OF_WEEK);
    	calendar.add( Calendar.DATE, step ); 
    	return fmt_date_1.format( calendar.getTime() );
    }

	public static final String formatFirstOfCurrentMonth( ) {		//本月1号
		Calendar calendar = Calendar.getInstance( );
		calendar.setTime( new Date() );
		calendar.set( Calendar.DATE, 1 );
		return fmt_date_1.format( calendar.getTime() );
	}

    public static final String formatFirstOfPreviousMonth( ) {		//上月1号
    	Calendar calendar = Calendar.getInstance( ); 
    	calendar.setTime( new Date() );
    	calendar.add(Calendar.MONTH, -1 );
    	calendar.set( Calendar.DATE, 1 );
    	return fmt_date_1.format( calendar.getTime() );
    }
    
    public static final String formatLastOfPreviousMonth( ) {		//上月月末
    	Calendar calendar = Calendar.getInstance( );   
    	calendar.setTime( new Date() );   
    	calendar.set( Calendar.DATE, 1 );      
    	calendar.add( Calendar.DATE, -1 ); 
    	return fmt_date_1.format( calendar.getTime() );
    }

    public static final String formatFirstOfNextMonth( ) {			//下月1号
    	Calendar calendar = Calendar.getInstance( );   
    	calendar.setTime( new Date() );   
    	calendar.add( Calendar.MONTH, 1 );
    	calendar.set( Calendar.DATE, 1 );      
    	return fmt_date_1.format( calendar.getTime() );
    }
    
    public static final String formatLastOfNextMonth( ) {			//下月月末
    	Calendar calendar = Calendar.getInstance( );   
    	calendar.setTime( new Date() );   
    	calendar.add( Calendar.MONTH, 2 );
    	calendar.set( Calendar.DATE, 1 );      
    	calendar.add( Calendar.DATE, -1 ); 
    	return fmt_date_1.format( calendar.getTime() );
    }

	public static final String formatFirstOfCurrentYear( ) {		//今年1月1号
		Calendar calendar = Calendar.getInstance( );
		calendar.setTime( new Date() );
		calendar.set( Calendar.MONTH, 0 );
		calendar.set( Calendar.DATE, 1 );
		return fmt_date_1.format( calendar.getTime() );
	}

    public static final String formatFirstOfPreviousYear( ) {		//去年1月1号
    	Calendar calendar = Calendar.getInstance( ); 
    	calendar.setTime( new Date() );
    	calendar.add(Calendar.YEAR, -1 );
    	calendar.set( Calendar.MONTH, 0 );
    	calendar.set( Calendar.DATE, 1 );
    	return fmt_date_1.format( calendar.getTime() );
    }
    
    public static final String formatLastOfPreviousYear( ) {		//去年12月31号
    	Calendar calendar = Calendar.getInstance( );   
    	calendar.setTime( new Date() );
    	calendar.set( Calendar.MONTH, 0 );
    	calendar.set( Calendar.DATE, 1 );      
    	calendar.add( Calendar.DATE, -1 ); 
    	return fmt_date_1.format( calendar.getTime() );
    }

    /**
     * About Date - Step 
     */
    public static final String formatDateStepDays( int interval ) {							//以当前日期为起始，加减天数
		GregorianCalendar calendar = new GregorianCalendar( );
		calendar.add( Calendar.DATE, interval );
		return fmt_date_1.format( calendar.getTime() );
	}

	public static final String formatPreviousDate( String interval ) {						//以当前日期为起始，减天数
    	return formatDate( Integer.parseInt(interval)*(-1) );
	}

    public static final String formatDateStepDays( String startDate, int interval ) {		//以指定日期为起始，加减天数
    	GregorianCalendar calendar = buildCalendar( startDate );
		calendar.add( Calendar.DATE, interval );
		return fmt_date_1.format( calendar.getTime() );
	}

	public static final String formatDateStepMonths( int interval ) {						//以当前日期为起始，加减月数
		Calendar calendar = Calendar.getInstance( );
		calendar.setTime( new Date() );
		calendar.add( Calendar.MONTH, interval );
		return fmt_date_1.format( calendar.getTime() );
	}

    public static final String formatDateStepYears( int interval ) {						//以当前日期为起始，加减年数
    	Calendar calendar = Calendar.getInstance( ); 
    	calendar.setTime( new Date() );
    	calendar.add( Calendar.YEAR, interval );
    	return fmt_date_1.format( calendar.getTime() );
    }

    /**
     * About Date - 格式化日期
     */
    public static final String formatCurrentDate( ) {				//当天
        return fmt_date_1.format( new Date( ) );
    }
    
    public static final String formatCur_yyyymmdd( ) {				//当天
    	return fmt_yyyymmdd.format( new Date( ) );
    }
    
    public static final String formatDate( Object date ) {    	
        return date == null ? "" : fmt_date_1.format( (Date)date );
    }

    
    public static final String formatDate( Object date, boolean displayBorder ) {
    	if ( date == null ) {
    		return "";
    	}
    	String display = fmt_date_1.format( (Date)date );
    	if ( !displayBorder ) {
    	 	if ( display.equals( BORDER_DATE_1 ) || display.equals( BORDER_DATE_2 ) ) {
    			return "";
    		}    		
    	}
    	return display;
    }
    
    public static final String formatDate( Object date, boolean displayBorder, String placeDate ) {
    	if ( date == null ) {
    		return "";
    	}
    	String display = fmt_date_1.format( (Date)date );
    	if ( !displayBorder ) {
    	 	if ( display.equals( BORDER_DATE_1 ) || display.equals( BORDER_DATE_2 ) ) {
    			return placeDate;
    		}    		
    	}
    	return display;
    }
    
    public static final String format_mmdd( Object date ) {
        return date == null ? "" : fmt_mmdd.format( (Date)date );
    }
    
    public static final String format_mmdd( Object date, boolean displayBorder ) {
    	if ( date == null ) {
    		return "";
    	}
    	String display = fmt_date_1.format( (Date)date );
    	if ( !displayBorder ) {
    	 	if ( display.equals( BORDER_DATE_1 ) || display.equals( BORDER_DATE_2 ) ) {
    			return "";
    		}    		
    	}
    	return fmt_mmdd.format( (Date)date );
    }
       
    /**
     * About Date - Time 
     */
    public static final String formatCurrentTime( ) {				//Time
        return fmt_time.format( new Date( ) );
    }
    public static final String formatCur_HHmmss( ) {				//Time
        return fmt_HHmmss.format( new Date( ) );
    }
    
    public static final String formatCurrentTime2( ) {				//Time
        return fmt_time_2.format( new Date( ) );
    }
    
    public static final String formatTime( Object date ) {    	
        return date == null ? "" : fmt_time.format( (Date)date );
    }
    
    public static final String formatTime( Object date, boolean displayBorder ) {
    	if ( date == null ) {
    		return "";
    	}
    	String display = fmt_time.format( (Date)date );
    	if ( !displayBorder ) {
    	 	if ( display.equals( "00:00:01" ) ) {
    			return "";
    		}    		
    	}
    	return display;
    }
    
    /**
     * About Date - DateTime 
     */
    public static final String formatCurrentDateTime( ) {			//Datetime
        return fmt_datetime.format( LocalDateTime.now() );
    }
    
    public static final String formatDateTime( Object date ) {    	
        return date == null ? "" : fmt_datetime.format( (LocalDateTime)date );
    }
    
    public static final String formatDateTime( Object date, boolean displayBorder ) {
    	if ( date == null ) {
    		return "";
    	}
    	String display = fmt_datetime.format( (LocalDateTime)date );
    	if ( !displayBorder ) {
    	 	if ( display.equals( "2001-01-01 00:00:01" ) || display.indexOf("2049")>=0 ) {
    			return "";
    		}    		
    	}
    	return display;
    }
    
    public static final String formatDateTime2( Object date ) {
        return date == null ? "" : fmt_datetime_2.format( (LocalDateTime)date );
    }
    
    public static final String formatDateTime2( Object date, boolean displayBorder ) {
    	if ( date == null ) {
    		return "";
    	}
    	String display = fmt_datetime_2.format( (LocalDateTime)date );
    	if ( !displayBorder ) {
    	 	if ( display.equals( "2001-01-01 00:00:01" ) || display.indexOf("2049")>=0  ) {
    			return "";
    		}    		
    	}
    	return display;
    }
    
    public static final String formatDateTime3( Object date ) {
        return date == null ? "" : fmt_datetime_3.format( (LocalDateTime)date );
    }
    
    public static final String formatDateTime3( Object date, boolean displayBorder ) {
    	if ( date == null ) {
    		return "";
    	}
    	String tDisplay = fmt_year.format( (Date)date );
    	if ( !displayBorder ) {
    	 	if ( tDisplay.equals( "2001" ) || tDisplay.equals( "2049" ) ) {
    			return "";
    		}    		
    	}
    	return fmt_datetime_3.format( (LocalDateTime)date );
    }
    
    /**
     * About Date - Day
     */
    public static final int getLastDayOfCurrentMonth( ) {    	//获得当月最末一天
    	Calendar calendar = Calendar.getInstance( );   
    	calendar.setTime( new Date() );   
    	calendar.add( Calendar.MONTH, 1 );
    	calendar.set( Calendar.DATE, 1 );      
    	calendar.add( Calendar.DATE, -1 ); 
    	return calendar.get( Calendar.DAY_OF_MONTH );
    }
    
    public static final int getLastDayOfPreviousMonth( ) {    	//获得上月月最末一天
    	Calendar calendar = Calendar.getInstance( );   
    	calendar.setTime( new Date() );   
    	calendar.set( Calendar.DATE, 1 );      
    	calendar.add( Calendar.DATE, -1 ); 
    	return calendar.get( Calendar.DAY_OF_MONTH );
    }
    
    public static final int getLastDayOfNextMonth( ) {    		//获得下月最后一天
    	Calendar calendar = Calendar.getInstance( );   
    	calendar.setTime( new Date() );   
    	calendar.add( Calendar.MONTH, 2 );
    	calendar.set( Calendar.DATE, 1 );      
    	calendar.add( Calendar.DATE, -1 ); 
    	return calendar.get( Calendar.DAY_OF_MONTH );
    }
    
    /**
     * About Date - Week 
     */
    public static final String formatWeekZh( Object date ) {				//星期几(中文)
        return date == null ? "" : fmt_week_zh.format( (Date)date );
    } 
    
    public static final String formatWeekEn( Object date ) {				//星期几(英文)
        return date == null ? "" : fmt_week_en.format( (Date)date );
    } 
    
    public static final int getWeekDay( String strDate ) {					//星期中的第几天
    	try {   
    		GregorianCalendar calendar = new GregorianCalendar( );
    		if ( strDate.indexOf("-") >= 0 ) {
    			calendar.setTime( fmt_date_1.parse( strDate ) );
        	} else if ( strDate.indexOf( "/" ) >= 0 ) {
        		calendar.setTime( fmt_date_2.parse( strDate ) );
        	} else {
        		calendar.setTime( fmt_yyyymmdd.parse( strDate ) );
        	}
    		return calendar.get( Calendar.DAY_OF_WEEK );
        } catch( ParseException pe ) { 
        	System.out.println( "DateUtils.getWeekDay(String)>> " + pe.getMessage() );
        }      
        return -1;
    }
    
    public static final String formatWeekTag( int weekDay ) {				//得出对应的中文星期几
		String result = null;
		switch ( weekDay ) {
			case 1 : result = "日"; break;
			case 2 : result = "一"; break;
			case 3 : result = "二"; break;
			case 4 : result = "三"; break;
			case 5 : result = "四"; break;
			case 6 : result = "五"; break;
			case 7 : result = "六"; break;
			default : result = "";
		}
		return result;
	}
    
    /**
     * About Date - Month 
     */
    public static final String formatCurrentMonthZh( ) {					//当前月份
		return fmt_month_zh.format( new Date( ) );
	}

	public static final String formatCurrentMonth1( ) {						//当前月份
		return fmt_month_1.format( new Date() );
	}

	public static final String formatCurrentMonth2( ) {						//当前月份
		return fmt_month_2.format( new Date() );
	}

	public static final String formatPreviousMonth1( ) {					//上月月份
		Calendar calendar = Calendar.getInstance( );
		calendar.setTime( new Date() );
		calendar.add(Calendar.MONTH, -1 );
		return fmt_month_1.format( calendar.getTime() );
	}

	public static final String formatPreviousMonth2( ) {					//上月月份
		Calendar calendar = Calendar.getInstance( );
		calendar.setTime( new Date() );
		calendar.add(Calendar.MONTH, -1 );
		return fmt_month_2.format( calendar.getTime() );
	}

	public static final String formatMonthZh( Calendar calendar ) {
		return fmt_month_zh.format( calendar.getTime( ) );
	}
	
	public static final String formatMonth1( Calendar calendar ) {
		return fmt_month_1.format( calendar.getTime( ) );
	}

	public static final String formatMonth2Step( boolean ifCalendarDay, int interval ) {		//以当前日期为起始，增加天数或月数
    	Calendar calendar = Calendar.getInstance( ); 
    	calendar.setTime( new Date() );
    	calendar.add( ifCalendarDay ? Calendar.DATE : Calendar.MONTH, interval );
    	return fmt_month_2.format( calendar.getTime() );
    }

	/**
     * About Date - Year 
     */
	public static final String formatCurrentYear( ) {						//Year
		return fmt_year.format( new Date( ) );
	}

	public static final String formatYear( Calendar calendar ) {
		return fmt_year.format( calendar.getTime( ) );
	}

	public static final String formatYearZh( Calendar calendar ) {
		return fmt_year_zh.format( calendar.getTime( ) );		
	}
    
	/**
     * About Date - Calc 
     */
    public static final int calcDaysBetween( Date date1, Date date2 ) {			//两个日期之间的天数
    	if ( date2.before( date1 ) ) {
			return -1;
		}
        long interval = (date2.getTime()-date1.getTime())/86400000;		//24*3600*1000
        return (int)interval;
    }
    
    public static final int calcDaysBetween( String date1, String date2 ) {		//两个日期之间的天数
		try {
			Date d1 = fmt_date_1.parse( date1 );
			Date d2 = fmt_date_1.parse( date2 );
			return calcDaysBetween( d1, d2 );
		} catch( ParseException e ) {
			return -9;
		}
	}
    
    public static final int calcMinutesBetween( int hour, int minute ) {		//当前时间距指定时间的分钟数
    	Calendar ca = Calendar.getInstance( ); 
    	ca.setTime( new java.util.Date() );		//得到当前时间 
    	ca.set( Calendar.HOUR_OF_DAY, hour );
    	ca.set( Calendar.MINUTE, minute );
    	
    	return (int)Math.ceil( (ca.getTimeInMillis() - System.currentTimeMillis())/60000 );	//1000*60
    }
    
    public static final int calcMinutesBetween( String time ) { 				////当前时间距指定时间的分钟数，接受格式HHMM
    	Calendar ca = Calendar.getInstance( ); 
    	ca.setTime( new java.util.Date() );		//得到当前时间 
    	try {
			ca.set( Calendar.HOUR_OF_DAY, Integer.parseInt( time.substring(0, 2)) );
			ca.set( Calendar.MINUTE, Integer.parseInt( time.substring(2,4)) );
		} catch (NumberFormatException nfe) {
			System.out.println( "DateUtils.calcMinutesBetween(time)>>" + nfe.getMessage() );
			return 9999;
		}
    	
    	return (int)Math.ceil( (ca.getTimeInMillis()-System.currentTimeMillis())/60000 );	//1000*60
    }
    
    public static final int calcMinutesBetween( String time1, String time2 ) { 	//两个时间之间的分钟数，接受格式HHMM
    	Date nowdate = new java.util.Date( );	//得到当前时间 
    	Calendar ca1 = Calendar.getInstance( ); 
    	ca1.setTime( nowdate );
    	Calendar ca2 = Calendar.getInstance( ); 
    	ca2.setTime( nowdate );
    	try {
			ca1.set( Calendar.HOUR_OF_DAY, Integer.parseInt( time1.substring(0, 2)) );
			ca1.set( Calendar.MINUTE, Integer.parseInt( time1.substring(2,4)) );
			ca2.set( Calendar.HOUR_OF_DAY, Integer.parseInt( time2.substring(0, 2)) );
			ca2.set( Calendar.MINUTE, Integer.parseInt( time2.substring(2,4)) );
		} catch (NumberFormatException nfe) {
			System.out.println( "DateUtils.calcMinutesBetween(time1,time2)>>" + nfe.getMessage() );
			return 9999;
		}
    	
    	return (int)Math.ceil( (ca2.getTimeInMillis()-ca1.getTimeInMillis())/60000 );	//1000*60
    }

	/**
	 * MAIN TEST
	 */
	public static void main( String[] args ) {
		System.out.println( DateUtils.calcMinutesBetween( "0940" ) );
		System.out.println( DateUtils.calcMinutesBetween( "0940","1840" ) );
		System.out.println( DateUtils.getLastDayOfCurrentMonth() );

		System.out.println( DateUtils.formatCurrentDateTime() );
	}


}