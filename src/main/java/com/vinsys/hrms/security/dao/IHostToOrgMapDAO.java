package com.vinsys.hrms.security.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.security.entity.HostToOrgMap;

public interface IHostToOrgMapDAO extends JpaRepository<HostToOrgMap, Long> {

	HostToOrgMap findByHostNameAndIsActiveAndOrgId(String hostName, String status,Long orgId);

}
