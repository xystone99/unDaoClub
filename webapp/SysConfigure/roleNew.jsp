<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import="com.undao.enumeration.SysAstricts" %>
<%@ page import="xms.beans.sysConfigure.Role" %>
<%@ page import="com.undao.enumeration.PageHref" %>

<%
	int PAGE_TAG = 0;
	boolean acceptInnerUser = true;
	String[] needAstricts = { SysAstricts.QX_ACCESS_REFUSED  };
%>
<%@ include file="../include/inc_header.logic" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>  
<title>新建角色</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<link rel="stylesheet" type="text/css" href="../css/childWindow.css" />
</head>

<body>
<form name="myForm" method="post" action="processRootBuild.jsp" onsubmit="return checkValue(this);">
<table cellspacing="0" cellpadding="0">
	<tr class="head_tr">
	<td width="100%" align="left"><b>新建角色&gt;&gt;</b></td>
	</tr>
</table><br/>

<table cellspacing="0" cellpadding="0">
	<tr class="content_tr">
	<td align="right">名&nbsp;&nbsp;称：</td>
	<td >&nbsp;</td>
	<td align="left" colspan="3"><input type="text" name="<%=Role.QP_NAME_ZH%>" size="20" maxlength="20" /></td>
	</tr>

	<tr class="content_tr">
	<td align="right">排序标记：</td>
	<td >&nbsp;</td>
	<td align="left"><input type="text" name="<%=Role.QP_SORT_TAG%>" size="8" maxlength="5" value="ABC" /></td>
	<td align="right">角色首页：</td>
        <td align="left"><select name="<%=Role.QP_HREF_INDEX %>"><option value="0">--选择登录首页--</option>><%=PageHref.PURE_OPTIONS %></select></td>
	</tr>
    
    <tr class="content_tr" valign="bottom">
	<td width="18%" align="right">设定角色权限：</td>
	<td width="1%"></td>
	<td width="29%" align="left">可选权限</td>
	<td width="15%"></td>
	<td width="37%" align="left">已选权限</td>
	</tr>
		
	<tr bgcolor="#eeeeee"> 
	<td ></td>
	<td ></td>
	<td align="left"><select name="sHang" multiple="multiple" style="width:180px" size="22"><%=SysAstricts.PURE_OPTIONS %></select></td>
	<td valign="middle" align="center">
		<input type="button" value="&gt;" style="width:50px" onclick="javascript:moveOptions(sHang, sHold);" /><br/><br/>
		<input type="button" value="&gt;&gt;" style="width:50px" onclick="javascript:moveOptionsAll(sHang, sHold);" /><br/><br/>
		<input type="button" value="&lt;" style="width:50px" onclick="javascript:moveOptions(sHold, sHang);" /><br/><br/>
		<input type="button" value="&lt;&lt;" style="width:50px" onclick="javascript:moveOptionsAll(sHold, sHang);" /><br/><br/>
	</td>
	<td align="left"><select name="sHold" multiple="multiple" style="width:168px" size="22"></select></td>
	</tr>
	
	
	<tr class="content_tr">
	<td align="right">备注信息：</td>
	<td >&nbsp;</td>
	<td align="left" colspan="3"><input type="text" name="<%=Role.QP_REMARK%>" size="60" maxlength="80" /></td>
	</tr>
</table><br/>

<table cellspacing="0" cellpadding="0">
	<tr class="tail_tr">
	<td width="100%" align="right">
		<input type="hidden" name="Action" value="RoleNew" />
		<input class="input_btn" type="hidden" name="<%=Role.QP_ASTRICTS %>" />
		<input class="input_btn" type="reset" name="reset" value="重新输入" />
		<input class="input_btn" type="submit" name="Submit" value="提交信息" />
	</td>
	</tr>
</table>
</form>

<script type="text/javascript" src="../include/stc_validate.js"></script>
<script type="text/javascript" src="../include/stc_function.js"></script>

<script type="text/javascript">
function checkValue( inForm ) {
	var strErr = new String( "" );
	if ( isNull( inForm.<%=Role.QP_NAME_ZH%> ) ) {
		strErr = strErr + "\n名称不允许为空！";
	}
	if ( !checkLetterFig( inForm.<%=Role.QP_SORT_TAG%>, false ) ) {
		strErr = strErr + "\n排序标记不允许为空，且只接受英文字母和数字！";
	}
	if ( inForm.<%=Role.QP_HREF_INDEX%>.value == "0" ) {
		strErr = strErr + "\n角色首页未选择！";
	}
	var holdAstricts = "";
	for ( var j=0; j<inForm.sHold.length; j++ ) {
		holdAstricts = holdAstricts + inForm.sHold.options[j].value + "-";
	}
	inForm.<%=Role.QP_ASTRICTS %>.value = holdAstricts;
	if ( strErr != "" ) {
		alert( strErr );
		return false;
	}
	return confirm( "确定提交吗？" );
}
</script>
</body>
</html>