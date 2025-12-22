package com.vinsys.hrms.idp.service.impl;

import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.dao.IEmployeeDesignationDAO;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeReportingManager;
import com.vinsys.hrms.dao.IHRMSLoginEntityTypeDAO;
import com.vinsys.hrms.dao.IHRMSMapLoginEntityTypeDAO;
import com.vinsys.hrms.dao.idp.IdpEmailLogDAO;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.LoginEntityType;
import com.vinsys.hrms.entity.MasterRole;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.dao.IIdpDAO;
import com.vinsys.hrms.idp.dao.IMapEmployeeIdpStatusDAO;
import com.vinsys.hrms.idp.entity.Idp;
import com.vinsys.hrms.idp.entity.IdpEmailLog;
import com.vinsys.hrms.idp.entity.IdpFlowHistory;
import com.vinsys.hrms.idp.entity.MapEmployeeIdpStatus;
import com.vinsys.hrms.idp.enumconstant.IdpFlowHistoryActionType;
import com.vinsys.hrms.idp.helper.IdpConverterHelper;
import com.vinsys.hrms.idp.helper.IdpDetailsHelper;
import com.vinsys.hrms.idp.helper.IdpEntityHelper;
import com.vinsys.hrms.idp.helper.IdpRoleFilterHelper;
import com.vinsys.hrms.idp.service.IIdpService;
import com.vinsys.hrms.idp.utils.IdpEnums;
import com.vinsys.hrms.idp.vo.getidp.IdpListItemVO;
import com.vinsys.hrms.idp.vo.getidp.IdpListResponseVO;
import com.vinsys.hrms.idp.vo.getidp.IdpVO;
import com.vinsys.hrms.idp.vo.save.IdpSaveResponseVO;
import com.vinsys.hrms.idp.vo.submit.IdpSubmitVO;
import com.vinsys.hrms.idp.workflow.context.IdpContext;
import com.vinsys.hrms.idp.workflow.plan.IdpPlan;
import com.vinsys.hrms.kra.dao.IFuncationHeadToDivisionFunctionDAO;
import com.vinsys.hrms.kra.entity.FuncationHeadToDivisionFunction;
import com.vinsys.hrms.master.dao.IMasterRoleDao;
import com.vinsys.hrms.master.dao.IMasterYearDAO;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.ResponseCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.vinsys.hrms.idp.utils.IdpEnums.Status.PENDING;

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
    IHRMSEmployeeReportingManager reportingManagerDao;
    @Autowired
    IHRMSMapLoginEntityTypeDAO mapLoginEntityDAO;
    @Autowired
    IMasterRoleDao masterRoleDAO;
    @Autowired
    IHRMSLoginEntityTypeDAO loginEntityTypeDAO;
    @Autowired
    IHRMSCandidateDAO candidateDAO;
    @Autowired
    IdpEmailLogDAO idpEmailLogDAO;
    @Autowired
    IMapEmployeeIdpStatusDAO mapEmployeeIdpStatusDAO;

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
            IdpRoleFilterHelper roleFilterHelper,
            IdpConverterHelper converterHelper) {
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
    public IdpSaveResponseVO saveIdp(IdpVO idpVO) throws HRMSException {
        // Initialize workflow context
        IdpContext context = new IdpContext(idpVO.getId(), idpDAO);
        IdpPlan plan = new IdpPlan();
        context.setIdpPlan(plan);
        context.setIdpAction("Save");
        //context.executeAction();

        Idp idp = context.getIdp();
        Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
        Employee loggedInEmployee = entityHelper.getActiveEmployee(loggedInEmpId);

        // Validation: Check if employee's IDP submission status is 'open'
        Optional<MapEmployeeIdpStatus> empIdpStatus = mapEmployeeIdpStatusDAO.findByEmployeeId(loggedInEmpId);

        if (empIdpStatus.isPresent()) {
            // If IDP entry exists, check if status is 'open'
            String status = empIdpStatus.get().getIdpSubmissionStatus();
            if (status == null || !status.equalsIgnoreCase("open")) {
                throw new HRMSException(1500, "Employee can only save IDP when submission status is 'open'");
            }
        }

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
     *
     * @param idpSubmitVO the submit action DTO
     */
    @Override
    @Transactional
    public void submitIdp(IdpSubmitVO idpSubmitVO) throws HRMSException {
        // Initialize workflow context
        IdpContext context = new IdpContext(idpSubmitVO.getId(), idpSubmitVO.getActiveFlowId(), idpDAO);
        Idp idp = context.getIdp();

        Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
        List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
        // Validate current owner's role and employeeId of the given IDP
        Optional<IdpFlowHistory> flowHistory = detailsHelper.getFlowHistoryById(idpSubmitVO.getActiveFlowId());
        String ownerRole = flowHistory.get().getEmployeeRole();

        if (!loggedInEmpId.equals(flowHistory.get().getEmployeeId())) {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
        }

        if (!HRMSHelper.isRolePresent(roles, ownerRole)) {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
        }

        // Update current flow history's action and mark as inactive
        detailsHelper.updateFlowHistory(idpSubmitVO);

        Employee employee = employeeDAO.findActiveEmployeeById(idp.getEmployeeId(), IHRMSConstants.isActive);
        if (ownerRole.equals(ERole.EMPLOYEE.name()) &&
                idpSubmitVO.getAction().equals(IdpFlowHistoryActionType.SUBMIT.getValue())) {
            moveNextToManagerBucket(idp, employee);
        } else if (idpSubmitVO.getAction().equals(IdpFlowHistoryActionType.APPROVE.getValue())) {
            if (ownerRole.equals(ERole.MANAGER.name())) {
                moveNextToDivisionHeadBucket(idp, employee, loggedInEmpId);
            } else if (ownerRole.equals(ERole.DIVISIONHEAD.name())) {
                moveNextToTdHeadBucket(idp);
            } else if (ownerRole.equals(ERole.TDHEAD.name())) {
                finalTdHeadApproval(idp);
            }
        } else if (idpSubmitVO.getAction().equals(IdpFlowHistoryActionType.REJECT.getValue())) {
            if (ownerRole.equals(ERole.MANAGER.name())) {
                moveBackToEmployeeBucket(idp, employee);
            } else if (ownerRole.equals(ERole.DIVISIONHEAD.name())) {
                moveBackToManagerBucket(idp, employee);
            } else if (ownerRole.equals(ERole.TDHEAD.name())) {
                moveBackToDivisionHeadBucket(idp, employee);
                Long yearId = iMasterYearDAO.findRunningYear().getId();
                idpDAO.updateIdpRequestAmountByYear(yearId);
            }
        }

        idp.setStatus(idpSubmitVO.getAction());
        idpDAO.save(idp);
        sendEmail(idp);
    }

    private void moveBackToEmployeeBucket(Idp idp, Employee employee) {
        // Manager → Employee
        detailsHelper.createFlowHistory(idp, employee.getId(), ERole.EMPLOYEE.name());
    }

    private void moveNextToManagerBucket(Idp idp, Employee employee) {
        Employee rm = employee.getEmployeeReportingManager().getReporingManager();
        detailsHelper.createFlowHistory(idp, rm.getId(), ERole.MANAGER.name());
    }

    private void moveBackToManagerBucket(Idp idp, Employee employee) {
        // Division Head → Manager
        Employee rm = employee.getEmployeeReportingManager().getReporingManager();
        detailsHelper.createFlowHistory(idp, rm.getId(), ERole.MANAGER.name());
    }

    private void moveNextToDivisionHeadBucket(Idp idp, Employee employee, Long rmEmpId) throws HRMSException {
        Long divId = employee.getEmployeeDivision().getDivision().getId();
        List<FuncationHeadToDivisionFunction> functionHeads = functionHeadToDivisionFunctionDAO.findByDivisionAndIsActive(divId, ERecordStatus.Y.name());

        if (functionHeads.isEmpty()) {
            // No function head found for the division, move to TD Head
            moveNextToTdHeadBucket(idp);
        } else {
            // verify if idp's employee is direct/indirect reportee of function head
            Long divisionHeadEmpId = null;

            for (FuncationHeadToDivisionFunction functionHead : functionHeads) {
                Long divisionHeadId = functionHead.getEmployee();

                List<Long> reporteeIds = reportingManagerDao
                        .findReporteeIdsRecursivelyByDivisionHeadId(divisionHeadId);
                reporteeIds.remove(divisionHeadId);

                boolean isIdpEmployeeReportsToDivisionHead = reporteeIds.contains(idp.getEmployeeId());
                if (isIdpEmployeeReportsToDivisionHead) {
                    divisionHeadEmpId = divisionHeadId;
                    break;
                }
            }

            //If function head found and not same as RM, assign to division head
            if (!HRMSHelper.isNullOrEmpty(divisionHeadEmpId) && !rmEmpId.equals(divisionHeadEmpId)) {
                detailsHelper.createFlowHistory(idp, divisionHeadEmpId, ERole.DIVISIONHEAD.name());
            } else {
                moveNextToTdHeadBucket(idp);
            }
        }

    }

    private void moveBackToDivisionHeadBucket(Idp idp, Employee employee) {
        Long divId = employee.getEmployeeDivision().getDivision().getId();
        List<FuncationHeadToDivisionFunction> functionHeads = functionHeadToDivisionFunctionDAO.findByDivisionAndIsActive(divId, ERecordStatus.Y.name());

        if (functionHeads.isEmpty()) {
            // No function head found for the division, move back to Manager
            moveBackToManagerBucket(idp, employee);
        } else {
            // verify if idp's employee is direct/indirect reportee of function head
            Long divisionHeadEmpId = null;

            for (FuncationHeadToDivisionFunction functionHead : functionHeads) {
                Long divisionHeadId = functionHead.getEmployee();

                List<Long> reporteeIds = reportingManagerDao
                        .findReporteeIdsRecursivelyByDivisionHeadId(divisionHeadId);
                reporteeIds.remove(divisionHeadId);

                boolean isIdpEmployeeReportsToDivisionHead = reporteeIds.contains(idp.getEmployeeId());
                if (isIdpEmployeeReportsToDivisionHead) {
                    divisionHeadEmpId = divisionHeadId;
                    break;
                }
            }

            //If function head found, assign back to division head
            if (!HRMSHelper.isNullOrEmpty(divisionHeadEmpId)) {

                Long rmEmpId = employee.getEmployeeReportingManager().getReporingManager().getId();

                if(rmEmpId.equals(divisionHeadEmpId)){
                    //If division head is same as RM, move back to Manager bucket
                    moveBackToManagerBucket(idp, employee);
                    return;
                }

                detailsHelper.createFlowHistory(idp, divisionHeadEmpId, ERole.DIVISIONHEAD.name());
            } else {
                moveBackToManagerBucket(idp, employee);
            }
        }
    }

    private void moveNextToTdHeadBucket(Idp idp) throws HRMSException {
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

        Long yearId = iMasterYearDAO.findRunningYear().getId();
        idpDAO.updateIdpRequestAmountByYear(yearId);
    }

    private void finalTdHeadApproval(Idp idp) throws HRMSException {
        // Validate if all IDP details have training assigned
        List<com.vinsys.hrms.idp.entity.IdpDetails> allDetails = idp.getDetails();
        if (allDetails != null && !allDetails.isEmpty()) {
            for (com.vinsys.hrms.idp.entity.IdpDetails detail : allDetails) {
                if (detail.getTraining() == null) {
                    throw new HRMSException(1500,
                            "Cannot approve IDP. All IDP details must have training assigned. " +
                                    "Detail '" + detail.getTrainingName() + "' does not have a training assignment.");
                }
            }
        }
        // Update all IDP details status to "Approved"
        detailsHelper.updateIdpDetailsStatus(idp, "Approved");
        // Create ApprovedTrainings records for each detail with training
        detailsHelper.createApprovedTrainingsForIdp(idp, idp.getEmployeeId());
    }

    private void sendEmail(Idp idp) {
        Optional<IdpFlowHistory> flowHistoryForEmail = detailsHelper.getFlowHistoryById(idp.getActiveFlowId());
        IdpEmailLog saveEmailLog = new IdpEmailLog();
        saveEmailLog.setIdp(idp);
        saveEmailLog.setIdpFlow(flowHistoryForEmail.get());
        saveEmailLog.setActionType(IdpEnums.EmailType.FOLLOWUP.getKey());
        saveEmailLog.setStatus(PENDING.getKey());
        idpEmailLogDAO.save(saveEmailLog);
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
    }

    @Override
    public IdpListResponseVO getAllIdp(String role, Long departmentId) throws HRMSException {

        // Logged-in user data
        Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
        Employee loggedInEmployee = entityHelper.getActiveEmployee(loggedInEmpId);

        List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
        // Validate role
        if (!HRMSHelper.isRolePresent(roles, role)) {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
        }

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
        String managerName = employeeDAO.findActiveEmployeeById(rm.getId(), IHRMSConstants.isActive).getCandidate().getFirstName() + " " +
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
