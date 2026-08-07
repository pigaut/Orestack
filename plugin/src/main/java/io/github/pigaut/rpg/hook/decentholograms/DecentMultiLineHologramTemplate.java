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

import java.util.*;

public class DecentMultiLineHologramTemplate implements HologramTemplate {

    private final EnhancedPlugin plugin;
    private final List<String> lines;
    private final int update;
    private final int viewDistance;

    public DecentMultiLineHologramTemplate(@NotNull EnhancedPlugin plugin, @NotNull List<String> lines,
                                           int update, int viewDistance) {
        this.plugin = plugin;
        this.lines = lines;
        this.update = update;
        this.viewDistance = viewDistance;
    }

    @Override
    public @Nullable Hologram spawn(@NotNull Location location, @NotNull Rotation rotation, @NotNull Context context) {
        LocationUtil.setDefaultWorldIfMissing(location);

        return new DecentHologram(plugin, location, lines.size(), viewDistance, update) {
            @Override
            public void update(eu.decentsoftware.holograms.api.holograms.Hologram decentHologram) {
                for (int i = 0; i < lines.size(); i++) {
                    String parsedLine = PlaceholderUtil.parseAll(context, lines.get(i));
                    DHAPI.setHologramLine(decentHologram, i, parsedLine);
                }
            }
        };
    }

}
