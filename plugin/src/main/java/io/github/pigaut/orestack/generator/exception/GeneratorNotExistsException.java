package io.github.pigaut.orestack.generator.exception;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.plugin.*;
import org.jetbrains.annotations.*;

public class GeneratorNotExistsException extends GeneratorCreateException {

    private static final EnhancedPlugin PLUGIN = OrestackPlugin.getInstance();
    private static final String TRANSLATION_ID = "generator-not-exists";

    public GeneratorNotExistsException(String world, int x, int y, int z) {
        super(world, x, y, z, PLUGIN.getTranslation(TRANSLATION_ID));
    }

    public @NotNull String getMessage() {
        return PLUGIN.getTranslation(TRANSLATION_ID);
    }

}
