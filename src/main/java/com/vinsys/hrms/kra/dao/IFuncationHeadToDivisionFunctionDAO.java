package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.kra.entity.FuncationHeadToDivisionFunction;

public interface IFuncationHeadToDivisionFunctionDAO extends JpaRepository<FuncationHeadToDivisionFunction, Long> {


    @Query(value = "select division from tbl_map_functionheads_to_function tmhtd where employee =?1 and is_active =?2 ", nativeQuery = true)
    List<Long> getDivisionByEmployeeAndIsActive(Long empId, String isActive);

    List<FuncationHeadToDivisionFunction> findByDivisionAndIsActive(Long division, String isActive);

    List<FuncationHeadToDivisionFunction> findByIsActive(String name);

    @Query(value = "SELECT * FROM tbl_map_functionheads_to_function ORDER BY id ASC", countQuery = "SELECT COUNT(*) FROM tbl_map_functionheads_to_function", nativeQuery = true)
    Page<FuncationHeadToDivisionFunction> findAllSortedAsc(Pageable pageable);
}
