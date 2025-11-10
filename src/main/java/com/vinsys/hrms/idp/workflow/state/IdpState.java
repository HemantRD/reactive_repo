package com.vinsys.hrms.idp.workflow.state;

import com.vinsys.hrms.idp.workflow.action.IdpAction;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class representing a generic IDP workflow state.
 * <p>
 * Each concrete subclass (e.g., {@link DraftState}, {@link ReviewState}, {@link ApprovedState})
 * defines its own allowed transitions using {@link #initializeAllowedActions()}.
 * </p>
 */
public abstract class IdpState {

    /**
     * Maps target state → allowed action.
     * Example: allowTransition(ReviewState.class, SubmitForReviewAction.class);
     */
    protected final Map<Class<? extends IdpState>, Class<? extends IdpAction>> allowedActionsOnState = new HashMap<>();

    /** Initializes allowed transitions when the state is constructed. */
    protected IdpState() {
        initializeAllowedActions();
    }

    /** Defines which transitions are allowed from this state. */
    protected abstract void initializeAllowedActions();

    /** Registers a valid workflow transition. */
    @SuppressWarnings("unchecked")
    protected <S extends IdpState, A extends IdpAction> void allowTransition(Class<S> nextState, Class<A> action) {
        allowedActionsOnState.put(
                (Class<? extends IdpState>) nextState,
                (Class<? extends IdpAction>) action
        );
    }

    /** Hook method for pre-work (executed before transition). */
    protected abstract void doPrework();

    /** Hook method for main work (executed during transition). */
    protected abstract void doWork();

    /** Hook method for post-work (executed after transition). */
    protected abstract void doPostWork();

    /** Checks whether a given action is valid in this state. */
    public boolean isValidAction(Class<? extends IdpAction> actionClass) {
        return allowedActionsOnState.containsValue(actionClass);
    }

    /**
     * Executes a given action only if allowed for this state.
     *
     * @param action The action to execute
     */
    public void doAction(IdpAction action) {
        if (!isValidAction(action.getClass())) {
            throw new IllegalStateException(
                    "Action '" + action.getClass().getSimpleName()
                            + "' is not allowed in state: " + this.getClass().getSimpleName()
            );
        }

        doPrework();
        action.execute();
        doWork();
        doPostWork();
    }

    /** Returns allowed transitions from the current state. */
    public Map<Class<? extends IdpState>, Class<? extends IdpAction>> getAllowedActionsOnState() {
        return allowedActionsOnState;
    }
}
