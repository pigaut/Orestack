package io.github.pigaut.rpg.module.function.config;

import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.execute.*;
import io.github.pigaut.rpg.module.function.foreach.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.delay.*;
import io.github.pigaut.yaml.node.*;
import io.github.pigaut.yaml.node.scalar.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FunctionLoader implements ConfigLoader<Function> {

    private final EnhancedPlugin plugin;

    public FunctionLoader(@NotNull EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getErrorDescription() {
        return "invalid function";
    }

    @Override
    public @NotNull Function loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String functionName = scalar.toString(CaseStyle.SNAKE);

        Function function = plugin.getGlobalFunction(functionName);
        if (function != null) {
            return function;
        }

        Set<String> functionNames = plugin.getGlobalFunctions().getAllExistingNames();
        if (functionNames.contains(functionName)) {
            return new LazyFunction(plugin, functionName);
        }

        String actionName = scalar.toString().split(" ")[0];
        ConfigLoader<? extends Action> actionLoader = plugin.getAction(actionName);
        if (actionLoader != null) {
            return new SimpleFunction(actionLoader.loadFromScalar(scalar));
        }

        throw new InvalidConfigException(scalar, "Could not find function/action with name: " + CaseFormatter.toCamelCase(actionName));
    }

    @Override
    public @NotNull Function loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        if (section.isEmpty()) {
            return Function.EMPTY;
        }

        if (section.isSet("for|for-each|for-every")) {
            ForEachSource<?> forEachSource = section.getRequired("for|for-each|for-every", ForEachSource.class);
            return new ForEachFunction(forEachSource, loadFunction(section));
        }

        return loadFunction(section);
    }

    private @NotNull Function loadFunction(@NotNull ConfigSection section) throws InvalidConfigException {
        Function function;
        if (section.isSet("if")) {
            function = new ConditionalFunction(
                    section.getRequired("if", Condition.class),
                    section.get("then|do|do-this", Function.class).withDefault(Function.EMPTY),
                    section.get("else|or|or-else", Function.class).withDefault(Function.EMPTY)
            );
        }
        else if (section.isSet("if-not")) {
            function = new ConditionalFunction(
                    section.getRequired("if-not", NotCondition.class),
                    section.get("then|do|do-this", Function.class).withDefault(Function.EMPTY),
                    section.get("else|or|or-else", Function.class).withDefault(Function.EMPTY)
            );
        }
        else if (section.isSet("if-any")) {
            function = new ConditionalFunction(
                    section.getRequired("if-any", OrCondition.class),
                    section.get("then|do|do-this", Function.class).withDefault(Function.EMPTY),
                    section.get("else|or|or-else", Function.class).withDefault(Function.EMPTY)
            );
        }
        else if (section.isSet("if-none")) {
            function = new ConditionalFunction(
                    section.getRequired("if-none", NorCondition.class),
                    section.get("then|do|do-this", Function.class).withDefault(Function.EMPTY),
                    section.get("else|or|or-else", Function.class).withDefault(Function.EMPTY)
            );
        }
        else if (section.isSet("do|do-this|action|actions")) {
            function = new SimpleFunction(
                    section.getRequired("do|do-this|action|actions", Action.class));
        }
        else if (section.isSet("switch")) {
            String conditionName = section.getRequiredString("switch");
            ConfigLoader<? extends Condition> conditionLoader = plugin.getCondition(conditionName);
            if (conditionLoader == null) {
                throw new InvalidConfigException(section, "switch", "Could not find condition with name: " + conditionName);
            }

            List<SwitchCase> cases = new ArrayList<>();
            for (KeyedField caseSection : section.getRequiredSection("cases").getNestedFields()) {
                String key = caseSection.getKey();

                ConfigScalar scalar = new RootScalar(plugin.getConfigurator());
                scalar.setValue(conditionName + " " + key);

                Condition condition = conditionLoader.loadFromScalar(scalar);
                Function caseFunction = caseSection.get(Function.class)
                        .withDefault(null);

                cases.add(new SwitchCase(condition, caseFunction));
            }

            Function defaultCase = section.get("default", Function.class)
                    .withDefault(null);

            function = new SwitchFunction(cases, defaultCase);
        }
        else {
            throw new InvalidConfigException(section, "Function doesn't contain any valid statement");
        }

        Integer repetitions = section.getInteger("repeat|repetitions")
                .require(Requirements.positive())
                .withDefault(null);

        Integer interval = section.get("interval|period", Delay.class)
                .check(repetitions != null, "repetitions must be set to use interval delay")
                .mapIfValid(Delay::toTicks)
                .withDefault(null);

        if (interval != null) {
            function = new PeriodicFunction(plugin, function, interval, repetitions);
        }
        else if (repetitions != null) {
            function = new RepeatedFunction(function, repetitions);
        }

        Integer delay = section.get("delay", Delay.class)
                .mapIfValid(Delay::toTicks)
                .withDefault(null);

        if (delay != null) {
            function = new DelayedFunction(plugin, function, delay);
        }

        Double chance = section.getDouble("chance")
                .require(Requirements.between(0, 1))
                .withDefault(null);

        if (chance != null) {
            function = new ChanceFunction(function, chance);
        }

        return function;
    }

    @Override
    public @NotNull Function loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        List<Function> functions = sequence.getAllRequired(Function.class);
        if (functions.isEmpty()) {
            return Function.EMPTY;
        }
        if (functions.size() == 1) {
            return functions.get(0);
        }
        return new MultiFunction(sequence.getAllRequired(Function.class));
    }

}

