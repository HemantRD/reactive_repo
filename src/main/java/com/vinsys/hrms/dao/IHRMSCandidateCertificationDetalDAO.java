package com.vinsys.hrms.dao;

import java.util.HashSet;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.CandidateCertification;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;

/**
 * DAO for candidate certification details
 * 
 * @author shome.nitin
 */
public interface IHRMSCandidateCertificationDetalDAO extends JpaRepository<CandidateCertification, Long> {

	/**
	 * To find Candidate certification by candidate professional details
	 * 
	 * @param candidateProfessionalDetail
	 * @return HashSet<CandidateCertification>
	 */
	public HashSet<CandidateCertification> findBycandidateProfessionalDetail(
			CandidateProfessionalDetail candidateProfessionalDetail);

}
