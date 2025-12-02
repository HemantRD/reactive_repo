package com.vinsys.hrms.idp.workflow.action;

/**
 * Represents the workflow action that approves an IDP plan,
 * transitioning it from the {@code REVIEW} state to the {@code APPROVED} state.
 * <p>
 * Typically executed by a manager or approver after verifying that
 * the plan meets all review criteria.
 */
public class SavePlanAction extends IdpAction {

    @Override
    public String getName() {
        return "SAVE";
    }

    /**
     * Executes the approval logic for the IDP plan.
     * <p>
     * In a full implementation, this could include:
     * <ul>
     *   <li>Logging approval metadata (approver, timestamp)</li>
     *   <li>Triggering email or notification workflows</li>
     *   <li>Persisting the updated state in the database</li>
     * </ul>
     */
    @Override
    public void execute() {
        System.out.println("[Action] Approving IDP plan — transitioning to APPROVED state...");
    }
}