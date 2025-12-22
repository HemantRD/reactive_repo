package com.vinsys.hrms.entity.traveldesk;

public class BPMDetails {

	private static final long serialVersionUID = 1L;

	private long id;
	private String businessUnit;
	private String clientName;
	public String bdName;
	private String training_start_date;
	private String training_end_date;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getBdName() {
		return bdName;
	}

	public void setBdName(String bdName) {
		this.bdName = bdName;
	}

	public String getTraining_start_date() {
		return training_start_date;
	}

	public void setTraining_start_date(String training_start_date) {
		this.training_start_date = training_start_date;
	}

	public String getTraining_end_date() {
		return training_end_date;
	}

	public void setTraining_end_date(String training_end_date) {
		this.training_end_date = training_end_date;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}