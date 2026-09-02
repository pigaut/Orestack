package io.github.pigaut.rpg.module.function.action.protagonist;

import io.github.pigaut.rpg.module.function.action.mob.*;
import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.module.sound.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class PlaySoundAtProtagonist implements ProtagonistAction.Executor {

    private final SoundEffect sound;

    public PlaySoundAtProtagonist(SoundEffect sound) {
        this.sound = sound;
    }

    @Override
    public void execute(@NotNull LivingEntity protagonist) {
        Location location = protagonist.getLocation();
        sound.play(protagonist instanceof Player player ? player : null, location);
    }

}