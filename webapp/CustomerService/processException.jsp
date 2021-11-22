<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import = "xms.*" %>
<%@ page import = "xms.beans.customerService.*" %>
<%@ page import="com.undao.enumeration.SysAstricts" %>

<%
	int PAGE_TAG = 0;
	boolean acceptInnerUser = true;
	String[] needAstricts = { SysAstricts.QX_CUS_EXCEPTION  };
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
	if ( action.equals( "FeeYWDelete" ) ) {

		
	}
	
	%><%@ include file="../include/inc_process_action.logic" %><%
	
%>