package io.github.pigaut.orestack.event;

import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.stage.*;
import io.github.pigaut.voxel.event.*;
import org.bukkit.block.*;

public abstract class GeneratorEvent extends CancellableEvent {

    private final Block block;
    private final BlockGenerator generator;
    private final GeneratorStage stage;

    protected GeneratorEvent(Block block, BlockGenerator generator, GeneratorStage stage) {
        this.block = block;
        this.generator = generator;
        this.stage = stage;
    }

    public Block getBlock() {
        return block;
    }

    public BlockGenerator getGenerator() {
        return generator;
    }

    public GeneratorStage getGeneratorStage() {
        return stage;
    }

}
