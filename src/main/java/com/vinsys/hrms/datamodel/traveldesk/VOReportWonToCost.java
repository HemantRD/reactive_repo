package com.vinsys.hrms.datamodel.traveldesk;

public class VOReportWonToCost {

	private String won;
	private String departmentName;
	private String bdName;
	private String createdDate;
	private String travelRequestId;
	private String subRequestId;
	private String requestType;
	private double totalCost;
	private double refundAmount;
	private double chargeableAmount;

	public String getWon() {
		return won;
	}

	public void setWon(String won) {
		this.won = won;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getBdName() {
		return bdName;
	}

	public void setBdName(String bdName) {
		this.bdName = bdName;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getTravelRequestId() {
		return travelRequestId;
	}

	public void setTravelRequestId(String travelRequestId) {
		this.travelRequestId = travelRequestId;
	}

	public String getSubRequestId() {
		return subRequestId;
	}

	public void setSubRequestId(String subRequestId) {
		this.subRequestId = subRequestId;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public double getChargeableAmount() {
		return chargeableAmount;
	}

	public void setChargeableAmount(double chargeableAmount) {
		this.chargeableAmount = chargeableAmount;
	}
}
