package com.vinsys.hrms.datamodel.traveldesk;

public class VOApproverAassignmentRequest {

	private VOTraveldeskApprover approver;
	private String approvalType;
	private long parentId;
	private long childId;
	private VOTraveldeskComment comment;

	public VOTraveldeskApprover getApprover() {
		return approver;
	}

	public void setApprover(VOTraveldeskApprover approver) {
		this.approver = approver;
	}

	public String getApprovalType() {
		return approvalType;
	}

	public void setApprovalType(String approvalType) {
		this.approvalType = approvalType;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public long getChildId() {
		return childId;
	}

	public void setChildId(long childId) {
		this.childId = childId;
	}

	public VOTraveldeskComment getComment() {
		return comment;
	}

	public void setComment(VOTraveldeskComment comment) {
		this.comment = comment;
	}

}
