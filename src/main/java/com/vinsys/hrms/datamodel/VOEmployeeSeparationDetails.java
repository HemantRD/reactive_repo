package com.vinsys.hrms.datamodel;

public class VOEmployeeSeparationDetails extends VOAuditBase {

	private long id;
	private VOEmployee employee;
	private String resignationDate;

	private String actualRelievingDate;
	private VOMasterModeofSeparation modeofSeparation;
	private VOMasterModeofSeparationReason empseparationReason;
	// private VOMasterNoticePeriod noticePeriod;
	private int noticePeriod;
	private String employeeComment;
	private VOMasterModeofSeparationReason roReason;
	private String roComment;
	private String roActionDate;
	private VOMasterModeofSeparationReason orgReason;
	private String orgComment;
	private String orgActionDate;
	private VOMasterModeofSeparationReason HRReason;
	private String HRComment;
	private String hrActionDate;
	private String roApproverStatus;
	private String orgApproverStatus;
	private String hrApproverStatus;
	private String status;
	private String systemEscalatedLevel;
	private String employeeAction;

	private String empWdComment;
	private String empWdDate;
	private String ROWdComment;
	private String ROWdActionDate;
	private String ROWdAction;
	private String org_level_emp_WdComment;
	private String org_level_emp_Wd_action_Date;
	private String org_level_emp_WdAction;
	private String hr_WdComment;
	private String hr_WdActionDate;
	private String hr_WdAction;
	private String approval_status;
	private String loggedInEmployee;
	private String showactualRelievingDate;
	private String showresign_date_display;
	
	public String getApproval_status() {
		return approval_status;
	}
	public void setApproval_status(String approval_status) {
		this.approval_status = approval_status;
	}
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

	public String getResignationDate() {
		return resignationDate;
	}

	public void setResignationDate(String resignationDate) {
		this.resignationDate = resignationDate;
	}

	public String getActualRelievingDate() {
		return actualRelievingDate;
	}

	public void setActualRelievingDate(String actualRelievingDate) {
		this.actualRelievingDate = actualRelievingDate;
	}

	public VOMasterModeofSeparation getModeofSeparation() {
		return modeofSeparation;
	}

	public void setModeofSeparation(VOMasterModeofSeparation modeofSeparation) {
		this.modeofSeparation = modeofSeparation;
	}

	public VOMasterModeofSeparationReason getEmpseparationReason() {
		return empseparationReason;
	}

	public void setEmpseparationReason(VOMasterModeofSeparationReason empseparationReason) {
		this.empseparationReason = empseparationReason;
	}

	/*
	 * public VOMasterNoticePeriod getNoticePeriod() { return noticePeriod; } public
	 * void setNoticePeriod(VOMasterNoticePeriod noticePeriod) { this.noticePeriod =
	 * noticePeriod; }
	 */
	public String getEmployeeComment() {
		return employeeComment;
	}

	public void setEmployeeComment(String employeeComment) {
		this.employeeComment = employeeComment;
	}

	public VOMasterModeofSeparationReason getRoReason() {
		return roReason;
	}

	public void setRoReason(VOMasterModeofSeparationReason roReason) {
		this.roReason = roReason;
	}

	public String getRoComment() {
		return roComment;
	}

	public void setRoComment(String roComment) {
		this.roComment = roComment;
	}

	public VOMasterModeofSeparationReason getOrgReason() {
		return orgReason;
	}

	public void setOrgReason(VOMasterModeofSeparationReason orgReason) {
		this.orgReason = orgReason;
	}

	public String getOrgComment() {
		return orgComment;
	}

	public void setOrgComment(String orgComment) {
		this.orgComment = orgComment;
	}

	public VOMasterModeofSeparationReason getHRReason() {
		return HRReason;
	}

	public void setHRReason(VOMasterModeofSeparationReason hRReason) {
		HRReason = hRReason;
	}

	public String getHRComment() {
		return HRComment;
	}

	public void setHRComment(String hRComment) {
		HRComment = hRComment;
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

	public String getRoActionDate() {
		return roActionDate;
	}

	public void setRoActionDate(String roActionDate) {
		this.roActionDate = roActionDate;
	}

	public String getOrgActionDate() {
		return orgActionDate;
	}

	public void setOrgActionDate(String orgActionDate) {
		this.orgActionDate = orgActionDate;
	}

	public String getHrActionDate() {
		return hrActionDate;
	}

	public void setHrActionDate(String hrActionDate) {
		this.hrActionDate = hrActionDate;
	}

	public String getEmployeeAction() {
		return employeeAction;
	}

	public void setEmployeeAction(String employeeAction) {
		this.employeeAction = employeeAction;
	}

	public int getNoticePeriod() {
		return noticePeriod;
	}

	public void setNoticePeriod(int noticePeriod) {
		this.noticePeriod = noticePeriod;
	}

	public String getEmpWdComment() {
		return empWdComment;
	}

	public void setEmpWdComment(String empWdComment) {
		this.empWdComment = empWdComment;
	}

	public String getEmpWdDate() {
		return empWdDate;
	}

	public void setEmpWdDate(String empWdDate) {
		this.empWdDate = empWdDate;
	}

	public String getROWdComment() {
		return ROWdComment;
	}

	public void setROWdComment(String rOWdComment) {
		ROWdComment = rOWdComment;
	}

	public String getROWdActionDate() {
		return ROWdActionDate;
	}

	public void setROWdActionDate(String rOWdActionDate) {
		ROWdActionDate = rOWdActionDate;
	}

	public String getROWdAction() {
		return ROWdAction;
	}

	public void setROWdAction(String rOWdAction) {
		ROWdAction = rOWdAction;
	}

	public String getOrg_level_emp_WdComment() {
		return org_level_emp_WdComment;
	}

	public void setOrg_level_emp_WdComment(String org_level_emp_WdComment) {
		this.org_level_emp_WdComment = org_level_emp_WdComment;
	}

	public String getOrg_level_emp_Wd_action_Date() {
		return org_level_emp_Wd_action_Date;
	}

	public void setOrg_level_emp_Wd_action_Date(String org_level_emp_Wd_action_Date) {
		this.org_level_emp_Wd_action_Date = org_level_emp_Wd_action_Date;
	}

	public String getOrg_level_emp_WdAction() {
		return org_level_emp_WdAction;
	}

	public void setOrg_level_emp_WdAction(String org_level_emp_WdAction) {
		this.org_level_emp_WdAction = org_level_emp_WdAction;
	}

	public String getHr_WdComment() {
		return hr_WdComment;
	}

	public void setHr_WdComment(String hr_WdComment) {
		this.hr_WdComment = hr_WdComment;
	}

	public String getHr_WdActionDate() {
		return hr_WdActionDate;
	}

	public void setHr_WdActionDate(String hr_WdActionDate) {
		this.hr_WdActionDate = hr_WdActionDate;
	}

	public String getHr_WdAction() {
		return hr_WdAction;
	}

	public void setHr_WdAction(String hr_WdAction) {
		this.hr_WdAction = hr_WdAction;
	}
	
	public String getLoggedInEmployee() {
		return loggedInEmployee;
	}
	
	public void setLoggedInEmployee(String loggedInEmployee) {
		this.loggedInEmployee = loggedInEmployee;
	}
	public String getShowactualRelievingDate() {
		return showactualRelievingDate;
	}
	public void setShowactualRelievingDate(String showactualRelievingDate) {
		this.showactualRelievingDate = showactualRelievingDate;
	}
	public String getShowresign_date_display() {
		return showresign_date_display;
	}
	public void setShowresign_date_display(String showresign_date_display) {
		this.showresign_date_display = showresign_date_display;
	}
	
	
	
	

}
