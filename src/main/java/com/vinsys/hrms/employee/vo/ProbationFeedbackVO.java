package com.vinsys.hrms.employee.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Onkar A
 *
 * 
 */
public class ProbationFeedbackVO {

	private Long id;
	@Schema(required = true, description = "Only when HR save feedback")
	private String hrComment;
	@Schema(required = true, description = "Only when manager save feedback")
	private String managerComment;
	private Boolean managerSubmitted;
	private String status;
	private Boolean hrSubmitted;
	@Schema(required = true, description = "Only when HR save feedback")
	private Long extendedBy;

	@Schema(required = true, description = "Only when HR/Manager savefeed back")
	private Long employeeId;
	private String employeeName;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date confirmationDueDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Kolkata")
	private Date dateOfJoinig;
	private String department;
	private String designation;
	private Boolean isFormProbationEnable;

	@Schema(required = true)
	private List<ProbationParameterVO> probationParameter;

	private float managerRatingPercentage;
	private float empRatingPercentage;
//	private String actionTakenBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHrComment() {
		return hrComment;
	}

	public void setHrComment(String hrComment) {
		this.hrComment = hrComment;
	}

	public String getManagerComment() {
		return managerComment;
	}

	public void setManagerComment(String managerComment) {
		this.managerComment = managerComment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getHrSubmitted() {
		return hrSubmitted;
	}

	public void setHrSubmitted(Boolean hrSubmitted) {
		this.hrSubmitted = hrSubmitted;
	}

	public Long getExtendedBy() {
		return extendedBy;
	}

	public void setExtendedBy(Long extendedBy) {
		this.extendedBy = extendedBy;
	}

	public List<ProbationParameterVO> getProbationParameter() {
		return probationParameter;
	}

	public void setProbationParameter(List<ProbationParameterVO> probationParameter) {
		this.probationParameter = probationParameter;
	}

//	public String getActionTakenBy() {
//		return actionTakenBy;
//	}
//
//	public void setActionTakenBy(String actionTakenBy) {
//		this.actionTakenBy = actionTakenBy;
//	}

	public Boolean getManagerSubmitted() {
		return managerSubmitted;
	}

	public void setManagerSubmitted(Boolean roSubmitted) {
		this.managerSubmitted = roSubmitted;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Date getConfirmationDueDate() {
		return confirmationDueDate;
	}

	public void setConfirmationDueDate(Date confirmationDueDate) {
		this.confirmationDueDate = confirmationDueDate;
	}

	public Date getDateOfJoinig() {
		return dateOfJoinig;
	}

	public void setDateOfJoinig(Date dateOfJoinig) {
		this.dateOfJoinig = dateOfJoinig;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public float getManagerRatingPercentage() {
		return managerRatingPercentage;
	}

	public void setManagerRatingPercentage(float managerRatingPercentage) {
		this.managerRatingPercentage = managerRatingPercentage;
	}

	public float getEmpRatingPercentage() {
		return empRatingPercentage;
	}

	public void setEmpRatingPercentage(float empRatingPercentage) {
		this.empRatingPercentage = empRatingPercentage;
	}

	

	public Boolean getIsFormProbationEnable() {
		return isFormProbationEnable;
	}

	public void setIsFormProbationEnable(Boolean isFormProbationEnable) {
		this.isFormProbationEnable = isFormProbationEnable;
	}

}
