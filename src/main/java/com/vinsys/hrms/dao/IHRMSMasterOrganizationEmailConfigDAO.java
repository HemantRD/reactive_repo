package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterOrganizationEmailConfig;

public interface IHRMSMasterOrganizationEmailConfigDAO extends JpaRepository<MasterOrganizationEmailConfig, Long> {

	@Query("select config from MasterOrganizationEmailConfig config where config.orgId = ?1 and config.division.id = ?2")
	public MasterOrganizationEmailConfig findByorganizationAnddivision(long org, long division);

	@Query("select config from MasterOrganizationEmailConfig config where config.orgLevelEmployee.id = ?1 and config.orgId = ?2 ")
	public List<MasterOrganizationEmailConfig> findBYorgLevelEmployeeAndOrgId(long orgLevelEmpId,Long orgId);
}
