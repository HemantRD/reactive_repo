package com.vinsys.hrms.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateActivity;

public interface IHRMSCandidateActivityDAO extends JpaRepository<CandidateActivity, Long> {

	// @Query("SELECT DISTINCT candidate FROM CandidateActivity candidateActivity "
	// + " LEFT JOIN candidateActivity.candidate candidate "
	// + " LEFT JOIN candidate.candidateActivityActionDetails
	// candidateActivityActionDetails "
	// + " JOIN candidate.loginEntity loginEntity " + " JOIN
	// candidate.candidateProfessionalDetail profDetail "
	// + " WHERE loginEntity.organization.id = ?1 "
	// + " AND candidate.candidateStatus = ?2 ")

	@Query("SELECT DISTINCT candidate FROM Candidate candidate "
			+ " LEFT JOIN candidate.candidateActivities candidateActivity "
			+ " LEFT JOIN candidate.candidateActivityActionDetails candidateActivityActionDetails "
			+ " JOIN candidate.loginEntity loginEntity JOIN candidate.candidateProfessionalDetail profDetail "
			+ " WHERE loginEntity.organization.id = ?1 AND ( candidate.candidateStatus = ?2 OR candidate.candidateStatus = ?5 ) "
			+ " AND profDetail.division.id = ?3 AND candidate.isActive = ?4 ")
	public List<Candidate> findAllPendingCandidateActivity(Long organizationId, String candidateStatus,
			long divisionId, String candIsActive, String candidateStatusAlt);

	@Query("SELECT DISTINCT candidate FROM CandidateActivity candidateActivity "
			+ " JOIN candidateActivity.candidate candidate JOIN candidate.candidateActivityActionDetails candidateActivityActionDetails "
			+ " JOIN candidate.loginEntity loginEntity JOIN candidate.candidateProfessionalDetail profDetail "
			+ " WHERE loginEntity.organization.id = ?1 AND profDetail.division.id = ?7 AND candidate.isActive = ?8 "
			+ " AND candidateActivityActionDetails.typeOfAction != 'ONBOARD' " + " AND " + " ( "
			+ " 	candidateActivity.masterCandidateActivity.id = ?2 " + " OR	profDetail.designation.id = ?3 "
			+ " 	OR profDetail.department.id = ?4 " + " OR	candidate.id = ?5 " + " OR	candidate.emailId like ?6 "
			+ " ) ")
	public List<Candidate> findCandidateActivityWithFilter(Long organizationId, Long masterActivityId,
			Long designationId, Long departmentId, Long candidateId, String emailId, long divisionId, String candIsActive);

	@Query("SELECT DISTINCT candidate FROM CandidateActivity candidateActivity "
			+ " JOIN candidateActivity.candidate candidate "
			+ " JOIN candidate.candidateActivityActionDetails candidateActivityActionDetails "
			+ " JOIN candidate.loginEntity loginEntity " + " JOIN candidate.candidateProfessionalDetail profDetail "
			+ " JOIN profDetail.designation designation " + " WHERE loginEntity.organization.id = ?1 "
			+ " AND candidateActivityActionDetails.typeOfAction = 'ONBOARD' ")
	public List<Candidate> findAllOnboardCandidateList(Long organizationId);

	@Query("SELECT DISTINCT candidate FROM CandidateActivity candidateActivity "
			+ " JOIN candidateActivity.candidate candidate "
			+ " JOIN candidate.candidateActivityActionDetails candidateActivityActionDetails "
			+ " JOIN candidate.loginEntity loginEntity " + " JOIN candidate.candidateProfessionalDetail profDetail "
			+ " WHERE loginEntity.organization.id = ?1 "
			+ " AND candidateActivityActionDetails.typeOfAction = 'ONBOARD' "
			+ " AND ( candidate.id = ?2 OR profDetail.dateOfJoining BETWEEN ?3 AND ?4 ) ")
	public List<Candidate> findOnboardCandidateListWithFilter(Long organizationId, Long candidateId, Date fromDate,
			Date toDate);

}
