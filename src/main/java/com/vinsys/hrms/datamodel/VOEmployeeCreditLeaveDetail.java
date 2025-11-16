package com.vinsys.hrms.datamodel;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VOEmployeeCreditLeaveDetail extends VOAuditBase implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id;
    private VOEmployee employee;
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
	private float leaveAvailable;

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public VOEmployee getEmployee() {
	return employee;
    }

    public void setEmployee(VOEmployee employee) {
	this.employee = employee;
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

    public static long getSerialversionuid() {
	return serialVersionUID;
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

	public float getLeaveAvailable() {
		return leaveAvailable;
	}

	public void setLeaveAvailable(float leaveAvailable) {
		this.leaveAvailable = leaveAvailable;
	}
    
}
