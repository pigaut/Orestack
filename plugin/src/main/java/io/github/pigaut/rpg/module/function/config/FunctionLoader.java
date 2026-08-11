package io.github.pigaut.rpg.module.function.config;

import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.execute.*;
import io.github.pigaut.rpg.module.function.foreach.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.execute.*;
import io.github.pigaut.rpg.module.function.foreach.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.util.*;
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

    public FunctionLoader(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getErrorDescription() {
        return "invalid function";
    }

    @Override
    public @NotNull Function loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String functionName = scalar.toString(CaseStyle.SNAKE);

        Function function = plugin.getFunction(functionName);
        if (function != null) {
            return function;
        }

        DispatchableAction action = scalar.get(DispatchableAction.class).orElse(null);
        if (action != null) {
            return new SimpleFunction(action);
        }

        Set<String> functionNames = plugin.getFunctions().getAllExistingNames();
        if (functionNames.contains(functionName)) {
            return new LazyFunction(plugin, functionName);
        }

        throw new InvalidConfigException(scalar, "Could not find function/action with name: " + CaseFormatter.toCamelCase(functionName.split(" ")[0]));
    }

    @Override
    public @NotNull Function loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        if (section.isEmpty()) {
            return Function.EMPTY;
        }

        String name = StringUtil.randomName();
        String group = null;
        if (!section.isRoot() && section.getParent() instanceof ConfigRoot root) {
            name = section.getKey();
            group = Group.byFunctionFile(root.getFile());
        }

        if (section.isSet("for|for-each|for each|for-every|for every")) {
            return new ForEachFunction(name, group,
                    section.getRequired("for|for-each|for each|for-every|for every", ForEachSource.class),
                    loadFunction(section, name, group));
        }

        return loadFunction(section, name, group);
    }

    private @NotNull Function loadFunction(@NotNull ConfigSection section, @NotNull String name,
                                           @Nullable String group) throws InvalidConfigException {
        Function function;
        if (section.isSet("if|condition|conditions")) {
            function = new ConditionalFunction(name, group,
                    section.getRequired("if|condition|conditions", Condition.class),
                    section.get("then|do|do-this|do this", Function.class).withDefault(Function.EMPTY),
                    section.get("else|or|or-else|or else", Function.class).withDefault(Function.EMPTY)
            );
        }
        else if (section.isSet("if-not|if not")) {
            function = new ConditionalFunction(name, group,
                    section.getRequired("if-not|if not", NegativeCondition.class),
                    section.get("then|do|do-this|do this", Function.class).withDefault(Function.EMPTY),
                    section.get("else|or|or-else|or else", Function.class).withDefault(Function.EMPTY)
            );
        }
        else if (section.isSet("if-any|if any")) {
            function = new ConditionalFunction(name, group,
                    section.getRequired("if-any|if any", DisjunctiveCondition.class),
                    section.get("then|do|do-this|do this", Function.class).withDefault(Function.EMPTY),
                    section.get("else|or|or-else|or else", Function.class).withDefault(Function.EMPTY)
            );
        }
        else if (section.isSet("do|action|actions")) {
            function = new SimpleFunction(name, group,
                    section.getRequired("do|action|actions", DispatchableAction.class));
        }
        else if (section.isSet("switch")) {
            String conditionName = section.getRequiredString("switch");
            ConfigLoader<? extends Condition> conditionLoader = plugin.getConditionLoader(conditionName);
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

            function = new SwitchFunction(name, group, cases.toArray(new SwitchCase[0]), defaultCase);
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
        String functionName = StringUtil.randomName();
        String functionGroup = null;
        if (!sequence.isRoot() && sequence.getParent() instanceof ConfigRoot root) {
            functionName = sequence.getKey();
            if (root.hasFile()) {
                functionGroup = Group.byFunctionFile(root.getFile());
            }
        }

        List<Function> functions = sequence.getAllRequired(Function.class);
        if (functions.isEmpty()) {
            return Function.EMPTY;
        }
        if (functions.size() == 1) {
            return functions.get(0);
        }
        return new MultiFunction(functionName, functionGroup, sequence.getAllRequired(Function.class));
    }

}

