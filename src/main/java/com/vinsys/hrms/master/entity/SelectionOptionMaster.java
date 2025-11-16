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
 * @author vidya.chandane this entity is created for selection options ,
 *         dropdwon - Yes and No
 */
@Entity
@Table(name = "tbl_mst_select_options")
public class SelectionOptionMaster extends AuditBase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "select_options")
	private String selectionOptions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSelectionOptions() {
		return selectionOptions;
	}

	public void setSelectionOptions(String selectionOptions) {
		this.selectionOptions = selectionOptions;
	}

}
