<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import = "xms.beans.customerService.*" %>
<%@ page import="com.undao.enumeration.SysAstricts" %>
<%@ page import="com.undao.enumeration.EnumConstants" %>
<%@ page import="xms.XmsInitial" %>
<%@ page import="com.undao.control.AbstractDaemon" %>
<%
	int PAGE_TAG = 0;
	boolean acceptInnerUser = true;
	String[] needAstricts = {SysAstricts.QX_CUS_UPDATE  };
%>
<%@ include file="../include/inc_header.logic" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>  
<title>新建运输计划</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<link rel="stylesheet" type="text/css" href="../css/childWindow.css" />
<link rel="stylesheet" type="text/css" href="../css/jquery-ui-1.10.4.custom.css" />
</head>

<body>
<form name="myForm" method="post" action="processBusiness.jsp" onsubmit="return checkValue(this);">
<table cellspacing="0" cellpadding="0">
	<tr class="head_tr">
	<td width="100%" align="left"><b>新建运输计划&gt;&gt;</b></td>
	</tr>
	<tr class="empty_tr2"><td colspan="6"></td></tr>
</table><br/>

<table id="date" cellspacing="0" cellpadding="0">
	<tr class="content_tr">
	<td width="13%" align="right">客户简称:</td>
	<td width="1%"></td>
	<td width="33%" align="left"><input type="text" id="acCus" name="acCus" size="16" maxlength="20" placeholder="空格或关键字首字母" class="input_text" />
		<input type="hidden" name="<%=TransPlan.QP_OBJ_P%>" value="0" /></td>
	<td width="13%" align="right">发货日期:</td>
	<td width="1%"></td>
	<td width="39%" align="left"><input id="thDate1" type="text" name="<%=TransPlan.QP_PLAN_DATE%>" size="12" maxlength="10" class="input_text" ondblclick="javascript:setCurrentDate(this)" placeholder="双击" /></td>
	</tr>
	<tr class="content_tr30">
	<td align="right">运输线路:</td>
	<td></td>
	<td align="left" colspan="4"><input type="text" id="acTransLine" name="acTransLine" size="30" maxlength="50" placeholder="空格或关键字" class="input_text" />
		<input type="hidden" name="<%=TransPlan.QP_TRANS_L%>" value="0" /></td>
	</tr>

	<tr class="empty_tr"><td colspan="6"></td></tr><tr class="empty_tr"><td colspan="6"></td></tr>

	<tr class="content_tr">
	<td align="right">发货方:</td>
	<td></td>
	<td align="left"><input type="text" name="<%=TransPlan.QP_NE_ZH1%>" size="16" maxlength="20" class="input_text" readonly /></td>
	<td align="right">窗口时间:</td>
	<td></td>
	<td align="left"><input type="text" name="<%=TransPlan.QP_WINDOW_1%>" size="20" maxlength="20" class="input_text" /></td>
	</tr>

	<tr class="content_tr">
	<td align="right">发货联系人:</td>
	<td></td>
	<td align="left"><input type="text" name="<%=TransPlan.QP_LINKMAN_1%>" size="24" maxlength="20" class="input_text" readonly /></td>
	<td align="right">详细地址:</td>
	<td></td>
	<td align="left"><input type="text" name="<%=TransPlan.QP_ADDRESS_1%>" size="32" maxlength="20" class="input_text" readonly /></td>
	</tr>

	<tr class="content_tr">
	<td align="right">其它说明:</td>
	<td></td>
	<td align="left" colspan="4"><input type="text" name="<%=TransPlan.QP_REMARK_1%>" size="80" maxlength="50" class="input_text" placeholder="单据、道口、特殊要求等" /></td>
	</tr>
	<tr class="empty_tr"><td colspan="6"></td></tr>

	<tr class="content_tr">
	<td align="right">收货方:</td>
	<td></td>
	<td align="left"><input type="text" name="<%=TransPlan.QP_NE_ZH2%>" size="16" maxlength="20" class="input_text" readonly /></td>
	<td align="right">窗口时间:</td>
	<td></td>
	<td align="left"><input type="text" name="<%=TransPlan.QP_WINDOW_2%>" size="20" maxlength="20" class="input_text" /></td>
	</tr>

	<tr class="content_tr">
	<td align="right">收货联系人:</td>
	<td></td>
	<td align="left"><input type="text" name="<%=TransPlan.QP_LINKMAN_2%>" size="24" maxlength="20" class="input_text" readonly /></td>
	<td align="right">详细地址:</td>
	<td></td>
	<td align="left"><input type="text" name="<%=TransPlan.QP_ADDRESS_2%>" size="32" maxlength="20" class="input_text" readonly /></td>
	</tr>

	<tr class="content_tr">
	<td align="right">其它说明:</td>
	<td></td>
	<td align="left" colspan="4"><input type="text" name="<%=TransPlan.QP_REMARK_2%>" size="80" maxlength="50" class="input_text" placeholder="单据、道口、特殊要求等" /></td>
	</tr>

	<tr class="empty_tr"><td colspan="6"></td></tr><tr class="empty_tr"><td colspan="6"></td></tr>

	<tr class="content_tr30">
	<td align="right">运输类型:</td>
	<td></td>
		<td lign="left"><select name="<%=TransPlan.QP_PLAN_K%>" class="select" onchange="javascript:changePlanK(this)"><option value="0">--选择运输类型--</option><%=EnumConstants.TRANS_PLAN_K_OPTIONS%></select></td>
	<td align="right">时效要求等级:</td>
	<td></td>
	<td lign="left"><select name="<%=TransPlan.QP_TIME_LEVEL%>" class="select"><option value="0">--时效要求等级--</option><%=EnumConstants.TIME_LEVEL_OPTIONS%></select></td>
	</tr>

	<tr id="trRecycle" class="content_tr30">
	<td align="right">返空占车米数:</td>
	<td></td>
	<td align="left" colspan="4"><input type="text" name="<%=TransPlan.QP_QTY_METER_R%>" size="12" maxlength="3" class="input_text" />&nbsp;&nbsp;
		<select id="whList" name="sWareList" class="select" onchange="javascript:changeMultiSelected(this,myForm.<%=TransPlan.QP_NE_RECYCLE%>)"><option value="0">--选择返空仓库--</option><%=EnumConstants.WH_RECYCLE_OPTIONS%></select>&nbsp;<span id="multi_selected_list"></span>
		<input type="hidden" name="<%=TransPlan.QP_NE_RECYCLE%>" /></td>
	</tr>

	<tr class="content_tr30">
	<td align="right">占车米数:</td>
	<td></td>
	<td align="left"><input type="text" name="<%=TransPlan.QP_QTY_METER%>" size="12" maxlength="3" class="input_text" /></td>
	<td align="right">重量/体积:</td>
	<td></td>
	<td align="left"><input type="text" name="<%=TransPlan.QP_QTY_W%>" size="5" maxlength="7" class="input_text" />(吨)&nbsp;/&nbsp;<input type="text" name="<%=TransPlan.QP_QTY_V%>" size="5" maxlength="7" class="input_text" />(方)</td>
	</tr>
</table><br/>

<table cellspacing="0" cellpadding="0">
	<tr class="tail_tr">
	<td align="left"><input type="button" name="reset" value="刷新页面" class="input_btn" onclick="javascript:reloadCurrentPage();" /></td>
	<td align="right">
		<input type="hidden" name="Action" value="TransPlanNew" />
		<input type="submit" name="btnSubmit" value="提交信息" class="input_btn" />
	</td>
	</tr>
</table>
</form>

<script type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" src="../js/jquery-ui.min.js"></script>
<script type="text/javascript" src="../js/jquery.form.js"></script>

<script type="text/javascript" src="../include/stc_validate.js"></script>
<script type="text/javascript" src="../include/stc_function.js"></script>
<script type="text/javascript" src="../include/udTransPlan.js"></script>

<script type="text/javascript">
$(document).ready( function () {
    jQueryLoad( "<%=XmsInitial.getContextPath()%>" );
    $("#trRecycle").hide( );
    setIntervalDate( myForm.<%=TransPlan.QP_PLAN_DATE%>, 1 );
});
</script>

</body>
</html>