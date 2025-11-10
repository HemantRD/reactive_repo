package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.kra.entity.KraCycleStatus;

public interface KraStatusDao extends JpaRepository<KraCycleStatus, Long>{

	@Query(value = "select * from tbl_mst_kra_cycle_status where status=?1", nativeQuery = true)
	public KraCycleStatus findByName(String status);

	public List<KraCycleStatus> findByIsActive(String name);

	public KraCycleStatus findByStatus(String status);
}
