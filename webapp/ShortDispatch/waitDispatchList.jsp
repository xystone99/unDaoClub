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
<%@ page import="xms.queries.shortDispatch.WaitDispatchList" %>
<%@ page import="com.undao.control.AbstractDaemon" %>
<%@ page import="xms.beans.shortDispatch.Dispatch" %>
<%
	int PAGE_TAG = CtrlConstants.PG_WAIT_DISPATCH_LIST;
	boolean acceptInnerUser = true;
	String[] needAstricts = { SysAstricts.QX_DISPATCH_READ  };
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
WaitDispatchList waitDispatchList = new WaitDispatchList( XmsInitial.getDataSource() );
waitDispatchList.setCloudID(AbstractDaemon.getCloudID(request));

String cur_page = request.getParameter( WaitDispatchList.QP_CUR_PAGE );
if ( cur_page != null ) {
	waitDispatchList.setCurrentPage( Integer.parseInt( cur_page ) );
	waitDispatchList.setPageSize( Integer.parseInt(request.getParameter( WaitDispatchList.QP_PAGE_SIZE )) );

	String[] arr_params = waitDispatchList.getParamSerial();
	for ( int j=0; j<arr_params.length; j++ ) {
		waitDispatchList.setParameterValue( arr_params[j], request.getParameter( arr_params[j]) );
	}
} else {
	waitDispatchList.setDefaultParameterValue( );
}

waitDispatchList.executeQuery();
CommonSet dataSet = waitDispatchList.getQueryResult( );
%>

<div id="container">
<%@ include file="../include/menu_2_ShortDispatch.block" %>

<form name="queryForm" method="post" action="waitDispatchList.jsp">
<table width="100%" border="0" cellspacing="1" cellpadding="0">
	<tr class="query_tr">
	<td align="right" width="950">
		装卸地名称:
		<input type="text" name="<%=WaitDispatchList.QP_LOAD_NAME%>" size="10" maxlength="10" class="input_text" />&nbsp;&nbsp;&nbsp;&nbsp;
		发货日期:
		<input id="pDate" type="text" name="<%=WaitDispatchList.QP_DATE%>" size="10" maxlength="10" class="input_text" ondblclick="javascript:setCurrentDate(this)" />&nbsp;&nbsp;&nbsp;&nbsp;
		调度路径:
		<input type="text" name="<%=WaitDispatchList.QP_ROUTE_ZH%>" size="10" maxlength="10" class="input_text" />&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" name="btnQuery" value="开始检索" class="input_text" onclick="javascript:checkCommit('Query')" />
	</td>
	<td align="right" bgcolor="#bdb76b">
		车牌号:
		<input type="text" id="acTruck" name="acTruck" size="8" maxlength="20" placeholder="快速检索" class="input_text" />
		<input type="text" name="<%=Dispatch.QP_PLATE_NUMBER%>" size="10" maxlength="20" class="input_text" />
		<input type="hidden" name="<%=Dispatch.QP_TRUCK%>" value="0" />&nbsp;&nbsp;&nbsp;&nbsp;
		司机：
		<input type="text" id="acDriver" name="acDriver" size="8" maxlength="20" placeholder="快速检索" class="input_text" />
		<input type="text" name="<%=Dispatch.QP_TEL_DRIVER%>" size="16" maxlength="20" class="input_text" />
		<input type="hidden" name="<%=Dispatch.QP_DRIVER%>" value="0" />&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" name="btnCreate" value="保存车次" class="input_text" onclick="javascript:checkCommit('Save')" />
	</td>
	<td align="right">

	</td>
	</tr>
	<tr class="query_tr">
	<td align="right">
		<input type="checkbox" id="cbIf" name="cbIfHide" onclick="javascript:setHideValueOfCheck(this,queryForm.<%=WaitDispatchList.QP_IF_HIDE%>)" /><label for="cbIf">隐藏已安排计划</label>&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="hidden" name="<%=WaitDispatchList.QP_IF_HIDE%>" value="Y" />

		<select name="<%=WaitDispatchList.QP_SORT_TAG%>">
			<option value="<%=WaitDispatchList.INPUT_DATE_ASC%>">--排序规则--</option>
			<option value="<%=WaitDispatchList.INPUT_DATE_DESC%>">创建日期降序</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;

		<select name="<%=WaitDispatchList.QP_PLAN_K%>" class="select"><option value="All">--计划类型--</option><%=EnumConstants.TRANS_PLAN_K_OPTIONS%></select>&nbsp;&nbsp;&nbsp;&nbsp;
		客户简称:
		<input type="text" name="<%=WaitDispatchList.QP_OBJECT_P%>" size="10" maxlength="10" class="input_text" />&nbsp;&nbsp;&nbsp;&nbsp;
		计划人员:
		<input type="text" name="<%=WaitDispatchList.QP_USER_ZH%>" size="10" maxlength="10" class="input_text" />&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" name="btnExport" value="导出报表" class="input_text" onclick="javascript:checkCommit('Export')" />
	</td>
	<td align="right" bgcolor="#bdb76b">
		其它说明:
		<input type="text" name="<%=Dispatch.QP_REMARK%>" size="50" maxlength="50" class="input_text" />&nbsp;&nbsp;&nbsp;&nbsp;
		副驾：
		<input type="text" id="acSubDriver" name="acSubDriver" size="8" maxlength="20" placeholder="快速检索" class="input_text" />
		<input type="text" name="fSubDriverName" size="16" maxlength="20" class="input_text"  readonly="readonly" />
		<input type="hidden" name="<%=Dispatch.QP_SUB_DRIVER%>" value="0" />&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" name="btnRefresh" value="刷新页面" class="input_text" onclick="javascript:checkCommit('Refresh')" />
	</td>
	</tr>
</table>

<table width="100%" border="0" cellpadding="0" cellspacing="1" class="parent_table">
	<thead>
	<tr>
	<th width="50">序号</th>
	<th width="130">日期-计划员</th>
	<th width="100">客户简称</th>
	<th width="80">运输类型</th>
	<th width="80">时效要求</th>
	<th width="160">发货方-发货窗口</th>
	<th width="160">发货说明</th>
	<th width="160">收货方-收货窗口</th>
	<th width="160">收货说明</th>
	<th width="160">返空箱说明</th>
	<th width="130">货量(吨位&方数)</th>
	<th width="80">占车米数</th>
	<th width="90">调度路径</th>
	<th width="80">操作</th>
	<th >已安排车次</th>
	</tr>
	</thead>
	
	<tbody>
	<%
	int baseIndex = waitDispatchList.getBaseIndex();
	BigDecimal pageWeight = new BigDecimal(0);
	BigDecimal pageVolume = new BigDecimal(0);
	BigDecimal pageMeter = new BigDecimal(0);
	for ( int j = 0; j < dataSet.getRowCount(); j ++ ) {
		%>
		<tr bgcolor="<%=j%2==1?"#EFFFFF":"#EEEEEEE" %>">
		<td align="center"><%=baseIndex+j %></td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"plan_date")%><br/>&nbsp;[<%=dataSet.getValue(j,"user_zh")%>]</td>
		<td align="center"><%=dataSet.getValue(j,"obj_short")%></td>
		<td align="center"><%=dataSet.getValue(j,"plan_k")%></td>
		<td align="center"><%=dataSet.getValue(j,"time_level")%></td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"ne_zh1")%><br/>&nbsp;[<%=dataSet.getValue(j,"window_1")%>]</td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"remark_1")%></td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"ne_zh2")%><br/>&nbsp;[<%=dataSet.getValue(j,"window_2")%>]</td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"remark_2")%></td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"ne_recycle")%><br/>&nbsp;<%=DecimalUtils.formatQty(dataSet.getValue(j,"qty_meter_r"),false,"米")%></td>
		<td align="right"><%=DecimalUtils.formatQty(dataSet.getValue(j,"qty_w"),false,"吨")%>&nbsp;<%=DecimalUtils.formatQty(dataSet.getValue(j,"qty_v"),false,"方")%>&nbsp;</td>
		<td align="right"><%=DecimalUtils.formatQty(dataSet.getValue(j,"qty_meter"),false,"米")%>&nbsp;</td>
		<td align="left">&nbsp;<%=dataSet.getValue(j,"route_zh")%></td>
		<td><input type="checkbox" id="cb<%=j%>" name="cb<%=j%>" onchange="javascript:checkSelectedTransPlan(this,'<%=dataSet.getValue(j,"trans_p")%>')" /><label for="cb<%=j%>">选择</label>&nbsp;&nbsp;
			<input type="hidden" name="hID<%=j%>" value="<%=dataSet.getValue(j,"trans_p")%>" /></td>

		<td align="left">&nbsp;<%=dataSet.getValue(j,"dispatch_remark")%>&nbsp;</td>
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
	<td align="right">本页合计：</td>
	<td align="right"><%=DecimalUtils.formatQty(pageWeight,true,"吨")%>&nbsp;<%=DecimalUtils.formatQty(pageVolume,true,"方")%>&nbsp;</td>
	<td align="right"><%=DecimalUtils.formatQty(pageMeter,true,"米")%>&nbsp;</td>
	<td></td>
	<td></td>
	<td></td>
	</tr>
	</tbody> 
</table>
<table width="100%" border="0" cellpadding="0" cellspacing="1">
	<tfoot><tr><td align="right"><%=XmsUtils.buildPageSeeker2("queryForm",waitDispatchList.getCurrentPage(),waitDispatchList.numOfPages()) %></td></tr></tfoot>
	<input type="hidden" name="<%=Dispatch.QP_TRANS_PLANS%>" />
</table>
</form> 

</div><!-- container -->
<%@ include file="../include/menu_3_Footer.block" %>
<script type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" src="../js/jquery-ui.min.js"></script>
<script type="text/javascript" src="../js/jquery.form.js"></script>

<script type="text/javascript" src="../include/stc_validate.js"></script>
<script type="text/javascript" src="../include/stc_function.js"></script>
<script type="text/javascript">
	queryForm.<%=WaitDispatchList.QP_SORT_TAG%>.value = "<%=waitDispatchList.getParameterValue(WaitDispatchList.QP_SORT_TAG)%>";
	queryForm.<%=WaitDispatchList.QP_PLAN_K%>.value = "<%=waitDispatchList.getParameterValue(WaitDispatchList.QP_PLAN_K)%>";
	queryForm.<%=WaitDispatchList.QP_OBJECT_P%>.value = "<%=waitDispatchList.getParameterValue(WaitDispatchList.QP_OBJECT_P)%>";
	queryForm.<%=WaitDispatchList.QP_DATE%>.value = "<%=waitDispatchList.getParameterValue(WaitDispatchList.QP_DATE)%>";
	queryForm.<%=WaitDispatchList.QP_LOAD_NAME%>.value = "<%=waitDispatchList.getParameterValue(WaitDispatchList.QP_LOAD_NAME)%>";
	queryForm.<%=WaitDispatchList.QP_USER_ZH%>.value = "<%=waitDispatchList.getParameterValue(WaitDispatchList.QP_USER_ZH)%>";
	queryForm.<%=WaitDispatchList.QP_ROUTE_ZH%>.value = "<%=waitDispatchList.getParameterValue(WaitDispatchList.QP_ROUTE_ZH)%>";
	queryForm.<%=AbstractQuery.QP_PAGE_SIZE%>.value = "<%=waitDispatchList.getPageSize()%>";

	queryForm.<%=WaitDispatchList.QP_IF_HIDE%>.value = "<%=waitDispatchList.getParameterValue(WaitDispatchList.QP_IF_HIDE)%>";
	setCheckValue(queryForm.cbIfHide, queryForm.<%=WaitDispatchList.QP_IF_HIDE%>.value, "Y" );
</script>

<script type="text/javascript">
	var countRows = "<%=dataSet.getRowCount()%>";
	$(document).ready( function () {
		$("#pDate").datepicker({dateFormat: 'yy-mm-dd'});
	});
	$("#acTruck").autocomplete({ source: "<%=XmsInitial.getContextPath()%>/fetchtruck?Action=Normal", minLength: 1, select: function(event, ui ) {
		queryForm.<%=Dispatch.QP_TRUCK%>.value = ui.item.ID;
		queryForm.<%=Dispatch.QP_PLATE_NUMBER%>.value = ui.item.PlateNumber;
		if ( ui.item.ID == "0" ) {
			$("input[name='<%=Dispatch.QP_PLATE_NUMBER%>']").removeAttr("readonly");;
		} else {
			$("input[name='<%=Dispatch.QP_PLATE_NUMBER%>']").attr("readonly","readonly");
		}
	}});

	$("#acDriver").autocomplete({ source: "<%=XmsInitial.getContextPath()%>/fetchdriver?Action=Driver", minLength: 1, select: function(event, ui ) {
		queryForm.<%=Dispatch.QP_DRIVER%>.value = ui.item.ID;
		queryForm.<%=Dispatch.QP_TEL_DRIVER%>.value = ui.item.Name;
		if ( ui.item.ID == "0" ) {
			$("input[name='<%=Dispatch.QP_TEL_DRIVER%>']").removeAttr("readonly");;
		} else {
			$("input[name='<%=Dispatch.QP_TEL_DRIVER%>']").attr("readonly","readonly");
		}
	}});
	$("#acSubDriver").autocomplete({ source: "<%=XmsInitial.getContextPath()%>/fetchdriver?Action=SubDriver", minLength: 1, select: function(event, ui ) {
		queryForm.<%=Dispatch.QP_SUB_DRIVER%>.value = ui.item.ID;
		queryForm.fSubDriverName.value = ui.item.Name;
	}});
	$(".parent_table tr").click(function() {
		$(this).addClass("hover").siblings("tr").removeClass("hover");
	});
</script>

<script type="text/javascript">
function checkSelectedTransPlan( obj, transPlanID ) {
	var transPlans = queryForm.<%=Dispatch.QP_TRANS_PLANS%>.value;
	if ( obj.checked == true ) {
		queryForm.<%=Dispatch.QP_TRANS_PLANS%>.value = transPlans + transPlanID + "-";
	} else {
		for ( var j=0; j<countRows; j++ ) {
			queryForm["cb"+j].checked = false;
			queryForm.<%=Dispatch.QP_TRANS_PLANS%>.value = "";
		}
		alert( "请重新选择。\n选择的第一条线路为计件线路。" )
	}
}

function checkCommit( actionTag ) {
	if ( actionTag == "Export") {
		alert( "正在建设中......" );
	} else if ( actionTag == "Refresh" ) {
		window.location.reload();
	} else if ( actionTag == "Save" ) {
		if ( isNull(queryForm.<%=Dispatch.QP_PLATE_NUMBER%>) ) {
			alert( "请选择或录入车牌号！" );
			return;
		}
		if ( isNull(queryForm.<%=Dispatch.QP_TEL_DRIVER%>) ) {
			alert( "请选择或录入司机信息！" );
			return;
		}
		if ( isNull(queryForm.<%=Dispatch.QP_TRANS_PLANS%>) ) {
			alert( "请选择关联的运输计划！" );
			return;
		}
		if ( confirm( "确认保存车次吗？" ) ) {
			queryForm.target = "_self";
			queryForm.action = "processBusiness.jsp?Action=DispatchNew";
			queryForm.submit();
		}
	} else if ( actionTag == "Query" ) {
		queryForm.target = "_self";
		queryForm.action = "waitDispatchList.jsp";
		queryForm.submit();
	}
};
</script>

</body>
</html>
