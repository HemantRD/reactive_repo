package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.master.entity.SelectionOptionMaster;

public interface IMasterSelectionOptionDAO extends JpaRepository<SelectionOptionMaster, Long>{

	List<SelectionOptionMaster> findByIsActive(String isActive);
}
