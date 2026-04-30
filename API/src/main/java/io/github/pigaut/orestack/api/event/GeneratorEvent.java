package io.github.pigaut.orestack.api.event;

import org.bukkit.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

public abstract class GeneratorEvent extends Event implements Cancellable {

    private final Location origin;
    private final String generator;
    private final int phase;
    private boolean cancelled = false;

    protected GeneratorEvent(Location origin, String generator, int phase) {
        this.origin = origin;
        this.generator = generator;
        this.phase = phase;
    }

    /**
     * Gets the origin location of the generator.
     * <p>
     * The origin represents the center where the generator is placed in the world.
     *
     * @return the {@link Location} of the generator's origin
     */
    public @NotNull Location getOrigin() {
        return origin;
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
     * Deprecated use GeneratorEvent#getGeneratorPhase
     */
    @Deprecated(forRemoval = true)
    public int getGeneratorStage() {
        return phase;
    }

    /**
     * Returns the current phase of the generator that triggered the event.
     *
     * @return the current phase of the generator
     */
    public int getGeneratorPhase() {
        return phase;
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
