package io.github.pigaut.rpg.module.gate;

public enum GateTransition {

    NONE,
    OPENING,
    CLOSING;

    public boolean isOpening() {
        return this == OPENING;
    }

    public boolean isClosing() {
        return this == CLOSING;
    }

}
