package io.github.pigaut.rpg.core.protagonist;

import io.github.pigaut.yaml.delay.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface CooldownHolder {

    void addCooldown(@NotNull String name, @NotNull Delay duration);

    void removeCooldown(@NotNull String name);

    boolean hasCooldown(@NotNull String name);

    long getCooldownRemaining(@NotNull String name);

    @NotNull
    Map<String, Long> getCooldowns();

}
