package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterExtensionType;

public interface IHRMSMasterExtensionTypeDAO extends JpaRepository<MasterExtensionType, Long> {

	@Query("SELECT extType From MasterExtensionType extType where extType.orgId = ?1 and extType.isActive = ?2")
	public List<MasterExtensionType> findByOrganizationId(long organizationId, String isActive);
}
