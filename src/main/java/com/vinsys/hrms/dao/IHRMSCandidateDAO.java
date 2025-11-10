package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.LoginEntity;

public interface IHRMSCandidateDAO extends JpaRepository<Candidate, Long> {

	public Candidate findByemailId(String emailId);
	
	public Candidate findByemailIdAndIsActive(String emailId,String isActive);
	
	public List<Candidate> findByMobileNumberAndIsActive(Long mobileNumber,String isActive);
	
	//public Candidate findByemailIdAndCandidateId(String emailId,Long id);

	public Candidate findByloginEntity(LoginEntity entity);

	@Query("select candidate from Candidate candidate JOIN candidate.loginEntity loginEntity "
			+ " where loginEntity.organization.id = ?4 AND ((candidate.firstName like %?1% or  candidate.middleName like %?1% or candidate.lastName like  %?1% ) "
			+ " or candidate.emailId like  %?2% or candidate.candidateProfessionalDetail.aadhaarCard like %?3% )")
	public List<Candidate> findByCustomQuery(String name, String emailId, String aadharCard, long orgId,
			Pageable pageable);

	@Query("select count(candidate) from Candidate candidate JOIN candidate.loginEntity loginEntity "
			+ " where loginEntity.organization.id = ?4 AND ((candidate.firstName like %?1% or  candidate.middleName like %?1% or candidate.lastName like  %?1% ) "
			+ " or candidate.emailId like  %?2% or candidate.candidateProfessionalDetail.aadhaarCard like %?3%) and candidate.isActive =?5 ")
	public int findByCustomQuery(String name, String emailId, String aadharCard, long orgId, String isActive);

	@Query(" SELECT candidate FROM Candidate candidate JOIN candidate.loginEntity loginEntity "
			+ " JOIN candidate.candidateProfessionalDetail profDetail "
			+ " WHERE loginEntity.organization.id = ?1 AND candidate.candidateStatus = ?2 "
			+ " AND candidate.isActive = ?3 AND profDetail.division.id = ?4 ORDER BY candidate.createdDate DESC ")
	public List<Candidate> findManageCandidate(long organizationId, String candidateStatus, String candIsActive,
			long divisionId, Pageable pageable);

	@Query(" SELECT count(candidate) FROM Candidate candidate JOIN candidate.loginEntity loginEntity "
			+ " JOIN candidate.candidateProfessionalDetail profDetail "
			+ " WHERE loginEntity.organization.id = ?1 AND candidate.candidateStatus = ?2 AND candidate.isActive = ?3 "
			+ " AND profDetail.division.id = ?4 ")
	// + " ORDER BY candidate.createdDate DESC ")
	public int findManageCandidate(long organizationId, String candidateStatus, String candIsActive, long divisionId);

//	@Query(" SELECT candidate FROM Candidate candidate " + " JOIN candidate.loginEntity loginEntity "
//			+ " JOIN candidate.candidateProfessionalDetail profDetail " + " WHERE loginEntity.organization.id = ?1 "
//			+ " AND ( candidate.candidateStatus = ?2 OR candidate.candidateStatus = ?3 ) "
//			+ " AND candidate.isActive = ?4 "
//			+ " AND (( UPPER(candidate.firstName) like %?5% OR UPPER(candidate.lastName) like %?6% ) "
//			+ " AND UPPER(candidate.emailId) like %?7% AND UPPER(profDetail.aadhaarCard) like %?8% ) "
//			+ " AND profDetail.division.id = ?9 " + " ORDER BY candidate.createdDate DESC ")
//	public List<Candidate> findManageCandidateWithFilter(long organizationId, String candidateStatus1,
//			String candidateStatus2, String candIsActive, String firstName, String lastName, String emailId,
//			String aadhaarCard, long divisionId, Pageable pageable);

//	*********************************************Updated Query***********************************************************
	@Query(" SELECT candidate FROM Candidate candidate " + " JOIN candidate.loginEntity loginEntity "
			+ " JOIN candidate.candidateProfessionalDetail profDetail " + " WHERE loginEntity.organization.id = ?1 "
			+ " AND ( candidate.candidateStatus = ?2 OR candidate.candidateStatus = ?3 ) "
			+ " AND candidate.isActive = ?4 "
			+ " AND (( UPPER(candidate.firstName) like %?5% OR UPPER(candidate.lastName) like %?6% ) "
			+ " AND UPPER(candidate.emailId) like %?7% "
			+ " AND ((profDetail.aadhaarCard) = null OR UPPER(profDetail.aadhaarCard) like %?8% )) "
			+ " AND profDetail.division.id = ?9" + " ORDER BY candidate.createdDate DESC ")
	public List<Candidate> findManageCandidateWithFilter(long organizationId, String candidateStatus1,
			String candidateStatus2, String candIsActive, String firstName, String lastName, String emailId,
			String aadhaarCard, long divisionId, Pageable pageable);

//	*********************************************************************************************************

	@Query(" SELECT count(candidate) FROM Candidate candidate " + " JOIN candidate.loginEntity loginEntity "
			+ " JOIN candidate.candidateProfessionalDetail profDetail " + " WHERE loginEntity.organization.id = ?1 "
			+ " AND ( candidate.candidateStatus = ?2 OR candidate.candidateStatus = ?3 ) "
			+ " AND candidate.isActive = ?4 "
			+ " AND (( UPPER(candidate.firstName) like %?5% OR UPPER(candidate.lastName) like %?6% ) "
			+ " AND UPPER(candidate.emailId) like %?7% "
			+ "AND ((profDetail.aadhaarCard) = null OR UPPER(profDetail.aadhaarCard) like %?8% )) "
			+ " AND profDetail.division.id = ?9 ")
	// + " ORDER BY candidate.createdDate DESC ")
	public int findManageCandidateWithFilter(long organizationId, String candidateStatus1, String candidateStatus2,
			String candIsActive, String firstName, String lastName, String emailId, String aadhaarCard,
			long divisionId);

	@Query(" SELECT candidate FROM Candidate candidate " + " JOIN candidate.loginEntity loginEntity "
			+ " JOIN candidate.candidateProfessionalDetail profDetail " + " WHERE loginEntity.organization.id = ?1 "
			+ " AND ( candidate.candidateStatus = ?2 OR candidate.candidateStatus = ?3 ) "
			+ " AND candidate.isActive = ?4 ")
	public List<Candidate> findHRWorkspaceCandidateWithFilter(long organizationId, String candidateStatus1,
			String candidateStatus2, String candIsActive, long candidateId, Pageable pageable);

	@Query(" SELECT count(candidate) FROM Candidate candidate " + " JOIN candidate.loginEntity loginEntity "
			+ " JOIN candidate.candidateProfessionalDetail profDetail " + " WHERE loginEntity.organization.id = ?1 "
			+ " AND ( candidate.candidateStatus = ?2 OR candidate.candidateStatus = ?3 ) "
			+ " AND candidate.isActive = ?4 ")
	public int findHRWorkspaceCandidateWithFilter(long organizationId, String candidateStatus1, String candidateStatus2,
			String candIsActive, long candidateId);

	public int countByisActive(String isActive);

	@Query(" SELECT candidate FROM Candidate candidate JOIN candidate.loginEntity loginEntity "
			+ " JOIN candidate.candidateProfessionalDetail profDetail LEFT JOIN candidate.employee employee "
			+ " WHERE loginEntity.organization.id = ?1 AND profDetail.division.id = ?2 "
			+ " AND candidate.isActive = ?3 ")
	public List<Candidate> getAllCandidateNameIdListDAO(long organizationId, long divisionId, String candidateStatus);

	@Query(" SELECT candidate FROM Candidate candidate JOIN candidate.loginEntity loginEntity "
			+ " JOIN candidate.candidateProfessionalDetail profDetail WHERE loginEntity.organization.id = ?1 "
			+ " AND profDetail.division.id = ?2 AND candidate.candidateStatus = ?3 AND candidate.isActive = ?4 "
			+ " AND ( UPPER(candidate.firstName) like %?5% OR UPPER(candidate.lastName) like %?5% ) "
			+ " ORDER BY candidate.createdDate DESC ")
	public List<Candidate> findOnboardCandidateWithFilterName(long organizationId, long divisionId,
			String candidateStatus, String candIsActive, String candidateName, Pageable pageable);

	@Query(" SELECT count(candidate) FROM Candidate candidate JOIN candidate.loginEntity loginEntity "
			+ " JOIN candidate.candidateProfessionalDetail profDetail WHERE loginEntity.organization.id = ?1 "
			+ " AND profDetail.division.id = ?2 AND candidate.candidateStatus = ?3 AND candidate.isActive = ?4 "
			+ " AND ( UPPER(candidate.firstName) like %?5% OR UPPER(candidate.lastName) like %?5% ) ")
	public int findOnboardCandidateWithFilterName(long organizationId, long divisionId, String candidateStatus,
			String candIsActive, String candidateName);

	@Query(" SELECT candidate FROM Candidate candidate JOIN candidate.loginEntity loginEntity "
			+ " JOIN candidate.candidateProfessionalDetail profDetail WHERE loginEntity.organization.id = ?1 "
			+ " AND profDetail.division.id = ?2 AND candidate.candidateStatus = ?3 AND candidate.isActive = ?4 "
			+ " ORDER BY candidate.createdDate DESC ")
	public List<Candidate> findOnboardCandidate(long organizationId, long divisionId, String candidateStatus,
			String candIsActive, Pageable pageable);

	@Query(" SELECT count(candidate) FROM Candidate candidate JOIN candidate.loginEntity loginEntity "
			+ " JOIN candidate.candidateProfessionalDetail profDetail WHERE loginEntity.organization.id = ?1 "
			+ " AND profDetail.division.id = ?2 AND candidate.candidateStatus = ?3 AND candidate.isActive = ?4 ")
	public int findOnboardCandidate(long organizationId, long divisionId, String candidateStatus, String candIsActive);

	@Query(" SELECT candidate FROM Candidate candidate JOIN candidate.loginEntity loginEntity "
			+ " JOIN candidate.candidateProfessionalDetail profDetail WHERE loginEntity.organization.id = ?1 "
			+ " AND profDetail.division.id = ?2 AND candidate.candidateStatus = ?3 AND candidate.isActive = ?4 "
			+ " AND ( profDetail.dateOfJoining BETWEEN TO_DATE(?5,'yyyy-MM-dd') AND TO_DATE(?6, 'yyyy-MM-dd') ) "
			+ " ORDER BY candidate.createdDate DESC ")
	public List<Candidate> findOnboardCandidateWithFilterDOJ(long organizationId, long divisionId,
			String candidateStatus, String candIsActive, String fromDOJ, String toDOJ, Pageable pageable);

	@Query(" SELECT count(candidate) FROM Candidate candidate JOIN candidate.loginEntity loginEntity "
			+ " JOIN candidate.candidateProfessionalDetail profDetail WHERE loginEntity.organization.id = ?1 "
			+ " AND profDetail.division.id = ?2 AND candidate.candidateStatus = ?3 AND candidate.isActive = ?4 "
			+ " AND ( profDetail.dateOfJoining BETWEEN TO_DATE(?5,'yyyy-MM-dd') AND TO_DATE(?6,'yyyy-MM-dd') ) ")
	public int findOnboardCandidateWithFilterDOJ(long organizationId, long divisionId, String candidateStatus,
			String candIsActive, String fromDOJ, String toDOJ);

	@Query(" SELECT candidate FROM Candidate candidate JOIN candidate.loginEntity loginEntity "
			+ " JOIN candidate.candidateProfessionalDetail profDetail WHERE loginEntity.organization.id = ?1 "
			+ " AND profDetail.division.id = ?2 AND candidate.candidateStatus = ?3 AND candidate.isActive = ?4 "
			+ " AND ( UPPER(candidate.firstName) like %?5% OR UPPER(candidate.lastName) like %?5% ) "
			+ " AND profDetail.dateOfJoining BETWEEN TO_DATE(?6,'yyyy-MM-dd') AND TO_DATE(?7, 'yyyy-MM-dd') "
			+ " ORDER BY candidate.createdDate DESC ")
	public List<Candidate> findOnboardCandidateWithFilterNameDOJ(long organizationId, long divisionId,
			String candidateStatus, String candIsActive, String candidateName, String fromDOJ, String toDOJ,
			Pageable pageable);

	@Query(" SELECT count(candidate) FROM Candidate candidate JOIN candidate.loginEntity loginEntity "
			+ " JOIN candidate.candidateProfessionalDetail profDetail WHERE loginEntity.organization.id = ?1 "
			+ " AND profDetail.division.id = ?2 AND candidate.candidateStatus = ?3 AND candidate.isActive = ?4 "
			+ " AND ( UPPER(candidate.firstName) like %?5% OR UPPER(candidate.lastName) like %?5% ) "
			+ " AND profDetail.dateOfJoining BETWEEN TO_DATE(?6,'yyyy-MM-dd') AND TO_DATE(?7,'yyyy-MM-dd') ")
	public int findOnboardCandidateWithFilterNameDOJ(long organizationId, long divisionId, String candidateStatus,
			String candIsActive, String candidateName, String fromDOJ, String toDOJ);

	@Query(" SELECT candidate FROM Candidate candidate JOIN FETCH candidate.loginEntity le "
			+ " JOIN FETCH candidate.candidateProfessionalDetail profDetail "
			+ "WHERE candidate.emailId = ?1 AND candidate.isActive = ?2 ")
	public Candidate findCandidateByEmailId(String emailId, String isActive);

	@Query(" SELECT candidate FROM Candidate candidate " + " JOIN FETCH candidate.employmentType "
			+ " WHERE candidate.id = ?1")
	public Candidate findCandidateEmploymentStatusByCandId(long candidateId);

	@Query("select candidate from Candidate candidate where candidate.id= ?1 and candidate.isActive = ?2")
	public Candidate findByIdAndIsActive(Long id, String isActive);

	@Query("select candidate from Candidate candidate where candidate.id= ?1 and candidate.isActive = ?2 and candidate.orgId=?3")
	public Candidate findByIdAndIsActiveAndorgId(Long id, String isActive, Long orgId);
	/*
	 * @Query("select emp from Employee emp " + " join fetch emp.candidate cand" +
	 * " join fetch cand.loginEntity logEnt" //+
	 * "join fetch cand.employmentType empType "
	 * +" join fetch emp.employeeReportingManager erm where cand.employmentType.employmentTypeDescription = ?2 AND emp.isActive = ?1 "
	 * ) public List<Employee> findProbationaryCandidate(String isActive,String
	 * ProbationCode);
	 */

	/*
	 * @Query("select erm from Employee emp " + " join fetch emp.candidate cand" +
	 * " join fetch cand.candidateProfessionalDetail cpd" //+
	 * "join fetch cand.employmentType empType "
	 * +" join fetch emp.employeeReportingManager erm where cand.employmentType.employmentTypeDescription = ?2 AND emp.isActive = ?1 and (now() - cpd.dateOfJoining)>15"
	 * + "group by erm.reporingManager.id") public List<Employee>
	 * findProbationaryCandidate(String isActive,String ProbationCode);
	 */

	/*
	 * @Query(value ="select rept.reporting_manager_id ,count(emp.id) \r\n" +
	 * "from tbl_employee emp\r\n" +
	 * " left JOIN tbl_candidate cand on cand.id=emp.candidate_id\r\n " +
	 * " left JOIN tbl_mst_employment_type emplType on emplType.id=cand.employment_type_id "
	 * +
	 * " left JOIN tbl_candidate_professional_detail cpd on cpd.candidate_id=cand.id\r\n "
	 * +
	 * " left JOIN tbl_employee_reporting_manager rept on rept.employee_id=emp.id\r\n "
	 * + " where emplType.employment_type_description=?2 and emp.is_active=?1 and "
	 * 
	 * +
	 * "(DATE_PART('day', CURRENT_DATE)  - DATE_PART('day',cpd.date_of_joining)-\r\n"
	 * + "   (CAST (emp.probation_period AS INTEGER) * 30)) >= 15\r\n" +
	 * "group by rept.reporting_manager_id ",nativeQuery = true) public
	 * List<Employee> findProbationaryCandidate(String isActive,String
	 * ProbationCode);
	 */

	@Query(value = " SELECT rept.reporting_manager_id, count(emp.id)  " + "      FROM   tbl_employee emp "
			+ "        JOIN tbl_candidate cand " + "              ON cand.id = emp.candidate_id "
			+ "         JOIN tbl_candidate_professional_detail cpd " + "              ON cpd.candidate_id = cand.id "
			+ "        JOIN tbl_employee_reporting_manager rept " + "              ON rept.employee_id = emp.id " +

			"		 JOIN tbl_mst_employment_type emplType " + "		       ON emplType.id=cand.employment_type_id"
			+ "    WHERE  emplType.employment_type_description = ?2 "
			+ "       AND emp.is_active = ?1 AND rept.is_active=?3 " +

			"       AND  " + "       extract('day' from( Now() - cpd.date_of_joining)) >= "
			+ "       ((Cast ( emp.probation_period AS INTEGER) * 30)-?4) "
			+ "  GROUP  BY rept.reporting_manager_id  ", nativeQuery = true)
	public List<Object[]> findProbationaryCandidate(String isActive, String probationCode, String isManagerActive,
			int reminderNoofDays);

	@Query(value = " SELECT rept.reporting_manager_id,emp.id  " + "      FROM   tbl_employee emp "
			+ "        JOIN tbl_candidate cand " +

			"              ON cand.id = emp.candidate_id " + "         JOIN tbl_candidate_professional_detail cpd "
			+ "              ON cpd.candidate_id = cand.id " + "        JOIN tbl_employee_reporting_manager rept "
			+ "              ON rept.employee_id = emp.id " + "		 JOIN tbl_mst_employment_type emplType "
			+ "		       ON emplType.id=cand.employment_type_id" +

			"    WHERE  emplType.employment_type_description = ?2 " + "       AND emp.is_active = ?1  "
			+ "      AND rept.reporting_manager_id=?4" +

			"       AND  " + "       extract('day' from( Now() - cpd.date_of_joining)) >= "
			+ "       ((Cast ( emp.probation_period AS INTEGER) * 30)-?3) ", nativeQuery = true)
	public List<Object[]> findProbationdaryEmployeeUSingRO(String isActive, String probationCode, int reminderNoofDays,
			long reptManagerId);

	/**
	 * Query Added By Ritesh Kolhe
	 */
	/*
	 * @Query(value
	 * =" SELECT emp.id,cpd.date_of_joining,emp.probation_period,  + \" Extract('day' FROM( Now() - cpd.date_of_joining ))AS prb_days, \" + \" ( ( Cast (emp.probation_period AS INTEGER) * 30 ) - ?3 ) AS prb_period \""
	 * + "      FROM   tbl_employee emp " + "        JOIN tbl_candidate cand " +
	 * "              ON cand.id = emp.candidate_id " +
	 * "         JOIN tbl_candidate_professional_detail cpd " +
	 * "              ON cpd.candidate_id = cand.id " + //
	 * "        JOIN tbl_employee_reporting_manager rept " + //
	 * "              ON rept.employee_id = emp.id " +
	 * "		 JOIN tbl_mst_employment_type emplType " +
	 * "		       ON emplType.id=cand.employment_type_id" +
	 * 
	 * "    WHERE  emplType.employment_type_description = ?2 " +
	 * "       AND emp.is_active = ?1  " +
	 * 
	 * "       AND  " +
	 * "       extract('day' from( Now() - cpd.date_of_joining)) >= " +
	 * "       ((Cast ( emp.probation_period AS INTEGER) * 30)-?3) ",nativeQuery =
	 * true) public List<Object[]> findProbationdaryEmployee(String isActive,String
	 * probationCode,int reminderNoofDays);
	 */

	/*
	 * @Query(value
	 * =" SELECT emp.id,cand.id AS CANDIDATE_ID, cand.first_name, cand.last_name, emp.official_email_id,cpd.division_id,lge.organization_id, cpd.date_of_joining,emp.probation_period,  + \" Extract('day' FROM( Now() - cpd.date_of_joining )) as cd, \" + \" ( ( Cast (emp.probation_period AS INTEGER) * 30 ) + ?3 ) as dd \""
	 * + "      FROM   tbl_employee emp " + "        JOIN tbl_candidate cand " +
	 * "              ON cand.id = emp.candidate_id " +
	 * "         JOIN tbl_candidate_professional_detail cpd " +
	 * "              ON cpd.candidate_id = cand.id " +
	 * "        JOIN tbl_employee_reporting_manager rept " +
	 * "              ON rept.employee_id = emp.id " +
	 * "		 JOIN tbl_mst_employment_type emplType " +
	 * "		       ON emplType.id=cand.employment_type_id" +
	 * "		 JOIN tbl_login_entity lge"+
	 * "			   ON cand.login_entity_id = lge.id" +
	 * "    WHERE  emplType.employment_type_description = ?2 " +
	 * "       AND emp.is_active = ?1  " +
	 * 
	 * "       AND  " +
	 * "       extract('day' FROM( Now() - cpd.date_of_joining )) > " +
	 * "       (( Cast (emp.probation_period AS INTEGER) * 30 ) + ?3 ) ",nativeQuery
	 * = true) public List<Object[]> findProbationdaryEmployee(String
	 * isActive,String probationCode,int reminderNoofDays);
	 */
	@Query(value = "SELECT emp.id,cand.id AS CANDIDATE_ID, cand.first_name, cand.last_name, emp.official_email_id,cpd.division_id,lge.organization_id, cpd.date_of_joining,emp.probation_period,"
			+ " Extract('day' FROM( Now() - cpd.date_of_joining )), "
			+ " ( ( Cast (emp.probation_period AS INTEGER) * 30 ) + ?3 ) " + " FROM   tbl_employee emp "
			+ " JOIN tbl_candidate cand " + " ON cand.id = emp.candidate_id  "
			+ " JOIN tbl_candidate_professional_detail cpd " + " ON cpd.candidate_id = cand.id "
			+ " JOIN tbl_employee_reporting_manager rept " + " ON rept.employee_id = emp.id "
			+ " JOIN tbl_mst_employment_type emplType " + " ON emplType.id = cand.employment_type_id  "
			+ " JOIN tbl_login_entity lge" + " ON cand.login_entity_id = lge.id"
			+ " WHERE  emplType.employment_type_description = ?2 " + " AND emp.is_active = ?1 "
			+ " AND extract('day' FROM( Now() - cpd.date_of_joining )) > (( Cast (emp.probation_period AS INTEGER) * 30 ) + ?3 ) ", nativeQuery = true)
	public List<Object[]> findProbationdaryEmployee(String isActive, String probationCode, int reminderNoofDays);

//	@Query(value = "SELECT emp.id,cand.id AS CANDIDATE_ID, cand.first_name, cand.last_name, emp.official_email_id,cpd.division_id,lge.organization_id, cpd.date_of_joining,"
//			+ " Extract('day' FROM( Now() - cpd.date_of_joining )), " + " ( ( Cast (emp.probation_period AS INTEGER) * 30 ) - ?3 ) "
//			+ " FROM   tbl_employee emp "
//			+ " JOIN tbl_candidate cand "
//			+ " ON cand.id = emp.candidate_id  "
//			+ " JOIN tbl_candidate_professional_detail cpd "
//			+ " ON cpd.candidate_id = cand.id "
//			+ " JOIN tbl_employee_reporting_manager rept "
//			+ " ON rept.employee_id = emp.id "
//			+ " JOIN tbl_mst_employment_type emplType "
//			+ " ON emplType.id = cand.employment_type_id  "
//			+ " JOIN tbl_login_entity lge"
//			+ " ON cand.login_entity_id = lge.id"
//			+ " WHERE  emplType.employment_type_description = ?2 "
//			+ " AND emp.is_active = ?1 "  
//			+ "   AND (Extract('day' FROM( (SELECT cpd.date_of_joining + INTERVAL '6 month') - cpd.date_of_joining )))-5=Extract('day' FROM( Now() - cpd.date_of_joining ))", nativeQuery = true)  
//	public List<Object[]> findEmployeeForProbationNotification(String isActive, String candidateProbationStatus, int reminderNoofDays);

	@Query(value = "SELECT emp.id,cand.id AS CANDIDATE_ID, cand.first_name, cand.last_name, emp.official_email_id,cpd.division_id,lge.organization_id, cpd.date_of_joining,emp.probation_period,"
			+ " Extract('day' FROM( Now() - cpd.date_of_joining )), "
			+ " ( ( Cast (emp.probation_period AS INTEGER) * 30 ) - ?3 ) " + " FROM   tbl_employee emp "
			+ " JOIN tbl_candidate cand " + " ON cand.id = emp.candidate_id  "
			+ " JOIN tbl_candidate_professional_detail cpd " + " ON cpd.candidate_id = cand.id "
			+ " JOIN tbl_employee_reporting_manager rept " + " ON rept.employee_id = emp.id "
			+ " JOIN tbl_mst_employment_type emplType " + " ON emplType.id = cand.employment_type_id  "
			+ " JOIN tbl_login_entity lge" + " ON cand.login_entity_id = lge.id"
			+ " WHERE  emplType.employment_type_description = ?2 " + " AND emp.is_active = ?1 "
			// + "AND (cpd.date_of_joining + (INTERVAL '1 month'* ( Cast
			// (emp.probation_period AS INTEGER))))\\:\\:date= (now())\\:\\:date"
			+ "AND  ( cpd.date_of_joining + (interval '1 month'* ( cast (emp.probation_period AS integer))))\\:\\:date= (now())\\:\\:date", nativeQuery = true)
	public List<Object[]> findEmployeeForProbationNotification(String isActive, String candidateProbationStatus,
			int reminderNoofDays);

	// 90day REtiterment Schedular Query
	// Author:Monika

	@Query(value = "select cemp.employee_id,cand.id AS CANDIDATE_ID,cand.first_name, cand.last_name,emp.official_email_id,\r\n"
			+ "			cpd.division_id,lge.organization_id,cemp.retirement_date\r\n"
			+ "						from tbl_employee_current_detail cemp\r\n"
			+ "					    JOIN tbl_employee emp \r\n"
			+ "						ON emp.id = cemp.employee_id\r\n"
			+ "						JOIN tbl_candidate cand\r\n"
			+ "						ON cand.id = emp.candidate_id\r\n"
			+ "						 JOIN tbl_candidate_professional_detail cpd\r\n"
			+ "					    ON cpd.candidate_id = cand.id \r\n"
			+ "						 JOIN tbl_mst_employment_type emplType \r\n"
			+ "						 ON emplType.id = cand.employment_type_id\r\n"
			+ "						 JOIN tbl_login_entity lge\r\n"
			+ "						 ON cand.login_entity_id = lge.id\r\n"
			+ "						where EXTRACT(year from CURRENT_TIMESTAMP )=extract(year from (retirement_date - INTERVAL '90 DAYS'))\r\n"
			+ "					 and EXTRACT(month from CURRENT_TIMESTAMP )=extract(month from (retirement_date - INTERVAL '90 DAYS'))\r\n"
			+ "					  and EXTRACT(day from CURRENT_TIMESTAMP )=extract(day from (retirement_date - INTERVAL '90 DAYS'))\r\n"
			+ "						 and cemp.is_active =?1\r\n" + "", nativeQuery = true)
	public List<Object[]> findEmployeeForRetirement(String isActive);

	// 7day REtiterment Schedular Query
	// Author:Monika
	@Query(value = "select cemp.employee_id,cand.id AS CANDIDATE_ID,cand.first_name, cand.last_name,emp.official_email_id,\r\n"
			+ "	cpd.division_id,lge.organization_id,cemp.retirement_date\r\n"
			+ "			from tbl_employee_current_detail cemp\r\n" + "		    JOIN tbl_employee emp \r\n"
			+ "			ON emp.id = cemp.employee_id\r\n" + "			JOIN tbl_candidate cand\r\n"
			+ "			ON cand.id = emp.candidate_id\r\n" + "			 JOIN tbl_candidate_professional_detail cpd\r\n"
			+ "		    ON cpd.candidate_id = cand.id \r\n" + "			 JOIN tbl_mst_employment_type emplType \r\n"
			+ "			 ON emplType.id = cand.employment_type_id\r\n" + "			 JOIN tbl_login_entity lge\r\n"
			+ "			 ON cand.login_entity_id = lge.id\r\n"
			+ "			where EXTRACT(year from CURRENT_TIMESTAMP )=extract(year from (retirement_date - INTERVAL '7 DAYS'))\r\n"
			+ "			 and EXTRACT(month from CURRENT_TIMESTAMP )=extract(month from (retirement_date - INTERVAL '7 DAYS'))\r\n"
			+ "			  and EXTRACT(day from CURRENT_TIMESTAMP )=extract(day from (retirement_date - INTERVAL '7 DAYS'))\r\n"
			+ "			 and cemp.is_active =?1", nativeQuery = true)
	public List<Object[]> findEmployeeForRetirementCompletion(String isActive);

	// added by monika
	@Query(value = " select  emp.id,emp.official_email_id,emp.is_active,can.date_of_birth,can.email_id,can.first_name,can.middle_name\r\n"
			+ "		,can.last_name,can.mobile_number,can.gender,cpd.aadhaar_card,cpd.date_of_joining,cpd.pan_card,\r\n"
			+ "		cpd.branch_id,cpd.division_id,cpd.department_id,cpdt.marital_status,erm.reporting_manager_id,emp.candidate_id\r\n"
			+ "		from tbl_employee emp  join tbl_candidate can\r\n" + "		on can.id=emp.candidate_id\r\n"
			+ "		join tbl_candidate_professional_detail cpd\r\n" + "		on cpd.candidate_id=emp.candidate_id\r\n"
			+ "		join tbl_candidate_personal_detail cpdt\r\n" + "		on cpdt.candidate_id=emp.candidate_id\r\n"
			+ "		join tbl_employee_reporting_manager erm\r\n"
			+ "		on erm.employee_id=emp.id", nativeQuery = true)
	public List<Object[]> findEmployeeDetails();

	/**
	 * @author Monika Gargote.
	 * @param isActive
	 * @param probationCode
	 * @param isManagerActive
	 * @param reminderNoofDays
	 * @return
	 */
	@Query(value = " SELECT rept.reporting_manager_id, count(emp.id)  " + "      FROM   tbl_employee emp "
			+ "        JOIN tbl_candidate cand " + "              ON cand.id = emp.candidate_id "
			+ "         JOIN tbl_candidate_professional_detail cpd " + "              ON cpd.candidate_id = cand.id "
			+ "        JOIN tbl_employee_reporting_manager rept " + "              ON rept.employee_id = emp.id "
			+ "JOIN tbl_probation_feedback tpf ON tpf.employee_id = emp.id"
			+ "		 JOIN tbl_mst_employment_type emplType " + "		       ON emplType.id=cand.employment_type_id"
			+ "    WHERE  emplType.employment_type_description = ?2 "
			+ "       AND emp.is_active = ?1 AND rept.is_active=?3 " +

			"       AND  " + "       extract('day' from( Now() - cpd.date_of_joining)) >= "
			+ "       ((Cast ( emp.probation_period AS INTEGER) * 30)-?4) "
			+ " AND tpf.ro_submitted='false' GROUP  BY rept.reporting_manager_id ", nativeQuery = true)
	public List<Object[]> findProbationaryCandidateToHR(String isActive, String probationCode, String isManagerActive,
			int reminderNoofDays);

	/**
	 * @author Monika Gargote.
	 * @param isActive
	 * @param probationCode
	 * @param reminderNoofDays
	 * @param reptManagerId
	 * @return
	 */
	@Query(value = "SELECT rept.reporting_manager_id,emp.id  \r\n" + "			     FROM   tbl_employee emp \r\n"
			+ "			      JOIN tbl_candidate cand \r\n"
			+ "			              ON cand.id = emp.candidate_id  \r\n"
			+ "			       JOIN tbl_candidate_professional_detail cpd  \r\n"
			+ "			            ON cpd.candidate_id = cand.id  \r\n"
			+ "			        JOIN tbl_employee_reporting_manager rept \r\n"
			+ "			            ON rept.employee_id = emp.id \r\n"
			+ "				 JOIN tbl_mst_employment_type emplType \r\n"
			+ "            ON emplType.id=cand.employment_type_id\r\n"
			+ "			JOIN tbl_probation_feedback tpf ON tpf.employee_id = emp.id\r\n"
			+ "			  WHERE  emplType.employment_type_description =?2 \r\n" + "			  AND emp.is_active =?1\r\n"
			+ "			AND rept.reporting_manager_id=?4  AND  tpf.ro_submitted='false'AND tpf.is_active='Y' AND\r\n"
			+ "			 extract('day' from( Now() - cpd.date_of_joining)) >=  \r\n"
			+ "			((Cast ( emp.probation_period AS INTEGER) * 30)-?3)", nativeQuery = true)
	public List<Object[]> findProbationdaryEmployeeUSingROToHR(String isActive, String probationCode,
			int reminderNoofDays, long reptManagerId);

	// Added By Monika
	@Query(value = "SELECT emp.id,cpd.division_id,lge.organization_id,emp.official_email_id,cpd.date_of_joining,emp.probation_period\r\n"
			+ "	FROM   tbl_employee emp\r\n" + "	JOIN tbl_candidate cand\r\n"
			+ "	ON cand.id = emp.candidate_id\r\n" + "	JOIN tbl_candidate_professional_detail cpd\r\n"
			+ "	ON cpd.candidate_id = cand.id\r\n" + "	JOIN tbl_mst_employment_type emplType\r\n"
			+ "	ON emplType.id=cand.employment_type_id\r\n" + "	JOIN tbl_login_entity lge\r\n"
			+ "	ON cand.login_entity_id = lge.id\r\n" + "WHERE  emplType.employment_type_description = ?2\r\n"
			+ "AND emp.is_active = ?1 AND extract('day' from( Now() - cpd.date_of_joining)) >=((Cast ( emp.probation_period AS INTEGER) * 30))", nativeQuery = true)
	public List<Object[]> findProbationaryCandidate(String isActive, String probationCode);

	@Query(value="select * from tbl_candidate tc join tbl_candidate_professional_detail tcpd ON tcpd.candidate_id =tc.id where tc.org_id=?1 and tc.is_active =?2 order by tc.id  desc",nativeQuery=true)
	public List<Candidate> findByOrgIdAndIsActive(Long orgId,String isActive,Pageable pageable);
	
	
	@Query(value="select count(*) from tbl_candidate tc join tbl_candidate_professional_detail tcpd ON tcpd.candidate_id =tc.id where tc.org_id=?1 and tc.is_active =?2",nativeQuery=true)
	public long countOfprofiles(Long orgId,String isActive);
	
	@Query(value = "SELECT COUNT(cand.*) " +
            "FROM tbl_candidate cand " +
            "JOIN tbl_candidate_professional_detail tcpd ON tcpd.candidate_id = cand.id " +
            "JOIN tbl_login_entity tle ON tle.id = cand.login_entity_id " +
            "JOIN tbl_mst_department tmd ON tmd.id = tcpd.department_id " +
            "JOIN tbl_mst_designation tmd2 ON tmd2.id = tcpd.designation_id " +
            "join tbl_mst_employment_type employeement on employeement.id =cand.employment_type_id " +
            "WHERE tle.org_id = ?1 " +
            "AND cand.is_active = ?2 " +
            "AND (CAST(cand.id AS TEXT) ILIKE CONCAT(?3, '%') " +
            "OR tmd2.designation_name ILIKE CONCAT(?3, '%') " +
            "OR tmd.department_name ILIKE CONCAT(?3, '%') " +
            "OR cand.first_name ILIKE CONCAT(?3, '%') " +
            "OR employeement.employment_type_name  ILIKE CONCAT(?3, '%')" +
            "OR cand.last_name ILIKE CONCAT(?3, '%'))", nativeQuery = true)
	public long countOfprofilesByKeyword(Long orgId,String isActive,String keyword);
	
	@Query(value = "SELECT * FROM tbl_candidate cand " +
            "JOIN tbl_candidate_professional_detail tcpd ON tcpd.candidate_id = cand.id " +
            "JOIN tbl_login_entity tle ON tle.id = cand.login_entity_id " +
            "JOIN tbl_mst_department tmd ON tmd.id = tcpd.department_id " +
            "JOIN tbl_mst_designation tmd2 ON tmd2.id = tcpd.designation_id " +
            "join tbl_mst_employment_type employeement on employeement.id =cand.employment_type_id " +
            "WHERE tle.org_id = ?1 " +
            "AND cand.is_active = ?2 " +
            "AND (CAST(cand.id AS TEXT) ILIKE CONCAT(?3, '%') " +
            "OR tmd2.designation_name ILIKE CONCAT(?3, '%') " +
            "OR tmd.department_name ILIKE CONCAT(?3, '%') " +
            "OR cand.first_name ILIKE CONCAT(?3, '%') " +
            "OR cand.last_name ILIKE CONCAT(?3, '%') " +
            "OR employeement.employment_type_name  ILIKE CONCAT(?3, '%'))" +
            "ORDER BY cand.id ASC " +
            "LIMIT ?#{#pageable.pageSize} OFFSET ?#{#pageable.offset}",
    nativeQuery = true)
	public List<Candidate> findByOrgIdAndIsActiveAndKeyword(Long orgId,String isActive,String keyword,Pageable pageable);

	
	
	@Query(value="select * from tbl_candidate where email_id  like %?1% ",nativeQuery=true)
	public List<Candidate> findByEmailLike(String email);

	public boolean existsByEmploymentTypeIdAndIsActive(Long id, String isactive);
	
	@Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END " + "FROM tbl_candidate c "
			+ "WHERE LOWER(TRIM(c.title)) = LOWER(TRIM(:title)) " + "  AND c.is_active = :isActive", nativeQuery = true)
	boolean existsByTitleAndIsActive(@Param("title") String title, @Param("isActive") String isActive);
}
