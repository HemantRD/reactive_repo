package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.CandidateProfessionalDetail;

public interface IHRMSCandidateProfessionalDetailDAO extends JpaRepository<CandidateProfessionalDetail, Long> {

	
	  @Query(value = "SELECT * FROM tbl_candidate_professional_detail;", nativeQuery = true)
	    List<CandidateProfessionalDetail> findAll();
	  
	  boolean existsByGradeAndIsActive(String grade, String isActive);

	boolean existsByBranchIdAndIsActive(Long id, String isactive);

	boolean existsByDepartmentIdAndIsActive(Long id, String isactive);
	
	boolean existsByDivisionIdAndIsActive(Long divisionId, String isActive);

	boolean existsByDesignationIdAndIsActive(Long id, String isactive);
	  
	  
	

}
