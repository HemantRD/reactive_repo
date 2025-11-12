package com.vinsys.hrms.dao.traveldesk;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.traveldesk.MasterVehicle;

public interface IHRMSMasterVehicleDAO extends JpaRepository<MasterVehicle, Long> {

	@Query("select vehicle from MasterVehicle vehicle where vehicle.organization.id=?1 AND vehicle.division.id=?2 "
			+ " AND vehicle.isActive=?3")
	public List<MasterVehicle> findAllVehicleByOrgIdandDivId(long orgId, long divId, String isActive);

	@Query(" SELECT mv FROM MasterVehicle mv WHERE mv.id = ?1 AND mv.isActive = ?2 ")
	public MasterVehicle findVehicleById(long driverId, String isActive);
	
	@Query("select vehicle from MasterVehicle vehicle where vehicle.division.id=?1 AND vehicle.isActive=?2")
	public List<MasterVehicle> findAllVehicleByDivIdAndIsActive(long divId, String isActive);
	
	@Query("select count(vehicle) from MasterVehicle vehicle where vehicle.division.id=?1 AND vehicle.isActive=?2")
	public long countOfVehicle(long divId, String isActive); 
	
	@Query("select vehicle from MasterVehicle vehicle where vehicle.id=?1 AND vehicle.isActive=?2")
	public MasterVehicle findIdAndIsActive(Long id, String isActive);

}
