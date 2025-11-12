package com.vinsys.hrms.idp.workflow.context;

import com.vinsys.hrms.idp.workflow.action.IdpAction;
import com.vinsys.hrms.idp.workflow.factory.ActionFactory;
import com.vinsys.hrms.idp.workflow.plan.IdpPlan;

public class IdpContext {

    private final IdpPlan plan;

    public IdpContext(IdpPlan plan) {
        this.plan = plan;
    }

    public void executeAction(String actionName) {
        IdpAction idpAction = ActionFactory.getIdpActionFromString(actionName);
        this.plan.doAction(idpAction);
    }
}
