package com.vinsys.hrms.directonboard.vo;

/*
 * This class use for to response required flag which was return by "/validation" API.
 */

public class ValidationVO {
	private String isCandidateIdCreated;
	private String isEmployeeIdCreated;

	public String getIsCandidateIdCreated() {
		return isCandidateIdCreated;
	}

	public void setIsCandidateIdCreated(String isCandidateIdCreated) {
		this.isCandidateIdCreated = isCandidateIdCreated;
	}

	public String getIsEmployeeIdCreated() {
		return isEmployeeIdCreated;
	}

	public void setIsEmployeeIdCreated(String isEmployeeIdCreated) {
		this.isEmployeeIdCreated = isEmployeeIdCreated;
	}

}
