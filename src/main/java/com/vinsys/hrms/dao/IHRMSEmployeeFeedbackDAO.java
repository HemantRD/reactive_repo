package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeFeedback;

public interface IHRMSEmployeeFeedbackDAO extends JpaRepository<EmployeeFeedback, Long> {

	public List<EmployeeFeedback> findByEmployee(Employee emp);
	
	@Query("select empFb from EmployeeFeedback empFb "
			+ " join fetch empFb.feedbackQuestion que"
			+ " left join fetch empFb.feedbackOption opt"
			+ " where empFb.employee.id = ?1 order by empFb.feedbackQuestion asc, empFb.feedbackOption asc")
	public List<EmployeeFeedback> findByEmployeeCustomQuery(long empId);
}
