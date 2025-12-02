package com.vinsys.hrms.kra.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
public class KraCountForHR {
	@Id
	@Column(name = "department_id")
	private Long departmentId;
	
	@Column(name = "department_name")
	private String departmentName;
	
	@Column(name = "incomplete")
    private Long incompleteCount;
	
	@Column(name = "notsubmitted")
    private Long notSubmittedCount;
	
	@Column(name = "inprocess")
    private Long inprocess;
	
	@Column(name = "approved")
    private Long approved;
	
	@Column(name = "rejected")
    private Long rejected;
	
	@Column(name = "total")
    private Long totalCount;

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getIncompleteCount() {
		return incompleteCount;
	}

	public void setIncompleteCount(Long incompleteCount) {
		this.incompleteCount = incompleteCount;
	}

	public Long getNotSubmittedCount() {
		return notSubmittedCount;
	}

	public void setNotSubmittedCount(Long notSubmittedCount) {
		this.notSubmittedCount = notSubmittedCount;
	}

	public Long getInprocess() {
		return inprocess;
	}

	public void setInprocess(Long inprocess) {
		this.inprocess = inprocess;
	}

	public Long getApproved() {
		return approved;
	}

	public void setApproved(Long approved) {
		this.approved = approved;
	}

	public Long getRejected() {
		return rejected;
	}

	public void setRejected(Long rejected) {
		this.rejected = rejected;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
	

}
