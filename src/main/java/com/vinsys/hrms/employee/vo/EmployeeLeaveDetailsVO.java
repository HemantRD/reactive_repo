package com.vinsys.hrms.employee.vo;

import java.util.List;

public class EmployeeLeaveDetailsVO {

	private List<EmployeeGetAllAppliedLeavesVO> leaveDetails;
//	private ProfileVO managerProfile;

	
	public List<EmployeeGetAllAppliedLeavesVO> getLeaveDetails() {
		return leaveDetails;
	}

	public void setLeaveDetails(List<EmployeeGetAllAppliedLeavesVO> leaveDetails) {
		this.leaveDetails = leaveDetails;
	}

//	public ProfileVO getManagerProfile() {
//		return managerProfile;
//	}
//
//	public void setManagerProfile(ProfileVO managerProfile) {
//		this.managerProfile = managerProfile;
//	}
}
