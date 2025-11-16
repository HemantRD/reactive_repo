package com.vinsys.hrms.datamodel.attendance;

import com.vinsys.hrms.datamodel.VOAuditBase;

public class VOAttendanceMDBSwapData extends VOAuditBase {

	
	private long cardNo;
	private long orgId;
	private String swipeDateTime;
	private String direction;
	private String remarks;
	
	
	public long getCardNo() {
		return cardNo;
	}
	public void setCardNo(long cardNo) {
		this.cardNo = cardNo;
	}
	public long getOrgId() {
		return orgId;
	}
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	public String getSwipeDateTime() {
		return swipeDateTime;
	}
	public void setSwipeDateTime(String swipeDateTime) {
		this.swipeDateTime = swipeDateTime;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
	
	
}
