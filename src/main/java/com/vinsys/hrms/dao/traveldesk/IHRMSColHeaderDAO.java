package com.vinsys.hrms.dao.traveldesk;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.traveldesk.ColHeader;

public interface IHRMSColHeaderDAO extends JpaRepository<ColHeader, Long> {

	@Query("SELECT colHeader FROM ColHeader colHeader where colHeader.screenName = ?1 AND colHeader.isActive =?2 ")
	public List<ColHeader> findByScreenName(String screenName, String isActive);

}
