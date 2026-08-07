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
import org.jetbrains.annotations.*;

public class FancyBlockHologramTemplate implements HologramTemplate {

    private final HologramStyle style;
    private final Material material;

    public FancyBlockHologramTemplate(@NotNull HologramStyle style, @NotNull Material material) {
        this.style = style;
        this.material = material;
    }

    @Override
    public @Nullable Hologram spawn(@NotNull Location location, @NotNull Rotation rotation, @NotNull Context context) {
        return new FancyBlockHologram(style, location, material);
    }

}
