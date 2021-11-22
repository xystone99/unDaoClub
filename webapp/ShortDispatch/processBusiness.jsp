<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import = "xms.*" %>
<%@ page import = "xms.beans.customerService.*" %>
<%@ page import="com.undao.enumeration.SysAstricts" %>
<%@ page import="com.undao.control.AbstractDaemon" %>
<%@ page import="xms.beans.shortDispatch.Dispatch" %>
<%@ page import="xms.queries.shortDispatch.WaitDispatchList" %>

<%
	int PAGE_TAG = 0;
	boolean acceptInnerUser = true;
	String[] needAstricts = {SysAstricts.QX_DISPATCH_UPDATE };
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
	boolean is_back_with_refresh = false;
%>
<%	
	if ( action.equals( "DispatchNew" ) ) {					//新增车次
		Dispatch dipatch = new Dispatch( XmsInitial.getDataSource(), Dispatch.BTYPE_INSERT );
		AbstractDaemon.fixQueryParams( dipatch, request, true, false, true );
		dipatch.executeCall( );
		info_tag = dipatch.getResultDisplay( );
		if ( !dipatch.getResult().equals( Dispatch.R_SQL_EXCEPTION ) ) {
			is_back_with_refresh = true;
		} else {
			out.println( dipatch.getCallString() );
		}

	} else if ( action.equals( "DispatchDelete" ) ) {
		Dispatch dipatch = new Dispatch( XmsInitial.getDataSource(), Dispatch.BTYPE_DELETE );
		AbstractDaemon.fixQueryParams( dipatch, request, true, false, true );
		dipatch.executeCall( );
		info_tag = dipatch.getResultDisplay( );
		if ( !dipatch.getResult().equals( Dispatch.R_SQL_EXCEPTION ) ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else {
			out.println( dipatch.getCallString() );
		}

	}

	%><%@ include file="../include/inc_process_action.logic" %><%
	
%>