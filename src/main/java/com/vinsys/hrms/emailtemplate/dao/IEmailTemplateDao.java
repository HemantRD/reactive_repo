package com.vinsys.hrms.emailtemplate.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.emailtemplate.entity.EmailTemplateEntity;

public interface IEmailTemplateDao extends JpaRepository<EmailTemplateEntity, Integer>{
	
	public EmailTemplateEntity findByTemplateName(String templateName);

}
