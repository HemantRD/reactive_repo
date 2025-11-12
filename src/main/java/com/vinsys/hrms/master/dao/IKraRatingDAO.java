package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.master.entity.KraRating;


public interface IKraRatingDAO extends JpaRepository<KraRating, Long>{

	KraRating findByIsActiveAndId(String name, Long id);

	List<KraRating> findByIsActiveOrderById(String string);

	@Query(value = "SELECT * FROM tbl_mst_kra_rating r WHERE r.lable LIKE ?1%", nativeQuery = true)
	List<KraRating> findByLabelStartingWith(String label);

	
	@Query(value = "SELECT * FROM tbl_mst_kra_rating r WHERE category_id=?1 and is_passfail=?2", nativeQuery = true)
	List<KraRating> findByCatId(Long catId,String passfail);
	
	@Query(value = "SELECT * FROM tbl_mst_kra_rating r WHERE category_id=?1 and on_occurrence=?2 and mid_year=?3", nativeQuery = true)
	List<KraRating> findByCategoryAndMatrixAndMidYear(Long catId,String onoccurrence,String active);
	
	@Query(value = "SELECT * FROM tbl_mst_kra_rating r WHERE category_id=?1 and is_passfail=?2 and mid_year=?3", nativeQuery = true)
	List<KraRating> findByCatIdAndPassFailAndMidYear(Long catId,String passfail,String active);
	
	@Query(value = "SELECT * FROM tbl_mst_kra_rating r WHERE category_id=?1 and on_occurrence=?2", nativeQuery = true)
	List<KraRating> findByCategoryandMatrix(Long catId,String onoccurrence);
	
	
	/*
	 * @Query(value =
	 * "select * from tbl_mst_kra_rating where is_active = ? and mid_year = ?",
	 * nativeQuery = true) List<KraRating> findByMidYearRating(String isActive,
	 * String midYear);
	 */
	



		
	
	
	
}
