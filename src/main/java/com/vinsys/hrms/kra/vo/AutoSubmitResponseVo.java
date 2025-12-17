package com.vinsys.hrms.kra.vo;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class AutoSubmitResponseVo {
	private String totalSelfRating;
	private String totalManagerRating;
	private String empFunctionalAvg;
	private String empCoreAvg;
	private String empLeadershipAvg;
	private String managerFunctionalAvg;
	private String managerCoreAvg;
	private String managerLeadershipAvg;

	public String getTotalSelfRating() {
		return totalSelfRating;
	}

	public void setTotalSelfRating(String totalSelfRating) {
		this.totalSelfRating = totalSelfRating;
	}

	public String getTotalManagerRating() {
		return totalManagerRating;
	}

	public void setTotalManagerRating(String totalManagerRating) {
		this.totalManagerRating = totalManagerRating;
	}

	public String getEmpFunctionalAvg() {
		return empFunctionalAvg;
	}

	public void setEmpFunctionalAvg(String empFunctionalAvg) {
		this.empFunctionalAvg = empFunctionalAvg;
	}

	public String getEmpCoreAvg() {
		return empCoreAvg;
	}

	public void setEmpCoreAvg(String empCoreAvg) {
		this.empCoreAvg = empCoreAvg;
	}

	public String getEmpLeadershipAvg() {
		return empLeadershipAvg;
	}

	public void setEmpLeadershipAvg(String empLeadershipAvg) {
		this.empLeadershipAvg = empLeadershipAvg;
	}

	public String getManagerFunctionalAvg() {
		return managerFunctionalAvg;
	}

	public void setManagerFunctionalAvg(String managerFunctionalAvg) {
		this.managerFunctionalAvg = managerFunctionalAvg;
	}

	public String getManagerCoreAvg() {
		return managerCoreAvg;
	}

	public void setManagerCoreAvg(String managerCoreAvg) {
		this.managerCoreAvg = managerCoreAvg;
	}

	public String getManagerLeadershipAvg() {
		return managerLeadershipAvg;
	}

	public void setManagerLeadershipAvg(String managerLeadershipAvg) {
		this.managerLeadershipAvg = managerLeadershipAvg;
	}

}
