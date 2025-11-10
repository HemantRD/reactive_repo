package com.vinsys.hrms.util;

/**
 * @author Onkar A
 *
 * 
 */
public class LoginRequestVO {

	//private String username;
	//private String password;
	private String msalidtoken;
	private String currentVersion;
	private String gpsLogin;

//	public String getUsername() {
//		return username;
//	}
//
//	public void setUsername(String username) {
//		this.username = username;
//	}

//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}

	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}

	public String getGpsLogin() {
		return gpsLogin;
	}

	public void setGpsLogin(String gpsLogin) {
		this.gpsLogin = gpsLogin;
	}

	public String getMsalidtoken() {
		return msalidtoken;
	}

	public void setMsalidtoken(String msalidtoken) {
		this.msalidtoken = msalidtoken;
	}
}
