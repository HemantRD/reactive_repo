package com.vinsys.hrms.dao.traveldesk;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.traveldesk.MapCabDriverVehicle;

public interface IHRMSMapCabDriverVehicleDAO extends JpaRepository<MapCabDriverVehicle, Long> {

	@Query(" SELECT cabDrivVeh FROM MapCabDriverVehicle cabDrivVeh WHERE cabDrivVeh.id = ?1 "
			+ " AND cabDrivVeh.isActive = ?2 ")
	public MapCabDriverVehicle findMapCabDriverVehicleById(long id, String isActive);

	@Query(" SELECT cabDrivVeh FROM MapCabDriverVehicle cabDrivVeh WHERE cabDrivVeh.cabRequestPassengerId = ?1 "
			+ " AND cabDrivVeh.isRecurring = ?2 AND cabDrivVeh.isActive = ?3 ")
	public List<MapCabDriverVehicle> findMapCabDriverByCabReqId(long cabReqId, boolean isRecurring, String isActive);
	
	@Query(" SELECT cabDrivVeh FROM MapCabDriverVehicle cabDrivVeh WHERE cabDrivVeh.cabRequestPassengerId = ?1 "
			+ " AND cabDrivVeh.isRecurring = ?2 AND cabDrivVeh.tripWay = ?3 AND cabDrivVeh.isActive = ?4 ")
	public MapCabDriverVehicle findMapCabDriverByCabReqIdTripWay(long cabReqId, boolean isRecurring, String tripWay, 
			String isActive);
	
	@Query(" SELECT cabDrivVeh FROM MapCabDriverVehicle cabDrivVeh WHERE cabDrivVeh.driverId.id = ?1 "
			+ " AND cabDrivVeh.isActive = ?2 ")
	public List<MapCabDriverVehicle> findMapCabDriverVehicleByDriverId(long driverId, String isActive);
}
