package com.vinsys.hrms.idp.service;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.vo.employeeidpstatus.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEmployeeIdpStatusService {

    /**
     * Create a new employee IDP status record
     *
     * @param request the request containing employee IDP status data
     * @return the created employee IDP status response
     */
    List<EmployeeIdpStatusResponseVO> createIdpStatus(EmployeeIdpStatusRequestVO request);

    /**
     * Get employee IDP status by ID
     *
     * @param id the ID of the employee IDP status
     * @return the employee IDP status response
     */
    EmployeeIdpStatusFullResponseVO getIdpStatus(Long id);

    /**
     * Get employee IDP status by employee ID
     *
     * @param employeeId the employee ID
     * @return the employee IDP status response
     */
    EmployeeIdpStatusFullResponseVO getByEmployeeId(Long employeeId);

    /**
     * Get all employee IDP statuses
     *
     * @return list of all employee IDP status responses
     */
    List<EmployeeIdpStatusFullResponseVO> getAllIdpStatuses();

    HRMSBaseResponse<List<EmployeeIdpStatusVO>> getOrganizationEmployeesForIdp(EmpStatusListingReq request, Pageable pageable) throws HRMSException;

    byte[] getOrganizationEmployeesForIdpExcel(EmpStatusListingReq request, Pageable pageable) throws HRMSException;
}
