<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import="com.undao.control.CtrlConstants" %>
<%@ page import="com.undao.enumeration.SysAstricts" %>
<%@ page import="xms.XmsInitial" %>
<%@ page import="com.undao.database.CommonSet" %>
<%@ page import="xms.XmsUtils" %>
<%@ page import="com.undao.database.AbstractQuery" %>
<%@ page import="com.undao.enumeration.EnumConstants" %>
<%@ page import="com.undao.control.AbstractDaemon" %>
<%@ page import="com.undao.utils.DateUtils" %>
<%@ page import="xms.queries.shortDispatch.TruckIdleSummary" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="static com.undao.database.DatabaseConstants.SQL_HYPHEN" %>
<%
	int PAGE_TAG = CtrlConstants.PG_TRUCK_IDLE_SUMMARY;
	boolean acceptInnerUser = true;
	String[] needAstricts = { SysAstricts.QX_DISPATCH_READ  };
%>
<%@ include file="../include/inc_header.logic" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>车辆闲置情况概览</title>
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
TruckIdleSummary idleSummary = new TruckIdleSummary( XmsInitial.getDataSource() );
idleSummary.setCloudID(AbstractDaemon.getCloudID(request));

String cur_page = request.getParameter( TruckIdleSummary.QP_CUR_PAGE );
if ( cur_page != null ) {
	idleSummary.setPageSize( Integer.parseInt(request.getParameter(TruckIdleSummary.QP_PAGE_SIZE)) );
	idleSummary.setCurrentPage( Integer.parseInt( cur_page ) );

	String[] arr_params = idleSummary.getParamSerial();
	for ( int j=0; j<arr_params.length; j++ ) {
		idleSummary.setParameterValue( arr_params[j], request.getParameter( arr_params[j]) );
	}
} else {
	idleSummary.setDefaultParameterValue( );
}
idleSummary.executeQuery();
CommonSet dataSet = idleSummary.getQueryResult( );
LocalDate date1 = DateUtils.buildDate( idleSummary.getParameterValue( TruckIdleSummary.QP_DATE1 ) );
LocalDate date2 = DateUtils.buildDate( idleSummary.getParameterValue( TruckIdleSummary.QP_DATE2 ) );
%>

<div id="container">
<%@ include file="../include/menu_2_ShortDispatch.block" %>

<form name="queryForm" method="post" action="truckIdleSummary.jsp" onsubmit="javascript:return checkQuery(this);">
<table width="100%" border="0" cellspacing="1" cellpadding="0">
	<tr class="query_tr">
	<td align="right">
		<select name="<%=TruckIdleSummary.QP_SORT_TAG%>" class="select">
		<option value="<%=TruckIdleSummary.COMPANY_ASC%>">--排序规则--</option>
		<option value="<%=TruckIdleSummary.PLATE_ASC%>">车牌号升序</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;

		<select name="<%=TruckIdleSummary.QP_COMPANY%>" class="select"><option value="All">--所属仓库--</option><%=XmsInitial.getXmsContainer().getMasterCompany().getSelectOptions(AbstractDaemon.getCloudID(request))%></select>&nbsp;&nbsp;&nbsp;&nbsp;
		<select name="<%=TruckIdleSummary.QP_IDLE_K%>" class="select">
		<option value="All">--闲置类型--</option>
		<option value="Idle">闲置</option>
		<option value="Lack">空缺</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		车牌号:
		<input type="text" name="<%=TruckIdleSummary.QP_PLATE%>" size="10" maxlength="10" class="input_text" />&nbsp;&nbsp;&nbsp;&nbsp;
		起止日期:
		<input id="thDate1" type="text" name="<%=TruckIdleSummary.QP_DATE1%>" size="10" maxlength="10" class="input_text" ondblclick="javascript:setCurrentDate(this)" />—<input id="thDate2" type="text" name="<%=TruckIdleSummary.QP_DATE2%>" size="10" maxlength="10" class="input_text" ondblclick="javascript:setCurrentDate(this)" />&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="submit" name="btnQuery" value="开始检索" class="input_text" />
	</td>
	</tr>
</table>

<table width="100%" border="0" cellpadding="0" cellspacing="1">
	<thead>
	<tr>
	<th width="50">序号</th>
	<th width="120">所属仓库</th>
	<th width="100">车牌号/车型</th>
	<%
	LocalDate tempDate = date1.plusDays( 0 );
	for ( int h=1; tempDate.isBefore(date2) || tempDate.isEqual(date2); h++ ) {
		%><th width="180"><%=DateUtils.formatLocalDate(tempDate)%>(<%=DateUtils.formatWeekTag(tempDate)%>)</th><%
		tempDate = date1.plusDays( h );
	}
	%>
	<th></th>
	</tr>
	</thead>
	
	<tbody>
	<%
	StringBuilder bufKey = new StringBuilder();
	int baseIndex = idleSummary.getBaseIndex();
	for ( int j = 0; j < dataSet.getRowCount(); j ++ ) {
		int compID = ((Long)dataSet.getValue(j,"cur_company")).intValue();
		int truckID = ((Long)dataSet.getValue(j,"truck")).intValue();
		%>
		<tr class="<%=j%2==1?"content1_tr":"content2_tr" %>"> 
		<td align="center"><%=baseIndex+j %></td>
		<td align="center"><a href="javascript:void(0)"><%=dataSet.getValue(j,"company_zh")%></a></td>
		<td align="center"><%=dataSet.getValue(j,"plate_number")%></td>
		<%
		tempDate = date1.plusDays( 0 );
		for ( int h=1; tempDate.isBefore(date2) || tempDate.isEqual(date2); h++ ) {
			bufKey.delete(0, bufKey.length() );
			bufKey.append( compID ).append( SQL_HYPHEN ).append( truckID ).append( SQL_HYPHEN ).append( DateUtils.formatLocal_mmdd(tempDate) );
			String remark = idleSummary.getIdleRemark(bufKey.toString());
			if ( remark.length() > 0 ) {
				%><td align="left"><span style="color:<%=truckID>0?"blue":"red"%>"><%=remark%></span></td><%
			} else {
				%><td></td><%
			}

			tempDate = date1.plusDays( h );
		}
		%>
		<td></td>
		</tr>
		<%
	}
	%>
	<tr class="total_tr">
	<td></td>
    <td></td>
	<td align="right">本页合计：</td>
	<%
	tempDate = date1.plusDays( 0 );
	for ( int h=1; tempDate.isBefore(date2) || tempDate.isEqual(date2); h++ ) {
		%><td></td><%
		tempDate = date1.plusDays( h );
	}
	%>
    <td></td>
	</tr> 
	</tbody> 
</table> 

<table width="100%" border="0" cellpadding="0" cellspacing="1">
	<tfoot><tr><td align="right"><%=XmsUtils.buildPageSeeker2("queryForm",idleSummary.getCurrentPage(),idleSummary.numOfPages()) %></td></tr></tfoot>
</table>
</form> 

</div><!-- container -->
<%@ include file="../include/menu_3_Footer.block" %>
<script type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" src="../js/jquery-ui.min.js"></script>

<script type="text/javascript" src="../include/stc_validate.js"></script>
<script type="text/javascript" src="../include/stc_function.js"></script>
<script type="text/javascript">
	queryForm.<%=TruckIdleSummary.QP_SORT_TAG%>.value = "<%=idleSummary.getParameterValue(TruckIdleSummary.QP_SORT_TAG)%>";
	queryForm.<%=TruckIdleSummary.QP_IDLE_K%>.value = "<%=idleSummary.getParameterValue(TruckIdleSummary.QP_IDLE_K)%>";
	queryForm.<%=TruckIdleSummary.QP_COMPANY%>.value = "<%=idleSummary.getParameterValue(TruckIdleSummary.QP_COMPANY)%>";
	queryForm.<%=TruckIdleSummary.QP_DATE1%>.value = "<%=idleSummary.getParameterValue(TruckIdleSummary.QP_DATE1)%>";
	queryForm.<%=TruckIdleSummary.QP_DATE2%>.value = "<%=idleSummary.getParameterValue(TruckIdleSummary.QP_DATE2)%>";
	queryForm.<%=TruckIdleSummary.QP_PLATE%>.value = "<%=idleSummary.getParameterValue(TruckIdleSummary.QP_PLATE)%>";
	queryForm.<%=AbstractQuery.QP_PAGE_SIZE%>.value = "<%=idleSummary.getPageSize()%>";
</script>

<script type="text/javascript">
$(document).ready( function () {
	$("#thDate1,#thDate2").datepicker({dateFormat: 'yy-mm-dd'});
});
function checkQuery( inForm ) {
    var j = getDateSub( inForm.<%=TruckIdleSummary.QP_DATE1%>.value, inForm.<%=TruckIdleSummary.QP_DATE2%>.value );
    if (j >= 10 ) {
        alert( "起止日期最多间隔10天！" );
        return false;
    }
    return true;
}
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
