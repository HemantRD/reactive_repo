package com.vinsys.hrms.master.vo;

import java.util.List;

import com.vinsys.hrms.dashboard.vo.MasterLeaveTypeVO;

/**
 * @author Onkar A
 *
 * 
 */
public class MasterLeaveTypesDetailsVO {
	private List<MasterLeaveTypeVO> leaveTypesDetails;

	public List<MasterLeaveTypeVO> getLeaveTypesDetails() {
		return leaveTypesDetails;
	}

	public void setLeaveTypesDetails(List<MasterLeaveTypeVO> leaveTypesDetails) {
		this.leaveTypesDetails = leaveTypesDetails;
	}

}
