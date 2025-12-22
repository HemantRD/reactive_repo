package com.vinsys.hrms.security.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.security.entity.RoleFunctionMapping;

public interface IRoleFunctionMappingDAO extends JpaRepository<RoleFunctionMapping, Long> {

	@Query(value = "select tmrf.* from tbl_map_role_function tmrf join tbl_mst_role tmr on tmr.id = tmrf.role_id join tbl_mst_functions tmf on tmf.id = tmrf.function_id  where tmr.role_name in (?2) and tmf.function_url = ?1 and tmrf.is_active =?3 and tmr.is_active =?3 ", nativeQuery = true)
	List<RoleFunctionMapping> isAuthorizedFunction(String function, List<String> role,String isAcive);
	
	@Query(value = "select tmrf.* from tbl_map_role_function tmrf join tbl_mst_role tmr on tmr.id = tmrf.role_id join tbl_mst_functions tmf on tmf.id = tmrf.function_id  where tmr.role_name in (?2) and tmf.function_name = ?1 and tmrf.is_active =?3 and tmr.is_active =?3 ", nativeQuery = true)
	List<RoleFunctionMapping> isAuthorizedFunctionName(String functionName, List<String> role,String isAcive);
}
