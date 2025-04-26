package com.demo.app.domain.model.account;


/**
 * The enum Account status.
 */
public enum AccountStatus {
    /**
     * Created account status.
     */
    CREATED {
        @Override
        public boolean canTransitionTo(AccountStatus target) {
            return target == ACTIVATED || target == DEACTIVATED;
        }
    },
    /**
     * Active account status.
     */
    ACTIVATED {
        @Override
        public boolean canTransitionTo(AccountStatus target) {
            return target == DEACTIVATED;
        }
    },
    /**
     * Deactivated account status.
     */
    DEACTIVATED {
        @Override
        public boolean canTransitionTo(AccountStatus target) {
            return target == ACTIVATED;
        }
    };

    /**
     * Can transition to boolean.
     *
     * @param target the target
     * @return the boolean
     */
    public abstract boolean canTransitionTo(AccountStatus target);
}