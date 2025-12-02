package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vinsys.hrms.kra.entity.Kra;
import com.vinsys.hrms.kra.entity.KraCycle;
import com.vinsys.hrms.kra.entity.KraDetails;
import com.vinsys.hrms.kra.entity.KraWf;

public interface IKraWfDao extends JpaRepository<KraWf, Long> {

	public KraWf findByIdAndIsActive(String id, String isActive);

	public KraWf findByKraAndIsActive(Kra kra, String isActive);
	
	public List<KraWf> findByStatusAndPendingWithAndIsActive(String status,String pending,String isActive);


	public KraWf findByKraAndCycleIdAndIsActive(Kra kra, KraCycle cycle, String name);

	public List<KraWf> findByIsActiveAndCycleId(String isactive, KraCycle cycle);
	
	KraWf findByKraAndCycleId(Kra kra, KraCycle Cycle);

	public boolean existsByKraAndCycleId(Kra trnKra, KraCycle cycle);

	@Query(value = "SELECT * FROM tbl_trn_kra_wf WHERE mst_kra_cycle_id = :cycleId", nativeQuery = true)
	List<KraWf> findByCycleId(@Param("cycleId") Long cycleId);

	@Modifying
	@Query(value="DELETE FROM tbl_trn_kra_wf WHERE mst_kra_cycle_id = :cycleId", nativeQuery = true)
	void deleteByCycleId(@Param("cycleId") Long cycleId);
	
	@Query(value = "SELECT * FROM tbl_trn_kra_wf WHERE kra_id = :kraId", nativeQuery = true)
	public KraWf findByKraId(Long kraId);

	public KraWf findByKraAndPendingWithAndIsActive(Kra kra,String pending,String isActive);
	
	
	@Query(value = "SELECT * FROM tbl_trn_kra_wf WHERE pending_with=?1 and status!=?2 and is_active=?3", nativeQuery = true)
	public KraWf findByPendingWithStatusAndIsActive(String pendingWith, String status,String isActive);
	
	@Query(value = "SELECT * FROM tbl_trn_kra_wf WHERE pending_with=?1 and status!=?2 and is_active=?3", nativeQuery = true)
	public long countByPendingWithStatusAndIsActive(String pendingWith, String status,String isActive);
}

