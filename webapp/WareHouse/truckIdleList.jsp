<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import="com.undao.control.CtrlConstants" %>
<%@ page import="com.undao.enumeration.SysAstricts" %>
<%@ page import="xms.XmsInitial" %>
<%@ page import="com.undao.database.CommonSet" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.undao.utils.DecimalUtils" %>
<%@ page import="xms.XmsUtils" %>
<%@ page import="com.undao.database.AbstractQuery" %>
<%@ page import="com.undao.enumeration.EnumConstants" %>
<%@ page import="com.undao.control.AbstractDaemon" %>
<%@ page import="com.undao.utils.DateUtils" %>
<%@ page import="xms.queries.wareHouse.TruckIdleList" %>
<%@ page import="xms.beans.wareHouse.TruckIdle" %>
<%
	int PAGE_TAG = CtrlConstants.PG_TRUCK_IDLE_LIST;
	boolean acceptInnerUser = true;
	String[] needAstricts = { SysAstricts.QX_WH_READ  };
%>
<%@ include file="../include/inc_header.logic" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>车辆闲置一览</title>
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
TruckIdleList idleList = new TruckIdleList( XmsInitial.getDataSource() );
idleList.setCloudID(AbstractDaemon.getCloudID(request));

String cur_page = request.getParameter( TruckIdleList.QP_CUR_PAGE );
if ( cur_page != null ) {
	idleList.setPageSize( Integer.parseInt(request.getParameter(TruckIdleList.QP_PAGE_SIZE)) );
	idleList.setCurrentPage( Integer.parseInt( cur_page ) );

	String[] arr_params = idleList.getParamSerial();
	for ( int j=0; j<arr_params.length; j++ ) {
		idleList.setParameterValue( arr_params[j], request.getParameter( arr_params[j]) );
	}
} else {
	idleList.setDefaultParameterValue( );
}
idleList.executeQuery();
CommonSet dataSet = idleList.getQueryResult( );
%>

<div id="container">
<%@ include file="../include/menu_2_WareHouse.block" %>

<form name="queryForm" method="post" action="truckIdleList.jsp">
<table width="100%" border="0" cellspacing="1" cellpadding="0">
	<tr class="query_tr">
	<td align="right">
		<select name="<%=TruckIdleList.QP_SORT_TAG%>" class="select">
		<option value="<%=TruckIdleList.IDLE_DATE_ASC%>">--排序规则--</option>
		<option value="<%=TruckIdleList.COMPANY_ASC%>">公司升序</option>
			<option value="<%=TruckIdleList.USER_ASC%>">用户升序</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;

		<select name="<%=TruckIdleList.QP_COMPANY%>" class="select"><option value="All">--所属仓库--</option><%=EnumConstants.TRANS_PLAN_K_OPTIONS%></select>&nbsp;&nbsp;&nbsp;&nbsp;
		<select name="<%=TruckIdleList.QP_IDLE_K%>" class="select">
		<option value="All">--闲置类型--</option>
		<option value="Idle">闲置</option>
		<option value="Lack">空缺</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		车牌号:
		<input type="text" name="<%=TruckIdleList.QP_PLATE%>" size="10" maxlength="10" class="input_text" />&nbsp;&nbsp;&nbsp;&nbsp;
		起止日期:
		<input id="thDate1" type="text" name="<%=TruckIdleList.QP_DATE1%>" size="10" maxlength="10" class="input_text" ondblclick="javascript:setCurrentDate(this)" />—<input id="thDate2" type="text" name="<%=TruckIdleList.QP_DATE2%>" size="10" maxlength="10" class="input_text" ondblclick="javascript:setCurrentDate(this)" />&nbsp;&nbsp;&nbsp;&nbsp;
		登记人:
		<input type="text" name="<%=TruckIdleList.QP_USER%>" size="10" maxlength="10" class="input_text" />&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="submit" name="btnQuery" value="开始检索" class="input_text" />&nbsp;<input type="button" name="btnNew" value="新增闲置计划" class="input_text" onclick="openNew()"  />
	</td>
	</tr>
</table>

<table width="100%" border="0" cellpadding="0" cellspacing="1">
	<thead>
	<tr>
	<th width="50">序号</th>
	<th width="150">所属仓库</th>
	<th width="90">闲置类型</th>
	<th width="120">车牌号</th>
	<th width="180">司机</th>
	<th width="90">起始日期</th>
	<th width="90">截止日期</th>
	<th >备注</th>
	<th width="90">登记人</th>
	<th width="135">登记日期</th>
	<th width="120">操作</th>
	</tr>
	</thead>
	
	<tbody>
	<%
	int baseIndex = idleList.getBaseIndex();
	for ( int j = 0; j < dataSet.getRowCount(); j ++ ) {
		Long trans_p = (Long)dataSet.getValue(j,"truck_i");
		%>
		<tr class="<%=j%2==1?"content1_tr":"content2_tr" %>"> 
		<td align="center"><%=baseIndex+j %></td>
		<td align="center"><a href="javascript:void(0)"><%=dataSet.getValue(j,"company_zh")%></a></td>
		<td align="center"><%=EnumConstants.getDisplay((String)dataSet.getValue(j,"idle_k"))%></td>
		<td align="center"><%=dataSet.getValue(j,"plate_number")%></td>
		<td align="center"><%=dataSet.getValue(j,"tel_driver")%></td>
		<td align="center"><%=dataSet.getValue(j,"start_date")%></td>
		<td align="center"><%=dataSet.getValue(j,"end_date")%></td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"remark")%></td>
		<td align="center"><%=dataSet.getValue(j,"user_a_zh")%></td>
		<td align="center"><%=DateUtils.formatDateTime2(dataSet.getValue(j,"input_date"))%></td>
		<td align="center"><a href="javascript:openDelete('<%=((Long)dataSet.getValue(j,"truck_i")).toString()%>')">删除</a></td>
		</tr>
		<%
	}
	%>
	<tr class="total_tr">
	<td></td>
	<td></td>
	<td></td>
	<td align="right">本页合计：</td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	</tr> 
	</tbody> 
</table> 

<table width="100%" border="0" cellpadding="0" cellspacing="1">
	<tfoot><tr><td align="right"><%=XmsUtils.buildPageSeeker2("queryForm",idleList.getCurrentPage(),idleList.numOfPages()) %></td></tr></tfoot>
</table>
</form> 

</div><!-- container -->
<%@ include file="../include/menu_3_Footer.block" %>
<script type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" src="../js/jquery-ui.min.js"></script>

<script type="text/javascript" src="../include/stc_validate.js"></script>
<script type="text/javascript" src="../include/stc_function.js"></script>
<script type="text/javascript">
	queryForm.<%=TruckIdleList.QP_SORT_TAG%>.value = "<%=idleList.getParameterValue(TruckIdleList.QP_SORT_TAG)%>";
	queryForm.<%=TruckIdleList.QP_IDLE_K%>.value = "<%=idleList.getParameterValue(TruckIdleList.QP_IDLE_K)%>";
	queryForm.<%=TruckIdleList.QP_COMPANY%>.value = "<%=idleList.getParameterValue(TruckIdleList.QP_COMPANY)%>";
	queryForm.<%=TruckIdleList.QP_DATE1%>.value = "<%=idleList.getParameterValue(TruckIdleList.QP_DATE1)%>";
	queryForm.<%=TruckIdleList.QP_DATE2%>.value = "<%=idleList.getParameterValue(TruckIdleList.QP_DATE2)%>";
	queryForm.<%=TruckIdleList.QP_PLATE%>.value = "<%=idleList.getParameterValue(TruckIdleList.QP_PLATE)%>";
	queryForm.<%=TruckIdleList.QP_USER%>.value = "<%=idleList.getParameterValue(TruckIdleList.QP_USER)%>";
	queryForm.<%=AbstractQuery.QP_PAGE_SIZE%>.value = "<%=idleList.getPageSize()%>";
</script>

<script type="text/javascript">
$(document).ready( function () {
	$("#thDate1,#thDate2").datepicker({dateFormat: 'yy-mm-dd'});
});
function openNew( ) {
	var strStatus = "toolbar=no,Scrollbars=yes,status=yes,width=640,height=480,left=" + getCenteredLeft(640) + ",top=" + getCenteredTop(480);
	window.open( "truckIdleNew.jsp?WM=<%=CtrlConstants.WM_CHILD%>", "NewTruckIdle", strStatus, false );
}
function openDelete( id ) {
	if ( confirm( "确定删除吗？" ) ) {
		var strStatus = "toolbar=no,Scrollbars=yes,status=yes,width=640,height=480,left=" + getCenteredLeft(640) + ",top=" + getCenteredTop(480);
		window.open( "processBusiness.jsp?WM=<%=CtrlConstants.WM_CHILD%>&Action=TruckIdleDelete&ID="+id, "DeleteTruckIdle", strStatus, false );
	}
}
</script>

</body>
</html>
