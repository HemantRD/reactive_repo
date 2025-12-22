package com.vinsys.hrms.datamodel;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VOCreditLeaveRequest {
	
    private long id;
    private List<VOEmployee> employees;
    private VOMasterLeaveType masterLeaveType;
    private float noOfDays;
    @JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date fromDate;
    @JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date toDate;
    private String openingBalanceConsidered;
    private String creditedBy;
	private String comment;
	 @JsonFormat
	    (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date postedDate;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public List<VOEmployee> getEmployees() {
		return employees;
	}
	public void setEmployees(List<VOEmployee> employees) {
		this.employees = employees;
	}
	public VOMasterLeaveType getMasterLeaveType() {
		return masterLeaveType;
	}
	public void setMasterLeaveType(VOMasterLeaveType masterLeaveType) {
		this.masterLeaveType = masterLeaveType;
	}
	public float getNoOfDays() {
		return noOfDays;
	}
	public void setNoOfDays(float noOfDays) {
		this.noOfDays = noOfDays;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getOpeningBalanceConsidered() {
		return openingBalanceConsidered;
	}
	public void setOpeningBalanceConsidered(String openingBalanceConsidered) {
		this.openingBalanceConsidered = openingBalanceConsidered;
	}
	public String getCreditedBy() {
		return creditedBy;
	}
	public void setCreditedBy(String creditedBy) {
		this.creditedBy = creditedBy;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getPostedDate() {
		return postedDate;
	}
	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}
	
}
