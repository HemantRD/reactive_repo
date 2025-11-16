package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vinsys.hrms.kra.entity.ViKpiTemplate;


@Repository
public interface IKpiTemplateDAO extends JpaRepository<ViKpiTemplate, Long> {

	//List<ViKpiTemplate> findByEmployeeId(Long id);

	
	List<ViKpiTemplate> findByEmpIdIn(List<Long> employeeIds);

	

}
