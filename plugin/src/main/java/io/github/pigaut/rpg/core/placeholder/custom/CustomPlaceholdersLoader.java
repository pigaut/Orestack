package io.github.pigaut.rpg.core.placeholder.custom;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.node.line.*;
import io.github.pigaut.yaml.node.scalar.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CustomPlaceholdersLoader implements ConfigLoader.Line<CustomPlaceholders> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid custom placeholders";
    }

    @Override
    public @NotNull LineStyle getLineStyle() {
        return LineStyle.SPACED;
    }

    @Override
    public @NotNull CustomPlaceholders loadFromLine(ConfigLine line) throws InvalidConfigException {
        Map<String, Object> placeholders = new HashMap<>();
        line.getAllFlags().forEach((key, scalar) -> {
            String id = CaseFormatter.toSnakeCase(key);
            Object value = scalar.getValue();
            if (!(value instanceof Number)) {
                value = scalar.toString(ColorUtil.FORMATTER);
            }
            placeholders.put(id, value);
        });

        return new CustomPlaceholders(placeholders);
    }

    @Override
    public @NotNull CustomPlaceholders loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        Map<String, Object> placeholders = new HashMap<>();
        for (KeyedScalar scalar : section.getNestedScalars()) {
            String id = CaseFormatter.toSnakeCase(scalar.getKey());
            Object value = scalar.getValue();
            if (!(value instanceof Number)) {
                value = scalar.toString(ColorUtil.FORMATTER);
            }
            placeholders.put(id, value);
        }
        return new CustomPlaceholders(placeholders);
    }

}
