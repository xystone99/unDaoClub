<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import = "xms.beans.customerService.*" %>
<%@ page import="com.undao.enumeration.SysAstricts" %>
<%@ page import="com.undao.enumeration.EnumConstants" %>
<%@ page import="xms.XmsInitial" %>
<%@ page import="com.undao.control.AbstractDaemon" %>
<%
	int PAGE_TAG = 0;
	boolean acceptInnerUser = true;
	String[] needAstricts = {SysAstricts.QX_CUS_UPDATE  };
%>
<%@ include file="../include/inc_header.logic" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>  
<title>新建运输计划</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<link rel="stylesheet" type="text/css" href="../css/childWindow.css" />
<link rel="stylesheet" type="text/css" href="../css/jquery-ui-1.10.4.custom.css" />
</head>

<body>
<form name="myForm" method="post" action="processBusiness.jsp" onsubmit="return checkValue(this);">
<table cellspacing="0" cellpadding="0">
	<tr class="head_tr">
	<td width="100%" align="left"><b>新建运输计划&gt;&gt;</b></td>
	</tr>
	<tr class="empty_tr2"><td colspan="6"></td></tr>
</table><br/>

<table id="date" cellspacing="0" cellpadding="0">
	<tr class="content_tr">
	<td width="13%" align="right">客户简称:</td>
	<td width="1%"></td>
	<td width="33%" align="left"><input type="text" id="acCus" name="acCus" size="12" maxlength="20" placeholder="快速检索" class="input_text" />
		<input type="hidden" name="<%=TransPlan.QP_OBJ_P%>" value="0" /></td>
	<td width="13%" align="right">发货日期:</td>
	<td width="1%"></td>
	<td width="39%" align="left"><input id="thDate1" type="text" name="<%=TransPlan.QP_PLAN_DATE%>" size="12" maxlength="10" class="input_text" ondblclick="javascript:setCurrentDate(this)" placeholder="双击" /></td>
	</tr>
	<tr class="content_tr30">
	<td align="right">运输线路:</td>
	<td></td>
	<td align="left" colspan="4"><input type="text" id="acTransLine" name="acTransLine" size="30" maxlength="50" class="input_text" />
		<input type="hidden" name="<%=TransPlan.QP_TRANS_L%>" value="0" /></td>
	</tr>

	<tr class="empty_tr"><td colspan="6"></td></tr>

	<tr class="content_tr">
	<td align="right">发货方:</td>
	<td></td>
	<td align="left"><input type="text" name="<%=TransPlan.QP_NE_ZH1%>" size="16" maxlength="20" class="input_text" readonly /></td>
	<td align="right">窗口时间:</td>
	<td></td>
	<td align="left"><input type="text" name="<%=TransPlan.QP_WINDOW_1%>" size="20" maxlength="20" class="input_text" /></td>
	</tr>

	<tr class="content_tr">
	<td align="right">发货联系人:</td>
	<td></td>
	<td align="left"><input type="text" name="<%=TransPlan.QP_LINKMAN_1%>" size="24" maxlength="20" class="input_text" readonly /></td>
	<td align="right">详细地址:</td>
	<td></td>
	<td align="left"><input type="text" name="<%=TransPlan.QP_ADDRESS_1%>" size="32" maxlength="20" class="input_text" readonly /></td>
	</tr>

	<tr class="content_tr">
	<td align="right">其它说明:</td>
	<td></td>
	<td align="left" colspan="4"><input type="text" name="<%=TransPlan.QP_REMARK_1%>" size="80" maxlength="50" class="input_text" placeholder="单据、道口、特殊要求等" /></td>
	</tr>
	<tr class="empty_tr"><td colspan="6"></td></tr>

	<tr class="content_tr">
	<td align="right">收货方:</td>
	<td></td>
	<td align="left"><input type="text" name="<%=TransPlan.QP_NE_ZH2%>" size="16" maxlength="20" class="input_text" readonly /></td>
	<td align="right">窗口时间:</td>
	<td></td>
	<td align="left"><input type="text" name="<%=TransPlan.QP_WINDOW_2%>" size="20" maxlength="20" class="input_text" /></td>
	</tr>

	<tr class="content_tr">
	<td align="right">收货联系人:</td>
	<td></td>
	<td align="left"><input type="text" name="<%=TransPlan.QP_LINKMAN_2%>" size="24" maxlength="20" class="input_text" readonly /></td>
	<td align="right">详细地址:</td>
	<td></td>
	<td align="left"><input type="text" name="<%=TransPlan.QP_ADDRESS_2%>" size="32" maxlength="20" class="input_text" readonly /></td>
	</tr>

	<tr class="content_tr">
	<td align="right">其它说明:</td>
	<td></td>
	<td align="left" colspan="4"><input type="text" name="<%=TransPlan.QP_REMARK_2%>" size="80" maxlength="50" class="input_text" placeholder="单据、道口、特殊要求等" /></td>
	</tr>

	<tr class="empty_tr"><td colspan="6"></td></tr>

	<tr class="content_tr30">
	<td align="right">运输类型:</td>
	<td></td>
		<td lign="left"><select name="<%=TransPlan.QP_PLAN_K%>" class="select" onchange="javascript:changePlanK(this)"><option value="0">--选择运输类型--</option><%=EnumConstants.TRANS_PLAN_K_OPTIONS%></select></td>
	<td align="right">时效要求等级:</td>
	<td></td>
	<td lign="left"><select name="<%=TransPlan.QP_TIME_LEVEL%>" class="select"><option value="0">--时效要求等级--</option><%=EnumConstants.TIME_LEVEL_OPTIONS%></select></td>
	</tr>

	<tr id="trRecycle" class="content_tr30">
	<td align="right">返空占车米数:</td>
	<td></td>
	<td align="left"><input type="text" name="<%=TransPlan.QP_QTY_METER_R%>" size="12" maxlength="3" class="input_text" /></td>
	<td align="right">返空仓库:</td>
	<td></td>
	<td colspan="4" align="left"><select id="whList" name="sWareList" class="select" onchange="javascript:changeMultiSelected(this,myForm.<%=TransPlan.QP_NE_RECYCLE%>)"><option value="0">--选择仓库--</option><%=XmsInitial.getXmsContainer().getMasterCompany().getSelectOptions(AbstractDaemon.getCloudID(request))%></select>&nbsp;<span id="multi_selected_list"></span>
		<input type="hidden" name="<%=TransPlan.QP_NE_RECYCLE%>" /></td>
	</tr>

	<tr class="content_tr30">
	<td align="right">占车米数:</td>
	<td></td>
	<td align="left"><input type="text" name="<%=TransPlan.QP_QTY_METER%>" size="12" maxlength="3" class="input_text" /></td>
	<td align="right">重量/体积:</td>
	<td></td>
	<td align="left"><input type="text" name="<%=TransPlan.QP_QTY_W%>" size="5" maxlength="7" class="input_text" />(吨)&nbsp;/&nbsp;<input type="text" name="<%=TransPlan.QP_QTY_V%>" size="5" maxlength="7" class="input_text" />(方)</td>
	</tr>
</table><br/>

<table cellspacing="0" cellpadding="0">
	<tr class="tail_tr">
	<td align="left"><input type="button" name="reset" value="刷新页面" class="input_btn" onclick="javascript:reloadCurrentPage();" /></td>
	<td align="right">
		<input type="hidden" name="Action" value="TransPlanNew" />
		<input type="submit" name="btnSubmit" value="提交信息" class="input_btn" />
	</td>
	</tr>
</table>
</form>

<script type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" src="../js/jquery-ui.min.js"></script>
<script type="text/javascript" src="../js/jquery.form.js"></script>

<script type="text/javascript" src="../include/stc_validate.js"></script>
<script type="text/javascript" src="../include/stc_function.js"></script>

<script type="text/javascript">
	setIntervalDate( myForm.<%=TransPlan.QP_PLAN_DATE%>, 1 );
	$("#trRecycle").hide( );
</script>

<script type="text/javascript">
$("#acCus").autocomplete({ source: "<%=XmsInitial.getContextPath()%>/fetchcus?Action=Fetch", minLength: 1, select: function(event, ui ) {
	myForm.<%=TransPlan.QP_OBJ_P%>.value = ui.item.ID;
	$("#acTransLine").autocomplete({ source: "<%=XmsInitial.getContextPath()%>/fetchtransline?CusID="+ui.item.ID, minLength: 0, select: function(event, ui ) {
		myForm.<%=TransPlan.QP_TRANS_L%>.value = ui.item.ID;
		myForm.<%=TransPlan.QP_TIME_LEVEL%>.value = ui.item.TimeLevel;
		myForm.<%=TransPlan.QP_PLAN_K%>.value = ui.item.PlanK;
		myForm.<%=TransPlan.QP_NE_ZH1%>.value = ui.item.Name1;
		myForm.<%=TransPlan.QP_ADDRESS_1%>.value = ui.item.Address1;
		myForm.<%=TransPlan.QP_LINKMAN_1%>.value = ui.item.Linkman1;
		myForm.<%=TransPlan.QP_WINDOW_1%>.value = ui.item.Window1;
		myForm.<%=TransPlan.QP_REMARK_1%>.value = ui.item.Remark1;
		myForm.<%=TransPlan.QP_NE_ZH2%>.value = ui.item.Name2;
		myForm.<%=TransPlan.QP_ADDRESS_2%>.value = ui.item.Address2;
		myForm.<%=TransPlan.QP_LINKMAN_2%>.value = ui.item.Linkman2;
		myForm.<%=TransPlan.QP_WINDOW_2%>.value = ui.item.Window2;
		myForm.<%=TransPlan.QP_REMARK_2%>.value = ui.item.Remark2;
	}});
}});

function changePlanK( objPlanK ) {
	if ( objPlanK.value == "返空提货" || objPlanK.value == "返空送货" ) {
		$("#trRecycle").show( );
	} else {
		myForm.<%=TransPlan.QP_QTY_METER_R%>.value = "";
		myForm.<%=TransPlan.QP_NE_RECYCLE%>.value = "";
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
	if ( inForm.<%=TransPlan.QP_OBJ_P%>.value == "0" ) {
		strErr = strErr + "\n请选择客户！";
	}
	if ( inForm.<%=TransPlan.QP_TRANS_L%>.value == "0" ) {
		strErr = strErr + "\n请选择运输线路！";
	}
	if ( !checkDate( inForm.<%=TransPlan.QP_PLAN_DATE%> ,false,"") ) {
		strErr = strErr + "\n计划日期格式不正确！";
	}
	if ( inForm.<%=TransPlan.QP_PLAN_K%>.value == "0" ) {
		strErr = strErr + "\n请选择运输类型！";
	}
	if ( isNull(inForm.<%=TransPlan.QP_NE_ZH1%>) ) {
		strErr = strErr + "\n发货方为空！";
	}
	if ( inForm.<%=TransPlan.QP_REMARK_1%>.value.length > 45 ) {
		strErr = strErr + "\其它说明(收货)限50个字符！";
	}
	if ( isNull(inForm.<%=TransPlan.QP_NE_ZH2%>) ) {
		strErr = strErr + "\n收货方为空！";
	}
	if ( inForm.<%=TransPlan.QP_REMARK_2%>.value.length > 45 ) {
		strErr = strErr + "\其它说明(收货)限50个字符！";
	}
	if ( myForm.<%=TransPlan.QP_PLAN_K%>.value == "返空提货" || myForm.<%=TransPlan.QP_PLAN_K%>.value == "返空送货" ) {
		if (!checkQty(inForm.<%=TransPlan.QP_QTY_METER_R%>, false, false, 3, 0)) {
			strErr = strErr + "\n返空占车米数不允许为空，且只接受整数！";
		}
		if ( isNull(inForm.<%=TransPlan.QP_NE_RECYCLE%>) ) {
			strErr = strErr + "\n返空仓库为空！";
		}
	}

	if ( !checkQty( inForm.<%=TransPlan.QP_QTY_W%> ,true, false, 3, 3) ) {
		strErr = strErr + "\n重量允许为空，最多3位小数！";
	}
	if ( !checkQty( inForm.<%=TransPlan.QP_QTY_V%> ,true, false, 3, 3) ) {
		strErr = strErr + "\n体积允许为空，最多3位小数！";
	}
	if ( !checkQty( inForm.<%=TransPlan.QP_QTY_METER%> ,true, false, 3, 0) ) {
		strErr = strErr + "\n占车米数允许为空，只接受整数！";
	}

	if ( strErr != "" ) {
		alert( strErr );
		return false;
	}
	isNullWithDefault( inForm.<%=TransPlan.QP_QTY_METER%>, "0" );
	isNullWithDefault( inForm.<%=TransPlan.QP_QTY_METER_R%>, "0" );
	isNullWithDefault( inForm.<%=TransPlan.QP_QTY_W%>, "0" );
	isNullWithDefault( inForm.<%=TransPlan.QP_QTY_V%>, "0" );
	return confirm( "确定提交吗？" );
}
</script>
</body>
</html>