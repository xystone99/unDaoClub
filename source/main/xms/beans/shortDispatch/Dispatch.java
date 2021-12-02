package xms.beans.shortDispatch;

import com.undao.database.AbstractBean;

import javax.sql.DataSource;
import java.util.HashMap;

public class Dispatch extends AbstractBean {

	public final static String QP_ID = "ID";							//车次ID
	public final static String QP_DISPT_SERIAL = "disptSerial";			//车次号
	public final static String QP_DEPART_DATE = "fPlanDate";   			//发车日期
	public final static String QP_TRUCK = "fTruck";   					//车辆ID
	public final static String QP_PLATE_NUMBER = "fPlateNumber";   		//车牌号
	public final static String QP_DRIVER = "fDriver";   				//司机ID
	public final static String QP_TEL_DRIVER = "fTelDriver";   			//司机姓名+电话
	public final static String QP_SUB_DRIVER = "fSubDriver";   			//副驾驶或押运员
	public final static String QP_TRANS_MODE = "transMode";				//运输模式
	public final static String QP_TRANS_PLANS = "fTransPlans";   		//关联的运输计划
	public final static String QP_REMARK = "fRemark";					//其它说明


	private final static String[] i_param_serial = {
		QP_DISPT_SERIAL, QP_DEPART_DATE, QP_TRUCK, QP_PLATE_NUMBER, QP_DRIVER, QP_TEL_DRIVER, QP_SUB_DRIVER, QP_TRANS_MODE, QP_TRANS_PLANS, QP_REMARK
	};
	private final static String[] d_param_serial = { QP_ID  };

	private final static HashMap<String,String> mapResult = new HashMap<String,String>();
	static {
		mapResult.put( R_NEW_SUCCESS, "车次已创建." );
		mapResult.put( R_DELETE_SUCCESS, "车次已删除." );
		mapResult.put( R_INVALID, "禁止修改或删除其他用户创建的车次." );
		mapResult.put( R_IN_USE, "禁止修改或删除已被引用的车次." );
		mapResult.put( R_OVERFLOW, "车次序号可能选择有误，请检查." );
	}

	public Dispatch(DataSource dataSource, int beanType) {
		super(dataSource, beanType);
		if ( beanType == BTYPE_INSERT ) {
			setProcedureString( "{CALL proc_dispatch_new(?,?,?,?,?,?,?,?,?,?,  ?,?,  ?,?  ) }" );
			setCountReturnValues( 2 );
			setParamSerial( i_param_serial );
		} else if ( beanType == BTYPE_DELETE ) {
			setProcedureString( "{CALL proc_dispatch_delete(?,  ?,?,  ? ) }" );
			setCountReturnValues( 1 );
			setParamSerial( d_param_serial );
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
