package com.vinsys.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_mst_extension_type")
public class MasterExtensionType extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_mst_extension_type", sequenceName = "seq_mst_extension_type", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_extension_type")
	private Long id;

	@Column(name = "extension_type")
	private String extensionType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getExtensionType() {
		return extensionType;
	}

	public void setExtensionType(String extensionType) {
		this.extensionType = extensionType;
	}

}
