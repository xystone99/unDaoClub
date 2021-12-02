// JavaScript Document

/****************************************************************************************************
 * TransPlan New & Update
 ****************************************************************************************************/
function jQueryLoad( contextPath ) {
	$("#thDate1").datepicker({dateFormat: 'yy-mm-dd'});
	$("#acCus").autocomplete({ source: contextPath + "/fetchcus?Action=Fetch", minLength: 1, select: function(event, ui ) {
		myForm.fObjP.value = ui.item.ID;
		fetchTransLine( contextPath, ui.item.ID );
	}});
}

function fetchTransLine( contextPath, cusID ) {
	$("#acTransLine").autocomplete({ source: contextPath + "/fetchtransline?CusID="+cusID, minLength: 0, select: function(event, ui ) {
		myForm.fTransL.value = ui.item.ID;
		myForm.fTimeLevel.value = ui.item.TimeLevel;
		myForm.fPlanK.value = ui.item.PlanK;
		myForm.fNeZh1.value = ui.item.Name1;
		myForm.fAddress1.value = ui.item.Address1;
		myForm.fLinkman1.value = ui.item.Linkman1;
		myForm.fWindow1.value = ui.item.Window1;
		myForm.fRemark1.value = ui.item.Remark1;
		myForm.fNeZh2.value = ui.item.Name2;
		myForm.fAddress2.value = ui.item.Address2;
		myForm.fLinkman2.value = ui.item.Linkman2;
		myForm.fWindow2.value = ui.item.Window2;
		myForm.fRemark2.value = ui.item.Remark2;
		changePlanK( myForm.fPlanK );
	}});
}

function changePlanK( objPlanK ) {
	if ( objPlanK.value == "返空提货" || objPlanK.value == "单程送货" ) {
		$("#trRecycle").show( );
	} else {
		myForm.fQtyMeterR.value = "";
		myForm.fNeRecycle.value = "";
		$("#multi_selected_list").text("");
		$("#trRecycle").hide( );
	}
}

function changeMultiSelected(objSelect, objHidden ) {
	var val = objSelect.value;
	if ( objSelect.value == "0" ) {
		return;
	}
	if ( objHidden.value.indexOf(val) > -1 ) {
		alert( "禁止重复选择！" );
		return;
	}
	var display = objSelect.options[objSelect.selectedIndex].text;
	$("#multi_selected_list").append("<span class='increaseSpacing'>"+ display + "<b id='" + val + "'>x</b></span>");
	$("#"+val).click(function(){
		objHidden.value = objHidden.value.replace( display+",", "" );
		$(this).parent().remove();
	});
	objHidden.value = objHidden.value + display + ","
}

function checkValue( inForm ) {
	var strErr = new String( "" );
	if ( inForm.fObjP.value == "0" ) {
		strErr = strErr + "\n请选择客户！";
	}
	if ( inForm.fTransL.value == "0" ) {
		strErr = strErr + "\n请选择运输线路！";
	}
	if ( !checkDate( inForm.fPlanDate ,false,"") ) {
		strErr = strErr + "\n计划日期格式不正确！";
	}
	if ( inForm.fPlanK.value == "0" ) {
		strErr = strErr + "\n请选择运输类型！";
	}
	if ( isNull(inForm.fNeZh1) ) {
		strErr = strErr + "\n发货方为空！";
	}
	if ( inForm.fRemark1.value.length > 45 ) {
		strErr = strErr + "\其它说明(收货)限50个字符！";
	}
	if ( isNull(inForm.fNeZh2) ) {
		strErr = strErr + "\n收货方为空！";
	}
	if ( inForm.fRemark2.value.length > 45 ) {
		strErr = strErr + "\其它说明(收货)限50个字符！";
	}
	if ( myForm.fPlanK.value == "返空提货" || !isNull(inForm.fQtyMeterR) ) {
		if (!checkQty(inForm.fQtyMeterR, false, false, 3, 0)) {        
			strErr = strErr + "\n返空占车米数不允许为空，且只接受整数！";
		}
		if ( isNull(inForm.fNeRecycle) ) {
			strErr = strErr + "\n返空仓库为空！";
		}
	}
	if ( !checkQty( inForm.fQtyW ,true, false, 3, 3) ) {
		strErr = strErr + "\n重量允许为空，最多3位小数！";
	}
	if ( !checkQty( inForm.fQtyV, true, false, 3, 3) ) {
		strErr = strErr + "\n体积允许为空，最多3位小数！";
	}
	if ( !checkQty( inForm.fQtyMeter, true, false, 3, 0) ) {
		strErr = strErr + "\n占车米数允许为空，只接受整数！";
	}
	
	if ( strErr != "" ) {
		alert( strErr );
		return false;
	}
	
	isNullWithDefault( inForm.fQtyMeter, "0" );
	isNullWithDefault( inForm.fQtyMeterR, "0" );
	isNullWithDefault( inForm.fQtyW, "0" );
	isNullWithDefault( inForm.fQtyV, "0" );
	return confirm( "确定提交吗？" );
}

