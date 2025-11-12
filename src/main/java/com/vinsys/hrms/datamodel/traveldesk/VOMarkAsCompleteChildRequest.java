package com.vinsys.hrms.datamodel.traveldesk;

public class VOMarkAsCompleteChildRequest {

	private long organizationId;
	private long seqId;
	private Long childRequestId;
	private String childType;
	private long loggedInEmployeeId;
	private double amount;
	private long travelRequestId;
	private String comment;
	private double totalRefundAmount;

	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	public long getSeqId() {
		return seqId;
	}

	public void setSeqId(long seqId) {
		this.seqId = seqId;
	}

	public Long getChildRequestId() {
		return childRequestId;
	}

	public void setChildRequestId(Long childRequestId) {
		this.childRequestId = childRequestId;
	}

	public String getChildType() {
		return childType;
	}

	public void setChildType(String childType) {
		this.childType = childType;
	}

	public long getLoggedInEmployeeId() {
		return loggedInEmployeeId;
	}

	public void setLoggedInEmployeeId(long loggedInEmployeeId) {
		this.loggedInEmployeeId = loggedInEmployeeId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public long getTravelRequestId() {
		return travelRequestId;
	}

	public void setTravelRequestId(long travelRequestId) {
		this.travelRequestId = travelRequestId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public double getTotalRefundAmount() {
		return totalRefundAmount;
	}

	public void setTotalRefundAmount(double totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}

}
