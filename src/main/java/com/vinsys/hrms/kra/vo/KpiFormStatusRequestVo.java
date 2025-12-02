package com.vinsys.hrms.kra.vo;

import java.util.ArrayList;
import java.util.List;

public class KpiFormStatusRequestVo {

	private Long yearId;
	private List<KpiFormStatusCycleVo> cycles;
	private Long id;

	public Long getYearId() {
		return yearId;
	}

	public void setYearId(Long yearId) {
		this.yearId = yearId;
	}

	public List<KpiFormStatusCycleVo> getCycles() {
		return cycles;
	}

	public void setCycles(List<KpiFormStatusCycleVo> cycles) {
		this.cycles = cycles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
