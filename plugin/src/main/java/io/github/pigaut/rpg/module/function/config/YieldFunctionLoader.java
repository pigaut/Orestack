package io.github.pigaut.rpg.module.function.config;

import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.action.system.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.yield.*;
import io.github.pigaut.rpg.module.function.execute.*;
import io.github.pigaut.rpg.module.function.foreach.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.node.scalar.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class YieldFunctionLoader<C extends Function & YieldFunction<R>, R> implements ConfigLoader<C> {

    private final EnhancedPlugin plugin;
    private final Class<C> functionType;
    private final Class<R> returnType;
    private final java.util.function.Function<Function, C> wrapper;

    public YieldFunctionLoader(EnhancedPlugin plugin, Class<C> functionType, Class<R> returnType,
                               java.util.function.Function<Function, C> wrapper) {
        this.plugin = plugin;
        this.functionType = functionType;
        this.returnType = returnType;
        this.wrapper = wrapper;
    }

    private C wrap(@NotNull Function function) {
        return wrapper.apply(function);
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid function";
    }

    @Override
    public @NotNull C loadFromScalar(@NotNull ConfigScalar scalar) throws InvalidConfigException {
        R value = scalar.getRequired(returnType);
        return wrap(new SimpleFunction(new YieldAction(value)));
    }

    @Override
    public @NotNull C loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        C defaultEvaluation = wrap(new SimpleFunction(Action.EMPTY));
        if (section.isEmpty()) {
            return defaultEvaluation;
        }

        Function function;
        if (section.isSet("for|for-each|for each|for-every|for every")) {
            function = new ForEachFunction(
                    section.getRequired("for|for-each|for each|for-every|for every", ForEachSource.class),
                    section.get(Function.class).withDefault(Function.EMPTY)
            );
        }
        else if (section.isSet("if|condition|conditions")) {
            function = new ConditionalFunction(
                    section.getRequired("if|condition|conditions", Condition.class),
                    section.get("then|do|return|yield", functionType).withDefault(defaultEvaluation),
                    section.get("else|or|or-else|or else", functionType).withDefault(defaultEvaluation)
            );
        }
        else if (section.isSet("if-not|if not")) {
            function = new ConditionalFunction(
                    section.getRequired("if-not|if not", NegativeCondition.class),
                    section.get("then|do|return|yield", functionType).withDefault(defaultEvaluation),
                    section.get("else|or|or-else|or else", functionType).withDefault(defaultEvaluation)
            );
        }
        else if (section.isSet("if-any|if any")) {
            function = new ConditionalFunction(
                    section.getRequired("if-any|if any", DisjunctiveCondition.class),
                    section.get("then|do|return|yield", functionType).withDefault(defaultEvaluation),
                    section.get("else|or|or-else|or else", functionType).withDefault(defaultEvaluation)
            );
        }
        else if (section.isSet("return|yield")) {
            R value = section.getRequired("return|yield", returnType);
            function = new SimpleFunction(new YieldAction(value));
        }
        else if (section.isSet("switch")) {
            String conditionName = section.getRequiredString("switch");
            ConfigLoader<? extends Condition> conditionLoader = plugin.getCondition(conditionName);
            if (conditionLoader == null) {
                throw new InvalidConfigException(section, "switch", "Could not find condition with name: " + conditionName);
            }

            List<SwitchCase> cases = new ArrayList<>();
            ConfigSection casesSection = section.getRequiredSection("cases");
            for (String key : casesSection.getKeys()) {
                ConfigScalar scalar = new RootScalar(plugin.getConfigurator());
                scalar.setValue(conditionName + " " + key);
                Condition condition = conditionLoader.loadFromScalar(scalar);

                Function caseFunction = casesSection.getRequired(key, functionType);
                cases.add(new SwitchCase(condition, caseFunction));
            }

            Function defaultCase = section.get("default", functionType).withDefault(null);
            function = new SwitchFunction(cases, defaultCase);
        }
        else {
            throw new InvalidConfigException(section, "Function doesn't contain any valid statement");
        }

        return wrap(function);
    }

    @Override
    public @NotNull C loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        List<C> functions = sequence.getAllRequired(functionType);
        if (functions.isEmpty()) {
            return wrap(Function.EMPTY);
        }

        if (functions.size() == 1) {
            return functions.get(0);
        }

        return wrap(new MultiFunction(new ArrayList<>(functions)));
    }

}