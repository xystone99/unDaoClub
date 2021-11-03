<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import = "com.cuanfun.utils.*" %>
<%@ page import = "com.cuanfun.database.*" %>
<%@ page import = "com.cuanfun.control.*" %>
<%@ page import = "com.cuanfun.cache.*" %>
<%@ page import = "com.cuanfun.enumeration.*" %>
<%@ page import = "xms.*" %>
<%
	int PAGE_TAG = CtrlConstants.PG_CONSOLE_PANEL;
	boolean acceptInnerUser = true;
	String[] needAstricts = { SysAstricts.QX_SYS_CONFIGURE };
%>
<%@ include file="/include/inc_header.logic" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>控制台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />    
<link rel="stylesheet" type="text/css" href="/css/default.css" />
</head>

<body>
<%@ include file="/include/menu_1_Header.block" %>
 
<div id="container">
<%@ include file="/include/menu_2_SysConfigure.block" %>

<% 
	String PERFORM_MONTH_OPTIONS = SystemicVariables.getInstance().getMonthOptions( );
%>
<form name="myForm">
<table width="100%" border="0" cellspacing="1" cellpadding="0" align="center">
	<tr class="query_tr">
	<td width="100%" align="left"><b>绩效考核设定&gt;&gt;</b></td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="1" cellpadding="0" align="center">
	<tr class="content1_tr">
    <td align="left" width="100">系统参数设置：</td>
	<td align="left" width="500">
		&nbsp;打分月份：<select name="PerformMonth"><option value="0">--请选择月份--</option><%=PERFORM_MONTH_OPTIONS%></select>&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;结账月份：<select name="PerformMonthC"><option value="0">--请选择月份--</option><%=PERFORM_MONTH_OPTIONS%></select>
		</td>
	<td align="left"><input type="button" name="btnPerformStatus" value="设定系统参数" onclick="javascript:changePerformVariables();" /></td>
	</tr>
	
	<tr class="content1_tr">
    <td align="left" width="100">每月的初始化：</td>
	<td align="left">
		&nbsp;设定月份：<select name="PerformMonthI"><option value="0">--请选择月份--</option><%=PERFORM_MONTH_OPTIONS%></select>
		</td>
	<td align="left"><input type="button" name="btnPerformStatus" value="初始化选定月份" onclick="javascript:initializePerformMonth();" /></td>
	</tr>
</table><br/>

<table width="100%" border="0" cellspacing="1" cellpadding="0" align="center">
	<tr class="query_tr">
	<td width="100%" align="left"><b>薪酬福利设定&gt;&gt;</b></td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="1" cellpadding="0" align="center">
	<tr class="content1_tr">
    <td align="left" width="100">系统参数设置：</td>
	<td align="left" width="500">
		&nbsp;发放月份：<select name="SalaryMonth"><option value="0">--请选择月份--</option><%=PERFORM_MONTH_OPTIONS%></select>&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;结账月份：<select name="SalaryMonthC"><option value="0">--请选择月份--</option><%=PERFORM_MONTH_OPTIONS%></select>&nbsp;&nbsp;&nbsp;&nbsp;
		<select name="SalaryUpdate"><option value="Accept">允许修改</option><option value="Refused">禁止修改</option></select>
		</td>
	<td align="left"><input type="button" name="btnPerformStatus" value="设定系统参数" onclick="javascript:changeSalaryVariables();" /></td>
	</tr>
	
	<tr class="content1_tr">
    <td align="left" width="100">每月的初始化：</td>
	<td align="left">
		&nbsp;设定月份：<select name="SalaryMonthI"><option value="0">--请选择月份--</option><%=PERFORM_MONTH_OPTIONS%></select>
		</td>
	<td align="left"><input type="button" name="btnPerformStatus" value="初始化选定月份" onclick="javascript:initializeSalaryMonth();" /></td>
	</tr>
</table><br/>

<table width="100%" border="0" cellspacing="1" cellpadding="0" align="center">
	<tr class="query_tr">
	<td width="100%" align="left"><b>系统管理设定&gt;&gt;</b></td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="1" cellpadding="0" align="center">
	<tr class="content1_tr">
    <td align="left" width="100">系统参数设置：</td>
	<td align="left" width="500">
		&nbsp;是否允许登录：<select name="AcceptLogin"><option value="Yes">允许登录</option><option value="No">禁止登录</option></select>&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;是否只读状态：<select name="AcceptUpdate"><option value="Yes">允许修改</option><option value="No">禁止修改</option></select>&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
	<td align="left"><input type="button" name="btnSysVariables" value="设定系统状态" onclick="javascript:changeSystemStatus();" /></td>
	</tr>
</table>
</form>

</div><!-- container -->
<%@ include file="/include/menu_3_Footer.block" %>

<script type="text/javascript" src="/include/stc_validate.js"></script>
<script type="text/javascript" src="/include/stc_function.js"></script>
<script type="text/javascript">
	setSelectValue(myForm.PerformMonth, "<%=SystemicVariables.getInstance().getVariableValue(SystemicVariables.CUR_PERFORM_MONTH)%>");
	setSelectValue(myForm.PerformMonthC, "<%=SystemicVariables.getInstance().getVariableValue(SystemicVariables.PERFORM_CLOSE_MONTH)%>");
	myForm.PerformMonthI.value = "0";
	setSelectValue(myForm.SalaryMonth, "<%=SystemicVariables.getInstance().getVariableValue(SystemicVariables.CUR_SALARY_MONTH)%>");
	setSelectValue(myForm.SalaryMonthC, "<%=SystemicVariables.getInstance().getVariableValue(SystemicVariables.SALARY_CLOSE_MONTH)%>");
	setSelectValue(myForm.SalaryUpdate, "<%=SystemicVariables.getInstance().getVariableValue(SystemicVariables.SALARY_UPDATE)%>");
	myForm.SalaryMonthI.value = "0";
	setSelectValue(myForm.AcceptLogin, "<%=SystemicVariables.getInstance().getVariableValue(SystemicVariables.IF_ACCEPT_LOGIN)%>");
	setSelectValue(myForm.AcceptUpdate, "<%=SystemicVariables.getInstance().getVariableValue(SystemicVariables.IF_ACCEPT_UPDATE)%>");
</script>

<script type="text/javascript">
function changePerformVariables( ) {
	if ( myForm.PerformMonth.value == "0" ) {
		alert( "请选择打分月份！" );
		return;
	}
	if ( myForm.PerformMonthC.value == "0" ) {
		alert( "请选择结账月份！" );
		return;
	}
	if ( confirm( "您确定更改绩效考核的系统参数吗？" ) ) {
		window.open( "/SysConfigure/processRootBuild.jsp?Action=ChangePerformVariables&Month="+myForm.PerformMonth.value+"&CloseMonth="+myForm.PerformMonthC.value, "_blank", "toolbar=no,Scrollbars=yes,status=yes,width=640,height=480,left=0,top=0" );
	}
}
function initializePerformMonth( ) {
	if ( myForm.PerformMonthI.value == "0" ) {
		alert( "请选择初始化月份！" );
		return;
	}
	if ( confirm( "您确订初始化该月份吗?" ) ) {
		if ( confirm( "此操作不可恢复，该月数据将被全部清除。您确订继续吗?" ) ) {
			window.open( "/SysConfigure/processRootBuild.jsp?Action=InitializePerformMonth&Month="+myForm.PerformMonthI.value, "_blank", "toolbar=no,Scrollbars=yes,status=yes,width=640,height=480,left=0,top=0" );
		}
	}
}
function changeSalaryVariables( ) {
	if ( myForm.SalaryMonth.value == "0" ) {
		alert( "请选择发放月份！" );
		return;
	}
	if ( myForm.SalaryMonthC.value == "0" ) {
		alert( "请选择结账月份！" );
		return;
	}
	if ( confirm( "您确定更改薪酬模块的系统参数吗？" ) ) {
		window.open( "/SysConfigure/processRootBuild.jsp?Action=ChangeSalaryVariables&Month="+myForm.SalaryMonth.value+"&CloseMonth="+myForm.SalaryMonthC.value+"&SalaryUpdate="+myForm.SalaryUpdate.value, "_blank", "toolbar=no,Scrollbars=yes,status=yes,width=640,height=480,left=0,top=0" );
	}
}
function initializeSalaryMonth( ) {
	if ( myForm.SalaryMonthI.value == "0" ) {
		alert( "请选择初始化月份！" );
		return;
	}
	if ( confirm( "您确订初始化该月份吗?" ) ) {
		if ( confirm( "此操作不可恢复，该月数据将被全部清除。您确订继续吗?" ) ) {
			window.open( "/SysConfigure/processRootBuild.jsp?Action=InitializeSalaryMonth&Month="+myForm.SalaryMonthI.value, "_blank", "toolbar=no,Scrollbars=yes,status=yes,width=640,height=480,left=0,top=0" );
		}
	}
}
function changeSystemStatus( ) {
	if ( confirm( "您确定重新设置系统状态吗？" ) ) {
		window.open( "/SysConfigure/processRootBuild.jsp?Action=ChangeSystemStatus&AcceptLogin="+myForm.AcceptLogin.value+"&AcceptUpdate="+myForm.AcceptUpdate.value, "_blank", "toolbar=no,Scrollbars=yes,status=yes,width=640,height=480,left=0,top=0" );
	}
}
</script>

</body>
</html>
