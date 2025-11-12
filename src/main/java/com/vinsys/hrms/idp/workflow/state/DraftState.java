package com.vinsys.hrms.idp.workflow.state;

import com.vinsys.hrms.idp.workflow.action.SubmitForReviewAction;

/**
 * Represents the DRAFT state in the IDP workflow.
 * <p>
 * From this state, the IDP can be submitted for review.
 * </p>
 */
public class DraftState extends IdpState {

    @Override
    protected void initializeAllowedActions() {
        allowTransition(ReviewState.class, SubmitForReviewAction.class);
    }

    @Override
    protected void doPrework() {
        System.out.println("[DraftState] Preparing to submit for review...");
    }

    @Override
    protected void doWork() {
        System.out.println("[DraftState] Submitting draft for review...");
    }

    @Override
    protected void doPostWork() {
        System.out.println("[DraftState] Submission complete. Transitioning to ReviewState.");
    }
}