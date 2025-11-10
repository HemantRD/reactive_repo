package com.vinsys.hrms.dashboard.vo;

import com.vinsys.hrms.datamodel.VOMasterBranch;
import com.vinsys.hrms.datamodel.VOMasterDivision;
import com.vinsys.hrms.datamodel.VOOrganization;

public class HolidayVO {

	private long id;
	private String holidayName;
	private String holidayDate;
	private long holidayYear;
	private String restricted;
	private String day;
	
	private VOOrganization organization;
	private VOMasterDivision division;
	private VOMasterBranch branch;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getHolidayName() {
		return holidayName;
	}
	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}
	public String getHolidayDate() {
		return holidayDate;
	}
	public void setHolidayDate(String holidayDate) {
		this.holidayDate = holidayDate;
	}
	public long getHolidayYear() {
		return holidayYear;
	}
	public void setHolidayYear(long holidayYear) {
		this.holidayYear = holidayYear;
	}
	public String getRestricted() {
		return restricted;
	}
	public void setRestricted(String restricted) {
		this.restricted = restricted;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
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
	
	
}
