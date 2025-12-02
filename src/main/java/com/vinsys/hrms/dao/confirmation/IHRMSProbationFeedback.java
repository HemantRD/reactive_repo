package com.vinsys.hrms.dao.confirmation;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.confirmation.ProbationFeedback;

public interface IHRMSProbationFeedback extends JpaRepository<ProbationFeedback, Long> {

	@Query("select feedback from ProbationFeedback feedback where feedback.isActive=?1 and feedback.employee.id =?2 order by feedback.createdDate desc")
	public List<ProbationFeedback> findByIsActiveAndEmployeeId(String active, long employeeId);

	// query added by Ritesh Kolhe//Should not use list

	/*
	 * @Query("select feedback from ProbationFeedback feedback where feedback.isActive=?1 and feedback.employee.id =?2 and feedback.status = 'Probation'"
	 * ) public List<ProbationFeedback>
	 * findByIsActiveAndEmployeeIdAndProbationStatus(String active,long employeeId);
	 */
	@Query("select feedback from ProbationFeedback feedback where feedback.isActive=?1 and feedback.employee.id =?2 and feedback.status = 'Probation'")
	public ProbationFeedback findByIsActiveAndEmployeeIdAndProbationStatus(String active, long employeeId);

	@Query("select feedback from ProbationFeedback feedback where feedback.id=?1 and feedback.isActive =?2")
	public ProbationFeedback findByIdAndIsActive(Long id, String active);

	/*
	 * @Query("select emp from Employee emp" + " join fetch emp.candidate cand " +
	 * " join fetch emp.employeeReportingManager rm " +
	 * " join fetch emp.probationFeedback pfb " +
	 * " where rm.reporingManager.id=?1 and emp.isActive =?2 and rm.isActive =?2 and pfb.isActive =?2 and pfb.status !=?3 and pfb.hrSubmitted = ?4"
	 * ) public List<Employee> findPendingSubordinateConfirmation(long
	 * rmEmpid,String isActive, String probationstatus, Boolean hrSubmitted);
	 */

	@Query("select emp from Employee emp" + " join fetch emp.candidate cand "
			+ " join fetch emp.employeeReportingManager rm " + " join fetch emp.probationFeedback pfb "
			+ " where rm.reporingManager.id=?1 and emp.isActive =?2 and rm.isActive =?2 and pfb.isActive =?2 and pfb.hrSubmitted = ?3")
	public List<Employee> findPendingSubordinateConfirmation(long rmEmpid, String isActive, Boolean hrSubmitted);

	@Query("select emp from Employee emp " + " join fetch emp.candidate cand "
			+ " join fetch emp.probationFeedback pfb "
			+ " where cand.candidateEmploymentStatus = ?1 and emp.isActive=?2 and pfb.isActive =?2 and pfb.hrSubmitted = ?3 and pfb.roSubmitted = ?4")
	public List<Employee> findPendingConfirmationForHR(String probationstatus, String isActive, Boolean hrSubmitted,
			Boolean roSubmitted); /// add 4th paramater to get value of roSubmitted

	@Query("select emp from Employee emp " + " join fetch emp.candidate cand "
			+ " where emp.id = ?1 and emp.isActive =?2")
	public Employee findPendingConfirmationForEmployee(long empId, String isActive);

	// Added By Monika
	public Boolean existsByEmployeeId(Long id);

	/*
	 * Added by akanksha to reopen probation from to employee
	 * 
	 * @Modifying
	 * 
	 * @Query("update probationFeedback pfb set is_active='N' where pfb.id = ?1 ")
	 * 
	 * @Transactional void deleteProbationForm(long employeeId);
	 */

	@Query("select feedback from ProbationFeedback feedback where feedback.isActive=?1 and feedback.employee.id =?2 order by feedback.createdDate desc")
	public ProbationFeedback findByIsActiveAndEmpId(String active, long employeeId);
	
	@Query("select feedback from ProbationFeedback feedback where feedback.isActive=?1 and feedback.employee.id =?2 and feedback.id=?3 order by feedback.createdDate desc")
	public ProbationFeedback findByIsActiveAndEmpIdAndId(String active, long employeeId,long id);

	@Query(value = "select	* from	tbl_probation_feedback pf join tbl_employee te on te.id = pf.employee_id join tbl_employee_reporting_manager term on "
			+ "	term.employee_id = te.id where term.reporting_manager_id =?2 and pf.is_active =?1 and te.is_active =?1 and pf.probation_status =?3 ", nativeQuery = true)
	public List<ProbationFeedback> findByIsActiveAndRmIdAndStatus(String active, Long rmId, String probationStatus,
			Pageable pageable);

	@Query(value = "select	count(*) from	tbl_probation_feedback pf join tbl_employee te on te.id = pf.employee_id join tbl_employee_reporting_manager term on "
			+ "	term.employee_id = te.id where term.reporting_manager_id =?2 and pf.is_active =?1 and te.is_active =?1 and pf.probation_status =?3 ", nativeQuery = true)
	public long countByIsActiveAndRmIdAndStatus(String active, Long rmId, String probationStatus);

	@Query(value="select * from tbl_probation_feedback feedback where feedback.employee_id =?1 order by feedback.created_date DESC", nativeQuery = true)
	public List<ProbationFeedback> findByEmpId(long employeeId);
	
	@Query(value="select * from tbl_probation_feedback tpf where employee_id =?1 and id =?2",nativeQuery = true)
	public ProbationFeedback findByEmpIdAndId(long employeeId,long id);
}
