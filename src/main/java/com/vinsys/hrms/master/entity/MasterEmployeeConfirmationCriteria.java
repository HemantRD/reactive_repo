package com.vinsys.hrms.master.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_mst_emp_confirmation_criteria")
public class MasterEmployeeConfirmationCriteria extends AuditBase {

	@Id
	@Column(name = "entity_id")
	private Long entityId;
	@Column(name = "status")
	private String status;
	@Column(name = "min_percentage")
	private long minimumPercentage;
	@Column(name = "max_percentage")
	private long maximumPercentage;

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getMinimumPercentage() {
		return minimumPercentage;
	}

	public void setMinimumPercentage(long minimumPercentage) {
		this.minimumPercentage = minimumPercentage;
	}

	public long getMaximumPercentage() {
		return maximumPercentage;
	}

	public void setMaximumPercentage(long maximumPercentage) {
		this.maximumPercentage = maximumPercentage;
	}

}
