package com.vinsys.hrms.kra.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.vinsys.hrms.entity.MasterRole;

@Entity
@Table(name = "tbl_mst_kra_cycle_calender")
public class KraCycleCalender {

	@Id
	@SequenceGenerator(name = "seq_mst_kra_cycle_calender", sequenceName = "seq_mst_kra_cycle_calender", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_kra_cycle_calender")
	@Column(name = "id", columnDefinition = "serial")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_date")
	private Date startDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_date")
	private Date endDate;
	
	
	@Column(name = "is_active")
	private String isActive;
	
	@Column(name = "current_phase")
	private String currentPhase;
	
	@ManyToOne
	@JoinColumn(name = "status")
	private KraCycleStatus status;
	
	@ManyToOne
	@JoinColumn(name = "cycle_id")
	private KraCycle cycleId;
	
	@ManyToOne
	@JoinColumn(name = "role_id")
	private MasterRole roleId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
	public String getCurrentPhase() {
		return currentPhase;
	}

	public void setCurrentPhase(String currentPhase) {
		this.currentPhase = currentPhase;
	}

	public KraCycleStatus getStatus() {
		return status;
	}

	public void setStatus(KraCycleStatus status) {
		this.status = status;
	}

	public KraCycle getCycleId() {
		return cycleId;
	}

	public void setCycleId(KraCycle cycleId) {
		this.cycleId = cycleId;
	}

	public MasterRole getRoleId() {
		return roleId;
	}

	public void setRoleId(MasterRole roleId) {
		this.roleId = roleId;
	}
	
	
	
	
	
}
