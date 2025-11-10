package com.vinsys.hrms.master.dao;

/**
 * @author Onkar A.
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.master.entity.MasterLeavePolicy;

public interface IMasterLeavePolicyDAO extends JpaRepository<MasterLeavePolicy, Long> {

	@Query(value = "select * from tbl_mst_leave_policy where organization =?1 and division =?2 and branch =?3 and department =?4 and leave_type =?5 ", nativeQuery = true)
	MasterLeavePolicy findPolicyByDepartmentAndLeaveType(Long orgId, Long divisionId, Long branchId, Long departmentId,
			Long leaveType);

	@Query(value = "select * from tbl_mst_leave_policy where organization =?1 and division =?2 and branch =?3  and leave_type =?4 ", nativeQuery = true)
	MasterLeavePolicy findPolicyByBranchAndLeaveType(Long orgId, Long divisionId, Long branchId, Long leaveType);
}
