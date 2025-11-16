package com.vinsys.hrms.employee.vo;

import com.vinsys.hrms.master.vo.ModeOfSeparationVO;
import com.vinsys.hrms.master.vo.ReleaseTypeVO;
import com.vinsys.hrms.master.vo.SeparationReasonVO;

import io.swagger.v3.oas.annotations.media.Schema;

public class EmployeeSeparationRequestVO {

	private long id;

	private String actualRelievingDate;
	@Schema(required = true)
	private ModeOfSeparationVO modeofSeparation;

	@Schema(required = true)
	private String employeeComment;
	@Schema(required = true)
	private SeparationReasonVO separationReasonVO;
	private String roComment;
	private String orgComment;
	private String empCancelComment;
	private String orgReason;

	// manager will set release date and release type
	ReleaseTypeVO releaseTypeVO;
	String releaseDateByRo;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmployeeComment() {
		return employeeComment;
	}

	public void setEmployeeComment(String employeeComment) {
		this.employeeComment = employeeComment;
	}

	public SeparationReasonVO getSeparationReasonVO() {
		return separationReasonVO;
	}

	public void setSeparationReasonVO(SeparationReasonVO separationReasonVO) {
		this.separationReasonVO = separationReasonVO;
	}

	public ModeOfSeparationVO getModeofSeparation() {
		return modeofSeparation;
	}

	public void setModeofSeparation(ModeOfSeparationVO modeofSeparation) {
		this.modeofSeparation = modeofSeparation;
	}

	public String getEmpCancelComment() {
		return empCancelComment;
	}

	public void setEmpCancelComment(String empCancelComment) {
		this.empCancelComment = empCancelComment;
	}

	public String getRoComment() {
		return roComment;
	}

	public void setRoComment(String roComment) {
		this.roComment = roComment;
	}

	public String getOrgComment() {
		return orgComment;
	}

	public void setOrgComment(String orgComment) {
		this.orgComment = orgComment;
	}

	public String getActualRelievingDate() {
		return actualRelievingDate;
	}

	public void setActualRelievingDate(String actualRelievingDate) {
		this.actualRelievingDate = actualRelievingDate;
	}

	public ReleaseTypeVO getReleaseTypeVO() {
		return releaseTypeVO;
	}

	public void setReleaseTypeVO(ReleaseTypeVO releaseTypeVO) {
		this.releaseTypeVO = releaseTypeVO;
	}

	public String getReleaseDateByRo() {
		return releaseDateByRo;
	}

	public void setReleaseDateByRo(String releaseDateByRo) {
		this.releaseDateByRo = releaseDateByRo;
	}

	public String getOrgReason() {
		return orgReason;
	}

	public void setOrgReason(String orgReason) {
		this.orgReason = orgReason;
	}
	
	

}
