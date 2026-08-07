package io.github.pigaut.rpg.core.placeholder.settings;

import io.github.pigaut.rpg.core.progressbar.*;
import io.github.pigaut.rpg.core.progressbar.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface PlaceholderSettings {

    @Nullable
    Object getPlaceholderFallback(@NotNull String placeholder);

    void registerPlaceholderFallback(@NotNull String placeholder, @NotNull Object fallback);

    @NotNull
    List<ProgressBar> getProgressBars();

    @NotNull
    List<ProgressBar> getCountdownBars();

}
