package io.github.pigaut.orestack.core;

public class VirtualGeneratorUnsupportedException extends GeneratorCreateException {

    public VirtualGeneratorUnsupportedException() {
        super("Virtual generators require a paper server with PacketEvents installed.");
    }

}
