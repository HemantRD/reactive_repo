package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.kra.entity.FuncationHeadToDivisionFunction;

public interface IFuncationHeadToDivisionFunctionDAO extends JpaRepository<FuncationHeadToDivisionFunction, Long> {

	
	@Query(value = "select division from tbl_map_functionheads_to_function tmhtd where employee =?1 and is_active =?2 ", nativeQuery = true)
	List<Long> getDivisionByEmployeeAndIsActive(Long empId, String isActive);
}
