package com.vinsys.hrms.master.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_mst_travel_approver_slab")
public class MasterTravelApproverSlab extends AuditBase{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long slabId;
	
	@Column(name="min_amount")
	private float minAmount;
	
	@Column(name="max_amount")
	private float maxAmount;
	
	public Long getSlabId() {
		return slabId;
	}

	public void setSlabId(Long slabId) {
		this.slabId = slabId;
	}


	public float getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(float minAmount) {
		this.minAmount = minAmount;
	}

	public float getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(float maxAmount) {
		this.maxAmount = maxAmount;
	}
	
	
	
}
