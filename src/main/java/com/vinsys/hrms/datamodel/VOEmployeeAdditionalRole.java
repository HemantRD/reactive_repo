package com.vinsys.hrms.datamodel;

public class VOEmployeeAdditionalRole extends VOAuditBase {

    private long id;
    private VOEmployeeCurrentDetail employeeCurrentDetail;
    private VOLoginEntityType loginEntityType;
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public VOEmployeeCurrentDetail getEmployeeCurrentDetail() {
        return employeeCurrentDetail;
    }
    public void setEmployeeCurrentDetail(VOEmployeeCurrentDetail employeeCurrentDetail) {
        this.employeeCurrentDetail = employeeCurrentDetail;
    }
    public VOLoginEntityType getLoginEntityType() {
        return loginEntityType;
    }
    public void setLoginEntityType(VOLoginEntityType loginEntityType) {
        this.loginEntityType = loginEntityType;
    }

   

}
