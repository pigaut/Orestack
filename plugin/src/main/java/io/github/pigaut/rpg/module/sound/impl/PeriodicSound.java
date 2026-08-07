package io.github.pigaut.rpg.module.sound.impl;

import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class PeriodicSound implements SoundEffect {

    private final EnhancedPlugin plugin;
    private final SoundEffect sound;
    private final int interval;
    private final int repetitions;

    public PeriodicSound(EnhancedPlugin plugin, SoundEffect particle, int interval, int repetitions) {
        this.plugin = plugin;
        this.sound = particle;
        this.interval = interval;
        this.repetitions = repetitions;
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
        sound.play(player, location);
        for (int i = 1; i < repetitions + 1; i++) {
            final long delay = (long) interval * i;
            plugin.getScheduler().runTaskLater(delay, () -> sound.play(player, location));
        }
    }

}
