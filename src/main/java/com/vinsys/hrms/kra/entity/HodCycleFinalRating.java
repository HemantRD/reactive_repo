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
import com.vinsys.hrms.master.entity.KraRating;

@Entity
@Table(name = "tbl_mst_hod_cycle_final_rating")
public class HodCycleFinalRating extends AuditBase{
	
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_hod_final_rating", sequenceName = "seq_hod_final_rating", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_hod_final_rating")
	private Long id;

		
	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;
	
	@ManyToOne
	@JoinColumn(name = "mc_member_id")
	private Employee mcMemberId;
	
	@ManyToOne
	@JoinColumn(name = "cycle_id")
	private KraCycle cycleId;

	
	@ManyToOne
	@JoinColumn(name = "score")
	private KraRating score;
	
	@Column(name = "comment")
	private String comment;
	
	@ManyToOne()
	@JoinColumn(name = "kra_id")
	private Kra kra;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Employee getMcMemberId() {
		return mcMemberId;
	}

	public void setMcMemberId(Employee mcMemberId) {
		this.mcMemberId = mcMemberId;
	}

	public KraCycle getCycleId() {
		return cycleId;
	}

	public void setCycleId(KraCycle cycleId) {
		this.cycleId = cycleId;
	}


	public KraRating getScore() {
		return score;
	}

	public void setScore(KraRating score) {
		this.score = score;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Kra getKra() {
		return kra;
	}

	public void setKra(Kra kra) {
		this.kra = kra;
	}
	
	
}
