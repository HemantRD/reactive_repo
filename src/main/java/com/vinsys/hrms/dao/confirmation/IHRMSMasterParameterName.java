package com.vinsys.hrms.dao.confirmation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.confirmation.MasterEvaluationParameter;

public interface IHRMSMasterParameterName extends JpaRepository<MasterEvaluationParameter, Long> {
	
	@Query("Select parameter from MasterEvaluationParameter parameter where parameter.isActive= ?1 and parameter.organization.id = ?2")
	public List<MasterEvaluationParameter> findAllParameterNames(String active,Long orgId);

	@Query("Select param from MasterEvaluationParameter param where param.id = ?1 and param.isActive = ?2 ")
	public  MasterEvaluationParameter findByIdAndIsActive(Long id,String active);
}
