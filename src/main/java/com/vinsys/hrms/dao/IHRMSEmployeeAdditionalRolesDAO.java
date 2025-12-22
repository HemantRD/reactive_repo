package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.EmployeeAdditionalRole;

public interface IHRMSEmployeeAdditionalRolesDAO extends JpaRepository<EmployeeAdditionalRole, Long> {

}
