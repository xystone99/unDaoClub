<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import = "xms.*" %>
<%@ page import = "xms.procedures.*" %>
<%@ page import="xms.beans.shortDispatch.Dispatch" %>
<%@ page import="com.undao.control.AbstractDaemon" %>
<%
	int PAGE_TAG = 0;
	boolean acceptInnerUser = true;
	String[] needAstricts = null;
%>
<%@ include file="../include/inc_header.logic" %>

<%
	String action = request.getParameter( "Action" );
	String host_name = request.getRemoteHost( );
	String host_address = request.getRemoteAddr( );

%>

<%
	String result = "#";
	String info_tag = "#";
	String redirect_url = "#";
	boolean is_refresh_parent = false;
	boolean is_close_window = false;
	boolean is_back = false;
    boolean is_back_with_refresh = false;
	
	if ( action.equals( "PasswordUpdate" ) ) {
        UpdatePassword updatePassword = new UpdatePassword( XmsInitial.getDataSource() );
        AbstractDaemon.fixQueryParams( updatePassword, request, true, false, true );
        updatePassword.executeCall( );
        info_tag = updatePassword.getResultDisplay( );
        if ( !updatePassword.getResult().equals( Dispatch.R_SQL_EXCEPTION ) ) {
            is_close_window = true;
			out.println( updatePassword.getCallString() );
        } else {
            out.println( updatePassword.getCallString() );
        }

	}
	
	%><%@ include file="../include/inc_process_action.logic" %><%
	
%>