package io.github.pigaut.rpg.hook.fancyholograms;

import de.oliver.fancyholograms.api.*;
import de.oliver.fancyholograms.api.data.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.core.hologram.style.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.task.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.core.hologram.style.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.task.*;
import io.github.pigaut.rpg.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;
import org.joml.*;

public abstract class FancyTextHologram implements Hologram {

    private final HologramManager hologramManager = FancyHologramsPlugin.get().getHologramManager();

    private final String name;
    private final de.oliver.fancyholograms.api.hologram.Hologram hologram;
    private @Nullable Task updateTask;

    public FancyTextHologram(@NotNull EnhancedPlugin plugin, @NotNull HologramStyle style, @NotNull Location location, double yHeight) {
        this.name = StringUtil.randomName();

        LocationUtil.setDefaultWorldIfMissing(location);
        location.subtract(0, yHeight, 0);

        TextHologramData hologramData = new TextHologramData(name, location);
        hologramData.setPersistent(false);
        hologramData.setVisibilityDistance(style.getViewDistance());
        hologramData.setSeeThrough(style.isSeeThrough());
        hologramData.setTextShadow(style.isShadow());
        hologramData.setBillboard(style.getBillboard());
        hologramData.setTextAlignment(style.getAlignment());
        hologramData.setBrightness(style.getBrightness());
        hologramData.setBackground(style.getBackground());
        hologramData.setScale(new Vector3f(style.getScaleX(), style.getScaleY(), style.getScaleZ()));
        hologramData.setShadowRadius(style.getShadowRadius());
        hologramData.setShadowStrength(style.getShadowStrength());

        int taskTimer = style.getUpdateInterval();
        this.updateTask = taskTimer > 0 ? plugin.getScheduler().runTaskTimer(taskTimer, () -> {
            if (exists()) {
                update();
            } else if (updateTask != null) {
                updateTask.cancel();
            }
        }) : null;

        hologram = hologramManager.create(hologramData);
        hologramManager.addHologram(hologram);

        update(hologram);

        hologram.forceUpdate();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (hologram.isWithinVisibilityDistance(player)) {
                hologram.forceShowHologram(player);
            }
        }
    }

    @Override
    public void update() {
        if (!exists()) {
            throw new IllegalStateException("Hologram doesn't exist in the world.");
        }

        update(hologram);

        hologram.forceUpdate();
        for (Player player : Bukkit.getOnlinePlayers()) {
            hologram.updateShownStateFor(player);
            hologram.refreshHologram(player);
        }
    }

    protected abstract void update(@NotNull de.oliver.fancyholograms.api.hologram.Hologram fancyHologram);

    @Override
    public boolean exists() {
        return hologramManager.getHologram(name).isPresent();
    }

    @Override
    public void remove() {
        try {
            hologramManager.removeHologram(hologram);
        } catch (NullPointerException ignored) {
            // FancyHolograms is disabled. Temporary fix.
        }

        if (updateTask != null) {
            if (!updateTask.isCancelled()) {
                updateTask.cancel();
            }
            updateTask = null;
        }
    }

}
