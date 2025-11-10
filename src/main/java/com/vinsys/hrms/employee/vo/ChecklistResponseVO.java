package com.vinsys.hrms.employee.vo;

import java.util.List;

public class ChecklistResponseVO {

	//private String approver;
	private double totalAmount;
	private List<ChecklistVO> checklist;

	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public List<ChecklistVO> getChecklist() {
		return checklist;
	}
	public void setChecklist(List<ChecklistVO> checklist) {
		this.checklist = checklist;
	}
	
	
}