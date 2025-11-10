package com.vinsys.hrms.reimbursement.exception;

public class InvalidInputException extends Exception{
	
	private String message;

	public InvalidInputException() {
	}

	public InvalidInputException(String message) {
		super();
		this.message = message;
	}

}
