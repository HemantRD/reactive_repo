package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.OrgDivWiseHRMapping;
import com.vinsys.hrms.entity.Organization;

public interface IHRMSMapOrgDivHrDAO extends JpaRepository<OrgDivWiseHRMapping, Long> {
	
	@Query(" SELECT odHrMap FROM OrgDivWiseHRMapping odHrMap WHERE odHrMap.organization.id = ?1 AND odHrMap.division.id = ?2 "
			+ " AND odHrMap.isActive = ?3 ")
	public List<OrgDivWiseHRMapping> findOrgDivWiseHrMapping(long orgId, long divId, String isActive);

	public OrgDivWiseHRMapping findByIsActiveAndOrganization(String name, Organization org);
	

}
