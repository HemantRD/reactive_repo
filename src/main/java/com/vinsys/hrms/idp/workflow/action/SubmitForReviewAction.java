package com.vinsys.hrms.idp.workflow.action;

/**
 * Represents the workflow action that submits an IDP from
 * the {@code DRAFT} state to the {@code REVIEW} state.
 * <p>
 * This action is typically triggered when an employee
 * completes their self-assessment and sends the IDP
 * for managerial review.
 */
public class SubmitForReviewAction extends IdpAction {

    @Override
    public String getName() {
        return "SUBMIT";
    }

    /**
     * Executes the submit-for-review action.
     * In a real-world scenario, this could include:
     * - Validation logic
     * - Notification triggers
     * - Audit logging
     */
    @Override
    public void execute() {
        System.out.println("[Action] Submitting IDP for review...");
    }
}