package com.vinsys.hrms.dao.traveldesk;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.vinsys.hrms.entity.traveldesk.AccommodationGuest;

public interface IHRMSAccommodationGuestDAO extends JpaRepository<AccommodationGuest, Long> {

	@Query("SELECT guest from AccommodationGuest guest where  guest.accomodationReq.id = ?1"
			+ " and guest.isActive = ?2 and guest.accomodationReq.isActive = ?2	")
	public List<AccommodationGuest> findByAccomodationReq(long accomoddationRequestId, String isActive);

	@Query("SELECT guest from AccommodationGuest guest WHERE guest.accomodationReq.id = ?1 AND guest.isActive = ?2")
	public List<AccommodationGuest> findAccommodationGuestByAccId(long accommodationId, String isActive);

	@Transactional
	@Modifying
	@Query("UPDATE AccommodationGuest guest set guest.isActive='N' , guest.updatedDate = NOW() WHERE guest.id in ?1")
	public void updateGuestIds(List<Long> dbGuestIds);
}
