package com.vinsys.hrms.idp.helper;

import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.dao.IHRMSEmployeeReportingManager;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeReportingManager;
import com.vinsys.hrms.idp.dao.IIdpDAO;
import com.vinsys.hrms.idp.dao.IIdpListReportViewDAO;
import com.vinsys.hrms.idp.entity.Idp;
import com.vinsys.hrms.idp.entity.IdpListReportView;
import com.vinsys.hrms.kra.dao.IFuncationHeadToDivisionFunctionDAO;
import com.vinsys.hrms.kra.dao.IHodToDepartmentMap;
import com.vinsys.hrms.kra.dao.IHrToDepartmentDAO;
import com.vinsys.hrms.kra.entity.HodToDepartmentMap;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.HRMSHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper class for fetching IDPs based on user role using vw_idp_list_report view
 * This helper now uses the database view to fetch department and division information
 */
@Component
public class IdpRoleFilterHelper {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IIdpDAO idpDAO;

    @Autowired
    private IIdpListReportViewDAO idpListReportViewDAO;

    @Autowired
    private IHRMSEmployeeReportingManager reportingManagerDao;

    @Autowired
    private IHodToDepartmentMap hodToDepartmentMap;

    @Autowired
    private IFuncationHeadToDivisionFunctionDAO funcationHeadToDivisionFunctionDAO;

    @Autowired
    private IHrToDepartmentDAO hrtodepartment;

    /**
     * Get IDPs filtered by user role
     */
    public List<Idp> getIdpsByRole(String role, Long loggedInEmpId, Employee loggedInEmployee) {
        if (ERole.EMPLOYEE.name().equalsIgnoreCase(role)) {
            return getEmployeeIdps(loggedInEmpId);
        } else if (ERole.MANAGER.name().equalsIgnoreCase(role)) {
            return getManagerIdps(loggedInEmployee);
//        } else if (ERole.HOD.name().equalsIgnoreCase(role)) {
//            return getHodIdps(loggedInEmpId);
//        } else if (ERole.HR.name().equalsIgnoreCase(role)) {
//            return getHrIdps(loggedInEmpId);
        } else if (ERole.DIVISIONHEAD.name().equalsIgnoreCase(role)) {
            return getDivisionHeadIdps(loggedInEmpId);
        } else if (ERole.TDHEAD.name().equalsIgnoreCase(role)) {
            return getAdminIdps();
        }

        return Collections.emptyList();
    }

    /**
     * Get IDPs for employee role
     */
    private List<Idp> getEmployeeIdps(Long employeeId) {
        return idpDAO.findByEmployeeId(employeeId);
    }

    /**
     * Get IDPs for manager role (direct reports)
     */
    private List<Idp> getManagerIdps(Employee manager) {
        List<EmployeeReportingManager> reportees = reportingManagerDao
                .findByReporingManagerAndIsActive(manager, ERecordStatus.Y.name());

        if (reportees.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> reporteeIds = reportees.stream()
                .map(r -> r.getEmployee().getId())
                .collect(Collectors.toList());

        List<Idp> idps = idpDAO.findByEmployeeIdIn(reporteeIds);
        if (HRMSHelper.isNullOrEmpty(idps)) {
            idps = Collections.emptyList();
        }
        return idps;
    }

    /**
     * Get IDPs for HOD role (by mapped departments using view)
     */
    private List<Idp> getHodIdps(Long hodEmployeeId) {
        List<HodToDepartmentMap> departmentMaps = hodToDepartmentMap
                .findByEmployeeIdAndIsActive(hodEmployeeId, ERecordStatus.Y.name());

        if (departmentMaps.isEmpty()) {
            log.info("No departments found for HOD employee: {}", hodEmployeeId);
            return Collections.emptyList();
        }

        List<Long> departmentIds = departmentMaps.stream()
                .map(HodToDepartmentMap::getDepartmentId)
                .collect(Collectors.toList());

        log.info("HOD employee {} has access to departments: {}", hodEmployeeId, departmentIds);

        // Use view to fetch IDPs by department
        List<IdpListReportView> viewResults = idpListReportViewDAO.findByEmpDepartmentIdIn(departmentIds);

        // Convert view results to IDP entity IDs and fetch full entities
        return convertViewResultsToIdps(viewResults);
    }

    /**
     * Get IDPs for HR role (by mapped departments using view)
     */
    private List<Idp> getHrIdps(Long hrEmployeeId) {
        List<Long> departmentIds = hrtodepartment.findByEmployeeIds(hrEmployeeId);

        if (HRMSHelper.isNullOrEmpty(departmentIds)) {
            log.info("No departments found for HR employee: {}", hrEmployeeId);
            return Collections.emptyList();
        }

        log.info("HR employee {} has access to departments: {}", hrEmployeeId, departmentIds);

        // Use view to fetch IDPs by department
        List<IdpListReportView> viewResults = idpListReportViewDAO.findByEmpDepartmentIdIn(departmentIds);

        return convertViewResultsToIdps(viewResults);
    }

    /**
     * Get IDPs for DIVISIONHEAD role (by mapped divisions using view)
     */
    private List<Idp> getDivisionHeadIdps(Long divisionHeadId) {
        List<Long> reporteeIds = reportingManagerDao
                .findReporteeIdsRecursivelyByDivisionHeadId(divisionHeadId);

        if (!HRMSHelper.isNullOrEmpty(reporteeIds)) {
            reporteeIds.remove(divisionHeadId);
        } else {
            return Collections.emptyList();
        }

        List<Idp> idps = idpDAO.findByEmployeeIdIn(reporteeIds);
        if (HRMSHelper.isNullOrEmpty(idps)) {
            idps = Collections.emptyList();
        }
        return idps;
    }

    /**
     * Get all IDPs for admin role
     */
    private List<Idp> getAdminIdps() {
        return idpDAO.findAll();
    }

    /**
     * Convert view results to IDP entities
     */
    private List<Idp> convertViewResultsToIdps(List<IdpListReportView> viewResults) {
        if (viewResults.isEmpty()) {
            return Collections.emptyList();
        }

        // Extract IDP IDs from view results
        List<Long> idpIds = viewResults.stream()
                .map(IdpListReportView::getId)
                .distinct()
                .collect(Collectors.toList());

        // Fetch full IDP entities
        return idpDAO.findAllById(idpIds);
    }

    /**
     * Filter IDPs by department ID using the view
     * This is used for additional filtering when departmentId parameter is provided in API
     */
    public List<Idp> filterByDepartment(List<Idp> idpList, Long departmentId) {
        if (idpList == null || idpList.isEmpty()) {
            return Collections.emptyList();
        }

        if (departmentId == null) {
            return idpList;
        }

        log.info("Applying additional department filter: departmentId={}", departmentId);

        // Get IDP IDs from the input list
        List<Long> idpIds = idpList.stream()
                .map(Idp::getId)
                .collect(Collectors.toList());

        // Query view to get only IDPs that belong to the specified department
        List<IdpListReportView> viewResults = idpListReportViewDAO.findByEmployeeIdIn(
                idpList.stream().map(Idp::getEmployeeId).collect(Collectors.toList())
        );

        // Filter view results by department
        List<Long> filteredIdpIds = viewResults.stream()
                .filter(view -> departmentId.equals(view.getEmpDepartmentId()))
                .map(IdpListReportView::getId)
                .distinct()
                .collect(Collectors.toList());

        // Return only IDPs that match the department filter
        return idpList.stream()
                .filter(idp -> filteredIdpIds.contains(idp.getId()))
                .collect(Collectors.toList());
    }
}
