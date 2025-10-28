package io.github.pigaut.orestack.util;

public class GeneratorOverlapException extends GeneratorCreateException {

    public GeneratorOverlapException() {
        super("Failed to create generator. Reason: location is already occupied by another generator.");
    }

    public GeneratorOverlapException(String world, int x, int y, int z) {
        super(world, x, y, z, "location is already occupied by another generator");
    }

}
