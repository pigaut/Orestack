package io.github.pigaut.rpg.module.stat.config;

import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.module.stat.modifier.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.module.stat.modifier.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.node.line.*;
import org.jetbrains.annotations.*;

public class LeveledStatModifierLoader implements ConfigLoader<LeveledStatModifier> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid stat modifier";
    }

    @Override
    public @NotNull LeveledStatModifier loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        ConfigLine line = scalar.toLine(LineStyle.SPACED);

        StatOperation operation;
        boolean offsetByOne = false;

        String token = line.getRequiredString(0);
        if (token.startsWith("+") || token.startsWith("-")) {
            if (token.endsWith("%")) {
                operation = StatOperation.SCALE;
            } else {
                operation = StatOperation.ADD;
            }
        }
        else if (token.startsWith("*")) {
            operation = StatOperation.MULTIPLY;
        }
        else {
            operation = line.getRequired(0, StatOperation.class);
            offsetByOne = true;
        }

        double amount = line.getRequiredDouble(offsetByOne ? 1 : 0);

        line.getString(offsetByOne ? 2 : 1).require(s -> s.equalsIgnoreCase("every"),
                "Expected format: <amount> every <level> level(s)");

        int levelInterval = line.getRequiredInteger(offsetByOne ? 3 : 2);

        line.getString(offsetByOne ? 4 : 3).require(s -> StringUtil.isAnyEqualIgnoreCase(s, "level", "levels"),
                "Expected format: <amount> every <level> level(s)");

        return new LeveledStatModifier(amount, levelInterval, operation);
    }

}
