package io.github.pigaut.rpg.module.gate.exception;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class GateNotExistsException extends GateCreateException {

    private static final EnhancedPlugin PLUGIN = RpgMakerPlugin.getInstance();
    private static final String TRANSLATION_ID = "gate-not-exists";

    public GateNotExistsException(String world, int x, int y, int z) {
        super(world, x, y, z, PLUGIN.getTranslation(TRANSLATION_ID));
    }

    public @NotNull String getMessage() {
        return PLUGIN.getTranslation(TRANSLATION_ID);
    }

}
