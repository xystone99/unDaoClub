package com.undao.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class DateUtils {

	public final static String RTN_BLANK = "";
	public final static String BORDER_DATE_1 = "2001-01-01";
	public final static String BORDER_DATE_2 = "2049-12-31";
	public final static String BORDER_TIME = "00:00:01";
	public final static String BORDER_DATETIME = "2001-01-01 00:00:01";
	public final static String BORDER_YEAR_1 = "2001";
	public final static String BORDER_YEAR_2 = "2049";
	public final static String[] WEEK_TAG = { "一", "二", "三", "四", "五", "六", "日" };

	private static DateTimeFormatter fmt_date_1 = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );
	private static DateTimeFormatter fmt_date_2 = DateTimeFormatter.ofPattern( "MM/dd/yyyy" );
	private static DateTimeFormatter fmt_yyyymmdd = DateTimeFormatter.ofPattern( "yyyyMMdd" );
	private static DateTimeFormatter fmt_mmdd = DateTimeFormatter.ofPattern( "MM-dd" );

	private static DateTimeFormatter fmt_datetime = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );
	private static DateTimeFormatter fmt_datetime_2 = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm" );
	private static DateTimeFormatter fmt_datetime_3 = DateTimeFormatter.ofPattern( "MM-dd HH:mm" );

	private static DateTimeFormatter fmt_time = DateTimeFormatter.ofPattern( "HH:mm:ss" );
	private static DateTimeFormatter fmt_time_2 = DateTimeFormatter.ofPattern( "HH:mm" );
	private static DateTimeFormatter fmt_HHmmss = DateTimeFormatter.ofPattern( "HHmmss" );
	private static DateTimeFormatter fmt_HHmm = DateTimeFormatter.ofPattern( "HHmm" );

	private static DateTimeFormatter fmt_year = DateTimeFormatter.ofPattern( "yyyy" );
	private static DateTimeFormatter fmt_year_zh = DateTimeFormatter.ofPattern( "yyyy年");

	private static DateTimeFormatter fmt_month_1 = DateTimeFormatter.ofPattern( "yyyyMM" );
	private static DateTimeFormatter fmt_month_2 = DateTimeFormatter.ofPattern( "yyyy-MM" );
	private static DateTimeFormatter fmt_month_zh = DateTimeFormatter.ofPattern( "yyyy年MM月" );

	private static DateTimeFormatter fmt_week_zh = DateTimeFormatter.ofPattern( "EEEE" );
	private static DateTimeFormatter ffmt_week_en = DateTimeFormatter.ofPattern( "EEE", Locale.ENGLISH );
		   
    /**
     * About Date - Build 
     */
    public static final LocalDate buildDate( String strDate ) {			//Convert
        if ( strDate.indexOf("-") >= 0 ) {
        	return LocalDate.parse( strDate, fmt_date_1 );
        } else if ( strDate.indexOf( "/" ) >= 0 ) {
        	return LocalDate.parse( strDate, fmt_date_2 );
        } else {
        	return LocalDate.parse( strDate, fmt_yyyymmdd );
        }
    }
    
    /**
     * About Date - Special 
     */
    public static final String formatFirstOfCurrentWeek( ) {		//本周周一
    	LocalDate locDate = LocalDate.now( );
		return fmt_date_1.format( locDate.plusDays( 1 - locDate.get( ChronoField.DAY_OF_WEEK ) ) );
    }       
    
    public static final String formatFirstOfPreviousWeek( ) {		//上周周一
		LocalDate locDate = LocalDate.now( );
		return fmt_date_1.format( locDate.plusDays( 1 - 7 - locDate.get( ChronoField.DAY_OF_WEEK ) ) );
    }
    
    public static final String formatLastOfPreviousWeek( ) {  		//上周周末
		LocalDate locDate = LocalDate.now( );
		return fmt_date_1.format( locDate.plusDays( 1 - 1 - locDate.get( ChronoField.DAY_OF_WEEK ) ) );
    }

	public static final String formatFirstOfCurrentMonth( ) {		//本月1号
		LocalDate locDate = LocalDate.now( );
		return fmt_date_1.format( locDate.plusDays( 1 - locDate.get( ChronoField.DAY_OF_MONTH ) ) );
	}

    public static final String formatFirstOfPreviousMonth( ) {		//上月1号
		LocalDate locDate = LocalDate.now( ).plusMonths( -1 );
		return fmt_date_1.format( locDate.plusDays( 1 - locDate.get( ChronoField.DAY_OF_MONTH ) ) );
    }
    
    public static final String formatLastOfPreviousMonth( ) {		//上月月末
		LocalDate locDate = LocalDate.now( );
		return fmt_date_1.format( locDate.plusDays( 1 - 1 - locDate.get( ChronoField.DAY_OF_MONTH ) ) );
    }

    public static final String formatFirstOfNextMonth( ) {			//下月1号
		LocalDate locDate = LocalDate.now( ).plusMonths( 1 );
		return fmt_date_1.format( locDate.plusDays( 1 - locDate.get( ChronoField.DAY_OF_MONTH ) ) );
    }
    
    public static final String formatLastOfNextMonth( ) {			//下月月末
    	LocalDate locDate = LocalDate.now( ).plusMonths( 2 );
		return fmt_date_1.format( locDate.plusDays( 1 - 1 - locDate.get( ChronoField.DAY_OF_MONTH ) ) );
    }

	public static final String formatFirstOfCurrentYear( ) {		//今年1月1号
		LocalDate locDate = LocalDate.now( );
		return fmt_date_1.format( locDate.plusDays( 1 - locDate.get( ChronoField.DAY_OF_YEAR ) ) );
	}

    public static final String formatFirstOfPreviousYear( ) {		//去年1月1号
		LocalDate locDate = LocalDate.now( ).plusYears( -1 );
		return fmt_date_1.format( locDate.plusDays( 1 - locDate.get( ChronoField.DAY_OF_YEAR ) ) );
    }
    
    public static final String formatLastOfPreviousYear( ) {		//去年12月31号
		LocalDate locDate = LocalDate.now( );
		return fmt_date_1.format( locDate.plusDays( 1 - 1 - locDate.get( ChronoField.DAY_OF_YEAR ) ) );
    }

    /**
     * About Date - Step 
     */
    public static final String formatDateStepDays( int interval ) {							//以当前日期为起始，加减天数
		return fmt_date_1.format( LocalDate.now().plusDays( interval ) );
	}

    public static final String formatDateStepDays( String startDate, int interval ) {		//以指定日期为起始，加减天数
    	LocalDate stDate = buildDate( startDate );
		return fmt_date_1.format( stDate.plusDays( interval ) );
	}

	public static final String formatDateStepMonths( int interval ) {						//以当前日期为起始，加减月数
		return fmt_date_1.format( LocalDate.now().plusMonths( interval ) );
	}

    public static final String formatDateStepYears( int interval ) {						//以当前日期为起始，加减年数
		return fmt_date_1.format( LocalDate.now().plusYears( interval ) );
    }

    /**
     * About Date - 格式化日期
     */
    public static final String formatCurrentDate( ) {				//当天
        return fmt_date_1.format( LocalDate.now() );
    }
    
    public static final String formatCur_yyyymmdd( ) {				//当天
    	return fmt_yyyymmdd.format( LocalDate.now() );
    }
    
    public static final String formatDate( Object date ) {
        return date == null ? RTN_BLANK : fmt_date_1.format( ((java.sql.Date)date).toLocalDate() );
    }

    public static final String formatDate( Object date, boolean displayBorder ) {
    	if ( date == null ) {
    		return RTN_BLANK;
    	}
    	String display = fmt_date_1.format( ((java.sql.Date)date).toLocalDate() );
    	if ( !displayBorder ) {
    	 	if ( display.equals( BORDER_DATE_1 ) || display.equals( BORDER_DATE_2 ) ) {
    			return RTN_BLANK;
    		}    		
    	}
    	return display;
    }
    
    public static final String formatDate( Object date, boolean displayBorder, String placeDate ) {
    	if ( date == null ) {
    		return RTN_BLANK;
    	}
    	String display = fmt_date_1.format( ((java.sql.Date)date).toLocalDate() );
    	if ( !displayBorder ) {
    	 	if ( display.equals( BORDER_DATE_1 ) || display.equals( BORDER_DATE_2 ) ) {
    			return placeDate;
    		}    		
    	}
    	return display;
    }
    
    public static final String format_mmdd( Object date ) {
        return date == null ? RTN_BLANK : fmt_mmdd.format( ((java.sql.Date)date).toLocalDate() );
    }
    
    public static final String format_mmdd( Object date, boolean displayBorder ) {
    	if ( date == null ) {
    		return RTN_BLANK;
    	}
		LocalDate locDate = ((java.sql.Date)date).toLocalDate();
    	String display = fmt_date_1.format( locDate );
    	if ( !displayBorder ) {
    	 	if ( display.equals( BORDER_DATE_1 ) || display.equals( BORDER_DATE_2 ) ) {
    			return RTN_BLANK;
    		}    		
    	}
    	return fmt_mmdd.format( locDate );
    }
       
    /**
     * About Date - Time 
     */
    public static final String formatCurrentTime( ) {				//Time
        return fmt_time.format( LocalDateTime.now() );
    }
    public static final String formatCur_HHmmss( ) {				//Time
        return fmt_HHmmss.format( LocalDateTime.now() );
    }
    
    public static final String formatCurrentTime2( ) {				//Time
        return fmt_time_2.format( LocalDateTime.now() );
    }
    
    public static final String formatTime( Object date ) {    	
        return date == null ? RTN_BLANK : fmt_time.format( ((java.sql.Date)date).toLocalDate() );
    }
    
    public static final String formatTime( Object date, boolean displayBorder ) {
    	if ( date == null ) {
    		return RTN_BLANK;
    	}
    	String display = fmt_time.format( ((java.sql.Date)date).toLocalDate() );
    	if ( !displayBorder ) {
    	 	if ( display.equals( BORDER_TIME ) ) {
    			return RTN_BLANK;
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
    		return RTN_BLANK;
    	}
    	String display = fmt_datetime.format( (LocalDateTime)date );
    	if ( !displayBorder ) {
    	 	if ( display.equals( BORDER_DATETIME ) || display.indexOf( BORDER_YEAR_2 )>=0 ) {
    			return RTN_BLANK;
    		}    		
    	}
    	return display;
    }
    
    public static final String formatDateTime2( Object date ) {
        return date == null ? RTN_BLANK : fmt_datetime_2.format( (LocalDateTime)date );
    }
    
    public static final String formatDateTime2( Object date, boolean displayBorder ) {
    	if ( date == null ) {
    		return RTN_BLANK;
    	}
    	String display = fmt_datetime_2.format( (LocalDateTime)date );
    	if ( !displayBorder ) {
    	 	if ( display.equals( BORDER_DATETIME ) || display.indexOf( BORDER_YEAR_2 )>=0  ) {
    			return RTN_BLANK;
    		}    		
    	}
    	return display;
    }
    
    public static final String formatDateTime3( Object date ) {
        return date == null ? RTN_BLANK : fmt_datetime_3.format( (LocalDateTime)date );
    }
    
    public static final String formatDateTime3( Object date, boolean displayBorder ) {
    	if ( date == null ) {
    		return RTN_BLANK;
    	}
    	String tDisplay = fmt_year.format( ((java.sql.Date)date).toLocalDate() );
    	if ( !displayBorder ) {
    	 	if ( tDisplay.equals( BORDER_YEAR_1 ) || tDisplay.equals( BORDER_YEAR_2 ) ) {
    			return "";
    		}    		
    	}
    	return fmt_datetime_3.format( (LocalDateTime)date );
    }
    
    /**
     * About Date - Day
     */
    public static final int getLastDayOfCurrentMonth( ) {    	//获得当月最末一天
    	LocalDate locDate = LocalDate.now().plusMonths( 1 );
		return locDate.plusDays( 1 - 1 - locDate.get( ChronoField.DAY_OF_MONTH ) ).get( ChronoField.DAY_OF_MONTH );
    }
    
    public static final int getLastDayOfPreviousMonth( ) {    	//获得上月月最末一天
		LocalDate locDate = LocalDate.now();
		return locDate.plusDays( 1 - 1 - locDate.get( ChronoField.DAY_OF_MONTH ) ).get( ChronoField.DAY_OF_MONTH );
    }
    
    public static final int getLastDayOfNextMonth( ) {    		//获得下月最后一天
		LocalDate locDate = LocalDate.now().plusMonths( 2 );
		return locDate.plusDays( 1 - 1 - locDate.get( ChronoField.DAY_OF_MONTH ) ).get( ChronoField.DAY_OF_MONTH );
    }
    
    /**
     * About Date - Week 
     */
	public static final String formatWeekEn( Object date ) {				//星期几(英文)
		if ( date == null ) {
			return RTN_BLANK;
		}
		if ( date instanceof LocalDate ) {
			return ffmt_week_en.format( (LocalDate)date );
		}
		return ffmt_week_en.format( ((java.sql.Date)date).toLocalDate() );
	}

	public static final String formatWeekZh( Object date ) {				//星期几(中文)
    	if ( date == null ) {
    		return RTN_BLANK;
		}
    	if ( date instanceof LocalDate ) {
    		return fmt_week_zh.format( (LocalDate)date );
		}
    	return fmt_week_zh.format( ((java.sql.Date)date).toLocalDate() );
    }

    public static final int getWeekDay( String strDate ) {					//星期中的第几天
    	LocalDate locDate = buildDate( strDate );
    	return locDate.get( ChronoField.DAY_OF_WEEK );
    }
    
    public static final String formatWeekTag( int weekDay ) {				//得出对应的中文星期几
		return WEEK_TAG[weekDay -1];
	}
    
    /**
     * About Date - Month 
     */
    public static final String formatCurrentMonthZh( ) {					//当前月份
		return fmt_month_zh.format( LocalDate.now() );
	}

	public static final String formatCurrentMonth1( ) {						//当前月份
		return fmt_month_1.format( LocalDate.now() );
	}

	public static final String formatCurrentMonth2( ) {						//当前月份
		return fmt_month_2.format( LocalDate.now() );
	}

	public static final String formatPreviousMonth1( ) {					//上月月份
    	return fmt_month_1.format( LocalDate.now().plusMonths( -1 ) );
	}

	public static final String formatPreviousMonth2( ) {					//上月月份
		return fmt_month_2.format( LocalDate.now().plusMonths( -1 ) );
	}
	
	public static final String formatMonth1( Object date ) {
		if ( date instanceof LocalDate ) {
			return fmt_month_1.format( (LocalDate)date );
		} else if ( date instanceof LocalDateTime ) {
			return fmt_month_1.format( (LocalDateTime)date );
		}
		return fmt_month_1.format(((java.sql.Date)date).toLocalDate());
	}

	public static final String formatMonth2( Object date ) {		//以当前日期为起始，增加天数或月数
		if ( date instanceof LocalDate ) {
			return fmt_month_2.format( (LocalDate)date );
		} else if ( date instanceof LocalDateTime ) {
			return fmt_month_2.format( (LocalDateTime)date );
		}
		return fmt_month_2.format(((java.sql.Date)date).toLocalDate());
    }

	public static final String formatMonthZh( Object date ) {
		if ( date instanceof LocalDate ) {
			return fmt_month_zh.format( (LocalDate)date );
		} else if ( date instanceof LocalDateTime ) {
			return fmt_month_zh.format( (LocalDateTime)date );
		}
		return fmt_month_zh.format(((java.sql.Date)date).toLocalDate());
	}

	/**
     * About Date - Year 
     */
	public static final String formatCurrentYear( ) {						//Year
		return fmt_year.format( LocalDate.now() );
	}

	public static final String formatYear( Object date ) {
		if ( date instanceof LocalDate ) {
			return fmt_year.format( (LocalDate)date );
		} else if ( date instanceof LocalDateTime ) {
			return fmt_year.format( (LocalDateTime)date );
		}
		return fmt_year.format(((java.sql.Date)date).toLocalDate());
	}

	public static final String formatYearZh( Object date ) {
		if ( date instanceof LocalDate ) {
			return fmt_year_zh.format( (LocalDate)date );
		} else if ( date instanceof LocalDateTime ) {
			return fmt_year_zh.format( (LocalDateTime)date );
		}
		return fmt_year_zh.format(((java.sql.Date)date).toLocalDate());
	}
    
	/**
     * About Date - Calc 
     */
    public static final int calcDaysBetween( LocalDate date1, LocalDate date2 ) {			//两个日期之间的天数
    	return (int)date1.until( date2, ChronoUnit.DAYS );
    }
    
    public static final int calcDaysBetween( String date1, String date2 ) {		//两个日期之间的天数
    	LocalDate locDate1 = buildDate( date1 );
		LocalDate locDate2 = buildDate( date2 );
		return (int)locDate1.until( locDate2, ChronoUnit.DAYS );
	}
    
    public static final int calcMinutesBetween( int hour, int minute ) {		//当前时间距指定时间的分钟数
    	LocalTime locTime = LocalTime.of( hour, minute );
    	return (int)LocalTime.now().until( locTime, ChronoUnit.MINUTES );
    }
    
    public static final int calcMinutesBetween( String time ) { 				////当前时间距指定时间的分钟数，接受格式HHMM
    	LocalTime locTime = LocalTime.parse( time, fmt_HHmm );
		return (int)LocalTime.now().until( locTime, ChronoUnit.MINUTES );
    }
    
    public static final int calcMinutesBetween( String time1, String time2 ) { 	//两个时间之间的分钟数，接受格式HHMM
		LocalTime locTime1 = LocalTime.parse( time1, fmt_HHmm );
		LocalTime locTime2 = LocalTime.parse( time2, fmt_HHmm );
		return (int)locTime1.until( locTime2, ChronoUnit.MINUTES );
    }

	/**
	 * MAIN TEST
	 */
	public static void main( String[] args ) {
		System.out.println( formatWeekEn(LocalDate.now()) );
		System.out.println( formatWeekZh(LocalDate.now()) );
		System.out.println( getWeekDay("2021-11-07") );
	}


}