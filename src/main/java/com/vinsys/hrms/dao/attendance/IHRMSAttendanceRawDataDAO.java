package com.vinsys.hrms.dao.attendance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.attendance.AttendanceRawData;

public interface IHRMSAttendanceRawDataDAO extends JpaRepository<AttendanceRawData, Long> {

	List<AttendanceRawData> findByorgId(long orgId);

	}
