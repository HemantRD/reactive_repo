package com.vinsys.hrms.dao;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.CandidateOverseasExperience;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;

public interface IHRMSCandidateOverseasExpDAO extends JpaRepository<CandidateOverseasExperience, Long> {

	public Set<CandidateOverseasExperience> findBycandidateProfessionalDetail(
			CandidateProfessionalDetail professionalDetails);
}
