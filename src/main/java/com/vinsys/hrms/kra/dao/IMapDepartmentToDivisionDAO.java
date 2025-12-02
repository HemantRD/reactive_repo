package com.vinsys.hrms.kra.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.kra.entity.MapDepartmentToDivision;

public interface IMapDepartmentToDivisionDAO extends JpaRepository<MapDepartmentToDivision, Long> {

	MapDepartmentToDivision findByIdAndIsActive(Long id, String active);

	@Query(value = "SELECT * FROM tbl_map_department_to_division ORDER BY id ASC", countQuery = "SELECT COUNT(*) FROM tbl_map_department_to_division", nativeQuery = true)
	Page<MapDepartmentToDivision> findAllSortedAsc(Pageable pageable);
}
