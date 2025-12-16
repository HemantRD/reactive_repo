package com.vinsys.hrms.employee.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Onkar A
 *
 * 
 */
public class EmployeeSeprationDetailsResponseVO {

	private Long id;
	private Long employeeId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date resignationDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date relievingDate;
	private Integer noticePeriod;
	private String resignationReason;
	private String employeeComment;

	// manager/ro
	private String empCancelComment;
	private String roReason;
	private String roComment;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date roActionDate;
	// org
	private String orgReason;
	private String orgComment;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date orgActionDate;
	// hr
	private String hrReason;
	private String hrComment;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date hrActionDate;

	private String roApproverStatus;
	private String orgApproverStatus;
	private String hrApproverStatus;
	private String status;
	private String systemEscalatedLevel;
	private String employeeAction;

	//private String empWdComment;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date empWdDate;
	private String roWdComment;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date roWdActionDate;
	private String roWdAction;
	private String orgLevelEmpWdComment;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date orgLevelEmpWdActionDate;
	private String orgLevelEmpWdAction;
	private String hrWdComment;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date hrWdActionDate;
	private String hrWdAction;
	private String employeeName;
	private String roApproverName;
	private String hrApproverName;
	private String isFeedbackFormdisplay;
	
	
	
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Date getResignationDate() {
		return resignationDate;
	}

	public void setResignationDate(Date resignationDate) {
		this.resignationDate = resignationDate;
	}

	public Date getRelievingDate() {
		return relievingDate;
	}

	public void setRelievingDate(Date relievingDate) {
		this.relievingDate = relievingDate;
	}

	public Integer getNoticePeriod() {
		return noticePeriod;
	}

	public void setNoticePeriod(Integer noticePeriod) {
		this.noticePeriod = noticePeriod;
	}

	public String getResignationReason() {
		return resignationReason;
	}

	public void setResignationReason(String resignationReason) {
		this.resignationReason = resignationReason;
	}

	public String getEmployeeComment() {
		return employeeComment;
	}

	public void setEmployeeComment(String employeeComment) {
		this.employeeComment = employeeComment;
	}

	public String getRoReason() {
		return roReason;
	}

	public void setRoReason(String roReason) {
		this.roReason = roReason;
	}

	public String getRoComment() {
		return roComment;
	}

	public void setRoComment(String roComment) {
		this.roComment = roComment;
	}

	public Date getRoActionDate() {
		return roActionDate;
	}

	public void setRoActionDate(Date roActionDate) {
		this.roActionDate = roActionDate;
	}

	public String getOrgReason() {
		return orgReason;
	}

	public void setOrgReason(String orgReason) {
		this.orgReason = orgReason;
	}

	public String getOrgComment() {
		return orgComment;
	}

	public void setOrgComment(String orgComment) {
		this.orgComment = orgComment;
	}

	public Date getOrgActionDate() {
		return orgActionDate;
	}

	public void setOrgActionDate(Date orgActionDate) {
		this.orgActionDate = orgActionDate;
	}

	public String getHrReason() {
		return hrReason;
	}

	public void setHrReason(String hrReason) {
		this.hrReason = hrReason;
	}

	public String getHrComment() {
		return hrComment;
	}

	public void setHrComment(String hrComment) {
		this.hrComment = hrComment;
	}

	public Date getHrActionDate() {
		return hrActionDate;
	}

	public void setHrActionDate(Date hrActionDate) {
		this.hrActionDate = hrActionDate;
	}

	public String getRoApproverStatus() {
		return roApproverStatus;
	}

	public void setRoApproverStatus(String roApproverStatus) {
		this.roApproverStatus = roApproverStatus;
	}

	public String getOrgApproverStatus() {
		return orgApproverStatus;
	}

	public void setOrgApproverStatus(String orgApproverStatus) {
		this.orgApproverStatus = orgApproverStatus;
	}

	public String getHrApproverStatus() {
		return hrApproverStatus;
	}

	public void setHrApproverStatus(String hrApproverStatus) {
		this.hrApproverStatus = hrApproverStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSystemEscalatedLevel() {
		return systemEscalatedLevel;
	}

	public void setSystemEscalatedLevel(String systemEscalatedLevel) {
		this.systemEscalatedLevel = systemEscalatedLevel;
	}

	public String getEmployeeAction() {
		return employeeAction;
	}

	public void setEmployeeAction(String employeeAction) {
		this.employeeAction = employeeAction;
	}

//	public String getEmpWdComment() {
//		return empWdComment;
//	}
//
//	public void setEmpWdComment(String empWdComment) {
//		this.empWdComment = empWdComment;
//	}

	public Date getEmpWdDate() {
		return empWdDate;
	}

	public void setEmpWdDate(Date empWdDate) {
		this.empWdDate = empWdDate;
	}

	public String getRoWdComment() {
		return roWdComment;
	}

	public void setRoWdComment(String roWdComment) {
		this.roWdComment = roWdComment;
	}

	public Date getRoWdActionDate() {
		return roWdActionDate;
	}

	public void setRoWdActionDate(Date roWdActionDate) {
		this.roWdActionDate = roWdActionDate;
	}

	public String getRoWdAction() {
		return roWdAction;
	}

	public void setRoWdAction(String roWdAction) {
		this.roWdAction = roWdAction;
	}

	public String getOrgLevelEmpWdComment() {
		return orgLevelEmpWdComment;
	}

	public void setOrgLevelEmpWdComment(String orgLevelEmpWdComment) {
		this.orgLevelEmpWdComment = orgLevelEmpWdComment;
	}

	public Date getOrgLevelEmpWdActionDate() {
		return orgLevelEmpWdActionDate;
	}

	public void setOrgLevelEmpWdActionDate(Date orgLevelEmpWdActionDate) {
		this.orgLevelEmpWdActionDate = orgLevelEmpWdActionDate;
	}

	public String getOrgLevelEmpWdAction() {
		return orgLevelEmpWdAction;
	}

	public void setOrgLevelEmpWdAction(String orgLevelEmpWdAction) {
		this.orgLevelEmpWdAction = orgLevelEmpWdAction;
	}

	public String getHrWdComment() {
		return hrWdComment;
	}

	public void setHrWdComment(String hrWdComment) {
		this.hrWdComment = hrWdComment;
	}

	public Date getHrWdActionDate() {
		return hrWdActionDate;
	}

	public void setHrWdActionDate(Date hrWdActionDate) {
		this.hrWdActionDate = hrWdActionDate;
	}

	public String getHrWdAction() {
		return hrWdAction;
	}

	public void setHrWdAction(String hrWdAction) {
		this.hrWdAction = hrWdAction;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmpCancelComment() {
		return empCancelComment;
	}

	public void setEmpCancelComment(String empCancelComment) {
		this.empCancelComment = empCancelComment;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoApproverName() {
		return roApproverName;
	}

	public void setRoApproverName(String roApproverName) {
		this.roApproverName = roApproverName;
	}

	public String getHrApproverName() {
		return hrApproverName;
	}

	public void setHrApproverName(String hrApproverName) {
		this.hrApproverName = hrApproverName;
	}

	public String getIsFeedbackFormdisplay() {
		return isFeedbackFormdisplay;
	}

	public void setIsFeedbackFormdisplay(String isFeedbackFormdisplay) {
		this.isFeedbackFormdisplay = isFeedbackFormdisplay;
	}
	
	
}
