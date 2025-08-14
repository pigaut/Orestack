package io.github.pigaut.orestack.util;

public class GeneratorOverlapException extends Exception {

    public GeneratorOverlapException() {
        super("Cannot create generator because it overlaps with another.");
    }

}
