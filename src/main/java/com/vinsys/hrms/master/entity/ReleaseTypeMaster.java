package com.vinsys.hrms.master.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;

/**
 * @author Onkar A
 *
 * 
 */
@Entity
@Table(name = "tbl_mst_release_type")
public class ReleaseTypeMaster extends AuditBase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "release_type")
	private String releaseType;

	@Column(name = "value")
	private String value;
	@Column(name = "release_date_mandt")
	private String releaseDateMandatory;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReleaseType() {
		return releaseType;
	}

	public void setReleaseType(String releaseType) {
		this.releaseType = releaseType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getReleaseDateMandatory() {
		return releaseDateMandatory;
	}

	public void setReleaseDateMandatory(String releaseDateMandatory) {
		this.releaseDateMandatory = releaseDateMandatory;
	}
}
