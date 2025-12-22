package com.vinsys.hrms.idp.workflow.action;

/**
 * Abstract base class representing an executable workflow action
 * within the IDP State Machine.
 * <p>
 * Each concrete subclass (e.g., {@link SubmitForReviewAction},
 * {@link ApprovePlanAction}, {@link RejectPlanAction})
 * must define a user-friendly name and the specific business logic
 * inside the {@link #execute()} method.
 * </p>
 */
public abstract class IdpAction {

    /**
     * Returns a human-readable name for this action.
     * Example: "Submit for Review", "Approve Plan", etc.
     *
     * @return the display name of the action
     */
    public abstract String getName();

    /**
     * Executes the core business logic of the action.
     * Called by the current {@link com.vinsys.hrms.idp.workflow.state.IdpState}.
     */
    public abstract void execute();
}