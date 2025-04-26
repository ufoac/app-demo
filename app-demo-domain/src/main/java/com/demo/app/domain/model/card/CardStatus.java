package com.demo.app.domain.model.card;


/**
 * The enum Card status.
 */
public enum CardStatus {
    /**
     * Created card status.
     */
    CREATED {
        @Override
        public boolean canTransitionTo(CardStatus target) {
            return target == ASSIGNED;
        }
    },
    /**
     * Assigned card status.
     */
    ASSIGNED {
        @Override
        public boolean canTransitionTo(CardStatus target) {
            return target == ACTIVATED || target == DEACTIVATED;
        }
    },
    /**
     * Active card status.
     */
    ACTIVATED {
        @Override
        public boolean canTransitionTo(CardStatus target) {
            return target == DEACTIVATED;
        }
    },
    /**
     * Deactivated card status.
     */
    DEACTIVATED {
        @Override
        public boolean canTransitionTo(CardStatus target) {
            return target == ACTIVATED;
        }
    };

    /**
     * Can transition to boolean.
     *
     * @param target the target
     * @return the boolean
     */
    public abstract boolean canTransitionTo(CardStatus target);
}