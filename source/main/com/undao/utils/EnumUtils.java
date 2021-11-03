/**
 * Created At 2014-6-16 下午01:12:48
 */
package com.undao.utils;

/**
 * @author Administrator
 * 字典工具类	
 */
public class EnumUtils {

	/**
	 * 根据对照数组和对照项，检索关联数组中的相同下标项的值
	 * @param arrContrast	: 对照数组
	 * @param contrastItem	: 对照数组中的对照项
	 * @param arrRelevance	: 关联数组
	 * @return
	 */
	public static String fetchRelevanceItem( String[] arrContrast, String contrastItem, String[] arrRelevance ) {
		for ( int j=0; j<arrContrast.length; j++ ) {
			if ( arrContrast[j].equals( contrastItem ) ) {
				return arrRelevance[j];
			}
		}
		return arrRelevance[0];
	}
	
	public static String fetchRelevanceItem( byte[] arrContrast, byte contrastItem, String[] arrRelevance ) {
		for ( int j=0; j<arrContrast.length; j++ ) {
			if ( arrContrast[j] == contrastItem ) {
				return arrRelevance[j];
			}
		}
		return arrRelevance[0];
	}
	
	public static byte fetchRelevanceItem( String[] arrContrast, String contrastItem, byte[] arrRelevance ) {
		for ( int j=0; j<arrContrast.length; j++ ) {
			if ( arrContrast[j].equals( contrastItem ) ) {
				return arrRelevance[j];
			}
		}
		return arrRelevance[0];
	}
	
	/**
	 * 根据值数组和显示文本数组生成HtmlSelect对象
	 * @param arrKey		: 值数组
	 * @param arrDisplay	: 显示数组
	 * @param containFirst	: 是否附加"全部"选项，值为"All"或"0"
	 * @param firstKey		: 附加选项的值(一般为"All"或"0")
	 * @param firstDisplay	: 附加选项的显示文本(一般为"全部"或"未选择")
	 * @return				: HtmlSelect的字符串表示
	 */
	public static String createSelectOptions( String[] arrKey, String[] arrDisplay, boolean containFirst, String firstKey, String firstDisplay ) {
		StringBuilder bufSelect = new StringBuilder();
		if ( containFirst ) {
			bufSelect.append( "<option value=\"" ).append( firstKey ).append( "\"" ).append( containFirst ? " selected>" : ">" ).append( firstDisplay ).append( "</option>" );
		}
 		for ( int j=0; j<arrKey.length; j++ ) {
			bufSelect.append( "<option value=\"" ).append( arrKey[j] ).append( "\"" ).append( containFirst||j>0?">":" selected>" );
			bufSelect.append( arrDisplay[j] ).append( "</option>" );
		}
		return bufSelect.toString();
	}
	
	/**
	 * 根据值数组生成MySQL表中的ENUM类型字段
	 * @param arrKey		: 值数组
	 * @return				：MySQL表中的ENUM类型字段
	 */
	public static String getEnumSerials( String[] arrKey ) {
		StringBuilder buf = new StringBuilder();
		for( int j=0; j<arrKey.length; j++ ) {
			buf.append( "'" ).append( arrKey[j] ).append( "'," );
		}
		return buf.deleteCharAt(buf.length()-1).toString( );
	}

}
