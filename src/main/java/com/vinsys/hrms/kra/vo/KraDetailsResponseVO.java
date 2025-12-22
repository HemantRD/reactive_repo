package com.vinsys.hrms.kra.vo;

import java.util.List;

public class KraDetailsResponseVO {
	private Long kraId;
	private List<KraDetailsVO> kraDetailsVOList;
	private String pendingWith;
	private String status;
	private String employeeName;
	private Float totalWeightage;
	private String isEmployeeKraSubmitted;
	private String actionByRM;
	private String rmKraActionEnable;
	private String empKraActionEnable;
	private String activeCycleName;
	private String kpiFormCycle;

	public List<KraDetailsVO> getKraDetailsVOList() {
		return kraDetailsVOList;
	}

	public void setKraDetailsVOList(List<KraDetailsVO> kraDetailsVOList) {
		this.kraDetailsVOList = kraDetailsVOList;
	}

	public String getPendingWith() {
		return pendingWith;
	}

	public void setPendingWith(String pendingWith) {
		this.pendingWith = pendingWith;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Long getKraId() {
		return kraId;
	}

	public void setKraId(Long kraId) {
		this.kraId = kraId;
	}

	public Float getTotalWeightage() {
		return totalWeightage;
	}

	public void setTotalWeightage(Float totalWeightage) {
		this.totalWeightage = totalWeightage;
	}

	public String getIsEmployeeKraSubmitted() {
		return isEmployeeKraSubmitted;
	}

	public void setIsEmployeeKraSubmitted(String isEmployeeKraSubmitted) {
		this.isEmployeeKraSubmitted = isEmployeeKraSubmitted;
	}

	public String getActionByRM() {
		return actionByRM;
	}

	public void setActionByRM(String actionByRM) {
		this.actionByRM = actionByRM;
	}

	public String getRmKraActionEnable() {
		return rmKraActionEnable;
	}

	public void setRmKraActionEnable(String rmKraActionEnable) {
		this.rmKraActionEnable = rmKraActionEnable;
	}

	public String getEmpKraActionEnable() {
		return empKraActionEnable;
	}

	public void setEmpKraActionEnable(String empKraActionEnable) {
		this.empKraActionEnable = empKraActionEnable;
	}

	public String getActiveCycleName() {
		return activeCycleName;
	}

	public void setActiveCycleName(String activeCycleName) {
		this.activeCycleName = activeCycleName;
	}

	public String getKpiFormCycle() {
		return kpiFormCycle;
	}

	public void setKpiFormCycle(String kpiFormCycle) {
		this.kpiFormCycle = kpiFormCycle;
	}
	
}
