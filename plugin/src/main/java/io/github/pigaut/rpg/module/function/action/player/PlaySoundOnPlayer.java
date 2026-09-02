package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.module.sound.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class PlaySoundOnPlayer implements PlayerAction.Executor {

    private final SoundEffect sound;

    public PlaySoundOnPlayer(@NotNull SoundEffect sound) {
        this.sound = sound;
    }

    @Override
    public void execute(@NotNull Player player) {
        sound.play(player, player.getLocation());
    }

}
