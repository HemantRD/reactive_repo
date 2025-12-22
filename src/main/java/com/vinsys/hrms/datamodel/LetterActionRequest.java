package com.vinsys.hrms.datamodel;

import com.vinsys.hrms.entity.CandidateLetter;

public class LetterActionRequest extends VOAuditBase{

	private String action;
	private CandidateLetter letter;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public CandidateLetter getLetter() {
		return letter;
	}

	public void setLetter(CandidateLetter letter) {
		this.letter = letter;
	}

}
