package io.github.pigaut.rpg.plugin.manager.config;

import io.github.pigaut.yaml.*;
import org.jetbrains.annotations.*;

import java.util.*;

@FunctionalInterface
public interface ConfigBacked {

    @NotNull
    ErrorCollector loadConfigurationData();

}
