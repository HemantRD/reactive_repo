package com.vinsys.hrms.master.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;

/**
 * 
 * @author vidya.chandane
 *
 */

@Entity
@Table(name = "tbl_mst_reim_claim_category")
public class MasterClaimCategory extends AuditBase {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "claim_category")
	private String claimCategory;

	@Column(name = "description")
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClaimCategory() {
		return claimCategory;
	}

	public void setClaimCategory(String claimCategory) {
		this.claimCategory = claimCategory;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
