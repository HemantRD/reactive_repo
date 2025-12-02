package com.vinsys.hrms.employee.vo;

import java.util.Map;

public class CheckListByApproverVO {

	private Map<String,ChecklistResponseVO> checklist;
	private double totalAmount;

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Map<String, ChecklistResponseVO> getChecklist() {
		return checklist;
	}

	public void setChecklist(Map<String, ChecklistResponseVO> checklist) {
		this.checklist = checklist;
	}
	
}
