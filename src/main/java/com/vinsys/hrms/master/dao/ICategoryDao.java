package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vinsys.hrms.kra.entity.DelegationMapping;
import com.vinsys.hrms.kra.entity.KraYear;
import com.vinsys.hrms.master.entity.Category;

public interface ICategoryDao extends JpaRepository<Category, Long> {

	Category findByIdAndIsActive(long categoryId, String isactive);

	Category findByCategoryNameAndIsActive(String category, String name);

	Category findByCategoryNameAndKraYearAndIsActive(String category, KraYear kraYear, String name);

	List<Category> findByKraYearAndIsActive(KraYear kraYear, String isactive);

	List<Category> findByIsActive(String isactive);

	List<Category> findByIsActive(String isactive, Pageable pageable);

	long countByIsActive(String isActive);

	@Query(value = "select * from tbl_mst_kra_category " + "where is_active = ?1 "
			+ "and lower(category_name) like lower(concat('%', ?2, '%'))", nativeQuery = true)
	List<Category> searchByIsActiveAndText(@Param("isActive") String isActive, @Param("searchText") String searchText,
			Pageable pageable);

	@Query(value = "select count(*) from tbl_mst_kra_category " + "where is_active = ?1 "
			+ "and lower(category_name) like lower(concat('%', ?2, '%'))", nativeQuery = true)
	long countSearchByIsActiveAndText(@Param("isActive") String isActive, @Param("searchText") String searchText);

}
