package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.master.entity.MasterGender;

public interface IMasterGenderDAO extends JpaRepository<MasterGender, Long>{

	List<MasterGender> findByIsActive(String isActive);

	MasterGender findByGender(String gender);
	
	public boolean existsByGender(String gender);
}
