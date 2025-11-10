package com.vinsys.hrms.datamodel;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vinsys.hrms.entity.AuditBase;

public class VOCandidateOverseasExperience extends AuditBase {

    private static final long serialVersionUID = 1L;
    private long id;
    private VOCandidateProfessionalDetail candidateProfessionalDetail;
    private String company;
    private String project;
    @JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date fromDate;
    @JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date toDate;
    private String location;
    private String duration;
    private String responsibility;

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public VOCandidateProfessionalDetail getCandidateProfessionalDetail() {
	return candidateProfessionalDetail;
    }

    public void setCandidateProfessionalDetail(VOCandidateProfessionalDetail candidateProfessionalDetail) {
	this.candidateProfessionalDetail = candidateProfessionalDetail;
    }

    public String getCompany() {
	return company;
    }

    public void setCompany(String company) {
	this.company = company;
    }

    public String getProject() {
	return project;
    }

    public void setProject(String project) {
	this.project = project;
    }

    public Date getFromDate() {
	return fromDate;
    }

    public void setFromDate(Date fromDate) {
	this.fromDate = fromDate;
    }

    public Date getToDate() {
	return toDate;
    }

    public void setToDate(Date toDate) {
	this.toDate = toDate;
    }

    public String getLocation() {
	return location;
    }

    public void setLocation(String location) {
	this.location = location;
    }

    public String getDuration() {
	return duration;
    }

    public void setDuration(String duration) {
	this.duration = duration;
    }

    public String getResponsibility() {
	return responsibility;
    }

    public void setResponsibility(String responsibility) {
	this.responsibility = responsibility;
    }

}
