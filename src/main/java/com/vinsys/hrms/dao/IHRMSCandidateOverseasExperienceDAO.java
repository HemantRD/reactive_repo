package com.vinsys.hrms.dao;

import java.util.HashSet;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.CandidateOverseasExperience;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;

public interface IHRMSCandidateOverseasExperienceDAO extends JpaRepository<CandidateOverseasExperience, Long> {

	public HashSet<CandidateOverseasExperience> findBycandidateProfessionalDetail(
			CandidateProfessionalDetail candidateProfessionalDetail);
}
