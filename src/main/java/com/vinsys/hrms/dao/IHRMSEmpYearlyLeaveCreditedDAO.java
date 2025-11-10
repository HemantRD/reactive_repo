package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.EmpYearlyLeaveCreditedStatus;

public interface IHRMSEmpYearlyLeaveCreditedDAO extends JpaRepository<EmpYearlyLeaveCreditedStatus, Long> {

	public EmpYearlyLeaveCreditedStatus findByEmployeeIdAndYear(long employeId, long year);

}
