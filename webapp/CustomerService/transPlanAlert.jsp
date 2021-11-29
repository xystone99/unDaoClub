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
<%@ page import="xms.beans.shortDispatch.Dispatch" %>
<%@ page import="xms.queries.customerService.TransPlanAlert" %>
<%@ page import="com.undao.utils.DateUtils" %>
<%
	int PAGE_TAG = CtrlConstants.PG_TRANS_PLAN_ALERT;
	boolean acceptInnerUser = true;
	String[] needAstricts = { SysAstricts.QX_CUS_READ  };
%>
<%@ include file="../include/inc_header.logic" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>运输计划预警</title>
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
TransPlanAlert transPlanAlert = new TransPlanAlert( XmsInitial.getDataSource() );
transPlanAlert.setCloudID(AbstractDaemon.getCloudID(request));

String cur_page = request.getParameter( TransPlanAlert.QP_CUR_PAGE );
if ( cur_page != null ) {
	transPlanAlert.setCurrentPage( Integer.parseInt( cur_page ) );
	transPlanAlert.setPageSize( Integer.parseInt(request.getParameter( TransPlanAlert.QP_PAGE_SIZE )) );

	String[] arr_params = transPlanAlert.getParamSerial();
	for ( int j=0; j<arr_params.length; j++ ) {
		transPlanAlert.setParameterValue( arr_params[j], request.getParameter( arr_params[j]) );
	}
} else {
	transPlanAlert.setDefaultParameterValue( );
}

transPlanAlert.executeQuery();
CommonSet dataSet = transPlanAlert.getQueryResult( );
%>

<div id="container">
<%@ include file="../include/menu_2_CustomerService.block" %>

<form name="queryForm" method="post" action="transPlanAlert.jsp">
<table width="100%" border="0" cellspacing="1" cellpadding="0">
	<tr class="query_tr">
	<td align="right">
		<input type="checkbox" id="cbIf" name="cbIfHide" onclick="javascript:checkSelectType(this,queryForm.<%=TransPlanAlert.QP_IF_HIDE%>)" /><label for="cbIf">隐藏已安排计划</label>&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="hidden" name="<%=TransPlanAlert.QP_IF_HIDE%>" value="Y" />

		<select name="<%=TransPlanAlert.QP_SORT_TAG%>">
		<option value="<%=TransPlanAlert.INPUT_DATE_ASC%>">创建日期升序</option>
		<option value="<%=TransPlanAlert.INPUT_DATE_DESC%>">创建日期降序</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<select name="<%=TransPlanAlert.QP_PLAN_K%>" class="select"><option value="All">--计划类型--</option><%=EnumConstants.TRANS_PLAN_K_OPTIONS%></select>&nbsp;&nbsp;&nbsp;&nbsp;

		客户简称:
		<input type="text" name="<%=TransPlanAlert.QP_OBJECT_P%>" size="10" maxlength="10" class="input_text" />&nbsp;&nbsp;&nbsp;&nbsp;

		发货日期:
		<input id="pDate1" type="text" name="<%=TransPlanAlert.QP_DATE1%>" size="10" maxlength="10" class="input_text" ondblclick="javascript:setCurrentDate(this)" />—<input id="pDate2" type="text" name="<%=TransPlanAlert.QP_DATE2%>" size="10" maxlength="10" class="input_text" ondblclick="javascript:setCurrentDate(this)" />&nbsp;&nbsp;&nbsp;&nbsp;
		装卸地名称:
		<input type="text" name="<%=TransPlanAlert.QP_LOAD_NAME%>" size="10" maxlength="10" class="input_text" />&nbsp;&nbsp;&nbsp;&nbsp;
		登记人:
		<input type="text" name="<%=TransPlanAlert.QP_USER_ZH%>" size="10" maxlength="10" class="input_text" />&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="submit" name="btnQuery" value="开始检索" class="input_text" />
	</td>
	</tr>
</table>

<table width="100%" border="0" cellpadding="0" cellspacing="1">
	<thead>
	<tr>
	<th width="50">序号</th>
	<th width="120">日期-计划员</th>
	<th width="110">客户简称</th>
	<th width="120">运输类型&时效<br/>要求</th>
	<th width="135">发货方-发货窗口</th>
	<th width="180">发货说明</th>
	<th width="135">收货方-收货窗口</th>
	<th width="180">收货说明</th>
	<th width="180">返空箱说明</th>
	<th width="100">吨位&方数</th>
	<th width="90">占车米数</th>
	<th width="220">调度回报</th>
	<th >仓库回报</th>
	</tr>
	</thead>
	
	<tbody>
	<%
	int baseIndex = transPlanAlert.getBaseIndex();
	BigDecimal pageWeight = new BigDecimal(0);
	BigDecimal pageVolume = new BigDecimal(0);
	BigDecimal pageMeter = new BigDecimal(0);
	for ( int j = 0; j < dataSet.getRowCount(); j ++ ) {
		Long trans_p = (Long)dataSet.getValue(j,"trans_p");
		%>
		<tr class="<%=j%2==1?"content1_tr":"content2_tr" %>">
		<td align="center"><%=baseIndex+j %></td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"plan_date")%><br/>&nbsp;[<%=dataSet.getValue(j,"user_zh")%>]</td>
			<td align="center"><a href="javascript:void(0)" onclick="javascript:openUpdate('<%=trans_p.toString()%>')"><%=dataSet.getValue(j,"obj_short")%></a></td>
		<td align="center"><%=dataSet.getValue(j,"plan_k")%>&nbsp;[<%=dataSet.getValue(j,"time_level")%>]</td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"ne_zh1")%><br/>&nbsp;[<%=dataSet.getValue(j,"window_1")%>]</td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"remark_1")%></td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"ne_zh2")%><br/>&nbsp;[<%=dataSet.getValue(j,"window_2")%>]</td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"remark_2")%></td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"ne_recycle")%><br/>&nbsp;<%=DecimalUtils.formatQty(dataSet.getValue(j,"qty_meter_r"),false,"米")%></td>
		<td align="right"><%=DecimalUtils.formatQty(dataSet.getValue(j,"qty_w"),false,"吨")%>&nbsp;<%=DecimalUtils.formatQty(dataSet.getValue(j,"qty_v"),false,"方")%>&nbsp;</td>
		<td align="right"><%=DecimalUtils.formatQty(dataSet.getValue(j,"qty_meter"),false,"米")%>&nbsp;</td>
		<td align="left"><%
			if ( ((String)dataSet.getValue(j,"dispatch_remark")).length() > 1 ) {
				%><%=dataSet.getValue(j,"dispatch_remark")%><br/>&nbsp;[<%=dataSet.getValue(j,"user_zh_d")%>&nbsp;&nbsp;<%=DateUtils.formatDateTime3(dataSet.getValue(j,"input_date_d"))%>]<%
			}
			%></td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"wh_remark")%><br/>&nbsp;[<%=dataSet.getValue(j,"user_zh_w")%>&nbsp;&nbsp;<%=DateUtils.formatDateTime3(dataSet.getValue(j,"input_date_w"))%>]</td>
		</tr>
		<%
		pageWeight = pageWeight.add( (BigDecimal)dataSet.getValue(j,"qty_w") );
		pageVolume = pageVolume.add( (BigDecimal)dataSet.getValue(j,"qty_v") );
		pageMeter = pageMeter.add( (BigDecimal)dataSet.getValue(j,"qty_meter") );
	}
	%>
	<tr class="total_tr">
	<td></td>
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
	</tr>
	</tbody> 
</table> 

<table width="100%" border="0" cellpadding="0" cellspacing="1">
	<tfoot><tr><td align="right"><%=XmsUtils.buildPageSeeker2("queryForm",transPlanAlert.getCurrentPage(),transPlanAlert.numOfPages()) %></td></tr></tfoot>
	<input type="hidden" name="<%=Dispatch.QP_TRANS_PLANS%>" />
</table>
</form> 

</div><!-- container -->
<%@ include file="../include/menu_3_Footer.block" %>
<script type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" src="../js/jquery-ui.min.js"></script>

<script type="text/javascript" src="../include/stc_validate.js"></script>
<script type="text/javascript" src="../include/stc_function.js"></script>
<script type="text/javascript">
	queryForm.<%=TransPlanAlert.QP_SORT_TAG%>.value = "<%=transPlanAlert.getParameterValue(TransPlanAlert.QP_SORT_TAG)%>";
	queryForm.<%=TransPlanAlert.QP_PLAN_K%>.value = "<%=transPlanAlert.getParameterValue(TransPlanAlert.QP_PLAN_K)%>";
	queryForm.<%=TransPlanAlert.QP_OBJECT_P%>.value = "<%=transPlanAlert.getParameterValue(TransPlanAlert.QP_OBJECT_P)%>";
	queryForm.<%=TransPlanAlert.QP_DATE1%>.value = "<%=transPlanAlert.getParameterValue(TransPlanAlert.QP_DATE1)%>";
	queryForm.<%=TransPlanAlert.QP_DATE2%>.value = "<%=transPlanAlert.getParameterValue(TransPlanAlert.QP_DATE2)%>";
	queryForm.<%=TransPlanAlert.QP_LOAD_NAME%>.value = "<%=transPlanAlert.getParameterValue(TransPlanAlert.QP_LOAD_NAME)%>";
	queryForm.<%=TransPlanAlert.QP_USER_ZH%>.value = "<%=transPlanAlert.getParameterValue(TransPlanAlert.QP_USER_ZH)%>";
	queryForm.<%=AbstractQuery.QP_PAGE_SIZE%>.value = "<%=transPlanAlert.getPageSize()%>";

	queryForm.<%=TransPlanAlert.QP_IF_HIDE%>.value = "<%=transPlanAlert.getParameterValue(TransPlanAlert.QP_IF_HIDE)%>";
	setCheckValue(queryForm.cbIfHide, queryForm.<%=TransPlanAlert.QP_IF_HIDE%>.value, "Y" );
</script>

<script type="text/javascript">
	$(document).ready( function () {
		$("#pDate1,#pDate2").datepicker({dateFormat: 'yy-mm-dd'});
	});
</script>

<script type="text/javascript">
function checkSelectType( objCheck, objType ) {
	objType.value = objCheck.checked ? "Y" : "N";
}

function checkCommit( actionTag ) {
	return true;
};
function openUpdate( id ) {
	var strStatus = "toolbar=no,Scrollbars=yes,status=yes,width=800,height=600,left=" + getCenteredLeft(800) + ",top=" + getCenteredTop(600);
	window.open( "transPlanUpdate.jsp?WM=<%=CtrlConstants.WM_CHILD%>&ID="+id, "UpdateTransPlan", strStatus, false );
}
</script>

</body>
</html>
