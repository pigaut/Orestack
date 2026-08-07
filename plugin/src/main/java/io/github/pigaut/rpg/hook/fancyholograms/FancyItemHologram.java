package io.github.pigaut.rpg.hook.fancyholograms;

import de.oliver.fancyholograms.api.*;
import de.oliver.fancyholograms.api.data.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.core.hologram.style.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.core.hologram.style.*;
import io.github.pigaut.rpg.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;
import org.joml.*;

public class FancyItemHologram implements Hologram {

    private final HologramManager hologramManager = FancyHologramsPlugin.get().getHologramManager();

    private final String name;
    private final de.oliver.fancyholograms.api.hologram.Hologram hologram;

    public FancyItemHologram(@NotNull HologramStyle style, @NotNull Location location, @NotNull ItemStack item) {
        this.name = StringUtil.randomName();

        LocationUtil.setDefaultWorldIfMissing(location);
        location.subtract(0, 0.2, 0);

        ItemHologramData hologramData = new ItemHologramData(name, location);
        hologramData.setPersistent(false);
        hologramData.setVisibilityDistance(style.getViewDistance());
        hologramData.setBillboard(style.getBillboard());
        hologramData.setBrightness(style.getBrightness());
        hologramData.setScale(new Vector3f(style.getScaleX(), style.getScaleY(), style.getScaleZ()));
        hologramData.setShadowRadius(style.getShadowRadius());
        hologramData.setShadowStrength(style.getShadowStrength());
        hologramData.setItemStack(item);

        hologram = hologramManager.create(hologramData);
        hologramManager.addHologram(hologram);
        hologram.forceUpdate();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (hologram.isWithinVisibilityDistance(player)) {
                hologram.forceShowHologram(player);
            }
        }
    }

    @Override
    public void update() {}

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
    }

}
