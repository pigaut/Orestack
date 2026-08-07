package io.github.pigaut.rpg.module.function.action.server;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.sound.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class PlaySound implements Action {

    private final SoundEffect sound;
    private final Location location;

    public PlaySound(SoundEffect sound, World world, double x, double y, double z) {
        this.sound = sound;
        this.location = new Location(world, x, y, z);
    }

    @Override
    public void execute(@NotNull Context context) {
        sound.play(context.player(), location);
    }

}
