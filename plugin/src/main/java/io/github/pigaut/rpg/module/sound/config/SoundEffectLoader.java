package io.github.pigaut.rpg.module.sound.config;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.module.sound.impl.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.module.sound.impl.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class SoundEffectLoader implements ConfigLoader<SoundEffect> {

    private final EnhancedPlugin plugin;

    public SoundEffectLoader(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getErrorDescription() {
        return "invalid sound effect";
    }

    @Override
    public @NotNull SoundEffect loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String soundName = scalar.toString(CaseStyle.SNAKE);
        SoundEffect foundSound = plugin.getSound(soundName);
        if (foundSound == null) {
            Sound sound = SoundUtil.getSound(soundName);
            if (sound != null) {
                return new SimpleSound(sound);
            }
            throw new InvalidConfigException(scalar, "Could not find sound effect with name: '" + soundName + "'");
        }

        return foundSound;
    }

    @Override
    public @NotNull SoundEffect loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        String soundName = section.getKey();
        String soundGroup = Group.bySoundFile(section.getRoot().getFile());
        Sound sound = section.getRequired("sound", Sound.class);
        float volume = section.getFloat("volume").withDefault(1.0f);
        float pitch = section.getFloat("pitch").withDefault(1.0f);
        double offsetX = section.getDouble("offset.x").withDefault(0d);
        double offsetY = section.getDouble("offset.y").withDefault(0d);
        double offsetZ = section.getDouble("offset.z").withDefault(0d);
        boolean playerOnly = section.getBoolean("player-only").withDefault(false);

        SoundEffect soundEffect = new SimpleSound(soundName, soundGroup, sound, volume, pitch,
                offsetX, offsetY, offsetZ, playerOnly);

        Integer repetitions = section.getInteger("repeat|repetitions")
                .require(Requirements.positive())
                .withDefault(null);

        Integer interval = section.get("interval|period", Delay.class)
                .check(repetitions != null, "repetitions must be set to use interval delay")
                .mapIfValid(Delay::toTicks)
                .withDefault(null);

        if (interval != null) {
            soundEffect = new PeriodicSound(plugin, soundEffect, interval, repetitions);
        }
        else if (repetitions != null) {
            soundEffect = new RepeatedSound(soundEffect, repetitions);
        }

        Integer delay = section.get("delay", Delay.class)
                .mapIfValid(Delay::toTicks)
                .withDefault(null);

        if (delay != null) {
            soundEffect = new DelayedSound(plugin, soundEffect, delay);
        }

        return soundEffect;
    }

    @Override
    public @NotNull SoundEffect loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        String soundName = sequence.getKey();
        String soundGroup = Group.bySoundFile(sequence.getRoot().getFile());
        return new MultiSound(soundName, soundGroup, sequence, sequence.getAllRequired(SoundEffect.class));
    }

}
