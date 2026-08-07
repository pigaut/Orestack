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
import org.jetbrains.annotations.*;

public class DecentBlockHologramTemplate implements HologramTemplate {

    private static final int LINES = 1;
    private static final int UPDATE = 0;

    private final EnhancedPlugin plugin;
    private final Material material;
    private final int viewDistance;

    public DecentBlockHologramTemplate(@NotNull EnhancedPlugin plugin, @NotNull Material material,
                                       int viewDistance) {
        this.plugin = plugin;
        this.material = material;
        this.viewDistance = viewDistance;
    }

    @Override
    public @Nullable Hologram spawn(@NotNull Location location, @NotNull Rotation rotation, @NotNull Context context) {
        LocationUtil.setDefaultWorldIfMissing(location);

        return new DecentHologram(plugin, location, LINES, viewDistance, UPDATE) {
            @Override
            public void update(eu.decentsoftware.holograms.api.holograms.Hologram decentHologram) {
                DHAPI.setHologramLine(decentHologram, 0, material);
            }
        };
    }

}
