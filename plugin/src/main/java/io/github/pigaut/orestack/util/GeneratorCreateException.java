package io.github.pigaut.orestack.util;

public class GeneratorCreateException extends Exception {

    public GeneratorCreateException(String message) {
        super(message);
    }

    public GeneratorCreateException(String world, int x, int y, int z, String reason) {
        super(String.format("Failed to create generator at %s, %d, %d, %d. Reason: %s.", world, x, y, z, reason));
    }

}
