package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.HtmlTemplate;

public interface IHRMSHtmlTemplateDAO extends JpaRepository<HtmlTemplate, Long> {

	public HtmlTemplate findBytemplateName(String name);

	public HtmlTemplate findBymasterCandidateActivity(long masterCandidateActivityId);

	@Query("select template from HtmlTemplate template where template.masterCandidateActivity.id = ?1 and template.templateType = ?2")
	public HtmlTemplate findByActivityIdAndType(long activityId, String type);
}
