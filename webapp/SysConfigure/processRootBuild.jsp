<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import="xms.*" %>
<%@ page import="com.undao.control.*" %>
<%@ page import="xms.beans.sysConfigure.Role" %>
<%@ page import="com.undao.enumeration.SysAstricts" %>
<%@ page import="com.undao.database.DatabaseConstants" %>


<%
	int PAGE_TAG = 0;
	boolean acceptInnerUser = true;
	String[] needAstricts = { SysAstricts.QX_ASTRICT_CONFIGURE };
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
	if ( action.equals( "RoleNew" ) ) {						//角色管理
		Role role = new Role( XmsInitial.getDataSource(), Role.BTYPE_INSERT );
		AbstractDaemon.fixQueryParams( role, request, false, true );
		role.executeCall( );
		info_tag = role.getResultDisplay();
		if ( role.getResult().equals(Role.R_NEW_SUCCESS) ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else {
			out.println( role.getCallString() );
		}
		
	} else if ( action.equals( "RoleUpdate" ) ) {
		Role role = new Role( XmsInitial.getDataSource(), Role.BTYPE_UPDATE );
		AbstractDaemon.fixQueryParams( role, request, false, true );
		role.executeCall( );
		info_tag = role.getResultDisplay( );
		if ( role.getResult().equals(Role.R_UPDATE_SUCCESS) ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else {
			out.println( role.getCallString() );
		}
		
	} else if ( action.equals( "RoleDelete" ) ) {		
		Role role = new Role( XmsInitial.getDataSource(), Role.BTYPE_DELETE );
		AbstractDaemon.fixQueryParams( role, request, false, true );
		role.executeCall( );
		info_tag = role.getResultDisplay( );
		if ( role.getResult().equals(Role.R_DELETE_SUCCESS) ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else {
			out.println( role.getCallString() );
		}

	}

	%><%@ include file="../include/inc_process_action.logic" %><%
	
%>