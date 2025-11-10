package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.EmployeTypeToLeaveMap;

public interface IHRMSEmployeTypeToLeaveMapDAO extends JpaRepository<EmployeTypeToLeaveMap, Long> {
	
	List<EmployeTypeToLeaveMap> findByEmpTypeIdAndIsActive(Long entityId,String isActive);
	
	List<EmployeTypeToLeaveMap> findByEmpTypeIdAndIsActiveAndOrgId(Long entityId,String isActive,Long orgId);
}
