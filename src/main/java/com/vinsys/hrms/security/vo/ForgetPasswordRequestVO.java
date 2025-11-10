/**
 * 
 */
package com.vinsys.hrms.security.vo;

/**
 * @author Onkar A
 *
 * 
 */
public class ForgetPasswordRequestVO {
	private long employeeId;
	private String officialEmailId;
	private String dob;

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public String getOfficialEmailId() {
		return officialEmailId;
	}

	public void setOfficialEmailId(String officialEmailId) {
		this.officialEmailId = officialEmailId;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

}
