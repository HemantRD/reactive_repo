package com.vinsys.hrms.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.EmployeeMasterDetails;

public interface IEmployeeMasterDetailsDAO extends JpaRepository<EmployeeMasterDetails, Long> {

	Optional<EmployeeMasterDetails> findById(Long employeeId);

	@Query("SELECT e FROM EmployeeMasterDetails e " +
		       "WHERE LOWER(e.employeeCode) LIKE LOWER(CONCAT('%', ?1, '%')) " +
		       "OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', ?1, '%')) " +
		       "OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', ?1, '%')) " +
		       "OR LOWER(e.officialEmailId) LIKE LOWER(CONCAT('%', ?1, '%')) " +
		       "OR LOWER(e.officialMobileNumber) LIKE LOWER(CONCAT('%', ?1, '%')) " +
		       "OR LOWER(e.departmentName) LIKE LOWER(CONCAT('%', ?1, '%')) " +
		       "OR LOWER(e.designation) LIKE LOWER(CONCAT('%', ?1, '%')) " +
		       "OR LOWER(e.branchName) LIKE LOWER(CONCAT('%', ?1, '%')) " +
		       "OR LOWER(e.reportingManagerName) LIKE LOWER(CONCAT('%', ?1, '%')) " +
		       "OR LOWER(e.gradeDescription) LIKE LOWER(CONCAT('%', ?1, '%')) " +
		       "OR LOWER(e.functionName) LIKE LOWER(CONCAT('%', ?1, '%'))")
		List<EmployeeMasterDetails> searchAllColumns(String keyword);



}
