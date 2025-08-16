package io.github.pigaut.orestack.util;

public class GeneratorLimitException extends GeneratorCreateException {

    public GeneratorLimitException() {
        super("Failed to create generator. Reason: The free version only allows up to 25 multi block generators.");
    }

    public GeneratorLimitException(String world, int x, int y, int z) {
        super(world, x, y, z, "The free version only allows up to 25 multi block generators");
    }

}
