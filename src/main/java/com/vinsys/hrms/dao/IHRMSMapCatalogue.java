package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MapCatalogue;

public interface IHRMSMapCatalogue extends JpaRepository<MapCatalogue, Long> {

	public List<MapCatalogue> findByisActive(String isActive);

	@Query("select catalogue from MapCatalogue catalogue where catalogue.approver.id = ?1 "
			+ " AND catalogue.orgId = ?2 AND catalogue.division.id = ?3")
	public List<MapCatalogue> findByApproverId(Long approvedId, long orgId, long divId);
	
	@Query("select catalogue from MapCatalogue catalogue where catalogue.approver.id = null "
			+ " AND catalogue.orgId = ?1 ")
	public List<MapCatalogue> findCatalogueForRO(long orgId);
	
	@Query("select catalogue from MapCatalogue catalogue JOIN FETCH catalogue.approver where catalogue.isActive = ?1 and catalogue.division.id = ?2 and catalogue.orgId = ?3 ")
	public List<MapCatalogue> findAllCataloguewithEmployeeByisActiveAndOrgId(String isActive, long divisionId,Long orgId);
	
	@Query("select catalogue from MapCatalogue catalogue JOIN FETCH catalogue.approver where catalogue.isActive = ?1 and catalogue.orgId = ?2 ")
	public List<MapCatalogue> findAllCataloguewithEmployeeByisActiveAndOrgId(String isActive,Long orgId);
	
	@Query("select catalogue from MapCatalogue catalogue where catalogue.orgId = ?1 and catalogue.division.id = ?2")
	public List<MapCatalogue> findCatalogueListByOrgandDiv(long orgId,long divisionId);
	
	@Query("select catalogue from MapCatalogue catalogue "
			+ " join fetch catalogue.approver "
			+ " where catalogue.orgId = ?1 and catalogue.name = ?2 and catalogue.division.id = ?3")
	public MapCatalogue findHR(long orgId,String roleName,long divisionId);
	
	@Query("select catalogue from MapCatalogue catalogue where catalogue.approver.id = ?1 "
			+ " AND catalogue.orgId = ?2")
	public List<MapCatalogue> findByApprover(Long approvedId, long orgId);

	@Query(value="select * from tbl_map_catalogue where approver_id is not null and approver_id =?1 and org_id=?2 and division_id =?3 and department_id  =?4",nativeQuery = true)
	public MapCatalogue findByApproverOrganizationDivisionDepartment(Long approverId, Long orgId, Long divId, long deptId);
	
	
	@Query(value="select * from tbl_map_catalogue where approver_id is not null and approver_id =?1 and org_id=?2 and division_id =?3 and is_active =?4",nativeQuery = true)
	public MapCatalogue findByApproverOrganizationDivision(Long approverId, Long orgId, Long divId, String isActive);
	
	@Query(value="select * from tbl_map_catalogue where approver_id is not null and approver_id =?1 and org_id=?2  and is_active =?3",nativeQuery = true)
	public List<MapCatalogue> findByApproverOrganizationDivisionId(Long approverId, Long orgId, String isActive);
	
	@Query(value="select * from tbl_map_catalogue where approver_id is not null and approver_id =?1 and org_id=?2  and is_active =?3 and name=?4",nativeQuery = true)
	public List<MapCatalogue> findByApproverOrganizationDivisionIdAndName(Long approverId, Long orgId, String isActive,String Name);

	@Query(value = "SELECT approver_id FROM tbl_map_catalogue WHERE id = ?1 and  org_id = ?2", nativeQuery = true)
	public Long findApproverIdByCatalogueIdAndOrgId(Long catalogueId,Long orgId);

	
}
