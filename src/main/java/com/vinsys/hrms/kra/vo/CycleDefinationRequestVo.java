package com.vinsys.hrms.kra.vo;

import java.util.List;

public class CycleDefinationRequestVo {

	private KraCycleResponseVo cycle;
	private String status;
	private List<RoleWiseCycleDefinationRequestVo> roleWiseResponse;
	
	public KraCycleResponseVo getCycle() {
		return cycle;
	}
	public void setCycle(KraCycleResponseVo cycle) {
		this.cycle = cycle;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<RoleWiseCycleDefinationRequestVo> getRoleWiseResponse() {
		return roleWiseResponse;
	}
	public void setRoleWiseResponse(List<RoleWiseCycleDefinationRequestVo> roleWiseResponse) {
		this.roleWiseResponse = roleWiseResponse;
	}
	
	
}
