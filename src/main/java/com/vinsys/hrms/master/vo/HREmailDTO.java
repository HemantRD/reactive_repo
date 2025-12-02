package com.vinsys.hrms.master.vo;

public class HREmailDTO {
	private Long id;
	private String firstName;
	private String lastName;
	private String officialEmailId;

	public HREmailDTO(Long id, String firstName, String lastName, String officialEmailId) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.officialEmailId = officialEmailId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getOfficialEmailId() {
		return officialEmailId;
	}

	public void setOfficialEmailId(String officialEmailId) {
		this.officialEmailId = officialEmailId;
	}

}
