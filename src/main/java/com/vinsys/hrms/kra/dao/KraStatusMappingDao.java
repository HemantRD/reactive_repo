package com.vinsys.hrms.kra.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.kra.entity.KraStatusMapping;

public interface KraStatusMappingDao extends JpaRepository<KraStatusMapping, Long>{

	
	@Query(value = "select * from tbl_kra_status_mapping where status=?1 and pending_with=?2  and role_name =?3 and org_id=?4 and is_active=?5", nativeQuery = true)
	public KraStatusMapping findByStatusAndPendingWithAndRoleNameAndOrgIdAndIsActive(String status, String pendingWith, String roleName, Long orgId,String isActive);
}
