package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vinsys.hrms.kra.entity.MapDepartmentToDivision;

public interface IMapDepartmentToDivisionDAO extends JpaRepository<MapDepartmentToDivision, Long> {

	 MapDepartmentToDivision findByIdAndIsActive(Long id,String active);

}
