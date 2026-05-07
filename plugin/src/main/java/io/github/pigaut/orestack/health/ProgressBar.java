package io.github.pigaut.orestack.health;

import org.jetbrains.annotations.*;

import java.util.*;

public class ProgressBar {

    private final String id;
    private final Map<Integer, String> barByHealthPercentage;

    public ProgressBar(@NotNull String id, Map<Integer, String> barByProgress) {
        this.id = id;
        this.barByHealthPercentage = barByProgress;
    }

    public @NotNull String getId() {
        return id;
    }

    public @Nullable String getBarByProgress(int progress) {
        return barByHealthPercentage.get(progress);
    }

}
