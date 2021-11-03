<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import = "xms.*" %>
<%@ page import = "xms.beans.customerService.*" %>
<%@ page import="com.undao.enumeration.SysAstricts" %>
<%@ page import="com.undao.control.AbstractDaemon" %>
<%@ page import="xms.beans.shortDispatch.Dispatch" %>
<%@ page import="xms.queries.shortDispatch.WaitDispatchList" %>
<%@ page import="xms.procedures.WareReturnDispatch" %>

<%
	int PAGE_TAG = 0;
	boolean acceptInnerUser = true;
	String[] needAstricts = {SysAstricts.QX_WH_UPDATE };
%>
<%@ include file="../include/inc_header.logic" %>

<%
	String action = request.getParameter( "Action" );
	
	String result = "#";
	String info_tag = "#";
	String redirect_url = "#";
	boolean is_modal_dialog = false;
	boolean is_refresh_parent = false;
	boolean is_close_window = false;
	boolean is_back = false;
	boolean is_back_with_refresh = false;
%>
<%	
	if ( action.equals( "ReturnDispatch" ) ) {					//仓库回报
		WareReturnDispatch returnDispatch = new WareReturnDispatch( XmsInitial.getDataSource() );
		AbstractDaemon.fixQueryParams( returnDispatch, request, true, true );
		returnDispatch.executeCall( );
		info_tag = returnDispatch.getResultDisplay( );
		if ( !returnDispatch.getResult().equals( WareReturnDispatch.R_SQL_EXCEPTION ) ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else {
			out.println( returnDispatch.getCallString() );
		}

	}

	%><%@ include file="../include/inc_process_action.logic" %><%
	
%>