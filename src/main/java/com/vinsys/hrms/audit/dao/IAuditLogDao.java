package com.vinsys.hrms.audit.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.audit.entity.AuditLog;

public interface IAuditLogDao extends JpaRepository<AuditLog, Long> {
	

	List<AuditLog> findByIsActiveOrderByIdDesc(String isActive, Pageable pageable);

	
	long countByIsActive(String isActive);
}
