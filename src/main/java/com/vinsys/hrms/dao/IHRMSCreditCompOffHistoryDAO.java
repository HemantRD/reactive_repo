package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.EmployeeCompOffCreditLeaveHistory;

public interface IHRMSCreditCompOffHistoryDAO extends JpaRepository<EmployeeCompOffCreditLeaveHistory, Long>{

}
