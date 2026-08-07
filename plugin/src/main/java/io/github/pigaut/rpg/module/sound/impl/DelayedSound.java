package io.github.pigaut.rpg.module.sound.impl;

import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class DelayedSound implements SoundEffect {

    private final EnhancedPlugin plugin;
    private final SoundEffect sound;
    private final int delay;

    public DelayedSound(EnhancedPlugin plugin, SoundEffect sound, int delay) {
        this.plugin = plugin;
        this.sound = sound;
        this.delay = delay;
    }

    @Override
    public @NotNull String getName() {
        return sound.getName();
    }

    @Override
    public @Nullable String getGroup() {
        return sound.getGroup();
    }

    @Override
    public void play(@Nullable Player player, @NotNull Location location) {
        plugin.getScheduler().runTaskLater(delay, () -> {
            sound.play(player, location);
        });
    }

}
