<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import = "java.math.*" %>
<%@ page import = "xms.*" %>
<%@ page import="com.undao.control.CtrlConstants" %>
<%@ page import="com.undao.enumeration.SysAstricts" %>
<%@ page import="xms.queries.wareHouse.WaitInOutList" %>
<%@ page import="com.undao.control.AbstractDaemon" %>
<%@ page import="com.undao.database.CommonSet" %>
<%@ page import="com.undao.enumeration.EnumConstants" %>
<%@ page import="com.undao.database.AbstractQuery" %>
<%@ page import="com.undao.utils.DecimalUtils" %>
<%@ page import="com.undao.utils.DateUtils" %>
<%@ page import="xms.procedures.WareReturnDispatch" %>
<%
	int PAGE_TAG = CtrlConstants.PG_WAIT_INOUT_LIST;
	boolean acceptInnerUser = true;
	String[] needAstricts = { SysAstricts.QX_WH_READ  };
%>
<%@ include file="../include/inc_header.logic" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>等待出入库一览</title>
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
WaitInOutList waitInOutList = new WaitInOutList( XmsInitial.getDataSource() );
waitInOutList.setCloudID( AbstractDaemon.getCloudID(request) );

String cur_page = request.getParameter( WaitInOutList.QP_CUR_PAGE );
if ( cur_page != null ) {
	waitInOutList.setCurrentPage( Integer.parseInt( cur_page ) );
	waitInOutList.setPageSize( Integer.parseInt(request.getParameter( WaitInOutList.QP_PAGE_SIZE )) );
	String[] arr_params = waitInOutList.getParamSerial();
	for ( int j=0; j<arr_params.length; j++ ) {
		waitInOutList.setParameterValue( arr_params[j], request.getParameter( arr_params[j]) );
	}
} else {
	waitInOutList.setDefaultParameterValue( );
}
waitInOutList.executeQuery( );
CommonSet dataSet = waitInOutList.getQueryResult( );
%>

<div id="container">
<%@ include file="../include/menu_2_WareHouse.block" %>

<form name="queryForm" method="post" action="waitInOutList.jsp">
<div>
	<div class="query">
	<table width="100%" border="0" cellspacing="1" cellpadding="0">
		<tr class="query_tr">
		<td align="right">
			<select name="<%=WaitInOutList.QP_SORT_TAG%>">
				<option value="<%=WaitInOutList.PLAN_K_ASC%>">--排序规则--</option>
				<option value="<%=WaitInOutList.DEPART_DATE_ASC%>">发车日期升序</option>
				<option value="<%=WaitInOutList.PLATE_NUMBER_ASC%>">车牌号升序</option>
			</select>&nbsp;&nbsp;&nbsp;&nbsp;

			<select name="<%=WaitInOutList.QP_PLAN_K%>" class="select"><option value="All">--计划类型--</option><%=EnumConstants.TRANS_PLAN_K_OPTIONS%></select>&nbsp;&nbsp;&nbsp;&nbsp;
			客户简称:
			<input type="text" name="<%=WaitInOutList.QP_OBJECT_P%>" size="10" maxlength="10" class="input_text" />&nbsp;&nbsp;&nbsp;&nbsp;
			车牌号:
			<input type="text" name="<%=WaitInOutList.QP_PLATE%>" size="10" maxlength="10" class="input_text" />&nbsp;&nbsp;&nbsp;&nbsp;
			司机:
			<input type="text" name="<%=WaitInOutList.QP_DRIVER%>" size="10" maxlength="10" class="input_text" />&nbsp;&nbsp;&nbsp;&nbsp;
			发车起止日期:
			<input id="thDate1" type="text" name="<%=WaitInOutList.QP_DATE1%>" size="10" maxlength="10" class="input_text" ondblclick="javascript:setCurrentDate(this)" />—<input id="thDate2" type="text" name="<%=WaitInOutList.QP_DATE2%>" size="10" maxlength="10" class="input_text" ondblclick="javascript:setCurrentDate(this)" />&nbsp;&nbsp;&nbsp;&nbsp;
			装卸地名称:
			<input type="text" name="<%=WaitInOutList.QP_LOAD_NAME%>" size="10" maxlength="10" class="input_text" />&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" name="btnQuery" value="开始检索" onclick="javascript:checkSubmit('Query');" />&nbsp;<input type="button" name="btnExport" value="导出报表" onclick="javascript:checkSubmit('Export');" />
		</tr>
	</table>
	
	<table width="100%" border="0" cellpadding="0" cellspacing="1">
		<thead>
		<tr>
		<th width="50">序号</th>
		<th width="90">发车日期</th>
		<th width="150">车牌&司机</th>
		<th width="150">发货方-时间窗口</th>
		<th width="180">发货说明</th>
		<th width="150">收货方-收货窗口</th>
		<th width="180">收货说明</th>
		<th width="150">返空说明</th>
		<th width="100">货量(吨&方)</th>
		<th width="80">占车米数</th>
		<th width="80">时效要求</th>
		<th width="80">运输类型</th>
		<th width="80">调度员</th>
		<th >仓库回报</th>
		</tr>
		</thead>
		
		<tbody>
		<%
		int baseIndex = waitInOutList.getBaseIndex();
		int pos_index = -1;
		long preDisptID = 0;
		BigDecimal pageWeight = new BigDecimal(0);
		BigDecimal pageVolume = new BigDecimal(0);
		BigDecimal pageMeter = new BigDecimal(0);
		for ( int j = 0; j < dataSet.getRowCount(); j ++ ) {
			Long disptID = (Long)dataSet.getValue(j,"dispt");
			Long truckID = (Long)dataSet.getValue(j,"truck");
			if ( preDisptID != disptID.longValue() ) {
				pos_index++;
				%>
				<tr class="<%=pos_index%2==1?"content1_tr":"content2_tr" %>">
				<td align="center"><%=baseIndex+pos_index %></td>
				<td align="center"><%=dataSet.getValue(j,"depart_date")%></td>
				<td align="left">&nbsp;<%=truckID.intValue()>0?"[自有]":"[社会]"%><%=dataSet.getValue(j,"plate_number")%><br/>&nbsp;<%=dataSet.getValue(j,"tel_driver")%></td>
				<%
			} else {
				%>
				<tr class="<%=pos_index%2==1?"content1_tr":"content2_tr" %>">
				<td align="center"></td>
				<td align="center"></td>
				<td align="left"></td>
				<%
			}
			%>
			<td align="left">&nbsp;<%=dataSet.getValue(j,"ne_zh1")%><br/>&nbsp;[<%=dataSet.getValue(j,"window_1")%>]</td>
			<td align="left">&nbsp;<%=dataSet.getValue(j,"remark_1")%></td>
			<td align="left">&nbsp;<%=dataSet.getValue(j,"ne_zh2")%><br/>&nbsp;[<%=dataSet.getValue(j,"window_2")%>]</td>
			<td align="left">&nbsp;<%=dataSet.getValue(j,"remark_2")%></td>
			<td align="left">&nbsp;<%=dataSet.getValue(j,"ne_recycle")%>&nbsp;<br/>&nbsp;<%=DecimalUtils.formatQty(dataSet.getValue(j,"qty_meter_r"),false,"米")%></td>
			<td align="right"><%=DecimalUtils.formatQty(dataSet.getValue(j,"qty_w"),false,"吨")%>&nbsp;<%=DecimalUtils.formatQty(dataSet.getValue(j,"qty_v"),false,"方")%>&nbsp;</td>
			<td align="right"><%=DecimalUtils.formatQty(dataSet.getValue(j,"qty_meter"),false,"米")%>&nbsp;</td>
			<td align="center"><%=dataSet.getValue(j,"time_level")%></td>
			<td align="center"><%=dataSet.getValue(j,"plan_k")%></td>

			<%
			if ( preDisptID != disptID.longValue() ) {
				preDisptID = disptID.longValue();
				%><td align="center"><%=dataSet.getValue(j,"user_a")%></td><%
			} else {
				%><td align="center"></td><%
			}
			%>
			<td align="left">&nbsp;[<a href="javascript:void(0)" onclick="javascript:openReturnDispatch('<%=disptID.longValue()%>','<%=((Long)dataSet.getValue(j,"trans_p")).longValue()%>')">回报</a>]
				<%if (dataSet.getValue(j,"input_date_w") != null ) {
					%>&nbsp;<%=dataSet.getValue(j,"wh_remark")%><br/>&nbsp;[<%=dataSet.getValue(j,"user_zh_w")%>&nbsp;&nbsp;<%=DateUtils.formatDateTime3(dataSet.getValue(j,"input_date_w"))%>]<%
				}%>
			</td>
			</tr>
			<%
			pageWeight = pageWeight.add( (BigDecimal)dataSet.getValue(j,"qty_w") );
			pageVolume = pageVolume.add( (BigDecimal)dataSet.getValue(j,"qty_v") );
			pageMeter = pageMeter.add( (BigDecimal)dataSet.getValue(j,"qty_meter") );
		}%>
		<tr class="total_tr">
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td align="right">本页合计：</td>
		<td align="right"><%=DecimalUtils.formatQty(pageWeight,true,"吨")%>&nbsp;<%=DecimalUtils.formatQty(pageVolume,true,"方")%>&nbsp;</td>
		<td align="right"><%=DecimalUtils.formatQty(pageMeter,true,"米")%>&nbsp;</td>
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
		<tfoot><tr><td align="right">
		<input type="hidden" name="hAction" value="Query" />
		<%=XmsUtils.buildPageSeeker2("queryForm",waitInOutList.getCurrentPage(),waitInOutList.numOfPages()) %></td></tr></tfoot>
	</table>
	</div>
</div>
</form> 

</div><!-- container -->
<%@ include file="../include/menu_3_Footer.block" %>
<script type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" src="../js/jquery-ui.min.js"></script>

<script type="text/javascript" src="../include/stc_validate.js"></script>
<script type="text/javascript" src="../include/stc_function.js"></script>
<script type="text/javascript">
	queryForm.<%=WaitInOutList.QP_SORT_TAG%>.value = "<%=waitInOutList.getParameterValue(WaitInOutList.QP_SORT_TAG)%>";
	queryForm.<%=WaitInOutList.QP_PLAN_K%>.value = "<%=waitInOutList.getParameterValue(WaitInOutList.QP_PLAN_K)%>";
	queryForm.<%=WaitInOutList.QP_OBJECT_P%>.value = "<%=waitInOutList.getParameterValue(WaitInOutList.QP_OBJECT_P)%>";
	queryForm.<%=WaitInOutList.QP_PLATE%>.value = "<%=waitInOutList.getParameterValue(WaitInOutList.QP_PLATE)%>";
	queryForm.<%=WaitInOutList.QP_DRIVER%>.value = "<%=waitInOutList.getParameterValue(WaitInOutList.QP_DRIVER)%>";
	queryForm.<%=WaitInOutList.QP_DATE1%>.value = "<%=waitInOutList.getParameterValue(WaitInOutList.QP_DATE1)%>";
	queryForm.<%=WaitInOutList.QP_DATE2%>.value = "<%=waitInOutList.getParameterValue(WaitInOutList.QP_DATE2)%>";
	queryForm.<%=WaitInOutList.QP_LOAD_NAME%>.value = "<%=waitInOutList.getParameterValue(WaitInOutList.QP_LOAD_NAME)%>";
	queryForm.<%=AbstractQuery.QP_PAGE_SIZE%>.value = "<%=waitInOutList.getPageSize()%>";
</script>
<script type="text/javascript">
$(document).ready( function () {
	$("#thDate1,#thDate2").datepicker({dateFormat: 'yy-mm-dd'});
});
function openReturnDispatch( disptID, transP ) {
	var strStatus = "toolbar=no,Scrollbars=yes,status=yes,width=640,height=480,left=" + getCenteredLeft(640) + ",top=" + getCenteredTop(480);
	window.open( "returnDispatch.jsp?WM=<%=CtrlConstants.WM_CHILD%>&DisptID="+disptID+"&TransP="+transP, "_blank", strStatus );
}

function checkSubmit( subAction ) {
	if ( subAction == "Query" ) {
		queryForm.action = "waitInOutList.jsp";
		queryForm.target = "_self";
	} else if ( subAction == "Export" ) {
		alert( "正在建设中......" );
	}
	queryForm.submit();
}
</script>

</body>
</html>
