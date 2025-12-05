package io.github.pigaut.orestack.generator.template;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorOptionsManager extends Manager implements ConfigBacked {

    private final OrestackPlugin plugin;

    private List<GeneratorTemplate> veinGenerators;

    public GeneratorOptionsManager(OrestackPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public @NotNull List<ConfigurationException> loadConfigurationData() {
        List<ConfigurationException> errorsFound = new ArrayList<>();

        ConfigSection config = plugin.getConfiguration();

        veinGenerators = config.getElements("vein-generators", GeneratorTemplate.class)
                .withDefault(List.of(), errorsFound::add);

        return errorsFound;
    }

    public boolean isVeinGenerator(@NotNull Generator generator) {
        return veinGenerators.contains(generator.getTemplate());
    }

}
