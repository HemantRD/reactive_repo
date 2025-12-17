package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vinsys.hrms.master.entity.Objectives;

public interface IObjectivesDAO extends JpaRepository<Objectives, Long> {

	@Query(value = "SELECT * FROM tbl_mst_kra_objectives WHERE mst_sub_category_id = ?1 AND is_active = ?2", countQuery = "SELECT count(*) FROM tbl_mst_kra_objectives WHERE mst_sub_category_id = ?1 AND is_active = ?2", nativeQuery = true)
	public List<Objectives> findBySubCategoryId(Long subCategoryId, String isActive, Pageable pageable);

	public Objectives findByIdAndIsActive(Long id, String isactive);

	@Query(value = "SELECT COUNT(*) > 0 FROM tbl_mst_kra_objectives WHERE LOWER(description) = LOWER(:description)", nativeQuery = true)
	boolean existsByDescription(@Param("description") String description);

	@Query(value = "SELECT COUNT(*) > 0 " + "FROM tbl_mst_kra_objectives " + "WHERE mst_category_id = :categoryId "
			+ "AND mst_sub_category_id = :subCategoryId " + "AND is_active = :isActive ", nativeQuery = true)
	boolean existsByCategoryAndSubCategory(@Param("categoryId") Long categoryId,
			@Param("subCategoryId") Long subCategoryId, @Param("isActive") String isActive);

	public Objectives findByCategoryIdAndSubCategoryIdAndIsActive(Long categoryId, Long subCategoryId, String name);
	
	@Query(value = "SELECT * FROM tbl_mst_kra_objectives WHERE mst_sub_category_id = ?1 AND is_active = ?2", countQuery = "SELECT count(*) FROM tbl_mst_kra_objectives WHERE mst_sub_category_id = ?1 AND is_active = ?2", nativeQuery = true)
	public List<Objectives> findBySubCategory(Long subCategoryId, String isActive);

}
