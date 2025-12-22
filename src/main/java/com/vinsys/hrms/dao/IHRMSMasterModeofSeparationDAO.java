package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterModeofSeparation;

public interface IHRMSMasterModeofSeparationDAO extends JpaRepository<MasterModeofSeparation, Long> {

	@Query("select separationMode from MasterModeofSeparation separationMode where separationMode.isActive = 'Y' and separationMode.orgId=?1")
	public List<MasterModeofSeparation> findAllMasterModeofSeparationCustomQuery(long orgId);
	
	@Query("select separationMode from MasterModeofSeparation separationMode where separationMode.isActive = 'Y' and separationMode.orgId=?1 and separationMode.modeOfSeparationCode=?2")
	public List<MasterModeofSeparation> findMasterModeofSeparationForEmpCustomQuery(long orgId,String code);
	
	@Query(value="SELECT * FROM tbl_map_org_mode_of_separation WHERE id = ?1 ",nativeQuery = true)
	public MasterModeofSeparation findByModeIdAndOrgId(long id,Long orgId);
	
	boolean existsByModeOfSeparationName(String resignationReasonName);
	
	@Query(value=" SELECT * FROM tbl_map_org_mode_of_separation WHERE id = ?1 ",nativeQuery = true)
	public MasterModeofSeparation findBySeprationId(long sepId);
	
}



