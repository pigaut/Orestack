package io.github.pigaut.rpg.hook.fancyholograms.template;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.core.hologram.style.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.core.hologram.style.*;
import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.hook.fancyholograms.*;
import io.github.pigaut.rpg.hook.fancyholograms.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class FancyItemHologramTemplate implements HologramTemplate {

    private final HologramStyle style;
    private final ItemStack item;

    public FancyItemHologramTemplate(@NotNull HologramStyle style, @NotNull ItemStack item) {
        this.style = style;
        this.item = item;
    }

    @Override
    public @Nullable Hologram spawn(@NotNull Location location, @NotNull Rotation rotation, @NotNull Context context) {
        return new FancyItemHologram(style, location, item);
    }

}
