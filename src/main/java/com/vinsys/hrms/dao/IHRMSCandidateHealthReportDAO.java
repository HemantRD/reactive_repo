package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.CandidateHealthReport;
import com.vinsys.hrms.entity.CandidatePersonalDetail;

public interface IHRMSCandidateHealthReportDAO extends JpaRepository<CandidateHealthReport, Long> {

	public CandidateHealthReport findBycandidatePersonalDetail(CandidatePersonalDetail candidatePersonalDetail);

}
