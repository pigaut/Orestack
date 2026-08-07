package io.github.pigaut.rpg.module.function.action.block;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.sound.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class PlaySoundAtBlock implements Action {

    private final SoundEffect sound;

    public PlaySoundAtBlock(SoundEffect sound) {
        this.sound = sound;
    }

    @Override
    public void execute(@NotNull Context context) {
        Block block = context.block();
        if (block != null) {
            Player player = context.player();
            sound.play(player, block.getLocation());
        }
    }

}
