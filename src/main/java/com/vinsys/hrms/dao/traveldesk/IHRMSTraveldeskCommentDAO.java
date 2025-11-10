package com.vinsys.hrms.dao.traveldesk;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.traveldesk.TraveldeskComment;

public interface IHRMSTraveldeskCommentDAO extends JpaRepository<TraveldeskComment, Long> {

	@Query("SELECT ticketReq from TraveldeskComment ticketReq "
			+ "WHERE ticketReq.childId = ?1 and ticketReq.childType = ?2 ")
	public List<TraveldeskComment> getCommentsOfRequest(long id, String childType);
}
