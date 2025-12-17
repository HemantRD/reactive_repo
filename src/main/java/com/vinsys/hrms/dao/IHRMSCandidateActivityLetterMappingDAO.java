package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.CandidateActivityLetterMapping;
import com.vinsys.hrms.entity.CandidateLetter;

public interface IHRMSCandidateActivityLetterMappingDAO extends JpaRepository<CandidateActivityLetterMapping, Long>{

	
	public CandidateActivityLetterMapping findBycandidateLetter(CandidateLetter letter);
}
