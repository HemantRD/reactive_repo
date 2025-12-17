package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.master.entity.DegreeMaster;

public interface IMasterDegreeDAO extends JpaRepository<DegreeMaster, Long> {
	List<DegreeMaster> findByIsActive(String isActive);

	public boolean existsByDegreeName(String degreeName);

}
