package com.vinsys.hrms.kra.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vinsys.hrms.kra.entity.KraCycle;
import com.vinsys.hrms.kra.entity.KraCycleStatus;
import com.vinsys.hrms.kra.entity.KraYear;

public interface IKraCycleDAO extends JpaRepository<KraCycle, Long> {

	KraCycle findByIdAndIsActive(Long cycleId, String name);
	
	@Query(value = "SELECT *  FROM tbl_mst_kra_cycle WHERE id = ?1", nativeQuery = true)
	KraCycle findByKraId(Long cycleId);

	KraCycle findByStatus(KraCycleStatus status);

	@Query(value = "SELECT MAX(id) AS latest_smaller_id FROM tbl_mst_kra_cycle WHERE id < ?1", nativeQuery = true)
	Long findKraCycleById(Long id);

	KraCycle findByIsActive(String name);

	List<KraCycle> findByYear(Optional<KraYear> year);

	KraCycle findByYearAndCycleName(KraYear year, String cycle);
	
	@Query(value = "SELECT * FROM tbl_mst_kra_cycle tmkc WHERE tmkc.year = ?1 AND tmkc.mst_cycle_type = ?2", nativeQuery = true)
	KraCycle findByYearAndCycleType(KraYear year, Long cycleTypeId);
 

	KraCycle findByYearAndId(KraYear year, Long cycleId);

	List<KraCycle> findByYearAndIsActive(KraYear year, String name);

	List<KraCycle> findByYearOrderByIdAsc(KraYear year);

	List<KraCycle> findByYearAndOrgId(KraYear kraYear, long orgId);

	KraCycle findByIdAndYear(Long cycleId, KraYear year);

	@Query(value = "SELECT *\r\n" + "FROM tbl_mst_kra_cycle tecld\r\n" + "WHERE tecld.end_date IS not NULL\r\n"
			+ "  AND start_date is not null\r\n"
			+ "and CURRENT_DATE between  start_date and end_date;", nativeQuery = true)
	List<KraCycle> findValidCycleEntry();

	boolean existsByStatusAndIsActive(KraCycleStatus openStatus, String isactive);

	List<KraCycle> findByYearIdAndOrgId(Long yearId, Long orgId);

	@Query(value = "SELECT * FROM tbl_mst_kra_cycle " +
		       "WHERE year = ?1 " +
		       "  AND org_id = ?2 " +
		       "  AND (cycle_name ILIKE %?3% OR description ILIKE %?3%) " +
		       "  AND is_active IS NOT NULL"
		       +" ORDER BY cycle_name ASC", 
		       nativeQuery = true)
		List<KraCycle> searchByYearIdAndText(long yearId, Long orgId, String keyword, Pageable pageable);


	@Query(value = "SELECT count(*) FROM tbl_mst_kra_cycle " + "WHERE year = ?1 " + "  AND org_id = ?2 "
			+ "  AND (cycle_name ILIKE %?3% OR description ILIKE %?3%)", nativeQuery = true)
	long countByYearIdAndText(long yearId, Long orgId, String keyword);
	
	
	@Query(value = "SELECT * FROM tbl_mst_kra_cycle WHERE description = ?1 AND year = ?2", nativeQuery = true)
	KraCycle findByDescriptionAndYear(String cycleName, Long yearId);

	@Query(value = "SELECT * FROM tbl_mst_kra_cycle WHERE id =?1", nativeQuery = true)
	KraCycle findByKraCycle(Long formCycleId);

	List<KraCycle> findAllByStatus(KraCycleStatus status);

	@Query(value = "SELECT * FROM tbl_mst_kra_cycle WHERE year =?1", nativeQuery = true)
	List<KraCycle> findByYearId(Long id);

	@Query(value = "SELECT * FROM tbl_mst_kra_cycle WHERE mst_cycle_type = ?1 AND year = ?2", nativeQuery = true)
	KraCycle findByCycleTypeAndYear(Long cycleTypeId, Long yearId);

	List<KraCycle> findByIdInAndYearAndOrgId(List<Long> cycleIds, KraYear kraYear, long orgId);

	//KraCycle findByIdAndYearAndOrgId(Long kpiSubmissionTypeId, KraYear kraYear, long orgId);
	
	
	@Query("SELECT kc FROM KraCycle kc WHERE kc.cycleTypeId = :cycleTypeId AND kc.year = :year AND kc.orgId = :orgId")
	KraCycle findByIdAndYearAndOrgId(@Param("cycleTypeId") Long cycleTypeId, @Param("year") KraYear year,
			@Param("orgId") Long orgId);

	List<KraCycle> findByOrgId(Long orgId);

	List<KraCycle> findByStatusAndOrgId(KraCycleStatus openStatus, Long orgId);

	


}
