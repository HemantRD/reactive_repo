package com.vinsys.hrms.datamodel;

import java.util.List;

public class LeaveDetailsResponse extends HRMSBaseResponse{

    private long id;
    private String name;
    private List<VOEmployeeLeaveDetail> employeeLeaveDetail;
    private List<VOEmployeeLeaveApplied> employeeAppliedLeavesDetails;
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<VOEmployeeLeaveDetail> getEmployeeLeaveDetail() {
        return employeeLeaveDetail;
    }
    public void setEmployeeLeaveDetail(List<VOEmployeeLeaveDetail> employeeLeaveDetail) {
        this.employeeLeaveDetail = employeeLeaveDetail;
    }
    public List<VOEmployeeLeaveApplied> getEmployeeAppliedLeavesDetails() {
        return employeeAppliedLeavesDetails;
    }
    public void setEmployeeAppliedLeavesDetails(List<VOEmployeeLeaveApplied> employeeAppliedLeavesDetails) {
        this.employeeAppliedLeavesDetails = employeeAppliedLeavesDetails;
    }
    
    
    
    
    
}
