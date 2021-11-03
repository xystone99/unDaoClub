// JavaScript Document

/**
 * 为空的验证
 */
function isNull( obj ) {						//字符串为NULL或者只存在空格的检查
	obj.value = Trim(obj.value);
	return obj.value.length <= 0;
}

function isNullWithDefault( obj, defValue ) {	//判断是否为空，如果为空，设置默认值
	obj.value = Trim(obj.value);
	if ( obj.value.length <= 0 ) {
		obj.value = defValue;
		return true;
	}
	return false;
}

/**
 * 字符串中字符的检查(只允许数字)
 * @param {Object} obj			：待判断组件
 * @param {Object} acceptNull	：是否允许为空
 * @param {Object} defValue	    ：如果允许为空,且当组件值为空时,所赋的默认值
 */
function checkFigure( obj, acceptNull ) {	
	if ( isNull(obj) ) {
		return acceptNull ? true : false;
	}
	var value = obj.value;	
	for ( var i = 0; i < value.length; i++ ) {
		if (!( value.charCodeAt(i) >= 48 && value.charCodeAt(i) <= 57 ) ) {
			return false;
		}
	}
	return true;
}

function checkFigureWithDefault( obj, acceptNull, defValue ) {	
	if ( isNull(obj) ) {
		if ( acceptNull ) {	
			obj.value = defValue;
			return true;
		} 
		return false;
	}
	var value = obj.value;	
	for ( var i = 0; i < value.length; i++ ) {
		if (!( value.charCodeAt(i) >= 48 && value.charCodeAt(i) <= 57 ) ) {
			return false;
		}
	}
	return true;
}

function checkMobile( obj, acceptNull ) {		//验证手机号码
	if ( isNull(obj) ) {
		return acceptNull ? true : false;
	}
	var mobileReg = /^(((13[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
	return mobileReg.test( obj.value );
} 

function checkPlateNumber( obj, acceptNull ) {	//验证车牌号
	if ( isNull(obj) ) {
		return acceptNull ? true : false;
	}
	var plateReg = /(^[\u4E00-\u9FA5]{1}[A-Z0-9]{6}$)|(^[A-Z]{2}[A-Z0-9]{2}[A-Z0-9\u4E00-\u9FA5]{1}[A-Z0-9]{4}$)|(^[\u4E00-\u9FA5]{1}[A-Z0-9]{5}[挂学警军港澳]{1}$)|(^[A-Z]{2}[0-9]{5}$)|(^(08|38){1}[A-Z0-9]{4}[A-Z0-9挂学警军港澳]{1}$)/;
	return plateReg.test( obj.value );
}

/**
 * 判断组件值是否包含某个字符串
 * @param {Object} obj			：待判断组件
 * @param {Object} acceptNull	：是否允许为空
 * @param {Object} containval	：要是否包含的字符串
 */
function contains( obj, acceptNull, containVal ) {
	if ( isNull(obj) ) {
		return acceptNull ? true : false;
	}
	var value = obj.value;
	return value.indexOf( containVal ) >= 0; 
}

function checkNumeric( obj, acceptNull ) {		//只允许数值型
	if ( isNull(obj) ) {
		return acceptNull ? true : false;
	}
	var value = obj.value;
	return isNaN( value ) ? false : true;	
}

function checkLetter( obj, acceptNull ) {		//只允许字母
	if ( isNull(obj) ) {
		return acceptNull ? true : false;
	}
	var value = obj.value;
	for ( var i = 0; i < value.length; i++ ) {
		if (!(( value.charCodeAt(i) >= 97 && value.charCodeAt(i) <= 122) || ( value.charCodeAt(i) >= 65 && value.charCodeAt(i) <= 90 ) ) ) {
			return false;
		}
	}
	return true;
}

function checkLetterFig( obj, acceptNull ) {	//只允许字母和数字
	if ( isNull(obj) ) {
		return acceptNull ? true : false;
	}
	var value = obj.value;	
	for ( var i = 0; i < value.length; i++ ) {
		var code = value.charCodeAt(i);
		if (!( (code >= 97 && code <= 122) || (code >= 65 && code <= 90) || (code >= 48 && code <= 57) )) {
			return false;
		}
	}
	return true;
}

/**
 * PlaceHolder 
 */
function fixHolder( obj, defaultValue ) {
	obj.value = defaultValue;
	obj.style.color = "#aaa";
	$(obj).focus( function(){ 
		if ( obj.value == defaultValue ) { 
			obj.value = ""; 
			obj.style.color = "#111";
		}
	});
	$(obj).blur(  function(){ 
		if (obj.value == "") { 
			obj.value = defaultValue; 
			obj.style.color = "#aaa";
		}
	});
}

function fixHolderWithInitial( obj, defaultValue, initValue ) {
	if (Trim(initValue).length > 0) {
		obj.value = Trim(initValue);
		obj.style.color = "#111";
	} else {
		obj.value = defaultValue;
		obj.style.color = "#aaa";
	}
	$(obj).focus( function(){ 
		if ( obj.value == defaultValue ) { 
			obj.value = ""; 
			obj.style.color = "#111";
		}
	});
	$(obj).blur(  function(){ 
		if (obj.value == "") { 
			obj.value = defaultValue; 
			obj.style.color = "#aaa";
		}
	});
}

function isNullWithHolder( obj, defValue ) {
	obj.value = Trim(obj.value);
	if ( obj.value == defValue ) {
		obj.value = "";
	}
	return obj.value.length <= 0;
}

function getHolderValue( obj, defaultValue ) {
	return isNullWithHolder( obj, defaultValue ) ? "" : obj.value;
}

/**
 * SPACE32	&38	'39	(40	)41	*42	-45	.46	=61	_95 
 */
function checkPrimaryKey( obj, acceptNull ) {	//允许字母数字及横线和下划线
	if ( isNull( obj ) ) {
		return acceptNull ? true : false;
	}
	var value = obj.value;
	for ( var i = 0; i < value.length; i++ ) {
		var code = value.charCodeAt(i);
		if (!( (code >= 97 && code <= 122) || (code >= 65 && code <= 90) || (code >= 48 && code <= 57) || (code==32) || (code == 40) || (code==41) || (code==42) || (code == 45) || (code==46) || (code==95) ) ) {
			return false;
		}
	}
	return true;
}

function checkEmail( obj, acceptNull ) {		//检查E_mail地址
	if ( isNull( obj ) ) {
		return acceptNull ? true : false;
	}
	var myReg = /^[_a-z0-9]+@([_a-z0-9]+\.)+[a-z0-9]{2,3}$/;
	return myReg.test( obj.value );
}

/**
 * Qty Check
 */
var qty_format0 = /^[\-]?\d{1,9}\.?(\d{0,5})$/;

function addCommas( obj ) {
	var value = obj.value;
	var strMinus = "";
	var strIntegral = "";
	var strDecimal = "";
	
	var pointIndex = value.length;
	if ( value.indexOf(".") > 0 ) {
		pointIndex = value.indexOf(".");
	}
	if ( eval( value ) < 0 ) {
		strMinus = "-";
		strIntegral = value.substring(1,pointIndex);
	} else {
		strMinus = "";
		strIntegral = value.substring(0,pointIndex);
	}
	strDecimal = value.substr(pointIndex);
	
	var after = ""; 
	var cnt = 0;
	for ( var i = strIntegral.length - 1; i >= 0; i-- ) { 
		after = strIntegral.charAt(i) + after; 
		cnt++; 
		if ( ( (cnt % 3) == 0 ) && ( i != 0 ) ) 
			after = ","+ after; 
	} 
	obj.value =  strMinus + after + strDecimal;
}

function removeCommas( obj ) {
	var value = obj.value;
	var after = ""; 
	for ( var i = 0; i < value.length; i++ ) { 
		if ( value.charAt(i) != "," ) {
			after += value.charAt( i );
		}
	} 
	obj.value = after;
}

function checkQty( obj, acceptNull, acceptMinus, integralLength, decimalLength ) {
	if ( isNull(obj) ) {
		return acceptNull ? true : false;		
	}	
	removeCommas( obj );	
	var value = obj.value;
	if ( qty_format0.test( value ) ) {
		if ( eval(value) < 0 && acceptMinus == false ) {
			return false;
		}
		
		var int_length = null;
		var decimal_length = null;
			
		var tmp = value.split( "." );	
		if ( tmp.length >= 2) {
			int_length = ("" + eval(tmp[0])).length;
			decimal_length = ("" + eval(tmp[1])).length;
		} else {
			int_length = ("" + eval(tmp[0])).length;
			decimal_length = 0;
		}
		if ( (int_length>integralLength) || (decimal_length>decimalLength) ) {
			return false;
		}
	} else {
		return false;
	}
	return true;
}

/**
 * Time CHECK
 */
var time_format = new RegExp("^([0-1]\\d|2[0-3]):[0-5]\\d$");
 
function checkTime( obj, acceptNull ) {
	var value = obj.value;
	if ( isNull( obj ) ) {
		return acceptNull ? true : false;
	}
	if ( time_format.test( value ) ) {
		return true;
	} else {
		return false;
	}
}

/**
 * Date CHECK
 */
var date_format0 = /^(\d{4})(\d{1,2})(\d{1,2})$/;
var date_format1 = /^(\d{4})\-(\d{1,2})\-(\d{1,2})$/;
var date_format2 = /^(\d{4})\/(\d{1,2})\/(\d{1,2})$/;

function checkDate( obj, acceptNull, range_flg ) {	// for SUBMIT
	var value = obj.value;
	if ( isNull( obj ) ) {
		return acceptNull ? true : false;
	}		
	var min_year = 2011;
	var max_year = 2030;
	
	if ( range_flg == "Birth" ) {
		min_year = 1961;
		max_year = 2000;	
	}  
		
	if ( date_format1.test( value ) ) {
		if ( eval(RegExp.$1) < min_year || eval(RegExp.$1) > max_year ) {
			return false;
		}
		if ( eval(RegExp.$2) > 12 ) return false;
		if ( eval(RegExp.$3) > 31 ) return false;
		var dt_test = new Date( eval(RegExp.$1), Number(eval(RegExp.$2) -1 ), eval(RegExp.$3) );
		if ( dt_test.getMonth() != Number(eval(RegExp.$2)-1) ) {
			return false;
		}
	} else {
		return false;
	}
	return true;
}

function checkDateTime( obj, acceptNull, range_flg ) { 
	if ( isNull( obj ) ) {
		return acceptNull ? true : false;
	}
	var min_year = 2017;
	var max_year = 2019;
	
	if ( range_flg == "Birth" ) {
		min_year = 1961;
		max_year = 2003;	
	}
	var value = obj.value;
	var dateTimeFormat = /^(\d{4})-(\d{1,2})-(\d{1,2}) (\d{1,2}):(\d{1,2})$/; 	//格式为：YYYY-MM-DD HH:MM 
	if ( dateTimeFormat.test( value ) ) {
		if ( eval(RegExp.$1) < min_year || eval(RegExp.$1) > max_year ) {
			return false;
		}
		if ( eval(RegExp.$2) > 12 ) return false;
		if ( eval(RegExp.$3) > 31 ) return false;
		if ( eval(RegExp.$4) > 23 ) return false;
		if ( eval(RegExp.$3) > 59 ) return false;
		var dt_test = new Date( eval(RegExp.$1), Number(eval(RegExp.$2) -1 ), eval(RegExp.$3), eval(RegExp.$4), eval(RegExp.$5) );
		if ( dt_test.getMonth() != Number(eval(RegExp.$2)-1) ) {
			return false;
		}
	} else {
		alert( "false" );
		return false;
	}
	return true; 
}

/**
 * 计算单价(两位小数)
 */
function calcQtyAmount( qtyObj, priceObj, amountObj ) {
	if ( isNull( qtyObj ) ) {
		return;
	}
	if ( isNull( amountObj ) ) {
		return;
	}
	priceObj.value = Math.round(amountObj.value*100/qtyObj.value)/100;
}

/**
 * 计算金额和税费(两位小数)
 */
function calcAmountTax( qtyObj, priceObj, amountObj, taxObj ) {
	if ( isNull( qtyObj ) ) {
		return;
	}
	if ( isNull( priceObj ) ) {
		return;
	}
	amountObj.value = Math.round(priceObj.value*qtyObj.value*100)/100;
	calcTax( amountObj, taxObj );
}

/**
 * 计算税费(两位小数)
 */
function calcTax( amountObj, taxObj ) {
	if ( isNull( amountObj ) ) {
		return;
	}
	taxObj.value = Math.round(amountObj.value*6/1.06)/100;
}

/**
 * 计算调整金额(两位小数)
 */
function calcAfterAdjust( initObj, adjustObj, afterObj ) {
	if ( isNull( initObj ) ) {
		return;
	}
	removeCommas( initObj );
	afterObj.value = initObj.value*1 + adjustObj.value*1;
}

function calcBeforeAdjust( initObj, adjustObj, afterObj ) {
	if ( isNull( initObj ) ) {
		return;
	}
	removeCommas( initObj );
	adjustObj.value = afterObj.value*1 - initObj.value*1;
}

/**
 * 按比例调整件数/重量/体积
 */
function changeWeightVolByQty( originQty, originWeight, originVolume, qtyOBJ, weightOBJ, volOBJ ) {
	if ( !checkQty(qtyOBJ,false,false,3,0) ) {
		weightOBJ.value = "";
		volOBJ.value = "";
		return;
	}
	var curQty = qtyOBJ.value;
	if ( curQty*1 > originQty*1 ) {
		alert( "件数不能超过" + originQty + "!" );
		qtyOBJ.value = "";
		weightOBJ.value = "";
		volOBJ.value = "";
		return;
	}
	weightOBJ.value = Math.round(originWeight*curQty*1000/originQty)/1000;
	volOBJ.value = Math.round(originVolume*curQty*1000/originQty)/1000;
}

/**
 * 比较两个浮点数的大小
 */
function compareDecimal( d1, d2 ) {
	var data1 = parseFloat( removeCommas( d1 ) );
	var data2 = parseFloat( removeCommas( d2 ) );
	if ( data1 > data2 ) {
		return 1;
	} else if ( data1 < data2 ) {
		return -1;
	}
	return 0;
}
