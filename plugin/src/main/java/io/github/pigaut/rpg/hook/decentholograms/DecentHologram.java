package io.github.pigaut.rpg.hook.decentholograms;

import eu.decentsoftware.holograms.api.*;
import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.task.*;
import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.task.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class DecentHologram implements Hologram {

    protected final EnhancedPlugin plugin;
    private final eu.decentsoftware.holograms.api.holograms.Hologram hologram;
    protected @Nullable Task updateTask;

    protected DecentHologram(@NotNull EnhancedPlugin plugin, @NotNull Location location, int lines, int viewDistance, int update) {
        this.plugin = plugin;
        this.hologram = DHAPI.createHologram(UUID.randomUUID().toString(), location, Collections.nCopies(lines, ""));
        this.updateTask = update > 0 ? plugin.getScheduler().runTaskTimer(update, () -> {
            if (exists()) {
                update();
            }
            else if (updateTask != null) {
                updateTask.cancel();
            }
        }) : null;
        update();
        hologram.setDisplayRange(viewDistance);
        hologram.showAll();
    }

    @Override
    public void update() {
        if (!exists()) {
            throw new IllegalStateException("Hologram doesn't exist in the world.");
        }
        update(hologram);
    }

    public abstract void update(eu.decentsoftware.holograms.api.holograms.Hologram decentHologram);

    @Override
    public boolean exists() {
        return hologram.isEnabled();
    }

    @Override
    public void remove() {
        if (hologram.isEnabled()) {
            hologram.destroy();
        }
        if (updateTask != null) {
            if (!updateTask.isCancelled()) {
                updateTask.cancel();
            }
            updateTask = null;
        }
    }

}
