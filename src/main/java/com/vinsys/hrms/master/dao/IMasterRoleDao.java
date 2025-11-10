package com.vinsys.hrms.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterRole;

public interface IMasterRoleDao extends JpaRepository<MasterRole, Long> {

	@Query(value="select r.* from tbl_map_role_function fr\r\n"
			+ "join tbl_mst_role r on r.id =fr.role_id  \r\n"
			+ "join tbl_mst_functions f on f.id=fr.function_id \r\n"
			+ "where  lower(r.role_name) in(lower(?1)) \r\n"
			+ "and ?2 like '%' || f.function_url \r\n"
			+ "and fr.is_active =?3",nativeQuery = true)
	MasterRole findByRoleNameFunction(List<String> list, String uri, String isactive);
	

	@Query(value="select * from tbl_mst_role where role_name=?1 and org_id=?2 and is_active=?3",nativeQuery = true)
	MasterRole findByRoleNameOrgIdIsActive(String name,Long orgId, String isActive);
	
	MasterRole findByRoleNameAndIsActive(String roleName, String isActive);

	@Query(value="select * from tbl_mst_role where (role_name=?1 OR role_name=?2 OR role_name=?3)",nativeQuery = true)
	List<MasterRole> findByRoleName(String name, String name2, String name3);


	MasterRole findByRoleName(String name);
	
	List<MasterRole> findByIsActive(String isActive);

}
