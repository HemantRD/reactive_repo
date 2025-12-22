package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterMenu;

public interface IHRMSMenuDAO extends JpaRepository<MasterMenu, Long> {

//	@Query(value = "select DISTINCT menu.* from tbl_mst_menu menu where role in (?1) and is_active =?2 order by seq_no ", nativeQuery  =true)
//	List<MasterMenu> getMenuByRoleAndIsActive(Object[] roleIds, String isActive);

	@Query(value = "select DISTINCT menu.* from tbl_mst_menu menu where role =?1 and is_active =?2 order by seq_no ", nativeQuery = true)
	List<MasterMenu> getMenuByRoleAndIsActive(Long roleId, String isActive);

	@Query(value = "select DISTINCT menu.* from tbl_mst_menu menu where role in ?1 and is_active =?2 order by seq_no ", nativeQuery = true)
	List<MasterMenu> getMenuByRoleIdInAndIsActive(Object[] objects, String isActive);

	@Query(value = "select DISTINCT menu.* from tbl_mst_menu menu where role in ?1 and is_active =?2 and org_id=?3 order by seq_no ", nativeQuery = true)
	List<MasterMenu> getMenuByRoleIdInAndIsActiveAndOrgId(Object[] objects, String isActive, Long orgId);

}
