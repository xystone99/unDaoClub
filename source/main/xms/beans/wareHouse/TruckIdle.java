package xms.beans.wareHouse;

import com.undao.database.AbstractBean;

import javax.sql.DataSource;
import java.util.HashMap;

public class TruckIdle extends AbstractBean {

	public final static String QP_ID = "ID";							//闲置ID
	public final static String QP_IDLE_K = "fIdleK";					//闲置类型
	public final static String QP_TRUCK = "fTruck";   					//车辆ID
	public final static String QP_PLATE_NUMBER = "fPlateNumber";   		//车牌号
	public final static String QP_DRIVER = "fDriver";   				//司机ID
	public final static String QP_TEL_DRIVER = "fTelDriver";   			//司机姓名+电话
	public final static String QP_START_DATE = "fStartDate";   			//开始日期
	public final static String QP_END_DATE = "fEndDate";   				//结束日期
	public final static String QP_REMARK = "fRemark";					//其它说明


	private final static String[] i_param_serial = {
		QP_IDLE_K, QP_TRUCK, QP_PLATE_NUMBER, QP_DRIVER, QP_TEL_DRIVER, QP_START_DATE, QP_END_DATE, QP_REMARK
	};
	private final static String[] d_param_serial = { QP_ID  };

	private final static HashMap<String,String> mapResult = new HashMap<String,String>();
	static {
		mapResult.put( R_NEW_SUCCESS, "车辆闲置信息已登记." );
		mapResult.put( R_DELETE_SUCCESS, "车辆闲置信息已删除." );
		mapResult.put( R_INVALID, "非法操作." );
		mapResult.put( R_IN_USE, "与之前提交的信息重复，请检查." );
	}

	public TruckIdle(DataSource dataSource, int beanType) {
		super(dataSource, beanType);
		if ( beanType == BTYPE_INSERT ) {
			setProcedureString( "{CALL proc_truck_idle_feedback(?,?,?,?,?,?,?,?,  ?,?,?,  ?  ) }" );
			setCountReturnValues( 1 );
			setParamSerial( i_param_serial );
		} else if ( beanType == BTYPE_DELETE ) {
			setProcedureString( "{CALL proc_truck_idle_delete(?,  ?,?,  ? ) }" );
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
