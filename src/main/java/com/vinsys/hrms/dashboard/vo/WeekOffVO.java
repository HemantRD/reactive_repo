package com.vinsys.hrms.dashboard.vo;

import com.vinsys.hrms.datamodel.VOMasterBranch;
import com.vinsys.hrms.datamodel.VOMasterDepartment;
import com.vinsys.hrms.datamodel.VOMasterDivision;
import com.vinsys.hrms.datamodel.VOOrganization;

public class WeekOffVO {

	
	private long weekNumber;
	private String weekOffDays;
	private VOOrganization organization;
	private VOMasterDivision division;
	private VOMasterBranch branch;
	private VOMasterDepartment department;
	
	public long getWeekNumber() {
		return weekNumber;
	}
	public void setWeekNumber(long weekNumber) {
		this.weekNumber = weekNumber;
	}
	public String getWeekOffDays() {
		return weekOffDays;
	}
	public void setWeekOffDays(String weekOffDays) {
		this.weekOffDays = weekOffDays;
	}
	public VOOrganization getOrganization() {
		return organization;
	}
	public void setOrganization(VOOrganization organization) {
		this.organization = organization;
	}
	public VOMasterDivision getDivision() {
		return division;
	}
	public void setDivision(VOMasterDivision division) {
		this.division = division;
	}
	public VOMasterBranch getBranch() {
		return branch;
	}
	public void setBranch(VOMasterBranch branch) {
		this.branch = branch;
	}
	public VOMasterDepartment getDepartment() {
		return department;
	}
	public void setDepartment(VOMasterDepartment department) {
		this.department = department;
	}
	
	
}
