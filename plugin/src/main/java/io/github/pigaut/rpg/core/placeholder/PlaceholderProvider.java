package io.github.pigaut.rpg.core.placeholder;

import org.jetbrains.annotations.*;

@FunctionalInterface
public interface PlaceholderProvider {

    void register(@NotNull PlaceholderRegistry placeholders);

}
