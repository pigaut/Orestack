package io.github.pigaut.orestack.options;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorOptionsManager extends Manager implements ConfigBacked {

    private final OrestackPlugin plugin;

    public GeneratorOptionsManager(OrestackPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public @NotNull List<ConfigurationException> loadConfigurationData() {
        return List.of();
    }

}
