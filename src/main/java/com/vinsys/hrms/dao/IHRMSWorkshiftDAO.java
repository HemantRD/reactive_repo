package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterWorkshift;

public interface IHRMSWorkshiftDAO extends JpaRepository<MasterWorkshift, Long> {

	public List<MasterWorkshift> findByOrgId(Long organization);

	@Query("SELECT mstWrk FROM MasterWorkshift mstWrk where mstWrk.workshiftName = ?1 "
			+ " AND mstWrk.orgId = ?2 ")
	public MasterWorkshift findGeneralWorkshiftByOrganization(String generalStr, long orgId);
}
