package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.kra.entity.HrToHodMap;

public interface IHrToHodMapDao extends JpaRepository<HrToHodMap,Long> {

	
	@Query(value = "SELECT * FROM tbl_map_hr_to_hod where hod_employee_id=?1 and is_active=?2", nativeQuery = true)
	List<HrToHodMap> findByEmpIdAndIsActive(Long empId,String isActive);
	
	@Query(value = "SELECT * FROM tbl_map_hr_to_hod where hr_employee_id=?1 and is_active=?2", nativeQuery = true)
	List<HrToHodMap> findByHrIdAndIsActive(Long hrId,String isActive);
	
	
	
	

}
