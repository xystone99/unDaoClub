<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import="com.undao.enumeration.SysAstricts" %>
<%@ page import="com.undao.enumeration.EnumConstants" %>
<%@ page import="xms.XmsInitial" %>
<%@ page import="xms.beans.wareHouse.TruckIdle" %>
<%@ page import="com.undao.control.AbstractDaemon" %>
<%
	int PAGE_TAG = 0;
	boolean acceptInnerUser = true;
	String[] needAstricts = {SysAstricts.QX_WH_UPDATE  };
%>
<%@ include file="../include/inc_header.logic" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>  
<title>闲置车辆登记</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<link rel="stylesheet" type="text/css" href="../css/childWindow.css" />
<link rel="stylesheet" type="text/css" href="../css/jquery-ui-1.10.4.custom.css" />
</head>

<body>
<form name="myForm" method="post" action="processBusiness.jsp" onsubmit="return checkValue(this);">
<table style="width:590px" cellspacing="0" cellpadding="0">
	<tr class="head_tr">
	<td width="100%" align="left"><b>闲置(缺少)车辆登记&gt;&gt;</b></td>
	</tr>
	<tr class="empty_tr2"><td colspan="6"></td></tr>
</table><br/>

<table style="width:590px" cellspacing="0" cellpadding="0">
	<tr class="content_tr30">
	<td align="right">当前仓库:</td>
	<td ></td>
	<td align="left" colspan="4"><input type="text" name="acCus" size="20" maxlength="20" class="input_text" value="<%=AbstractDaemon.getCurrentCompanyName(request)%>" readonly="readonly" /></td>
	</tr>

	<tr class="empty_tr"><td colspan="6"></td></tr>

	<tr class="content_tr30">
	<td align="right">闲置类型:</td>
	<td></td>
	<td lign="left" colspan="4"><select name="<%=TruckIdle.QP_IDLE_K%>" class="select" onchange="javascript:changeIdleK(this);"><%=EnumConstants.TRUCK_IDLE_K_OPTIONS%></select></td>
	</tr>

	<tr class="content_tr30">
	<td align="right">车牌号（车型）:</td>
	<td></td>
	<td align="left" colspan="4">
		<input type="text" id="acTruck" name="acTruck" size="10" maxlength="20" placeholder="关键字" class="input_text" />
		<input type="text" name="<%=TruckIdle.QP_PLATE_NUMBER%>" size="20" maxlength="20" class="input_text" />
		<input type="hidden" name="<%=TruckIdle.QP_TRUCK%>" value="0" />
		</td>
	</tr>

	<tr class="content_tr30">
	<td align="right">驾驶员:</td>
	<td></td>
	<td align="left" colspan="4">
		<input type="text" id="acDriver" name="acDriver" size="10" maxlength="20" placeholder="姓名首字母" class="input_text" />
		<input type="text" name="<%=TruckIdle.QP_TEL_DRIVER%>" size="20" maxlength="20" class="input_text" />
		<input type="hidden" name="<%=TruckIdle.QP_DRIVER%>" value="0" />&nbsp;&nbsp;&nbsp;
	</td>
	</tr>

	<tr class="content_tr">
	<td width="19%" align="right">闲置起始日期:</td>
	<td width="1%"></td>
	<td width="30%" align="left"><input type="text" id="thDate1" name="<%=TruckIdle.QP_START_DATE%>" size="12" maxlength="10" class="input_text" /></td>
	<td width="14%" align="right">结束日期:</td>
	<td width="1%"></td>
	<td width="35%" align="left"><input type="text" id="thDate2" name="<%=TruckIdle.QP_END_DATE%>" size="12" maxlength="10" class="input_text" /></td>
	</tr>

	<tr class="empty_tr"><td colspan="6"></td></tr>

	<tr class="content_tr">
	<td align="right">其它说明:</td>
	<td></td>
	<td align="left" colspan="4"><input type="text" name="<%=TruckIdle.QP_REMARK%>" size="50" maxlength="50" class="input_text" placeholder="原因等其它说明" /></td>
	</tr>
	<tr class="empty_tr"><td colspan="6"></td></tr>
</table><br/>

<table style="width:590px" cellspacing="0" cellpadding="0">
	<tr class="tail_tr">
	<td align="left"><input type="button" name="reset" value="刷新页面" class="input_btn" onclick="javascript:reloadCurrentPage();" /></td>
	<td align="right">
		<input type="hidden" name="Action" value="TruckIdleNew" />
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
	setIntervalDate( myForm.<%=TruckIdle.QP_START_DATE%>, 1 );
	setIntervalDate( myForm.<%=TruckIdle.QP_END_DATE%>, 1 );
	$("#thDate1,#thDate2").datepicker({dateFormat: 'yy-mm-dd'});
</script>

<script type="text/javascript">
function changeIdleK( obj ) {
	if ( obj.value == "Idle" ) {
		$("input[name='acTruck']").val("").removeAttr("readonly");
		$("input[name='acDriver']").val("").removeAttr("readonly");

		$("input[name='<%=TruckIdle.QP_PLATE_NUMBER%>']").val("").attr("readonly","readonly");
		$("input[name='<%=TruckIdle.QP_TEL_DRIVER%>']").val("").attr("readonly","readonly");

	} else if ( obj.value == "Lack" ) {
		$("input[name='acTruck']").val("空缺车辆").attr("readonly","readonly");
		$("input[name='acDriver']").val("空缺司机").attr("readonly","readonly");

		$("hidden[name='<%=TruckIdle.QP_TRUCK%>']").val("0");
		$("hidden[name='<%=TruckIdle.QP_TRUCK%>']").val("0");

		$("input[name='<%=TruckIdle.QP_PLATE_NUMBER%>']").val("").removeAttr("readonly");
		$("input[name='<%=TruckIdle.QP_TEL_DRIVER%>']").val("").removeAttr("readonly");
	}
}

$("#acTruck").autocomplete({ source: "<%=XmsInitial.getContextPath()%>/fetchtruck?Action=TruckIdle", minLength: 1, select: function(event, ui ) {
	myForm.<%=TruckIdle.QP_TRUCK%>.value = ui.item.ID;
	myForm.<%=TruckIdle.QP_PLATE_NUMBER%>.value = ui.item.PlateNumber;
	if ( ui.item.ID == "0" ) {
		$("input[name='<%=TruckIdle.QP_PLATE_NUMBER%>']").removeAttr("readonly");
	} else {
		$("input[name='<%=TruckIdle.QP_PLATE_NUMBER%>']").attr("readonly","readonly");
	}
}});

$("#acDriver").autocomplete({ source: "<%=XmsInitial.getContextPath()%>/fetchdriver?Action=TruckIdle", minLength: 1, select: function(event, ui ) {
	myForm.<%=TruckIdle.QP_DRIVER%>.value = ui.item.ID;
	myForm.<%=TruckIdle.QP_TEL_DRIVER%>.value = ui.item.Name;
	if ( ui.item.ID == "0" ) {
		$("input[name='<%=TruckIdle.QP_TEL_DRIVER%>']").removeAttr("readonly");;
	} else {
		$("input[name='<%=TruckIdle.QP_TEL_DRIVER%>']").attr("readonly","readonly");
	}
}});

function checkValue( inForm ) {
	var strErr = new String( "" );
	if ( isNull(inForm.<%=TruckIdle.QP_PLATE_NUMBER%>) ) {
		strErr = strErr + "\n车牌号(车型)为空！";
	}
	if ( !checkDate( inForm.<%=TruckIdle.QP_START_DATE%> ,false,"") ) {
		strErr = strErr + "\n闲置起始日期为空或格式不正确！";
	}
	if ( !checkDate( inForm.<%=TruckIdle.QP_END_DATE%> ,false,"") ) {
		strErr = strErr + "\n闲置结束日期为空或格式不正确！";
	}

	if ( strErr != "" ) {
		alert( strErr );
		return false;
	}
	return confirm( "确定提交吗？" );
}
</script>
</body>
</html>