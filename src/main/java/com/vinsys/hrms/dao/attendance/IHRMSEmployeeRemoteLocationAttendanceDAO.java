package com.vinsys.hrms.dao.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.attendance.EmployeeRemoteLocationAttendanceDetail;

public interface IHRMSEmployeeRemoteLocationAttendanceDAO
		extends JpaRepository<EmployeeRemoteLocationAttendanceDetail, Long> {

	@Query(" SELECT empRemLoc FROM EmployeeRemoteLocationAttendanceDetail empRemLoc "
			+ " WHERE empRemLoc.isActive = ?1 AND empRemLoc.employee.id = ?2 ")
	public EmployeeRemoteLocationAttendanceDetail getEmployeeRemoteLocationDetailByEmpId(String isActive, long empId);
}
