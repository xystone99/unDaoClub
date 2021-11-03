<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import = "xms.beans.customerService.*" %>
<%@ page import="com.undao.enumeration.SysAstricts" %>
<%@ page import="xms.procedures.WareReturnDispatch" %>
<%@ page import="com.undao.database.CommonSet" %>
<%@ page import="com.undao.database.DBUtils" %>
<%@ page import="xms.XmsInitial" %>
<%@ page import="xms.queries.QueryConstants" %>
<%@ page import="com.undao.enumeration.EnumConstants" %>
<%
	int PAGE_TAG = 0;
	boolean acceptInnerUser = true;
	String[] needAstricts = {SysAstricts.QX_WH_UPDATE  };
%>
<%@ include file="../include/inc_header.logic" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>  
<title>仓库回报车辆出入</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<link rel="stylesheet" type="text/css" href="../css/childWindow.css" />
<link rel="stylesheet" type="text/css" href="../css/jquery-ui-1.10.4.custom.css" />
</head>

<body>
<%
	String dispt_id = request.getParameter("DisptID");
	String trans_p = request.getParameter("TransP");
	CommonSet dataSet = DBUtils.prepareQuery(XmsInitial.getDataSource(), QueryConstants.SQL_PLAN_AND_DISPATCH, dispt_id, trans_p );
	if ( dataSet.getRowCount() < 1 ) {
		return;
	}
%>
<form name="myForm" method="post" action="processBusiness.jsp" onsubmit="return checkValue(this);">
<table cellspacing="0" cellpadding="0">
	<tr class="head_tr">
	<td width="100%" align="left"><b>仓库回报车辆出入&gt;&gt;</b></td>
	</tr>
	<tr class="empty_tr2"><td colspan="6"></td></tr>
</table><br/>

<table id="date" cellspacing="0" cellpadding="0">
	<tr class="content_tr30">
	<td align="right" width="16%">运输线路:</td>
	<td width="2%"></td>
	<td align="left"><%=dataSet.getValue("ne_zh1")%>&nbsp;--->&nbsp;<%=dataSet.getValue("ne_zh2")%></td>
	</tr>

	<tr class="content_tr30">
	<td align="right">车辆信息:</td>
	<td ></td>
	<td align="left"><%=dataSet.getValue("plate_number")%>&nbsp;&nbsp;<%=dataSet.getValue("tel_driver")%></td>
	</tr>

	<tr class="content_tr30">
	<td align="right">回报内容:</td>
	<td></td>
	<td align="left"><input type="text" name="<%=WareReturnDispatch.QP_REMARK%>" size="60" maxlength="50" value="<%=dataSet.getValue("wh_remark")%>" class="input_text" placeholder="简洁描述" /></td>
	</tr>

	<tr class="empty_tr"><td colspan="6"></td></tr>

</table><br/>

<table cellspacing="0" cellpadding="0">
	<tr class="tail_tr">
	<td align="left"><input type="button" name="reset" value="清空内容" class="input_btn" onclick="javascript:clearFeedback();" />
		&nbsp;<select name="whStatements" class="select" onchange="javascript:selectFeedback(this)"><option>--选择标准回报语句--</option><%=EnumConstants.WH_RETURN_DISPATCH_OPTIONS%></select></td>
	<td align="right">
		<input type="hidden" name="Action" value="ReturnDispatch" />
		<input type="hidden" name="<%=WareReturnDispatch.QP_DISPT%>" value="<%=dispt_id%>" />
		<input type="hidden" name="<%=WareReturnDispatch.QP_TRANS_P%>" value="<%=trans_p%>" />
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
function clearFeedback( ) {
	if ( confirm( "确定清空回报内容吗？" ) ) {
		myForm.<%=WareReturnDispatch.QP_REMARK%>.value = "";
	}
}
function selectFeedback( obj ) {
	if ( obj.selectedIndex > 0 ) {
		myForm.<%=WareReturnDispatch.QP_REMARK%>.value = myForm.<%=WareReturnDispatch.QP_REMARK%>.value + obj.value;
	}
}
function checkValue( inForm ) {
	var strErr = new String( "" );
	if ( isNull(inForm.<%=WareReturnDispatch.QP_REMARK%>) ) {
		strErr = strErr + "\n回报信息为空！";
	}
	if ( inForm.<%=WareReturnDispatch.QP_REMARK%>.value.length > 45 ) {
		alert( "回报信息限50个字符！" );
		return;
	}
	if ( isNull(inForm.<%=WareReturnDispatch.QP_REMARK%>) ) {
		return confirm( "回报信息为空！确认提交吗？" );
	} else {
		return confirm( "确定回报吗？" );
	}
}
</script>
</body>
</html>