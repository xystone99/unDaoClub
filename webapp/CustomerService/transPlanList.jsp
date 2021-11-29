<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import="com.undao.control.CtrlConstants" %>
<%@ page import="com.undao.enumeration.SysAstricts" %>
<%@ page import="xms.XmsInitial" %>
<%@ page import="xms.queries.customerService.TransPlanList" %>
<%@ page import="com.undao.database.CommonSet" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.undao.utils.DecimalUtils" %>
<%@ page import="xms.XmsUtils" %>
<%@ page import="com.undao.database.AbstractQuery" %>
<%@ page import="com.undao.enumeration.EnumConstants" %>
<%@ page import="com.undao.control.AbstractDaemon" %>
<%@ page import="com.undao.utils.DateUtils" %>
<%
	int PAGE_TAG = CtrlConstants.PG_TRANS_PLAN_LIST;
	boolean acceptInnerUser = true;
	String[] needAstricts = { SysAstricts.QX_CUS_READ  };
%>
<%@ include file="../include/inc_header.logic" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>运输计划一览</title>
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
TransPlanList transPlanList = new TransPlanList( XmsInitial.getDataSource() );
transPlanList.setCloudID(AbstractDaemon.getCloudID(request));

String cur_page = request.getParameter( TransPlanList.QP_CUR_PAGE );
if ( cur_page != null ) {
	transPlanList.setCurrentPage( Integer.parseInt( cur_page ) );
	transPlanList.setPageSize( Integer.parseInt(request.getParameter( TransPlanList.QP_PAGE_SIZE )) );

	String[] arr_params = transPlanList.getParamSerial();
	for ( int j=0; j<arr_params.length; j++ ) {
		transPlanList.setParameterValue( arr_params[j], request.getParameter( arr_params[j]) );
	}
} else {
	transPlanList.setDefaultParameterValue( );
}
transPlanList.executeQuery();
CommonSet dataSet = transPlanList.getQueryResult( );
%>

<div id="container">
<%@ include file="../include/menu_2_CustomerService.block" %>

<form name="queryForm" method="post" action="transPlanList.jsp">
<table width="100%" border="0" cellspacing="1" cellpadding="0">
	<tr class="query_tr">
	<td align="right">
		<select name="<%=TransPlanList.QP_SORT_TAG%>">
		<option value="<%=TransPlanList.INPUT_DATE_ASC%>">创建日期升序</option>
		<option value="<%=TransPlanList.INPUT_DATE_DESC%>">创建日期降序</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;

		<select name="<%=TransPlanList.QP_PLAN_K%>" class="select"><option value="All">--计划类型--</option><%=EnumConstants.TRANS_PLAN_K_OPTIONS%></select>&nbsp;&nbsp;&nbsp;&nbsp;
		客户简称:
		<input type="text" name="<%=TransPlanList.QP_OBJECT_P%>" size="10" maxlength="10" class="input_text" />&nbsp;&nbsp;&nbsp;&nbsp;
		计划起止日期:
		<input id="thDate1" type="text" name="<%=TransPlanList.QP_DATE1%>" size="10" maxlength="10" class="input_text" ondblclick="javascript:setCurrentDate(this)" />—<input id="thDate2" type="text" name="<%=TransPlanList.QP_DATE2%>" size="10" maxlength="10" class="input_text" ondblclick="javascript:setCurrentDate(this)" />&nbsp;&nbsp;&nbsp;&nbsp;
		装卸地名称:
		<input type="text" name="<%=TransPlanList.QP_LOAD_NAME%>" size="10" maxlength="10" class="input_text" />&nbsp;&nbsp;&nbsp;&nbsp;
		登记人:
		<input type="text" name="<%=TransPlanList.QP_USER_ZH%>" size="10" maxlength="10" class="input_text" />&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="submit" name="btnQuery" value="开始检索" class="input_text" />&nbsp;<input type="button" name="btnNew" value="新增运输计划" class="input_text" onclick="openNew()"  />
	</td>
	</tr>
</table>

<table width="100%" border="0" cellpadding="0" cellspacing="1">
	<thead>
	<tr>
	<th width="50">序号</th>
	<th width="120">日期-计划员</th>
	<th width="100">客户简称</th>
	<th width="120">运输类型&时效<br/>要求</th>
	<th width="135">发货方-时间窗口</th>
	<th width="200">发货方地址/发货联系人</th>
	<th width="200">发货说明</th>
	<th width="135">收货方-时间窗口</th>
	<th width="200">收货方地址/收货联系人</th>
	<th width="200">收货说明</th>
	<th >返空说明</th>
	<th width="100">吨位&方数</th>
	<th width="90">占车米数</th>
	</tr>
	</thead>
	
	<tbody>
	<%
	int baseIndex = transPlanList.getBaseIndex();
	BigDecimal pageWeight = new BigDecimal(0);
	BigDecimal pageVolume = new BigDecimal(0);
	BigDecimal pageMeter = new BigDecimal(0);
	for ( int j = 0; j < dataSet.getRowCount(); j ++ ) {
		Long trans_p = (Long)dataSet.getValue(j,"trans_p");
		%>
		<tr class="<%=j%2==1?"content1_tr":"content2_tr" %>"> 
		<td align="center"><%=baseIndex+j %></td>
		<td align="left">&nbsp;<%=DateUtils.format_mmdd(dataSet.getValue(j,"plan_date"))%><br/>&nbsp;[<%=dataSet.getValue(j,"user_zh")%>]</td>
		<td align="center"><a href="javascript:void(0)" onclick="javascript:openUpdate('<%=trans_p.toString()%>')"><%=dataSet.getValue(j,"obj_short")%></a></td>
		<td align="center"><%=dataSet.getValue(j,"plan_k")%>&nbsp;[<%=dataSet.getValue(j,"time_level")%>]</td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"ne_zh1")%><br/>&nbsp;[<%=dataSet.getValue(j,"window_1")%>]</td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"address_1")%><br/>&nbsp;[<%=dataSet.getValue(j,"linkman_1")%>]</td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"remark_1")%></td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"ne_zh2")%><br/>&nbsp;[<%=dataSet.getValue(j,"window_2")%>]</td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"address_2")%><br/>&nbsp;[<%=dataSet.getValue(j,"linkman_2")%>]</td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"remark_2")%></td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"ne_recycle")%><br/>&nbsp;<%=DecimalUtils.formatQty(dataSet.getValue(j,"qty_meter_r"),false,"米")%></td>
		<td align="right"><%=DecimalUtils.formatQty(dataSet.getValue(j,"qty_w"),false,"吨")%>&nbsp;<%=DecimalUtils.formatQty(dataSet.getValue(j,"qty_v"),false,"方")%>&nbsp;</td>
		<td align="right"><%=DecimalUtils.formatQty(dataSet.getValue(j,"qty_meter"),false,"米")%>&nbsp;</td>
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
	<td></td>
	<td></td>
	<td align="right">本页合计：</td>
	<td align="right"><%=DecimalUtils.formatQty(pageWeight,true,"吨")%>&nbsp;<%=DecimalUtils.formatQty(pageVolume,true,"方")%>&nbsp;</td>
	<td align="right"><%=DecimalUtils.formatQty(pageMeter,"米")%>&nbsp;</td>
	</tr> 
	</tbody> 
</table> 

<table width="100%" border="0" cellpadding="0" cellspacing="1">
	<tfoot><tr><td align="right"><%=XmsUtils.buildPageSeeker2("queryForm",transPlanList.getCurrentPage(),transPlanList.numOfPages()) %></td></tr></tfoot>
</table>
</form> 

</div><!-- container -->
<%@ include file="../include/menu_3_Footer.block" %>
<script type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" src="../js/jquery-ui.min.js"></script>

<script type="text/javascript" src="../include/stc_validate.js"></script>
<script type="text/javascript" src="../include/stc_function.js"></script>
<script type="text/javascript">
	queryForm.<%=TransPlanList.QP_SORT_TAG%>.value = "<%=transPlanList.getParameterValue(TransPlanList.QP_SORT_TAG)%>";
	queryForm.<%=TransPlanList.QP_PLAN_K%>.value = "<%=transPlanList.getParameterValue(TransPlanList.QP_PLAN_K)%>";
	queryForm.<%=TransPlanList.QP_OBJECT_P%>.value = "<%=transPlanList.getParameterValue(TransPlanList.QP_OBJECT_P)%>";
	queryForm.<%=TransPlanList.QP_DATE1%>.value = "<%=transPlanList.getParameterValue(TransPlanList.QP_DATE1)%>";
	queryForm.<%=TransPlanList.QP_DATE2%>.value = "<%=transPlanList.getParameterValue(TransPlanList.QP_DATE2)%>";
	queryForm.<%=TransPlanList.QP_LOAD_NAME%>.value = "<%=transPlanList.getParameterValue(TransPlanList.QP_LOAD_NAME)%>";
	queryForm.<%=TransPlanList.QP_USER_ZH%>.value = "<%=transPlanList.getParameterValue(TransPlanList.QP_USER_ZH)%>";
	queryForm.<%=AbstractQuery.QP_PAGE_SIZE%>.value = "<%=transPlanList.getPageSize()%>";
</script>

<script type="text/javascript">
$(document).ready( function () {
	$("#thDate1,#thDate2").datepicker({dateFormat: 'yy-mm-dd'});
});
function openNew( ) {
	var strStatus = "toolbar=no,Scrollbars=yes,status=yes,width=800,height=600,left=" + getCenteredLeft(800) + ",top=" + getCenteredTop(600);
	window.open( "transPlanNew.jsp?WM=<%=CtrlConstants.WM_CHILD%>", "NewTransPlan", strStatus, false );
}
function openUpdate( id ) {
	var strStatus = "toolbar=no,Scrollbars=yes,status=yes,width=800,height=600,left=" + getCenteredLeft(800) + ",top=" + getCenteredTop(600);
	window.open( "transPlanUpdate.jsp?WM=<%=CtrlConstants.WM_CHILD%>&ID="+id, "UpdateTransPlan", strStatus, false );
}
</script>

</body>
</html>
