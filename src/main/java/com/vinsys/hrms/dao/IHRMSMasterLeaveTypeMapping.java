package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterCandidateLeaveTypeMapping;

public interface IHRMSMasterLeaveTypeMapping extends JpaRepository<MasterCandidateLeaveTypeMapping, Long> {

	@Query("SELECT CAST(m.leavecount AS float) FROM MasterCandidateLeaveTypeMapping m WHERE m.divisionId =?1 AND m.leaveCycle = ?2")
	Float findLeaveCountByDivisionIdAndLeaveCycle(long divisionId, String leaveCycle);

}
