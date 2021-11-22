<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import = "xms.*" %>
<%@ page import = "xms.beans.customerService.*" %>
<%@ page import="com.undao.enumeration.SysAstricts" %>
<%@ page import="com.undao.control.AbstractDaemon" %>

<%
	int PAGE_TAG = 0;
	boolean acceptInnerUser = true;
	String[] needAstricts = {SysAstricts.QX_CUS_UPDATE };
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
	if ( action.equals( "TransPlanNew" ) ) {					//新增运输计划
		TransPlan transPlan = new TransPlan( XmsInitial.getDataSource(), TransPlan.BTYPE_INSERT );
		AbstractDaemon.fixQueryParams( transPlan, request, true, false, true );
		transPlan.executeCall( );
		info_tag = transPlan.getResultDisplay( );
		if ( !transPlan.getResult().equals( TransPlan.R_SQL_EXCEPTION ) ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else {
			out.println( transPlan.getCallString() );
		}

	} else if ( action.equals( "TransPlanUpdate" ) ) {
		TransPlan transPlan = new TransPlan( XmsInitial.getDataSource(), TransPlan.BTYPE_UPDATE );
		AbstractDaemon.fixQueryParams( transPlan, request, true, false, true );
		transPlan.executeCall( );
		info_tag = transPlan.getResultDisplay( );
		if ( !transPlan.getResult().equals( TransPlan.R_SQL_EXCEPTION ) ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else {
			out.println( transPlan.getCallString() );
		}

	} else if ( action.equals( "TransPlanDelete" ) ) {
		TransPlan transPlan = new TransPlan( XmsInitial.getDataSource(), TransPlan.BTYPE_DELETE );
		AbstractDaemon.fixQueryParams( transPlan, request, true, false, true );
		transPlan.executeCall( );
		info_tag = transPlan.getResultDisplay( );
		if ( !transPlan.getResult().equals( TransPlan.R_SQL_EXCEPTION ) ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else {
			out.println( transPlan.getCallString() );
		}

	}

	%><%@ include file="../include/inc_process_action.logic" %><%
	
%>