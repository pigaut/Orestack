package io.github.pigaut.rpg.module.generator.exception;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class GeneratorOverlapException extends GeneratorCreateException {

    private static final EnhancedPlugin PLUGIN = RpgMakerPlugin.getInstance();
    private static final String TRANSLATION_ID = "generator-overlap";

    public GeneratorOverlapException(Location location) {
        this(LocationUtil.getWorldOrDefault(location).toString(),
                location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public GeneratorOverlapException(String world, int x, int y, int z) {
        super(world, x, y, z, PLUGIN.getTranslation(TRANSLATION_ID));
    }

    public @NotNull String getMessage() {
        return PLUGIN.getTranslation(TRANSLATION_ID);
    }

}
