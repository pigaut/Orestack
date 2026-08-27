package io.github.pigaut.rpg.module.sound.impl;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.server.Server;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class SimpleSound implements SoundEffect {

    private final String name;
    private final String group;
    private final Sound sound;
    private final float volume;
    private final float pitch;
    private final double offsetX, offsetY, offsetZ;
    private final boolean playerOnly;

    public SimpleSound(@NotNull Sound sound) {
        this(StringUtil.generateRandomName(), null, sound, 1, 1, 0, 0, 0, false);
    }

    public SimpleSound(@NotNull String name, @Nullable String group,
                       @NotNull Sound sound, float volume, float pitch,
                       double offsetX, double offsetY, double offsetZ,
                       boolean playerOnly) {
        this.name = name;
        this.group = group;
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.playerOnly = playerOnly;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @Nullable String getGroup() {
        return group;
    }

    public void play(@Nullable Player player, @NotNull Location location) {
        Location offsetLocation = LocationUtil.getOffsetLocation(location, offsetX, offsetY, offsetZ);

        if (playerOnly) {
            if (player != null) {
                player.playSound(offsetLocation, sound, volume, pitch);
            }
            return;
        }

        World world = location.getWorld();
        if (world == null) {
            world = Server.getDefaultWorld();
        }
        world.playSound(offsetLocation, sound, volume, pitch);
    }

}
