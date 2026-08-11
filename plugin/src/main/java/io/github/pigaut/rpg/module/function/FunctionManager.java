package io.github.pigaut.rpg.module.function;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.manager.module.Module;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.convert.format.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

public class FunctionManager extends ConfigBackedManager<Function> {

    public FunctionManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, Module.FUNCTIONS, Function.class);
    }

    public @NotNull Set<String> getAllExistingNames() {
        Set<String> names = new HashSet<>();
        for (File file : plugin.getFiles("functions")) {
            ConfigSection functionConfig = YamlConfig.loadSectionOrEmpty(file);
            for (String key : functionConfig.getKeys()) {
                names.add(CaseFormatter.toSnakeCase(key));
            }
        }
        return names;
    }

}
