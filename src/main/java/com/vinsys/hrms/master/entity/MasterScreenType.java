package com.vinsys.hrms.master.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_mst_screen_type")
public class MasterScreenType extends AuditBase {

	@Id
	@SequenceGenerator(name = "seq_mst_screen_type", sequenceName = "seq_mst_screen_type", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_screen_type")
	@Column(name = "id")
	private int id;
	@Column(name = "screen_name")
	private String screenName;
	@Column(name = "role")
	private Long role;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public Long getRole() {
		return role;
	}

	public void setRole(Long role) {
		this.role = role;
	}

}
