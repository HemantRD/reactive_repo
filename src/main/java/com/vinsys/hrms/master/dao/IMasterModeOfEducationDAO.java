package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.master.entity.ModeOfEducationMaster;

public interface IMasterModeOfEducationDAO extends JpaRepository<ModeOfEducationMaster, Long> {
	
	List<ModeOfEducationMaster> findByIsActive(String isActive);
	
	public boolean existsByModeOfEducation(String modeOfEducation);

}
