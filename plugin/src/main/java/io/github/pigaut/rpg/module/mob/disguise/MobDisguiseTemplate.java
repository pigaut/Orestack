package io.github.pigaut.rpg.module.mob.disguise;

public interface MobDisguiseTemplate {

    MobDisguiseTemplate EMPTY = new EmptyMobDisguiseTemplate();

    void applyToNextEntity();

}