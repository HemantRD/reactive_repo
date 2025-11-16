package com.vinsys.hrms.datamodel;

import java.util.List;

public class VOSeparationLetterRequest extends VOAuditBase {
	
	private long id;
	private VOEmployee employee;
	private List<VOOrgDivSeparationLetter> voOrgDivSeparationLetters;
	
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
	public List<VOOrgDivSeparationLetter> getVoOrgDivSeparationLetters() {
		return voOrgDivSeparationLetters;
	}
	public void setVoOrgDivSeparationLetters(List<VOOrgDivSeparationLetter> voOrgDivSeparationLetters) {
		this.voOrgDivSeparationLetters = voOrgDivSeparationLetters;
	}

}
