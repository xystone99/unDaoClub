package xms.queries;

import com.undao.database.DatabaseConstants;

public interface QueryConstants extends DatabaseConstants {


	//运输计划及关联车次信息
	public final static String SQL_PLAN_AND_DISPATCH = "SELECT * FROM view_dispatch_detail WHERE dispt = ? AND trans_p = ?";

	//运单毛利信息查询
	public final static String WAYBILL_GROSS_SQL = "SELECT * FROM trn_waybill_gross WHERE waybill = ?";

	//已选回单列表
	public final static String RECEIPT_SELECTED_SQL = "SELECT * FROM view_receipt_selected WHERE packg = ?";

	//车次基础信息查询
	public final static String DISPT_SQL = "SELECT * FROM trn_dispatch WHERE dispt = ?";

	//库存检索
	public final static String STOCK_SQL = "SELECT * FROM trn_stock WHERE company = ? AND waybill = ?";

	
}
