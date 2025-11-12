package com.vinsys.hrms.idp.workflow.state;

public class ApprovedState extends IdpState {

    @Override
    protected void initializeAllowedActions() {
        // No further transitions
    }

    @Override
    protected void doPrework() {
        System.out.println("[ApprovedState] Pre-approval tasks done.");
    }

    @Override
    protected void doWork() {
        System.out.println("[ApprovedState] IDP is already approved. No further actions allowed.");
    }

    @Override
    protected void doPostWork() {
        System.out.println("[ApprovedState] Final state reached.");
    }
}
