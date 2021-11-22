<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import = "xms.*" %>
<%@ page import="com.undao.control.AbstractDaemon" %>
<%@ page import="xms.procedures.UpdatePassword" %>

<%
	int PAGE_TAG = 0;
	boolean acceptInnerUser = true;
	String[] needAstricts = null;
%>
<%@ include file="../include/inc_header.logic" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>修改账户信息</title>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" /> 
<link rel="stylesheet" type="text/css" href="../css/childWindow.css" />
</head>
 
<body>
<form name="myForm" method="post" action="../Self/processPassword.jsp" onsubmit="return checkValue(this);">
<table style="width:500px" cellspacing="0" cellpadding="0">
	<thead><tr class="head_tr"><th>修改我的帐户信息&gt;&gt;</th></tr></thead>
</table><br/>

<table style="width:500px" cellspacing="0" cellpadding="0">
    <tr class="content_tr30">
	<td width="40%" align="right">原密码：</td>
	<td width="60%" align="left">&nbsp;&nbsp;<input type="password" name="<%=UpdatePassword.QP_LOGIN_PWD%>" size="22" maxlength="20" /></td>
	</tr>
	
	<tr class="content_tr30">
	<td align="right">新用户名：</td>
	<td align="left">&nbsp;&nbsp;<input type="text" name="<%=UpdatePassword.QP_LOGIN_NAME_NEW%>" size="20" maxlength="16" value="<%=loginName%>" <%=loginName.equals(AbstractDaemon.INNER_ADMIN)?"readonly=\"readonly\"":""%> /></td>
	</tr>
	
	<tr class="content_tr30">
	<td align="right">新密码：</td>
	<td align="left">&nbsp;&nbsp;<input type="password" name="<%=UpdatePassword.QP_LOGIN_PWD_NEW%>" size="22" maxlength="20" /></td>
	</tr>

	<tr class="content_tr30">
	<td align="right">确认新密码：</td>
	<td align="left">&nbsp;&nbsp;<input type="password" name="tLoginPwdNew2" size="22" maxlength="20" /></td>
	</tr>
</table><br/>

<table style="width:500px" cellspacing="0" cellpadding="0">
	<tr class="tail_tr">
	<td width="100%" align="right">
		<input type="hidden" name="Action" value="PasswordUpdate" />
		<input type="reset" name="btnReset" value="重新输入" />
		<input type="submit" name="btnSubmit" value="提交更新" /> 
	</td>
	</tr>
</table>
</form>

<script type="text/javascript" src="/include/stc_validate.js"></script>
<script type="text/javascript" src="/include/stc_function.js"></script> 
<script type="text/javascript">
function checkValue( inForm ) {
	var strErr = new String( "" );
	if ( isNull( inForm.<%=UpdatePassword.QP_LOGIN_PWD%> ) ) {
		strErr = strErr + "\n请输入原密码！";
	}
	if ( isNull( inForm.<%=UpdatePassword.QP_LOGIN_NAME_NEW%> ) ) {
		strErr = strErr + "\n请输入新的用户名！";
	}
	if ( !checkLetterFig( inForm.<%=UpdatePassword.QP_LOGIN_PWD_NEW%>, false ) || checkNumeric(inForm.<%=UpdatePassword.QP_LOGIN_PWD_NEW%>, false) ) {
		strErr = strErr + "\n新密码不允许为空，且必须由字母和数字混合组成！";
	}
	if ( inForm.<%=UpdatePassword.QP_LOGIN_PWD_NEW%>.value.length < 6 ) {
		strErr = strErr + "\n密码长度不能少于6位！";
	}
	if ( inForm.<%=UpdatePassword.QP_LOGIN_PWD_NEW%>.value != inForm.tLoginPwdNew2.value ) {
		strErr = strErr + "\n新密码前后两次输入不一致，请重新输入！";
		inForm.<%=UpdatePassword.QP_LOGIN_PWD_NEW%>.value = "";
		inForm.tLoginPwdNew2.value = "";
	}
	if ( strErr != "" ) {
		alert( strErr );
		return false;
	}
	return confirm( "确定修改吗？" );
}
</script>

</body>
</html>
