<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import = "xms.*" %>
<%@ page import="com.undao.enumeration.SysAstricts" %>
<%@ page import="com.undao.cache.OnlineManager" %>

<%
	int PAGE_TAG = 0;
	boolean acceptInnerUser = true;
	String[] needAstricts = { SysAstricts.QX_SYS_CONFIGURE };
%>
<%@ include file="../include/inc_header.logic" %>

<%
	String action = request.getParameter( "Action" );
	
	String result = "#";
	String info_tag = "#";
	String redirect_url = "#";
	boolean is_refresh_parent = false;
	boolean is_close_window = false;
	boolean is_back = false;	
%>
<%	
	if ( action.equals( "CutSession" ) ) {					//断开特定链接
		OnlineManager.getInstance().cutSession( request.getParameter( "SessionID" ) );
		info_tag = "链接已断开!";
		is_refresh_parent = true;
		is_close_window = true;
		
	} else if ( action.equals( "CutAllSession" ) ) {		//断开所有链接
		OnlineManager.getInstance().cutAllSession( );
		info_tag = "已断开所有链接!";
		is_refresh_parent = true;
		is_close_window = true;
	}

	%><%@ include file="../include/inc_process_action.logic" %><%
	
%>