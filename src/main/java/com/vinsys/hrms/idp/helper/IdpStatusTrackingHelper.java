package com.vinsys.hrms.idp.helper;

import com.vinsys.hrms.idp.dao.IIdpProgressHistoryDAO;
import com.vinsys.hrms.idp.dao.IMapEmployeeIdpStatusDAO;
import com.vinsys.hrms.idp.dao.IMapEmployeeIdpStatusHistoryDAO;
import com.vinsys.hrms.idp.entity.*;
import com.vinsys.hrms.idp.enumconstant.IdpRecordStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Helper class for managing IDP status tracking across multiple tables
 * - IdpProgressHistory: Tracks progress on individual IDP details
 * - MapEmployeeIdpStatus: Current status mapping for employees
 * - MapEmployeeIdpStatusHistory: Historical status changes
 */
@Component
public class IdpStatusTrackingHelper {

    @Autowired
    private IIdpProgressHistoryDAO idpProgressHistoryDAO;

    @Autowired
    private IMapEmployeeIdpStatusDAO mapEmployeeIdpStatusDAO;

    @Autowired
    private IMapEmployeeIdpStatusHistoryDAO mapEmployeeIdpStatusHistoryDAO;

    /**
     * Create initial progress tracking entries when IDP is saved
     * Status: Pending (0%)
     */
    public void createInitialProgressTracking(Idp idp, IdpDetails idpDetail) {
        IdpProgressHistory progress = IdpProgressHistory.builder()
                .idp(idp)
                .idpDetail(idpDetail)
                .employeeId(idp.getEmployeeId())
                .logDate(LocalDate.now())
                .progressValue(0)
                .progressUnit("%")
                .status("Pending")
                .build();

        idpProgressHistoryDAO.save(progress);
    }

    /**
     * Update progress when employee starts working on IDP detail
     * Status: In Progress (10%)
     */
    public void updateProgressToInProgress(Idp idp, IdpDetails idpDetail) {
        IdpProgressHistory progress = IdpProgressHistory.builder()
                .idp(idp)
                .idpDetail(idpDetail)
                .employeeId(idp.getEmployeeId())
                .logDate(LocalDate.now())
                .progressValue(10)
                .progressUnit("%")
                .status("In Progress")
                .build();

        idpProgressHistoryDAO.save(progress);
    }

    /**
     * Update progress when IDP detail is completed
     * Status: Completed (100%)
     */
    public void updateProgressToCompleted(Idp idp, IdpDetails idpDetail) {
        IdpProgressHistory progress = IdpProgressHistory.builder()
                .idp(idp)
                .idpDetail(idpDetail)
                .employeeId(idp.getEmployeeId())
                .logDate(LocalDate.now())
                .progressValue(100)
                .progressUnit("%")
                .status("Completed")
                .build();

        idpProgressHistoryDAO.save(progress);
    }

    /**
     * Create or update employee IDP status mapping
     * Called when IDP status changes (Draft, Submitted, Approved, etc.)
     */
    public void updateEmployeeIdpStatusMapping(Long employeeId, String status, String updatedBy) {
        // Find or create employee IDP status
        MapEmployeeIdpStatus employeeStatus = mapEmployeeIdpStatusDAO
                .findByEmployeeId(employeeId)
                .orElse(MapEmployeeIdpStatus.builder()
                        .employeeId(employeeId)
                        .recordStatus(IdpRecordStatus.ACTIVE.getValue())
                        .build());

        employeeStatus.setUpdatedOn(Instant.now());
        employeeStatus.setUpdatedBy(updatedBy);

        // Save the mapping
        MapEmployeeIdpStatus savedStatus = mapEmployeeIdpStatusDAO.save(employeeStatus);

        // Create history entry
        createEmployeeIdpStatusHistory(savedStatus, employeeId, status, updatedBy);
    }

    /**
     * Create history entry for employee IDP status change
     */
    private void createEmployeeIdpStatusHistory(MapEmployeeIdpStatus employeeStatus,
                                               Long employeeId,
                                               String status,
                                               String updatedBy) {
        MapEmployeeIdpStatusHistory history = MapEmployeeIdpStatusHistory.builder()
                .employeeIdpStatus(employeeStatus)
                .employeeId(employeeId)
                .recordStatus(status)
                .updatedOn(Instant.now())
                .updatedBy(updatedBy)
                .build();

        mapEmployeeIdpStatusHistoryDAO.save(history);
    }

    /**
     * Comprehensive status update - called on major IDP actions
     * Updates both employee status mapping and creates history
     */
    public void trackIdpStatusChange(Idp idp, String newStatus, Long updatedByEmployeeId) {
        String updatedBy = updatedByEmployeeId != null ? updatedByEmployeeId.toString() : "SYSTEM";

        // Update employee IDP status mapping and history
        updateEmployeeIdpStatusMapping(idp.getEmployeeId(), newStatus, updatedBy);
    }

    /**
     * Track IDP submission - marks all details as "In Progress"
     */
    public void trackIdpSubmission(Idp idp) {
        // Mark all IDP details as "In Progress"
        if (idp.getDetails() != null) {
            for (IdpDetails detail : idp.getDetails()) {
                updateProgressToInProgress(idp, detail);
            }
        }

        // Update employee status mapping
        trackIdpStatusChange(idp, "Submitted", idp.getEmployeeId());
    }

    /**
     * Track IDP approval - employee can start working on training
     */
    public void trackIdpApproval(Idp idp, Long approvedByEmployeeId) {
        // Update employee status mapping
        trackIdpStatusChange(idp, "Approved", approvedByEmployeeId);
    }

    /**
     * Track IDP rejection - back to draft
     */
    public void trackIdpRejection(Idp idp, Long rejectedByEmployeeId) {
        // Update employee status mapping
        trackIdpStatusChange(idp, "Rejected", rejectedByEmployeeId);
    }

    /**
     * Track individual detail completion (when employee completes training)
     */
    public void trackDetailCompletion(Idp idp, IdpDetails idpDetail, Long employeeId) {
        // Mark detail as completed
        updateProgressToCompleted(idp, idpDetail);

        // Check if all details are completed
        boolean allCompleted = checkIfAllDetailsCompleted(idp);
        if (allCompleted) {
            trackIdpStatusChange(idp, "Completed", employeeId);
        }
    }

    /**
     * Check if all IDP details are completed
     */
    private boolean checkIfAllDetailsCompleted(Idp idp) {
        if (idp.getDetails() == null || idp.getDetails().isEmpty()) {
            return false;
        }

        // In a real implementation, query the latest progress for each detail
        // For now, simplified logic
        return false;
    }
}
