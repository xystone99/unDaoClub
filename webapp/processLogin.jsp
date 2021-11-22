<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import="com.undao.control.*" %>
<%@ page import="com.undao.cache.*" %>
<%@ page import="xms.*" %>
<%@ page import="xms.procedures.LoginSystem" %>

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
	
	if ( action.equals( "Login" ) ) {		// *********************** Login ************************//
		String loginName = request.getParameter( LoginSystem.QP_LOGIN_NAME );
		String cloudID = request.getParameter( LoginSystem.QP_CLOUD_ID );

		if ( !XmsInitial.getXmsContainer().getSystemicVariables().isGlobalAcceptLogin() ) {
			info_tag = "系统维护中，请稍后再试！";
			is_back = true;
		} else if ( XmsInitial.getXmsContainer().getSystemicVariables().isAcceptLogin(cloudID) || loginName.equals(CtrlConstants.INNER_ADMIN) ) {
			LoginSystem loginSystem = new LoginSystem( XmsInitial.getDataSource() );
			AbstractDaemon.fixQueryParams( loginSystem, request, false, false, false );
			loginSystem.executeCall();
			String[] y_result = loginSystem.getReturnValues( );
			result = y_result[7];

			if ( result.equals( "LoginSuccess" ) ) {
				Cookie cookie = new Cookie("tUserName", loginName );
	    		response.addCookie(cookie);
	    		cookie.setMaxAge( 3600*24*7 );
	    				
				session.setAttribute( CtrlConstants.SESS_LANDER_ID, y_result[0] );
				session.setAttribute( CtrlConstants.SESS_LANDER_NAME, y_result[1] );
				session.setAttribute( CtrlConstants.SESS_LANDER_ROLE, y_result[2] );
				session.setAttribute( CtrlConstants.SESS_ASTRICT_LEVEL, y_result[3] );
				session.setAttribute( CtrlConstants.SESS_CUR_COMPANY, y_result[4] );
				session.setAttribute( CtrlConstants.SESS_CUR_COMPANY_NAME, XmsInitial.getXmsContainer().getMasterCompany().getDisplay(y_result[4]) );
				session.setAttribute( CtrlConstants.SESS_AVALIABLE_COMPANIES, y_result[5] );
				session.setAttribute( CtrlConstants.SESS_AVALIABLE_OPTIONS, XmsInitial.getXmsContainer().getMasterCompany().getAvaliableSelectOptions(y_result[5]));
				session.setAttribute( CtrlConstants.SESS_LANDER_FLG, y_result[6] );
				session.setAttribute( CtrlConstants.SESS_LOGIN_NAME, loginName );
				session.setAttribute( CtrlConstants.SESS_CLOUD_ID, cloudID );
				OnlineManager.getInstance().createSession(session,request.getRemoteAddr(),y_result[0],y_result[1]);

				if ( AbstractDaemon.isSystemAdministrator(request) ) {
					redirect_url = "./SysConfigure/roleList.jsp";
				} else {
					redirect_url = XmsInitial.getXmsContainer().getMasterRole().getHrefByID( y_result[2] );
				}
			} else {
				out.println( loginSystem.getCallString() );
				info_tag = loginSystem.getResultDisplay( );
				//is_back = true;
			}
		} else {
			info_tag = "当前系统已被管理员设置为禁止登录，请稍后再试！";
			is_back = true;
		}

	} else if ( action.equals( "ExitSystem" ) ) {
		session.invalidate( );
		%>		    	
		<script language="javascript">
			window.parent.location.href = "login.jsp";
		</script><%
		
	}
	
	%><%@ include file="./include/inc_process_action.logic" %><%
%>

