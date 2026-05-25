package io.github.pigaut.orestack.gate.exception;

public class GateOverlapException extends GateCreateException {

    public GateOverlapException() {
        super("Failed to create gate. Reason: location is already occupied by another gate.");
    }

    public GateOverlapException(String world, int x, int y, int z) {
        super(world, x, y, z, "location is already occupied by another gate");
    }

}
