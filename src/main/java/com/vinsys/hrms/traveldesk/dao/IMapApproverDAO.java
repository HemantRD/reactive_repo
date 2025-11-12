package com.vinsys.hrms.traveldesk.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.traveldesk.entity.MapTravelDeskApprover;


public interface IMapApproverDAO extends JpaRepository<MapTravelDeskApprover, Long > {
	
	 @Query(value = "SELECT * FROM tbl_map_td_approver tbl WHERE tbl.td_approver_id = ?1  AND tbl.is_active = ?2 ", nativeQuery = true)
		public List<MapTravelDeskApprover> findApproverIdAndIsActive(Long empId, String isActive);
	 
	 
	 @Query(value = "SELECT * FROM tbl_map_td_approver tbl WHERE tbl.td_approver_id = ?1  AND tbl.is_active = ?2 ", nativeQuery = true)
	 MapTravelDeskApprover findIdByIsActive(Long empId, String isActive);
	 
	 @Query(value = "select department_id from tbl_map_travel_approver tmta where approver_id =?1 and is_active =?2; ", nativeQuery = true)
		public List<MapTravelDeskApprover> findDepartmentIdIdAndIsActive(Long empId, String isActive);
	 
	 @Query(value = "SELECT * FROM tbl_map_td_approver tbl WHERE tbl.division_id = ?1  AND tbl.is_active = ?2 ", nativeQuery = true)
	 MapTravelDeskApprover findIdByDivisionIdAndIsActive(Long divId, String isActive);
	 
	 @Query(value = "SELECT DISTINCT td.td_approver_id FROM tbl_map_td_approver td WHERE td.division_id IN (?1) ",nativeQuery = true)
	    List<Long> findApproverIdsByDivisionIds(List<Long> divisionIds);
	 @Query(value = "SELECT * FROM tbl_map_td_approver tbl WHERE tbl.division_id = ?1  AND tbl.is_active = ?2 ", nativeQuery = true)
	 List<MapTravelDeskApprover> findIdByDivisionId(Long divId, String isActive);

}
