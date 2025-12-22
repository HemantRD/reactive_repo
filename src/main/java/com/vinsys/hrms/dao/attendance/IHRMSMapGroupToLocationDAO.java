package com.vinsys.hrms.dao.attendance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.attendance.MapGroupToLocation;
import com.vinsys.hrms.entity.attendance.MasterOrgDivLocationGroup;

public interface IHRMSMapGroupToLocationDAO extends JpaRepository<MapGroupToLocation, Long> {

	@Query(" SELECT grpToLoc FROM MapGroupToLocation grpToLoc "
			+ " WHERE grpToLoc.isActive = ?1 AND grpToLoc.mstLocationGroup = ?2 ")
	public List<MapGroupToLocation> getGroupToLocationByGroup(String isActive, MasterOrgDivLocationGroup group);
}
