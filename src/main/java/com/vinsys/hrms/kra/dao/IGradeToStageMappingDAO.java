package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vinsys.hrms.kra.entity.GradeToStageMapping;

public interface IGradeToStageMappingDAO extends JpaRepository<GradeToStageMapping, Long> {

	boolean existsByGradeIdAndCategoryIdAndSubCategoryIdAndIsActive(Long gradeId, Long categoryId, Long subCategoryId,
			String name);

	GradeToStageMapping findByIdAndIsActive(Long mappingId, String name);

	boolean existsByGradeIdAndCategoryIdAndSubCategoryIdAndIsActiveAndIdNot(Long gradeId, Long categoryId,
			Long subCategoryId, String name, Long id);

	@Query("SELECT g " + "FROM GradeToStageMapping g " + "JOIN GradeMaster gm ON g.gradeId = gm.id "
			+ "JOIN Category c ON g.categoryId = c.id " + "JOIN Subcategory sc ON g.subCategoryId = sc.id "
			+ "WHERE g.isActive = :status " + "  AND (:gradeId IS NULL OR g.gradeId = :gradeId) "
			+ "  AND (:categoryId IS NULL OR g.categoryId = :categoryId) "
			+ "  AND (:subCategoryId IS NULL OR g.subCategoryId = :subCategoryId)")
	List<GradeToStageMapping> findAllMappingsWithFilters(@Param("status") String status, @Param("gradeId") Long gradeId,
			@Param("categoryId") Long categoryId, @Param("subCategoryId") Long subCategoryId);

	List<GradeToStageMapping> findAllByIsActive(String name);

	Long countByIsActive(String name);

	Long countByIsActiveAndGradeIdAndCategoryIdAndSubCategoryId(String isActive, Long gradeId, Long categoryId,
			Long subCategoryId);



}
