package com.vinsys.hrms.datamodel;

import java.util.Date;

import com.vinsys.hrms.entity.MasterCandidateOnboardActionReason;

public class VOCandidateActivityActionDetail extends VOAuditBase {

	private long id;
	private VOCandidate candidate;
	private String typeOfAction;
	private String hrComment;
	private Date hrActedOn;
	private MasterCandidateOnboardActionReason masterCandidateOnboardActionReason;

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

	public String getHrComment() {
		return hrComment;
	}

	public void setHrComment(String hrComment) {
		this.hrComment = hrComment;
	}

	public Date getHrActedOn() {
		return hrActedOn;
	}

	public void setHrActedOn(Date hrActedOn) {
		this.hrActedOn = hrActedOn;
	}

	public String getTypeOfAction() {
		return typeOfAction;
	}

	public void setTypeOfAction(String typeOfAction) {
		this.typeOfAction = typeOfAction;
	}

	public MasterCandidateOnboardActionReason getMasterCandidateOnboardActionReason() {
		return masterCandidateOnboardActionReason;
	}

	public void setMasterCandidateOnboardActionReason(
			MasterCandidateOnboardActionReason masterCandidateOnboardActionReason) {
		this.masterCandidateOnboardActionReason = masterCandidateOnboardActionReason;
	}

}
