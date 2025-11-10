package com.vinsys.hrms.kra.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.kra.entity.EmployeeReport;

public interface IEmployeeReportDAO extends JpaRepository<EmployeeReport, String> {

}
