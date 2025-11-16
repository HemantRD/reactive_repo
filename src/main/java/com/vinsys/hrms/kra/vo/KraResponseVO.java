package com.vinsys.hrms.kra.vo;

public class KraResponseVO {

	private Long id;
	private Long cycleId;	
	private String cycleName;
	private Long cycleType;
	private String empName;
	private String kraYear;
	private String selfRating;
	private String managerRating;
	//private float rmRating;
	private String finalRating;
	private String lastYearRating;
	//private float hodRating;
	private String mcMemberRating;
	//private float hrRating;
	private String status;
	private String department;
	private Long empId;
	private String rmKraActionEnable;
	private String empKraActionEnable;
	private String CountryName;
	private String isHrCalibrated;
	private String isfinalcalibrate;
	private String correctionComment;
	private String hcdCorrectionComment;
	private String calibrationComment;
	private String grade;
	private String pendingWith;
	private String employeeCode;
	private String delegationTo;
	private String designation;
	private String reportingManager;
	private String role;
	private Long delegationId;
	private String isFunctionHeadSubmit;
    private String rmAiComment;
    private String fdhAiComment;
    private String hodAiComment;
    private String hrAiComment;
    private String calibrateAiComment;

    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCycleId() {
		return cycleId;
	}
	public void setCycleId(Long cycleId) {
		this.cycleId = cycleId;
	}
	public String getCycleName() {
		return cycleName;
	}
	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getKraYear() {
		return kraYear;
	}
	public void setKraYear(String kraYear) {
		this.kraYear = kraYear;
	}
	public String getSelfRating() {
		return selfRating;
	}
	public void setSelfRating(String selfRating) {
		this.selfRating = selfRating;
	}
	public String getManagerRating() {
		return managerRating;
	}
	public void setManagerRating(String managerRating) {
		this.managerRating = managerRating;
	}

	/*
	 * public float getRmRating() { return rmRating; } public void setRmRating(float
	 * rmRating) { this.rmRating = rmRating; }
	 */
	public String getFinalRating() {
		return finalRating;
	}
	public void setFinalRating(String finalRating) {
		this.finalRating = finalRating;
	}
	public String getLastYearRating() {
		return lastYearRating;
	}
	public void setLastYearRating(String lastYearRating) {
		this.lastYearRating = lastYearRating;
	}

	/*
	 * public float getHodRating() { return hodRating; } public void
	 * setHodRating(float hodRating) { this.hodRating = hodRating; } public float
	 * getHrRating() { return hrRating; } public void setHrRating(float hrRating) {
	 * this.hrRating = hrRating; }
	 */
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public Long getEmpId() {
		return empId;
	}
	public void setEmpId(Long empId) {
		this.empId = empId;
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
	public String getCountryName() {
		return CountryName;
	}
	public void setCountryName(String countryName) {
		CountryName = countryName;
	}
	public String getIsHrCalibrated() {
		return isHrCalibrated;
	}
	public void setIsHrCalibrated(String isHrCalibrated) {
		this.isHrCalibrated = isHrCalibrated;
	}
	public String getCorrectionComment() {
		return correctionComment;
	}
	public void setCorrectionComment(String correctionComment) {
		this.correctionComment = correctionComment;
	}
	public String getCalibrationComment() {
		return calibrationComment;
	}
	public void setCalibrationComment(String calibrationComment) {
		this.calibrationComment = calibrationComment;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getPendingWith() {
		return pendingWith;
	}
	public void setPendingWith(String pendingWith) {
		this.pendingWith = pendingWith;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getIsfinalcalibrate() {
		return isfinalcalibrate;
	}
	public void setIsfinalcalibrate(String isfinalcalibrate) {
		this.isfinalcalibrate = isfinalcalibrate;
	}
	public String getHcdCorrectionComment() {
		return hcdCorrectionComment;
	}
	public void setHcdCorrectionComment(String hcdCorrectionComment) {
		this.hcdCorrectionComment = hcdCorrectionComment;
	}
	public String getDelegationTo() {
		return delegationTo;
	}
	public void setDelegationTo(String delegationTo) {
		this.delegationTo = delegationTo;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getReportingManager() {
		return reportingManager;
	}
	public void setReportingManager(String reportingManager) {
		this.reportingManager = reportingManager;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Long getDelegationId() {
		return delegationId;
	}
	public void setDelegationId(Long delegationId) {
		this.delegationId = delegationId;
	}
	public String getMcMemberRating() {
		return mcMemberRating;
	}
	public void setMcMemberRating(String mcMemberRating) {
		this.mcMemberRating = mcMemberRating;
	}
	public Long getCycleType() {
		return cycleType;
	}
	public void setCycleType(Long cycleType) {
		this.cycleType = cycleType;
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

}
