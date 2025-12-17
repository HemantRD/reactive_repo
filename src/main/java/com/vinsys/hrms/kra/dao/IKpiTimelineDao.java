package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.kra.entity.KpiTimeline;

public interface IKpiTimelineDao extends JpaRepository<KpiTimeline, Long> {

//	@Query(value = "select * from tbl_mst_kra_timeline where is_active=?1 and org_id=?2 and mst_kra_cycle_id=?3 and year=?4 ORDER BY id ASC", nativeQuery = true)
	public List<KpiTimeline> findByIsActiveAndOrgIdAndCycleIdAndYearIdOrderByIdAsc(String isActive, Long orgId, Long cycleId, Long yearId);

	KpiTimeline findByIdAndIsActive(Long id, String isActive);

	List<KpiTimeline> findByIsActiveOrderByIdDesc(String isActive, Pageable pageable);
	
	long countByIsActive(String isActive);

	List<KpiTimeline> findByIsActiveAndCycleIdAndYearIdOrderByIdDesc(String isActive, Long cycleId, Long yearId, Pageable pageable);
	
	long countByIsActiveAndCycleIdAndYearIdOrderByIdDesc(String isActive, Long cycleId, Long yearId);

	List<KpiTimeline> findByIsActiveAndYearIdOrderByIdDesc(String isActive, Long yearId, Pageable pageable);
	
	long countByIsActiveAndYearId(String isActive, Long yearId);

	List<KpiTimeline> findByIsActiveAndCycleIdOrderByIdDesc(String isActive, Long cycleId, Pageable pageable);
	
	long countByIsActiveAndCycleId(String isActive, Long cycleId);
}
