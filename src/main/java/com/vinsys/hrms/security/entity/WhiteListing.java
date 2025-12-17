package com.vinsys.hrms.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table()
public class WhiteListing extends AuditBase {

	/**
	 * WhiteListing
	 */
	private static final long serialVersionUID = -4736906578808667793L;

	@Column(name = "entity_id")
	@Id
	private Long entityId;
	@Column(name = "url")
	private String url;
	@Column(name = "description")
	private String description;

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}
}
