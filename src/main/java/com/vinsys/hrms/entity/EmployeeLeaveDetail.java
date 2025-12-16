package com.vinsys.hrms.entity;

import java.io.Serializable;

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

@Entity
@Table(name = "tbl_employee_leave_detail")
public class EmployeeLeaveDetail extends AuditBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_employee_leave_detail", sequenceName = "seq_employee_leave_detail", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_employee_leave_detail")
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee;
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "leave_type_id")
	private MasterLeaveType masterLeaveType;
	@Column(name = "closing_balance")
	private Float closingBalance;
	@Column(name = "py_leave_encashment")
	private Float pyLeaveEncashment;
	@Column(name = "leave_carried_over")
	private Float leaveCarriedOver;
	@Column(name = "leave_earned")
	private Float leaveEarned;
	@Column(name = "fy_leave_encashment")
	private Float fyLeaveEncashment;
	@Column(name = "total_eligibility")
	private Float totalEligibility;
	@Column(name = "number_of_days_availed")
	private Float numberOfDaysAvailed;
	@Column(name = "leave_available")
	private Float leaveAvailable;
	@Column(name = "year")
	private Integer year;

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

	public Float getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(Float closingBalance) {
		this.closingBalance = closingBalance;
	}

	public Float getPyLeaveEncashment() {
		return pyLeaveEncashment;
	}

	public void setPyLeaveEncashment(Float pyLeaveEncashment) {
		this.pyLeaveEncashment = pyLeaveEncashment;
	}

	public Float getLeaveCarriedOver() {
		return leaveCarriedOver;
	}

	public void setLeaveCarriedOver(Float leaveCarriedOver) {
		this.leaveCarriedOver = leaveCarriedOver;
	}

	public Float getLeaveEarned() {
		return leaveEarned;
	}

	public void setLeaveEarned(Float leaveEarned) {
		this.leaveEarned = leaveEarned;
	}

	public Float getFyLeaveEncashment() {
		return fyLeaveEncashment;
	}

	public void setFyLeaveEncashment(Float fyLeaveEncashment) {
		this.fyLeaveEncashment = fyLeaveEncashment;
	}

	public Float getTotalEligibility() {
		return totalEligibility;
	}

	public void setTotalEligibility(Float totalEligibility) {
		this.totalEligibility = totalEligibility;
	}

	public Float getNumberOfDaysAvailed() {
		return numberOfDaysAvailed;
	}

	public void setNumberOfDaysAvailed(Float numberOfDaysAvailed) {
		this.numberOfDaysAvailed = numberOfDaysAvailed;
	}

	public Float getLeaveAvailable() {
		return leaveAvailable;
	}

	public void setLeaveAvailable(Float leaveAvailable) {
		this.leaveAvailable = leaveAvailable;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

}
