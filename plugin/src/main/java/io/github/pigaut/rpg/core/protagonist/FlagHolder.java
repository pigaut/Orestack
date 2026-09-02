package io.github.pigaut.rpg.core.protagonist;

import io.github.pigaut.yaml.delay.Delay;
import org.jetbrains.annotations.*;

import java.util.*;

public interface FlagHolder {

    void addFlag(@NotNull String flag);

    void addTemporaryFlag(@NotNull String flag, @NotNull Delay duration);

    void removeFlag(@NotNull String flag);

    boolean hasFlag(@NotNull String flag);

    @NotNull
    Collection<String> getFlags();

}
