package com.vinsys.hrms.entity.attendance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;
import com.vinsys.hrms.entity.Employee;

@Entity
@Table(name = "tbl_employee_remote_location_attendance_detail")
public class EmployeeRemoteLocationAttendanceDetail extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_employee_remote_loc_attendance", sequenceName = "seq_employee_remote_loc_attendance", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_employee_remote_loc_attendance")
	private long id;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee;

	@Column(name = "is_remote_location_attendance_allowed")
	private String isRemoteLocationAttendanceAllowed;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "map_org_div_location_group")
	private MasterOrgDivLocationGroup mstOrgDivLocationGroup;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getIsRemoteLocationAttendanceAllowed() {
		return isRemoteLocationAttendanceAllowed;
	}

	public void setIsRemoteLocationAttendanceAllowed(String isRemoteLocationAttendanceAllowed) {
		this.isRemoteLocationAttendanceAllowed = isRemoteLocationAttendanceAllowed;
	}

	public MasterOrgDivLocationGroup getMstOrgDivLocationGroup() {
		return mstOrgDivLocationGroup;
	}

	public void setMstOrgDivLocationGroup(MasterOrgDivLocationGroup mstOrgDivLocationGroup) {
		this.mstOrgDivLocationGroup = mstOrgDivLocationGroup;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
