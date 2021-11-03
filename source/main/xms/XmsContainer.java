package xms;


import com.undao.cache.MasterRole;
import com.undao.cache.RoleAstricts;
import com.undao.cache.SystemicVariables;

import javax.sql.DataSource;

public class XmsContainer {

    private SystemicVariables systemicVariables = null;
    private MasterRole masterRole = null;
    private RoleAstricts roleAstricts = null;

    /**
     * 初始化容器
     */
    public void initialContainer(DataSource dataSource) {
        systemicVariables.setDataSource( dataSource );
        systemicVariables.fixSingletonObject( );

        masterRole.setDataSource( dataSource );
        masterRole.fixSingletonObject( );

        roleAstricts.setDataSource( dataSource );
        roleAstricts.fixSingletonObject( );
    }


    public SystemicVariables getSystemicVariables() {
        return systemicVariables;
    }

    public void setSystemicVariables(SystemicVariables systemicVariables) {
        this.systemicVariables = systemicVariables;
    }

    public MasterRole getMasterRole() {
        return masterRole;
    }

    public void setMasterRole(MasterRole masterRole) {
        this.masterRole = masterRole;
    }

    public RoleAstricts getRoleAstricts() {
        return roleAstricts;
    }

    public void setRoleAstricts(RoleAstricts roleAstricts) {
        this.roleAstricts = roleAstricts;
    }
}
