package com.vinsys.hrms.idp.service.impl;

import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.dao.*;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.LoginEntityType;
import com.vinsys.hrms.entity.MasterRole;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.dao.IIdpDAO;
import com.vinsys.hrms.idp.entity.Idp;
import com.vinsys.hrms.idp.entity.IdpFlowHistory;
import com.vinsys.hrms.idp.enumconstant.IdpFlowHistoryActionType;
import com.vinsys.hrms.idp.helper.*;
import com.vinsys.hrms.idp.service.IIdpService;
import com.vinsys.hrms.idp.vo.getidp.IdpListItemVO;
import com.vinsys.hrms.idp.vo.getidp.IdpListResponseVO;
import com.vinsys.hrms.idp.vo.getidp.IdpVO;
import com.vinsys.hrms.idp.vo.save.IdpSaveResponseVO;
import com.vinsys.hrms.idp.vo.submit.IdpSubmitVO;
import com.vinsys.hrms.idp.workflow.context.IdpContext;
import com.vinsys.hrms.idp.workflow.plan.IdpPlan;
import com.vinsys.hrms.kra.dao.IFuncationHeadToDivisionFunctionDAO;
import com.vinsys.hrms.kra.dao.IHodToDepartmentMap;
import com.vinsys.hrms.kra.entity.FuncationHeadToDivisionFunction;
import com.vinsys.hrms.master.dao.IMasterRoleDao;
import com.vinsys.hrms.master.dao.IMasterYearDAO;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.IHRMSConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link IIdpService} responsible for managing the lifecycle
 * and workflow transitions of Individual Development Plans (IDP).
 * <p>
 * This class handles:
 * <ul>
 *     <li>Creating or updating IDP records</li>
 *     <li>Executing workflow transitions (Submit, Approve, Reject)</li>
 *     <li>Review and feedback operations</li>
 *     <li>Retrieving IDP details and listings</li>
 * </ul>
 * <p>
 * Workflow transitions are driven using the State Design Pattern via
 * {@link IdpContext}, {@link IdpPlan}, and {@link com.vinsys.hrms.idp.workflow.state.IdpState}.
 * </p>
 *
 * @author Dhaval
 * @since 1.0
 */
@Service
public class IdpServiceImpl implements IIdpService {

    private final IIdpDAO idpDAO;
    private final IHRMSEmployeeDAO employeeDAO;
    private final IEmployeeDesignationDAO designationDAO;

    // Helper classes
    private final IdpEntityHelper entityHelper;
    private final IdpDetailsHelper detailsHelper;
    private final IdpRoleFilterHelper roleFilterHelper;
    private final IdpConverterHelper converterHelper;

    @Autowired
    IFuncationHeadToDivisionFunctionDAO functionHeadToDivisionFunctionDAO;
    @Autowired
    IHodToDepartmentMap hodToDepartmentMap;
    @Autowired
    IHRMSMapLoginEntityTypeDAO mapLoginEntityDAO;
    @Autowired
    IMasterRoleDao masterRoleDAO;
    @Autowired
    IHRMSLoginEntityTypeDAO loginEntityTypeDAO;
    @Autowired
    IHRMSCandidateDAO candidateDAO;

    @Autowired
    IMasterYearDAO iMasterYearDAO;

    /**
     * Constructor injection for all dependencies
     */
    @Autowired
    public IdpServiceImpl(
            IIdpDAO idpDAO,
            IHRMSEmployeeDAO employeeDAO,
            IEmployeeDesignationDAO designationDAO,
            IdpEntityHelper entityHelper,
            IdpDetailsHelper detailsHelper,
            IdpPermissionHelper permissionHelper,
            IdpRoleFilterHelper roleFilterHelper,
            IdpConverterHelper converterHelper,
            IdpStatusTrackingHelper statusTrackingHelper) {
        this.idpDAO = idpDAO;
        this.employeeDAO = employeeDAO;
        this.designationDAO = designationDAO;
        this.entityHelper = entityHelper;
        this.detailsHelper = detailsHelper;
        this.roleFilterHelper = roleFilterHelper;
        this.converterHelper = converterHelper;
    }

    /**
     * Creates or updates an IDP record.
     * <p>
     * - If {@code id} is null → creates a new DRAFT record. <br>
     * - If {@code id} exists → updates existing record fields.
     * </p>
     *
     * @param idpVO the IDP data transfer object containing employee and plan details
     * @return the persisted {@link IdpVO} after creation or update
     */
    @Override
    @Transactional
    public IdpSaveResponseVO saveIdp(IdpVO idpVO) {
        // Initialize workflow context
        IdpContext context = new IdpContext(idpVO.getId(), idpDAO);
        IdpPlan plan = new IdpPlan();
        context.setIdpPlan(plan);
        context.setIdpAction("Save");
        //context.executeAction();

        Idp idp = context.getIdp();
        Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
        Employee loggedInEmployee = entityHelper.getActiveEmployee(loggedInEmpId);

        // For new IDP, populate employee details
        if (context.getIdpId() == null) {
            Idp tempIdp = entityHelper.createNewIdp(loggedInEmployee);
            BeanUtils.copyProperties(tempIdp, idp);
        }
        idp.setYear(iMasterYearDAO.findRunningYear());

        // Save IDP entity
        idp = idpDAO.save(idp);

        // Create flow history for new IDP
        if (context.getIdpId() == null) {
            detailsHelper.createFlowHistory(idp, loggedInEmpId, ERole.EMPLOYEE.name());
            idp = idpDAO.save(idp); // Save again to update activeFlowId
        }

        // Save IDP details if provided
        if (idpVO.getIdpDetails() != null && !idpVO.getIdpDetails().isEmpty()) {
            detailsHelper.saveIdpDetailsFromVO(idp, idpVO.getIdpDetails());
        }

        return generateSaveResponse(idp);
    }

    /**
     * Executes a basic submit operation for backward compatibility.
     * Internally delegates to {@link(IdpSubmitVO)}.
     *
     * @param idpSubmitVO the submit action DTO
     */
    @Override
    @Transactional
    public void submitIdp(IdpSubmitVO idpSubmitVO) throws HRMSException {
        // Initialize workflow context
        IdpContext context = new IdpContext(idpSubmitVO.getId(), idpSubmitVO.getActiveFlowId(), idpDAO);

        Idp idp = context.getIdp();

        Optional<IdpFlowHistory> flowHistory = detailsHelper.getFlowHistroryById(idpSubmitVO.getActiveFlowId());
        String currentRole = flowHistory.get().getEmployeeRole();

        Employee employee = employeeDAO.findActiveEmployeeById(idp.getEmployeeId(), IHRMSConstants.isActive);
        if (currentRole.equals(ERole.EMPLOYEE.name()) &&
                idpSubmitVO.getAction().equals(IdpFlowHistoryActionType.SUBMIT.getValue())) {
            Employee rm = employee.getEmployeeReportingManager().getReporingManager();
            detailsHelper.createFlowHistory(idp, rm.getId(), ERole.MANAGER.name());
            // Update current flow history's action and mark as inactive
            detailsHelper.updateFlowHistory(idpSubmitVO);
        } else if (idpSubmitVO.getAction().equals(IdpFlowHistoryActionType.APPROVE.getValue())) {
            if (currentRole.equals(ERole.MANAGER.name())) {
                Long divId = employee.getEmployeeDivision().getDivision().getId();
                FuncationHeadToDivisionFunction funcationHeadToDivisionFunction = functionHeadToDivisionFunctionDAO.findByDivisionAndIsActive(divId, ERecordStatus.Y.name()).get(0);
                detailsHelper.createFlowHistory(idp, funcationHeadToDivisionFunction.getEmployee(), ERole.DIVISIONHEAD.name());
                // Update current flow history's action and mark as inactive
                detailsHelper.updateFlowHistory(idpSubmitVO);
            } else if (currentRole.equals(ERole.DIVISIONHEAD.name())) {

                MasterRole tdHeadRole = masterRoleDAO.findByRoleName(ERole.TDHEAD.name());
                if (tdHeadRole == null) {
                    throw new HRMSException(1500, "TDHEAD role not configured in MasterRole table");
                }
                LoginEntityType loginEntityTypeTdHead = loginEntityTypeDAO.findByRoleIdAndIsActive(tdHeadRole.getId(), ERecordStatus.Y.name());
                if (loginEntityTypeTdHead == null) {
                    throw new HRMSException(1500, "TDHEAD login entity type not configured in LoginEntityType table");
                }

                List<Long> tdHeadLoginEntityIds = mapLoginEntityDAO.findLoginEntityIdsByLoginEntityTypeId(loginEntityTypeTdHead.getId());

                if (tdHeadLoginEntityIds.isEmpty()) {
                    throw new HRMSException(1500, "No TDHEAD users mapped in MapLoginEntityType table");
                }

                if (tdHeadLoginEntityIds.size() > 1) {
                    throw new HRMSException(1500, "Multiple TDHEAD users mapped in MapLoginEntityType table. Expected only one.");
                }

                List<Candidate> candidates = candidateDAO.findByLoginEntityIdIn(tdHeadLoginEntityIds);
                if (candidates.isEmpty()) {
                    throw new HRMSException(1500, "No Candidate found for the TDHEAD login entity id");
                }

                // Get the employee ID from the candidate
                Long tdHeadEmpId = candidates.get(0).getEmployee().getId();
                detailsHelper.createFlowHistory(idp, tdHeadEmpId, ERole.TDHEAD.name());
                // Update current flow history's action and mark as inactive
                detailsHelper.updateFlowHistory(idpSubmitVO);
            } else if (currentRole.equals(ERole.TDHEAD.name())) {
                // Final Approval - No further flow
                // Update current flow history's action and mark as inactive
                detailsHelper.updateFlowHistory(idpSubmitVO);
            }
        } else if (idpSubmitVO.getAction().equals(IdpFlowHistoryActionType.REJECT.getValue())) {
            if (currentRole.equals(ERole.MANAGER.name())) {
                // Manager → Employee
                detailsHelper.createFlowHistory(idp, employee.getId(), ERole.EMPLOYEE.name());
                // Update current flow history's action and mark as inactive
                detailsHelper.updateFlowHistory(idpSubmitVO);
            } else if (currentRole.equals(ERole.DIVISIONHEAD.name())) {
                // Division Head → Manager
                Employee rm = employee.getEmployeeReportingManager().getReporingManager();
                detailsHelper.createFlowHistory(idp, rm.getId(), ERole.MANAGER.name());
                // Update current flow history's action and mark as inactive
                detailsHelper.updateFlowHistory(idpSubmitVO);
            } else if (currentRole.equals(ERole.TDHEAD.name())) {
                // TDHEAD → Division Head
                Long divId = employee.getEmployeeDivision().getDivision().getId();
                FuncationHeadToDivisionFunction funcHead =
                        functionHeadToDivisionFunctionDAO.findByDivisionAndIsActive(divId, ERecordStatus.Y.name())
                                .get(0);
                detailsHelper.createFlowHistory(idp, funcHead.getEmployee(), ERole.DIVISIONHEAD.name());
                // Update current flow history's action and mark as inactive
                detailsHelper.updateFlowHistory(idpSubmitVO);
            }
        }

        idp.setStatus(idpSubmitVO.getAction());
        idpDAO.save(idp);

        // Track submission status
        //statusTrackingHelper.trackIdpSubmission(idp);
    }

    /**
     * Records or updates feedback for a given IDP record.
     * Typically performed during the review phase by managers.
     * Allows updating training details and adding comments in separate comments table.
     *
     * @param idpVO the updated IDP data containing review-related details
     */
    @Override
    @Transactional
    public void reviewIdp(IdpVO idpVO) throws HRMSException {

        IdpContext context = new IdpContext(idpVO.getId(), idpVO.getActiveFlowId(), idpDAO);

        // Update IDP details with manager's review comments and corrections
        if (idpVO.getIdpDetails() != null && !idpVO.getIdpDetails().isEmpty()) {
            detailsHelper.updateDetailsWithComments(context.getIdp(), idpVO.getIdpDetails(), idpVO.getActiveFlowId());
        }

        // Track review status
        //statusTrackingHelper.trackIdpStatusChange(idp, "Reviewed", loggedInEmpId);
    }

    @Override
    public IdpListResponseVO getAllIdp(String role, Long departmentId) throws HRMSException {

        // Logged-in user data
        List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
        Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
        Employee loggedInEmployee = entityHelper.getActiveEmployee(loggedInEmpId);

        // Validate role
/*        if (!HRMSHelper.isRolePresent(roles, role)) {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
        }*/

        // Use RoleFilterHelper to get IDPs based on role
        List<Idp> idpList = roleFilterHelper.getIdpsByRole(role, loggedInEmpId, loggedInEmployee);

        // Apply additional department filter if departmentId is provided
        if (departmentId != null) {
            idpList = roleFilterHelper.filterByDepartment(idpList, departmentId);
        }

        // Convert to VOs using ConverterHelper
        List<IdpListItemVO> idpListItems = converterHelper.convertIdpListToListItemVOs(
                idpList, loggedInEmpId, role, employeeDAO, designationDAO);

        // Build response
        return IdpListResponseVO.builder()
                .status("success")
                .data(IdpListResponseVO.DataWrapper.builder()
                        .idpList(idpListItems)
                        .build())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public IdpVO getIdpById(Long idpId) {
        // Fetch IDP entity
        Idp idp = idpDAO.findById(idpId).orElse(null);
        if (idp == null) {
            return null;
        }

        // Use ConverterHelper to convert to VO
        Employee employee = employeeDAO.findActiveEmployeeById(idp.getEmployeeId(), IHRMSConstants.isActive);
        Employee rm = employee.getEmployeeReportingManager().getReporingManager();
        String managerName= employeeDAO.findActiveEmployeeById(rm.getId(), IHRMSConstants.isActive).getCandidate().getFirstName() + " " +
                employeeDAO.findActiveEmployeeById(rm.getId(), IHRMSConstants.isActive).getCandidate().getLastName();

        return converterHelper.convertToIdpVO(idp, managerName, employeeDAO, employee);
    }

    private IdpSaveResponseVO generateSaveResponse(Idp entity) {
        if (entity == null) return null;

        return IdpSaveResponseVO.builder()
                .status("success")
                .data(IdpSaveResponseVO.DataVO.builder()
                        .id(entity.getId())
                        .activeFlowId(entity.getActiveFlowId())
                        .build())
                .build();
    }
}
