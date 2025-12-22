package com.vinsys.hrms.kra.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

//@JsonInclude(JsonInclude.Include.NON_NULL)
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
	private List<KraCategoryVo> category;
	private String rmAiComment;
	private String fdhAiComment;
	private String hodAiComment;
	private String hrAiComment;
	private String calibrateAiComment;

	private String isEmployeeDownload;
	private String isLineManagerDownload;
	private String isFunctionHeadDownload ;
	private String isHcdDownload ;
	private String isHodDownload ;
	
	private String functionSpecific;
	private String coreCompetencies;
	private String leadershipCompetencies;
	private String overallSummary;
	private String categoryWeight;
	private String selfAggregate;
	private String isKpiFormPublished;
	
	private String empFunctionalAvg;
	private String empCoreAvg;
	private String empLeadershipAvg;
	private String managerFunctionalAvg;
	private String managerCoreAvg;
	private String managerLeadershipAvg;
	

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

	public String getRmAiComment() {
		return rmAiComment;
	}

	public void setRmAiComment(String rmAiComment) {
		this.rmAiComment = rmAiComment;
	}

	public String getFdhAiComment() {
		return fdhAiComment;
	}

	public void setFdhAiComment(String fdhAiComment) {
		this.fdhAiComment = fdhAiComment;
	}

	public String getHodAiComment() {
		return hodAiComment;
	}

	public void setHodAiComment(String hodAiComment) {
		this.hodAiComment = hodAiComment;
	}

	public String getHrAiComment() {
		return hrAiComment;
	}

	public void setHrAiComment(String hrAiComment) {
		this.hrAiComment = hrAiComment;
	}

	public String getCalibrateAiComment() {
		return calibrateAiComment;
	}

	public void setCalibrateAiComment(String calibrateAiComment) {
		this.calibrateAiComment = calibrateAiComment;
	}

	public String getIsEmployeeDownload() {
		return isEmployeeDownload;
	}

	public void setIsEmployeeDownload(String isEmployeeDownload) {
		this.isEmployeeDownload = isEmployeeDownload;
	}

	public String getIsLineManagerDownload() {
		return isLineManagerDownload;
	}

	public void setIsLineManagerDownload(String isLineManagerDownload) {
		this.isLineManagerDownload = isLineManagerDownload;
	}

	public String getIsFunctionHeadDownload() {
		return isFunctionHeadDownload;
	}

	public void setIsFunctionHeadDownload(String isFunctionHeadDownload) {
		this.isFunctionHeadDownload = isFunctionHeadDownload;
	}

	public String getIsHcdDownload() {
		return isHcdDownload;
	}

	public void setIsHcdDownload(String isHcdDownload) {
		this.isHcdDownload = isHcdDownload;
	}

	public String getIsHodDownload() {
		return isHodDownload;
	}

	public void setIsHodDownload(String isHodDownload) {
		this.isHodDownload = isHodDownload;
	}

	public String getFunctionSpecific() {
		return functionSpecific;
	}

	public void setFunctionSpecific(String functionSpecific) {
		this.functionSpecific = functionSpecific;
	}

	public String getCoreCompetencies() {
		return coreCompetencies;
	}

	public void setCoreCompetencies(String coreCompetencies) {
		this.coreCompetencies = coreCompetencies;
	}

	public String getLeadershipCompetencies() {
		return leadershipCompetencies;
	}

	public void setLeadershipCompetencies(String leadershipCompetencies) {
		this.leadershipCompetencies = leadershipCompetencies;
	}

	public String getOverallSummary() {
		return overallSummary;
	}

	public void setOverallSummary(String overallSummary) {
		this.overallSummary = overallSummary;
	}

	public String getCategoryWeight() {
		return categoryWeight;
	}

	public void setCategoryWeight(String categoryWeight) {
		this.categoryWeight = categoryWeight;
	}

	public String getSelfAggregate() {
		return selfAggregate;
	}

	public void setSelfAggregate(String selfAggregate) {
		this.selfAggregate = selfAggregate;
	}

	public String getIsKpiFormPublished() {
		return isKpiFormPublished;
	}

	public void setIsKpiFormPublished(String isKpiFormPublished) {
		this.isKpiFormPublished = isKpiFormPublished;
	}

	public String getEmpFunctionalAvg() {
		return empFunctionalAvg;
	}

	public void setEmpFunctionalAvg(String empFunctionalAvg) {
		this.empFunctionalAvg = empFunctionalAvg;
	}

	public String getEmpCoreAvg() {
		return empCoreAvg;
	}

	public void setEmpCoreAvg(String empCoreAvg) {
		this.empCoreAvg = empCoreAvg;
	}

	public String getEmpLeadershipAvg() {
		return empLeadershipAvg;
	}

	public void setEmpLeadershipAvg(String empLeadershipAvg) {
		this.empLeadershipAvg = empLeadershipAvg;
	}

	public String getManagerFunctionalAvg() {
		return managerFunctionalAvg;
	}

	public void setManagerFunctionalAvg(String managerFunctionalAvg) {
		this.managerFunctionalAvg = managerFunctionalAvg;
	}

	public String getManagerCoreAvg() {
		return managerCoreAvg;
	}

	public void setManagerCoreAvg(String managerCoreAvg) {
		this.managerCoreAvg = managerCoreAvg;
	}

	public String getManagerLeadershipAvg() {
		return managerLeadershipAvg;
	}

	public void setManagerLeadershipAvg(String managerLeadershipAvg) {
		this.managerLeadershipAvg = managerLeadershipAvg;
	}

	
	
	
}
