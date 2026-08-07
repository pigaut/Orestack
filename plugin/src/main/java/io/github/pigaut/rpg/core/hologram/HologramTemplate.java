package io.github.pigaut.rpg.core.hologram;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.transform.Rotation;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public interface HologramTemplate {

    @Nullable
    Hologram spawn(@NotNull Location location, @NotNull Rotation rotation, @NotNull Context context);

    @Nullable
    default Hologram spawn(@NotNull Location location, @NotNull Context context) {
        return spawn(location, Rotation.NONE, context);
    }

}
