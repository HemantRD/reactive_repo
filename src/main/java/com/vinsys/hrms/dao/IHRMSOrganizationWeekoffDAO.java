package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.OrganizationWeekoff;

public interface IHRMSOrganizationWeekoffDAO extends JpaRepository<OrganizationWeekoff, Long> {

	@Query("select weekoff from OrganizationWeekoff weekoff where weekoff.orgId = ?1 and weekoff.division.id = ?2 "
			+ " and weekoff.branch.id = ?3  and weekoff.department.id = ?4")
	public List<OrganizationWeekoff> getWeekoffByOrgBranchDivDeptId(long orgId, long divId, long branchId, long departmentId);
}
