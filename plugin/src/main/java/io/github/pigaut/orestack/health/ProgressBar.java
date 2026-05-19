package io.github.pigaut.orestack.health;

import org.jetbrains.annotations.*;

import java.util.*;

public class ProgressBar {

    private final String id;
    private final Map<Integer, String> barByProgress;

    public ProgressBar(@NotNull String id, Map<Integer, String> barByProgress) {
        this.id = id;
        this.barByProgress = barByProgress;
    }

    public @NotNull String getId() {
        return id;
    }

    public @Nullable String getBarByProgress(int progress) {
        return barByProgress.get(progress);
    }

    public @NotNull ProgressBar inverted() {
        Map<Integer, String> invertedBarByProgress = new HashMap<>();
        for (int i = 0; i <= 100; i++) {
            String bar = barByProgress.getOrDefault(i, "");
            invertedBarByProgress.put(100 - i, bar);
        }
        return new ProgressBar(id, invertedBarByProgress);
    }

}
