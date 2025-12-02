package com.vinsys.hrms.dao.traveldesk;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.vinsys.hrms.entity.traveldesk.CabRequestPassenger;

public interface IHRMSCabRequestPassengerDAO extends JpaRepository<CabRequestPassenger, Long> {

	@Query(" SELECT cabPReq FROM CabRequestPassenger cabPReq WHERE cabPReq.id = ?1 AND cabPReq.isActive = ?2 ")
	public CabRequestPassenger findCabPassengerRequestById(long childReqId, String isActive);
	
	@Query(" SELECT cabPassenger FROM CabRequestPassenger cabPassenger WHERE cabPassenger.cabRequest.id = ?1 AND cabPassenger.isActive = ?2 ")
	public List<CabRequestPassenger> findCabPassengerListByRequestId(long cabRequestId, String isActive);

	@Transactional
	@Modifying
	@Query("UPDATE CabRequestPassenger passenger set passenger.isActive='N' , passenger.updatedDate = NOW()  WHERE passenger.id in ?1")
	public void updateGuestIds(List<Long> dbGuestIds);
	
	
	@Query(" SELECT cabPassenger FROM CabRequestPassenger cabPassenger WHERE cabPassenger.id = ?1 "
			+ "AND cabPassenger.oneWayTripStatus = ?2 "
			+ "AND cabPassenger.isActive = ?3 "
			//+ "AND DATE(cabPassenger.pickupDate) >= (current_date - 1) "
			+ "AND DATE(cabPassenger.pickupDate) <= (current_date + 10) ")
	public List<CabRequestPassenger> findCabPassengerListOneWayAndRowId(long cabRequestPassengerRowId, String status,
			String isActive);
	
	@Query(" SELECT cabPassenger FROM CabRequestPassenger cabPassenger WHERE cabPassenger.id = ?1 "
			+ "AND cabPassenger.returnTripStatus = ?2 "
			+ "AND cabPassenger.isActive = ?3 "
			//+ "AND DATE(cabPassenger.pickupDate) >= (current_date - 1) "
			+ "AND DATE(cabPassenger.pickupDate) <= (current_date + 10) ")
	public List<CabRequestPassenger> findCabPassengerListReturnByRowId(long cabRequestPassengerRowId, String status,
			String isActive);
	
}
