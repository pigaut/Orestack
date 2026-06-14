package io.github.pigaut.orestack.gate.exception;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.plugin.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class GateConflictException extends GateCreateException {

    private static final EnhancedPlugin PLUGIN = OrestackPlugin.getInstance();
    private static final String TRANSLATION_ID = "gate-conflict";

    public GateConflictException(Location location) {
        this(LocationUtil.getWorldOrDefault(location).toString(),
                location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public GateConflictException(String world, int x, int y, int z) {
        super(world, x, y, z, PLUGIN.getTranslation(TRANSLATION_ID));
    }

    public @NotNull String getMessage() {
        return PLUGIN.getTranslation(TRANSLATION_ID);
    }

}
