package xms;


import com.undao.cache.*;

import javax.sql.DataSource;

public class XmsContainer {

    private SystemicVariables systemicVariables = null;
    private MasterCompany masterCompany = null;
    private MasterPart masterPart = null;
    private MasterPost masterPost = null;
    private MasterRole masterRole = null;
    private RoleAstricts roleAstricts = null;

    /**
     * 初始化容器
     */
    public void initialContainer(DataSource dataSource) {
        systemicVariables.setDataSource( dataSource );
        systemicVariables.fixSingletonObject( );

        masterCompany.setDataSource( dataSource );
        masterCompany.fixSingletonObject( );

        masterPart.setDataSource( dataSource );
        masterPart.fixSingletonObject( );

        masterPost.setDataSource( dataSource );
        masterPost.fixSingletonObject( );

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

    public MasterCompany getMasterCompany() { return masterCompany; }

    public void setMasterCompany(MasterCompany masterCompany) { this.masterCompany = masterCompany; }


    public MasterPart getMasterPart() { return masterPart; }

    public void setMasterPart(MasterPart masterPart) { this.masterPart = masterPart; }


    public MasterPost getMasterPost() { return masterPost; }

    public void setMasterPost(MasterPost masterPost) { this.masterPost = masterPost; }


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
