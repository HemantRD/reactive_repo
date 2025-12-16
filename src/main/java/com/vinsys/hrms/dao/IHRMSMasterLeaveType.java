package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.MasterLeaveType;

public interface IHRMSMasterLeaveType extends JpaRepository<MasterLeaveType, Long> {

	MasterLeaveType findByLeaveTypeCode(String leaveTypeCode);
	
	MasterLeaveType findByLeaveTypeCodeAndOrgId(String leaveTypeCode , Long id);
}
