package com.vinsys.hrms.dao;

import java.util.HashSet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.CandidateProfessionalDetail;
import com.vinsys.hrms.entity.CandidateQualification;

public interface IHRMSCandidateQualificationDAO extends JpaRepository<CandidateQualification, Long> {

	public HashSet<CandidateQualification> findBycandidateProfessionalDetail(
			CandidateProfessionalDetail candidateProfessionalDetail);
	@Query(value = "select candidate_professional_detail_id,degree from tbl_candidate_qualification where passing_year_month=(select  max(passing_year_month) from tbl_candidate_qualification \r\n"
			+ "where candidate_professional_detail_id=?1);",nativeQuery = true)
	public Object[] [] findByQualoficationByProfessionalId(Long candidateProfessionalId);
	
	/**
	 * @author monika
	 * @param candidateProfessionalId
	 * @return
	 */
	@Query(value = "select candidate_professional_detail_id,degree from tbl_candidate_qualification where passing_year_month=(select  max(passing_year_month) from tbl_candidate_qualification \r\n"
			+ "where candidate_professional_detail_id=?1);",nativeQuery = true)
	public Object[][] findByQualificationByProfessionalId(Long candidateProfessionalId);

}
