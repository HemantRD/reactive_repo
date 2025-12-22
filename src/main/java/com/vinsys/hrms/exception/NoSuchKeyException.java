package com.vinsys.hrms.exception;

public class NoSuchKeyException extends RuntimeException {
	private static final long serialVersionUID = -1854496426373825295L;
 
	public NoSuchKeyException(String message) {
		super(message);
	}
}