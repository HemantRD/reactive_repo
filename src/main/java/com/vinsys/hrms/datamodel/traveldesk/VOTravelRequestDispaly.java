package com.vinsys.hrms.datamodel.traveldesk;

import com.vinsys.hrms.datamodel.VOAuditBase;

public class VOTravelRequestDispaly extends  VOAuditBase {
	private long id;
	private String requestedBy;
	private long seqId;
	private String travelStatus;
	private String requestSummaryCount;
	private String requestedDate;
	private long workOrderNo;
	
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getRequestedBy() {
		return requestedBy;
	}
	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}
	public long getSeqId() {
		return seqId;
	}
	public void setSeqId(long seqId) {
		this.seqId = seqId;
	}
	public String getTravelStatus() {
		return travelStatus;
	}
	public void setTravelStatus(String travelStatus) {
		this.travelStatus = travelStatus;
	}
	public String getRequestSummaryCount() {
		return requestSummaryCount;
	}
	public void setRequestSummaryCount(String requestSummaryCount) {
		this.requestSummaryCount = requestSummaryCount;
	}
	public String getRequestedDate() {
		return requestedDate;
	}
	public void setRequestedDate(String requestedDate) {
		this.requestedDate = requestedDate;
	}
	public long getWorkOrderNo() {
		return workOrderNo;
	}
	public void setWorkOrderNo(long workOrderNo) {
		this.workOrderNo = workOrderNo;
	}
	
	
	
	
	
	
	
}
