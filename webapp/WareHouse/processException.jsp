<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import = "com.cuanfun.utils.*" %>
<%@ page import = "com.cuanfun.database.*" %>
<%@ page import = "com.cuanfun.control.*" %>
<%@ page import = "com.cuanfun.cache.*" %>
<%@ page import = "com.cuanfun.enumeration.*" %>
<%@ page import = "xms.*" %>
<%@ page import = "xms.beans.wareHouse.*" %>

<%
	int PAGE_TAG = 0;
	boolean acceptInnerUser = true;
	String[] needAstricts = { SysAstricts.QX_WH_EXCEPTION }; 
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
	if ( action.equals( "InCusDelete" ) ) {				
		InCus inCus = new InCus( XmsInitial.getDataSource(), InCus.BTYPE_DELETE );
		AbstractDaemon.fixQueryParams( inCus, request );
		inCus.setParamValue( InCus.QP_EMP, AbstractDaemon.getLanderID(request) );
		inCus.executeCall( );
		info_tag = inCus.getResultDisplay( );
		if ( inCus.getResult() == InCus.RTN_SUCCESS ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else if ( inCus.getResult() == InCus.RTN_INVALID ) {
			is_back = true;
		} else {
			out.println( inCus.getCallString() );
		}
		
	} else if ( action.equals( "InSelfDelete" ) ) {		
		InSelf inSelf = new InSelf( XmsInitial.getDataSource(), InSelf.BTYPE_DELETE );
		AbstractDaemon.fixQueryParams( inSelf, request );
		inSelf.setParamValue( InSelf.QP_EMP, AbstractDaemon.getLanderID(request) );
		inSelf.executeCall( );
		info_tag = inSelf.getResultDisplay( );
		if ( inSelf.getResult() == InSelf.RTN_SUCCESS ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else if ( inSelf.getResult() == InSelf.RTN_INVALID ) {
			is_back = true;
		} else {
			out.println( inSelf.getCallString() );
		}
		
	} else if ( action.equals( "InSocialDelete" ) ) {			
		InSocial inSocial = new InSocial( XmsInitial.getDataSource(), InSocial.BTYPE_DELETE );
		AbstractDaemon.fixQueryParams( inSocial, request );
		inSocial.setParamValue( InSocial.QP_EMP, AbstractDaemon.getLanderID(request) );
		inSocial.executeCall( );
		info_tag = inSocial.getResultDisplay( );
		if ( inSocial.getResult() == InSocial.RTN_SUCCESS ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else if ( inSocial.getResult() == InSocial.RTN_INVALID ) {
			is_back = true;
		} else {
			out.println( inSocial.getCallString() );
		}
	
	} else if ( action.equals( "InGxDelete" ) ) {			
		InGx inGx = new InGx( XmsInitial.getDataSource(), InGx.BTYPE_DELETE );
		AbstractDaemon.fixQueryParams( inGx, request );
		inGx.setParamValue( InGx.QP_EMP, AbstractDaemon.getLanderID(request) );
		inGx.executeCall( );
		info_tag = inGx.getResultDisplay( );
		if ( inGx.getResult() == InGx.RTN_SUCCESS ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else if ( inGx.getResult() == InGx.RTN_INVALID ) {
			is_back = true;
		} else {
			out.println( inGx.getCallString() );
		}
	
	} else if ( action.equals( "OutCusDelete" ) ) {			
		OutCus outCus = new OutCus( XmsInitial.getDataSource(), OutCus.BTYPE_DELETE );
		AbstractDaemon.fixQueryParams( outCus, request );
		outCus.setParamValue( OutCus.QP_EMP, AbstractDaemon.getLanderID(request) );
		outCus.executeCall( );
		info_tag = outCus.getResultDisplay( );
		if ( outCus.getResult() == OutCus.RTN_SUCCESS ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else {
			out.println( outCus.getCallString() );
		}
	
	} else if ( action.equals( "OutSelfDelete" ) ) {			
		OutSelf outSelf = new OutSelf( XmsInitial.getDataSource(), OutSelf.BTYPE_DELETE );
		AbstractDaemon.fixQueryParams( outSelf, request );
		outSelf.setParamValue( OutSelf.QP_EMP, AbstractDaemon.getLanderID(request) );
		outSelf.executeCall( );
		info_tag = outSelf.getResultDisplay( );
		if ( outSelf.getResult() == OutSelf.RTN_SUCCESS ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else if ( outSelf.getResult() == OutSelf.RTN_INVALID ) {
			is_back = true;
		} else {
			out.println( outSelf.getCallString() );
		}
	
	} else if ( action.equals( "OutSocialDelete" ) ) {			
		OutSocial outSocial = new OutSocial( XmsInitial.getDataSource(), OutSocial.BTYPE_DELETE );
		AbstractDaemon.fixQueryParams( outSocial, request );
		outSocial.setParamValue( OutSocial.QP_EMP, AbstractDaemon.getLanderID(request) );
		outSocial.executeCall( );
		info_tag = outSocial.getResultDisplay( );
		if ( outSocial.getResult() == OutSocial.RTN_SUCCESS ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else if ( outSocial.getResult() == OutSocial.RTN_INVALID ) {
			is_back = true;
		} else {
			out.println( outSocial.getCallString() );
		}
	
	} else if ( action.equals( "OutGxDelete" ) ) {			
		OutGx outGx = new OutGx( XmsInitial.getDataSource(), OutGx.BTYPE_DELETE );
		AbstractDaemon.fixQueryParams( outGx, request );
		outGx.setParamValue( OutGx.QP_EMP, AbstractDaemon.getLanderID(request) );
		outGx.executeCall( );
		info_tag = outGx.getResultDisplay( );
		if ( outGx.getResult() == OutGx.RTN_SUCCESS ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else if ( outGx.getResult() == OutGx.RTN_INVALID ) {
			is_back = true;
		} else {
			out.println( outGx.getCallString() );
		}
	
	}

	%><%@ include file="/include/inc_process_action.logic" %><%
	
%>