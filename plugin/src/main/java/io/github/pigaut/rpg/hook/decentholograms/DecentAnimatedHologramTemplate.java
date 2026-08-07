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

public class DecentAnimatedHologramTemplate implements HologramTemplate {

    private final EnhancedPlugin plugin;
    private final List<String> frames;
    private final int update;
    private final int intervals;
    private final int viewDistance;

    public DecentAnimatedHologramTemplate(EnhancedPlugin plugin, List<String> frames, int update, int viewDistance) {
        this.plugin = plugin;
        this.frames = frames;
        this.update = update;
        this.intervals = frames.size() - 1;
        this.viewDistance = viewDistance;
        if (intervals < 1) {
            throw new IllegalArgumentException("Animated hologram needs at least two frames");
        }
    }

    @Override
    public @Nullable Hologram spawn(@NotNull Location location, @NotNull Rotation rotation, @NotNull Context context) {
        LocationUtil.setDefaultWorldIfMissing(location);

        return new DecentHologram(plugin, location, 1, viewDistance, update) {
            private int currentFrame = 0;

            @Override
            public void update(eu.decentsoftware.holograms.api.holograms.Hologram decentHologram) {
                String parsedText = PlaceholderUtil.parseAll(context, frames.get(currentFrame));
                DHAPI.setHologramLine(decentHologram, 0, parsedText);
                currentFrame = currentFrame >= intervals ? 0 : currentFrame + 1;
            }
        };
    }

}
