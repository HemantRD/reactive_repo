package com.vinsys.hrms.dao;

import com.vinsys.hrms.entity.CandidateProfessionalDetail;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.MasterEmploymentType;
import com.vinsys.hrms.entity.attendance.EmployeeACN;
import com.vinsys.hrms.master.vo.HREmailDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IHRMSEmployeeDAO extends JpaRepository<Employee, Long> {

	@Query("select emp from Employee emp " + " join fetch emp.candidate cand" + " join fetch cand.loginEntity logEnt"
			+ " join fetch emp.employeeReportingManager where logEnt.organization.id = ?1 AND emp.isActive = ?2 ")
	public List<Employee> getAllEmployeeByOrgId(long orgId, String empIsActive, Pageable pageable);

	@Query("SELECT emp FROM Employee emp JOIN FETCH emp.candidate cand  JOIN FETCH cand.loginEntity logEnt "
			+ " JOIN FETCH emp.employeeReportingManager JOIN FETCH cand.candidateProfessionalDetail profDet "
			+ " WHERE logEnt.organization.id = ?1 and emp.isActive = 'Y' ")
	public List<Employee> getAllEmpNameIdByOrg(long orgId);

	@Query(" SELECT emp FROM Employee emp JOIN FETCH emp.candidate cand JOIN FETCH cand.loginEntity loginEntity "
			+ " JOIN FETCH loginEntity.organization org JOIN FETCH emp.employeeDesignation empDes JOIN FETCH emp.employeeDivision empDiv "
			+ " WHERE empDes.designation.id = ?1 OR empDiv.division.id = ?2 OR cand.gender = ?3 ")
	public List<Employee> getAllEmpNameIdByOrgWithFilter(long designationId, long divisionId, String gender);

	@Query("select emp from Employee emp join fetch emp.employeeLeaveDetails eld " + " join fetch emp.candidate cand"
			+ " where eld.employee.id = ?1 and eld.year = ?2 and emp.isActive = ?3")
	public Employee getEmployeeLeaveDetailByEmpIdAndYear(long empId, Integer year, String isActive);

	public Employee findByofficialEmailId(String officialEmailId);
	
	


	@Query("select employee from Employee employee " + " JOIN FETCH employee.candidate candidate "
			+ " JOIN FETCH candidate.candidateProfessionalDetail professionalDetail "
			+ " JOIN FETCH candidate.loginEntity loginEntity JOIN FETCH loginEntity.organization organization JOIN FETCH professionalDetail.department department "
			+ " where DATE_PART('day', candidate.dateOfBirth) = DATE_PART('day', CURRENT_DATE) AND "
			+ " DATE_PART('month', candidate.dateOfBirth) = DATE_PART('month', CURRENT_DATE) AND employee.isActive = 'Y'"
			+ "AND professionalDetail.division.id = ?1 AND organization.id = ?2 ")
	public List<Employee> findEmployeeWithTodayBirthday(long divisionId, long organizationId);

	@Query("select employee from Employee employee " + " JOIN FETCH employee.candidate candidate "
			+ " JOIN FETCH candidate.loginEntity loginEntity "
			+ " JOIN FETCH candidate.candidateProfessionalDetail professionalDetail "
			+ " JOIN FETCH professionalDetail.department dpt " + " JOIN FETCH professionalDetail.city masterCity"
			+ " JOIN FETCH professionalDetail.division " + " JOIN FETCH loginEntity.organization organization "
			+ " where DATE_PART('day', professionalDetail.dateOfJoining) = DATE_PART('day', CURRENT_DATE) AND "
			+ " DATE_PART('month', professionalDetail.dateOfJoining) = DATE_PART('month', CURRENT_DATE) AND employee.isActive = 'Y' "
			+ "AND professionalDetail.division.id = ?1 AND organization.id = ?2 ")
	public List<Employee> findServiceCompletionOfEmployee(long divisionId, long organizationId);

	@Query(value = "select  le.org_id as oid ,pd.division_id  as did, "
			+ " count(e.id) as eid from tbl_employee e " + " join tbl_candidate c on e.candidate_id = c.id "
			+ " join tbl_candidate_professional_detail pd on pd.candidate_id = c.id "
			+ " join tbl_login_entity le on c.login_entity_id = le.id "
			+ " where DATE_PART('day', pd.date_of_joining) = DATE_PART('day', CURRENT_DATE) AND "
			+ " DATE_PART('month', pd.date_of_joining) = DATE_PART('month', CURRENT_DATE) AND e.is_active = 'Y' "
			+ " group by pd.division_id ,le.org_id ", nativeQuery = true)
	public List<Object[]> findIfEmployeeHasServiceCompletionCurrentDay();

	@Query(value = "select  le.org_id as oid ,pd.division_id  as did, "
			+ " count(e.id) as eid from tbl_employee e " + " join tbl_candidate c on e.candidate_id = c.id "
			+ " join tbl_candidate_professional_detail pd on pd.candidate_id = c.id "
			+ " join tbl_login_entity le on c.login_entity_id = le.id "
			+ " where DATE_PART('day', c.date_of_birth) = DATE_PART('day', CURRENT_DATE) AND "
			+ " DATE_PART('month', c.date_of_birth) = DATE_PART('month', CURRENT_DATE) AND e.is_active = 'Y' "
			+ " group by pd.division_id ,le.org_id ", nativeQuery = true)
	public List<Object[]> findIfEmployeeHasBirthdayCurrentDay();

	@Query(" SELECT emp FROM Employee emp JOIN FETCH emp.candidate cand "
			+ " WHERE cand.id = ?1 AND cand.isActive = ?2 AND emp.isActive = ?3 ")
	public Employee getEmployeeDetailsByCandidateId(long candidateId, String candIsActive, String empIsActive);

	@Query("select candProf from CandidateProfessionalDetail candProf " + " join fetch candProf.candidate cand "
			+ " join fetch cand.employee emp " + " where emp.id = ?1")
	public CandidateProfessionalDetail findDepartmentByEmployeeId(long employeeId);

	@Query("SELECT emp from Employee emp WHERE emp.id = ?1 AND emp.isActive = ?2")
	public Employee findActiveEmployeeById(long id, String isActive);

	@Query("SELECT emp from Employee emp JOIN FETCH emp.candidate cand where emp.candidate.loginEntity.organization.id=?1"
			+ " AND emp.isActive=?2 and emp.id in (select ext.employee.id from EmployeeExtension ext where ext.isActive = ?2 and ext.organization.id = ?1 GROUP BY ext.employee.id)")
	public List<Employee> findEmployeeOrgWiseWithExtension(long orgId, String isActive);

	@Query("select emp from Employee emp" + " join fetch emp.candidate cand"
			+ " join fetch cand.candidateProfessionalDetail candProf" + " join fetch candProf.department"
			+ " join fetch candProf.branch" + " join fetch candProf.division" + " join fetch cand.loginEntity loginEnty"
			+ " join fetch loginEnty.organization" + " where loginEnty.organization.id = ?1 and emp.id = ?2")
	public Employee findByOrgId(long orgid, long empId);

	public Employee findByEmployeeACN(EmployeeACN empAcn);

	public Employee findByOfficialEmailId(String string);

	@Query("SELECT emp FROM Employee emp JOIN FETCH emp.candidate cand  JOIN FETCH cand.loginEntity logEnt "
			+ " JOIN FETCH emp.employeeReportingManager JOIN FETCH cand.candidateProfessionalDetail profDet "
			+ " LEFT JOIN FETCH emp.employeeACN"
			+ " WHERE logEnt.organization.id = ?1 and emp.isActive = 'Y' and profDet.branch.id = ?2 ")
	public List<Employee> findByOrgIdBrachId(long orgId, long branchId);

	@Query(" SELECT emp FROM Employee emp JOIN emp.candidate cand JOIN cand.candidateProfessionalDetail cpd "
			+ " WHERE cand.isActive = ?1 AND emp.isActive = ?2 AND cand.employmentType in ?3  "
			+ "AND (CURRENT_DATE - cpd.dateOfJoining) > 365 ")
	public List<Employee> findEmpListByEmployementStatusAndDOJMoreThanOneYear(String candIsActive, String empIsActive,
			List<MasterEmploymentType> empTypeIds);
	
	@Query(" SELECT emp FROM Employee emp JOIN emp.candidate cand JOIN cand.candidateProfessionalDetail cpd "
			+ " WHERE cand.isActive = ?1 AND emp.isActive = ?2 AND cand.employmentType in ?3  "
			+ "AND (CURRENT_DATE - cpd.dateOfJoining) <= 365 ")
	public List<Employee> findEmpListByEmployementStatusAndDOJLessThanOneYear(String candIsActive, String empIsActive,
			List<MasterEmploymentType> empTypeIds);
	
	@Query("SELECT emp from Employee emp "
			+ " join fetch emp.candidate cand "
			+ " join fetch cand.candidateProfessionalDetail prof "
			+ " join fetch prof.department "
			+ " join fetch prof.designation "
			+ " join fetch emp.employeeReportingManager ro "
			+ " join fetch ro.reporingManager "
			+ " WHERE emp.id = ?1 AND emp.isActive = ?2")
	public Employee findActiveEmployeeWithCandidateByEmpIdAndOrgId(long id, String isActive,Long orgId);

	public List<Employee> findByIsActive(String string);
	
	@Query("SELECT emp FROM Employee emp WHERE emp.isActive = ?1 AND emp.cycleAllowed = ?2")
	List<Employee> findByIsActiveAndIsAllowedInCycle(String isActive, String cycleAllowed);


	
	@Query(" SELECT emp FROM Employee emp JOIN emp.candidate cand JOIN cand.candidateProfessionalDetail cpd "
			+ " WHERE emp.isActive = ?1 AND emp.id = ?2  "
			+ "AND ?3 >= cpd.dateOfJoining")
	public List<Employee>  findByEmployeeDOJ(String empIsActive, long empId, java.sql.Date doj);

	@Query(value = "SELECT\r\n"
			+ "    c.first_name,\r\n"
			+ "    c.last_name,\r\n"
			+ "    c.date_of_birth,\r\n"
			+ "    CASE\r\n"
			+ "        WHEN EXTRACT(MONTH FROM c.date_of_birth) = EXTRACT(MONTH FROM CURRENT_DATE)\r\n"
			+ "        AND EXTRACT(DAY FROM c.date_of_birth) = EXTRACT(DAY FROM CURRENT_DATE) THEN 'Today'\r\n"
			+ "        WHEN EXTRACT(MONTH FROM c.date_of_birth) = EXTRACT(MONTH FROM CURRENT_DATE)\r\n"
			+ "        AND EXTRACT(DAY FROM c.date_of_birth) = EXTRACT(DAY FROM CURRENT_DATE) + 1 THEN 'Tomorrow'\r\n"
			+ "        ELSE TO_CHAR(c.date_of_birth, 'DD Mon')\r\n"
			+ "    END AS birth_date_status,\r\n"
			+ "    e.official_email_id,\r\n"
			+ "    cp.candidate_photo,\r\n"
			+ "    c.id AS candId,\r\n"
			+ "    cpro.branch_id,\r\n"
			+ "    cpro.division_id,\r\n"
			+ "    l.org_id,\r\n"
			+ "    e.id AS empId\r\n"
			+ "FROM\r\n"
			+ "    tbl_employee e\r\n"
			+ "left JOIN tbl_candidate c ON e.candidate_id = c.id\r\n"
			+ "left JOIN tbl_candidate_personal_detail cp ON cp.candidate_id = c.id\r\n"
			+ "left JOIN tbl_candidate_professional_detail cpro ON cpro.candidate_id = c.id\r\n"
			+ "left JOIN tbl_login_entity l ON l.id = c.login_entity_id\r\n"
			+ " left JOIN tbl_trn_employee_separation_details ttesd ON ttesd.employee_id = e.id\r\n"
			+ "WHERE\r\n"
			+ "    l.org_id = ?1 \r\n"
			+ "    AND e.is_active = 'Y'\r\n"
			+ "    AND (ttesd.is_active = 'Y' or ttesd.is_active is null)\r\n"
			+ "    AND (\r\n"
			+ "        (EXTRACT(MONTH FROM c.date_of_birth) = EXTRACT(MONTH FROM CURRENT_DATE)\r\n"
			+ "        AND EXTRACT(DAY FROM c.date_of_birth) >= EXTRACT(DAY FROM CURRENT_DATE))\r\n"
			+ "        OR EXTRACT(MONTH FROM c.date_of_birth) > EXTRACT(MONTH FROM CURRENT_DATE)\r\n"
			+ "    )\r\n"
			+ "    AND ttesd.actual_relieving_date IS NULL\r\n"
			+ "ORDER BY\r\n"
			+ "    EXTRACT(MONTH FROM c.date_of_birth),\r\n"
			+ "    EXTRACT(DAY FROM c.date_of_birth)\r\n"
			+ "LIMIT 10;", nativeQuery = true)
	public List<Object[]> upcomingBirthdays(long orgId);

	@Query(value = "select\r\n"
			+ "	c.first_name,\r\n"
			+ "	c.last_name,\r\n"
			+ "	cpro.date_of_joining,\r\n"
			+ "			 			case\r\n"
			+ "		when DATE_PART('month', cpro.date_of_joining) = DATE_PART('month', NOW())\r\n"
			+ "		and DATE_PART('day', cpro.date_of_joining) = DATE_PART('day', NOW())\r\n"
			+ "			 			then 'Today'\r\n"
			+ "		when DATE_PART('month', cpro.date_of_joining) = DATE_PART('month', NOW())\r\n"
			+ "		and DATE_PART('day', cpro.date_of_joining) = DATE_PART('day', NOW())+ 1\r\n"
			+ "			 			then 'Tomorrow'\r\n"
			+ "		else to_char(cpro.date_of_joining, 'DD Mon')\r\n"
			+ "	end,\r\n"
			+ "			 			e.official_email_id,\r\n"
			+ "	cper.candidate_photo,\r\n"
			+ "	c.id cand_Id,\r\n"
			+ "	cpro.branch_id,\r\n"
			+ "	cpro.division_id,\r\n"
			+ "	l.org_id,\r\n"
			+ "	e.id empId\r\n"
			+ "from\r\n"
			+ "	tbl_employee e\r\n"
			+ "join tbl_candidate c on\r\n"
			+ "	e.candidate_id = c.id\r\n"
			+ "join tbl_candidate_professional_detail cpro on\r\n"
			+ "	cpro.candidate_id = c.id\r\n"
			+ "join tbl_candidate_personal_detail cper on\r\n"
			+ "	cper.candidate_id = c.id\r\n"
			+ " join tbl_login_entity l on\r\n"
			+ "	l.id = c.login_entity_id\r\n"
			+ "left join tbl_trn_employee_separation_details ttesd on\r\n"
			+ "	ttesd.employee_id = e.id\r\n"
			+ "where\r\n"
			+ "	l.org_id = 1\r\n"
			+ "	and (ttesd.is_active = 'Y' or ttesd.is_active is null)\r\n"
			+ "	and e.is_active = 'Y'\r\n"
			+ "	and DATE_PART('year', cpro.date_of_joining) != DATE_PART('year', NOW())\r\n"
			+ "	and\r\n"
			+ "			 			case\r\n"
			+ "		when DATE_PART('month', cpro.date_of_joining) = DATE_PART('month', NOW()) \r\n"
			+ "			 			then DATE_PART('month', cpro.date_of_joining) = DATE_PART('month', NOW())\r\n"
			+ "		and DATE_PART('day', cpro.date_of_joining) >= DATE_PART('day', NOW())\r\n"
			+ "		else \r\n"
			+ "			 			DATE_PART('month', cpro.date_of_joining) >= DATE_PART('month', NOW())\r\n"
			+ "	end\r\n"
			+ "	and (ttesd.actual_relieving_date> now() or  ttesd.actual_relieving_date is null)\r\n"
			+ "order by\r\n"
			+ "	DATE_PART('month', cpro.date_of_joining),\r\n"
			+ "	DATE_PART('day', cpro.date_of_joining)\r\n"
			+ "limit 10;", nativeQuery = true)
	public List<Object[]> upcomingServiceCompletions(long orgId);
	
	
	@Query("SELECT emp from Employee emp JOIN FETCH emp.candidate cand "
			+" JOIN FETCH cand.candidateProfessionalDetail cpd"
			+ " where emp.candidate.loginEntity.orgId=?1"
			+ " AND emp.isActive=?2 and cpd.department.id !=13" 
		     +  "ORDER BY emp.id ASC")
	public List<Employee> findEmployeeOrgWiseInfo(long orgId, String isActive);

	@Query(" SELECT emp FROM Employee emp "
			+ " JOIN emp.candidate cand "
			+ " JOIN cand.candidateProfessionalDetail cpd "
			+ " JOIN cpd.division div"
			+ " WHERE cand.isActive = ?1 AND emp.isActive = ?2 AND cand.employmentType in ?3  "
			+ " AND div.id in ?4")
	public List<Employee> findEmpListByEmployementStatusAndDivision(String candIsActive, String empIsActive,
			List<MasterEmploymentType> empTypeIds, List<Long> divIds);
	
	@Query(" SELECT emp FROM Employee emp "
			+ " JOIN emp.candidate cand "
			+ " JOIN cand.candidateProfessionalDetail cpd "
			+ " JOIN cpd.division div"
			+ " WHERE cand.isActive = ?1 AND emp.isActive = ?2  "
			+ " AND div.id in ?3")
	public List<Employee> findActiveEmpForCreditEmergencyLeavesSch1edulerByDivision(String candIsActive, String empIsActive, List<Long> divIds);
	
	@Query(" SELECT emp FROM Employee emp "
			+ " JOIN emp.candidate cand "
			+ " JOIN cand.candidateProfessionalDetail cpd"
			+ " JOIN cand.employmentType empType "
			+ " WHERE emp.isActive = ?1 AND emp.id = ?2  ")
	public Employee findEmpCandDOJByEmpId(String empIsActive, long empId);
	
	@Query(" SELECT emp FROM Employee emp "
			+ " JOIN fetch emp.candidate cand "
			+ " WHERE emp.id = ?1  ")
	public Employee findEmpCandByEmpId(long empId);
	
	@Query(" SELECT emp FROM Employee emp "
			+ " JOIN emp.candidate cand "
			+ " JOIN cand.candidateProfessionalDetail cpd "
			+ " WHERE cand.isActive = ?1 AND emp.isActive = ?2 AND cand.employmentType in ?3  "
			+ " AND (CURRENT_DATE - cpd.dateOfJoining) > 365 AND division_id = ?4 ")
	public List<Employee> findEmpListByEmployementStatusDOJAndDivMoreThanOneYear(String candIsActive, String empIsActive,
			List<MasterEmploymentType> empTypeIds,long divId);
	
	@Query(" SELECT emp FROM Employee emp JOIN emp.candidate cand JOIN cand.candidateProfessionalDetail cpd "
			+ " WHERE cand.isActive = ?1 AND emp.isActive = ?2 AND cand.employmentType in ?3  "
			+ "AND (CURRENT_DATE - cpd.dateOfJoining) <= 365 AND division_id = ?4 ")
	public List<Employee> findEmpListByEmployementStatusDOJAndDivIdLessThanOneYear(String candIsActive, String empIsActive,
			List<MasterEmploymentType> empTypeIds, long divId);
	
	@Query(value = "SELECT emp.id,cand.first_name, cand.last_name,emp.official_email_id from tbl_employee emp "
			+ " JOIN  tbl_candidate cand on emp.candidate_id=cand.id"
			+ " JOIN  tbl_candidate_professional_detail candp on candp.candidate_id=cand.id"
			+ " JOIN  tbl_login_entity le on cand.login_entity_id=le.id"
			+ " JOIN  tbl_map_login_entity_type mlet on mlet.login_entity_id=le.id"
			+ " WHERE mlet.login_entity_type_id=5"
			+ " AND le.org_id=?1  AND emp.is_active=?2 ",nativeQuery = true)
	public List<Object[]> findHREmailId(long orgId, String isActive);

	@Query("SELECT emp FROM Employee emp "
	        + "JOIN FETCH emp.candidate cand "
	        + "JOIN FETCH cand.candidateProfessionalDetail cpd "
	        + "WHERE emp.isActive = ?1 "
	        + "AND cpd.division.id = ?2 order by emp.id asc")  
	List<Employee> findDivfourEmpList(String isactive, long divId);
	

	@Query(value="SELECT emp.* FROM tbl_employee emp "
			+ "JOIN tbl_candidate cand on cand.id = emp.candidate_id "
			+ "JOIN tbl_candidate_professional_detail tcpd on tcpd.candidate_id =cand.id "
			+ "join tbl_login_entity tle on tle.id = cand.login_entity_id "
			+ "WHERE tle.org_id = ?1 AND emp.is_active  = ?2  order by emp.id  asc",nativeQuery=true)
		public List<Employee> findEmployeeOrgWiseInfo(long orgId, String isActive, Pageable pageable);
		
	@Query(value="SELECT COUNT(emp) FROM tbl_employee emp "
			+ "JOIN tbl_candidate cand on cand.id = emp.candidate_id  "
	        + "JOIN tbl_candidate_professional_detail tcpd on tcpd.candidate_id =cand.id "
	        + "join tbl_login_entity tle on tle.id = cand.login_entity_id "
	        + "WHERE tle.org_id = ?1 AND emp.is_active  = ?2 ",nativeQuery=true)
	public long countEmployeeOrgWiseInfo(long orgId, String isActive);


	@Query("SELECT emp FROM Employee emp "
		     + "JOIN FETCH emp.candidate cand "
		     + "JOIN FETCH cand.candidateProfessionalDetail cpd "
		     + "WHERE emp.candidate.loginEntity.organization.id = ?1 "
		     + "AND emp.isActive = ?2 "
		     + "AND cpd.branch.id = ?3   "
		     + "ORDER BY emp.id ASC")
		public List<Employee> findEmployeeOrgWiseBranchWiseInfo(long orgId, String isActive, long branchId, Pageable pageable);


	@Query("SELECT COUNT(emp) FROM Employee emp "
	        + "JOIN emp.candidate cand "
	        + "JOIN cand.candidateProfessionalDetail cpd "
	        + "WHERE emp.candidate.loginEntity.organization.id = ?1 "
	        + "AND emp.isActive = ?2 "
	        + "AND cpd.branch.id = ?3 ")
	public long countEmployeeOrgWiseBranchWiseInfo(long orgId, String isActive, Long branchId);
	
	@Query("SELECT emp FROM Employee emp "
		     + "JOIN FETCH emp.candidate cand "
		     + "JOIN FETCH cand.candidateProfessionalDetail cpd "
		     + "WHERE emp.candidate.loginEntity.organization.id = ?1 "
		     + "AND emp.isActive = ?2 "
		     + "AND (cpd.branch.branchName LIKE CONCAT(?3, '%') OR cpd.branch.branchName = ?3) "
		     + "ORDER BY emp.id ASC")
		public List<Employee> findEmployeeOrgWiseBranchNameWiseInfo(long orgId, String isActive, String branch, Pageable pageable);

	@Query("SELECT COUNT(emp) FROM Employee emp "
	        + "JOIN emp.candidate cand "
	        + "JOIN cand.candidateProfessionalDetail cpd "
	        + "WHERE emp.candidate.loginEntity.organization.id = ?1 "
	        + "AND emp.isActive = ?2 "
	        + "AND (cpd.branch.branchName LIKE CONCAT(?3, '%') OR cpd.branch.branchName = ?3 ) ")
	public long countEmployeeOrgWiseBranchNameWiseInfo(long orgId, String isActive, String branch);
	
	@Query("SELECT emp "
			+ " from Employee emp "
			+ " JOIN FETCH emp.candidate cand  "
			+ " join FETCH cand.loginEntity login  "
			+ " JOIN FETCH login.loginEntityTypes type "
			+ " WHERE emp.isActive  = 'Y' and type.id = ?1 Order by emp.id ASC")
	public List<Employee> getEmployeeByLoginEntityType(long loginEntityTypes);
	
	
	@Query("SELECT emp FROM Employee emp JOIN emp.candidate cand "
	        + "JOIN cand.candidateProfessionalDetail cpd "
	        + "WHERE emp.candidate.loginEntity.organization.id = ?1 "
	        + "AND emp.isActive = ?2  AND cpd.department.id =?3")
		public List<Employee> findEmployeeDepartmentWiseInfo(long orgId, String isActive,long deptId);
	
	@Query("SELECT COUNT(emp) FROM Employee emp JOIN emp.candidate cand "
	        + "JOIN cand.candidateProfessionalDetail cpd "
	        + "WHERE emp.candidate.loginEntity.organization.id = ?1 "
	        + "AND emp.isActive = ?2  AND cpd.department.id =?3")
	public long countEmployeeDepartmentWiseInfo(long orgId, String isActive,long deptId);
	
	
	@Query(value = "SELECT emp.* " +
		            "FROM tbl_employee emp " +
		            "JOIN tbl_candidate cand ON cand.id = emp.candidate_id " +
		            "JOIN tbl_candidate_professional_detail tcpd ON tcpd.candidate_id = cand.id " +
		            "JOIN tbl_login_entity tle ON tle.id = cand.login_entity_id " +
		            "JOIN tbl_mst_department tmd ON tmd.id = tcpd.department_id " +
		            "JOIN tbl_mst_division tmd2 ON tmd2.id = tcpd.division_id " +
		            "JOIN tbl_mst_branch tmb ON tmb.id = tcpd.branch_id " +
		            "JOIN tbl_mst_designation tmd3 ON tmd3.id = tcpd.designation_id " +
		            "JOIN tbl_employee_designation ted ON ted.employee_id = emp.id " +
		            "JOIN tbl_employee_division empdev ON empdev.employee_id = emp.id " +
		            "JOIN tbl_employee_branch teb ON teb.employee_id = emp.id " +
		            "JOIN tbl_employee_designation empdeg ON empdeg.employee_id = emp.id " +
		            "WHERE tle.org_id = ?1 " +
		            "AND emp.is_active = ?2 " +
		            "AND ( " +
		            "   emp.official_email_id ILIKE CONCAT('%', ?3, '%') " +
		            "   OR tmd.department_name ILIKE CONCAT('%', ?3, '%') " +
		            "   OR cand.first_name ILIKE CONCAT('%', ?3, '%') " +
		            "   OR cand.last_name ILIKE CONCAT('%', ?3, '%') " +
		            "   OR tmd2.division_name ILIKE CONCAT('%', ?3, '%') " +
		            "   OR tmd3.designation_name ILIKE CONCAT('%', ?3, '%') " +
		            "   OR tmb.branch_name ILIKE CONCAT('%', ?3, '%') " +
		            "   OR emp.employee_code ILIKE CONCAT('%', ?3, '%')) " +
		            "   OR tcpd.grade ILIKE CONCAT('%', ?3, '%')"
		            + "order by	emp.id asc",nativeQuery=true)	
	public List<Employee> findEmployeeOrgWiseInfoByKeyword(long orgId, String isActive, String keyword, Pageable pageable);

	
	@Query(value="SELECT COUNT(DISTINCT emp.id)\r\n"
			+ "FROM \r\n"
			+ "    tbl_employee emp\r\n"
			+ "    JOIN tbl_candidate cand \r\n"
			+ "        ON cand.id = emp.candidate_id\r\n"
			+ "    JOIN tbl_candidate_professional_detail tcpd \r\n"
			+ "        ON tcpd.candidate_id = cand.id\r\n"
			+ "    JOIN tbl_login_entity tle \r\n"
			+ "        ON tle.id = cand.login_entity_id\r\n"
			+ "    JOIN tbl_mst_department tmd \r\n"
			+ "        ON tmd.id = tcpd.department_id\r\n"
			+ "    JOIN tbl_mst_division tmd2 \r\n"
			+ "        ON tmd2.id = tcpd.division_id\r\n"
			+ "    JOIN tbl_mst_branch tmb \r\n"
			+ "        ON tmb.id = tcpd.branch_id\r\n"
			+ "    JOIN tbl_mst_designation tmd3 \r\n"
			+ "        ON tmd3.id = tcpd.designation_id\r\n"
			+ "    JOIN tbl_employee_designation ted \r\n"
			+ "        ON ted.employee_id = emp.id\r\n"
			+ "    JOIN tbl_employee_division empdev \r\n"
			+ "        ON empdev.employee_id = emp.id\r\n"
			+ "    JOIN tbl_employee_branch teb \r\n"
			+ "        ON teb.employee_id = emp.id\r\n"
			+ "    JOIN tbl_employee_designation empdeg \r\n"
			+ "        ON empdeg.employee_id = emp.id\r\n"
			+ "WHERE \r\n"
			+ "    tle.org_id = ?1\r\n"
			+ "    AND emp.is_active = ?2\r\n"
			+ "    AND (\r\n"
			+ "        emp.official_email_id ILIKE CONCAT('%', ?3, '%')\r\n"
			+ "        OR tmd.department_name ILIKE CONCAT('%', ?3, '%')\r\n"
			+ "        OR cand.first_name ILIKE CONCAT('%', ?3, '%')\r\n"
			+ "        OR cand.last_name ILIKE CONCAT('%', ?3, '%')\r\n"
			+ "        OR tmd2.division_name ILIKE CONCAT('%', ?3, '%')\r\n"
			+ "        OR tmd3.designation_name ILIKE CONCAT('%', ?3, '%')\r\n"
			+ "        OR tmb.branch_name ILIKE CONCAT('%', ?3, '%')\r\n"
			+ "        OR emp.employee_code ILIKE CONCAT('%', ?3, '%')\r\n"
			+ "        OR tcpd.grade ILIKE CONCAT('%', ?3, '%'));",nativeQuery=true)
	    public long countEmployeeOrgWiseInfoByKeyword(long orgId, String isActive,String keyword);
	
	@Query(value="select * from tbl_employee te where id IN (:ids)",nativeQuery=true)
	List<Employee> findEmpListByEmpIds(List<Long> ids);
	
	@Query(value="select * from tbl_employee emp where emp.org_id =?2 and emp.id =?1",nativeQuery=true)
	public Employee findByEmpIdAndOrgId(Long empId, Long orgId);

	@Query("select emp from Employee emp join fetch emp.employeeLeaveDetails eld " + " join fetch emp.candidate cand"
			+ " join fetch cand.loginEntity logEnt"
			+ " where eld.employee.id = ?1 and eld.year = ?2 and emp.isActive = ?3 and logEnt.organization.id=?4")
	public Employee getEmployeeLeaveDetailByEmpIdAndYearAndOrgId(long empId, Integer year, String isActive,Long orgId);

	@Query(value="SELECT * from tbl_employee emp WHERE emp.id = ?1 AND emp.is_active = ?2 and emp.org_id=?3",nativeQuery=true)
	public Employee findActiveEmployeeByIdAndOrgId(long id, String isActive , Long orgId);
	
	@Query(value="select * from tbl_employee where candidate_id =?1 and is_active =?2 and org_id=?3 ",nativeQuery=true)
	public Employee findByCandidateIdAndIsActiveAndOrgId(Long candidateId,String isActive,Long orgId);
	
	public Employee findByofficialEmailIdAndIsActive(String officialEmailId,String isActive);
	
	@Query(value="select * from tbl_employee where official_mobile_number =?1 and is_active =?2",nativeQuery=true)
	public Employee findByofficialMobileNumberAndIsActive(Long officialMobileNumber,String isActive);
	//public Employee findByofficialMobileNumberAndIsActive(Long officialMobileNumber,String isActive);

	public Employee findByIsActiveAndId(String name, Long employeeId);

	

	@Query(value="select * from tbl_employee where official_email_id  like %?1% ",nativeQuery=true)
	public List<Employee> findByEmailLike(String email);
	
	@Query(value="select * from tbl_employee where employee_code =?1",nativeQuery=true)
	public List<Employee> findByEmpCodeLike(String empCode);
	

	@Query("SELECT new com.vinsys.hrms.master.vo.HREmailDTO(emp.id, cand.firstName, cand.lastName, emp.officialEmailId) " +
		       "FROM Employee emp " +
		       "JOIN emp.candidate cand " +
		       "JOIN emp.candidate.loginEntity le " +
		       "JOIN emp.candidate.loginEntity.loginEntityTypes mlet " +
		       "WHERE mlet.id = :loginEntityTypeId AND emp.isActive = :isActive")
		List<HREmailDTO> findAllHREmailId(@Param("loginEntityTypeId") Long loginEntityTypeId, @Param("isActive") String isActive);

	public List<Employee> findAllByOrgIdAndIsActive(Long orgId, String isActive);
	
	@Query(value="select * from tbl_employee where employee_code =?1",nativeQuery=true)
	public Employee findByEmpCode(String empCode);
	
	
	@Query("SELECT emp FROM Employee emp "
		     + "JOIN FETCH emp.candidate cand "
		     + "JOIN FETCH cand.candidateProfessionalDetail cpd "
		     + "WHERE emp.candidate.loginEntity.organization.id = ?1 "
		     + "AND emp.isActive = ?2 "
		     + "AND cpd.gradeId.id = ?3   "
		     + "ORDER BY emp.id ASC")
		public List<Employee> findEmployeeOrgWiseGradeWiseInfo(long orgId, String isActive, long gradeId, Pageable pageable);
	
	
	@Query("SELECT emp FROM Employee emp "
		     + "JOIN FETCH emp.candidate cand "
		     + "JOIN FETCH cand.candidateProfessionalDetail cpd "
		     + "WHERE emp.candidate.loginEntity.organization.id = ?1 "
		     + "AND emp.isActive = ?2 "
		     + "AND cpd.department.id = ?3   "
		     + "ORDER BY emp.id ASC")
		public List<Employee> findEmployeeOrgWiseDeptWiseInfo(long orgId, String isActive, long deptId, Pageable pageable);

	@Query("SELECT COUNT(emp) FROM Employee emp "
	        + "JOIN emp.candidate cand "
	        + "JOIN cand.candidateProfessionalDetail cpd "
	        + "WHERE emp.candidate.loginEntity.organization.id = ?1 "
	        + "AND emp.isActive = ?2 "
	        + "AND cpd.department.id = ?3 ")
	public long countEmployeeOrgWiseDeptWiseInfo(long orgId, String isActive, Long deptId);
	
	@Query("SELECT COUNT(emp) FROM Employee emp "
	        + "JOIN emp.candidate cand "
	        + "JOIN cand.candidateProfessionalDetail cpd "
	        + "WHERE emp.candidate.loginEntity.organization.id = ?1 "
	        + "AND emp.isActive = ?2 "
	        + "AND cpd.gradeId.id = ?3 ")
	public long countEmployeeOrgWiseGradeWiseInfo(long orgId, String isActive, Long gradeId);

	@Query(value="select * from tbl_employee te where id in (?1) and is_active =?2 and is_allowed_in_cycle =?3",nativeQuery=true)
	public List<Employee> findByIdInAndIsActiveAndIsAllowedInCycle(List<Long> employeeIds, String name, String name2);

	public List<Employee> findByCandidateIdInAndIsActive(List<Long> candidateIds, String isactive);

	}

	

