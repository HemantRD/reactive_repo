package com.vinsys.hrms.kra.vo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.vinsys.hrms.kra.vo.RoleWiseData.ChartData;

public class DashboardResponseVo {

	private Date endDate;
	private String cycleName;
	private BigInteger activeCycleId;
	private BigInteger yearId;
	private String currentPhase;
	private BigInteger submittedCount;
	private BigInteger completedCount;
	private BigInteger totalCount;
	private BigInteger pendingCount;
	private BigDecimal submittedPercentage;
	private BigDecimal pendingPercentage;
	private BigDecimal completedPercentage;

	private String employeeStatus;
	private String pendingWith;
	private BigInteger cycleType;

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCycleName() {
		return cycleName;
	}

	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}

	public String getCurrentPhase() {
		return currentPhase;
	}

	public void setCurrentPhase(String currentPhase) {
		this.currentPhase = currentPhase;
	}

	public BigInteger getSubmittedCount() {
		return submittedCount;
	}

	public void setSubmittedCount(BigInteger submittedCount) {
		this.submittedCount = submittedCount;
	}

	public BigInteger getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(BigInteger totalCount) {
		this.totalCount = totalCount;
	}

	public BigInteger getPendingCount() {
		return pendingCount;
	}

	public void setPendingCount(BigInteger pendingCount) {
		this.pendingCount = pendingCount;
	}

	public BigDecimal getSubmittedPercentage() {
		return submittedPercentage;
	}

	public void setSubmittedPercentage(BigDecimal submittedPercentage) {
		this.submittedPercentage = submittedPercentage;
	}

	public BigDecimal getPendingPercentage() {
		return pendingPercentage;
	}

	public void setPendingPercentage(BigDecimal pendingPercentage) {
		this.pendingPercentage = pendingPercentage;
	}

	/*
	 * public BigInteger getPendingWithHod() { return pendingWithHod; }
	 * 
	 * public void setPendingWithHod(BigInteger pendingWithHod) {
	 * this.pendingWithHod = pendingWithHod; }
	 * 
	 * public BigDecimal getPendingWithHodPercentage() { return
	 * pendingWithHodPercentage; }
	 * 
	 * public void setPendingWithHodPercentage(BigDecimal pendingWithHodPercentage)
	 * { this.pendingWithHodPercentage = pendingWithHodPercentage; }
	 * 
	 * public BigInteger getPendingWithLineManager() { return
	 * pendingWithLineManager; }
	 * 
	 * public void setPendingWithLineManager(BigInteger pendingWithLineManager) {
	 * this.pendingWithLineManager = pendingWithLineManager; }
	 * 
	 * public BigDecimal getPendingWithLineManagerPercentage() { return
	 * pendingWithLineManagerPercentage; }
	 * 
	 * public void setPendingWithLineManagerPercentage(BigDecimal
	 * pendingWithLineManagerPercentage) { this.pendingWithLineManagerPercentage =
	 * pendingWithLineManagerPercentage; }
	 * 
	 * public BigInteger getPendingWithEmployee() { return pendingWithEmployee; }
	 * 
	 * public void setPendingWithEmployee(BigInteger pendingWithEmployee) {
	 * this.pendingWithEmployee = pendingWithEmployee; }
	 * 
	 * public BigDecimal getPendingWithEmployeePercentage() { return
	 * pendingWithEmployeePercentage; }
	 * 
	 * public void setPendingWithEmployeePercentage(BigDecimal
	 * pendingWithEmployeePercentage) { this.pendingWithEmployeePercentage =
	 * pendingWithEmployeePercentage; }
	 * 
	 * public BigInteger getPendingWithHr() { return pendingWithHr; }
	 * 
	 * public void setPendingWithHr(BigInteger pendingWithHr) { this.pendingWithHr =
	 * pendingWithHr; }
	 * 
	 * public BigDecimal getPendingWithHrPercentage() { return
	 * pendingWithHrPercentage; }
	 * 
	 * public void setPendingWithHrPercentage(BigDecimal pendingWithHrPercentage) {
	 * this.pendingWithHrPercentage = pendingWithHrPercentage; }
	 */

	public String getEmployeeStatus() {
		return employeeStatus;
	}

	public void setEmployeeStatus(String employeeStatus) {
		this.employeeStatus = employeeStatus;
	}

	public String getPendingWith() {
		return pendingWith;
	}

	public void setPendingWith(String pendingWith) {
		this.pendingWith = pendingWith;
	}

	public BigInteger getCompletedCount() {
		return completedCount;
	}

	public void setCompletedCount(BigInteger completedCount) {
		this.completedCount = completedCount;
	}

	public BigDecimal getCompletedPercentage() {
		return completedPercentage;
	}

	public void setCompletedPercentage(BigDecimal completedPercentage) {
		this.completedPercentage = completedPercentage;
	}

	public BigInteger getCycleType() {
		return cycleType;
	}

	public void setCycleType(BigInteger cycleType) {
		this.cycleType = cycleType;
	}

	public BigInteger getActiveCycleId() {
		return activeCycleId;
	}

	public void setActiveCycleId(BigInteger activeCycleId) {
		this.activeCycleId = activeCycleId;
	}

	public BigInteger getYearId() {
		return yearId;
	}

	public void setYearId(BigInteger yearId) {
		this.yearId = yearId;
	}

}
