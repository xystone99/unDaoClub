/**
 * Created At 2014-2-23 上午11:32:18
 */
package com.undao.control;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

import com.undao.database.AbstractBean;
import com.undao.database.AbstractProcedure;
import com.undao.database.AbstractQuery;

/**
 * @author Administrator
 *
 */
public abstract class AbstractDaemon extends HttpServlet implements CtrlConstants {

	protected boolean DEBUG = false;
	
	/**
	 * 设置DEBUG属性
	 */
	public void setDEBUG( boolean debug ) {
		this.DEBUG = debug;
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		process( req, resp );
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		process( req, resp );
	}

	@Override
	public void destroy() {
		super.destroy();
	}
	
	/**
	 * Servlet的处理逻辑，子类主要使用该方法
	 */
	protected abstract void process( HttpServletRequest request, HttpServletResponse response );
	
	/**
	 * 判断是否为内置帐户
	 */
	public static boolean isInnerUser( HttpServletRequest request ) {
		return request.getSession().getAttribute( SESS_LANDER_FLG ).equals( "Inner" );
	}
	
	/**
	 * 判断是否为根管理员
	 */
	public static boolean isSystemAdministrator( HttpServletRequest request ) {
		return request.getSession().getAttribute( SESS_LOGIN_NAME ).equals( INNER_ADMIN );
	}
	
	/**
	 * 从SESSION中取值
	 */
	public static String getCloudID( HttpServletRequest request ) {
		return (String)request.getSession().getAttribute( SESS_CLOUD_ID );
	}

	public static String getLanderID( HttpServletRequest request ) {				//登录者的ID
		return (String)request.getSession().getAttribute( SESS_LANDER_ID );
	}
	
	public static String getLanderName( HttpServletRequest request ) {				//登录者的姓名
		return (String)request.getSession().getAttribute( SESS_LANDER_NAME );
	}
	
	public static String getLanderRole( HttpServletRequest request ) {				//登录者的角色
		return (String)request.getSession().getAttribute( SESS_LANDER_ROLE );
	}
	
	public static String getLoginName( HttpServletRequest request ) {				//当前用户的登录名
		return (String)request.getSession().getAttribute( SESS_LOGIN_NAME );
	}
	
	public static String getAvailableCompanies( HttpServletRequest request ) {		//当前用户可访问范围
		return (String)request.getSession().getAttribute( SESS_AVALIABLE_COMPANIES );
	}

	public static String getAvailableCompanyOptions( HttpServletRequest request ) {	//当前用户可访问范围(OPTIONS列表)
		return (String)request.getSession().getAttribute( SESS_AVALIABLE_OPTIONS );
	}
	
	public static String getCurrentCompany( HttpServletRequest request ) {			//当前用户所在公司
		return (String)request.getSession().getAttribute( SESS_CUR_COMPANY );
	}

	public static String getCurrentCompanyName( HttpServletRequest request ) {			//当前用户所在公司名称
		return (String)request.getSession().getAttribute( SESS_CUR_COMPANY_NAME );
	}

	public static boolean isHighestAstrictLevel( HttpServletRequest request ) {		//权限级别-最高的
		return request.getSession().getAttribute( SESS_ASTRICT_LEVEL ).equals( ASTRICT_LEVEL_HIGHEST );
	}
	
	public static boolean isMiddleAstrictLevel( HttpServletRequest request ) {		//权限级别-一般的
		return request.getSession().getAttribute( SESS_ASTRICT_LEVEL ).equals( ASTRICT_LEVEL_MIDDLE );
	}

	public static boolean isLowestAstrictLevel( HttpServletRequest request ) {		//权限级别-一般的
		return request.getSession().getAttribute( SESS_ASTRICT_LEVEL ).equals( ASTRICT_LEVEL_LOWEST );
	}

	public static boolean canVisit( HttpServletRequest request, String moduleTag ) {	//是否允许访问某个系统模块
		return request.getSession().getAttribute( moduleTag ).equals( CTRL_CONST_Y );
	}

	/**
	 * 输出一条监控日志
	 */
	public static String outputActionLog( HttpServletRequest request, String dateTime, String remark ) {
		StringBuilder bufLog = new StringBuilder( );
		bufLog.append( "Warning：" ).append( dateTime ).append( " (" );
		bufLog.append( getLanderID(request) ).append( ", " ).append( getLoginName(request) ).append( ", " ).append( getLanderName(request) ).append( ") " );
		bufLog.append( remark );
		return bufLog.toString( );
	}
	
	/**
	 * 接收请求中的参数值，并保存到相关对象
	 */
	public static void fixQueryParams( AbstractBean bean, HttpServletRequest request, boolean supplyUserID, boolean supplyCurCompanyID, boolean supplyCloudID ) {
		String[] arr_params = bean.getParamSerial();
		for ( int j=0; j<arr_params.length; j++ ) {
			bean.setParameterValue( arr_params[j], request.getParameter( arr_params[j]) );
		}
		if ( supplyUserID ) {
			bean.setParameterValue( SESS_LANDER_ID, getLanderID(request) );
		}
		if ( supplyCurCompanyID ) {
			bean.setParameterValue( SESS_CUR_COMPANY, getCurrentCompany(request) );
		}
		if ( supplyCloudID ) {
			bean.setParameterValue( SESS_CLOUD_ID, getCloudID(request) );
		}
	}

	public static void fixQueryParams(AbstractProcedure proc, HttpServletRequest request, boolean supplyUserID, boolean supplyCurCompanyID, boolean supplyCloudID ) {
		String[] arr_params = proc.getParamSerial();
		for ( int j=0; j<arr_params.length; j++ ) {
			proc.setParameterValue( arr_params[j], request.getParameter( arr_params[j]) );
		}
		if ( supplyUserID ) {
			proc.setParameterValue( SESS_LANDER_ID, getLanderID(request) );
		}
		if ( supplyCurCompanyID ) {
			proc.setParameterValue( SESS_CUR_COMPANY, getCurrentCompany(request) );
		}
		if ( supplyCloudID ) {
			proc.setParameterValue( SESS_CLOUD_ID, getCloudID(request) );
		}
	}
	
	public static void fixQueryParams( AbstractQuery list, HttpServletRequest request ) {
		String[] arr_params = list.getParamSerial();
		for ( int j=0; j<arr_params.length; j++ ) {
			list.setParameterValue( arr_params[j], request.getParameter( arr_params[j]) );
		}
		String cur_page = request.getParameter( QP_CUR_PAGE );
		if ( cur_page != null ) {
			list.setCurrentPage( Integer.parseInt( cur_page ) );
		}
	}
	
	/**
	 * 输出和读取Cookie
	 */
	public static void setCookie( HttpServletResponse response, String ckName, String ckValue, int maxAge ) {
		Cookie cookie = new Cookie(ckName, ckValue);
		cookie.setMaxAge( maxAge );
		response.addCookie( cookie );
	}
	
	public static String getCookieValue( HttpServletRequest request, String ckName ) {
		Cookie cookies[] = request.getCookies( );
		if ( cookies == null ) return null;
		
		Cookie sCookie = null;
		for (int j=0; j<cookies.length; j++ ) {
			sCookie = cookies[j];
			if ( sCookie.getName().equals( ckName ) ) {
				return sCookie.getValue();
			}
		}
		return null;
	}

	/**
	 * 保存SESSION值
	 */
	protected void setSessionValue( HttpServletRequest request, String sesName, Object sesValue ) {
		request.getSession().setAttribute( sesName, sesValue );
	}
	
	/**
	 *  从SESSION中取值
	 */
	protected Object getSessionValue( HttpServletRequest request, String sesName ) {
		return request.getSession().getAttribute( sesName );
	}
	
	/**
	 * 清除SESSION值
	 */
	protected void removeSessionValue( HttpServletRequest request, String sesName ) {
		request.getSession().removeAttribute( sesName );
	}

	/**
	 * 检查SESSION有效性
	 */
	protected boolean checkSessionExpired( HttpServletRequest request ) {
		return getLanderID(request) == null ? true : false;
	}

	/**
	 * 生成JSON键值对
	 * @param tag	: 变量名
	 * @param value	: 变量值
	 * @return
	 */
	public static String makeJsonItem( String tag, Object value ) {
		StringBuilder buf = new StringBuilder();
		return buf.append( "\"" ).append( tag ).append( "\":\"" ).append( value ).append( "\"" ).toString( );
	}
	
	/**
	 * 向客户端输出字符串
	 */
	protected void outputToClient( HttpServletRequest request, HttpServletResponse response, String outCode ) {
		try {
    		//设置编码，这句最重要
    		response.setContentType("text/html; charset=utf-8");    
    		response.setCharacterEncoding("utf-8"); 
    		
    		//设置浏览器不要缓存  
    		response.setHeader("Pragma", "No-cache");     
    		response.setHeader("Cache-Control", "no-cache");    
    		response.setDateHeader("Expires", 0);
    		
    		response.getWriter().print( outCode );
    	} catch( IOException ioe ) {
    		if(DEBUG) System.out.println( "AbstractDaemon.outputToClient(" + outCode + ")>> " + ioe.getMessage() );
    	}
	}
	
	/**
	 * 向客户端输出JSON数据
	 */
	protected void outputJSON( HttpServletRequest request, HttpServletResponse response, String jsonCode ) {
		try {
    		//设置编码，这句最重要
    		response.setContentType("application/json; charset=utf-8");    
    		response.setCharacterEncoding("utf-8"); 
    		
    		//设置浏览器不要缓存  
    		response.setHeader("Pragma", "No-cache");     
    		response.setHeader("Cache-Control", "no-cache");    
    		response.setDateHeader("Expires", 0);
    		
    		response.getWriter().print( jsonCode );
    	} catch( IOException ioe ) {
    		if(DEBUG) System.out.println( "AbstractDaemon.outputJSON(" + jsonCode + ")>> " + ioe.getMessage() );
    	}
	}

	/**
	 * 检查验证码(验证码应该由服务器端生成，在客户端实现)
	 */
	protected String checkVerifyCode( HttpServletRequest request, String input ) {
		String t_VerifyTime = (String)request.getSession().getAttribute( SESS_VERIFY_TIME );
		if ( t_VerifyTime == null ) {
			return "请输入验证码！";
		}
		long verifyTime = Long.parseLong( t_VerifyTime );
		long curTime = System.currentTimeMillis();
		if ( curTime - verifyTime <= 3000 ) {		//间隔须大于3秒:3*1000
			return "输入太快了！";
		}
		if ( curTime - verifyTime >= 300000 ) {		//间隔须小于5分钟:5*60*1000
			return "验证码超时！";
		}
		String verifyCode = (String)request.getSession().getAttribute( SESS_VERIFY_CODE );
		if ( verifyCode.equals( input ) ) {
			removeSessionValue( request, SESS_VERIFY_TIME );
			removeSessionValue( request, SESS_VERIFY_CODE );
			return RET_SUCCESS;
		} else {
			return RET_FAILED;
		}
	}

}