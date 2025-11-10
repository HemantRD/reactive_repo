package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.EmailTemplate;
import com.vinsys.hrms.entity.MasterDivision;
import com.vinsys.hrms.entity.Organization;

public interface IHRMSEmailTemplateDAO extends JpaRepository<EmailTemplate, Long> {

	public EmailTemplate findBytemplateNameAndIsActiveAndDivisionAndOrganization(String name, String isActive, MasterDivision division, Organization organization );
}
