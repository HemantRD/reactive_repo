package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.master.entity.KpiTypeMaster;

public interface IKpiTypeDAO extends JpaRepository<KpiTypeMaster, Long>{
	
	List<KpiTypeMaster> findByIsActive(String isActive);

}
