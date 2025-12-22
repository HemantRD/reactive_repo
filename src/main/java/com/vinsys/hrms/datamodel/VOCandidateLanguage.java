package com.vinsys.hrms.datamodel;

public class VOCandidateLanguage extends VOAuditBase {

	private long id;
	private VOCandidatePersonalDetail candidatePersonalDetail;
	private String languageName;
	private String speak;
	private String read;
	private String write;
	private String motherTongue;
	private VOMasterLanguage language;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VOCandidatePersonalDetail getCandidatePersonalDetail() {
		return candidatePersonalDetail;
	}

	public void setCandidatePersonalDetail(VOCandidatePersonalDetail candidatePersonalDetail) {
		this.candidatePersonalDetail = candidatePersonalDetail;
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public String getSpeak() {
		return speak;
	}

	public void setSpeak(String speak) {
		this.speak = speak;
	}

	public String getRead() {
		return read;
	}

	public void setRead(String read) {
		this.read = read;
	}

	public String getWrite() {
		return write;
	}

	public void setWrite(String write) {
		this.write = write;
	}

	public String getMotherTongue() {
		return motherTongue;
	}

	public void setMotherTongue(String motherTongue) {
		this.motherTongue = motherTongue;
	}

	public VOMasterLanguage getLanguage() {
		return language;
	}

	public void setLanguage(VOMasterLanguage language) {
		this.language = language;
	}

}
