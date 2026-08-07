package io.github.pigaut.rpg.hook.fancyholograms.template;

import de.oliver.fancyholograms.api.data.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.core.hologram.style.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.core.hologram.style.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.hook.fancyholograms.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.hook.fancyholograms.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FancyAnimatedHologramTemplate implements HologramTemplate {

    private final EnhancedPlugin plugin;
    private final HologramStyle style;
    private final List<String> frames;
    private final int intervals;

    public FancyAnimatedHologramTemplate(@NotNull EnhancedPlugin plugin, @NotNull HologramStyle style, @NotNull List<String> frames) {
        this.plugin = plugin;
        this.style = style;
        this.frames = frames;
        this.intervals = frames.size() - 1;
        if (intervals < 1) {
            throw new IllegalArgumentException("Animated hologram needs at least two frames");
        }
    }

    @Override
    public @Nullable Hologram spawn(@NotNull Location location, @NotNull Rotation rotation, @NotNull Context context) {
        return new FancyTextHologram(plugin, style, location, 0.2) {
            private int currentFrame = 0;

            @Override
            public void update(de.oliver.fancyholograms.api.hologram.@NotNull Hologram fancyHologram) {
                String parsedText = PlaceholderUtil.parseAll(context, frames.get(currentFrame));
                TextHologramData hologramData = (TextHologramData) fancyHologram.getData();
                hologramData.setText(List.of(parsedText));
                currentFrame = currentFrame >= intervals ? 0 : currentFrame + 1;
            }

        };
    }

}
