package com.vinsys.hrms.kra.vo;

import java.util.List;

public class PmsDepartmentVo {

	private Long departmentId;
	private String departmentName;
	private List<EmpListVo> employees;
	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public List<EmpListVo> getEmployees() {
		return employees;
	}
	public void setEmployees(List<EmpListVo> employees) {
		this.employees = employees;
	}
	
	
	
}
