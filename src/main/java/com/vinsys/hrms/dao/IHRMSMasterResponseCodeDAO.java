package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.MasterResponseCode;

public interface IHRMSMasterResponseCodeDAO extends JpaRepository<MasterResponseCode, Long> {
	List<MasterResponseCode> findByIsActiveAndOrgId(String isActive, Long orgId);
	List<MasterResponseCode> findByIsActive(String isActive);
}
