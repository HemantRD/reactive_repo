package com.vinsys.hrms.employee.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.vinsys.hrms.audit.vo.AuditLogRequestVo;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.employee.vo.EmployeeAddressVO;
import com.vinsys.hrms.employee.vo.EmployeeProfileVO;
import com.vinsys.hrms.employee.vo.EmployeeVO;
import com.vinsys.hrms.employee.vo.HRListResponseVO;
import com.vinsys.hrms.exception.HRMSException;

public interface IOrganazationService {

	HRMSBaseResponse<List<EmployeeAddressVO>> findAllEmployeeAddressBook(Long branchId,String branch,String keyword ,Long gradeId, Long deptId, Pageable pageable) throws HRMSException;

	HRMSBaseResponse<List<EmployeeAddressVO>> findAllEmployeeAddressBookByDepartment() throws HRMSException;;
	
	HRMSBaseResponse<List<EmployeeVO>> getActiveEmployeeList() throws HRMSException;

	HRMSBaseResponse<EmployeeProfileVO> getEmployeeProfileDetails(Long employeeId) throws HRMSException;

	byte[] downloadEmployeeDetailsReport(String keyword) throws HRMSException;

	HRMSBaseResponse<List<HRListResponseVO>> getActiveHRList() throws HRMSException;
}
