<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import = "com.cuanfun.utils.*" %>
<%@ page import = "com.cuanfun.database.*" %>
<%@ page import = "com.cuanfun.control.*" %>
<%@ page import = "com.cuanfun.cache.*" %>
<%@ page import = "com.cuanfun.enumeration.*" %>
<%@ page import = "xms.*" %>
<%@ page import = "xms.beans.customerService.*" %>

<%
	int PAGE_TAG = 0;
	boolean acceptInnerUser = true;
	String[] needAstricts = { SysAstricts.QX_CS_UPDATE  }; 
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
	if ( action.equals( "FeeYWDelete" ) ) {
		FeeYW feeYW = new FeeYW( XmsInitial.getDataSource(), FeeYW.BTYPE_DELETE);
		AbstractDaemon.fixQueryParams( feeYW, request );
		feeYW.setParamValue(FeeYW.QP_EMP, AbstractDaemon.getLanderID(request) );
		feeYW.executeCall( );
		info_tag = feeYW.getResultDisplay( );
		if ( feeYW.getResult() == FeeYW.RTN_SUCCESS ) {
			is_refresh_parent = true;
			is_close_window = true;
		} else if( feeYW.getResult() == FeeYW.RTN_EXISTS ) {
			is_back = true;
		} else {
			out.println( feeYW.getCallString() );
		}
	
	} else if ( action.equals( "PackageDelete" ) ) {		
		ReceiptPackage packg = new ReceiptPackage( XmsInitial.getDataSource(), ReceiptPackage.BTYPE_DELETE );
		AbstractDaemon.fixQueryParams( packg, request );
		packg.setParamValue(ReceiptPackage.QP_EMP, AbstractDaemon.getLanderID(request) );
		packg.executeCall( );
		info_tag = packg.getResultDisplay( );
		if ( packg.getResult() == ReceiptPackage.RTN_SUCCESS ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else {
			out.println( packg.getCallString() );
		}
	
	} else if ( action.equals( "WayBillDelete" ) ) {		
		TransPlan wayBill = new TransPlan( XmsInitial.getDataSource(), TransPlan.BTYPE_DELETE );
		AbstractDaemon.fixQueryParams( wayBill, request );
		wayBill.executeCall( );
		info_tag = wayBill.getResultDisplay( );
		if ( wayBill.getResult() == TransPlan.RTN_SUCCESS ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else {
			out.println( wayBill.getCallString() );
		}
		
	}
	
	%><%@ include file="/include/inc_process_action.logic" %><%
	
%>