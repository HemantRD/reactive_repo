package com.vinsys.hrms.dao;

import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeReportingManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IHRMSEmployeeReportingManager extends JpaRepository<EmployeeReportingManager, Long> {

	@Query("select manager from EmployeeReportingManager manager where manager.reporingManager.id = ?1")
	public List<EmployeeReportingManager> findByreporingManager(long managerId);

	@Query("select manager from EmployeeReportingManager manager   JOIN Fetch manager.employee emp_mngr "
			+ " JOIN Fetch emp_mngr.employeeSeparationDetails esd where manager.reporingManager.id = ?1 and esd.isActive=?2 and esd.status=?3 order by esd.actualRelievingDate desc")
	public List<EmployeeReportingManager> findSeparationEmployeeByreporingManager(long managerId, String isActive,
			String empseparationStatus);

	@Query(value = "select  mngr.reporting_manager_id,cand1.first_name as \"fname\", cand1.last_name as \"lname\", emp1.official_email_id as \"offemailid\", ecat.resigned_employee_id,cand2.first_name, cand2.last_name, emp2.official_email_id, "
			+ " div.id as \"division_id\"," + " lgn.organization_id as \"organization_id\" "
			+ " from tbl_employee_reporting_manager mngr\r\n"
			+ " join tbl_map_employee_catalogue ecat on mngr.employee_id = ecat.resigned_employee_id and ecat.status=?1\r\n"
			+ " join tbl_map_catalogue cat on ecat.catalogue_id = cat.id and cat.approver_id is null\r\n"
			+ " join tbl_employee emp1 on mngr.reporting_manager_id = emp1.id\r\n"
			+ " join tbl_candidate cand1 on emp1.candidate_id = cand1.id\r\n"
			+ " join tbl_employee emp2 on ecat.resigned_employee_id = emp2.id\r\n"
			+ " join tbl_candidate cand2 on emp2.candidate_id = cand2.id\r\n"
			+ " join tbl_candidate_professional_detail prof  on cand1.id = prof.candidate_id\r\n"
			+ " join tbl_mst_division div on prof.division_id = div.id\r\n"
			+ " join tbl_login_entity lgn on cand1.login_entity_id = lgn.id"
			+ " join tbl_trn_employee_separation_details empsep on ecat.resigned_employee_id=empsep.employee_id "
			+ " where empsep.status!=?2" + " order by mngr.reporting_manager_id", nativeQuery = true)
	public List<Object[]> getPendingChecklistEmployeeWithRo(String pendingStatus, String completedStatus);

	@Query(value = "select * from tbl_employee_reporting_manager where employee_id =?1 and is_active =?2 ", nativeQuery = true)
	public EmployeeReportingManager findByEmployeeAndIsActive(Long empId, String isActive);

	@Query(value = "SELECT reporting_manager_id FROM tbl_employee_reporting_manager WHERE employee_id = ?1", nativeQuery = true)
	public Long findReportingManagerIdByEmployeeId(Long resignedEmployeeId);

	@Query(value = "SELECT DISTINCT reporting_manager_id FROM tbl_employee_reporting_manager term where is_active =?1 ", nativeQuery = true)
	public List<Long> findReportingManagerByIsActive(String string);

	@Query(value = "select count(*) from tbl_employee_reporting_manager term where term.reporting_manager_id =?1 and term.org_id =?2", nativeQuery = true)
	public long countfindByreporingManager(long managerId, long orgId);

	public List<EmployeeReportingManager> findByIsActive(String name);

	@Query(value = "select distinct(reporting_manager_id) from tbl_employee_reporting_manager term where is_active =?1 and reporting_manager_id is not null ", nativeQuery = true)
	public List<Long> getAllReportingManagerIds(String name);

	public List<EmployeeReportingManager> findByReporingManagerAndIsActive(Employee loggedInEmployee, String name);

	public EmployeeReportingManager findByReporingManagerAndEmployeeAndIsActive(Employee loggedInEmployee,
			Employee empKpi, String name);

	boolean existsByReporingManager_IdAndEmployee_Id(Long managerId, Long employeeId);

	@Query(value = "WITH RECURSIVE mgr_chain AS ( " + " SELECT erm.reporting_manager_id "
			+ " FROM tbl_employee_reporting_manager erm " + " WHERE erm.employee_id = ?1 " + " UNION ALL "
			+ " SELECT erm.reporting_manager_id " + " FROM tbl_employee_reporting_manager erm "
			+ " INNER JOIN mgr_chain mc ON erm.employee_id = mc.reporting_manager_id " + ") "
			+ "SELECT reporting_manager_id " + "FROM mgr_chain "
			+ "WHERE reporting_manager_id IS NOT NULL", nativeQuery = true)
	List<Long> findHierarchyManagers(Long empId);

	@Query(value = "WITH RECURSIVE emp_chain AS ( " + "    SELECT erm.employee_id "
			+ "    FROM tbl_employee_reporting_manager erm " + "    WHERE erm.reporting_manager_id = ?1 "
			+ "    UNION ALL " + "    SELECT erm.employee_id " + "    FROM tbl_employee_reporting_manager erm "
			+ "    INNER JOIN emp_chain ec " + "        ON erm.reporting_manager_id = ec.employee_id " + ") "
			+ "SELECT employee_id " + "FROM emp_chain " + "WHERE employee_id IS NOT NULL", nativeQuery = true)
	List<Long> findDownwardHierarchy(Long managerId);

    @Query(value = "with recursive reportee_cte as \n" +
            "(\n" +
            "	select \n" +
            "		term.employee_id \n" +
            "	from \n" +
            "		tbl_employee_reporting_manager term \n" +
            "	where \n" +
            "		term.reporting_manager_id = ?1 AND term.is_active = 'Y' \n" +
            "	union all \n" +
            "	select \n" +
            "		child.employee_id \n" +
            "	from \n" +
            "		tbl_employee_reporting_manager child \n" +
            "	join reportee_cte parent \n" +
            "	        on \n" +
            "		child.reporting_manager_id  = parent.employee_id AND child.is_active = 'Y' \n" +
            ") \n" +
            "select \n" +
            "	employee_id \n" +
            "from \n" +
            "	reportee_cte;", nativeQuery = true)
    public List<Long> findReporteeIdsRecursivelyByDivisionHeadId(Long divisionHeadId);

}
