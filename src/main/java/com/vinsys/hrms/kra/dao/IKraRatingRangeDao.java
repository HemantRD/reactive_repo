package com.vinsys.hrms.kra.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.kra.entity.KraRatingRange;

public interface IKraRatingRangeDao  extends JpaRepository<KraRatingRange, Long> {
	
	
	
	@Query(value="SELECT * \r\n"
			+ "FROM tbl_kra_rating_range\r\n"
			+ "WHERE ?1 BETWEEN low_point AND high_point",nativeQuery=true)
	public KraRatingRange findByAverage(Double average);

}
