package com.vinsys.hrms.dao.attendance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.attendance.AttendanceSwipeData;

public interface IHRMSAttendanceSwipeDataDAO extends JpaRepository<AttendanceSwipeData, Long>{
	
	@Query(" SELECT asd FROM AttendanceSwipeData asd WHERE asd.transactionId > ?1")
	public List<AttendanceSwipeData> getSwipes(long trasanctionId);
	

}
