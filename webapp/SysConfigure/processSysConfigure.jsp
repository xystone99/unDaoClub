<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ page import = "com.cuanfun.utils.*" %>
<%@ page import = "com.cuanfun.database.*" %>
<%@ page import = "com.cuanfun.control.*" %>
<%@ page import = "com.cuanfun.cache.*" %>
<%@ page import = "com.cuanfun.enumeration.*" %>
<%@ page import = "xms.*" %>
<%@ page import = "xms.beans.*" %>
<%@ page import = "xms.beans.hr.*" %>

<%
	int PAGE_TAG = 0;
	boolean acceptInnerUser = true;
	String[] needAstricts = { SysAstricts.QX_SYS_CONFIGURE }; 
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
	if ( action.equals( "ResetPsw" ) ) {							//重置密码
		String psw = CommonUtils.customPassword( );
		StringBuilder bufSQL = new StringBuilder();
		bufSQL.append( "UPDATE tbl_emp SET login_name=job_id, login_pwd=MD5('" ).append( psw ).append( "') WHERE emp=" ).append( request.getParameter( "ID" ) );
		
		DBUtils.executeUpdate( XmsInitial.getDataSource(), bufSQL.toString() );
		info_tag = "密码只显示一次：" + psw + "，请尽快修改登录名和登录密码，以免遗忘！";
		is_close_window = true;
		is_modal_dialog = true;
		is_refresh_parent = false;
	
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
		Employee emp = new Employee( XmsInitial.getDataSource(), Employee.BTYPE_UPDATE_SECURITY );
		AbstractDaemon.fixQueryParams( emp, request );
		emp.executeCall( );
		info_tag = emp.getResultDisplay();
		if ( emp.getResult() == Employee.RTN_SUCCESS ) {
			String empID = request.getParameter( Employee.QP_EMP );
			String mCompanies = request.getParameter( Employee.QP_M_COMPANIES );
			String bCompanies = request.getParameter( Employee.QP_B_COMPANIES );
			EmployeeGeometry.getInstance().setManageCompanies( empID, mCompanies );
			EmployeeGeometry.getInstance().setBusinessCompanies( empID, bCompanies );
			RoleAstricts.getInstance().fixSingletonObject( );
			is_close_window = true;
			is_refresh_parent = true;
		} else {
			info_tag = "操作失败，请检查输入!";
			out.println( emp.getCallString() );
		}
	
	} else if ( action.equals( "EmployeeDelete" ) ) {				//彻底删除员工信息
		Employee emp = new Employee( XmsInitial.getDataSource(), Employee.BTYPE_DELETE );
		AbstractDaemon.fixQueryParams( emp, request );
		emp.executeCall( );
		info_tag = emp.getResultDisplay();
		if ( emp.getResult() == Employee.RTN_SUCCESS ) {
			EvaluatedEmployee.getInstance().fixSingletonObject( );
			EmployeeGeometry.getInstance().fixSingletonObject( );
			is_close_window = true;
			is_refresh_parent = true;
		} else {
			info_tag = "操作失败，请检查输入!";
			out.println( emp.getCallString() );
		}
	
	} else if ( action.equals( "EvaluationNew" ) ) {				//考核指标
		Evaluation eval = new Evaluation( XmsInitial.getDataSource(), Evaluation.BTYPE_INSERT );
		AbstractDaemon.fixQueryParams( eval, request );
		eval.executeCall( );
		if ( eval.getResult() == Evaluation.RTN_SUCCESS ) {
			info_tag = "操作已成功!";
			is_close_window = true;
			is_refresh_parent = true;
			EvaluationGeometry.getInstance().fixSingletonObject( );
		} else {
			info_tag = "操作失败，请检查输入!";
			out.println( eval.getCallString() );
		}
		
	} else if ( action.equals( "EvaluationUpdate" ) ) {
		Evaluation eval = new Evaluation( XmsInitial.getDataSource(), Evaluation.BTYPE_UPDATE );
		AbstractDaemon.fixQueryParams( eval, request );
		eval.executeCall( );
		if ( eval.getResult() == Evaluation.RTN_SUCCESS ) {
			info_tag = "操作已成功!";
			is_close_window = true;
			is_refresh_parent = true;
			EvaluationGeometry.getInstance().fixSingletonObject( );
			
			System.out.println( eval.getCallString() );
		} else {
			info_tag = "操作失败，请检查输入!";
			out.println( eval.getCallString() );
		}
		
	} else if ( action.equals( "EvaluationDelete" ) ) {		
		Evaluation eval = new Evaluation( XmsInitial.getDataSource(), Evaluation.BTYPE_DELETE );
		AbstractDaemon.fixQueryParams( eval, request );
		eval.executeCall( );
		if ( eval.getResult() == Evaluation.RTN_SUCCESS ) {
			info_tag = "操作已成功!";
			is_close_window = true;
			is_refresh_parent = true;
			EvaluationGeometry.getInstance().fixSingletonObject( );
		} else {
			info_tag = "操作失败，请检查输入!";
			out.println( eval.getCallString() );
		}
		
	}

	%><%@ include file="/include/inc_process_action.logic" %><%
	
%>