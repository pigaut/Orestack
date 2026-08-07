package io.github.pigaut.rpg.module.sound.impl;

import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.yaml.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MultiSound implements SoundEffect {

    private final String name;
    private final String group;
    private final ConfigSequence sequence;
    private final List<SoundEffect> sounds;

    public MultiSound(@NotNull String name, @Nullable String group, ConfigSequence sequence,
                      @NotNull List<@NotNull SoundEffect> sounds) {
        this.name = name;
        this.group = group;
        this.sequence = sequence;
        this.sounds = sounds;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @Nullable String getGroup() {
        return group;
    }

    @Override
    public void play(@Nullable Player player, @NotNull Location location) {
        for (SoundEffect sound : sounds) {
            sound.play(player, location);
        }
    }

}
