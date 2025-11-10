package com.vinsys.hrms.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="tbl_trn_employee_separation_details")
public class EmployeeSeparationDetails extends AuditBase{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_trn_employee_separation_details", sequenceName = "seq_trn_employee_separation_details", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trn_employee_separation_details")
	private long id;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee;
	
	@Column(name = "resignation_date")
	@Temporal(TemporalType.DATE)
	private Date resignationDate;
	
	@Column(name = "actual_relieving_date")
	@Temporal(TemporalType.DATE)
	private Date actualRelievingDate;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "mode_of_separation_id", nullable = false)
	private MasterModeofSeparation modeofSeparation;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "emp_separation_reason_id", nullable = false)
	private MasterModeofSeparationReason empSeparationReason;
	
	/*@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "notice_period_id", nullable = false)
	private MasterOrg_NoticePeriod noticePeriod;*/
	
	
	@Column(name = "notice_period")
	private int noticeperiod;
	
	@Column(name = "employee_comment")
	private String employeeComment;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "ro_reason")
	private MasterModeofSeparationReason roReason;
	
	@Column(name = "ro_comment")
	private String roComment;
	
	@Column(name = "ro_action_date")
	@Temporal(TemporalType.DATE)
	private Date roActionDate;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "org_reason")
	private MasterModeofSeparationReason orgReason;
	
	@Column(name = "org_comment")
	private String orgComment;
	
	@Column(name = "org_action_date")
	@Temporal(TemporalType.DATE)
	private Date orgActionDate;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "hr_reason")
	private MasterModeofSeparationReason HRReason;
	
	@Column(name = "HR_comment")
	private String HRComment;
	
	@Column(name = "hr_action_date")
	@Temporal(TemporalType.DATE)
	private Date hrActionDate;

	@Column(name = "ro_approver_status")
	private String roApproverStatus;
	
	@Column(name = "org_approver_status")
	private String orgApproverStatus;
	
	@Column(name = "hr_approver_status")
	private String hrApproverStatus;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "system_escalated_level")
	private String systemEscalatedLevel;
	
	@Column(name = "emp_action")
	private String employeeAction;
	
	@Column(name = "emp_wd_comment")
	private String empWdComment;
	
	@Column(name = "emp_Wd_date")
	@Temporal(TemporalType.DATE)
	private Date empWdDate;
	
	@Column(name = "ro_Wd_comment")
	private String ROWdComment;
	
	@Column(name = "ro_Wd_action_date")
	@Temporal(TemporalType.DATE)
	private Date ROWdActionDate;
	
	@Column(name = "ro_Wd_Action_Status")
	private String ROWdActionStatus;
	
	@Column(name = "org_Wd_comment")
	private String org_level_emp_WdComment;
	
	@Column(name = "org_Wd_action_date")
	@Temporal(TemporalType.DATE)
	private Date org_level_emp_Wd_action_Date;
	
	@Column(name = "org_Wd_action_status")
	private String org_level_emp_WdActionStatus;
	
	@Column(name = "hr_Wd_comment")
	private String hr_WdComment;
	
	@Column(name = "hr_Wd_action_date")
	@Temporal(TemporalType.DATE)
	private Date hr_WdActionDate;
	
	@Column(name = "hr_Wd_action_status")
	private String hr_WdActionStatus;
	
	@Column(name = "separation_proof")
	private String separationProof;
	
	@Column(name = "early_release")
	private String earlyRelease;

	@Column(name = "release_date_by_ro")
	private Date releaseDateByRo;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Date getResignationDate() {
		return resignationDate;
	}

	public void setResignationDate(Date resignationDate) {
		this.resignationDate = resignationDate;
	}

	public Date getActualRelievingDate() {
		return actualRelievingDate;
	}

	public void setActualRelievingDate(Date actualRelievingDate) {
		this.actualRelievingDate = actualRelievingDate;
	}

	public MasterModeofSeparation getModeofSeparation() {
		return modeofSeparation;
	}

	public void setModeofSeparation(MasterModeofSeparation modeofSeparation) {
		this.modeofSeparation = modeofSeparation;
	}

	/*public MasterOrg_NoticePeriod getNoticePeriod() {
		return noticePeriod;
	}*/

	public MasterModeofSeparationReason getEmpSeparationReason() {
		return empSeparationReason;
	}

	public void setEmpSeparationReason(MasterModeofSeparationReason empSeparationReason) {
		this.empSeparationReason = empSeparationReason;
	}

	/*public void setNoticePeriod(MasterOrg_NoticePeriod noticePeriod) {
		this.noticePeriod = noticePeriod;
	}
*/
	public String getEmployeeComment() {
		return employeeComment;
	}

	public void setEmployeeComment(String employeeComment) {
		this.employeeComment = employeeComment;
	}

	public MasterModeofSeparationReason getRoReason() {
		return roReason;
	}

	public void setRoReason(MasterModeofSeparationReason roReason) {
		this.roReason = roReason;
	}

	public String getRoComment() {
		return roComment;
	}

	public void setRoComment(String roComment) {
		this.roComment = roComment;
	}

	public MasterModeofSeparationReason getOrgReason() {
		return orgReason;
	}

	public void setOrgReason(MasterModeofSeparationReason orgReason) {
		this.orgReason = orgReason;
	}

	public String getOrgComment() {
		return orgComment;
	}

	public void setOrgComment(String orgComment) {
		this.orgComment = orgComment;
	}

	public MasterModeofSeparationReason getHRReason() {
		return HRReason;
	}

	public void setHRReason(MasterModeofSeparationReason hRReason) {
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
	
	public Date getRoActionDate() {
		return roActionDate;
	}

	public void setRoActionDate(Date roActionDate) {
		this.roActionDate = roActionDate;
	}

	public Date getOrgActionDate() {
		return orgActionDate;
	}

	public void setOrgActionDate(Date orgActionDate) {
		this.orgActionDate = orgActionDate;
	}

	public Date getHrActionDate() {
		return hrActionDate;
	}

	public void setHrActionDate(Date hrActionDate) {
		this.hrActionDate = hrActionDate;
	}

	public String getEmployeeAction() {
		return employeeAction;
	}

	public void setEmployeeAction(String employeeAction) {
		this.employeeAction = employeeAction;
	}

	public int getNoticeperiod() {
		return noticeperiod;
	}

	public void setNoticeperiod(int noticeperiod) {
		this.noticeperiod = noticeperiod;
	}

	public String getEmpWdComment() {
		return empWdComment;
	}

	public void setEmpWdComment(String empWdComment) {
		this.empWdComment = empWdComment;
	}

	public Date getEmpWdDate() {
		return empWdDate;
	}

	public void setEmpWdDate(Date empWdDate) {
		this.empWdDate = empWdDate;
	}

	public String getROWdComment() {
		return ROWdComment;
	}

	public void setROWdComment(String rOWdComment) {
		ROWdComment = rOWdComment;
	}

	public Date getROWdActionDate() {
		return ROWdActionDate;
	}

	public void setROWdActionDate(Date rOWdActionDate) {
		ROWdActionDate = rOWdActionDate;
	}

	public String getROWdActionStatus() {
		return ROWdActionStatus;
	}

	public void setROWdActionStatus(String rOWdActionStatus) {
		ROWdActionStatus = rOWdActionStatus;
	}

	public String getOrg_level_emp_WdComment() {
		return org_level_emp_WdComment;
	}

	public void setOrg_level_emp_WdComment(String org_level_emp_WdComment) {
		this.org_level_emp_WdComment = org_level_emp_WdComment;
	}

	public Date getOrg_level_emp_Wd_action_Date() {
		return org_level_emp_Wd_action_Date;
	}

	public void setOrg_level_emp_Wd_action_Date(Date org_level_emp_Wd_action_Date) {
		this.org_level_emp_Wd_action_Date = org_level_emp_Wd_action_Date;
	}

	public String getOrg_level_emp_WdActionStatus() {
		return org_level_emp_WdActionStatus;
	}

	public void setOrg_level_emp_WdActionStatus(String org_level_emp_WdActionStatus) {
		this.org_level_emp_WdActionStatus = org_level_emp_WdActionStatus;
	}

	public String getHr_WdComment() {
		return hr_WdComment;
	}

	public void setHr_WdComment(String hr_WdComment) {
		this.hr_WdComment = hr_WdComment;
	}

	public Date getHr_WdActionDate() {
		return hr_WdActionDate;
	}

	public void setHr_WdActionDate(Date hr_WdActionDate) {
		this.hr_WdActionDate = hr_WdActionDate;
	}

	public String getHr_WdActionStatus() {
		return hr_WdActionStatus;
	}

	public void setHr_WdActionStatus(String hr_WdActionStatus) {
		this.hr_WdActionStatus = hr_WdActionStatus;
	}

	public String getSeparationProof() {
		return separationProof;
	}

	public void setSeparationProof(String separationProof) {
		this.separationProof = separationProof;
	}

	public String getEarlyRelease() {
		return earlyRelease;
	}

	public void setEarlyRelease(String earlyRelease) {
		this.earlyRelease = earlyRelease;
	}

	public Date getReleaseDateByRo() {
		return releaseDateByRo;
	}

	public void setReleaseDateByRo(Date releaseDateByRo) {
		this.releaseDateByRo = releaseDateByRo;
	}
	
	

}
