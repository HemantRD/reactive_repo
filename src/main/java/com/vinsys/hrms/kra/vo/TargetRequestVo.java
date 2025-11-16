package com.vinsys.hrms.kra.vo;

import java.util.List;

public class TargetRequestVo {
	
	 private List<LabelValuePair> targetLabelValuePairs;
	 
	

	public TargetRequestVo() {
		super();
	}

	public List<LabelValuePair> getTargetLabelValuePairs() {
		return targetLabelValuePairs;
	}

	public void setTargetLabelValuePairs(List<LabelValuePair> targetLabelValuePairs) {
		this.targetLabelValuePairs = targetLabelValuePairs;
	}
	 
	 
 public static class LabelValuePair {
	private String targetLabel;
	private String targetValue;
	
	
	public LabelValuePair(String targetLabel, String targetValue) {
		super();
		this.targetLabel = targetLabel;
		this.targetValue = targetValue;
	}
	
	public String getTargetLabel() {
		return targetLabel;
	}
	public void setTargetLabel(String targetLabel) {
		this.targetLabel = targetLabel;
	}
	public String getTargetValue() {
		return targetValue;
	}
	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

	public LabelValuePair() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
 }	
}
