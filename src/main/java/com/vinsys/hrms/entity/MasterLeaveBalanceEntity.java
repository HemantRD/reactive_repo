package com.vinsys.hrms.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

//@Entity
//@Table(name = "tbl_mst_leave_balance")
public class MasterLeaveBalanceEntity extends AuditBase {

	private static final long serialVersionUID = 1L;
    @Id
	@SequenceGenerator(name = "seq_mst_leave_balance", sequenceName = "seq_leave", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_leave_balance")
    private long id;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "division")
	private EmployeeDivision  division;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "leave_type_id")
	private MasterLeaveType masterLeaveType;
    
    @Column(name="leave_balance")
    private long leaveBalance;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public EmployeeDivision getDivision() {
		return division;
	}

	public void setDivision(EmployeeDivision division) {
		this.division = division;
	}

	public MasterLeaveType getMasterLeaveType() {
		return masterLeaveType;
	}

	public void setMasterLeaveType(MasterLeaveType masterLeaveType) {
		this.masterLeaveType = masterLeaveType;
	}

	public long getLeaveBalance() {
		return leaveBalance;
	}

	public void setLeaveBalance(long leaveBalance) {
		this.leaveBalance = leaveBalance;
	}
    
    
    
	
	
}
