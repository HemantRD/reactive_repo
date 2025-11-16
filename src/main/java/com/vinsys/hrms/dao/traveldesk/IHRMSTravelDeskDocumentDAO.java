package com.vinsys.hrms.dao.traveldesk;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.traveldesk.TraveldeskDocument;

public interface IHRMSTravelDeskDocumentDAO extends JpaRepository<TraveldeskDocument, Long> {

	@Query(" SELECT tr FROM TraveldeskDocument tr WHERE tr.travelRequest.id = ?1 AND tr.childId = ?2 "
			+ " AND  tr.childType=?3 AND tr.isActive = ?4 ")
	public List<TraveldeskDocument> getDocumentsUsingparentId(long travelRequestId, long childId, String childType,
			String isActive);

}
