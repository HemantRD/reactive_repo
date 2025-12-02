package com.vinsys.hrms.kra.vo;

public class PercentageHeaderVO {

	private String label;
	 private int colspan;
	 
	 
	public PercentageHeaderVO(String label, int colspan) {
		super();
		this.label = label;
		this.colspan = colspan;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getColspan() {
		return colspan;
	}
	public void setColspan(int colspan) {
		this.colspan = colspan;
	}
}
