package com.vinsys.hrms.exception;

/*
 * 
 * THIS IS USER DEFINED EXCEPTION CLASS
 * IT IS BEING USED TO THROW EXCEPTION 
 * 
 * */

public class HRMSException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String responseMessage ;
	private int responseCode;

	public HRMSException() {

	}

	public int getResponseCode() {
		return this.responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	
	public HRMSException(String responseMessage) {
		super(responseMessage);
		this.setResponseMessage(responseMessage);
	}

	public HRMSException(int responseCode, String responseMessage) {
		super(responseMessage);

		this.setResponseMessage(responseMessage);
		this.responseCode = responseCode;
	}

	public HRMSException(Throwable cause) {
		super(cause);
	}

	public HRMSException(String responseMessage, Throwable cause) {
		super(responseMessage, cause);
		this.setResponseMessage(responseMessage);
	}

	public HRMSException(String responseMessage, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(responseMessage, cause, enableSuppression, writableStackTrace);
		this.setResponseMessage(responseMessage);
	}

	public static Throwable getRootCause(Throwable throwable) {
		if (throwable.getCause() != null)
			return getRootCause(throwable.getCause());

		return throwable;
	}

	
}
