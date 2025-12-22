package com.vinsys.hrms.datamodel;

import java.io.Serializable;
import java.util.Date;

public class VOCandidateActivity extends VOAuditBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private VOCandidate candidate;
	private VOMasterCandidateActivity masterCandidateActivity;
	private String emailStatus;
	private String hrStatus;
	private String hrComment;
	private Date activityTriggredDate;
	private Date activityResponseDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VOCandidate getCandidate() {
		return candidate;
	}

	public void setCandidate(VOCandidate candidate) {
		this.candidate = candidate;
	}

	public VOMasterCandidateActivity getMasterCandidateActivity() {
		return masterCandidateActivity;
	}

	public void setMasterCandidateActivity(VOMasterCandidateActivity masterCandidateActivity) {
		this.masterCandidateActivity = masterCandidateActivity;
	}

	public String getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}

	public String getHrStatus() {
		return hrStatus;
	}

	public void setHrStatus(String hrStatus) {
		this.hrStatus = hrStatus;
	}

	public String getHrComment() {
		return hrComment;
	}

	public void setHrComment(String hrComment) {
		this.hrComment = hrComment;
	}

	public Date getActivityTriggredDate() {
		return activityTriggredDate;
	}

	public void setActivityTriggredDate(Date activityTriggredDate) {
		this.activityTriggredDate = activityTriggredDate;
	}

	public Date getActivityResponseDate() {
		return activityResponseDate;
	}

	public void setActivityResponseDate(Date activityResponseDate) {
		this.activityResponseDate = activityResponseDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
