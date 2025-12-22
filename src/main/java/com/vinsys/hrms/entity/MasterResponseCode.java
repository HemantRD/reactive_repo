package com.vinsys.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_mst_response_code")
public class MasterResponseCode extends AuditBase {

	@Id
	@Column(name = "entity_id")
	private Long entityId;

	@Column(name = "response_code")
	private Integer responseCode;

	@Column(name = "response_message")
	private String resposeMessage;

	@Column(name = "response_description")
	private String responseDescription;

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	public String getResposeMessage() {
		return resposeMessage;
	}

	public void setResposeMessage(String resposeMessage) {
		this.resposeMessage = resposeMessage;
	}

	public String getResponseDescription() {
		return responseDescription;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

}
