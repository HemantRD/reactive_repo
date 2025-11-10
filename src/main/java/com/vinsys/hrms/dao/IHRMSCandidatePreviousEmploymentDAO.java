package com.vinsys.hrms.dao;

import java.util.HashSet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.CandidatePreviousEmployment;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;

public interface IHRMSCandidatePreviousEmploymentDAO extends JpaRepository<CandidatePreviousEmployment, Long> {

	public HashSet<CandidatePreviousEmployment> findBycandidateProfessionalDetail(
			CandidateProfessionalDetail candidateProfessionalDetail);
	
	@Query(value="select * from tbl_candidate_previous_employment tc where tc.id=?1 and tc.is_active=?2 ",nativeQuery=true)
	public CandidatePreviousEmployment findByIdAndIsActive(Long id,String isActive);
}
