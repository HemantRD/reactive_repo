package com.vinsys.hrms.idp.workflow.plan;

import com.vinsys.hrms.idp.workflow.action.IdpAction;
import com.vinsys.hrms.idp.workflow.state.IdpState;

public class IdpPlan {

    private IdpState currentState;

    public IdpPlan(IdpState initialState) {
        this.currentState = initialState;
    }

    public IdpState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(IdpState currentState) {
        this.currentState = currentState;
    }

    public void doAction(IdpAction action) {
        if (currentState.isValidAction(action.getClass())) {
            currentState.doAction(action);
            // Transition logic (optional): update currentState if needed
        } else {
            throw new IllegalStateException(
                    "Action '" + action.getName() + "' not allowed in state " +
                            currentState.getClass().getSimpleName()
            );
        }
    }
}
