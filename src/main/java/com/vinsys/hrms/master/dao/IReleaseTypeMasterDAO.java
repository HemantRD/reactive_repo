package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.master.entity.ReleaseTypeMaster;

public interface IReleaseTypeMasterDAO extends JpaRepository<ReleaseTypeMaster, Long> {
	ReleaseTypeMaster findByIsActiveAndId(String isActive, Long id);

	List<ReleaseTypeMaster> findByIsActive(String isActive);

}
