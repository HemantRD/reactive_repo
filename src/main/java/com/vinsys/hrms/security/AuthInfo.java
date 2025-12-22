package com.vinsys.hrms.security;

import java.util.List;

/**
 * Any Claim Information should be added Here
 * 
 * @author Nilesh Devdas
 * @since 3.0
 * 
 */
public class AuthInfo {

	private Long employeeId;
	private List<String> roles;
	private Long candidateId;
	private String username;
	private Long orgId;

	public AuthInfo(Long employeeId, List<String> roles, Long candidateId, String username, List<Long> roleIds,
			Long orgId) {
		super();
		this.employeeId = employeeId;
		this.roles = roles;
		this.candidateId = candidateId;
		this.username = username;
		this.orgId = orgId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public Long getCandidateId() {
		return candidateId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public List<String> getRoles() {
		return roles;
	}

	public String getUsername() {
		return username;
	}

}
