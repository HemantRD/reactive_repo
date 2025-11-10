package com.vinsys.hrms.kra.vo;

import java.util.List;

public class NewKraResponse {

	private String finalRating;
	private String isEmployeeSubmit;
	private String isManagerSubmit;
	private String isFunctionHeadSubmit;
	private String lastYearRating;
	private String isObjectiveSubmit;
	private String isHcdApproved;
	private String ismanagerApproved;
	private String isHcdCorrect;
	private String isManagerObjectives;
	private String isEmployeeAccept;
	private String isDelegated;
	private String isReportingManager;
	private String totalWeight;
	private String cycleName;
	private String activeCycleName;
	private String formCycleName;
	private Long formCycleType;
	private String totalSelfRating;
	private String totalManagerRating;
	private Long cycleType;
	private Long activeCycleType;

	public Long getCycleType() {
		return cycleType;
	}

	public void setCycleType(Long cycleType) {
		this.cycleType = cycleType;
	}

	public String getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(String totalWeight) {
		this.totalWeight = totalWeight;
	}

	private List<KraCategoryVo> category;

	public List<KraCategoryVo> getCategory() {
		return category;
	}

	public void setCategory(List<KraCategoryVo> category) {
		this.category = category;
	}

	public String getFinalRating() {
		return finalRating;
	}

	public void setFinalRating(String finalRating) {
		this.finalRating = finalRating;
	}

	public String getIsEmployeeSubmit() {
		return isEmployeeSubmit;
	}

	public void setIsEmployeeSubmit(String isEmployeeSubmit) {
		this.isEmployeeSubmit = isEmployeeSubmit;
	}

	public String getIsManagerSubmit() {
		return isManagerSubmit;
	}

	public void setIsManagerSubmit(String isManagerSubmit) {
		this.isManagerSubmit = isManagerSubmit;
	}

	public String getLastYearRating() {
		return lastYearRating;
	}

	public void setLastYearRating(String lastYearRating) {
		this.lastYearRating = lastYearRating;
	}

	public String getIsObjectiveSubmit() {
		return isObjectiveSubmit;
	}

	public void setIsObjectiveSubmit(String isObjectiveSubmit) {
		this.isObjectiveSubmit = isObjectiveSubmit;
	}

	public String getIsHcdApproved() {
		return isHcdApproved;
	}

	public void setIsHcdApproved(String isHcdApproved) {
		this.isHcdApproved = isHcdApproved;
	}

	public String getIsHcdCorrect() {
		return isHcdCorrect;
	}

	public void setIsHcdCorrect(String isHcdCorrect) {
		this.isHcdCorrect = isHcdCorrect;
	}

	public String getIsmanagerApproved() {
		return ismanagerApproved;
	}

	public void setIsmanagerApproved(String ismanagerApproved) {
		this.ismanagerApproved = ismanagerApproved;
	}

	public String getIsEmployeeAccept() {
		return isEmployeeAccept;
	}

	public void setIsEmployeeAccept(String isEmployeeAccept) {
		this.isEmployeeAccept = isEmployeeAccept;
	}

	public String getCycleName() {
		return cycleName;
	}

	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}

	public String getIsManagerObjectives() {
		return isManagerObjectives;
	}

	public void setIsManagerObjectives(String isManagerObjectives) {
		this.isManagerObjectives = isManagerObjectives;
	}

	public String getIsDelegated() {
		return isDelegated;
	}

	public void setIsDelegated(String isDelegated) {
		this.isDelegated = isDelegated;
	}

	public String getIsReportingManager() {
		return isReportingManager;
	}

	public void setIsReportingManager(String isReportingManager) {
		this.isReportingManager = isReportingManager;
	}

	public String getActiveCycleName() {
		return activeCycleName;
	}

	public void setActiveCycleName(String activeCycleName) {
		this.activeCycleName = activeCycleName;
	}

	public String getFormCycleName() {
		return formCycleName;
	}

	public void setFormCycleName(String formCycleName) {
		this.formCycleName = formCycleName;
	}

	public String getTotalSelfRating() {
		return totalSelfRating;
	}

	public void setTotalSelfRating(String totalSelfRating) {
		this.totalSelfRating = totalSelfRating;
	}

	public String getTotalManagerRating() {
		return totalManagerRating;
	}

	public void setTotalManagerRating(String totalManagerRating) {
		this.totalManagerRating = totalManagerRating;
	}

	public Long getActiveCycleType() {
		return activeCycleType;
	}

	public void setActiveCycleType(Long activeCycleType) {
		this.activeCycleType = activeCycleType;
	}

	public Long getFormCycleType() {
		return formCycleType;
	}

	public void setFormCycleType(Long formCycleType) {
		this.formCycleType = formCycleType;
	}

	public String getIsFunctionHeadSubmit() {
		return isFunctionHeadSubmit;
	}

	public void setIsFunctionHeadSubmit(String isFunctionHeadSubmit) {
		this.isFunctionHeadSubmit = isFunctionHeadSubmit;
	}

}
