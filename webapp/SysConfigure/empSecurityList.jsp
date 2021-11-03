<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import = "com.cuanfun.utils.*" %>
<%@ page import = "com.cuanfun.database.*" %>
<%@ page import = "com.cuanfun.control.*" %>
<%@ page import = "com.cuanfun.cache.*" %>
<%@ page import = "com.cuanfun.enumeration.*" %>
<%@ page import = "xms.*" %>
<%@ page import = "xms.queries.sys.*" %>
<%
	int PAGE_TAG = CtrlConstants.PG_EMP_SECURITY;
	boolean acceptInnerUser = true;
	String[] needAstricts = { SysAstricts.QX_SYS_CONFIGURE }; 
%>
<%@ include file="/include/inc_header.logic" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>员工权限信息</title>   
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />    
<link rel="stylesheet" type="text/css" href="/css/default.css" />
</head>
  
<body>
<%@ include file="/include/menu_1_Header.block" %>
<%
EmployeeSecurityList empSecurityList = new EmployeeSecurityList( XmsInitial.getDataSource() );
empSecurityList.setIfSystemAdministrator( AbstractDaemon.isSystemAdministrator(request) );

String page_size = request.getParameter( EmployeeSecurityList.QP_PAGE_SIZE );
String cur_page = request.getParameter( EmployeeSecurityList.QP_CUR_PAGE );
if ( page_size != null ) {
	empSecurityList.setPageSize( Integer.parseInt(page_size) );
}
if ( cur_page != null ) {
	empSecurityList.setCurrentPage( Integer.parseInt( cur_page ) );
}		
String[] arr_params = empSecurityList.getParamSerial();
for ( int j=0; j<arr_params.length; j++ ) {
	empSecurityList.setParamValue( arr_params[j], request.getParameter( arr_params[j]) );
}
CommonSet dataSet = empSecurityList.getQueryResult( );
%>
<div id="container">
<%@ include file="/include/menu_2_SysConfigure.block" %>

<form name="queryForm" method="post" action="/SysConfigure/empSecurityList.jsp" >
<table width="100%" border="0" cellspacing="1" cellpadding="0">
	<tr class="query_tr">
	<td align="right">
		<select name="<%=EmployeeSecurityList.QP_COMPANY%>" ><%=Company.ALL_OPTIONS%></select>&nbsp;&nbsp;&nbsp;&nbsp;
		<select name="<%=EmployeeSecurityList.QP_PART%>" ><%=MasterPart.ALL_OPTIONS %></select>&nbsp;&nbsp;&nbsp;&nbsp;
		姓名:
		<input type="text" id="nameZh" name="<%=EmployeeSecurityList.QP_EMP_ZH%>"  size="8" maxlength="10" />&nbsp;&nbsp;&nbsp;&nbsp;
		<select name="<%=EmployeeSecurityList.QP_CAN_LOGIN%>"><%=EnumConstants.CAN_LOGIN_ALL_OPTIONS%></select>&nbsp;&nbsp;&nbsp;&nbsp;
		<select name="<%=EmployeeSecurityList.QP_ROLE%>"><option value="All">-所属角色-</option><%=MasterRole.getInstance().getAllOptions()%></select>&nbsp;&nbsp;&nbsp;&nbsp;
		<select name="<%=EmployeeSecurityList.QP_SYS_FLG%>" >
		<option value="All">-员工状态-</option>
		<option value="Normal">正常</option>
		<option value="Dimission">已离职</option>
		<option value="Cancel">已删除</option>
		</select>&nbsp;&nbsp;
		<input type="submit" name="btnQuery" value="开始检索" /></td>
	</tr>
</table>

<table width="100%" border="0" cellpadding="0" cellspacing="1">
	<thead>
	<tr>
	<th width="50">序号</th>
	<th width="120">工号/姓名</th>
	<th width="70">检索代码</th>
	<th width="120">所属公司/部门</th>
	<th width="90">可登录/有效</th>
	<th width="70">权限级别</th>
	<th width="80">登录名</th>
	<th width="120">所属角色</th>
	<th>访问 公司列表(行政/业务)</th>
	<th width="75">员工状态</th>
	<th width="100">操作</th>
	</tr>
	</thead>
	
	<tbody>
	<%
	int baseIndex = empSecurityList.getBaseIndex();
	for ( int j = 0; j < dataSet.getRowCount(); j ++ ) {
		String sysFlg = (String)dataSet.getValue(j,"sys_flg");
		String mCompanies = (String)dataSet.getValue(j,"m_companies");
		String bCompanies = (String)dataSet.getValue(j,"b_companies");
		boolean isReturn = mCompanies.length()>20 && bCompanies.length()>20; 
		%>
		<tr class="<%=j%2==1?"content1_tr":"content2_tr" %>"> 
		<td align="center"><%=baseIndex+j %></td>
		<td align="left">&nbsp;<a href="javascript:void(0)" onclick="javascript:openUpdate('<%=dataSet.getValue(j,"emp")%>');"><%=dataSet.getValue(j,"job_id")%>&nbsp;<%=dataSet.getValue(j,"emp_zh")%></a></td>
		<td align="center"><%=dataSet.getValue(j,"init_spell")%></td>
		<td align="left">&nbsp;<%=Company.getDisplay((String)dataSet.getValue(j,"company"))%>&nbsp;<%=dataSet.getValue(j,"part_zh")%></td>
		<td align="center"><%=dataSet.getValue(j,"can_login")%>&nbsp;/&nbsp;<%=dataSet.getValue(j,"if_valid")%></td>
		<td align="center"><%=dataSet.getValue(j,"astrict_level")%></td>
		<td align="center"><%=dataSet.getValue(j,"login_name")%></td>
		<td align="center"><%=CommonUtils.formatString(dataSet.getValue(j,"role_zh"),"-")%></td>
		<td align="left">&nbsp;[<%=dataSet.getValue(j,"m_companies")%>]&nbsp;<%=isReturn?"<br/>&nbsp;":""%>[<%=bCompanies%>]</td>
		<td align="center"><%=EnumConstants.getEmployeeStatusDisplay(sysFlg)%></td>
		<td align="left">&nbsp;<a href="javascript:void(0)" onclick="javascript:resetPsw('<%=dataSet.getValue(j,"emp")%>')">重置密码</a>		
			<%
			if ( sysFlg.equals( "Cancel" ) ) {
				%><a href="javascript:void(0)" onclick="javascript:checkDelete('<%=dataSet.getValue(j,"emp")%>');">删除</a><%
			}
			%>
		</td>
		</tr>
		<%
	}
	%>
	</tbody>
</table>

<table width="100%" border="0" cellpadding="0" cellspacing="1">
	<tfoot><tr><td align="right"><%=XmsUtils.buildPageSeeker2("queryForm",empSecurityList.getCurrentPage(),empSecurityList.numOfPages()) %></td></tr></tfoot>
</table>
</form> 
</div><!-- container -->
<%@ include file="/include/menu_3_Footer.block" %>

<script type="text/javascript" src="/include/stc_validate.js"></script>
<script type="text/javascript" src="/include/stc_function.js"></script>
<script type="text/javascript">
	queryForm.<%=AbstractQuery.QP_PAGE_SIZE%>.value = "<%=empSecurityList.getPageSize()%>";
	setSelectValue( queryForm.<%=EmployeeSecurityList.QP_COMPANY%>, "<%=empSecurityList.getParamValue(EmployeeSecurityList.QP_COMPANY)%>" );
	setSelectValue( queryForm.<%=EmployeeSecurityList.QP_PART%>, "<%=empSecurityList.getParamValue(EmployeeSecurityList.QP_PART)%>" );
	setSelectValue( queryForm.<%=EmployeeSecurityList.QP_CAN_LOGIN%>, "<%=empSecurityList.getParamValue(EmployeeSecurityList.QP_CAN_LOGIN)%>" );
	setSelectValue( queryForm.<%=EmployeeSecurityList.QP_ROLE%>, "<%=empSecurityList.getParamValue(EmployeeSecurityList.QP_ROLE)%>" );
	queryForm.<%=EmployeeSecurityList.QP_EMP_ZH%>.value = "<%=empSecurityList.getParamValue(EmployeeSecurityList.QP_EMP_ZH)%>";
	queryForm.<%=EmployeeSecurityList.QP_SYS_FLG%>.value = "<%=empSecurityList.getParamValue(EmployeeSecurityList.QP_SYS_FLG)%>";	
</script>

<script type="text/javascript">
function openUpdate( id ) {
	var strStatus = "toolbar=no,Scrollbars=yes,status=yes,width=640,height=480,left=" + getCenteredLeft(640) + ",top=" + getCenteredTop(480);
	window.open( "/SysConfigure/empSecurityUpdate.jsp?QpEmp="+id, "_blank", strStatus );
}
function resetPsw( emp ) {
	if ( confirm("您要重置该用户的登录密码吗？") && confirm("您确定要重置该用户的登录密码吗？") ) {
		window.showModalDialog("/SysConfigure/processSysConfigure.jsp?Action=ResetPsw&ID="+emp, self, "status:false;dialogWidth:640px;dialogHeight:480px"); 
	}
}
function checkDelete( id ) {
	if ( confirm('您确定删除吗？') ) {
		window.open( "/SysConfigure/processSysConfigure.jsp?Action=EmployeeDelete&QpEmp="+id, "_blank", "toolbar=no,Scrollbars=yes,status=yes,width=640,height=480,left=0,top=0" );
	}
}
</script>
</body>
</html>
