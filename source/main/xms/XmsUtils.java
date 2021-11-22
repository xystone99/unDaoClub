/**
 * XmsUtils.java 
 *
 * Created at 2008-7-7, 上午10:54:25
 */
package xms;

/**
 * @author X.Stone
 *
 */
import javax.servlet.http.HttpServletRequest;

import com.undao.database.*;
import com.undao.control.*;
import com.undao.cache.*;

public class XmsUtils {

	private static String CODE_EXIT_FROM_PARENT = "";
	private static String CODE_EXIT_FROM_CHILD = "";
	private static String CODE_EXIT_FROM_GRANDSON = "";

	private static String CODE_EXIT_INNER_PARENT = "";
	private static String CODE_EXIT_INNER_OTHER = "";

	private static String CODE_EXIT_ASTRICT_PARENT = "";
	private static String CODE_EXIT_ASTRICT_OTHER = "";

	static {
		StringBuilder bufResult = new StringBuilder();
		bufResult.append( "<Script language=\"javascript\">" );
		bufResult.append( "alert(\"由于您离开时间过长导致连接断开，请重新登录......\");" );
		bufResult.append( "window.location.href=\"../login.jsp\";" );
		bufResult.append( "</Script>" );
		CODE_EXIT_FROM_PARENT = bufResult.toString();

		bufResult.delete(0,bufResult.length() );
		bufResult.append( "<Script language=\"javascript\">" );
		bufResult.append( "alert(\"离开时间过长，连接断开，请重新登录......\");" );
		bufResult.append( "window.opener.location.href=\"" ).append( XmsInitial.getContextPath() ).append( "/login.jsp\";");
		bufResult.append( "window.close();" );
		bufResult.append( "</Script>" );
		CODE_EXIT_FROM_CHILD = bufResult.toString( );

		bufResult.delete(0,bufResult.length() );
		bufResult.append( "<Script language=\"javascript\">" );
		bufResult.append( "alert(\"离开时间过长，连接断开，请重新登录......\");" );
		bufResult.append( "window.opener.opener.location.href=\"" ).append( XmsInitial.getContextPath() ).append( "/login.jsp\";");
		bufResult.append( "window.close();" );
		bufResult.append( "window.opener.close();" );
		bufResult.append( "</Script>" );
		CODE_EXIT_FROM_GRANDSON = bufResult.toString( );

		bufResult.delete(0, bufResult.length() );
		bufResult.append( "<Script language=\"javascript\">" );
		bufResult.append( "alert(\"内置用户，无权操作......\");" );
		bufResult.append( "window.history.go(-1);" );
		bufResult.append( "</Script>" );
		CODE_EXIT_INNER_PARENT = bufResult.toString();

		bufResult.delete(0, bufResult.length() );
		bufResult.append( "<Script language=\"javascript\">" );
		bufResult.append( "alert(\"内置用户，无权操作......\");" );
		bufResult.append( "window.close();" );
		bufResult.append( "</Script>" );
		CODE_EXIT_INNER_OTHER = bufResult.toString();

		bufResult.delete(0, bufResult.length() );
		bufResult.append( "<Script language=\"javascript\">" );
		bufResult.append( "alert(\"权限不足......\");" );
		bufResult.append( "window.history.go(-1);" );
		bufResult.append( "</Script>" );
		CODE_EXIT_ASTRICT_PARENT = bufResult.toString();

		bufResult.delete(0, bufResult.length() );
		bufResult.append( "<Script language=\"javascript\">" );
		bufResult.append( "alert(\"权限不足......\");" );
		bufResult.append( "window.close();" );
		bufResult.append( "</Script>" );
		CODE_EXIT_ASTRICT_OTHER = bufResult.toString();
	}

	/**
	 * JSP Security Logic Process 
	 */
	public static final String securityCheck( HttpServletRequest request, boolean acceptInnerUser, String curRole, String[] needAstricts, int winMode ) {
		if ( AbstractDaemon.getCloudID( request ) == null ) {
			if ( winMode == CtrlConstants.WM_PARENT ) {
				return CODE_EXIT_FROM_PARENT;
			} else if ( winMode == CtrlConstants.WM_CHILD ) {
				return CODE_EXIT_FROM_CHILD;
			} else if ( winMode == CtrlConstants.WM_GRANDSON ) {
				return CODE_EXIT_FROM_GRANDSON;
			}
		}
		if ( AbstractDaemon.isSystemAdministrator( request ) ) {
			if ( acceptInnerUser ) {
				return CtrlConstants.RET_BLANK;
			} else {
				if ( winMode == CtrlConstants.WM_PARENT ) {
					return CODE_EXIT_INNER_PARENT;
				} else {
					return CODE_EXIT_INNER_OTHER;
				}
			}
		}
		
		if ( needAstricts == null ) {
			return CtrlConstants.RET_BLANK;
		}
		
		RoleAstricts roleAstricts = XmsInitial.getXmsContainer().getRoleAstricts( );
		if ( !roleAstricts.checkPassport( curRole, needAstricts ) ) {
			if ( winMode == CtrlConstants.WM_PARENT ) {
				return CODE_EXIT_ASTRICT_PARENT;
			} else  {
				return CODE_EXIT_ASTRICT_OTHER;
			}
		} 
		return CtrlConstants.RET_BLANK;
	}
	
	/**
	 * 查询分页器(主要用于攒房网)
	 * @param formName	：所属Form名称
	 * @param curPage	：当前页码(JSP页面指定Hidden组件)
	 * @param pageCount	：总页数
	 * @return			：动态生成分页器代码 
	 */
	public static final String buildPageSeeker1( String formName, int curPage, int pageCount ) {
		StringBuilder bufSeeker = new StringBuilder( );
		if ( pageCount > 0 ) {
			bufSeeker.append( "<a href=\"javascript:void(0)\" onclick=\"javascript:jumpToPage(1)\">首页</a>" );
		} else {
			bufSeeker.append( "<a href=\"javascript:alert('查询结果为空！');\">首页</a>" );
		}
		if ( curPage > 1 ) {
			bufSeeker.append( "<a href=\"javascript:void(0)\" onclick=\"javascript:jumpToPage(" ).append( curPage-1 ).append( ")\">上一页</a>" );
		} else {
			bufSeeker.append( "<a href=\"javascript:alert('已经是首页！');\">上一页</a>" );
		}
		
		for ( int countTag=1, j=curPage-4; countTag<=9 && j<=pageCount; j++ ) {
			if ( j <= 0 ) continue;
			if ( j == curPage ) {
				bufSeeker.append( "<a href=\"javascript:void(0);\" class=\"selected\">" ).append( j).append( "</a>" );
			} else {
				bufSeeker.append( "<a href=\"javascript:void(0)\" onclick=\"javascript:jumpToPage(" ).append( j ).append( ")\">" ).append( j ).append( "</a>" );
			}
			countTag++;
		}
		
		if ( curPage < pageCount ) {
			bufSeeker.append( "<a href=\"javascript:void(0)\" onclick=\"javascript:jumpToPage(" ).append( curPage+1 ).append( ")\">下一页</a>" );
		} else {
			bufSeeker.append( "<a href=\"javascript:alert('已经是末页！');\">下一页</a>" );
		}
		if ( pageCount > 0 ) {
			bufSeeker.append( "<a href=\"javascript:void(0)\" onclick=\"javascript:jumpToPage(" ).append( pageCount ).append( ")\">末页</a>" );
		} else {
			bufSeeker.append( "<a href=\"javascript:alert('查询结果为空！');\">末页</a>" );
		}
		bufSeeker.append( "<span>共" ).append( pageCount ).append( "页</span>" );
		
		bufSeeker.append( "<script type=\"text/javascript\">" );
		bufSeeker.append( "function jumpToPage(pageNo){\r\n" );		//查询
		bufSeeker.append( formName ).append( "." ).append( AbstractQuery.QP_CUR_PAGE ).append( ".value=pageNo;\r\n" );
		bufSeeker.append( formName ).append( ".submit();\r\n" ); 
		bufSeeker.append( "}\r\n" );
		bufSeeker.append( "</script>" );
		
		return bufSeeker.toString( );
	}

	public static final String buildPageSeeker2( String formName, int curPage, int pageCount ) {
		return buildPageSeeker2( formName, curPage, pageCount, false );
	}

	/**
	 * 查询分页器(主要用于后台)
	 * @param formName 		：查询表单名称
	 * @param curPage		：当前页码(自动生成隐含的HIDDEN组件)
	 * @param pageCount		：总页数,
	 * @param exportButton	: 是否生成导出按钮
	 * @return				：动态生成分页器代码
	 */
	public static final String buildPageSeeker2( String formName, int curPage, int pageCount, boolean exportButton ) {
		StringBuilder bufSeeker = new StringBuilder( );
		bufSeeker.append( "<input class=\"input_text\" type=\"hidden\" name=\"" ).append( AbstractQuery.QP_CUR_PAGE ).append( "\" value=\"" ).append( curPage ).append( "\">" );
		bufSeeker.append( "<p>" );
		if ( exportButton ) {
			bufSeeker.append( "<input type=\"button\" name=\"btnQuery\" value=\"导出一览表\" onclick=\"javascript:exportQuery();\" />&nbsp;&nbsp;&nbsp;&nbsp;" );
		}
		bufSeeker.append( "<select class=\"select\" name=\"" ).append( AbstractQuery.QP_PAGE_SIZE ).append( "\" onchange=\"javascript:retrieve();\" >" );
		bufSeeker.append( "<option value=\"100000\">全部列出</option>" );
		bufSeeker.append( "<option value=\"20\">每页20条</option>" );
		bufSeeker.append( "<option value=\"50\">每页50条</option>" );
		bufSeeker.append( "<option value=\"100\">每页100条</option>" );
		bufSeeker.append( "</select>&nbsp;&nbsp;" );
		
		bufSeeker.append( "<span>" ).append( pageCount>0?curPage:0 ).append( "&nbsp;/&nbsp;" ).append( pageCount ).append( "页</span>&nbsp;&nbsp;" );
		
		if ( pageCount > 0 ) {
			bufSeeker.append( "<a href=\"javascript:void(0)\" class=\"first\" onclick=\"javascript:jumpToPage(1)\">首页</a>&nbsp;&nbsp;" );
		} else {
			bufSeeker.append( "<a href=\"javascript:void(0)\" class=\"first\" onclick=\"javascript:alert('查询结果为空');\">首页</a>&nbsp;&nbsp;" );
		}
		if ( curPage > 1 ) {
			bufSeeker.append( "<a href=\"javascript:void(0)\" class=\"previous\" onclick=\"javascript:jumpToPage(" ).append( curPage-1 ).append( ")\">上一页</a>&nbsp;&nbsp;" );
		} else {
			bufSeeker.append( "<a href=\"javascript:void(0)\" class=\"previous\" onclick=\"javascript:alert('已经是首页！')\">上一页</a>&nbsp;&nbsp;" );
		}
		if ( curPage < pageCount ) {
			bufSeeker.append( "<a href=\"javascript:void(0)\" class=\"next\" onclick=\"javascript:jumpToPage(" ).append( curPage+1 ).append( ")\">下一页</a>&nbsp;&nbsp;" );
		} else {
			bufSeeker.append( "<a href=\"javascript:void(0)\" class=\"next\" onclick=\"javascript:alert('已经是末页！')\">下一页</a>&nbsp;&nbsp;" );
		}
		if ( pageCount > 0 ) {
			bufSeeker.append( "<a href=\"javascript:void(0)\" class=\"last\" onclick=\"javascript:jumpToPage(" ).append( pageCount ).append( ")\">末页</a>&nbsp;&nbsp;" );
		} else {
			bufSeeker.append( "<a href=\"javascript:void(0)\" class=\"last\" onclick=\"javascript:alert('查询结果为空');\">末页</a>&nbsp;&nbsp;" );
		}
	
		bufSeeker.append( "<span>跳转到第</span><input class=\"textborder\" type=\"text\" name=\"tJumpTo\" size=\"4\" maxlength=\"2\" value=\"").append(curPage).append( "\"><span>页</span>&nbsp;<a href=\"javascript:void(0)\" class=\"jump\" onclick=\"javascript:jumpToPage(" ).append(formName).append( ".tJumpTo.value)\">跳转</a>&nbsp;" );
		bufSeeker.append( "</p>" );
		
		bufSeeker.append( "<script>" );
		
		bufSeeker.append( "function retrieve(){\r\n" );		//更改每页记录数
		bufSeeker.append( formName ).append( "." ).append( AbstractQuery.QP_CUR_PAGE ).append( ".value=1;" );
		bufSeeker.append( formName ).append( ".submit();" ); 
		bufSeeker.append( "}\r\n" );
		
		bufSeeker.append( "function executeQuery(){\r\n" );		//查询
		bufSeeker.append( formName ).append( "." ).append( AbstractQuery.QP_CUR_PAGE ).append( ".value=1;" );
		bufSeeker.append( formName ).append( ".submit();" ); 
		bufSeeker.append( "}\r\n" );
		
		bufSeeker.append( "function jumpToPage(pageNo){\r\n" );			//跳转到指定页
		if ( pageCount <= 0 ) {
			bufSeeker.append( "alert(\"查询结果为空！\");\r\n" );  
		} else {
			bufSeeker.append( formName ).append( ".tJumpTo.value=Trim(" ).append( formName ).append( ".tJumpTo.value);\r\n" );
			bufSeeker.append( "if (!checkFigure(" ).append( formName ).append( ".tJumpTo,false)) { alert(\"请输入有效页码！\"); return; }\r\n" );
			bufSeeker.append( "if (" ).append( formName ).append( ".tJumpTo.value >100 ) { alert(\"请输入有效页码！\"); return; }\r\n" );
			bufSeeker.append( formName ).append( "." ).append( AbstractQuery.QP_CUR_PAGE ).append( ".value=pageNo;" );
			bufSeeker.append( formName ).append( ".submit();\r\n" ); 
		}
		bufSeeker.append( "}\r\n" );
		bufSeeker.append( "</script>" );
		return bufSeeker.toString( );
	}
	
}