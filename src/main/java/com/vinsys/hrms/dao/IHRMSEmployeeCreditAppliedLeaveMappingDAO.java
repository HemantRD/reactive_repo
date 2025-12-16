package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.EmployeeCreditAppliedLeaveMapping;

public interface IHRMSEmployeeCreditAppliedLeaveMappingDAO
		extends JpaRepository<EmployeeCreditAppliedLeaveMapping, Long> {

	@Query("SELECT ecalm FROM EmployeeCreditAppliedLeaveMapping ecalm "
			+ " WHERE ecalm.employeeLeaveApplied.id = ?1 AND ecalm.isActive = ?2 ")
	public List<EmployeeCreditAppliedLeaveMapping> getCandidateWithChecklistDetails(long employeeLeaveAppliedId,
			String isActive);

}
