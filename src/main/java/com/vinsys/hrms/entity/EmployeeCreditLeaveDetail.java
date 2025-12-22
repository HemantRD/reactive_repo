package com.vinsys.hrms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tbl_employee_credit_leave_detail")
public class EmployeeCreditLeaveDetail extends AuditBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_employee_credit_leave_detail", sequenceName = "seq_employee_credit_leave_detail", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_employee_credit_leave_detail")
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "leave_type_id")
	private MasterLeaveType masterLeaveType;
	@Column(name = "no_of_days")
	private Float noOfDays;
	@Column(name = "from_date")
	@Temporal(TemporalType.DATE)
	private Date fromDate;
	@Column(name = "to_date")
	@Temporal(TemporalType.DATE)
	private Date toDate;
	@Column(name = "opening_balance_considered")
	private String openingBalanceConsidered;
	@Column(name = "credited_by")
	private String creditedBy;

	@Column(name = "comment")
	private String comment;

	@Column(name = "posted_date")
	@Temporal(TemporalType.DATE)
	private Date postedDate;
	@Column(name = "leave_available")
	private Float leaveAvailable;

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

	public MasterLeaveType getMasterLeaveType() {
		return masterLeaveType;
	}

	public void setMasterLeaveType(MasterLeaveType masterLeaveType) {
		this.masterLeaveType = masterLeaveType;
	}

	public Float getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(Float noOfDays) {
		this.noOfDays = noOfDays;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getOpeningBalanceConsidered() {
		return openingBalanceConsidered;
	}

	public void setOpeningBalanceConsidered(String openingBalanceConsidered) {
		this.openingBalanceConsidered = openingBalanceConsidered;
	}

	public String getCreditedBy() {
		return creditedBy;
	}

	public void setCreditedBy(String creditedBy) {
		this.creditedBy = creditedBy;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}

	public Float getLeaveAvailable() {
		return leaveAvailable;
	}

	public void setLeaveAvailable(Float leaveAvailable) {
		this.leaveAvailable = leaveAvailable;
	}

}
