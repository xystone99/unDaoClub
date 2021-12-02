package xms.beans.customerService;

import javax.sql.DataSource;
import com.undao.database.AbstractBean;

import java.util.HashMap;

public class TransPlan extends AbstractBean {

	public final static String QP_ID = "ID";							//车次ID
	public final static String QP_PLAN_K = "fPlanK";					//计划类型
	public final static String QP_PLAN_DATE = "fPlanDate";   			//计划日期
	public final static String QP_OBJ_P = "fObjP";   					//客户
	public final static String QP_TIME_LEVEL = "fTimeLevel";   			//时效等级
	public final static String QP_NE_RECYCLE = "fNeRecycle";   			//返空仓库列表
	public final static String QP_TRANS_L = "fTransL";   				//运输线路
	public final static String QP_NE_ZH1 = "fNeZh1";   					//发货方
	public final static String QP_ADDRESS_1 = "fAddress1";   			//地址1
	public final static String QP_LINKMAN_1 = "fLinkman1";				//联系人1
	public final static String QP_WINDOW_1 = "fWindow1";				//窗口1
	public final static String QP_REMARK_1 = "fRemark1";				//备注1
	public final static String QP_NE_ZH2 = "fNeZh2";   					//收货方
	public final static String QP_ADDRESS_2 = "fAddress2";   			//地址2
	public final static String QP_LINKMAN_2 = "fLinkman2";				//联系人2
	public final static String QP_WINDOW_2 = "fWindow2";				//窗口2
	public final static String QP_REMARK_2 = "fRemark2";				//备注2
	public final static String QP_QTY_W = "fQtyW";						//重量
	public final static String QP_QTY_V = "fQtyV";						//体积
	public final static String QP_QTY_METER = "fQtyMeter";				//占车米数
	public final static String QP_QTY_METER_R = "fQtyMeterR";			//返空占车米数
	public final static String QP_DISPT_REMARK = "disptRemark";			//调度安排

	private final static String[] i_param_serial = {
		QP_PLAN_K, QP_PLAN_DATE, QP_OBJ_P, QP_TIME_LEVEL, QP_NE_RECYCLE, QP_TRANS_L, QP_NE_ZH1, QP_ADDRESS_1, QP_LINKMAN_1, QP_WINDOW_1,
		QP_REMARK_1, QP_NE_ZH2, QP_ADDRESS_2, QP_LINKMAN_2, QP_WINDOW_2, QP_REMARK_2, QP_QTY_W, QP_QTY_V, QP_QTY_METER, QP_QTY_METER_R,
		QP_DISPT_REMARK
	};
	private final static String[] u_param_serial = {
		QP_ID, QP_PLAN_K, QP_PLAN_DATE, QP_OBJ_P, QP_TIME_LEVEL, QP_NE_RECYCLE, QP_TRANS_L, QP_NE_ZH1, QP_ADDRESS_1, QP_LINKMAN_1,
		QP_WINDOW_1, QP_REMARK_1, QP_NE_ZH2, QP_ADDRESS_2, QP_LINKMAN_2, QP_WINDOW_2, QP_REMARK_2, QP_QTY_W, QP_QTY_V, QP_QTY_METER,
		QP_QTY_METER_R, QP_DISPT_REMARK
	};
	private final static String[] d_param_serial = { QP_ID  };
	private final static String[] q_param_serial = { QP_ID  };

	private final static HashMap<String,String> mapResult = new HashMap<String,String>();
	static {
		mapResult.put( R_NEW_SUCCESS, "计划添加成功." );
		mapResult.put( R_UPDATE_SUCCESS, "计划已修改." );
		mapResult.put( R_DELETE_SUCCESS, "计划已删除." );
		mapResult.put( R_INVALID, "禁止修改或删除其他用户的计划." );
		mapResult.put( R_IN_USE, "禁止修改或删除已被引用的计划." );
	}

	public TransPlan(DataSource dataSource, int beanType) {
		super(dataSource, beanType);
		if ( beanType == BTYPE_INSERT ) {
			setProcedureString( "{CALL proc_trans_plan_new(?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,  ?,?,  ?,?  ) }" );
			setCountReturnValues( 2 );
			setParamSerial( i_param_serial );
		} else if ( beanType == BTYPE_UPDATE ) {
			setProcedureString( "{CALL proc_trans_plan_update(?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,  ?,?,  ?  ) }" );
			setCountReturnValues( 1 );
			setParamSerial( u_param_serial );
		} else if ( beanType == BTYPE_DELETE ) {
			setProcedureString( "{CALL proc_trans_plan_delete(?,  ?,?,  ? ) }" );
			setCountReturnValues( 1 );
			setParamSerial( d_param_serial );
		} else if ( beanType == BTYPE_DETAIL ) {
			setDetailSQL( "SELECT * FROM view_trans_plan_list WHERE trans_p = ? " );
			setParamSerial( q_param_serial );
		}
	}

	@Override
	public String getResultDisplay() {
		String result = getReturnValues()[getCountReturnValues()-1];
		if ( mapResult.containsKey( result ) ) {
			return mapResult.get( result );
		}
		return super.getResultDisplay( result );
	}

}
