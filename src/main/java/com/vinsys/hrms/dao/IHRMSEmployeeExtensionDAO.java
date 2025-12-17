package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.EmployeeExtension;

public interface IHRMSEmployeeExtensionDAO extends JpaRepository<EmployeeExtension, Long> {

	@Query("SELECT ext FROM EmployeeExtension ext WHERE ext.employee.id=null AND ext.organization.id=?1 and ext.isActive=?2")
	public List<EmployeeExtension> findOtherExtension(long orgId,String isActive);
	
	@Query("SELECT ext FROM EmployeeExtension ext WHERE ext.id=?1 AND ext.isActive=?2")
	public EmployeeExtension findActiveExtensionById(long extensionId,String isActive);
}
