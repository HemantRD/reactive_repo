package com.vinsys.hrms.datamodel.attendance;

import com.vinsys.hrms.datamodel.VOEmployee;

public class VOEmployeeACN {

	private long id;
	private VOEmployee employee;
	private long empACN;
	private boolean isManagement;
	private boolean isFlexible;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VOEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(VOEmployee employee) {
		this.employee = employee;
	}

	public long getEmpACN() {
		return empACN;
	}

	public void setEmpACN(long empACN) {
		this.empACN = empACN;
	}

	public boolean isManagement() {
		return isManagement;
	}

	public void setManagement(boolean isManagement) {
		this.isManagement = isManagement;
	}

	public boolean isFlexible() {
		return isFlexible;
	}

	public void setFlexible(boolean isFlexible) {
		this.isFlexible = isFlexible;
	}

}
