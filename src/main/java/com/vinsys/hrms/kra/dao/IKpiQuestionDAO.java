package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vinsys.hrms.kra.entity.KpiQuestions;

public interface IKpiQuestionDAO extends JpaRepository<KpiQuestions, Long> {

	KpiQuestions findByIdAndIsActive(Long id, String isactive);

	@Query(value = "SELECT * FROM tbl_mst_kpi_questions " + "WHERE category_id = ?1 " + "AND subcategory_id = ?2 "
			+ "AND org_id = ?3 " + "AND is_active = 'Y' " + "AND question_name ILIKE %?4% "
			+ "ORDER BY is_active DESC", nativeQuery = true)
	List<KpiQuestions> searchQuestionsByCategoryAndSubcategory(long categoryId, long subCategoryId, Long orgId,
			String keyword, Pageable pageable);
	
	@Query(value = "SELECT count(*) FROM tbl_mst_kpi_questions " + "WHERE category_id = ?1 "
			+ "AND subcategory_id = ?2 " + "AND org_id = ?3 " + "AND is_active = 'Y' "
			+ "AND question_name ILIKE %?4% ", nativeQuery = true)
	long countQuestionsByCategoryAndSubcategory(long categoryId, long subCategoryId, Long orgId, String keyword);

	
	@Query("SELECT k FROM KpiQuestions k WHERE k.categoryId = :categoryId AND k.subcategoryId = :subcategoryId AND k.isActive = 'Y'")
	List<KpiQuestions> getQuestionsByCategoryAndSubcategory(@Param("categoryId") long categoryId,
			@Param("subcategoryId") long subcategoryId);

	@Query("SELECT COUNT(k) FROM KpiQuestions k WHERE k.categoryId = :categoryId AND k.subcategoryId = :subcategoryId AND k.isActive = 'Y'")
	long countQuestionsByCategoryAndSubcategory(@Param("categoryId") long categoryId,
			@Param("subcategoryId") long subcategoryId);

}
