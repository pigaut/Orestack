package io.github.pigaut.rpg.module.mob.disguise;

import me.libraryaddict.disguise.*;
import me.libraryaddict.disguise.disguisetypes.*;
import org.jetbrains.annotations.*;

public final class SimpleMobDisguiseTemplate implements MobDisguiseTemplate {

    private final Disguise disguise;

    public SimpleMobDisguiseTemplate(@NotNull Disguise disguise) {
        this.disguise = disguise;
    }

    @Override
    public void applyToNextEntity() {
        DisguiseAPI.disguiseNextEntity(disguise);
    }

}