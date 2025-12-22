package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.kra.entity.ViDashboardCount;

public interface IVIDashboardCountDAO extends JpaRepository<ViDashboardCount, String>{

	 List<ViDashboardCount> findByDepartmentId(Long departmentId);
	 List<ViDashboardCount> findAll();
	List<ViDashboardCount> findByEmployeeCodeIn(List <Long> ids);
	
	




	
}
