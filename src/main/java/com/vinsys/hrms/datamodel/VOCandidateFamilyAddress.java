package com.vinsys.hrms.datamodel;

import java.io.Serializable;

public class VOCandidateFamilyAddress extends VOAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private VOCandidateFamilyDetail candidateFamilyDetail;

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public VOCandidateFamilyDetail getCandidateFamilyDetail() {
	return candidateFamilyDetail;
    }

    public void setCandidateFamilyDetail(VOCandidateFamilyDetail candidateFamilyDetail) {
	this.candidateFamilyDetail = candidateFamilyDetail;
    }

}
