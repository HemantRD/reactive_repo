package com.vinsys.hrms.idp.workflow.action;

/**
 * Represents the workflow action that rejects an IDP plan during
 * the {@code REVIEW} state, sending it back to the {@code DRAFT} state.
 * <p>
 * Typically triggered by a manager or reviewer when the IDP
 * requires changes or additional information.
 */
public class RejectPlanAction extends IdpAction {

    @Override
    public String getName() {
        return "REJECT";
    }

    /**
     * Executes the reject action logic.
     * <p>
     * In a real-world application, this may include:
     * <ul>
     *   <li>Adding review comments</li>
     *   <li>Sending rejection notifications to the employee</li>
     *   <li>Auditing the rejection reason</li>
     * </ul>
     */
    @Override
    public void execute() {
        System.out.println("[Action] Rejecting IDP plan — moving back to DRAFT state...");
    }
}