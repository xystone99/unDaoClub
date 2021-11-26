<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import="com.undao.control.CtrlConstants" %>
<%@ page import="com.undao.control.AbstractDaemon" %>
<%
    int PAGE_TAG = CtrlConstants.PG_MY_DOMAIN;
    boolean acceptInnerUser = true;
    String[] needAstricts = null;
%>
<%@ include file="../include/inc_header.logic" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>我的主页</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />    
<link rel="stylesheet" type="text/css" href="../css/default.css" />
</head>

<body>
<%@ include file="../include/menu_1_Header.block" %>

<div id="container">
<%@ include file="../include/menu_2_Self.block" %>

<br/><br/><br/><br/><br/>
您好。系统正在建设中，欢迎提出宝贵意见！！！

</div><!-- container -->
<%@ include file="../include/menu_3_Footer.block" %>


</body>
</html>