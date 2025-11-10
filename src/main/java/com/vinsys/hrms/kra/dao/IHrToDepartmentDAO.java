package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.kra.entity.HrToDepartment;

public interface IHrToDepartmentDAO extends JpaRepository<HrToDepartment, Long> {

	List<HrToDepartment> findByIsActive(String isActive);

}
