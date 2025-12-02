package com.vinsys.hrms.master.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.master.entity.MasterBranding;

public interface IMasterBrandingDAO extends JpaRepository<MasterBranding, Long> {

//	List<MasterBranding> findByIsActive(String isActive);

	MasterBranding findByIsActiveAndOrgId(String isActive, Long orgId);

}
