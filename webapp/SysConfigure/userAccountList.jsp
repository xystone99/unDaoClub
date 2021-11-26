<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import="xms.queries.sysConfigure.UserAccountList" %>
<%@ page import="xms.XmsInitial" %>
<%@ page import="com.undao.control.AbstractDaemon" %>
<%@ page import="com.undao.database.CommonSet" %>
<%@ page import="com.undao.enumeration.EnumConstants" %>
<%@ page import="xms.XmsUtils" %>
<%@ page import="com.undao.database.AbstractQuery" %>
<%@ page import="com.undao.control.CtrlConstants" %>
<%@ page import="com.undao.enumeration.SysAstricts" %>
<%
	int PAGE_TAG = CtrlConstants.PG_USER_ACCOUNT_LIST;
	boolean acceptInnerUser = true;
	String[] needAstricts = { SysAstricts.QX_ASTRICT_CONFIGURE  };
%>
<%@ include file="../include/inc_header.logic" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户账户览</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<link rel="stylesheet" type="text/css" href="../css/default.css" />
<link rel="stylesheet" type="text/css" href="../css/jquery-ui-1.10.4.custom.css" />
</head>

<body>
<%@ include file="../include/menu_1_Header.block" %>
<%
	UserAccountList uAccountList = new UserAccountList( XmsInitial.getDataSource() );
	uAccountList.setCloudID(AbstractDaemon.getCloudID(request));

	String cur_page = request.getParameter( UserAccountList.QP_CUR_PAGE );
	if ( cur_page != null ) {
		uAccountList.setCurrentPage( Integer.parseInt( cur_page ) );
		uAccountList.setPageSize( Integer.parseInt(request.getParameter( UserAccountList.QP_PAGE_SIZE )) );

		String[] arr_params = uAccountList.getParamSerial();
		for ( int j=0; j<arr_params.length; j++ ) {
			uAccountList.setParameterValue( arr_params[j], request.getParameter( arr_params[j]) );
		}
	} else {
		uAccountList.setDefaultParameterValue( );
	}
	uAccountList.executeQuery();
	CommonSet dataSet = uAccountList.getQueryResult( );
%>

<div id="container">
<%@ include file="../include/menu_2_SysConfigure.block" %>

<form name="queryForm" method="post" action="userAccountList.jsp">
<table width="100%" border="0" cellspacing="1" cellpadding="0">
	<tr class="query_tr">
	<td align="right">
		姓名:
		<input type="text" name="<%=UserAccountList.QP_NAME%>" size="10" maxlength="10" class="input_text" />&nbsp;&nbsp;&nbsp;&nbsp;
		<select name="<%=UserAccountList.QP_COMPANY%>" class="select"><option value="All">--当前分公司--</option><%=XmsInitial.getXmsContainer().getMasterCompany().getSelectOptions(AbstractDaemon.getCloudID(request))%></select>&nbsp;&nbsp;&nbsp;&nbsp;
		<select name="<%=UserAccountList.QP_EMP_RELY%>" class="select"><option value="All">--是员工?--</option><option value=">0">是</option><option value="=0">否</option></select>&nbsp;&nbsp;&nbsp;&nbsp;
		<select name="<%=UserAccountList.QP_IF_DRIVER%>" class="select"><option value="All">--驾驶员?--</option><%=EnumConstants.YES_NO_OPTIONS%></select>&nbsp;&nbsp;&nbsp;&nbsp;
		<select name="<%=UserAccountList.QP_CAN_LOGIN%>" class="select"><option value="All">--允许登录?--</option><%=EnumConstants.YES_NO_OPTIONS%></select>&nbsp;&nbsp;&nbsp;&nbsp;
		<select name="<%=UserAccountList.QP_ROLE%>" class="select"><option value="All">--所属角色--</option><%=XmsInitial.getXmsContainer().getMasterRole().getSelectOptions(AbstractDaemon.getCloudID(request))%></select>&nbsp;&nbsp;&nbsp;&nbsp;
		<select name="<%=UserAccountList.QP_ASTRICT_LEVEL%>" class="select"><option value="All">--权限级别--</option><%=EnumConstants.ASTRICT_LEVEL_OPTIONS%></select>&nbsp;&nbsp;&nbsp;&nbsp;
		<select name="<%=UserAccountList.QP_AVALIABLE_COMPANYS%>" class="select"><option value="All">--可访问分公司--</option><%=XmsInitial.getXmsContainer().getMasterCompany().getSelectOptions(AbstractDaemon.getCloudID(request))%></select>&nbsp;&nbsp;&nbsp;&nbsp;
		<select name="<%=UserAccountList.QP_SYS_FLG%>" class="select"><option value="All">--用户状态--</option><%=EnumConstants.USER_STATUS_OPTIONS%></select>&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="submit" name="btnQuery" value="开始检索" class="input_text" />&nbsp;<input type="button" name="btnNew" value="新增用户" class="input_text" onclick="openNew()"  />
	</td>
	</tr>
</table>

<table width="100%" border="0" cellpadding="0" cellspacing="1">
	<thead>
	<tr>
	<th width="50">序号</th>
	<th width="120">姓名</th>
	<th width="100">当前分公司</th>
	<th width="130">手机号码</th>
	<th width="90">速记码</th>
	<th width="90">是员工？</th>
	<th width="90">允许登录？</th>
	<th width="150">登录名</th>
	<th width="100">所属角色</th>
	<th width="90">权限级别</th>
	<th >可访问公司列表</th>
	<th width="90">用户状态</th>
	<th width="90">操作</th>
	</tr>
	</thead>

	<tbody>
	<%
	int baseIndex = uAccountList.getBaseIndex();
	for ( int j = 0; j < dataSet.getRowCount(); j ++ ) {
		Long trans_p = (Long)dataSet.getValue(j,"trans_p");
		%>
		<tr class="<%=j%2==1?"content1_tr":"content2_tr" %>">
		<td align="center"><%=baseIndex+j %></td>
		<td align="center"><%=dataSet.getValue(j,"ne_zh")%></td>
		<td align="center"><%=XmsInitial.getXmsContainer().getMasterCompany().getDisplay((Long)dataSet.getValue(j,"cur_company")) %></td>
		<td align="center"><%=dataSet.getValue(j,"tel")%></td>
		<td align="center"><%=dataSet.getValue(j,"init_spell")%></td>
		<td align="center"><%=((Long)dataSet.getValue(j,"emp_rely")).intValue()>0?"是":"否"%></td>
		<td align="center"><%=dataSet.getValue(j,"can_login").equals("Y")?"允许":"禁止"%></td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"login_name")%></td>
		<td align="center"><%=XmsInitial.getXmsContainer().getMasterRole().getDisplay((Long)dataSet.getValue(j,"role")) %></td>
		<td align="center"><%=((Integer)dataSet.getValue(j,"astrict_level")).toString()%></td>
		<td align="left">&nbsp;<%=XmsInitial.getXmsContainer().getMasterCompany().getAvaliableList((String)dataSet.getValue(j,"available_companys"))%></td>
		<td align="center">&nbsp;<%=EnumConstants.getDisplay( (String)dataSet.getValue(j,"sys_flg") )%></td>
		<td></td>
		</tr>
		<%
	}
	%>
	</tbody>
</table>

<table width="100%" border="0" cellpadding="0" cellspacing="1">
	<tfoot><tr><td align="right"><%=XmsUtils.buildPageSeeker2("queryForm",uAccountList.getCurrentPage(),uAccountList.numOfPages()) %></td></tr></tfoot>
</table>
</form>

</div><!-- container -->
<%@ include file="../include/menu_3_Footer.block" %>
<script type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" src="../js/jquery-ui.min.js"></script>

<script type="text/javascript" src="../include/stc_validate.js"></script>
<script type="text/javascript" src="../include/stc_function.js"></script>
<script type="text/javascript">
	queryForm.<%=UserAccountList.QP_NAME%>.value = "<%=uAccountList.getParameterValue(UserAccountList.QP_NAME)%>";
	queryForm.<%=UserAccountList.QP_COMPANY%>.value = "<%=uAccountList.getParameterValue(UserAccountList.QP_COMPANY)%>";
	queryForm.<%=UserAccountList.QP_EMP_RELY%>.value = "<%=uAccountList.getParameterValue(UserAccountList.QP_EMP_RELY)%>";
	queryForm.<%=UserAccountList.QP_IF_DRIVER%>.value = "<%=uAccountList.getParameterValue(UserAccountList.QP_IF_DRIVER)%>";
	queryForm.<%=UserAccountList.QP_CAN_LOGIN%>.value = "<%=uAccountList.getParameterValue(UserAccountList.QP_CAN_LOGIN)%>";
	queryForm.<%=UserAccountList.QP_ROLE%>.value = "<%=uAccountList.getParameterValue(UserAccountList.QP_ROLE)%>";
	queryForm.<%=UserAccountList.QP_ASTRICT_LEVEL%>.value = "<%=uAccountList.getParameterValue(UserAccountList.QP_ASTRICT_LEVEL)%>";
	queryForm.<%=UserAccountList.QP_AVALIABLE_COMPANYS%>.value = "<%=uAccountList.getParameterValue(UserAccountList.QP_AVALIABLE_COMPANYS)%>";
	queryForm.<%=UserAccountList.QP_SYS_FLG%>.value = "<%=uAccountList.getParameterValue(UserAccountList.QP_SYS_FLG)%>";
	queryForm.<%=AbstractQuery.QP_PAGE_SIZE%>.value = "<%=uAccountList.getPageSize()%>";
</script>

<script type="text/javascript">
function openNew( ) {
	alert( "建设中......" );
	return;
	var strStatus = "toolbar=no,Scrollbars=yes,status=yes,width=800,height=600,left=" + getCenteredLeft(800) + ",top=" + getCenteredTop(600);
	window.open( "userAccountNew.jsp?WM=<%=CtrlConstants.WM_CHILD%>", "NewTransPlan", strStatus, false );
}
function openUpdate( id ) {
	alert( "建设中......" );
	return;
	var strStatus = "toolbar=no,Scrollbars=yes,status=yes,width=800,height=600,left=" + getCenteredLeft(800) + ",top=" + getCenteredTop(600);
	window.open( "userAccountUpdate.jsp?WM=<%=CtrlConstants.WM_CHILD%>&ID="+id, "UpdateTransPlan", strStatus, false );
}
</script>

</body>
</html>