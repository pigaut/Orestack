package io.github.pigaut.rpg.core.protagonist;

import org.jetbrains.annotations.*;

import java.util.*;

public interface FlagHolder {

    boolean hasFlag(@NotNull String flag);

    @NotNull
    Collection<String> getFlags();

    void addFlag(@NotNull String flag);

    void addTemporaryFlag(@NotNull String flag, int ticks);

    void removeFlag(@NotNull String flag);

}
