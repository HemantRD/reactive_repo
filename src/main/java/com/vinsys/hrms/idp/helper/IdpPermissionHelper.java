package com.vinsys.hrms.idp.helper;

import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeReportingManager;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeReportingManager;
import com.vinsys.hrms.idp.dao.IIdpListReportViewDAO;
import com.vinsys.hrms.idp.entity.Idp;
import com.vinsys.hrms.idp.entity.IdpListReportView;
import com.vinsys.hrms.kra.dao.IHodToDepartmentMap;
import com.vinsys.hrms.kra.dao.IHrToDepartmentDAO;
import com.vinsys.hrms.kra.entity.HodToDepartmentMap;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.HRMSHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Helper class for IDP permission validation
 */
@Component
public class IdpPermissionHelper {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final IHRMSEmployeeDAO employeeDAO;
    private final IHRMSEmployeeReportingManager reportingManagerDao;
    private final IHodToDepartmentMap hodToDepartmentMap;
    private final IHrToDepartmentDAO hrtodepartment;
    private final IIdpListReportViewDAO idpListReportViewDAO;

    @Autowired
    public IdpPermissionHelper(IHRMSEmployeeDAO employeeDAO,
                               IHRMSEmployeeReportingManager reportingManagerDao,
                               IHodToDepartmentMap hodToDepartmentMap,
                               IHrToDepartmentDAO hrtodepartment,
                               IIdpListReportViewDAO idpListReportViewDAO) {
        this.employeeDAO = employeeDAO;
        this.reportingManagerDao = reportingManagerDao;
        this.hodToDepartmentMap = hodToDepartmentMap;
        this.hrtodepartment = hrtodepartment;
        this.idpListReportViewDAO = idpListReportViewDAO;
    }

    /**
     * Validate if user has permission to review the IDP
     * @throws RuntimeException if permission denied
     */
    public void validateReviewPermission(Idp idp, Long loggedInEmpId, List<String> roles) {
        if (hasManagerPermission(idp, loggedInEmpId, roles)) {
            return;
        }

        if (hasHodPermission(idp, loggedInEmpId, roles)) {
            return;
        }

        if (hasHrPermission(idp, loggedInEmpId, roles)) {
            return;
        }

        if (hasAdminPermission(roles)) {
            return;
        }

        throw new RuntimeException("You do not have permission to review this IDP");
    }

    /**
     * Check if user has MANAGER permission
     */
    private boolean hasManagerPermission(Idp idp, Long loggedInEmpId, List<String> roles) {
        if (!HRMSHelper.isRolePresent(roles, ERole.MANAGER.name())) {
            return false;
        }

        Employee employee = employeeDAO.findActiveEmployeeById(idp.getEmployeeId(), ERecordStatus.Y.name());
        if (employee == null) {
            return false;
        }

        List<EmployeeReportingManager> reportingManagers = reportingManagerDao
                .findByReporingManagerAndIsActive(employee, ERecordStatus.Y.name());

        for (EmployeeReportingManager rm : reportingManagers) {
            if (rm.getReporingManager() != null &&
                rm.getReporingManager().getId().equals(loggedInEmpId)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if user has HOD permission
     */
    private boolean hasHodPermission(Idp idp, Long loggedInEmpId, List<String> roles) {
        if (!HRMSHelper.isRolePresent(roles, ERole.HOD.name())) {
            return false;
        }

        // Fetch departmentId from view
        IdpListReportView idpView = idpListReportViewDAO.findByIdpId(idp.getId());
        if (idpView == null || idpView.getEmpDepartmentId() == null) {
            log.warn("Department ID not found for IDP: {}", idp.getId());
            return false;
        }

        // Check if logged in HOD has access to this department
        List<HodToDepartmentMap> hodDepartments = hodToDepartmentMap
                .findByEmployeeIdAndIsActive(loggedInEmpId, ERecordStatus.Y.name());

        for (HodToDepartmentMap hodDept : hodDepartments) {
            if (hodDept.getDepartmentId().equals(idpView.getEmpDepartmentId())) {
                log.debug("HOD permission granted for IDP: {} by employee: {}", idp.getId(), loggedInEmpId);
                return true;
            }
        }

        log.debug("HOD permission denied for IDP: {} by employee: {}", idp.getId(), loggedInEmpId);
        return false;
    }

    /**
     * Check if user has HR permission
     */
    private boolean hasHrPermission(Idp idp, Long loggedInEmpId, List<String> roles) {
        if (!HRMSHelper.isRolePresent(roles, ERole.HR.name())) {
            return false;
        }

        // Fetch departmentId from view
        IdpListReportView idpView = idpListReportViewDAO.findByIdpId(idp.getId());
        if (idpView == null || idpView.getEmpDepartmentId() == null) {
            log.warn("Department ID not found for IDP: {}", idp.getId());
            return false;
        }

        // Check if logged in HR has access to this department
        List<Long> hrDepartments = hrtodepartment.findByEmployeeIds(loggedInEmpId);
        boolean hasPermission = hrDepartments != null && hrDepartments.contains(idpView.getEmpDepartmentId());

        if (hasPermission) {
            log.debug("HR permission granted for IDP: {} by employee: {}", idp.getId(), loggedInEmpId);
        } else {
            log.debug("HR permission denied for IDP: {} by employee: {}", idp.getId(), loggedInEmpId);
        }

        return hasPermission;
    }

    /**
     * Check if user has ADMIN permission
     */
    private boolean hasAdminPermission(List<String> roles) {
        return HRMSHelper.isRolePresent(roles, ERole.ADMIN.name());
    }
}
