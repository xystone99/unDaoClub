<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import = "xms.*" %>
<%@ page import="com.undao.enumeration.SysAstricts" %>
<%@ page import="com.undao.utils.DateUtils" %>
<%@ page import="com.undao.cache.*" %>

<%
	int PAGE_TAG = 0;
	boolean acceptInnerUser = true;
	String[] needAstricts = { SysAstricts.QX_ACCESS_REFUSED };
	String ACCESS_URL = "http://localhost:3721/Tools/refreshCache.jsp?ActionTag=RefreshCache&Target=";
%>
<%@ include file="../include/inc_header.logic" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>重新加载缓存数据</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>

<body>
<% 
	String actionTag = request.getParameter( "ActionTag" );
	String target = request.getParameter( "Target" );
	if ( (actionTag == null) || (!actionTag.equalsIgnoreCase("RefreshCache")) ) {
		out.println( "禁止操作，面墙自省......" );
		return;
	} 
%>

<form name="myForm">
<br/>服务器地址：<br/><input type="text" name="ServerIP" size="50" maxlength="20" /><br/><br/>
<% 
	String WEATHER_LINE = "WeatherLine";
	String MASTER_PART = "MasterPart";
	String MASTER_POST = "MasterPost";
	String FLUSH_PRIVILEGES = "FlushPrivileges";
	String RELOAD_EMPLOYEE = "ReloadEmployee";
	String RELOAD_DRIVER = "ReloadDriver";
	String RELOAD_LOADER = "ReloadLoader";
	String RELOAD_OILER = "ReloadOiler";
	String RELOAD_CHARGER = "ReloadCharger";
	String RELOAD_TRUCK = "ReloadTruck";
	String RELOAD_CUS = "ReloadCus";
	String RELOAD_FEE_ENGINE = "ReloadFeeEngine";
	String RELOAD_ADDRESS = "ReloadAddress";
	String RELOAD_PROVIDER = "ReloadProvider";
	String RELOAD_PAY_OBJ = "ReloadPayObj";
	
	String[] arrTarget = { 
		WEATHER_LINE, MASTER_PART, MASTER_POST, FLUSH_PRIVILEGES, 
		RELOAD_EMPLOYEE, RELOAD_DRIVER, RELOAD_LOADER, RELOAD_OILER, RELOAD_CHARGER, RELOAD_TRUCK,
		RELOAD_CUS, RELOAD_FEE_ENGINE, RELOAD_ADDRESS, RELOAD_PROVIDER, RELOAD_PAY_OBJ
	};
	String[] arrTitle = {
		"线路名称(天气预报)", "部门基础资料(同步至数据库)", "职位基础资料(同步至数据库)", "刷新权限缓存 ", 
		"刷新员工缓存 ", "刷新司机缓存 ", "刷新装卸工缓存 ", "刷新加油员缓存", "刷新收(付)款负责人缓存", "刷新车辆缓存", 
		"刷新客户(项目)缓存", "刷新费用引擎", "刷新收发货地址", "刷新非业务供应商", "刷新收付对象"
	};
	
	for ( int j=0; j<arrTarget.length; j++ ) {
		%>
		<a href="refreshCache.jsp?ActionTag=RefreshCache&Target=<%=arrTarget[j]%>" onclick="javascript:return confirm('确认刷新吗？');"><%=arrTitle[j]%></a><br/>
		<%
	}
%>
</form><br/>

<% 	
	
	StringBuilder bufResult = new StringBuilder( );
	out.println( DateUtils.formatCurrentDateTime() + " Started......<br/>");
	
	if ( target.equals( WEATHER_LINE ) ) {					//线路名称(天气预报)
		
	} else if ( target.equals( MASTER_PART ) ) {			//部门基础资料(同步至数据库)
	    XmsInitial.getXmsContainer().getMasterPart().fixSingletonObject();
		bufResult.append( "MasterPart Synchronize Finished.<br/>" );
	
	} else if ( target.equals( MASTER_POST ) ) {			//职位基础资料(同步至数据库)
	    XmsInitial.getXmsContainer().getMasterPost().fixSingletonObject();
		bufResult.append( "MasterPost Synchronize Finished.<br/>" );
	
	} else if ( target.equals( FLUSH_PRIVILEGES ) ) {		//刷新权限缓存
        XmsInitial.getXmsContainer().getMasterRole().fixSingletonObject();
		XmsInitial.getXmsContainer().getRoleAstricts().fixSingletonObject();
		bufResult.append( "权限信息已刷新！<br/>" );
	
	} else if ( target.equals( RELOAD_EMPLOYEE ) ) {		//刷新员工缓存
	    EmployeeGeometry.getInstance().fixSingletonObject( );
		bufResult.append( "员工信息已刷新！<br/>" );	
	
	} else if ( target.equals( RELOAD_DRIVER ) ) {			//刷新司机缓存
	    DriverGeometry.getInstance().fixSingletonObject( );
		bufResult.append( "司机信息已刷新！<br/>" );	
	
	} else if ( target.equals( RELOAD_LOADER ) ) {			//刷新装卸工缓存
		bufResult.append( "装卸工信息未加入缓存！<br/>" );
		
	} else if ( target.equals( RELOAD_OILER ) ) {			//刷新加油员缓存
		bufResult.append( "加油员信息未加入缓存！<br/>" );
	
	} else if ( target.equals( RELOAD_CHARGER ) ) {			//刷新收(付)款负责人缓存
		bufResult.append( "收付款负责人信息未加入缓存！<br/>" );
	
	} else if ( target.equals( RELOAD_TRUCK ) ) {			//刷新车辆缓存
		TruckGeometry.getInstance().fixSingletonObject( );		
		bufResult.append( "车辆信息已刷新！<br/>" );
	
	} else if ( target.equals( RELOAD_CUS ) ) {				//刷新客户缓存
		CustomerGeometry.getInstance().fixSingletonObject( );
		bufResult.append( "客户信息已刷新！<br/>" );
		
	} else if ( target.equals( RELOAD_FEE_ENGINE ) ) {		//刷新费用模板缓存

		bufResult.append( "费用模板未加入缓存！<br/>" );
		
	} else if ( target.equals( RELOAD_ADDRESS ) ) {			//刷新收发货地址缓存

		bufResult.append( "收发货地址信息尚未加入缓存！<br/>" );
		
	} else if ( target.equals( RELOAD_PROVIDER ) ) {		//刷新非业务供应商缓存

		bufResult.append( "非业务供应商信息尚未加入缓存！<br/>" );
	
	} else if ( target.equals( RELOAD_PAY_OBJ ) ) {			//刷新收付对象

		bufResult.append( "收付对象信息尚未加入缓存！<br/>" );
			
	}
	
	out.println( bufResult.toString() );
	out.println( DateUtils.formatCurrentDateTime() + " Finished......<br/>");
%>
</body>
</html>
