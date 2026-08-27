package io.github.pigaut.rpg.module.function.config;

import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.execute.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class GlobalFunctionLoader implements ConfigLoader.Any<GlobalFunction> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid function";
    }

    @Override
    public @NotNull GlobalFunction loadFromField(@NotNull ConfigField field) throws InvalidConfigException {
        String name = !field.isRoot() ? field.getKey() : StringUtil.generateRandomName();
        String group = Group.byFunctionFile(field.getRoot().getFile());
        Function function = field.getRequired(Function.class);
        return new GlobalFunction(name, group, function);
    }

}
