package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;

@Transactional(rollbackFor = Exception.class)
public interface IHRMSProfessionalDetailsDAO extends JpaRepository<CandidateProfessionalDetail, Long> {

	public CandidateProfessionalDetail findBycandidate(Candidate candidate);

	/**
	 * @author monika
	 * @param candidateId
	 * @return
	 */
	public CandidateProfessionalDetail findByCandidateId(Long candidateId);

}
