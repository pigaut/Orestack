package io.github.pigaut.rpg.module.function.condition.registry;

import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.plugin.registry.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.format.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ConditionRegistry extends AbstractRegistry<ConfigLoader<Condition>> implements ConfigLoader<Condition> {

    @Override
    public @NotNull String getErrorDescription() {
        return "invalid condition";
    }

    @Override
    public @NotNull Condition loadFromScalar(@NotNull ConfigScalar scalar) throws InvalidConfigException {
        ConfigLine line = scalar.toLine();
        String conditionName = line.getRequiredString(0);

        ConfigLoader<? extends Condition> loader = get(conditionName);
        if (loader == null) {
            throw new InvalidConfigException(line,
                    "Could not find condition with name: " + CaseFormatter.toCamelCase(conditionName));
        }

        return loader.loadFromScalar(scalar);
    }

    @Override
    public @NotNull Condition loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        if (section.isSet("if")) {
            return section.getRequired("if", Condition.class);
        }
        else if (section.isSet("if-not")) {
            return section.getRequired("if-not", NotCondition.class);
        }
        else if (section.isSet("if-any")) {
            return section.getRequired("if-any", OrCondition.class);
        }
        else if (section.isSet("if-none")) {
            return section.getRequired("if-none", NorCondition.class);
        }
        else {
            throw new InvalidConfigException(section, "Could not find any valid condition");
        }
    }

    @Override
    public @NotNull Condition loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        List<Condition> conditions = sequence.getAllRequired(Condition.class);
        if (conditions.isEmpty()) {
            return Condition.MET;
        }
        if (conditions.size() == 1) {
            return conditions.get(0);
        }
        return new AndCondition(conditions);
    }

}
