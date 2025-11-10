package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterCountry;

public interface IHRMSMasterCountryDAO extends JpaRepository<MasterCountry, Long> {

	@Query("select country from MasterCountry country where country.id = ?1")
	public List<MasterCountry> findAllMasterCountryByOrgIdCustomQuery(long id);

	@Query("select country from MasterCountry country where country.isActive = ?1 order by country.countryName asc")
	public List<MasterCountry> findAllMasterCountryCustomQuery(String isActive);

//	public boolean existsByStateName(String stateName);

//	public boolean existsByReasonName(String reasonName);
	
	
	@Query(value=" SELECT * FROM tbl_mst_country  WHERE id = ?1 ",nativeQuery = true)
	public MasterCountry findByCountryId(long countryId);
	
	@Query("select country from MasterCountry country where country.isActive = ?1 ")
	public List<MasterCountry> findByIsActive(String isActive);
	
	@Query("select country from MasterCountry country where country.countryName = ?1 and country.isActive = ?2 ")
	public MasterCountry findByName(String name,String isActive);

	public MasterCountry findByIdAndIsActive(long id, String isactive);

	@Query(value = "SELECT COUNT(*) > 0 FROM tbl_mst_country WHERE LOWER(country_name) = LOWER(:countryName)", nativeQuery = true)
	public boolean existsByCountryName(String countryName);
	
	public List<MasterCountry> findAllMasterCountryByOrgId(long id);
	
	@Query(value = "SELECT * FROM tbl_mst_country WHERE org_id = ?1 AND country_name ILIKE %?2% ORDER BY is_active DESC", nativeQuery = true)
	List<MasterCountry> searchCountryByOrgIdAndText(Long orgId, String keyword, Pageable pageable);

	@Query(value = "SELECT COUNT(*) FROM tbl_mst_country WHERE org_id = ?1 AND country_name ILIKE %?2%", nativeQuery = true)
	long countCountryByOrgIdAndText(Long orgId, String keyword);


}
