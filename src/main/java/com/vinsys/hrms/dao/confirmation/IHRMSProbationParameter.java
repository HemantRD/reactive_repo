package com.vinsys.hrms.dao.confirmation;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.confirmation.ProbationParameter;

public interface IHRMSProbationParameter extends JpaRepository<ProbationParameter, Long> {

	public Optional<ProbationParameter> findById(Long id);

	@Query(value = "select  sum(employee_rating)   from tbl_probation_parameter_feedback tppf where feedback_id =?1 ", nativeQuery = true)
	float getTotalEmpRating(Long feedbackId);

	@Query(value = "select  sum(manager_rating)  from tbl_probation_parameter_feedback tppf where feedback_id =?1 ", nativeQuery = true)
	float getTotalManagerRating(Long feedbackId);

}
