package com.vinsys.hrms.entity.confirmation;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.vinsys.hrms.entity.AuditBase;
import com.vinsys.hrms.entity.Employee;

@Entity
@SQLDelete(sql = "UPDATE tbl_probation_feedback SET is_active ='N' WHERE id=?")
@Where(clause = "is_active='Y' ")
@Table(name="tbl_probation_feedback")
public class ProbationFeedback extends AuditBase{
	
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "seq_probation_feedback", sequenceName = "seq_probation_feedback", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_probation_feedback")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "employee_id")
	private Employee employee;
	
	@Column(name="hr_comment")
	private String hrComment;
	
	@Column(name="manager_comment")
	private String managerComment;
	
	@Column(name="probation_status")
	private String status;
	
	@Column(name="hr_submitted")
	private Boolean hrSubmitted;
	
	@Column(name="ro_submitted")	///add boolean column to check p2c form Submitted by ro or not.
	private Boolean roSubmitted;

	@Column(name="extended_by_months")
	private Long extendedBy;
	
	@OneToMany(mappedBy = "feedback", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	//@JsonBackReference
	//@JsonManagedReference
	private List<ProbationParameter> probationParameter;


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

	public String getHrComment() {
		return hrComment;
	}

	public void setHrComment(String hrComment) {
		this.hrComment = hrComment;
	}

	public String getManagerComment() {
		return managerComment;
	}

	public void setManagerComment(String managerComment) {
		this.managerComment = managerComment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Boolean getHrSubmitted() {
		return hrSubmitted;
	}

	public void setHrSubmitted(Boolean hrSubmitted) {
		this.hrSubmitted = hrSubmitted;
	}
	
	//////add boolean setter and getter to get and set value of p2c form Submitted by ro or not.
	
	public Boolean getRoSubmitted() {
		return roSubmitted;
	}

	public void setRoSubmitted(Boolean roSubmitted) {
		this.roSubmitted = roSubmitted;
	}

	public Long getExtendedBy() {
		return extendedBy;
	}

	public void setExtendedBy(Long extendedBy) {
		this.extendedBy = extendedBy;
	}

	public List<ProbationParameter> getProbationParameter() {
		return probationParameter;
	}

	public void setProbationParameter(List<ProbationParameter> probationParameter) {
		this.probationParameter = probationParameter;
	}
	
}
