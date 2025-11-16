package com.vinsys.hrms.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.CandidateActivityActionDetail;

public interface IHRMSCandidateActivityActionDAO extends JpaRepository<CandidateActivityActionDetail, Long> {

	@Query(" select activityActionDetail from CandidateActivityActionDetail activityActionDetail "
			+ " where activityActionDetail.candidate.id = ?1")
	public List<CandidateActivityActionDetail> findActivityActionDetailByCandidate(Long id);

	@Query(" SELECT canActAction FROM CandidateActivityActionDetail canActAction "
			+ " JOIN FETCH canActAction.candidate candidate "
			+ " JOIN FETCH candidate.candidateProfessionalDetail profDetail "
			+ " JOIN FETCH profDetail.designation designation " + " JOIN candidate.loginEntity loginEntity "
			+ " WHERE canActAction.typeOfAction = ?1 " + " AND loginEntity.organization.id = ?2 ")
	public List<CandidateActivityActionDetail> findByTypeOfAction(String typeOfAction, Long orgId);

	@Query(" SELECT canActAction FROM CandidateActivityActionDetail canActAction "
			+ " JOIN canActAction.candidate candidate " + " JOIN candidate.candidateProfessionalDetail profDetail "
			+ " JOIN profDetail.designation designation " + " JOIN candidate.loginEntity loginEntity "
			+ " WHERE canActAction.typeOfAction = ?1 " + " AND loginEntity.organization.id = ?2 "
			+ " AND ( candidate.id = ?3 OR profDetail.dateOfJoining BETWEEN ?4 AND ?5 )")
	public List<CandidateActivityActionDetail> findByTypeOfActionWithFilter(String typeOfAction, Long orgId,
			Long candidateId, Date fromDate, Date toDate);
}
