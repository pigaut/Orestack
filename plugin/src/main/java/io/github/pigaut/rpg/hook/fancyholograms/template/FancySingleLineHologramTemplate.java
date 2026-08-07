package io.github.pigaut.rpg.hook.fancyholograms.template;

import de.oliver.fancyholograms.api.data.*;
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

public class FancySingleLineHologramTemplate implements HologramTemplate {

    private final EnhancedPlugin plugin;
    private final HologramStyle style;
    private final String line;

    public FancySingleLineHologramTemplate(@NotNull EnhancedPlugin plugin, @NotNull HologramStyle style, @NotNull String line) {
        this.plugin = plugin;
        this.style = style;
        this.line = line;
    }

    @Override
    public @Nullable Hologram spawn(@NotNull Location location, @NotNull Rotation rotation, @NotNull Context context) {
        return new FancyTextHologram(plugin, style, location, 0.2) {
            @Override
            protected void update(de.oliver.fancyholograms.api.hologram.@NotNull Hologram fancyHologram) {
                TextHologramData hologramData = (TextHologramData) fancyHologram.getData();
                String parsedText = PlaceholderUtil.parseAll(context, line);
                hologramData.setText(List.of(parsedText));
            }
        };
    }

}
