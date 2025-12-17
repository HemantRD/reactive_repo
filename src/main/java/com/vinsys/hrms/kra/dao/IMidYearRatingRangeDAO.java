package com.vinsys.hrms.kra.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.kra.entity.KraRatingRange;
import com.vinsys.hrms.kra.entity.MidYearRatingRange;

public interface IMidYearRatingRangeDAO  extends JpaRepository<MidYearRatingRange, Long> {
	
	
	
	@Query(value="SELECT * \r\n"
			+ "FROM tbl_mid_year_rating_range\r\n"
			+ "WHERE ?1 BETWEEN low_point AND high_point",nativeQuery=true)
	public MidYearRatingRange findByAverage(Double average);

}
