package io.github.pigaut.orestack.generator.template;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.data.structure.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.node.scalar.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorOptionsManager extends Manager implements ConfigBacked {

    private final OrestackPlugin plugin;

    private List<GeneratorTemplate> veinGenerators;
    private Map<GeneratorTemplate, StructureTemplate> virtualGeneratorsBarrierLayouts;

    public GeneratorOptionsManager(OrestackPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public @NotNull List<ConfigException> loadConfigurationData() {
        List<ConfigException> errors = new ArrayList<>();

        ConfigSection config = plugin.getConfiguration();

        veinGenerators = config.getList("vein-generators", GeneratorTemplate.class)
                .withDefaultOrElse(List.of(), errors::add);

        virtualGeneratorsBarrierLayouts = new HashMap<>();
        for (KeyedScalar scalar : config.getSectionOrCreate("per-player-generators-barrier-layouts").getNestedScalars()) {
            GeneratorTemplate generatorTemplate = scalar.getKeyAs(GeneratorTemplate.class)
                    .withDefaultOrElse(null, errors::add);

            StructureTemplate barrierLayout;
            if (scalar.equalsIgnoreCase("none")) {
                barrierLayout = StructureTemplate.createEmpty(plugin);
            } else {
                barrierLayout = scalar.get(StructureTemplate.class)
                        .withDefaultOrElse(null, errors::add);
            }

            if (generatorTemplate != null && barrierLayout != null) {
                virtualGeneratorsBarrierLayouts.put(generatorTemplate, barrierLayout);
            }
        }

        return errors;
    }

    public boolean isVeinGenerator(@NotNull Generator generator) {
        return veinGenerators.contains(generator.getTemplate());
    }

    public @Nullable StructureTemplate getVirtualGeneratorBarrierLayout(@NotNull GeneratorTemplate generatorTemplate) {
        return virtualGeneratorsBarrierLayouts.get(generatorTemplate);
    }

}
