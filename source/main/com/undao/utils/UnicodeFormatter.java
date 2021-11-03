/**
 * UnicodeFormatter.java
 *
 * Created on 20040401, 11:31 A.M
 */

package com.undao.utils;

/**
 * @author X.Stone
 *
 */

public class UnicodeFormatter {
    
    public static final char keyPlus = '+';
    public static final char keyMinus = '-';
    public static final char keyPd = '.';
    public static final char keySs = '/';
    
    public static final String keyStrPlus = "+";
    public static final String keyStrMinus = "-";
    public static final String keyStrPd = ".";
    public static final String keyStrCa = ",";
    public static final String keyStrSs = "/";
    
    private static final String keyValueSeparators = "=: \t\r\n\f";
    private static final String strictKeyValueSeparators = "=:";
    private static final String specialSaveChars = "=: \t\r\n\f#!";
    private static final String whiteSpaceChars = " \t\r\n\f";
    private static final char[ ] hexDigit = { '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'   };     /** A table of hex digits */
    
    /***
     * 把字符串转换成Unicode字符
     */
    public final static  String stringToUnicode(String theString ) {
        return stringToUnicode( theString, false );
    }
    
    public final static  String stringToUnicode( String theString, boolean escapeSpace ) {
        int len = theString.length( );
        StringBuffer outBuffer = new StringBuffer( len * 2 );
        for ( int x=0; x < len; x ++ ) {
            char aChar = theString.charAt( x );
            switch ( aChar ) {
                case ' ' :  if ( x == 0 || escapeSpace ) { outBuffer.append( '\\' );  }    outBuffer.append( ' ' );    break;
                case '\\' :  outBuffer.append( '\\' ); outBuffer.append( '\\' );   break;
                case '\t' :  outBuffer.append( '\\' ); outBuffer.append( 't' );    break;
                case '\n' : outBuffer.append( '\\' ); outBuffer.append( 'n' );   break;
                case '\r' :  outBuffer.append( '\\' ); outBuffer.append( 'r' );    break;
                case '\f' :  outBuffer.append( '\\' ); outBuffer.append( 'f' );     break;
                default:
                    if ( ( aChar < 0x0020 ) || ( aChar > 0x007e ) ) {
                        outBuffer.append( '\\' );
                        outBuffer.append( 'u' );
                        outBuffer.append( toHex( ( aChar >> 12 ) & 0xF ) );
                        outBuffer.append( toHex( ( aChar >>  8) & 0xF ) );
                        outBuffer.append( toHex( ( aChar >>  4) & 0xF ) );
                        outBuffer.append( toHex( aChar & 0xF ) );
                    } else {
                        if ( specialSaveChars.indexOf( aChar ) != -1 )  { outBuffer.append( '\\' );     }
                        outBuffer.append( aChar );
                    }
            }
        }
        return outBuffer.toString( );
    }
    
    /***
     * 把Unicode转换成本地字符串
     */
    private static char toHex( int nibble ) {
        return hexDigit[ ( nibble & 0xF ) ];
    }
    
    public static final String unicodeToString( String unicode ) {
        char aChar;
        if ( unicode == null )  return null;
        StringBuffer outBuffer = new StringBuffer( unicode.length( ) );
        for ( int index = 0; index < unicode.length( ); ) {
            aChar = unicode.charAt( index++ );
            if ( aChar != '\\' ) {
                outBuffer.append( aChar );
                continue;
            }
            aChar = unicode.charAt( index++ );
            if (aChar != 'u' ) {  // Read the xxxx
                if ( aChar == 't' )  aChar = '\t';
                else if ( aChar == 'r' ) aChar = '\r';
                else if ( aChar == 'n' ) aChar = '\n';
                else if ( aChar == 'f' ) aChar = '\f';
                outBuffer.append( aChar );
                continue;
            }
            int value=0;
            for ( int i = 0; i < 4; i ++ ) {
                aChar = unicode.charAt( index++ );
                switch ( aChar ) {
                    case '0' : case '1' : case '2' : case '3' : case '4' : case '5' : case '6' : case '7' : case '8' : case '9' :  value = ( value << 4 ) + aChar - '0';   break;
                    case 'a' : case 'b' : case 'c' : case 'd' : case 'e' : case 'f' :  value = ( value << 4 ) + 10 + aChar - 'a';   break;
                    case 'A' : case 'B' : case 'C' : case 'D' : case 'E' : case 'F' : value = ( value << 4 ) + 10 + aChar - 'A';   break;
                    default : throw new IllegalArgumentException( "Malformed \\uxxxx encoding." );
                }
            }
            outBuffer.append( ( char )value );
        }
        return outBuffer.toString( );
    }
    
    /***
     * Other methods
     */
    public static boolean isBASIC_LATIN( char c ) {
        if ( Character.UnicodeBlock.of( c ) == Character.UnicodeBlock.BASIC_LATIN ) {
            return true;
        }
        return false;
    }
    
    public static boolean isBASIC_LATIN( Character c ) {
        return isBASIC_LATIN( c.charValue( ) );
    }
    
    public static boolean isNUMBER_LATIN( char c ) {
        if ( Character.UnicodeBlock.of( c ) != Character.UnicodeBlock.BASIC_LATIN )  return false;
        if ( Character.isDigit( c ) ) {
            return true;
        } else if ( c == keyPlus || c == keyMinus || c == keyPd ) {
            return true;
        }
        return false;
    }
    
    public static boolean isNUMBER_LATIN( Character c ) {
        return isNUMBER_LATIN( c.charValue( ) );
    }
    
    public static boolean isDATE_LATIN( char c ) {
        if ( Character.UnicodeBlock.of( c ) != Character.UnicodeBlock.BASIC_LATIN )  return false;
        if ( Character.isDigit( c ) ) {
            return true;
        } else if ( c == keySs || c == keyMinus ) {
            return true;
        }
        return false;
    }
    
    public static boolean isDATE_LATIN( Character c ) {
        return isDATE_LATIN( c.charValue( ) );
    }
    
    public static boolean getUnicodeBlock( char c ) {
        return false;
    }
    
}