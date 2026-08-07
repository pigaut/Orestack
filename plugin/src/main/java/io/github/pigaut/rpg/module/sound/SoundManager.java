package io.github.pigaut.rpg.module.sound;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.manager.module.Module;
import org.jetbrains.annotations.*;

public class SoundManager extends ConfigBackedManager<SoundEffect> {

    public SoundManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, Module.SOUNDS, SoundEffect.class);
    }

}
