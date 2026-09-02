package io.github.pigaut.rpg.module.function.response;

public enum ResponseType {

    NONE(false, false),      // Dispatch continues normally
    CONTINUE(false, false),  // Continue to next element in for-each loop, or next function in multi-function
    RETURN(false, false),    // If response comes from a global function continue execution, else stop early
    STOP(true, false),
    ERROR(true, false),
    YIELD(true, false),

    MET(false, true),        // Condition is met
    UNMET(false, true);      // Condition is unmet

    private final boolean stopEarly;
    private final boolean conditional;

    ResponseType(boolean stopEarly, boolean conditional) {
        this.stopEarly = stopEarly;
        this.conditional = conditional;
    }

    public boolean isStopEarly() {
        return stopEarly;
    }

    public boolean isConditional() {
        return conditional;
    }
}
