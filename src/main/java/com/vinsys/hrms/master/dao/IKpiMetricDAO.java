package com.vinsys.hrms.master.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.kra.vo.PMSKraMetricVo;
import com.vinsys.hrms.master.entity.KpiMetricMaster;

public interface IKpiMetricDAO extends JpaRepository<KpiMetricMaster, Long> {

	List<KpiMetricMaster> findByIsActive(String isActive);

	KpiMetricMaster getById(PMSKraMetricVo metric);

	KpiMetricMaster findByMetric(String measurementCriteria);

	@Query(value = "SELECT COUNT(*) > 0 FROM tbl_mst_measurement_criteria WHERE LOWER(measurement_criteria) = LOWER(:metric)", nativeQuery = true)
	public boolean existsByMetric(String metric);

	KpiMetricMaster findByIdAndIsActive(Long id, String isactive);

	Optional<KpiMetricMaster> findById(Long id);

	@Query(value = "SELECT * FROM tbl_mst_measurement_criteria " + "WHERE org_id = ?1 "
			+ "AND (measurement_criteria ILIKE %?2%)", nativeQuery = true)
	List<KpiMetricMaster> searchMetricByOrgIdAndText(Long orgId, String searchText, Pageable pageable);

	@Query(value = "SELECT COUNT(*) FROM tbl_mst_measurement_criteria " + "WHERE org_id = ?1 "
			+ "AND (measurement_criteria ILIKE %?2%)", nativeQuery = true)
	long countMetricByOrgIdAndText(Long orgId, String searchText);

}
