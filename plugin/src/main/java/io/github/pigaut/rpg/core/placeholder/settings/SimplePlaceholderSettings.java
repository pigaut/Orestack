package io.github.pigaut.rpg.core.placeholder.settings;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.core.progressbar.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.core.progressbar.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.node.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SimplePlaceholderSettings implements PlaceholderSettings {

    private Object placeholderFallback;
    private Map<String, Object> placeholderFallbacks;
    private List<WildcardFallback> wildcardPlaceholderFallbacks;
    private List<ProgressBar> progressBars;
    private List<ProgressBar> invertedProgressBars;

    private record WildcardFallback(String[] parts, Object value) {}

    public void loadConfigurationData(@NotNull ConfigSection config) {
        placeholderFallback = config.getScalar("placeholder-fallback")
                .mapIfValid(ConfigScalar::getValue)
                .withDefault("");

        if (placeholderFallback instanceof String string) {
            placeholderFallback = ColorUtil.translateColors(string);
        }

        placeholderFallbacks = new HashMap<>();
        wildcardPlaceholderFallbacks = new ArrayList<>();
        for (KeyedField field : config.getSectionOrCreate("custom-placeholder-fallbacks").getNestedFields()) {
            String placeholder = field.getKey();
            Object value = field.getValue();
            if (value instanceof String string) {
                value = ColorUtil.translateColors(string);
            }

            if (value instanceof List<?> list) {
                List<Object> parsedValues = new ArrayList<>();
                for (Object element : list) {
                    if (element instanceof String string) {
                        parsedValues.add(ColorUtil.translateColors(string));
                        continue;
                    }
                    parsedValues.add(element);
                }
                value = parsedValues;
            }

            if (placeholder.indexOf('*') != -1) {
                wildcardPlaceholderFallbacks.add(new WildcardFallback(WildcardMatcher.splitPattern(placeholder), value));
            } else {
                placeholderFallbacks.put(placeholder, value);
            }
        }

        // Progress bar settings
        progressBars = config.getAll("progress-bars", ProgressBar.class)
                .withDefault(List.of());

        invertedProgressBars = new ArrayList<>();
        for (ProgressBar progressBar : progressBars) {
            invertedProgressBars.add(progressBar.inverted());
        }
    }

    public @Nullable Object getPlaceholderFallback(@NotNull String placeholder) {
        Object direct = placeholderFallbacks.get(placeholder);
        if (direct != null) {
            return direct;
        }

        for (WildcardFallback wildcard : wildcardPlaceholderFallbacks) {
            if (WildcardMatcher.tryMatch(wildcard.parts(), placeholder) != null) {
                return wildcard.value();
            }
        }

        return placeholderFallback;
    }

    public void registerPlaceholderFallback(@NotNull String placeholder, @NotNull Object fallback) {
        if (!placeholderFallbacks.containsKey(placeholder)) {
            placeholderFallbacks.put(placeholder, fallback);
        }
    }

    public @NotNull List<ProgressBar> getProgressBars() {
        return new ArrayList<>(progressBars);
    }

    public @NotNull List<ProgressBar> getCountdownBars() {
        return new ArrayList<>(invertedProgressBars);
    }

}
