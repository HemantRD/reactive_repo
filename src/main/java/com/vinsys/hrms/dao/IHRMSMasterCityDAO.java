package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vinsys.hrms.entity.MasterCity;

public interface IHRMSMasterCityDAO extends JpaRepository<MasterCity, Long> {

	@Query("select city from MasterCity city where city.masterState.id = ?1 and city.isActive = ?2 order by city.cityName asc")
	public List<MasterCity> findAllMasterCityByStateIdCustomQuery(long stateId, String isActive);

	@Query("select city from MasterCity city where city.isActive = ?1")
	public List<MasterCity> findByIsActive(String isActive);

	@Query("select city from MasterCity city where city.cityName = ?1 and city.isActive = ?2")
	public MasterCity findByCityName(String name, String isActive);

	public MasterCity findByIdAndIsActive(Long id, String isactive);

	@Query(value = "SELECT COUNT(*) > 0 FROM tbl_mst_city WHERE LOWER(city_name) = LOWER(:cityName)", nativeQuery = true)
	public boolean existsByCityName(String cityName);

	@Query(value = "SELECT * FROM tbl_mst_city " + "WHERE state_id = ?1 " + "AND org_id = ?2 "
			+ "AND city_name ILIKE %?3% " + "ORDER BY is_active DESC", nativeQuery = true)
	public List<MasterCity> searchCityByStateIdAndText(long stateId, Long orgId, String keyword, Pageable pageable);

	@Query(value = "SELECT count(*) \r\n" + "FROM tbl_mst_city \r\n" + "WHERE state_id = ?1 \r\n"
			+ "  AND org_id = ?2 \r\n" + "  AND city_name ILIKE %?3% ", nativeQuery = true)
	public long countByStateIdAndText(long stateId, Long orgId, String keyword);
}
