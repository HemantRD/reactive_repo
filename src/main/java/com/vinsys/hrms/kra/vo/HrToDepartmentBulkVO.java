package com.vinsys.hrms.kra.vo;

import java.util.List;

public class HrToDepartmentBulkVO {

	private Long empId;
	private List<Long> deptIds;

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public List<Long> getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(List<Long> deptIds) {
		this.deptIds = deptIds;
	}

}
