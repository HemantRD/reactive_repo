package com.vinsys.hrms.idp.workflow.context;

import com.vinsys.hrms.idp.entity.Idp;
import com.vinsys.hrms.idp.workflow.action.IdpAction;
import com.vinsys.hrms.idp.workflow.factory.ActionFactory;
import com.vinsys.hrms.idp.workflow.plan.IdpPlan;
import com.vinsys.hrms.idp.workflow.state.ApprovedState;
import com.vinsys.hrms.idp.workflow.state.DraftState;
import com.vinsys.hrms.idp.workflow.state.IdpState;
import com.vinsys.hrms.idp.workflow.state.ReviewState;
import com.vinsys.hrms.idp.dao.IIdpDAO;
import lombok.Getter;
import lombok.Setter;

public class IdpContext {

    private Long planId;

    @Getter
    @Setter
    private Idp idp;

    private IdpAction idpAction;
    private IdpState idpState;

    @Getter
    @Setter
    private IdpPlan idpPlan;

    public IdpContext(Long planId, IIdpDAO idpDao) {
        this.planId = planId;

        if (planId != null) {
            this.idp = idpDao.findById(planId).orElse(null);

            if (this.idp != null) {
                this.idpState = initializeStateFromPlan(this.idp.getStatus());
            } else {
                this.idpState = new DraftState();
            }
        } else {
            this.idpState = new DraftState();
        }
    }

    private IdpState initializeStateFromPlan(String status) {
        return getIdpState(status);
    }

    public static IdpState getIdpState(String status) {
        if (status == null) return new DraftState();

        switch (status.toUpperCase()) {
            case "REVIEW":
                return new ReviewState();
            case "APPROVED":
                return new ApprovedState();
            default:
                return new DraftState();
        }
    }

    public void setIdpAction(String actionName) {
        this.idpAction = ActionFactory.getIdpActionFromString(actionName);
    }

    /**
     * Executes workflow action using IdpPlan rules + IdpState behavior.
     */
    public void executeAction() {
        if (idpPlan == null) {
            throw new IllegalStateException("IdpPlan must be set before executing action.");
        }
        if (idpAction == null) {
            throw new IllegalStateException("IdpAction is not set.");
        }

        String currentStatus = (idp == null || idp.getStatus() == null) ? "DRAFT" : idp.getStatus();
        String actionName = idpAction.getName();

        // ✔ Validate action using IdpPlan
        if (!idpPlan.isActionAllowed(currentStatus, actionName)) {
            throw new IllegalStateException(
                    "Action '" + actionName + "' is NOT allowed in state '" + currentStatus + "'"
            );
        }

        // ✔ Execute behavior defined in State class
        idpState.doAction(idpAction);

        // ✔ Get next status from IdpPlan
        String nextStatus = idpPlan.getNextStatus(currentStatus, actionName);

        // ✔ Update IDP entity’s status
        if(idp!=null)
        idp.setStatus(nextStatus);

        // ✔ Update runtime state object
        this.idpState = getIdpState(nextStatus);
    }
}