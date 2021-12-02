/**
 * DatabaseConstants.java
 *
 * Created on 20080312, 10:15 A.M
 */
package com.undao.database;

/**
 * @author X.Stone
 *
 */
public interface DatabaseConstants {
	

	public static final int PARAM_SHORT = 1101;
	public static final int PARAM_INTEGER = 1102;
	public static final int PARAM_STRING = 1103;
	public static final int PARAM_DATE = 1104;
	public static final int PARAM_DECIMAL = 1105;

	public static final String ORDER_ASC = "ASC";				//升序标记
	public static final String ORDER_DESC = "DESC";				//降序标记
	
	public static final String SQL_EMPTY = "";
	public static final String SQL_SPACE = " ";
	public static final String SQL_ZERO = "0";
	public static final String SQL_COMMA = ",";
	public static final String SQL_HYPHEN = "-";
	public static final String SQL_UNDERLINE = "_";
	public static final String SQL_ALL = "All";
	public static final String SQL_NORMAL = "Normal";
	public static final String SQL_NOT = "N";
	public static final String SQL_YES = "Y";
	public static final int SQL_LIKE_LEFT = 11;
	public static final int SQL_LIKE_RIGHT = 12;
	public static final int SQL_LIKE_BOTH = 13;

	public final static String R_NEW_SUCCESS = "NewSuccess";					//新增成功
	public final static String R_UPDATE_SUCCESS = "UpdateSuccess";				//修改成功
	public final static String R_DELETE_SUCCESS = "DeleteSuccess";				//删除成功
	public final static String R_IN_USE = "InUse";								//使用中(一般用于删除时检查是否在使用)
	public final static String R_EXISTS = "Exists";								//已存在(一般用于新增时检查是否已存在)
	public final static String R_NOT_EXISTS = "NotExists";						//应存在而检索不到
	public final static String RTN_WAIT_CHECK = "WaitCheck";					//等待进一步检查
	public final static String R_INVALID = "Invalid";							//非法操作(应关注)
	public final static String R_OVERFLOW = "Overflow";							//溢出,超出范围(应关注)

	public final static String R_SQL_NOTFOUND = "SQLNotFound";					//数据库-编号不存在
	public final static String R_SQL_CONSTRAINT_ERROR = "SQLConstraintError";	//数据库-编号已经存在
	public final static String R_SQL_WARNING = "SQLWarning";					//数据库报告警告
	public final static String R_SQL_EXCEPTION = "SQLException";				//数据库报告异常

	public final static String RD_UNKNOWN = "未知的返回结果";

}
