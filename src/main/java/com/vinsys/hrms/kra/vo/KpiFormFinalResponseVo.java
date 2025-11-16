package com.vinsys.hrms.kra.vo;

import java.util.List;

public class KpiFormFinalResponseVo {

	private Long yearId;
	private List<KpiFormStatusResponseVo> cycles;

	public Long getYearId() {
		return yearId;
	}

	public void setYearId(Long yearId) {
		this.yearId = yearId;
	}

	public List<KpiFormStatusResponseVo> getCycles() {
		return cycles;
	}

	public void setCycles(List<KpiFormStatusResponseVo> cycles) {
		this.cycles = cycles;
	}

}
