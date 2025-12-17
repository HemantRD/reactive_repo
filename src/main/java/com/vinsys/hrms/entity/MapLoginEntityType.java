package com.vinsys.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_map_login_entity_type")
public class MapLoginEntityType  {
	
	private static final long serialVersionUID = 1L;
	
	

	@SequenceGenerator(name = "seq_map_login_entity_type", sequenceName = "seq_map_login_entity_type", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_map_login_entity_type")
	
	@Id
	@Column(name = "login_entity_id")
	private Long loginEntityId;
	
	
	@Column(name = "login_entity_type_id")
	private Long loginEntityTypeId;
	
	@Column(name = "org_id")
	private Long orgId;


	public Long getLoginEntityId() {
		return loginEntityId;
	}


	public void setLoginEntityId(Long loginEntityId) {
		this.loginEntityId = loginEntityId;
	}


	public Long getLoginEntityTypeId() {
		return loginEntityTypeId;
	}


	public void setLoginEntityTypeId(Long loginEntityTypeId) {
		this.loginEntityTypeId = loginEntityTypeId;
	}


	public Long getOrgId() {
		return orgId;
	}


	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

}
