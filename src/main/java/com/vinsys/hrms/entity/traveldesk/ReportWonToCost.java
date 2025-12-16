package com.vinsys.hrms.entity.traveldesk;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name="view_won_to_cost_report")
public class ReportWonToCost {

	@Id
	@Column(name = "row_number")
	private String rowNumber;
	@Column(name = "travel_id")
	private String travelRequestId;
	@Column(name = "sub_request_id")
	private String subRequestId;
	@Column(name = "won")
	private String won;
	@Column(name = "department_name")
	private String departmentName;
	@Column(name = "bd_name")
	private String bdName;
	@Column(name = "created_date")
	private Date createdDate;
	@Column(name = "request_type")
	private String requestType;
	@Column(name = "total_cost")
	private double totalCost;
	@Column(name = "refund_amount")
	private double refundAmount;
	@Column(name = "chargeable_amount")
	private double chargeableAmount;
	
	public String getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(String rowNumber) {
		this.rowNumber = rowNumber;
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
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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
