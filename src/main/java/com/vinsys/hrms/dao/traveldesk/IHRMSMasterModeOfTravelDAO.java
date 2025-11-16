package com.vinsys.hrms.dao.traveldesk;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.traveldesk.MasterModeOfTravel;

public interface IHRMSMasterModeOfTravelDAO extends JpaRepository<MasterModeOfTravel, Long>{

	
	
	@Query(" SELECT mtm FROM MasterModeOfTravel mtm "
			+ " WHERE mtm.orgId = ?1  AND mtm.isActive = ?2 ")
	public List<MasterModeOfTravel> getAllTravelModeOrgDivWiseDAO(long organizationId, String Status);
	
	public List<MasterModeOfTravel> getAllTravelModeByIsActive(String isActive);
	
	@Query(value="select count(tmmot) from tbl_mst_mode_of_travel tmmot",nativeQuery = true)
	public long countTravelMode();
	
	
	@Query(value="select * from tbl_mst_mode_of_travel tmmot where mode_of_travel=?1",nativeQuery = true)
	public MasterModeOfTravel findByName(String Name);
	
}
