package com.vinsys.hrms.datamodel.traveldesk;

public class VOTraveldeskApproverListRequest {

	private long employeeId;
	private long organizationId;
	private int pageNumber;
	private int pageSize;
	
	public long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}
	public long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPagesize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}
