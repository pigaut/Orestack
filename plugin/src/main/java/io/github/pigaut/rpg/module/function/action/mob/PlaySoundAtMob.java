package io.github.pigaut.rpg.module.function.action.mob;

import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.module.sound.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class PlaySoundAtMob implements MobAction {

    private final SoundEffect sound;

    public PlaySoundAtMob(SoundEffect sound) {
        this.sound = sound;
    }

    @Override
    public void execute(@NotNull Mob mob) {
        Location location = mob.getLocation();
        if (location != null) {
            sound.play(null, location);
        }
    }

}
