package com.vinsys.hrms.kra.vo;

public class SimpleData {

	private int count;
	private String label;

	public SimpleData(int count, String label) {
		this.count = count;
		this.label = label;
	}

	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
