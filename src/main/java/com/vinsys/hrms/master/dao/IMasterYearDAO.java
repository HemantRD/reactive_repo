package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.master.entity.YearMaster;

public interface IMasterYearDAO extends JpaRepository<YearMaster, Long> {

	List<YearMaster> findByIsActive(String isActive);

	@Query("select y from YearMaster y where y.status='Open'")
	YearMaster findRunningYear();

	@Query("select y.id from YearMaster y where y.status='Open'")
	Long getYearIdRunningYear();


	@Query(value = "SELECT * FROM tbl_mst_year WHERE year <= ?1 ORDER BY id DESC LIMIT 11", nativeQuery = true)
	List<YearMaster> getLastTenYears(Long year);

	YearMaster findByYear(Long year);
}