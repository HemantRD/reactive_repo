package com.vinsys.hrms.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_map_separation_letter_template")
public class SeparationLetterTemplate extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_map_seperation_letter_template", sequenceName = "seq_map_seperation_letter_template", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_map_seperation_letter_template")
	private long id;
	@Column(name = "template_name")
	private String templateName;
	@Column(name = "template")
	private String template;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "org_div_seperation_letter_id")
	private MapOrgDivSeparationLetter mapOrgDivSeperationLetter;
	@Column(name = "template_type")
	private String templateType;
	@Column(name = "file_name")
	private String fileName;
	@Column(name = "email_subject")
	private String emailSubject;
	@Column(name = "logo_img_path")
	private String logoPath;
	@Column(name = "signature_img_path")
	private String signaturePath;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public MapOrgDivSeparationLetter getMapOrgDivSeperationLetter() {
		return mapOrgDivSeperationLetter;
	}
	public void setMapOrgDivSeperationLetter(MapOrgDivSeparationLetter mapOrgDivSeperationLetter) {
		this.mapOrgDivSeperationLetter = mapOrgDivSeperationLetter;
	}
	public String getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getEmailSubject() {
		return emailSubject;
	}
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}
	public String getLogoPath() {
		return logoPath;
	}
	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}
	public String getSignaturePath() {
		return signaturePath;
	}
	public void setSignaturePath(String signaturePath) {
		this.signaturePath = signaturePath;
	}

}
