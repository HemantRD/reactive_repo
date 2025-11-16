package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vinsys.hrms.kra.entity.AnalyticalDepartmentWiseView;

public interface IAnalyticalDepartmentWiseDAO extends JpaRepository<AnalyticalDepartmentWiseView, Long> {

	@Query(value = "SELECT d.department_name AS departmentName, "
			+ "       d.department_description AS departmentDescription, " + "       COUNT(*) AS count "
			+ "FROM vw_analytical_department_wise d " + "WHERE d.year_id = :yearId " + "  AND d.cycleid = :cycleId "
			+ "GROUP BY d.department_name, d.department_description", nativeQuery = true)
	List<Object[]> getDepartmentSummary(@Param("yearId") Long yearId, @Param("cycleId") Long cycleId);

	@Query(value = "SELECT d.department_name AS departmentName, " + "       d.final_rating AS rating, "
			+ "       COUNT(*) AS count " + "FROM vw_analytical_department_wise d " + "WHERE d.year_id = :yearId "
			+ "  AND d.cycleid = :cycleId "
			+ "  AND d.department_id = COALESCE(CAST(:departmentId AS BIGINT), d.department_id) "
			+ "GROUP BY d.department_name, d.final_rating", nativeQuery = true)
	List<Object[]> getRatingSummary(@Param("yearId") Long yearId, @Param("cycleId") Long cycleId,
			@Param("departmentId") Long departmentId);

}
