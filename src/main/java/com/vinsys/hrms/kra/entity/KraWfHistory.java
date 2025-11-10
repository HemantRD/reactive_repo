package com.vinsys.hrms.kra.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;
import com.vinsys.hrms.entity.Employee;

@Entity
@Table(name = "tbl_trn_kra_wf_history")
public class KraWfHistory extends AuditBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_trn_kra_wf_history", sequenceName = "seq_trn_kra_wf_history", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trn_kra_wf_history")
	private Long id;

	@Column(name = "status")
	private String status;

	@Column(name = "pending_with")
	private String pendingWith;

	@ManyToOne
	@JoinColumn(name = "kra_id")
	private Kra kra;
	
	@ManyToOne
	@JoinColumn(name = "mst_kra_cycle_id")
	private KraCycle cycleId;
	
	@Column(name = "comment")
	private String comment;

	@Column(name = "hcd_correction")
	private String hcdCorrection;
	
	@ManyToOne
	@JoinColumn(name = "delegated_to")
	private Employee delegatedTo;
	
	@Column(name = "kra_wf_id")
	private Long kraWfId;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPendingWith() {
		return pendingWith;
	}

	public void setPendingWith(String pendingWith) {
		this.pendingWith = pendingWith;
	}

	public Kra getKra() {
		return kra;
	}

	public void setKra(Kra kra) {
		this.kra = kra;
	}

	public KraCycle getCycleId() {
		return cycleId;
	}

	public void setCycleId(KraCycle cycleId) {
		this.cycleId = cycleId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getHcdCorrection() {
		return hcdCorrection;
	}

	public void setHcdCorrection(String hcdCorrection) {
		this.hcdCorrection = hcdCorrection;
	}

	public Employee getDelegatedTo() {
		return delegatedTo;
	}

	public void setDelegatedTo(Employee delegatedTo) {
		this.delegatedTo = delegatedTo;
	}

	public Long getKraWfId() {
		return kraWfId;
	}

	public void setKraWfId(Long kraWfId) {
		this.kraWfId = kraWfId;
	}

}
