package io.github.pigaut.orestack.util;

public class GeneratorLimitException extends Exception {

    public GeneratorLimitException() {
        super("The free version only allows up to 25 multi block generators.");
    }

}
