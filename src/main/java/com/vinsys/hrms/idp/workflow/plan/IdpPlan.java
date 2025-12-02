package com.vinsys.hrms.idp.workflow.plan;

import java.util.Map;
import java.util.Set;

public class IdpPlan {

    // Workflow rules: Allowed actions per status
    private final Map<String, Set<String>> workflowRules = Map.of(
            "DRAFT", Set.of("SAVE", "SUBMIT"),
            "REVIEW", Set.of("APPROVE", "REJECT", "SAVE"),
            "APPROVED", Set.of() // no actions allowed
    );

    /**
     * Checks if an action is allowed in the current workflow status.
     */
    public boolean isActionAllowed(String currentStatus, String action) {
        if (currentStatus == null || action == null) return false;

        Set<String> allowed = workflowRules.get(currentStatus.toUpperCase());
        return allowed != null && allowed.contains(action.toUpperCase());
    }

    /**
     * Determines the next workflow status based on current status and action.
     * Java 11 compatible.
     */
    public String getNextStatus(String currentStatus, String action) {

        if (currentStatus == null) {
            return "DRAFT";
        }

        String status = currentStatus.toUpperCase();
        String act = action.toUpperCase();

        if (status.equals("DRAFT")) {
            if (act.equals("SUBMIT")) {
                return "REVIEW";
            }
            return "DRAFT"; // SAVE or others remain in DRAFT
        }

        if (status.equals("REVIEW")) {
            if (act.equals("APPROVE")) {
                return "APPROVED";
            }
            if (act.equals("REJECT")) {
                return "DRAFT";
            }
            return "REVIEW"; // SAVE keeps it in REVIEW
        }

        // APPROVED or unknown
        return status;
    }
}