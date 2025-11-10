package com.vinsys.hrms.idp.workflow.state;

import com.vinsys.hrms.idp.workflow.action.ApprovePlanAction;
import com.vinsys.hrms.idp.workflow.action.RejectPlanAction;
import com.vinsys.hrms.idp.workflow.state.IdpState;

/**
 * Represents the REVIEW state in the IDP workflow.
 * <p>
 * From this state, the IDP can either be:
 * <ul>
 *     <li>Approved → transitions to {@link ApprovedState}</li>
 *     <li>Rejected → transitions back to {@link DraftState}</li>
 * </ul>
 * </p>
 */
public class ReviewState extends IdpState {

    @Override
    protected void initializeAllowedActions() {
        allowTransition(ApprovedState.class, ApprovePlanAction.class);
        allowTransition(DraftState.class, RejectPlanAction.class);
    }

    @Override
    protected void doPrework() {
        System.out.println("[ReviewState] Preparing review decision...");
    }

    @Override
    protected void doWork() {
        System.out.println("[ReviewState] Processing review action...");
    }

    @Override
    protected void doPostWork() {
        System.out.println("[ReviewState] Review action completed. Transition applied.");
    }
}