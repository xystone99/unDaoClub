<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import="com.undao.control.CtrlConstants" %>
<%@ page import="com.undao.enumeration.SysAstricts" %>
<%@ page import="xms.XmsInitial" %>
<%@ page import="com.undao.database.CommonSet" %>
<%@ page import="com.undao.utils.DecimalUtils" %>
<%@ page import="xms.XmsUtils" %>
<%@ page import="com.undao.database.AbstractQuery" %>
<%@ page import="com.undao.control.AbstractDaemon" %>
<%@ page import="xms.queries.shortDispatch.DispatchHistory" %>
<%
	int PAGE_TAG = CtrlConstants.PG_DISPATCH_HISTORY;
	boolean acceptInnerUser = true;
	String[] needAstricts = { SysAstricts.QX_DISPATCH_READ  };
%>
<%@ include file="../include/inc_header.logic" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>车次一览</title>
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
DispatchHistory dispatchHistory = new DispatchHistory( XmsInitial.getDataSource() );
dispatchHistory.setCloudID(AbstractDaemon.getCloudID(request));

String page_size = request.getParameter( DispatchHistory.QP_PAGE_SIZE );
String cur_page = request.getParameter( DispatchHistory.QP_CUR_PAGE );
if ( page_size != null ) {
	dispatchHistory.setPageSize( Integer.parseInt(page_size) );
}
if ( cur_page != null ) {
	dispatchHistory.setCurrentPage( Integer.parseInt( cur_page ) );
}		
String[] arr_params = dispatchHistory.getParamSerial();
for ( int j=0; j<arr_params.length; j++ ) {
	dispatchHistory.setParameterValue( arr_params[j], request.getParameter( arr_params[j]) );
}
dispatchHistory.executeQuery();
CommonSet dataSet = dispatchHistory.getQueryResult( );
%>

<div id="container">
<%@ include file="../include/menu_2_ShortDispatch.block" %>

<form name="queryForm" method="post" action="dispatchHistory.jsp">
<table width="100%" border="0" cellspacing="1" cellpadding="0">
	<tr class="query_tr">
	<td align="right">
		<select name="<%=DispatchHistory.QP_SORT_TAG%>">
		<option value="<%=DispatchHistory.INPUT_DATE_ASC%>">--排序规则--</option>
		<option value="<%=DispatchHistory.INPUT_DATE_DESC%>">创建日期降序</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		车牌号:
		<input type="text" name="<%=DispatchHistory.QP_TRUCK%>" size="10" maxlength="10" class="input_text" />&nbsp;&nbsp;&nbsp;&nbsp;
		驾驶员:
		<input type="text" name="<%=DispatchHistory.QP_DRIVER%>" size="10" maxlength="10" class="input_text" />&nbsp;&nbsp;&nbsp;&nbsp;
		押运员:
		<input type="text" name="<%=DispatchHistory.QP_SUB_DRIVER%>" size="10" maxlength="10" class="input_text" />&nbsp;&nbsp;&nbsp;&nbsp;
		发车日期:
		<input id="pDate1" type="text" name="<%=DispatchHistory.QP_DATE1%>" size="10" maxlength="10" class="input_text" ondblclick="javascript:setCurrentDate(this)" />—<input id="pDate2" type="text" name="<%=DispatchHistory.QP_DATE2%>" size="10" maxlength="10" class="input_text" ondblclick="javascript:setCurrentDate(this)" />&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="submit" name="btnQuery" value="开始检索" class="input_text" />
	</td>
	</tr>
</table>

<table width="100%" border="0" cellpadding="0" cellspacing="1">
	<thead>
	<tr>
	<th width="50">序号</th>
	<th width="110">日期-调度</th>
	<th width="110">车牌号</th>
	<th width="160">驾驶员</th>
	<th width="90">押运员</th>
	<th >发货方->收货方;&nbsp;&nbsp;返空说明;&nbsp;&nbsp;吨/方;&nbsp;&nbsp;占车米数</th>
	<th width="220">其它说明</th>
	<th width="220">仓库回报</th>
	</tr>
	</thead>
	
	<tbody>
	<%
	int pos_index = 0;
	long preDisptID = dataSet.getRowCount()>0 ? ((Long)dataSet.getValue(0,"dispt")).longValue() : 0;
	int baseIndex = dispatchHistory.getBaseIndex();
	for ( int j = 0; j < dataSet.getRowCount(); ) {
		Long disptID = (Long)dataSet.getValue(j,"dispt");
		Long truckID = (Long)dataSet.getValue(j,"truck");
		%>
		<tr class="<%=pos_index%2==1?"content1_tr":"content2_tr" %>">
		<td align="center"><%=baseIndex+pos_index++ %></td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"depart_date")%><br/>&nbsp;[<a href="javascript:void(0)" onclick="javascript:openDelete('<%=disptID.toString()%>')">D</a>][<%=dataSet.getValue(j,"user_a")%>]</td>
		<td align="center">
			<%=truckID.intValue()>0?"[自有]":"[社会]"%><%=dataSet.getValue(j,"plate_number")%></td>
		<td align="center"><%=dataSet.getValue(j,"tel_driver")%></td>
		<td align="center"><%
			if ( ((Long)dataSet.getValue(j,"sub_driver")).longValue() > 0 ) {
				%><%=dataSet.getValue(j,"sub_driver_zh")%><%
			}%></td>
		<%
		StringBuilder buf = new StringBuilder();
		while ( (disptID.intValue() == preDisptID) && (j < dataSet.getRowCount()) ) {
			if ( buf.length() > 0 ) {
				buf.append( "<br/>&nbsp;" );
			}
			buf.append( dataSet.getValue(j,"ne_zh1") ).append( "-->" ).append( dataSet.getValue(j,"ne_zh2") ).append( ";&nbsp;" );
			buf.append( dataSet.getValue(j,"ne_recycle") ).append( "&nbsp;" ).append( DecimalUtils.formatQty(dataSet.getValue(j,"qty_meter_r"),false,"米") ).append( ";<br/>&nbsp;" );
			buf.append( DecimalUtils.formatQty(dataSet.getValue(j,"qty_w"),false,"吨") ).append( "&nbsp;" ).append( DecimalUtils.formatQty(dataSet.getValue(j,"qty_v"),false,"方") ).append( ";&nbsp;" );
			buf.append( DecimalUtils.formatQty(dataSet.getValue(j,"qty_meter"),false,"米") ).append( ";" );
			j++;
			if ( j < dataSet.getRowCount() ) {
				disptID = (Long)dataSet.getValue(j,"dispt");
			}

		}
		preDisptID = disptID.intValue();
		%>
		<td align="left"><%=buf.toString()%></td>
		<td align="left">&nbsp;<%=dataSet.getValue(j-1,"remark")%></td>
		<td align="left">&nbsp;<%=dataSet.getValue(j-1,"wh_remark")%></td>
		</tr>
		<%
	}
	%>
	<tr class="total_tr">
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td align="right">本页合计：</td>
	<td></td>
	<td></td>
	<td></td>
	</tr>
	</tbody> 
</table> 

<table width="100%" border="0" cellpadding="0" cellspacing="1">
	<tfoot><tr><td align="right"><%=XmsUtils.buildPageSeeker2("queryForm",dispatchHistory.getCurrentPage(),dispatchHistory.numOfPages()) %></td></tr></tfoot>
</table>
</form> 

</div><!-- container -->
<%@ include file="../include/menu_3_Footer.block" %>
<script type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" src="../js/jquery-ui.min.js"></script>

<script type="text/javascript" src="../include/stc_validate.js"></script>
<script type="text/javascript" src="../include/stc_function.js"></script>
<script type="text/javascript">
	queryForm.<%=DispatchHistory.QP_SORT_TAG%>.value = "<%=dispatchHistory.getParameterValue(DispatchHistory.QP_SORT_TAG)%>";
	queryForm.<%=DispatchHistory.QP_TRUCK%>.value = "<%=dispatchHistory.getParameterValue(DispatchHistory.QP_TRUCK)%>";
	queryForm.<%=DispatchHistory.QP_DRIVER%>.value = "<%=dispatchHistory.getParameterValue(DispatchHistory.QP_DRIVER)%>";
	queryForm.<%=DispatchHistory.QP_SUB_DRIVER%>.value = "<%=dispatchHistory.getParameterValue(DispatchHistory.QP_SUB_DRIVER)%>";
	queryForm.<%=DispatchHistory.QP_DATE1%>.value = "<%=dispatchHistory.getParameterValue(DispatchHistory.QP_DATE1)%>";
	queryForm.<%=DispatchHistory.QP_DATE2%>.value = "<%=dispatchHistory.getParameterValue(DispatchHistory.QP_DATE2)%>";
	queryForm.<%=AbstractQuery.QP_PAGE_SIZE%>.value = "<%=dispatchHistory.getPageSize()%>";
</script>

<script type="text/javascript">
	$(document).ready( function () {
		$("#pDate1,#pDate2").datepicker({dateFormat: 'yy-mm-dd'});
	});
</script>

<script type="text/javascript">
function checkCommit( actionTag ) {
	return true;
};
function openDelete( id ) {
	if (confirm( "确定删除吗？" ) ) {
		var strStatus = "toolbar=no,Scrollbars=yes,status=yes,width=800,height=600,left=" + getCenteredLeft(800) + ",top=" + getCenteredTop(600);
		window.open("processBusiness.jsp?WM=<%=CtrlConstants.WM_CHILD%>&Action=DispatchDelete&ID=" + id, "_blank", strStatus);
	}
}
</script>

</body>
</html>
