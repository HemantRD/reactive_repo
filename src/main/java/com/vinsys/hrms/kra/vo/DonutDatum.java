package com.vinsys.hrms.kra.vo;

import java.util.List;


public class DonutDatum {

	private String label;
	private List<Series> series;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<Series> getSeries() {
		return series;
	}

	public void setSeries(List<Series> series) {
		this.series = series;
	}

}
