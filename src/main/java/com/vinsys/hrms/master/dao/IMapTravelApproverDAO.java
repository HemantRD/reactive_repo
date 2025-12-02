package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.master.entity.MasterMapTravelApprover;

public interface IMapTravelApproverDAO extends JpaRepository<MasterMapTravelApprover, Long>{

	public MasterMapTravelApprover findByApproverSlabAndIsActive(Long approverSlab,String isActive);
	
	@Query(value="select * from tbl_map_travel_approver  where approver_slab =?1 and department_id =?4 and branch_id =?3 and division_id =?2",nativeQuery=true)
	public MasterMapTravelApprover findByApproverSlabAndDivisionIdAndBranchIdAndDepartmentId(Long approverSlab,Long divisionId,Long branchId,Long departmentId);
	
	@Query(value = "select department_id from tbl_map_travel_approver tmta where approver_id =?1 and is_active =?2", nativeQuery = true)
	public List<Long> findDepartmentIdIdAndIsActive(Long empId, String isActive);
}
