package com.vinsys.hrms.kra.vo;

import java.util.List;

public class DelegationRequestVo {

	private Long assigneeId;
	private List<EmpListVo> assignFor;
	
	private List<String> skippedEmpIds;
	
	public Long getAssigneeId() {
		return assigneeId;
	}
	public void setAssigneeId(Long assigneeId) {
		this.assigneeId = assigneeId;
	}
	public List<EmpListVo> getAssignFor() {
		return assignFor;
	}
	public void setAssignFor(List<EmpListVo> assignFor) {
		this.assignFor = assignFor;
	}
	public List<String> getSkippedEmpIds() {
		return skippedEmpIds;
	}
	public void setSkippedEmpIds(List<String> skippedEmpIds) {
		this.skippedEmpIds = skippedEmpIds;
	}

}
