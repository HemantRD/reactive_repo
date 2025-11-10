package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterOrg_NoticePeriod;

public interface IHRMSMasterNoticePeriod extends JpaRepository<MasterOrg_NoticePeriod, Long> {
	
	@Query("select noticePeriod from MasterOrg_NoticePeriod noticePeriod where noticePeriod.isActive = 'Y' and noticePeriod.orgId=?1")
	public List<MasterOrg_NoticePeriod> findAllMasterNoticePeriodCustomQuery(long orgid);

}
