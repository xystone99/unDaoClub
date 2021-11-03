<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import="xms.XmsInitial" %>
<%@ page import="xms.procedures.LoginSystem" %>
<%@ page import="com.undao.control.CtrlConstants" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>  
<title>管理工具</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<link rel="stylesheet" type="text/css" href="./css/loginDefault.css" />
<link rel="stylesheet" type="text/css" href="./css/userLogin.css" />
</head>

<body id="userlogin_body">
<form name="myForm" method="post" action="./processLogin.jsp" onsubmit="return checkValue(this);">
<div></div>

<div id="user_login">
<dl>
	<dd id="user_top">
	<ul>
    <li class="user_top_l"></li>
    <li class="user_top_c"></li>
    <li class="user_top_r"></li>
    </ul>
    </dd>
	<dd id=user_main>
	<ul>
    <li class=user_main_l></li>
    <li class=user_main_c>
    	<div class=user_main_box>
		<ul>
		<li class=user_main_text>用户名：  </li>
		<li class=user_main_input><input class=TxtUserNameCssClass id=TxtUserName name="<%=LoginSystem.QP_LOGIN_NAME%>" maxlength="15" /></li>
		</ul>
		<ul>
		<li class=user_main_text>密&nbsp;&nbsp;&nbsp;&nbsp;码： </li>
		<li class=user_main_input><input class=TxtPasswordCssClass id=TxtPassword type=password name="<%=LoginSystem.QP_LOGIN_PWD%>" maxlength="15" /></li></ul>
		<ul>
		<li class=user_main_text>Cookie： </li>
		<li class=user_main_input>
			<select id=DropExpiration name=DropExpiration> 
			<option value=None>不保存</option> 
			</select>
		</li>
		</ul>
		</div>
	</li>
    
    <li class=user_main_r>
    	<input type="hidden" name="Action" value="Login" />
    	<input class=IbtnEnterCssClass id=IbtnEnter style="BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; BORDER-RIGHT-WIDTH: 0px" onclick='javascript:WebForm_DoPostBackWithOptions(new WebForm_PostBackOptions("IbtnEnter", "", true, "", "", false, false))' type=image src="images/user_botton.gif" name=IbtnEnter />
    </li>
    </ul>
    </dd>
	
	<dd id=user_bottom>
	<ul>
    <li class=user_bottom_l></li>
	<li class=user_bottom_c><span style="MARGIN-TOP: 40px"><!--© XYZABC.COM 版权所有&nbsp;  <a href="http://beian.miit.gov.cn/" target="_blank">沪ICP备2021015242号</a>--></span></li>
    <li class=user_bottom_r><%=XmsInitial.getXmsContainer().getSystemicVariables().getClass().getName()%>-<%=XmsInitial.getXmsContainer().getSystemicVariables().isGlobalAcceptLogin()%></li>
    </ul>
    </dd>
</dl>
</div>
	<div><input type="hidden" name="<%=LoginSystem.QP_CLOUD_ID%>" value="<%=CtrlConstants.INNER_CLOUD_ID%>"></div>
</form>

<script type="text/javascript" src="./include/stc_validate.js"></script>
<script type="text/javascript" src="./include/stc_function.js"></script>

<script type="text/javascript">
	myForm.tLoginName.value = getCookieValue( "tUserName" );
</script>

<script type="text/javascript">
function checkValue( theForm ) { 
	if ( !checkValidBrowser( ) ) {
		alert( "建议使用IE11/Edge/Chrome浏览器......" );
	}
	if ( isNull( theForm.tLoginName ) ) {
		alert( "请输入用户名！" );
		return false;
	}
	if ( isNull( theForm.tLoginPwd ) ) {
		alert( "请输入密码！" );
		return false;
	}	
	return true;
}
function checkValidBrowser( ) {
	var userAgent = navigator.userAgent;	//取得浏览器的userAgent字符串
	var isOpera = userAgent.indexOf("Opera") > -1;	//判断是否Opera浏览器
	var isIE11 = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera;	//判断是否IE浏览器
	var isEdge = userAgent.indexOf("Edge") > -1;	//判断是否IE的Edge浏览器
	var isFF = userAgent.indexOf("Firefox") > -1;	//判断是否Firefox浏览器
	var isChrome = userAgent.indexOf("Chrome") > -1 && userAgent.indexOf("Safari") > -1;	//判断Chrome浏览器

	if (isIE11) {
		var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
		reIE.test(userAgent);
		var fIEVersion = parseFloat(RegExp["$1"]);
		isIE11 = fIEVersion == 11;
	}
	if ( isIE11 || isEdge || isChrome ) {
		return true;
	}
	return false;
}
</script>
</body>
</html>
