package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.MasterLeaveType;
import com.vinsys.hrms.entity.Organization;

public interface IHRMSMasterLeaveTypeDAO extends JpaRepository<MasterLeaveType, Long> {

	@Query("select masterLeaveType from MasterLeaveType masterLeaveType "
			+ " where masterLeaveType.organization.id = ?1 and masterLeaveType.isActive = ?2")
	/*
	 * + "	and masterLeaveType.division.id = ?2" +
	 * " and masterLeaveType.branch.id = ?3")
	 */
	public List<MasterLeaveType> findAllMasterLeaveTypeByOrgIdCustomQuery(long orgId,
			String isActive /* , long divId, long branchId */);

	public MasterLeaveType findByIsActiveAndOrganizationAndLeaveTypeCode(String isActive, Organization org,
			String leaveTypeCode);

	public MasterLeaveType findByLeaveTypeCode(String leaveTypeCode);
	public MasterLeaveType findByLeaveTypeCodeAndOrgId(String leaveTypeCode,Long orgId);

	public MasterLeaveType findByIdAndIsActive(Long id, String isActive);
	
	public MasterLeaveType findByIdAndIsActiveAndOrgId(Long id, String isActive,Long orgId);
	
}
