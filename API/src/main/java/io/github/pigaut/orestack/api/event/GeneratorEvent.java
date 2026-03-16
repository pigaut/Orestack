package io.github.pigaut.orestack.api.event;

import org.bukkit.event.*;
import org.jetbrains.annotations.*;

public abstract class GeneratorEvent extends Event implements Cancellable {

    private final String generator;
    private final int stage;
    private boolean cancelled = false;

    protected GeneratorEvent(String generator, int stage) {
        this.generator = generator;
        this.stage = stage;
    }

    /**
     * Returns the name of the generator that triggered the event.
     *
     * @return the name of the generator
     */
    public @NotNull String getGenerator() {
        return generator;
    }

    /**
     * Returns the current stage of the generator that triggered the event.
     *
     * @return the current stage of the generator
     */
    public int getGeneratorStage() {
        return stage;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
