package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterRole;
import com.vinsys.hrms.kra.entity.KraCycle;
import com.vinsys.hrms.kra.entity.KraCycleCalender;

public interface IKraCalenderDao extends JpaRepository<KraCycleCalender, Long> {

	
	@Query(value = "SELECT * FROM tbl_mst_kra_cycle_calender\r\n"
			+ "WHERE is_active = ?1 AND status = ?2 AND role_id = ?3 and cycle_id = ?4", nativeQuery = true)
	public KraCycleCalender findByIsActiveStatusAndRole(String isActive,Long statusId,Long roleId,Long cycleId);
	
	
	List<KraCycleCalender> findByisActive(String name);


	public List<KraCycleCalender> findByCycleIdAndIsActive(KraCycle existingCycle, String name);


	public KraCycleCalender findByCycleIdAndRoleIdAndIsActive(KraCycle cycle, MasterRole roleName, String name);

	public List<KraCycleCalender> findByRoleIdAndIsActive(MasterRole roleName, String name);


	public List<KraCycleCalender> findByIsActive(String name);


	public List<KraCycleCalender> findByCycleId(KraCycle kraCycle);


	@Modifying
	@Query(value = "DELETE FROM tbl_mst_kra_cycle_calender WHERE cycle_id = ?1", nativeQuery = true)
	void deleteByCycleId(Long cycleId);

}

