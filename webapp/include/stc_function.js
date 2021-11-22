// JavaScript Document

/****************************************************************************************************
 * System
 ****************************************************************************************************/
var screen_width = window.screen.width;
var screen_height = window.screen.height;

function getCenteredLeft( width ) {
	return (screen_width-width) / 2;
}

function getCenteredTop( height ) {
	return (screen_height-height) / 2;
}

function sleep( milliSeconds ) {	
	var startTime = new Date().getTime();
	while (new Date().getTime() < startTime + milliSeconds);
} 

function encodeURL( strURL ) {		//URL Encode
	return escape( strURL ).replace( /\+/g, "%2B" );
}

function reloadCurrentPage( ) { 
	if ( confirm("确定重置当前页面吗？") ) {
		setTimeout( "self.location.reload(); ",1000);
	}
}

function reloadCurrentPageWithoutPrompt( ) {
	setTimeout( "self.location.reload(); ",100);
}

/****************************************************************************************************
 * Cookie
 ****************************************************************************************************/
function encodeCookie( str ) { 
	var result = ""; 
	for ( var index = str.length-1; index >= 0; index-- ) { 
		result += str.charCodeAt( index ); 
		if ( i ) result += ";";		//用;作分隔符 
	}
	return result; 
} 

function decodeCookie( str ) { 
	var result = ""; 
	var strArr =str.split( ";" ); 	
	for ( var index = strArr.length - 1; index >= 0; index-- ) {
		result += String.fromCharCode( eval(strArr[index]) ); 
	}
	return result; 
} 

function saveCookie( name, value, cyc ) {
	var expires = new Date( );
    expires.setTime( expires.getTime( ) + cyc );
    document.cookie = name + "=" + escape(value) + ";expires=" + expires.toGMTString( ) + ";path=/";
}

function getCookieValue( name ) {
	if ( document.cookie == "" ) { 
		return "";
	}
	allCookies = document.cookie.split( "; " );
	for ( var i = 0; i < allCookies.length; i++ ) { 
		var thisCookie = allCookies[i].split( "=" );		
		if ( thisCookie[0] == name ) {				
			return unescape( thisCookie[1] );
		}			
	}
	return "";
}

function delCookie( name ) {
	var expires = new Date( );
	expires.setTime( expires.getTime( ) - 1 );
	document.cookie = name + "=BeDeleted; expires="+ expires.toGMTString( );
}

/****************************************************************************************************
 * 限定最大高度和宽度，按比例放大或缩小图片
 ****************************************************************************************************/
function resizeImage( maxWidth, maxHeight, objImg ) { 	
	var img = new Image( ); 
	img.src = objImg.src; 	
	var in_ratio = img.width / img.height;
	var b_ratio = maxWidth / maxHeight;
		
	if ( in_ratio - b_ratio == 0 ) {
		objImg.width = img.width;
		objImg.height = img.height;
	} else if ( in_ratio - b_ratio > 0 ) {
		objImg.width = maxWidth;
		objImg.height = maxWidth * img.height / img.width;
	} else {
		objImg.width = maxHeight * img.width / img.height;
		objImg.height = maxHeight;
	}	
}

/****************************************************************************************************
 * 弹出ModalDialog窗口，兼容Chrome
 ****************************************************************************************************/
function openModalDialog( url, width, height ) { 	
	return window.open( url, "_blank", "width="+width+"px,height="+height+"px,toolbar=no,Scrollbars=yes,resizable=yes,status=yes,left=0,top=0" );
	//if ( navigator.userAgent.indexOf("Chrome") > 0 ) {
	//	return window.open( url, "_blank", "width="+width+"px,height="+height+"px,toolbar=no,Scrollbars=yes,resizable=yes,status=yes,left=0,top=0" );
	//} else {
	//	return window.showModalDialog( url, window, "dialogWidth:" + width + "px;dialogHeight:" + height + "px;edge:raised;center:yes;tatus:false;resizable:yes;" );
	//}
}

/****************************************************************************************************
 * SELECT操作
 ****************************************************************************************************/
function moveOptions( fromObj, toObj ) {		//移动选中的SELECT项
	for ( var index = fromObj.length - 1; index >= 0; index -- ) {
		if ( fromObj.options[index].selected == false ) {
			continue;
		}
		var newOption = document.createElement( "Option" );
		newOption.text = fromObj.options[index].text;
		newOption.value = fromObj.options[index].value;
		toObj.add( newOption );
		fromObj.options[index] = null;
	}
}

function moveOptionsAll( fromObj, toObj ) {		//移动全部的SELECT项
	for ( var index = fromObj.length - 1; index >= 0; index -- ) {
		var newOption = document.createElement( "Option" );
		newOption.text = fromObj.options[index].text;
		newOption.value = fromObj.options[index].value;
		toObj.add( newOption );
		fromObj.options[index] = null;
	} 
}

function selectAllOptions( selectObj ) {		//选中全部的SELECT项
	for ( var index = 0; index < selectObj.length; index ++ ) {
		selectObj.options[index].selected = true;
	}
}

function fillOptions( sObj, series ) {			//向SELECT中添加可选项。格式为：Name1=Value1&Name2=Value2
	if ( series == "" ) {
		return;	
	}
	var arr = series.split( "&" );
	for ( var index = 0; index < arr.length; index ++ ) {
		tmpItem = arr[index].split( "=" );
		var newOption = document.createElement( "Option" );		
		newOption.value = tmpItem[0];
		newOption.text = tmpItem[1];		
		sObj.add( newOption );
	}	
}

function resetOptions( sObj, series ) {
	for( var j=sObj.options.length-1 ; j>= 0 ; j--) {
 		sObj.options[j] = null;
 	}
 	fillOptions( sObj, series );
}

function filterOption( sObj, optionKey ) {		//过滤Options，只保留optionKey
	for( var j=sObj.options.length-1 ; j>= 0 ; j--) {
 		if ( sObj.options[j].value != optionKey ) {
 			sObj.options[j] = null;
 		}
 	}
}

/**
 * 向SELECT中添加某一类别下的可选项。
 * sObj			: SELECT组件
 * series		: 所有待添加的可选项列表。格式为：K1=Name1=Value1&K2=Name2=Value2
 * kind			: 指定类别，只添加该类别下的可选项。相当于过滤器。
 * hold_first	: 是否保留SELECT组件中的第一个可选项
 */
function fillOptionsInKind( sObj, series, kind, hold_first ) {
	var min_index = 0;
	if ( hold_first ) {
		min_index = 1;
	}
	for ( var index = sObj.length - 1; index >= min_index; index -- ) {
		sObj.options[index] = null;
	}
	if ( (sObj.length >= 0) && (hold_first == "N") ) {
		sObj.options[0] = null;
	} 
	if ( series == "" || kind == "") {
		return;	
	}
			
	var arr = series.split( "&" );		
	for ( index = 0; index < arr.length; index ++ ) {
		tmpItem = arr[index].split( "=" );		
		if ( tmpItem[0] != kind ) {
			continue;	
		}	
		
		var newOption = document.createElement( "Option" );		
		newOption.value = tmpItem[1];
		newOption.text = tmpItem[2];		
		sObj.add( newOption );
	}	
}

/****************************************************************************************************
 * HTML组件：赋值
 ****************************************************************************************************/
function setValue( txtObj, val ) {			//普通组件
	txtObj.value = val;
}

function setValue( toObj, fromObj, defValue ) {		//如果fromObj为空,赋值defValue
	toObj.value = isNull(fromObj) ? defValue : fromObj.value;
}

function setCurrentDate( toObj ) {			//设置当前日期
	var today = new Date();      
	var day = today.getDate();
	var month = today.getMonth() + 1;    
	var year = today.getFullYear();      
	var date = year + "-" + (month<10?"0":"") + month + "-" + (day<10?"0":"") + day; 
	toObj.value = date;
}

function setIntervalDate( toObj, interval ) {	//设置日期(步长)
	var jTime = (new Date()).getTime();      
	var jDay = new Date( jTime + interval*(24*60*60*1000) );  
	var day = jDay.getDate();      
	var month = jDay.getMonth() + 1;      
	var year = jDay.getFullYear();      
	var date = year + "-" + (month<10?"0":"") + month + "-" + (day<10?"0":"") + day; 
	toObj.value = date;
}

function changeSelectSite( thisObj, forObj ) {	//选择始发站目的站
	forObj.value = thisObj.options[thisObj.options.selectedIndex].text;
}

function setSelectValue( sObj, val ) {		//SELECT
	for ( var index = 0; index < sObj.length; index ++ ) {
		if ( sObj.options[index].value == val ) {
			sObj.options[index].selected = true;
			return;
		}
	}
	sObj.options[0].selected = true;
}

function setCheckValue(cbObj, val, checkValue ) {
	cbObj.checked = val == checkValue;
}

function setTextDisplay( txObj, val ) {	//<BR>转为换行符
	txObj.value = val.replace(/<br \/>/g, "\r\n");
}

function setTextSave( txObj ) {			//换行符转为<BR>
	txObj.value = txObj.value.replace(/\r\n/g,'<br />');
}

function setHideValueOfCheck( objCheck, objHide ) {		//根据Check是否选中设置隐藏元素的值
	objHide.value = objCheck.checked ? "Y" : "N";
}

/****************************************************************************************************
 * 允许多选的SELECT取值、赋值、选中的条目个数
 * 格式为：'Val1','Val2','Val3'
 ****************************************************************************************************/
function getSelectValues( sObj ) {
	var result = "";
	for ( var index = 0; index < sObj.length; index ++ ) {
		if ( sObj.options[index].selected == true ) {
			result = result + "'" + sObj.options[index].value + "',";
		}
	}
	if ( result == "" ) {
		return "All";
	}
	return result.substring(0,result.length-1);
}

function setSelectValues( sObj, sValues ) {
	for ( var index = 0; index < sObj.length; index ++ ) {
		var val = sObj.options[index].value;
		if ( sValues.indexOf( val ) >= 0 ) {
			sObj.options[index].selected = true;
		}
	}	
}

function getCountSelected( sObj ) {
	var countSelected = 0;
	for ( var index = 0; index < sObj.length; index ++ ) {
		if ( sObj.options[index].selected == true ) {
			countSelected++;
		}
	}
	return countSelected;
}

/****************************************************************************************************
 * maths
 ****************************************************************************************************/
function mul( n1, n2 ) {
    var m = 0, s1 = n1.toString( ), s2 = n2.toString( );
    try {
        m += s1.split(".")[1].length;
    } catch( e ) {
    }
    try {
        m += s2.split(".")[1].length;
    } catch(e){
    }
    return Number( s1.replace(".","")) * Number(s2.replace(".","") ) / Math.pow(10, m);
}

function plus( arg1, arg2 ) {
    var r1,r2,m;
    try{ r1=arg1.toString().split(".")[1].length; }catch(e){r1=0; }
    try{ r2=arg2.toString().split(".")[1].length; }catch(e){r2=0; }
    m = Math.pow(10,Math.max(r1,r2));
    return (Math.round(arg1*m+arg2*m))/m;
}

/****************************************************************************************************
 * 回车键达到Tab键的效果(主要用于Form中)
 ****************************************************************************************************/
function shieldEnterToTab( ) {   
	if ( event.keyCode == 13 ) {
		event.keyCode = 9;  
	} 
}

/****************************************************************************************************
 * 去掉字符串前后的空格、零等累赘字符
 ****************************************************************************************************/
function LTrim( str ) { 			//去掉左边的空格
	var index; 
	for( index = 0; index < str.length - 1; index++ ) { 
		if ( str.charAt( index ) != " ") break; 
	} 
	str = str.substring( index, str.length ); 
	return str; 
} 

function RTrim( str ) {				//去掉右边的空格
	var index; 
	for( index = str.length - 1; index >= 0; index-- ) { 
		if ( str.charAt(index) != " " ) break; 
	} 
	str = str.substring( 0, index + 1 ); 
	return str; 
} 

function Trim( str ) {				//去掉两边的空格
	return LTrim( RTrim( str ) ); 
} 

function shrinkSpace( str ) {		//去掉字符串中的所有空格
	var result = "";
	for( var index = 0; index < str.length - 1; index++ ) { 
		if ( str.charAt( index ) != " ") {
			result = result + str.charAt( index );	
		}
	} 
	return result; 
}

function trimDecimal( val ) {		//去掉小数尾部的零
	if ( isNaN( val ) ) {
		return "";	
	}
	var dotIndex = val.indexOf( "." );
	if ( dotIndex < 0 ) {
		return val;	
	}
	var index = val.length - 1;
	for ( var i = val.length - 1; i >= dotIndex; i -- ) {
		if ( val.charAt( i ) != "0"	) {
			index = i;
			break;
		}
	}	
	return val.substring( 0, index == dotIndex ? dotIndex : index + 1 );
}

/****************************************************************************************************
 * Date About
 ****************************************************************************************************/
function getDateFromString( strDate ) { 		//根据固定格式的字符串去创建日期
	var arrYmd = strDate.split( "-" ); 
	var numYear = parseInt( arrYmd[0] ); 
	var numMonth = parseInt( arrYmd[1], 10 ) - 1; 
	var numDay = parseInt( arrYmd[2], 10 ); 
	return new Date( numYear, numMonth, numDay );         
} 

function getDateSub( strDate1, strDate2 ) {		//计算两个日期之间间隔多少天
	var datFrom = getDateFromString( strDate1 ); 
	var datTo = getDateFromString( strDate2 ); 
	var numDays = (datTo - datFrom) / ( 1000*60*60*24 ); 
	return numDays; 
}

function getCurrent_MD( ) {
	var myDate = new Date( );  
    var mm = (myDate.getMonth() + 1) + "";
    var dd = myDate.getDate()  + "";
    return (mm.length>1 ? mm : "0"+mm) + "-" + (dd.length>1?dd:"0"+dd);
}

function getCurrentDate( ) {					//获取当前日期值
	var myDate = new Date( ); 
	var yyyy = myDate.getFullYear();			//获取完整的年份(4位,1970-????) 
	var mm = (myDate.getMonth() + 1) + "";
    var dd = myDate.getDate()  + "";
	return yyyy + "-" + mm + "-" + dd;
}

function getCurrent_YMD( ) {					//获取当前日期值
	var myDate = new Date( ); 
	var yy = myDate.getFullYear();				//获取当前年份(2位)  
	var mm = (myDate.getMonth() + 1) + "";
    var dd = myDate.getDate()  + "";
	return (yy + "-" + mm + "-" + dd).substr(2);
}


/****************************************************************************************************
 * 手机端应用（重复）
 ****************************************************************************************************/
function isNullMobile( obj ) {		//字符串为NULL或者只存在空格的检查
	obj.value = Trim(obj.value);
	return obj.value.length <= 0;
}

