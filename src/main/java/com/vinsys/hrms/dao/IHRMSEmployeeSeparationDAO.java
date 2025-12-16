package com.vinsys.hrms.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeSeparationDetails;

public interface IHRMSEmployeeSeparationDAO extends JpaRepository<EmployeeSeparationDetails, Long> {

	@Query("select empSeparationDetails from EmployeeSeparationDetails empSeparationDetails where empSeparationDetails.employee.id=?1 and empSeparationDetails.isActive=?2")
	public List<EmployeeSeparationDetails> findSeparationDetailsByEmpId(long id, String isActive);

	@Query(" SELECT ela FROM EmployeeSeparationDetails ela JOIN FETCH ela.employee emp JOIN FETCH emp.candidate cand "
			+ " JOIN emp.employeeReportingManager erm  JOIN FETCH ela.empSeparationReason esre JOIN FETCH cand.candidateProfessionalDetail cpd JOIN FETCH cpd.designation desg JOIN FETCH cand.loginEntity cle JOIN FETCH cle.organization"
			+ " WHERE cand.isActive = ?1 AND emp.isActive = ?2 AND ela.status= ?3")
	public List<EmployeeSeparationDetails> getResignedEmployeeByStatus(String candIsActive, String empIsActive,
			String separationStatusSeq);

	@Query("select empSeparationDetails from EmployeeSeparationDetails empSeparationDetails  where empSeparationDetails.status=?1 and empSeparationDetails.employee.id=?2")
	public EmployeeSeparationDetails findSeparationDetailsUsingEmpIdandStatus(String status, long empId);

	@Query("select empSeparationDetails from EmployeeSeparationDetails empSeparationDetails  where empSeparationDetails.isActive=?1 and empSeparationDetails.employee.id=?2")
	public EmployeeSeparationDetails findSeparationDetailsUsingEmpIdandActive(String status, long empId);

	@Query(" SELECT ela FROM EmployeeSeparationDetails ela JOIN FETCH ela.employee emp JOIN FETCH emp.candidate cand "
			+ " JOIN emp.employeeReportingManager erm JOIN cand.loginEntity le JOIN le.organization org "
			+ " WHERE cand.isActive = ?1 AND emp.isActive = ?2 AND erm.reporingManager.id = ?3 "
			+ " AND (ela.status=?4 OR ela.status=?5 ) AND org.id = ?6")
	public List<EmployeeSeparationDetails> findSubordinateResignationApplied(String candIsActive, String empIsActive,
			long managerId, String separationStatusSeq1, String separationStatusSeq2,
			long orgId/* , Pageable Pageable */);

	@Query(" SELECT ela FROM EmployeeSeparationDetails ela JOIN FETCH ela.employee emp JOIN FETCH emp.candidate cand "
			+ " JOIN emp.employeeReportingManager erm JOIN cand.loginEntity le JOIN le.organization org "
			+ " WHERE ( cand.isActive = ?1 AND emp.isActive = ?2)"
			+ " AND ( ela.status=?3 OR ela.status=?4 ) AND org.id=?5")
	public List<EmployeeSeparationDetails> findAllResignationAppliedEmployee(String candIsActive, String empIsActive,
			String separationStatusSeq1, String separationStatusSeq2, long orgId/* , Pageable Pageable */);

	@Query("SELECT emp FROM Employee emp JOIN FETCH emp.employeeSeparationDetails esd JOIN Fetch emp.candidate cand "
			+ " JOIN FETCH cand.loginEntity cle "
			+ " WHERE cle.organization.id = ?1 and esd.isActive=?2 order by esd.actualRelievingDate desc")
	public List<Employee> getAllSeparationEmpNameIdByOrg(long orgId, String isActive);

	@Query("select es from EmployeeSeparationDetails es " + " JOIN FETCH Employee  emp on es.employee.id =  emp.id "
			+ " JOIN FETCH Candidate can on emp.candidate.id = can.id  "
			+ " JOIN FETCH CandidateProfessionalDetail cpd on cpd.candidate.id = can.id  "
			+ " JOIN FETCH LoginEntity le on le.id = can.loginEntity.id "
			+ " JOIN FETCH Organization org on org.id = le.organization.id "
			+ " where ((cpd.department.id = ?2 or cpd.designation.id =?4 or cpd.branch.id = ?5 or cpd.division.id = ?3  "
			+ " or ( es.actualRelievingDate  BETWEEN ?6  and ?7 ) ) or emp.id =?1 or es.status = ?8 )and es.isActive = ?9"
			+ " and org.id = ?10 order by es.resignationDate desc")
	public List<EmployeeSeparationDetails> findSeperatedEmployeeWithFilters(Long employeeCode, Long departmentId,
			Long divisionId, Long designationId, Long branchId, Date fromDate, Date toDate, String status,
			String isActive, long orgId/* , Pageable pageable */);

	@Query("select es from EmployeeSeparationDetails es " + " JOIN FETCH Employee  emp on es.employee.id =  emp.id "
			+ " JOIN FETCH Candidate can on emp.candidate.id = can.id  "
			+ " JOIN FETCH CandidateProfessionalDetail cpd on cpd.candidate.id = can.id  "
			+ " JOIN FETCH LoginEntity le on le.id = can.loginEntity.id "
			+ " JOIN FETCH Organization org on org.id = le.organization.id "
			+ " where ((cpd.department.id = ?2 or cpd.designation.id =?4 or cpd.branch.id = ?5 or cpd.division.id = ?3  "
			+ "   ) or emp.id =?1 or es.status = ?6 )and es.isActive = ?7 AND org.id = ?8"
			+ " order by es.resignationDate desc")
	public List<EmployeeSeparationDetails> findSeperatedEmployeeWithFilters(Long employeeCode, Long departmentId,
			Long divisionId, Long designationId, Long branchId, String status, String isActive,
			long orgId/*
						 * , Pageable pageable
						 */);

	public List<EmployeeSeparationDetails> findByisActive(String isActive);

	@Query(" select empSeparationDetails from EmployeeSeparationDetails empSeparationDetails  "
			+ " where empSeparationDetails.status !=?1 and empSeparationDetails.employee.id=?2 and empSeparationDetails.isActive=?3")
	public EmployeeSeparationDetails findSeparationDetailsUsingEmpIdAndStatusNoReject(String status, long empId,
			String isActive);

	@Query("select empSepDet from EmployeeSeparationDetails empSepDet "
			+ " JOIN empSepDet.employee emp JOIN emp.candidate cand JOIN cand.loginEntity le "
			+ " JOIN le.organization org "
			+ " where (empSepDet.status = ?1 or empSepDet.status = ?2 or empSepDet.status = ?3 or empSepDet.status = ?4) "
			+ " and empSepDet.isActive = ?5 AND org.id = ?6 order by empSepDet.resignationDate desc")
	public List<EmployeeSeparationDetails> findAllByStatus(String absconded, String terminated, String pending,
			String demise, String isActive, long orgId/* , Pageable pageable */);

	@Query(" select empSeparationDetails from EmployeeSeparationDetails empSeparationDetails  "
			+ " where  empSeparationDetails.employee.id=?1")
	public List<EmployeeSeparationDetails> findByemployeeId(long empId);

	@Query("select count(es) from EmployeeSeparationDetails es "
			+ " JOIN FETCH Employee  emp on es.employee.id =  emp.id "
			+ " JOIN FETCH Candidate can on emp.candidate.id = can.id  "
			+ " JOIN FETCH CandidateProfessionalDetail cpd on cpd.candidate.id = can.id  "
			+ " JOIN FETCH LoginEntity le on le.id = can.loginEntity.id "
			+ " JOIN FETCH Organization org on org.id = le.organization.id "
			+ " where ((cpd.department.id = ?2 or cpd.designation.id =?4 or cpd.branch.id = ?5 or cpd.division.id = ?3  "
			+ " or ( es.actualRelievingDate  BETWEEN ?6  and ?7 ) ) or emp.id =?1 or es.status = ?8 )and es.isActive = ?9"
			+ " AND org.id = ?10")
	public int findSeperatedEmployeeWithFiltersForCount(Long employeeCode, Long departmentId, Long divisionId,
			Long designationId, Long branchId, Date fromDate, Date toDate, String status, String isActive, long orgId);

	@Query("select count(es) from EmployeeSeparationDetails es "
			+ " JOIN FETCH Employee  emp on es.employee.id =  emp.id "
			+ " JOIN FETCH Candidate can on emp.candidate.id = can.id  "
			+ " JOIN FETCH CandidateProfessionalDetail cpd on cpd.candidate.id = can.id  "
			+ " JOIN FETCH LoginEntity le on le.id = can.loginEntity.id "
			+ " JOIN FETCH Organization org on org.id = le.organization.id "
			+ " where ((cpd.department.id = ?2 or cpd.designation.id =?4 or cpd.branch.id = ?5 or cpd.division.id = ?3  "
			+ "   ) or emp.id =?1 or es.status = ?6 )and es.isActive = ?7 AND org.id = ?8")
	public int findSeperatedEmployeeWithFiltersForCount(Long employeeCode, Long departmentId, Long divisionId,
			Long designationId, Long branchId, String status, String isActive, long orgId);

	@Query("select count(empSepDet) from EmployeeSeparationDetails empSepDet "
			+ " JOIN empSepDet.employee emp JOIN emp.candidate cand JOIN cand.loginEntity le "
			+ " JOIN le.organization org where (empSepDet.status = ?1 "
			+ " or empSepDet.status = ?2 or empSepDet.status = ?3 or empSepDet.status = ?4) "
			+ " and empSepDet.isActive = ?5 AND org.id = ?6")
	public int findAllByStatusForCount(String absconded, String terminated, String pending, String demise,
			String isActive, long orgId);

	@Query(value = "select  le.organization_id as oid ,pd.division_id  as did, "
			+ " count(e.id) as eid from tbl_employee e " + " join tbl_candidate c on e.candidate_id = c.id "
			+ " join tbl_candidate_professional_detail pd on pd.candidate_id = c.id "
			+ " join tbl_login_entity le on c.login_entity_id = le.id "
			+ "  join tbl_trn_employee_separation_details sepadet  on sepadet.employee_id=e.id"
			+ " where (DATE_PART('day', CURRENT_DATE) - DATE_PART('day', sepadet.resignation_date)>5) AND "
			+ " sepadet.ro_approver_status is null AND e.is_active = 'Y' "
			+ " group by pd.division_id ,le.organization_id ", nativeQuery = true)
	public List<Object[]> findRemainderforSeparationIfROnotTakenAnyAction();

	@Query("select es from EmployeeSeparationDetails es " + " JOIN FETCH Employee  emp on es.employee.id =  emp.id "
			+ " JOIN FETCH Candidate can on emp.candidate.id = can.id  "
			+ " JOIN FETCH CandidateProfessionalDetail cpd on cpd.candidate.id = can.id  "
			+ "  JOIN FETCH MasterDivision div on div.id=cpd.division "
			+ " JOIN FETCH LoginEntity le on le.id = can.loginEntity.id "
			+ " JOIN FETCH Organization org on org.id = le.organization.id "
			+ " where (DATE_PART('day', CURRENT_DATE) - DATE_PART('day', es.resignationDate)>5) AND "
			+ " es.roApproverStatus is null AND es.isActive =?3 AND div.id=?1 AND org.id = ?2 ")
	public List<EmployeeSeparationDetails> getDetailsRemainderforSeparationIfROnotTakenAnyAction(long divisionId,
			long orgId, String isActive);

	@Query("select es from EmployeeSeparationDetails es JOIN FETCH es.employee emp "
			+ " JOIN FETCH emp.candidate can JOIN FETCH can.candidateProfessionalDetail cpd "
			+ " JOIN FETCH cpd.division div JOIN FETCH can.loginEntity le JOIN FETCH le.organization org "
			+ " JOIN FETCH emp.employeeReportingManager erm "
			+ " where (DATE_PART('day', CURRENT_DATE) - DATE_PART('day', es.resignationDate)>?4) AND "
			+ " es.roApproverStatus is null AND es.isActive =?3 AND div.id=?1 AND org.id = ?2 ")
	public List<EmployeeSeparationDetails> getDetailsRemainderforSeparationIfROnotTakenAnyActionN(long divisionId,
			long orgId, String isActive, double sepReminderNoOfDays);

	@Query("SELECT separation from EmployeeSeparationDetails separation where separation.employee.id = ?1 AND separation.isActive = ?2")
	public EmployeeSeparationDetails findEmployeeIfResigned(long employeeId, String isActive);

	@Query("select empSepDet from EmployeeSeparationDetails empSepDet "
			+ " JOIN empSepDet.employee emp JOIN emp.candidate cand JOIN cand.loginEntity le "
			+ " JOIN cand.candidateProfessionalDetail cpd " + "  JOIN cpd.department dept "
			+ " JOIN le.organization org "
			+ " where empSepDet.isActive = ?1 AND org.id = ?2 order by empSepDet.resignationDate desc")
	public List<EmployeeSeparationDetails> getResignedEmpList(String isActive, long orgId/* , Pageable pageable */);

	@Query("select count(empSepDet) from EmployeeSeparationDetails empSepDet "
			+ " JOIN empSepDet.employee emp JOIN emp.candidate cand JOIN cand.loginEntity le "
			+ " JOIN le.organization org where empSepDet.isActive = ?1 AND org.id = ?2")
	public int getResignedEmpListForCount(String isActive, long orgId);

	@Query("select empSepDet from EmployeeSeparationDetails empSepDet "
			+ " JOIN empSepDet.employee emp JOIN emp.candidate cand JOIN cand.loginEntity le "
			+ " JOIN cand.candidateProfessionalDetail cpd " + " JOIN cpd.department dept "
			+ " JOIN le.organization org "
			+ " where empSepDet.isActive = ?1 AND org.id = ?2 order by empSepDet.resignationDate desc")
	public List<EmployeeSeparationDetails> getResignedEmpListforExcelDownload(String isActive, long orgId);

	@Query(" SELECT count(ela) FROM EmployeeSeparationDetails ela" + " JOIN  ela.employee emp JOIN  emp.candidate cand "
			+ " JOIN emp.employeeReportingManager erm JOIN cand.loginEntity le JOIN le.organization org "
			+ " WHERE ( cand.isActive = ?1 AND emp.isActive = ?2)"
			+ " AND (ela.status=?3 OR ela.status=?4 ) AND org.id=?5")
	public int findAllResignationAppliedEmployeeCount(String candIsActive, String empIsActive,
			String separationStatusSeq1, String separationStatusSeq2, long orgId);

	@Query(" SELECT Count(ela) FROM EmployeeSeparationDetails ela JOIN  ela.employee emp JOIN  emp.candidate cand "
			+ " JOIN emp.employeeReportingManager erm JOIN cand.loginEntity le JOIN le.organization org "
			+ " WHERE cand.isActive = ?1 AND emp.isActive = ?2 AND erm.reporingManager.id = ?3 "
			+ " AND (ela.status=?4 OR ela.status=?5 ) AND org.id = ?6")
	public int findResignedEmployee(String candIsActive, String empIsActive, long managerId,
			String separationStatusSeq1, String separationStatusSeq2, long orgId);

	@Query("SELECT count(separation) from EmployeeSeparationDetails separation where separation.employee.id = ?1 AND  ?2 > separation.actualRelievingDate AND separation.isActive = ?3")
	public int countByEmployeeActualRelievingDateIsActive(Long empId, Date date, String isActive);

	@Query(value = "SELECT count(*) from tbl_trn_employee_separation_details separation where separation.employee_id = ?1 AND"
			+ "  ?2 > separation.actual_relieving_date+1  AND separation.is_active =?3", nativeQuery = true)
	public int countByEmployeeRelievingDateMissingData(Long empId, Date date, String isActive);

	/**
	 * @author monika
	 * @param empId
	 * @return
	 */
	@Query(value = "select esd.employee_id ,max(esd.resignation_date) as resignation_date ,max(esd.actual_relieving_date) as \r\n"
			+ "actual_relieving_date from tbl_trn_employee_separation_details esd where esd.employee_id=?1  group by esd.employee_id\r\n"
			+ "", nativeQuery = true)
	public Object[][] findByEmployeeDetails(Long empId);

	@Query(" SELECT ela FROM EmployeeSeparationDetails ela JOIN FETCH ela.employee emp JOIN FETCH emp.candidate cand "
			+ " JOIN emp.employeeReportingManager erm JOIN cand.loginEntity le JOIN le.organization org "
			+ " WHERE ( cand.isActive = ?1 AND emp.isActive = ?2)"
			+ " AND ( ela.status=?3 OR ela.status=?4 ) AND org.id=?5")
	public List<EmployeeSeparationDetails> findAllResignationAppliedEmployee(String candIsActive, String empIsActive,
			String separationStatusSeq1, String separationStatusSeq2, long orgId, Pageable Pageable);

	@Query(" SELECT ela FROM EmployeeSeparationDetails ela JOIN FETCH ela.employee emp JOIN FETCH emp.candidate cand "
			+ " JOIN emp.employeeReportingManager erm JOIN cand.loginEntity le JOIN le.organization org "
			+ " WHERE cand.isActive = ?1 AND emp.isActive = ?2 AND erm.reporingManager.id = ?3 "
			+ " AND (ela.status=?4 OR ela.status=?5 ) AND org.id = ?6")
	public List<EmployeeSeparationDetails> findSubordinateResignationApplied(String candIsActive, String empIsActive,
			long managerId, String separationStatusSeq1, String separationStatusSeq2, long orgId, Pageable Pageable);

	@Query(value = "select 	* from  tbl_trn_employee_separation_details esd " + " inner join tbl_employee emp on\r\n"
			+ "	esd.employee_id = emp.id inner join tbl_candidate cand on\r\n"
			+ "	emp.candidate_id = cand.id inner join tbl_employee_reporting_manager rm on "
			+ "	emp.id = rm.employee_id inner join tbl_login_entity le on\r\n"
			+ "	cand.login_entity_id = le.id inner join tbl_organization org on\r\n"
			+ "	le.org_id = org.id where 	esd.status =?3  "
			+ "	and cand.is_active =?1  	and emp.is_active =?1  	and rm.reporting_manager_id =?2  "
			+ "	and org.id =?4 ", nativeQuery = true)
	public List<EmployeeSeparationDetails> findByResignedEmployee(String isActive, long loggedInEmpId,
			String employeeSeparationStatusPending, long orgId, Pageable pageable);

	@Query(value = "select 	count(*) from  tbl_trn_employee_separation_details esd "
			+ " inner join tbl_employee emp on\r\n" + "	esd.employee_id = emp.id\r\n"
			+ "inner join tbl_candidate cand on\r\n" + "	emp.candidate_id = cand.id\r\n"
			+ "inner join tbl_employee_reporting_manager rm on\r\n" + "	emp.id = rm.employee_id\r\n"
			+ "inner join tbl_login_entity le on\r\n" + "	cand.login_entity_id = le.id\r\n"
			+ "inner join tbl_organization org on\r\n" + "	le.org_id = org.id\r\n" + "where "
			+ "	esd.status =?3 \r\n" + "	and cand.is_active =?1 \r\n" + "	and emp.is_active =?1 \r\n"
			+ "	and rm.reporting_manager_id =?2 \r\n" + "	and org.id =?4 ", nativeQuery = true)
	public int countByResignedEmployee(String isActive, long loggedInEmpId, String employeeSeparationStatusPending,
			long orgId);

	@Query(value = "select * from tbl_trn_employee_separation_details where employee_id =?1 and is_active =?2 ", nativeQuery = true)
	public List<EmployeeSeparationDetails> findByEmpIdAndIsActive(Long loggedInEmpId, String isActive);
	
	@Query("select empSeparationDetails from EmployeeSeparationDetails empSeparationDetails  where empSeparationDetails.isActive=?1 and empSeparationDetails.id=?2")
	public EmployeeSeparationDetails findSeparationDetailsUsingIdandActive(String status, long id);
	
	//@Query(value = "select * from tbl_trn_employee_separation_details where employee_id =?1 order by ", nativeQuery = true)
	//public List<EmployeeSeparationDetails> findByEmpId(Long loggedInEmpId);
	@Query(value = "SELECT * FROM tbl_trn_employee_separation_details WHERE employee_id = ?1 ORDER BY CASE WHEN status = 'PENDING' THEN 0 ELSE 1 END, status DESC", nativeQuery = true)
	public List<EmployeeSeparationDetails> findByEmpId(Long loggedInEmpId);

	
	
	@Query(value = "select 	* from  tbl_trn_employee_separation_details esd " + " inner join tbl_employee emp on\r\n"
			+ "	esd.employee_id = emp.id inner join tbl_candidate cand on\r\n"
			+ "	emp.candidate_id = cand.id inner join tbl_employee_reporting_manager rm on "
			+ "	emp.id = rm.employee_id inner join tbl_login_entity le on\r\n"
			+ "	cand.login_entity_id = le.id inner join tbl_organization org on\r\n"
			+ "	le.organization_id = org.id where 	esd.status =?2  "
			+ "	and cand.is_active =?1  	and emp.is_active =?1   "
			+ "	and org.id =?3 ", nativeQuery = true)
	public List<EmployeeSeparationDetails> findAllResignedEmployee(String isActive,
			String employeeSeparationStatusPending, long orgId, Pageable pageable);

	@Query(value = "select 	count(*) from  tbl_trn_employee_separation_details esd " + " inner join tbl_employee emp on\r\n"
			+ "	esd.employee_id = emp.id inner join tbl_candidate cand on\r\n"
			+ "	emp.candidate_id = cand.id inner join tbl_employee_reporting_manager rm on "
			+ "	emp.id = rm.employee_id inner join tbl_login_entity le on\r\n"
			+ "	cand.login_entity_id = le.id inner join tbl_organization org on\r\n"
			+ "	le.organization_id = org.id where 	esd.status =?2  "
			+ "	and cand.is_active =?1  	and emp.is_active =?1   "
			+ "	and org.id =?3 ", nativeQuery = true)
	public int countAllResignedEmployee(String isActive,  String employeeSeparationStatusPending,
			long orgId);
	
	
	@Query(" select empSeparationDetails from EmployeeSeparationDetails empSeparationDetails  "
			+ " where  empSeparationDetails.employee.id=?1 and empSeparationDetails.id=?2 ")
	public List<EmployeeSeparationDetails> findByemployeeIdAndId(long empId,Long seprationId);

	
	@Query("select empSeparationDetails from EmployeeSeparationDetails empSeparationDetails  where empSeparationDetails.isActive=?1 and empSeparationDetails.employee.id=?2")
	public EmployeeSeparationDetails findSeparationDetailsUsingIdandIsActive(String status, long empId);
	
	
//	@Query(value = "select 	* from  tbl_trn_employee_separation_details esd " + " inner join tbl_employee emp on\r\n"
//			+ "	esd.employee_id = emp.id inner join tbl_candidate cand on\r\n"
//			+ "	emp.candidate_id = cand.id inner join tbl_candidate_professional_detail candp on "
//			+ "	cand.id = candp.candidate_id inner join tbl_login_entity le on\r\n"
//			+ "	cand.login_entity_id = le.id inner join tbl_organization org on\r\n"
//			+ "	le.organization_id = org.id where 	esd.status =?2  "
//			+ "and esd.org_approver_status=?3	and cand.is_active =?1  and candp.division_id=?5 and emp.is_active =?1  "
//			+ "	and org.id =?4 ", nativeQuery = true)
//	public List<EmployeeSeparationDetails> findByResignedEmployeeForApprover(String isActive,
//			String employeeSeparationStatusPending,String employeeSeparationStatusApproved, long orgId,long divId, Pageable pageable);
	
	@Query(value = "SELECT * FROM tbl_trn_employee_separation_details esd " +
	        "INNER JOIN tbl_employee emp ON esd.employee_id = emp.id " +
	        "INNER JOIN tbl_candidate cand ON emp.candidate_id = cand.id " +
	        "INNER JOIN tbl_candidate_professional_detail candp ON cand.id = candp.candidate_id " +
	        "INNER JOIN tbl_login_entity le ON cand.login_entity_id = le.id " +
	        "INNER JOIN tbl_organization org ON le.org_id = org.id " +
	        "INNER JOIN tbl_map_employee_catalogue cat ON cat.resigned_employee_id = esd.employee_id " +
	        "WHERE esd.status = ?2 AND esd.org_approver_status in (?3) AND cand.is_active = ?1 " +
	        "AND candp.division_id in(?5) AND emp.is_active = ?1 AND org.id = ?4 AND cat.status=?2 and cat.catalogue_id in(?6)", nativeQuery = true)
	public List<EmployeeSeparationDetails> findByResignedEmployeeForApprover(String isActive,
	        String employeeSeparationStatusPending,Object[] status, long orgId, List<Long>divId,List<Long>catId,
	        Pageable pageable);
	
	/** *
	 * @author vidya.chandane
	 * For all type of status
	 */
	@Query(value = "select 	* from  tbl_trn_employee_separation_details esd " + " inner join tbl_employee emp on\r\n"
			+ "	esd.employee_id = emp.id inner join tbl_candidate cand on\r\n"
			+ "	emp.candidate_id = cand.id inner join tbl_employee_reporting_manager rm on "
			+ "	emp.id = rm.employee_id inner join tbl_login_entity le on\r\n"
			+ "	cand.login_entity_id = le.id inner join tbl_organization org on\r\n"
			+ "	le.org_id = org.id where 	esd.status in ?3  "
			+ "	and cand.is_active =?1  	and emp.is_active =?1  	and rm.reporting_manager_id =?2  "
			+ "	and org.id =?4 ", nativeQuery = true)
	public List<EmployeeSeparationDetails> findByResignedEmployees(String isActive, long loggedInEmpId,
			Object[] employeeSeparationStatusPending, long orgId, Pageable pageable);	
	
	@Query(value = "select 	count(*) from  tbl_trn_employee_separation_details esd "
			+ " inner join tbl_employee emp on\r\n" + "	esd.employee_id = emp.id\r\n"
			+ "inner join tbl_candidate cand on\r\n" + "	emp.candidate_id = cand.id\r\n"
			+ "inner join tbl_employee_reporting_manager rm on\r\n" + "	emp.id = rm.employee_id\r\n"
			+ "inner join tbl_login_entity le on\r\n" + "	cand.login_entity_id = le.id\r\n"
			+ "inner join tbl_organization org on\r\n" + "	le.org_id = org.id\r\n" + "where "
			+ "	esd.status in ?3 \r\n" + "	and cand.is_active =?1 \r\n" + "	and emp.is_active =?1 \r\n"
			+ "	and rm.reporting_manager_id =?2 \r\n" + "	and org.id =?4 ", nativeQuery = true)
	public int countByResignedEmployees(String isActive, long loggedInEmpId, Object[] employeeSeparationStatusPending,
			long orgId);
	/**
	 * 
	 * @author vidya.chandane
	 * hand over checklist of resinged employees  
	 */
	
	@Query(value = "SELECT * FROM tbl_trn_employee_separation_details esd " +
	        "INNER JOIN tbl_employee emp ON esd.employee_id = emp.id " +
	        "INNER JOIN tbl_candidate cand ON emp.candidate_id = cand.id " +
	        "INNER JOIN tbl_candidate_professional_detail candp ON cand.id = candp.candidate_id " +
	        "INNER JOIN tbl_login_entity le ON cand.login_entity_id = le.id " +
	        "INNER JOIN tbl_organization org ON le.org_id = org.id " +
	        "INNER JOIN tbl_map_employee_catalogue cat ON cat.resigned_employee_id = esd.employee_id " +
	        "WHERE esd.status in (?7) AND esd.org_approver_status in (?3) AND cand.is_active = ?1 " +
	        "AND candp.division_id in(?5) AND emp.is_active = ?1 AND org.id = ?4 AND cat.status=?2 and cat.catalogue_id in(?6)", nativeQuery = true)
	public List<EmployeeSeparationDetails> findByResignedEmployeeForApproverss(String isActive,
	        String employeeSeparationStatusPending,Object[] status, long orgId, List<Long>divId,List<Long>catId,Object[] sepStatus,
	        Pageable pageable);
	
	@Query(value = "SELECT count(*) FROM tbl_trn_employee_separation_details esd "
			+ "INNER JOIN tbl_employee emp ON esd.employee_id = emp.id "
			+ "INNER JOIN tbl_candidate cand ON emp.candidate_id = cand.id "
			+ "INNER JOIN tbl_candidate_professional_detail candp ON cand.id = candp.candidate_id "
			+ "INNER JOIN tbl_login_entity le ON cand.login_entity_id = le.id "
			+ "INNER JOIN tbl_organization org ON le.org_id = org.id "
			+ "INNER JOIN tbl_map_employee_catalogue cat ON cat.resigned_employee_id = esd.employee_id "
			+ "WHERE esd.status in (?7) AND esd.org_approver_status in (?3) AND cand.is_active = ?1 "
			+ "AND candp.division_id in(?5) AND emp.is_active = ?1 AND org.id = ?4 AND cat.status=?2 and cat.catalogue_id in(?6)", nativeQuery = true)
	public int countByResignedEmployees(String isActive,
	        String employeeSeparationStatusPending,Object[] status, long orgId, List<Long>divId,List<Long>catId,Object[] sepStatus);
}
