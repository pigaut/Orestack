package io.github.pigaut.rpg.module.sound;

import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.plugin.manager.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public interface SoundEffect extends Identifiable {

    void play(@Nullable Player player, @NotNull Location location);

}
