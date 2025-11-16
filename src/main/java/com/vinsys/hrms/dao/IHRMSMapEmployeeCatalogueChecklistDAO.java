package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MapEmployeeCatalogueChecklist;

public interface IHRMSMapEmployeeCatalogueChecklistDAO extends JpaRepository<MapEmployeeCatalogueChecklist, Long> {

	@Query("select employeeCatalogueChecklist from MapEmployeeCatalogueChecklist employeeCatalogueChecklist where employeeCatalogueChecklist.employeeCatalogueMapping.id = ?1")
	public List<MapEmployeeCatalogueChecklist> findByEmployeeCatalogueMapping(Long employeeCatalogueMappingId);
	
	
}
