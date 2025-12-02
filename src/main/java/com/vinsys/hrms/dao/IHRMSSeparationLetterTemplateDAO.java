package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.SeparationLetterTemplate;

public interface IHRMSSeparationLetterTemplateDAO extends JpaRepository<SeparationLetterTemplate, Long> {

	@Query(" SELECT sepLetterTemp from SeparationLetterTemplate sepLetterTemp "
			+ " WHERE sepLetterTemp.mapOrgDivSeperationLetter.id = ?1 "
			+ " AND sepLetterTemp.templateType = ?2 AND sepLetterTemp.isActive = ?3 ")
	public SeparationLetterTemplate getSeperationLetterTemplateById(long orgDivSepLetterId, String templateType,
			String isActive);
}
