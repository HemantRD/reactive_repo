package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MapCatalogue;
import com.vinsys.hrms.entity.MapEmployeeCatalogue;

public interface IHRMSMapEmployeeCatalogueMappingDAO extends JpaRepository<MapEmployeeCatalogue, Long> {

	@Query("select empCatalogue from MapEmployeeCatalogue empCatalogue where  empCatalogue.resignedEmployee.id = ?1 and empCatalogue.catalogue.id = ?2 and empCatalogue.isActive=?3")
	public MapEmployeeCatalogue findByEmployeeIdAndCatalogue(Long employeeId, Long catalogueId, String isActive);

	@Query("select empCatalogue from MapEmployeeCatalogue empCatalogue where  empCatalogue.resignedEmployee.id = ?1 and empCatalogue.isActive=?2")
	public MapEmployeeCatalogue findByEmployeeId(Long employeeId, String isActive);

	@Query("select empCatalogue from MapEmployeeCatalogue empCatalogue where  empCatalogue.resignedEmployee.id = ?1 and empCatalogue.catalogue.id = ?2 and empCatalogue.isActive=?3")
	public List<MapEmployeeCatalogue> findByEmployeeIdAndCatalogueList(Long employeeId, Long catalogueId,
			String isActive);

	@Query("select empCatalogue from MapEmployeeCatalogue empCatalogue where  empCatalogue.resignedEmployee.id = ?1 and empCatalogue.isActive=?2")
	public List<MapEmployeeCatalogue> findByEmployeeIdList(Long employeeId, String isActive);

	@Query("select empCatalogue from MapEmployeeCatalogue empCatalogue JOIN FETCH empCatalogue.resignedEmployee emp "
			+ " JOIN FETCH emp.employeeSeparationDetails esd where  empCatalogue.catalogue.id = ?1 AND esd.isActive = ?2 "
			+ " order by  empCatalogue.status desc,esd.resignationDate desc ")
	public List<MapEmployeeCatalogue> findAllEmployeeCatalogue(Long catalogueId, String isActive);

	@Query("select empCatalogue from MapEmployeeCatalogue empCatalogue where  empCatalogue.resignedEmployee.id = ?1")
	public List<MapEmployeeCatalogue> findByEmployeeId(Long employeeId);

	public List<MapEmployeeCatalogue> findBycatalogueAndStatus(MapCatalogue catalogue, String status);

	@Query("select empCatalogue from MapEmployeeCatalogue empCatalogue where empCatalogue.catalogue.id=?1 and empCatalogue.resignedEmployee.id = ?2 and empCatalogue.isActive=?3 ")
	public MapEmployeeCatalogue findByIdAndResignedEmployee(Long catalogueId, long empId, String isActive);

}
