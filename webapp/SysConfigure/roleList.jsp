<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import = "xms.*" %>
<%@ page import="com.undao.control.CtrlConstants" %>
<%@ page import="com.undao.enumeration.SysAstricts" %>
<%@ page import="xms.queries.sysConfigure.RoleList" %>
<%@ page import="com.undao.control.AbstractDaemon" %>
<%@ page import="com.undao.database.CommonSet" %>
<%@ page import="com.undao.utils.DateUtils" %>
<%@ page import="com.undao.database.AbstractQuery" %>
<%
	int PAGE_TAG = CtrlConstants.PG_ROLE_LIST;
	boolean acceptInnerUser = true;
	String[] needAstricts = { SysAstricts.QX_ASTRICT_CONFIGURE };
%>
<%@ include file="../include/inc_header.logic" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>角色一览</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />    
<link rel="stylesheet" type="text/css" href="../css/default.css" />
</head>

<body>
<%@ include file="../include/menu_1_Header.block" %>

<%
RoleList roleList = new RoleList( XmsInitial.getDataSource() );
roleList.setIfSystemAdministrator( AbstractDaemon.isSystemAdministrator(request) );
roleList.setCloudID( AbstractDaemon.getCloudID(request) );

String cur_page = request.getParameter( RoleList.QP_CUR_PAGE );
if ( cur_page != null ) {
	roleList.setPageSize( Integer.parseInt(request.getParameter( RoleList.QP_PAGE_SIZE )) );
	roleList.setCurrentPage( Integer.parseInt( cur_page ) );

	String[] arr_params = roleList.getParamSerial();
	for ( int j=0; j<arr_params.length; j++ ) {
		roleList.setParameterValue( arr_params[j], request.getParameter( arr_params[j]) );
	}
} else {
	roleList.setDefaultParameterValue();
}
roleList.executeQuery();
CommonSet dataSet = roleList.getQueryResult( );
%>

<div id="container">
<%@ include file="../include/menu_2_SysConfigure.block" %>

<form name="queryForm" method="post" action="roleList.jsp" >
<table width="100%" border="0" cellspacing="1" cellpadding="0">
	<tr class="query_tr">
	<td align="right">
		<select name="<%=RoleList.QP_SORT_TAG%>">
		<option value="<%=RoleList.SORT_TAG_ASC%>">--排序规则--</option>
		<option value="<%=RoleList.INPUT_DATE_DESC%>">创建日期降序</option>
		</select>&nbsp;&nbsp;
		名称:&nbsp;
		<input type="text" name="<%=RoleList.QP_NAME_ZH%>" size="12" maxlength="20" />&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="submit" name="btnQuery" value="开始检索" />&nbsp;<input type="button" name="btnNew" value="新增角色" onclick="javascript:openNew()" />
	</td>
	</tr>
</table>

<table width="100%" border="0" cellpadding="0" cellspacing="1">
	<thead>
	<tr>
	<th width="50">序号</th>
	<th width="180">中文名称</th>
	<th width="80">排序标记</th>
	<th width="230">登录首页</th>
	<th>备注</th>
	<th width="150">登记日期</th>
	<th width="100">操作</th>
	</tr>
	</thead>
	
	<tbody>
	<%
	int baseIndex = roleList.getBaseIndex();
	for ( int j = 0; j < dataSet.getRowCount(); j ++ ) {
		%>
		<tr class="<%=j%2==1?"content1_tr":"content2_tr" %>"> 
		<td align="center"><%=baseIndex+j %></td>
		<td align="left">&nbsp;<a href="javascript:void(0)" onclick="javascript:openUpdate('<%=dataSet.getValue(j,"role")%>');"><%=dataSet.getValue(j,"ne_zh")%></a></td>
		<td align="center"><%=dataSet.getValue(j,"sort_tag")%></td>
		<td align="center"><%=(String)dataSet.getValue(j,"href_index")%></td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"remark")%></td>
		<td align="center"><%=DateUtils.formatDateTime(dataSet.getValue(j,"input_date"))%></td>
		<td align="center"><a href="javascript:void(0)" onclick="javascript:openDelete('<%=dataSet.getValue(j,"role")%>');">删除</a></td>
		</tr>
		<%
	}
	%>
	</tbody>
</table>

<table width="100%" border="0" cellpadding="0" cellspacing="1">
	<tfoot><tr><td align="right"><%=XmsUtils.buildPageSeeker2("queryForm",roleList.getCurrentPage(),roleList.numOfPages()) %></td></tr></tfoot>
</table>
</form> 
</div><!-- container -->
<%@ include file="../include/menu_3_Footer.block" %>

<script type="text/javascript" src="../include/stc_validate.js"></script>
<script type="text/javascript" src="../include/stc_function.js"></script>

<script type="text/javascript">
	queryForm.<%=AbstractQuery.QP_PAGE_SIZE%>.value = "<%=roleList.getPageSize()%>";
	queryForm.<%=RoleList.QP_SORT_TAG%>.value = "<%=roleList.getParameterValue(RoleList.QP_SORT_TAG)%>";
	queryForm.<%=RoleList.QP_NAME_ZH%>.value = "<%=roleList.getParameterValue(RoleList.QP_NAME_ZH)%>";
</script>

<script type="text/javascript">
function openNew( ) {
	var strStatus = "toolbar=no,Scrollbars=yes,status=yes,width=640,height=640,left=" + getCenteredLeft(640) + ",top=" + getCenteredTop(640);
	window.open( "roleNew.jsp?WM=<%=CtrlConstants.WM_CHILD%>", "NewRole", strStatus );
}
function openUpdate( id ) {
	var strStatus = "toolbar=no,Scrollbars=yes,status=yes,width=640,height=640,left=" + getCenteredLeft(640) + ",top=" + getCenteredTop(640);
	window.open( "roleUpdate.jsp?WM=<%=CtrlConstants.WM_CHILD%>&ID="+id, "UpdateRole", strStatus );
}
function openDelete( id ) {
	if ( confirm('确定删除吗？') ) {
		window.open( "processRootBuild.jsp?WM=<%=CtrlConstants.WM_CHILD%>&Action=RoleDelete&ID="+id, "DeleteRole", "toolbar=no,Scrollbars=yes,status=yes,width=640,height=480,left=0,top=0" );
	}
}
</script>
</body>
</html>