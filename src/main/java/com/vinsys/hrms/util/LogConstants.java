package com.vinsys.hrms.util;

public enum LogConstants {

	ENTERED("############ Entered {} ###############"), EXITED("############ Exited {} ###############"),
	EXCEPTION("############ Exception {} ###############");

	private String message;

	private LogConstants(String message) {
		this.message = message;
	}

	public String template() {
		return message;
	}

}
