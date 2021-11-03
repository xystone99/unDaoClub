/**
 * XmsInitial.java 
 *
 * Created at 2008-7-7, 上午10:54:25
 */
package xms;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspApplicationContext;
import javax.sql.DataSource;

import com.undao.cache.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author X.Stone
 *
 */
public class XmsInitial extends HttpServlet {

	private static String security_key = "abcxyz";
	private static DataSource dataSource_undao = null;
	private static String HOME_DIR = null;
	private static String TEMPORARY_DIR = null;
	private static String PHOTOS_DIR = null;
	private static String CONTEXT_PATH = "/";

	private static ApplicationContext springContext = null;
	private static XmsContainer xmsContainer = null;

	public static DataSource getDataSource( ) { return dataSource_undao; 	}
	public static String getHomeDir( ) { return HOME_DIR; 	}
	public static String getTemporaryDir( ) { return TEMPORARY_DIR; 	}
	public static String getPhotosDir( ) { return PHOTOS_DIR; 	}
	public static String getContextPath( ) { return CONTEXT_PATH; 	}
	public static String getSecurityKey( ) { return security_key; 	}

	public static ApplicationContext getSpringContext( ) { return springContext;	}
	public static XmsContainer getXmsContainer( ) { return xmsContainer;	}

	public static boolean isInnerUser( HttpServletRequest request ) {
    	HttpSession session = request.getSession( );
    	if ( session == null ) {
    		return false;
    	}
    	String broker_flg = (String)session.getAttribute( "BROKER_FLG" );
    	if ( broker_flg == null ) {
    		return false;
    	}
    	return broker_flg.equals( "Inner" );
    }
    
    public void init( ) {
		ServletConfig config = getServletConfig( );
		ServletContext context = getServletContext( );

		String mailListIP = config.getInitParameter( "MailListIP" );		//IP变化时，接收的邮件列表
		String pool_undao = config.getInitParameter( "unDaoDBPool" );	//数据库连接池

        HOME_DIR = context.getRealPath( "/" );
		TEMPORARY_DIR = context.getRealPath( "/Temporary/" );
		PHOTOS_DIR = context.getRealPath( "/Photos/" );
        CONTEXT_PATH = context.getContextPath( );

        try {     
			Context initCtx = new javax.naming.InitialContext( );   
			Context envCtx = (Context)initCtx.lookup( "java:comp/env" );   
			dataSource_undao = (DataSource)envCtx.lookup( pool_undao );
		} catch( NamingException ne ) {   
			System.out.println("XmsInitial init DataSource Error:" + ne.getMessage() );   
		}

		springContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		xmsContainer = (XmsContainer)springContext.getBean("xmsContainer");
		xmsContainer.initialContainer( dataSource_undao );

		TruckGeometry truckGeometry = TruckGeometry.getInstance( );
		truckGeometry.setDataSource( dataSource_undao );
		truckGeometry.fixSingletonObject( );

		DriverGeometry driverGeometry = DriverGeometry.getInstance( );
		driverGeometry.setDataSource( dataSource_undao );
		driverGeometry.fixSingletonObject( );

		CustomerGeometry cusGeometry = CustomerGeometry.getInstance( );
		cusGeometry.setDataSource( dataSource_undao );
		cusGeometry.fixSingletonObject( );

		TransLineGeometry addressGeometry = TransLineGeometry.getInstance( );
		addressGeometry.setDataSource( dataSource_undao );
		addressGeometry.fixSingletonObject( );

		/*
        AdminDivision aDivision = AdminDivision.getInstance( );
        aDivision.setDataSource( dataSource_undao );
        aDivision.fixSingletonObject( );

        EvaluationGeometry eGeometry = EvaluationGeometry.getInstance( );
        eGeometry.setDataSource( dataSource_undao );
        eGeometry.fixSingletonObject( );
        
        EmployeeGeometry empGeometry = EmployeeGeometry.getInstance( );
        empGeometry.setDataSource( dataSource_undao );
        empGeometry.fixSingletonObject(); 
        EvaluatedEmployee vEmp = EvaluatedEmployee.getInstance( );
        vEmp.setDataSource( dataSource_undao );
        vEmp.fixSingletonObject( );
        
        ProviderGeometry pGeometry = ProviderGeometry.getInstance( );
        pGeometry.setDataSource( dataSource_undao );
        pGeometry.fixSingletonObject( ); 

        LoadingGeometry loadGeometry = LoadingGeometry.getInstance( );
        loadGeometry.setDataSource( dataSource_undao );
        loadGeometry.fixSingletonObject( );
        loadGeometry.fixSingletonUppGroup( );
        
        OilerGeometry oilerGeometry = OilerGeometry.getInstance( );
        oilerGeometry.setDataSource( dataSource_undao );
        oilerGeometry.fixSingletonObject( ); 
        
        ChargerGeometry chargerGeometry = ChargerGeometry.getInstance( );
        chargerGeometry.setDataSource( dataSource_undao );
        chargerGeometry.fixSingletonObject( ); 
        
        PaymentObjectGeometry pObjGeometry = PaymentObjectGeometry.getInstance( );
        pObjGeometry.setDataSource( dataSource_undao );
        pObjGeometry.fixSingletonObject( ); 

        ProviderObjectGeometry prderGeometry = ProviderObjectGeometry.getInstance( );
        prderGeometry.setDataSource( dataSource_undao );
        prderGeometry.fixSingletonObject( ); 

        FeeEngineGeometry feeEngineGeometry = FeeEngineGeometry.getInstance( );
        feeEngineGeometry.setDataSource( dataSource_undao );
        feeEngineGeometry.fixSingletonObject( );
        
        ElcLine elcLine = ElcLine.getInstance( );
        elcLine.setDataSource( dataSource_undao );
        elcLine.fixSingletonObject( );

        WeatherGrasper weaGrasper = new WeatherGrasper( dataSource_logistics );
        new Thread(weaGrasper).start( );

        TmsUserList tmsUserList = TmsUserList.getInstance( );
        tmsUserList.setUserFile( getHomeDir() + File.separator + "UserList.xlsx" );

        WeChatAccessToken weChatAccessToken = WeChatAccessToken.getInstance();
        new Thread(weChatAccessToken).start();
        */
    }       
       
}
