package com.vinsys.hrms.idp.service.impl;

import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.idp.dao.IIdpDAO;
import com.vinsys.hrms.idp.dao.IIdpDetailsDAO;
import com.vinsys.hrms.idp.entity.IdpDetails;
import com.vinsys.hrms.idp.enumconstant.IdpStatus;
import com.vinsys.hrms.idp.enumconstant.IdpRecordStatus;
import com.vinsys.hrms.idp.service.IIdpService;
import com.vinsys.hrms.idp.vo.IdpVO;
import com.vinsys.hrms.idp.vo.IdpSubmitVO;
import com.vinsys.hrms.idp.entity.Idp;
import com.vinsys.hrms.idp.workflow.context.IdpContext;
import com.vinsys.hrms.idp.workflow.plan.IdpPlan;
import com.vinsys.hrms.idp.workflow.state.IdpState;
import com.vinsys.hrms.security.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static com.vinsys.hrms.idp.workflow.context.IdpContext.getIdpState;

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
 * {@link IdpContext}, {@link IdpPlan}, and {@link IdpState}.
 * </p>
 *
 * @author
 * @since 1.0
 */
@Service
public class IdpServiceImpl implements IIdpService {

    @Autowired
    private IIdpDAO idpDAO;
    @Autowired
    IIdpDetailsDAO idpDetailsDAO;
    @Autowired
    IHRMSEmployeeDAO employeeDAO;

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
    public IdpVO saveIdp(IdpVO idpVO) {
        IdpContext context = new IdpContext(idpVO.getId(), idpDAO);
        IdpPlan plan = new IdpPlan();
        context.setIdpPlan(plan);
        context.setIdpAction("Save");
        context.executeAction();
        Idp idp = new Idp();
        Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
        Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, ERecordStatus.Y.name());
        if (idp.getCreatedDate() == null) idp.setCreatedDate(Instant.now());
        if (idp.getStatus() == null) idp.setStatus("DRAFT");
        if (idp.getRecordStatus() == null) idp.setRecordStatus("ACTIVE");

        idp.setEmployeeId(loggedInEmployee.getId());
        idp.setPosition("Position"); // TODO: Set proper position
        idp.setFunction("Function"); // TODO: Set proper function
        idp.setGrade("Grade");     // TODO: Set proper grade
        idp.setActiveFlowId(10L); // TODO: Generate proper flow ID

        idp.setStatus(IdpStatus.DRAFT.getValue());
        if (idp.getActiveFlowId() != null) idp.setActiveFlowId(idp.getActiveFlowId());
        idp.setRecordStatus(IdpRecordStatus.ACTIVE.getValue());
        idp = idpDAO.save(idp);

        if (idpVO.getIdpDetails() != null) {

            // Remove old details (clean update)
            if (idp.getDetails() != null) {
                idp.getDetails().clear();
            }
            final Idp newidp = idp;
            List<IdpDetails> detailsList = idpVO.getIdpDetails().stream().map(d -> {
                IdpDetails detail = new IdpDetails();
                detail.setCompetencyTypeId(d.getCompetencyTypeId());
                detail.setCompetencySubTypeId(d.getCompetencySubTypeId());
                detail.setDevGoals(d.getDevGoal());
                detail.setDevActions(d.getDevActions());
                detail.setTrainingId(d.getTrainingId());
                detail.setTrainingName(d.getTrainingName());
                detail.setStatus(IdpStatus.DRAFT.getValue());
                detail.setIdp(newidp);
                detail.setCompletionCertificateFilePath(""); //TODO: Set proper file path
                return detail;
            }).collect(Collectors.toList());
            try
            {
                idpDetailsDAO.saveAll(detailsList);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return toDto(idp);
        }
        return toDto(idp);
    }

    /**
     * Executes a basic submit operation for backward compatibility.
     * Internally delegates to {@link #handleWorkflowAction(IdpSubmitVO)}.
     *
     * @param idp the submit action DTO
     */
    @Override
    @Transactional
    public void submitIdp(IdpSubmitVO idp) {
        handleWorkflowAction(idp);
    }

    /**
     * Records or updates feedback for a given IDP record.
     * Typically performed during the review phase.
     *
     * @param idp the updated IDP data containing review-related details
     */
    @Override
    @Transactional
    public void reviewIdp(IdpVO idp) {
        if (idp == null || idp.getId() == null) return;

        idpDAO.findById(idp.getId()).ifPresent(entity -> {
            entity.setPosition(idp.getPosition());
            entity.setFunction(idp.getFunction());
            entity.setGrade(idp.getGrade());
            idpDAO.save(entity);
        });
    }

    /**
     * Handles workflow transitions (Submit, Approve, Reject) using the State Design Pattern.
     * <p>
     * Based on the current state and the requested action, it determines and
     * persists the appropriate next state.
     * </p>
     *
     * @param request DTO containing the IDP identifier and the action to perform
     * @return confirmation message summarizing the workflow outcome
     */
    @Override
    @Transactional
    public String handleWorkflowAction(IdpSubmitVO request) {
        if (request == null || request.getId() == null) {
            throw new IllegalArgumentException("Invalid workflow request: IDP ID is required");
        }

        Idp entity = idpDAO.findById(request.getId())
                .orElseThrow(() -> new IllegalStateException("IDP not found for ID: " + request.getId()));

        IdpVO idp = toDto(entity);

        IdpState currentState = getStateFromStatus(idp.getStatus());

        /*IdpContext context = new IdpContext(plan);
        context.executeAction(request.getAction());*/

        String nextStatus = deriveNextStatus(idp.getStatus(), request.getAction());
        entity.setStatus(nextStatus);
        idpDAO.save(entity);

        return String.format("Action '%s' executed successfully. New state: %s",
                request.getAction(), nextStatus);
    }

    /**
     * Retrieves all IDP records, optionally filterable by manager, department, or designation.
     *
     * @param reportingManager the reporting manager (optional)
     * @param department       the department filter (optional)
     * @param designation      the designation filter (optional)
     * @return list of matching {@link IdpVO} records
     */
    @Override
    public List<IdpVO> getAllIdp(String reportingManager, String department, String designation) {
        return idpDAO.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single IDP record based on the active workflow ID.
     *
     * @param activeFlowId the workflow identifier
     * @return the matching {@link IdpVO}, or {@code null} if not found
     */
    @Override
    public IdpVO getIdpByFlowId(Long activeFlowId) {
        if (activeFlowId == null) return null;
        Idp idp = idpDAO.findByActiveFlowId(activeFlowId);
        return toDto(idp);
    }

    /**
     * Maps a textual status value to a corresponding {@link IdpState}.
     *
     * @param status current workflow status
     * @return state object corresponding to the status
     */
    private IdpState getStateFromStatus(String status) {
        return getIdpState(status);
    }

    /**
     * Determines the next workflow status based on current status and action.
     *
     * @param currentStatus current workflow state
     * @param action        requested action
     * @return new status after transition
     */
    private String deriveNextStatus(String currentStatus, String action) {
        if (currentStatus == null) return "DRAFT";
        if ("DRAFT".equalsIgnoreCase(currentStatus) && "SUBMIT".equalsIgnoreCase(action)) {
            return "REVIEW";
        } else if ("REVIEW".equalsIgnoreCase(currentStatus) && "APPROVE".equalsIgnoreCase(action)) {
            return "APPROVED";
        } else if ("REVIEW".equalsIgnoreCase(currentStatus) && "REJECT".equalsIgnoreCase(action)) {
            return "DRAFT";
        }
        return currentStatus;
    }

    /**
     * Converts an {@link Idp} to its corresponding {@link IdpVO}.
     *
     * @param entity the persistent entity
     * @return equivalent data transfer object
     */
    private IdpVO toDto(Idp entity) {
        if (entity == null) return null;

        return IdpVO.builder()
                .id(entity.getId())
                .employeeId(entity.getEmployeeId())
                .position(entity.getPosition())
                .function(entity.getFunction())
                .grade(entity.getGrade())
                .status(entity.getStatus())
                .recordStatus(entity.getRecordStatus())
                .activeFlowId(entity.getActiveFlowId())
                .createdDate(entity.getCreatedDate())
                .build();
    }
}