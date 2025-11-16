package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterModeofSeparationReason;

public interface IHRMSMasterModeofSeparationReasonDAO extends JpaRepository<MasterModeofSeparationReason, Long>{
	
	@Query("select separationModeReason from MasterModeofSeparationReason separationModeReason where separationModeReason.isActive = 'Y' and separationModeReason.masterModeofSeparation.id=?1")
	public List<MasterModeofSeparationReason> findAllMasterModeofSeparationReasonCustomQuery(long id);
	
	@Query(value="SELECT * FROM tbl_map_org_separation_reason WHERE id = ?1 ",nativeQuery = true)
	public MasterModeofSeparationReason findByReasonId(long id);
	
	boolean existsByReasonName(String reasonName);

	

}
