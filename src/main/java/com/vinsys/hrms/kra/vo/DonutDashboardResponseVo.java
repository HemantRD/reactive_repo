package com.vinsys.hrms.kra.vo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.vinsys.hrms.kra.vo.RoleWiseData.ChartData;

public class DonutDashboardResponseVo {

	private BigDecimal pendingWithLineManagerPercentage;
	private BigDecimal pendingWithHODPercentage;
	private BigDecimal pendingWithHRPercentage;
	private BigDecimal pendingWithEmployeePercentage;
	private BigDecimal submittedByLineManagerPercentage;
	private BigDecimal submittedByHODPercentage;
	private BigDecimal submittedByHRPercentage;
	private BigDecimal submittedByEmployeePercentage;
	private BigDecimal employeePercentage;
	private String cycleName;
	private Long cycleId;
	
	
	public BigDecimal getPendingWithLineManagerPercentage() {
		return pendingWithLineManagerPercentage;
	}
	public void setPendingWithLineManagerPercentage(BigDecimal pendingWithLineManagerPercentage) {
		this.pendingWithLineManagerPercentage = pendingWithLineManagerPercentage;
	}
	public BigDecimal getPendingWithHODPercentage() {
		return pendingWithHODPercentage;
	}
	public void setPendingWithHODPercentage(BigDecimal pendingWithHODPercentage) {
		this.pendingWithHODPercentage = pendingWithHODPercentage;
	}
	public BigDecimal getPendingWithHRPercentage() {
		return pendingWithHRPercentage;
	}
	public void setPendingWithHRPercentage(BigDecimal pendingWithHRPercentage) {
		this.pendingWithHRPercentage = pendingWithHRPercentage;
	}
	public BigDecimal getPendingWithEmployeePercentage() {
		return pendingWithEmployeePercentage;
	}
	public void setPendingWithEmployeePercentage(BigDecimal pendingWithEmployeePercentage) {
		this.pendingWithEmployeePercentage = pendingWithEmployeePercentage;
	}
	public BigDecimal getSubmittedByLineManagerPercentage() {
		return submittedByLineManagerPercentage;
	}
	public void setSubmittedByLineManagerPercentage(BigDecimal submittedByLineManagerPercentage) {
		this.submittedByLineManagerPercentage = submittedByLineManagerPercentage;
	}
	public BigDecimal getSubmittedByHODPercentage() {
		return submittedByHODPercentage;
	}
	public void setSubmittedByHODPercentage(BigDecimal submittedByHODPercentage) {
		this.submittedByHODPercentage = submittedByHODPercentage;
	}
	public BigDecimal getSubmittedByHRPercentage() {
		return submittedByHRPercentage;
	}
	public void setSubmittedByHRPercentage(BigDecimal submittedByHRPercentage) {
		this.submittedByHRPercentage = submittedByHRPercentage;
	}
	public BigDecimal getSubmittedByEmployeePercentage() {
		return submittedByEmployeePercentage;
	}
	public void setSubmittedByEmployeePercentage(BigDecimal submittedByEmployeePercentage) {
		this.submittedByEmployeePercentage = submittedByEmployeePercentage;
	}
	public BigDecimal getEmployeePercentage() {
		return employeePercentage;
	}
	public void setEmployeePercentage(BigDecimal employeePercentage) {
		this.employeePercentage = employeePercentage;
	}
	public String getCycleName() {
		return cycleName;
	}
	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}
	public Long getCycleId() {
		return cycleId;
	}
	public void setCycleId(Long cycleId) {
		this.cycleId = cycleId;
	}
	
	
}
