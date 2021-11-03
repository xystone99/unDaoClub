<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import = "com.cuanfun.utils.*" %>
<%@ page import = "com.cuanfun.database.*" %>
<%@ page import = "com.cuanfun.control.*" %>
<%@ page import = "com.cuanfun.cache.*" %>
<%@ page import = "com.cuanfun.enumeration.*" %>
<%@ page import = "xms.*" %>
<%@ page import = "xms.beans.hr.*" %>
<%
	int PAGE_TAG = 0;
	boolean acceptInnerUser = true;
	String[] needAstricts = { SysAstricts.QX_SYS_CONFIGURE };
%>
<%@ include file="/include/inc_header.logic" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>  
<title>修改员工权限信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" /> 
<link rel="stylesheet" type="text/css" href="/css/childWindow.css" />
</head>

<body>
<%
	Employee emp = new Employee( XmsInitial.getDataSource(), Employee.BTYPE_DETAIL );
	AbstractDaemon.fixQueryParams( emp, request );
	emp.executeQuery();
	CommonSet dataSet = emp.getQueryResult();
	boolean ifRootUser = AbstractDaemon.isSystemAdministrator(request);
%>
<form name="myForm" method="post" action="processSysConfigure.jsp" onsubmit="return checkValue(this);">
<table cellspacing="0" cellpadding="0">
	<tr class="head_tr">
	<td width="100%" align="left"><b>修改员工权限信息&gt;&gt;</b></td>
	</tr>
</table><br/>

<table cellspacing="0" cellpadding="0">	
	<tr class="content_tr2">
	<td width="18%" align="right">姓&nbsp;&nbsp;名:</td>
	<td width="1%">&nbsp;</td>
	<td width="32%" align="left"><input type="text" name="<%=Employee.QP_NAME_ZH%>" size="12" maxlength="20" readonly="readonly" /></td>
	<td width="17%" align="right">工号:</td>
	<td width="1%">&nbsp;</td>
	<td width="31%" align="left"><input type="text" name="<%=Employee.QP_JOB_ID%>" size="12" maxlength="20" style="text-transform:uppercase;" readonly="readonly" /></td>
	</tr>   
	
	<tr class="content_tr2">
	<td align="right">所属公司/部门:</td>
	<td >&nbsp;</td>
	<td align="left"><select name="<%=Employee.QP_COMPANY%>"><%=Company.PURE_OPTIONS%></select>&nbsp;<select name="<%=Employee.QP_PART%>"><%=MasterPart.ZERO_OPTIONS %></select></td>
	<td align="right">所属岗位:</td>
	<td >&nbsp;</td>
	<td align="left"><select name="<%=Employee.QP_POST%>"><%=MasterPost.ZERO_OPTIONS %></select></td>
	</tr>
	
	<tr class="content_tr2"><td colspan="6"></td></tr> 

	<tr class="content_tr2">
	<td align="right">员工类别:</td>
	<td >&nbsp;</td>
	<td align="left" colspan="4">
		<input type="checkbox" id="cbIfDriver" name="cbIfDriver" onclick="javascript:checkSelectType(this,myForm.<%=Employee.QP_IF_DRIVER%>)" /><label for="cbIfDriver">司机</label>&nbsp;&nbsp;
		<input type="checkbox" id="cbIfLoading" name="cbIfLoading" onclick="javascript:checkSelectType(this,myForm.<%=Employee.QP_IF_LOADING%>)" /><label for="cbIfLoading">装卸工</label>&nbsp;&nbsp;
		<input type="checkbox" id="cbIfOil" name="cbIfOil" onclick="javascript:checkSelectType(this,myForm.<%=Employee.QP_IF_OIL%>)" /><label for="cbIfOil">加油员</label>&nbsp;&nbsp;
		<input type="checkbox" id="cbIfCharge" name="cbIfCharge" onclick="javascript:checkSelectType(this,myForm.<%=Employee.QP_IF_CHARGE%>)" /><label for="cbIfCharge">收付负责人</label>&nbsp;&nbsp;
		<input type="hidden" name="<%=Employee.QP_IF_DRIVER%>" value="N" />
		<input type="hidden" name="<%=Employee.QP_IF_LOADING%>" value="N" />
		<input type="hidden" name="<%=Employee.QP_IF_OIL%>" value="N" />
		<input type="hidden" name="<%=Employee.QP_IF_CHARGE%>" value="N" />
	</td>
	
	</tr>
	
	<tr class="content_tr2">
	<td align="right">快速检索代码:</td>
	<td >&nbsp;</td>
	<td align="left"><input type="text" name="<%=Employee.QP_INIT_SPELL%>" size="12" maxlength="5" /></td>
	<td align="right">排序标记:</td>
	<td >&nbsp;</td>
	<td align="left"><input type="text" name="<%=Employee.QP_SORT_TAG%>" size="12" maxlength="5" /></td>
	</tr>
	
	<tr class="content_tr2">
	<td align="right">可考核:</td>
	<td >&nbsp;</td>
	<td align="left"><select name="<%=Employee.QP_CAN_EVAL%>"><option value="N">禁止</option><option value="Y">允许</option></select></td>
	<td align="right">考核人:</td>
	<td >&nbsp;</td>
	<td align="left"><select name="<%=Employee.QP_EMP_EVAL%>"><option value="0">-未选择-</option><%=EvaluatedEmployee.getInstance().getNormalOptions() %></select></td>
	</tr>
	
	<tr class="content_tr2"><td colspan="6"></td></tr> 
	
	<tr class="content_tr2">
	<td align="right">登录名:</td>
	<td >&nbsp;</td>
	<td align="left"><input type="text" name="<%=Employee.QP_LOGIN_NAME%>" size="16" maxlength="20" /></td>
	<td align="right">入职日期:</td>
	<td >&nbsp;</td>
	<td align="left"><input type="text" name="<%=Employee.QP_JOIN_DATE%>" size="12" maxlength="10" /></td>
	</tr>
	
	<tr class="content_tr2">
	<td align="right">可登录/有效标记:</td>
	<td >&nbsp;</td>
	<td align="left">
		<select name="<%=Employee.QP_CAN_LOGIN%>"><option value="N">禁止</option><option value="Y">允许</option></select>
		<select name="<%=Employee.QP_IF_VALID%>"><option value="Y">有效</option><option value="N">无效</option></select>
		<select name="<%=Employee.QP_ASTRICT_LEVEL%>"><option value="0">默认</option><option value="KF">客服</option><option value="CW">财务</option></select>
		</td>
	<td align="right">角&nbsp;&nbsp;色:</td>
	<td >&nbsp;</td>
	<td align="left"><select name="<%=Employee.QP_ROLE%>"><option value="0">-未选择-</option><%=ifRootUser ? MasterRole.getInstance().getUsableOptions() : MasterRole.getInstance().getNormalOptions()%></select></td>
	</tr>
	
	<tr class="content_tr2">	
	<td align="right">公司列表(行政):</td>
	<td >&nbsp;</td>
	<td align="left" colspan="4"><input type="text" name="<%=Employee.QP_M_COMPANIES%>" size="72" maxlength="120" /></td>	
	</tr>
	
	<tr class="content_tr2">	
	<td align="right">公司列表(业务):</td>
	<td >&nbsp;</td>
	<td align="left" colspan="4"><input type="text" name="<%=Employee.QP_B_COMPANIES%>" size="72" maxlength="120" /></td>	
	</tr>    	
</table><br/>

<table cellspacing="0" cellpadding="0">
	<tr class="tail_tr">
	<td width="100%" align="right">
		<input type="hidden" name="Action" value="SecurityUpdate" />
		<input type="hidden" name="<%=Employee.QP_EMP%>" value="<%=request.getParameter("QpEmp")%>" />
		<input class="input_btn" type="submit" name="Submit" value="提交信息" />
	</td>
	</tr>
</table>
</form>

<script type="text/javascript" src="/include/stc_validate.js"></script>
<script type="text/javascript" src="/include/stc_function.js"></script>

<script type="text/javascript">		<!--为表单赋值-->
	myForm.<%=Employee.QP_NAME_ZH%>.value = "<%=dataSet.getValue("ne_zh")%>";
	myForm.<%=Employee.QP_JOB_ID%>.value = "<%=dataSet.getValue("job_id")%>";
	myForm.<%=Employee.QP_COMPANY%>.value = "<%=dataSet.getValue("company")%>";
	myForm.<%=Employee.QP_PART%>.value = "<%=dataSet.getValue("part")%>";
	myForm.<%=Employee.QP_POST%>.value = "<%=dataSet.getValue("post")%>";
	myForm.<%=Employee.QP_IF_DRIVER%>.value = "<%=dataSet.getValue("if_driver")%>";
	myForm.<%=Employee.QP_IF_LOADING%>.value = "<%=dataSet.getValue("if_loading")%>";
	myForm.<%=Employee.QP_IF_OIL%>.value = "<%=dataSet.getValue("if_oil")%>";
	myForm.<%=Employee.QP_IF_CHARGE%>.value = "<%=dataSet.getValue("if_charge")%>";
	
	setCheckValue(myForm.cbIfDriver, myForm.<%=Employee.QP_IF_DRIVER%>.value, "Y" );
	setCheckValue(myForm.cbIfLoading, myForm.<%=Employee.QP_IF_LOADING%>.value, "Y" );
	setCheckValue(myForm.cbIfOil, myForm.<%=Employee.QP_IF_OIL%>.value, "Y" );
	setCheckValue(myForm.cbIfCharge, myForm.<%=Employee.QP_IF_CHARGE%>.value, "Y" );
	
	myForm.<%=Employee.QP_JOIN_DATE%>.value = "<%=dataSet.getValue("join_date")%>";
	myForm.<%=Employee.QP_INIT_SPELL%>.value = "<%=dataSet.getValue("init_spell")%>";
	myForm.<%=Employee.QP_SORT_TAG%>.value = "<%=dataSet.getValue("sort_tag")%>";
	myForm.<%=Employee.QP_CAN_EVAL%>.value = "<%=dataSet.getValue("can_eval")%>";
	myForm.<%=Employee.QP_EMP_EVAL%>.value = "<%=dataSet.getValue("emp_eval")%>";
	myForm.<%=Employee.QP_LOGIN_NAME%>.value = "<%=dataSet.getValue("login_name")%>";
	myForm.<%=Employee.QP_CAN_LOGIN%>.value = "<%=dataSet.getValue("can_login")%>";
	myForm.<%=Employee.QP_IF_VALID%>.value = "<%=dataSet.getValue("if_valid")%>";
	myForm.<%=Employee.QP_ASTRICT_LEVEL%>.value = "<%=dataSet.getValue("astrict_level")%>";
	myForm.<%=Employee.QP_ROLE%>.value = "<%=dataSet.getValue("role")%>";
	myForm.<%=Employee.QP_M_COMPANIES%>.value = "<%=dataSet.getValue("m_companies")%>";
	myForm.<%=Employee.QP_B_COMPANIES%>.value = "<%=dataSet.getValue("b_companies")%>";
</script>

<script type="text/javascript">
function checkSelectType( objCheck, objType ) {
	objType.value = objCheck.checked ? "Y" : "N";
}
function checkValue( inForm ) {
	return confirm( "确定提交吗？" );
};
</script>

</body>
</html>