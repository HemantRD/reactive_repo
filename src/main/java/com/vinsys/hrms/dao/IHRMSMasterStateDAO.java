package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterState;

public interface IHRMSMasterStateDAO extends JpaRepository<MasterState, Long> {

	@Query("select state from MasterState state where state.masterCountry.id = ?1 and state.isActive = ?2 order by state.stateName asc")
	public List<MasterState> findAllMasterStateByCountryIdCustomQuery(long countryId, String isActive);
	
	@Query(value = "SELECT COUNT(*) > 0 FROM tbl_mst_state WHERE LOWER(state_name) = LOWER(:stateName)", nativeQuery = true)
	public boolean existsByStateName(String stateName);

	public List<MasterState> findByIsActiveOrderById(String isActive);
	
	@Query("select state from MasterState state where state.stateName = ?1 and state.isActive = ?2 ")
	public MasterState findByName(String name, String isActive);
	
	MasterState findByIdAndIsActive(Long id,String isActive);
	
	  List<MasterState> findAllByMasterCountry_IdAndOrgId(Long countryId, Long orgId);
	  
		@Query(value = "SELECT * FROM tbl_mst_state "
				+ "WHERE country_id = ?1 AND org_id = ?2 AND state_name ILIKE %?3% "
				+ "ORDER BY is_active DESC", nativeQuery = true)
	  List<MasterState> searchStateByCountryIdAndText(long countryId, Long orgId, String keyword, Pageable pageable);

	  @Query(value = "SELECT COUNT(*) FROM tbl_mst_state WHERE country_id = ?1 AND org_id = ?2 AND state_name ILIKE %?3%", nativeQuery = true)
	  long countSearchStateByCountryIdAndText(long countryId, Long orgId, String keyword);

}
