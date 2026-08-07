package io.github.pigaut.rpg.hook.decentholograms;

import eu.decentsoftware.holograms.api.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class DecentItemHologramTemplate implements HologramTemplate {

    private static final int LINES = 1;
    private static final int UPDATE_INTERVAL = 0;

    private final EnhancedPlugin plugin;
    private final ItemStack item;
    private final int viewDistance;

    public DecentItemHologramTemplate(EnhancedPlugin plugin, ItemStack item, int viewDistance) {
        this.plugin = plugin;
        this.item = item;
        this.viewDistance = viewDistance;
    }

    @Override
    public @Nullable Hologram spawn(@NotNull Location location, @NotNull Rotation rotation, @NotNull Context context) {
        LocationUtil.setDefaultWorldIfMissing(location);

        return new DecentHologram(plugin, location, LINES, viewDistance, UPDATE_INTERVAL) {
            @Override
            public void update(eu.decentsoftware.holograms.api.holograms.Hologram decentHologram) {
                DHAPI.setHologramLine(decentHologram, 0, item);
            }
        };
    }

}
