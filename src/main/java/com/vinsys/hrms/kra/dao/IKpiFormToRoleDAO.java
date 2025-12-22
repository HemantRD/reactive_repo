package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vinsys.hrms.kra.entity.KpiFormToRole;
import com.vinsys.hrms.kra.entity.Kra;

public interface IKpiFormToRoleDAO extends JpaRepository<KpiFormToRole,Long> {

	
	@Query(value = "select * from tbl_map_kpiform_to_role", nativeQuery = true)
	List<KpiFormToRole> findAllData();

	KpiFormToRole findByRoleid(Long roleid);

}
