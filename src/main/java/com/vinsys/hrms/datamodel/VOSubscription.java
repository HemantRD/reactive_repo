package com.vinsys.hrms.datamodel;

import java.util.Date;

public class VOSubscription {

    private Long id;
    private VOOrganization organization;
    private String subscriptionType;
    private Date startDate;
    private Date endDate;
    private String subscriptionKey;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public VOOrganization getOrganization() {
	return organization;
    }

    public void setOrganization(VOOrganization organization) {
	this.organization = organization;
    }

    public String getSubscriptionType() {
	return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
	this.subscriptionType = subscriptionType;
    }

    public Date getStartDate() {
	return startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    public Date getEndDate() {
	return endDate;
    }

    public void setEndDate(Date endDate) {
	this.endDate = endDate;
    }

    public String getSubscriptionKey() {
	return subscriptionKey;
    }

    public void setSubscriptionKey(String subscriptionKey) {
	this.subscriptionKey = subscriptionKey;
    }

}
