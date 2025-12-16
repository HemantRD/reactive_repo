package com.vinsys.hrms.kra.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public class DonutCountResponse {
	
	private List<DonutDatum> donutData;

	public List<DonutDatum> getDonutData() {
	return donutData;
	}

	public void setDonutData(List<DonutDatum> donutData) {
	this.donutData = donutData;
	}

	
}
