package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.EmployeeSeparationDetailsWithHistory;

public interface IHRMSEmployeeSeparationHistoryDAO extends JpaRepository<EmployeeSeparationDetailsWithHistory, Long> {

}
