package com.vinsys.hrms.email.vo;

/**
 * 
 * @author amey.gangakhedkar
 *
 */
public class EmailRequestVO {

	private String emailAddress;
	private TemplateVO templateVo;
	private String filename;
	private Long customerId;
	private String emailCategory;
	private String actionName;

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getEmailCategory() {
		return emailCategory;
	}

	public void setEmailCategory(String emailCategory) {
		this.emailCategory = emailCategory;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public TemplateVO getTemplateVo() {
		return templateVo;
	}

	public void setTemplateVo(TemplateVO templateVo) {
		this.templateVo = templateVo;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	

}
