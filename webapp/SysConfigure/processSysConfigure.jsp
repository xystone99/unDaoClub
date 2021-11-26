<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import = "xms.*" %>
<%@ page import = "xms.beans.*" %>
<%@ page import="com.undao.enumeration.SysAstricts" %>
<%@ page import="com.undao.utils.CommonUtils" %>
<%@ page import="com.undao.database.DBUtils" %>
<%@ page import="com.undao.cache.OnlineManager" %>
<%@ page import="xms.beans.sysConfigure.UserAccount" %>
<%@ page import="com.undao.control.AbstractDaemon" %>

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
	boolean is_back_with_refresh = false;
%>
<%	
	if ( action.equals( "ResetPsw" ) ) {							//重置密码
		String psw = CommonUtils.createPassword( );
		StringBuilder bufSQL = new StringBuilder();
		bufSQL.append( "UPDATE tbl_user_account SET login_pwd=MD5('" ).append( psw ).append( "') WHERE user_a=" ).append( request.getParameter( "ID" ) );
		
		DBUtils.executeUpdate( XmsInitial.getDataSource(), bufSQL.toString() );
		info_tag = "密码只显示一次：" + psw + "，请尽快修改登录名和登录密码！";
		is_close_window = true;
	
	} else if ( action.equals( "CutSession" ) ) {					//断开特定链接
		OnlineManager.getInstance().cutSession( request.getParameter( "SessionID" ) );
		info_tag = "链接已断开!";
		is_refresh_parent = true;
		is_close_window = true;
		
	} else if ( action.equals( "CutAllSession" ) ) {				//断开所有链接
		OnlineManager.getInstance().cutAllSession( );
		info_tag = "已断开所有链接!";
		is_refresh_parent = true;
		is_close_window = true;
	
	} else if ( action.equals( "SecurityUpdate" ) ) {				//更新员工的权限信息

	} else if ( action.equals( "DeleteUserAccount" ) ) {			//彻底删除员工信息
		UserAccount uAccount = new UserAccount( XmsInitial.getDataSource(), UserAccount.BTYPE_DELETE );
		AbstractDaemon.fixQueryParams( uAccount, request, false, false, true );
		uAccount.executeCall( );
		info_tag = uAccount.getResultDisplay( );
		if ( !uAccount.getResult().equals( UserAccount.R_SQL_EXCEPTION ) ) {
			is_close_window = true;
			is_refresh_parent = true;
		} else {
			out.println( uAccount.getCallString() );
		}

	}

	%><%@ include file="../include/inc_process_action.logic" %><%
	
%>