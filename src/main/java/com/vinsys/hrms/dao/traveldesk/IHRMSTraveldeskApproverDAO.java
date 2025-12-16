package com.vinsys.hrms.dao.traveldesk;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.traveldesk.MasterTraveldeskApprover;

public interface IHRMSTraveldeskApproverDAO extends JpaRepository<MasterTraveldeskApprover, Long>{
	
	
	@Query("SELECT approver FROM MasterTraveldeskApprover approver "
			+ " where approver.isActive = ?1 and approver.orgId = ?2 and approver.division.id = ?3  and approver.approverType!=?4")
	public List<MasterTraveldeskApprover> findLoggedInApproverOrgDivWise(String status, long orgId, long divisionId,String approvarType);

	
	
	@Query("SELECT approver FROM MasterTraveldeskApprover approver WHERE approver.employee.id = ?1 AND approver.orgId = ?2 "
			+ " AND approver.division.id = ?3")
	public List<MasterTraveldeskApprover> findLoggedInEmployeeApproverOrNot(long employeeId, long orgId, long divisionId);
	
	
	@Query("SELECT approver FROM MasterTraveldeskApprover approver WHERE approver.orgId = ?1 AND approver.division.id = ?2  AND approver.approverType = ?3 AND approver.isActive = ?4")
	public List<MasterTraveldeskApprover> findTravelDeskEmployeeOrgWise(long orgId, long divisionId, String approverType,
			String isActive);
}
