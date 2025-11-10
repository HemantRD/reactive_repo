/**
 * @author Onkar A.
 */
package com.vinsys.hrms.constants;

public enum EResponse {
	SUCCESS(200, "SUCCESS"), FAILED(500, "FAILED"), UNAUTHORIZED(401, "UNAUTHORIZED"),
	UNAUTHENTICATED(403, "UNAUTHENTICATED"), 
	ERROR_INSUFFICIENT_DATA(501, "Error:Insufficient Data"), DATA_NOT_FOUND(502, "Data not found!"),
	INAVALID_DATA(503, "Inavalid Data !");

	private int code;
	private String message;

	private EResponse(int code, String message) {
		this.code = code;
		this.message = message;
	}

	EResponse(int l) {
		this.code = l;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
