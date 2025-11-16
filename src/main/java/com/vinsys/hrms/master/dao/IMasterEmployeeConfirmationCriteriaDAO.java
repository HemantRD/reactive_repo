package com.vinsys.hrms.master.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.master.entity.MasterEmployeeConfirmationCriteria;

public interface IMasterEmployeeConfirmationCriteriaDAO
		extends JpaRepository<MasterEmployeeConfirmationCriteria, Long> {

	@Query(value = "select * from tbl_mst_emp_confirmation_criteria tmecc  where min_percentage <=?1 and max_percentage >=?2 and is_active=?3 ", nativeQuery = true)
	MasterEmployeeConfirmationCriteria getStatusByMinAndMaxPercentage(float minPecrentage, float maxPercentage,
			String isActive);
}
