package io.github.pigaut.rpg.module.function.response;

// Responses used to determine function behaviour
public enum ResponseType {

    NONE, // Dispatch continues normally
    CONTINUE, // Continue to next element in for each loop or to next function in multi-function
    RETURN, // If response comes from a global function continue execution, else stop early
    STOP(true),
    ERROR(true),
    YIELD(true),

    MET, // Condition is met
    UNMET; // Condition is unmet

    private final boolean stopEarly;

    ResponseType() {
        this(false);
    }

    ResponseType(boolean stopEarly) {
        this.stopEarly = stopEarly;
    }

    public boolean isStopEarly() {
        return stopEarly;
    }

}
