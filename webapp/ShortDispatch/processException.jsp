<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import = "com.cuanfun.utils.*" %>
<%@ page import = "com.cuanfun.database.*" %>
<%@ page import = "com.cuanfun.control.*" %>
<%@ page import = "com.cuanfun.cache.*" %>
<%@ page import = "com.cuanfun.enumeration.*" %>
<%@ page import = "xms.*" %>
<%@ page import = "xms.beans.shortDispatch.*" %>

<%
	int PAGE_TAG = 0;
	boolean acceptInnerUser = true;
	String[] needAstricts = { SysAstricts.QX_SHORT_EXCEPTION  };
%>
<%@ include file="/include/inc_header.logic" %>

<%
	String action = request.getParameter( "Action" );
	
	String result = "#";
	String info_tag = "#";
	String redirect_url = "#";
	boolean is_modal_dialog = false;
	boolean is_refresh_parent = false;
	boolean is_close_window = false;
	boolean is_back = false;	
%>
<%	
	if ( action.equals( "ShortTSDelete" ) ) {					//删除自有车次(提送)
		ShortTS shortTS = new ShortTS( XmsInitial.getDataSource(), ShortTS.BTYPE_DELETE );
		AbstractDaemon.fixQueryParams( shortTS, request );
		shortTS.setParamValue(ShortTS.QP_EMP, AbstractDaemon.getLanderID(request) );
		shortTS.executeCall( );
		info_tag = shortTS.getResultDisplay( );
		if ( shortTS.getResult() == ShortTS.RTN_SUCCESS ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else if ( shortTS.getResult() == ShortTS.RTN_IN_USE || shortTS.getResult() == ShortTS.RTN_INVALID ) { 
			is_back = true;
		} else {
			out.println( shortTS.getCallString() );
		}
		
	} else if ( action.equals( "ShortYkDelete" ) ) {			//删除自有车次(移库)
		ShortYK shortYK = new ShortYK( XmsInitial.getDataSource(), ShortYK.BTYPE_DELETE );
		AbstractDaemon.fixQueryParams( shortYK, request );
		shortYK.setParamValue(ShortTS.QP_EMP, AbstractDaemon.getLanderID(request) );
		shortYK.executeCall( );
		info_tag = shortYK.getResultDisplay( );
		if ( shortYK.getResult() == ShortYK.RTN_SUCCESS ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else if ( shortYK.getResult() == ShortYK.RTN_IN_USE || shortYK.getResult() == ShortYK.RTN_INVALID ) { 
			is_back = true;
		} else {
			out.println( shortYK.getCallString() );
		}
		
	} else if ( action.equals( "ShortOtherDelete" ) ) {			//删除自有车次(其它)	
		ShortOther shortOther = new ShortOther( XmsInitial.getDataSource(), ShortOther.BTYPE_DELETE );
		AbstractDaemon.fixQueryParams( shortOther, request );
		shortOther.setParamValue(ShortTS.QP_EMP, AbstractDaemon.getLanderID(request) );
		shortOther.executeCall( );
		info_tag = shortOther.getResultDisplay( );
		if ( shortOther.getResult() == ShortOther.RTN_SUCCESS ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else if ( shortOther.getResult() == ShortOther.RTN_INVALID ) { 
			is_back = true;
		} else {
			out.println( shortOther.getCallString() );
		}
	
	} else if ( action.equals( "SocialTSDelete" ) ) {			//删除外雇车次(提送)
		SocialTS socialTS = new SocialTS( XmsInitial.getDataSource(), SocialTS.BTYPE_DELETE );
		AbstractDaemon.fixQueryParams( socialTS, request );
		socialTS.setParamValue(SocialTS.QP_EMP, AbstractDaemon.getLanderID(request) );
		socialTS.executeCall( );
		info_tag = socialTS.getResultDisplay( );
		
		if ( socialTS.getResult() == SocialTS.RTN_SUCCESS ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else if ( socialTS.getResult() == SocialTS.RTN_IN_USE || socialTS.getResult() == SocialTS.RTN_INVALID ) { 
			is_back = true;
		} else {
			out.println( socialTS.getCallString() );
		}
	
	} else if ( action.equals( "SocialYkDelete" ) ) {			//删除外雇车次(移库)	
		SocialYK socialYK = new SocialYK( XmsInitial.getDataSource(), SocialYK.BTYPE_DELETE );
		AbstractDaemon.fixQueryParams( socialYK, request );
		socialYK.setParamValue(ShortTS.QP_EMP, AbstractDaemon.getLanderID(request) );
		socialYK.executeCall( );
		info_tag = socialYK.getResultDisplay( );
		if ( socialYK.getResult() == SocialYK.RTN_SUCCESS ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else if ( socialYK.getResult() == SocialYK.RTN_IN_USE || socialYK.getResult() == SocialYK.RTN_INVALID ) { 
			is_back = true;
		} else {
			out.println( socialYK.getCallString() );
		}
	
	} 

	%><%@ include file="/include/inc_process_action.logic" %><%
	
%>