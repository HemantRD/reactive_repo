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

import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_mst_kra_cycle_history")
public class KraCycleHistory extends AuditBase {

	@Id
	@SequenceGenerator(name = "seq_mst_kra_cycle_history", sequenceName = "seq_mst_kra_cycle_history", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_kra_cycle_history")
	private Long id;
	@Column(name = "description")
	private String description;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "start_date")
	private Date startDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "end_date")
	private Date endDate;
	
	@Column(name = "cycle_name")
	private String cycleName;
	
	@ManyToOne
	@JoinColumn(name = "status")
	private KraCycleStatus status;
	
	@ManyToOne
	@JoinColumn(name = "year")
	private KraYear year;
	
	@Column(name = "is_locked")
	private String isLocked;
	
	@Column(name ="cycle_tbl_id")
	private Long cycleTblId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	

	public KraCycleStatus getStatus() {
		return status;
	}

	public void setStatus(KraCycleStatus status) {
		this.status = status;
	}

	

	/*
	 * public String getIsActive() { return isActive; }
	 * 
	 * public void setIsActive(String isActive) { this.isActive = isActive; }
	 */


	public String getCycleName() {
		return cycleName;
	}

	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}

	/*
	 * public Long getOrgId() { return orgId; }
	 * 
	 * public void setOrgId(Long orgId) { this.orgId = orgId; }
	 */

	public KraYear getYear() {
		return year;
	}

	public void setYear(KraYear year) {
		this.year = year;
	}

	public String getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(String isLocked) {
		this.isLocked = isLocked;
	}

	public Long getCycleTblId() {
		return cycleTblId;
	}

	public void setCycleTblId(Long cycleTblId) {
		this.cycleTblId = cycleTblId;
	}
	
	
	
}
