package io.github.pigaut.orestack.core;

public class VirtualGeneratorUnsupportedException extends GeneratorCreateException {

    public VirtualGeneratorUnsupportedException() {
        super("Failed to create generator. Reason: per-player generators require a paper server on 1.19.3+ with PacketEvents installed");
    }

    public VirtualGeneratorUnsupportedException(String world, int x, int y, int z) {
        super(world, x, y, z, "per-player generators require a paper server on 1.19.3+ with PacketEvents installed");
    }

}
