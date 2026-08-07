package io.github.pigaut.rpg.module.stat.config;

import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.module.stat.modifier.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.module.stat.modifier.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.node.line.*;
import org.jetbrains.annotations.*;

public class StatModifierLoader implements ConfigLoader<StatModifier> {

    @Override
    public @NotNull StatModifier loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
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
        return new StatModifier(amount, operation);
    }

}
