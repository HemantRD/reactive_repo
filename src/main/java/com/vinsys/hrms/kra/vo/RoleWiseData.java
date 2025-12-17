package com.vinsys.hrms.kra.vo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class RoleWiseData {

	private String roleName;
	private BigInteger submittedCount;
	private BigDecimal submittedPercentage;
	private BigInteger pendingCount;
	private BigDecimal pendingPercentage;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public BigInteger getSubmittedCount() {
		return submittedCount;
	}

	public void setSubmittedCount(BigInteger submittedCount) {
		this.submittedCount = submittedCount;
	}

	public BigDecimal getSubmittedPercentage() {
		return submittedPercentage;
	}

	public void setSubmittedPercentage(BigDecimal submittedPercentage) {
		this.submittedPercentage = submittedPercentage;
	}

	public BigInteger getPendingCount() {
		return pendingCount;
	}

	public void setPendingCount(BigInteger pendingCount) {
		this.pendingCount = pendingCount;
	}

	public BigDecimal getPendingPercentage() {
		return pendingPercentage;
	}

	public void setPendingPercentage(BigDecimal pendingPercentage) {
		this.pendingPercentage = pendingPercentage;
	}

	public static class ChartData {
		private List<RoleWiseData> roleWiseStats;
		private List<Map<String, Object>> seriesData;

		public ChartData() {
		}

		public ChartData(List<RoleWiseData> roleWiseStats, List<Map<String, Object>> seriesData) {
			this.roleWiseStats = roleWiseStats;
			this.seriesData = seriesData;
		}

		public List<RoleWiseData> getRoleWiseStats() {
			return roleWiseStats;
		}

		public void setRoleWiseStats(List<RoleWiseData> roleWiseStats) {
			this.roleWiseStats = roleWiseStats;
		}

		public List<Map<String, Object>> getSeriesData() {
			return seriesData;
		}

		public void setSeriesData(List<Map<String, Object>> seriesData) {
			this.seriesData = seriesData;
		}

	}
}
