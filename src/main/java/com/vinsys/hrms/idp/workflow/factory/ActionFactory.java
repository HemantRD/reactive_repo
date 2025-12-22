package com.vinsys.hrms.idp.workflow.factory;

import com.vinsys.hrms.idp.workflow.action.*;

public class ActionFactory {

    public static IdpAction getIdpActionFromString(String action) {
        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null");
        }

        switch (action.toUpperCase()) {
            case "SUBMIT":
                return new SubmitForReviewAction();
            case "APPROVE":
                return new ApprovePlanAction();
            case "REJECT":
                return new RejectPlanAction();
            case "SAVE":
                return new SavePlanAction();
            default:
                throw new IllegalArgumentException("Invalid IDP action: " + action);
        }
    }
}