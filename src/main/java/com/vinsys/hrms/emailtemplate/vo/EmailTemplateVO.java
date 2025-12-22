package com.vinsys.hrms.emailtemplate.vo;


/**
 * Model to accept request
 * 
 * @author amey.gangakhedkar
 *
 */
public class EmailTemplateVO {

	private String emailTemplateName;

	private String emailTemplateValue;

	private String emailTemplateSubject;

	private Long customerId;

	public String getEmailTemplateName() {
		return emailTemplateName;
	}

	public void setEmailTemplateName(String emailTemplateName) {
		this.emailTemplateName = emailTemplateName;
	}

	public String getEmailTemplateValue() {
		return emailTemplateValue;
	}

	public void setEmailTemplateValue(String emailTemplateValue) {
		this.emailTemplateValue = emailTemplateValue;
	}

	public String getEmailTemplateSubject() {
		return emailTemplateSubject;
	}

	public void setEmailTemplateSubject(String emailTemplateSubject) {
		this.emailTemplateSubject = emailTemplateSubject;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

}
