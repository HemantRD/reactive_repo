package com.vinsys.hrms.dao.traveldesk;

import java.util.List;

import com.vinsys.hrms.datamodel.traveldesk.VOReportWonToCost;

public class VOReportWonToCostResponse {

	private String won;
	private double totalExpenditure;
	private String departmentName;
	private String bdName;
	private List<VOReportWonToCost> wonToCostList;
	
	public String getWon() {
		return won;
	}
	public void setWon(String won) {
		this.won = won;
	}
	
	public double getTotalExpenditure() {
		return totalExpenditure;
	}
	public void setTotalExpenditure(double totalExpenditure) {
		this.totalExpenditure = totalExpenditure;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getBdName() {
		return bdName;
	}
	public void setBdName(String bdName) {
		this.bdName = bdName;
	}
	public List<VOReportWonToCost> getWonToCostList() {
		return wonToCostList;
	}
	public void setWonToCostList(List<VOReportWonToCost> wonToCostList) {
		this.wonToCostList = wonToCostList;
	}
	
	
}
