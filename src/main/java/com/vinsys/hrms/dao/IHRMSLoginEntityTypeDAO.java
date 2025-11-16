package com.vinsys.hrms.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.LoginEntityType;
import com.vinsys.hrms.entity.MasterRole;

public interface IHRMSLoginEntityTypeDAO extends JpaRepository<LoginEntityType, Long> {

	@Query("select entityType from LoginEntityType entityType where entityType.role.roleName =?1 "
			+ " AND entityType.organization.id = ?2 ")
	public LoginEntityType findByRoleName(String name, long orgId);

	@Query("SELECT entityType FROM LoginEntityType entityType WHERE entityType.organization.id =?1 "
			+ " AND entityType.isActive = ?2 ")
	public List<LoginEntityType> findByOrganizationId(Long id, String isActive);

	@Query("select entityType from LoginEntityType entityType where entityType.role.roleName =?1 ")
	public LoginEntityType findByRoleName(String name);
	
	@Query("SELECT entityType FROM LoginEntityType entityType WHERE entityType.orgId =?1 "
			+ " AND entityType.isActive = ?2 ")
	public List<LoginEntityType> findByOrgIdAndIsActive(Long id, String isActive);

	@Query("select count(*) from  LoginEntityType entityType WHERE entityType.isActive =?1")
	public long countOfTotalRecord(String isactive);

	public LoginEntityType findByRoleIdAndIsActive(Long roleId, String name);

	@Query("select entityType from LoginEntityType entityType WHERE role_id =?1")
	public Optional<LoginEntityType> findByRoleId(long id);

	

}
