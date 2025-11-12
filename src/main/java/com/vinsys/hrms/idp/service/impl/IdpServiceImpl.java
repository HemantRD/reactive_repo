package com.vinsys.hrms.idp.service.impl;

import com.vinsys.hrms.idp.dao.IIdpRepository;
import com.vinsys.hrms.idp.dto.IdpDto;
import com.vinsys.hrms.idp.dto.IdpSubmitDto;
import com.vinsys.hrms.idp.entity.IdpEntity;
import com.vinsys.hrms.idp.service.IdpService;
import com.vinsys.hrms.idp.workflow.context.IdpContext;
import com.vinsys.hrms.idp.workflow.plan.IdpPlan;
import com.vinsys.hrms.idp.workflow.state.ApprovedState;
import com.vinsys.hrms.idp.workflow.state.DraftState;
import com.vinsys.hrms.idp.workflow.state.IdpState;
import com.vinsys.hrms.idp.workflow.state.ReviewState;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link IdpService} responsible for managing the lifecycle
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
public class IdpServiceImpl implements IdpService {

    private final IIdpRepository idpRepository;

    public IdpServiceImpl(IIdpRepository idpRepository) {
        this.idpRepository = idpRepository;
    }

    /**
     * Creates or updates an IDP record.
     * <p>
     * - If {@code id} is null → creates a new DRAFT record. <br>
     * - If {@code id} exists → updates existing record fields.
     * </p>
     *
     * @param idp the IDP data transfer object containing employee and plan details
     * @return the persisted {@link IdpDto} after creation or update
     */
    @Override
    @Transactional
    public IdpDto saveIdp(IdpDto idp) {
        IdpEntity entity = (idp.getId() == null)
                ? new IdpEntity()
                : idpRepository.findById(idp.getId()).orElse(new IdpEntity());

        if (entity.getCreatedDate() == null) entity.setCreatedDate(Instant.now());
        if (entity.getStatus() == null) entity.setStatus("DRAFT");
        if (entity.getRecordStatus() == null) entity.setRecordStatus("ACTIVE");

        entity.setEmployeeId(idp.getEmployeeId());
        entity.setPosition(idp.getPosition());
        entity.setFunction(idp.getFunction());
        entity.setGrade(idp.getGrade());

        if (idp.getStatus() != null) entity.setStatus(idp.getStatus());
        if (idp.getActiveFlowId() != null) entity.setActiveFlowId(idp.getActiveFlowId());
        if (idp.getRecordStatus() != null) entity.setRecordStatus(idp.getRecordStatus());

        IdpEntity saved = idpRepository.save(entity);

        // Ensure workflow linkage is initialized
        if (saved.getActiveFlowId() == null) {
            saved.setActiveFlowId(saved.getId());
            saved = idpRepository.save(saved);
        }

        return toDto(saved);
    }

    /**
     * Executes a basic submit operation for backward compatibility.
     * Internally delegates to {@link #handleWorkflowAction(IdpSubmitDto)}.
     *
     * @param idp the submit action DTO
     */
    @Override
    @Transactional
    public void submitIdp(IdpSubmitDto idp) {
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
    public void reviewIdp(IdpDto idp) {
        if (idp == null || idp.getId() == null) return;

        idpRepository.findById(idp.getId()).ifPresent(entity -> {
            entity.setPosition(idp.getPosition());
            entity.setFunction(idp.getFunction());
            entity.setGrade(idp.getGrade());
            idpRepository.save(entity);
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
    public String handleWorkflowAction(IdpSubmitDto request) {
        if (request == null || request.getId() == null) {
            throw new IllegalArgumentException("Invalid workflow request: IDP ID is required");
        }

        IdpEntity entity = idpRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalStateException("IDP not found for ID: " + request.getId()));

        IdpDto idp = toDto(entity);

        IdpState currentState = getStateFromStatus(idp.getStatus());
        IdpPlan plan = new IdpPlan(currentState);
        IdpContext context = new IdpContext(plan);
        context.executeAction(request.getAction());

        String nextStatus = deriveNextStatus(idp.getStatus(), request.getAction());
        entity.setStatus(nextStatus);
        idpRepository.save(entity);

        return String.format("Action '%s' executed successfully. New state: %s",
                request.getAction(), nextStatus);
    }

    /**
     * Retrieves all IDP records, optionally filterable by manager, department, or designation.
     *
     * @param reportingManager the reporting manager (optional)
     * @param department       the department filter (optional)
     * @param designation      the designation filter (optional)
     * @return list of matching {@link IdpDto} records
     */
    @Override
    public List<IdpDto> getAllIdp(String reportingManager, String department, String designation) {
        return idpRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single IDP record based on the active workflow ID.
     *
     * @param activeFlowId the workflow identifier
     * @return the matching {@link IdpDto}, or {@code null} if not found
     */
    @Override
    public IdpDto getIdpByFlowId(Long activeFlowId) {
        if (activeFlowId == null) return null;
        return idpRepository.findByActiveFlowId(activeFlowId)
                .map(this::toDto)
                .orElse(null);
    }

    /**
     * Maps a textual status value to a corresponding {@link IdpState}.
     *
     * @param status current workflow status
     * @return state object corresponding to the status
     */
    private IdpState getStateFromStatus(String status) {
        if (status == null) return new DraftState();
        switch (status.toUpperCase()) {
            case "REVIEW":
                return new ReviewState();
            case "APPROVED":
                return new ApprovedState();
            default:
                return new DraftState();
        }
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
     * Converts an {@link IdpEntity} to its corresponding {@link IdpDto}.
     *
     * @param entity the persistent entity
     * @return equivalent data transfer object
     */
    private IdpDto toDto(IdpEntity entity) {
        if (entity == null) return null;

        return IdpDto.builder()
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