package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.kra.entity.KraCycleType;

public interface IKraCycleTypeDAO extends JpaRepository<KraCycleType, Long> {
	
	@Query(value = "SELECT * FROM tbl_mst_kra_cycle_type WHERE org_id = ?1 AND is_active = ?2 ORDER BY id ASC", nativeQuery = true)
	List<KraCycleType> findAllKraCycleTypesByOrgIdAndIsActive(Long orgId, String isactive);
	
	KraCycleType findByCycleTypeName(String cycleTypeName);

}