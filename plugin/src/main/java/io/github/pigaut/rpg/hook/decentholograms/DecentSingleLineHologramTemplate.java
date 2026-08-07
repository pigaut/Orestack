package io.github.pigaut.rpg.hook.decentholograms;

import eu.decentsoftware.holograms.api.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class DecentSingleLineHologramTemplate implements HologramTemplate {

    private static final int LINES = 1;

    private final EnhancedPlugin plugin;
    private final String line;
    private final int updateInterval;
    private final int viewDistance;

    public DecentSingleLineHologramTemplate(EnhancedPlugin plugin, String line) {
        this(plugin, line, 0, 48);
    }

    public DecentSingleLineHologramTemplate(EnhancedPlugin plugin, String line, int updateInterval, int viewDistance) {
        this.plugin = plugin;
        this.line = line;
        this.updateInterval = updateInterval;
        this.viewDistance = viewDistance;
    }

    @Override
    public @Nullable Hologram spawn(@NotNull Location location, @NotNull Rotation rotation, @NotNull Context context) {
        LocationUtil.setDefaultWorldIfMissing(location);

        return new DecentHologram(plugin, location, LINES, viewDistance, updateInterval) {
            @Override
            public void update(eu.decentsoftware.holograms.api.holograms.Hologram decentHologram) {
                String parsedText = PlaceholderUtil.parseAll(context, line);
                DHAPI.setHologramLine(decentHologram, 0, parsedText);
            }
        };
    }

}
