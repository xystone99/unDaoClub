package com.undao.utils;

import java.math.*;
import java.text.DecimalFormat;

public class DecimalUtils {	    	        
	
	private static String TAG_SPACE = "";
	private static String TAG_ZERO = "0";
	
	private static DecimalFormat fmt_NoCommaQty = new DecimalFormat( "#####.###" ); 
	
	private static DecimalFormat fmt_int = new DecimalFormat( "#,###,###" ); 
	private static DecimalFormat fmt_qty = new DecimalFormat( "#,###,###.###" ); 
	private static DecimalFormat fmt_qty_fit = new DecimalFormat( "#,###,###,###.000" );
	private static DecimalFormat fmt_money = new DecimalFormat( "#,###,###.##" );
	private static DecimalFormat fmt_money_fit = new DecimalFormat( "#,###,###,###.00" );
	private static DecimalFormat fmt_percent = new DecimalFormat( "#.##%" );
	
	/**
	 * 将BigDecimal转换为Float 
	 */
	public static final float toFloat( Object bVal, int scale ) {
		BigDecimal val = (BigDecimal)bVal;
		return val.setScale(scale,BigDecimal.ROUND_HALF_UP).floatValue( );  
	}
	
	/**
	 * 无逗号输出
	 */
	public static final String formatLongNoComma( Object iVal ) {
		return formatLongNoComma(iVal, true );
	}
	
    public static final String formatLongNoComma( Object iVal, boolean displayZero ) {
    	Long val = (Long)iVal;  	
    	if ( displayZero ) {
    		return ( (val==null) || (val.intValue()==0) ) ? TAG_ZERO : val.toString();
    	} else {
    		return ( (val==null) || (val.intValue()==0) ) ? TAG_SPACE : val.toString();
    	}
    }
    
    public static final String formatQtyNoComma( Object bVal ) {
    	return formatQtyNoComma(bVal, true );
	}
    
    public static final String formatQtyNoComma( Object bVal, boolean displayZero ) {
    	BigDecimal val = (BigDecimal)bVal;
		return ( val == null || ( !displayZero && val.abs().floatValue( ) < 0.0001 ) ) ? TAG_SPACE : fmt_NoCommaQty.format( val );
	}
    
	/**
     * Display Integer && Long
     */
    public static final String formatInteger( Object val ) {
    	return formatInteger( val, true );   
    }
    
    public static final String formatInteger( Object iVal, boolean displayZero ) {
    	Integer val = (Integer)iVal;    	
    	if ( displayZero ) {
    		return ( (val==null) || (val.intValue()==0) ) ? TAG_ZERO : fmt_int.format( val );
    	} else {
    		return ( (val==null) || (val.intValue()==0) ) ? "" : fmt_int.format( val );
    	}
    }
    
    public static final String formatInteger( Object iVal, boolean displayZero, String suffix ) {
    	Integer val = (Integer)iVal;
    	if ( displayZero ) {
    		return ( (val==null) || (val.intValue()==0) ) ? TAG_ZERO+suffix : fmt_int.format( val )+suffix;
    	} else {
    		return ( (val==null) || (val.intValue()==0) ) ? "" : fmt_int.format( val )+suffix;
    	}
	}
    
    public static final String formatIntegerNoComma( Object iVal, boolean displayZero ) {
    	Integer val = (Integer)iVal;    	
    	if ( displayZero ) {
    		return ( (val==null) || (val.intValue()==0) ) ? TAG_ZERO : val.toString();
    	} else {
    		return ( (val==null) || (val.intValue()==0) ) ? "" : val.toString();
    	}
    }
    
    
    public static final String formatLong( Object val ) {
    	return formatLong( val, true );   
    }
    
    public static final String formatLong( Object lVal, boolean displayZero ) {
    	Long val = (Long)lVal;
    	if ( displayZero ) {
    		return ( (val==null) || (val.intValue()==0) ) ? TAG_ZERO : fmt_int.format( val );
    	} else {
    		return ( (val==null) || (val.intValue()==0) ) ? "" : fmt_int.format( val );
    	}
    }
    
    public static final String formatLong( Object lVal, boolean displayZero, String suffix ) {
    	Long val = (Long)lVal;
		if ( displayZero ) {
    		return ( (val==null) || (val.intValue()==0) ) ? TAG_ZERO+suffix : fmt_int.format( val )+suffix;
    	} else {
    		return ( (val==null) || (val.intValue()==0) ) ? "" : fmt_int.format( val )+suffix;
    	}
	}
        

    public static final String formatQty( Object val ) {
    	return formatQty( val, true );   
    }
    
    public static final String formatQty( Object bVal, boolean displayZero ) {
    	BigDecimal val = (BigDecimal)bVal;
    	return ( val == null || ( !displayZero && val.abs().floatValue( ) < 0.001 ) ) ? TAG_SPACE : fmt_qty.format( val );
    }

    public static final String formatQty( Object bVal, boolean displayZero, boolean fitFraction ) {
    	BigDecimal val = (BigDecimal)bVal;
		if ( fitFraction ) {
			if ( val == null ) {
				return TAG_SPACE;
			}
			val = val.setScale(2, BigDecimal.ROUND_HALF_UP );
			return ( !displayZero && val.abs().floatValue( ) < 0.001 ) ? TAG_SPACE : fmt_qty_fit.format( val );
		}
		return ( val == null || ( !displayZero && val.abs().floatValue( ) < 0.0001 ) ) ? TAG_SPACE : fmt_qty.format( val );
	}
    
    public static final String formatQty( Object val, String qtyUnit ) {
    	return formatQty( val, true, false, qtyUnit );   
    }
    
    public static final String formatQty( Object val, boolean displayZero, String qtyUnit ) {
    	return formatQty( val, displayZero, false, qtyUnit );   
    }

	/**
	 * 格式化BigDecimal数据
	 *
	 * @param bVal			: BigDecimal对象
	 * @param displayZero	: 如果为零，是否显示？
	 * @param fitFraction	: 是否以零补足小数位
	 * @param qtyUnit		: 数量单位
	 * @return
	 */
    public static final String formatQty( Object bVal, boolean displayZero, boolean fitFraction, String qtyUnit ) {
    	BigDecimal val = (BigDecimal)bVal;
		if ( fitFraction ) {
			if ( val == null ) {
				return TAG_SPACE;
			}
			val = val.setScale(2, BigDecimal.ROUND_HALF_UP );
			return ( !displayZero && val.abs().floatValue( ) < 0.001 ) ? TAG_SPACE : fmt_qty_fit.format(val)+qtyUnit;
		}
		return ( val == null || ( !displayZero && val.abs().floatValue( ) < 0.0001 ) ) ? TAG_SPACE : fmt_qty.format(val)+qtyUnit;
	}
    
    /**
     * Display Money 
     */
    public static final String formatMoney( Object val ) {
    	return formatMoney( val, true );
    }
    
    public static final String formatMoney( Object bVal, boolean displayZero ) {
    	BigDecimal val = (BigDecimal)bVal;
    	return ( val == null || ( !displayZero && val.abs().floatValue( ) < 0.000001 ) ) ? TAG_SPACE : fmt_money.format( val );
    } 
    
    public static final String formatMoney( Object bVal, boolean displayZero, boolean fitFraction ) {
    	BigDecimal val = (BigDecimal)bVal;
		if ( fitFraction ) {
			if ( val == null ) {
				return TAG_SPACE;
			} else {
				val = val.setScale(2, BigDecimal.ROUND_HALF_UP );
				return ( !displayZero && val.abs().floatValue( ) < 0.0001 ) ? TAG_SPACE : fmt_money_fit.format( val );
			}
		} else {
			return ( val == null || ( !displayZero && val.abs().floatValue( ) < 0.0001 ) ) ? TAG_SPACE : fmt_money.format( val );
		}		
	}
    
    /**
     * Display Percent
     */
    public static final String formatPercent( Object pVal, boolean displayZero ) {
    	BigDecimal val = (BigDecimal)pVal;
    	if ( val == null || val.abs().floatValue() < 0.00001 ) {
    		return displayZero ? "0%" : TAG_SPACE;
    	}
		return fmt_percent.format( val.floatValue() );
	}
    
    public static final String formatPercent( Object boyVal, Object parentVal, boolean displayZero ) {
    	BigDecimal val1 = (BigDecimal)boyVal;
    	BigDecimal val2 = (BigDecimal)parentVal;
    	if ( val1 == null || val1.abs().floatValue() < 0.00001 ) {
    		return displayZero ? "0%" : TAG_SPACE;
    	}
    	if ( val2 == null || val2.abs().floatValue() < 0.00001 ) {
    		return "100%";
    	}    	
		return fmt_percent.format( val1.floatValue() / val2.floatValue() );
	}
	
}