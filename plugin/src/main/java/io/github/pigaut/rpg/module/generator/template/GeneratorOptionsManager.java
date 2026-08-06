package io.github.pigaut.rpg.module.generator.template;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.module.generator.*;
import io.github.pigaut.rpg.module.generator.*;
import io.github.pigaut.rpg.module.structure.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.module.generator.*;
import io.github.pigaut.rpg.module.structure.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.node.scalar.*;
import io.github.pigaut.yaml.node.section.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorOptionsManager extends Manager implements ConfigBacked {

    private final RpgMakerPlugin plugin;

    private List<GeneratorTemplate> veinGenerators;
    private Map<GeneratorTemplate, StructureTemplate> virtualGeneratorsBarrierLayouts;

    public GeneratorOptionsManager(RpgMakerPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public @NotNull ErrorCollector loadConfigurationData() {
        RootSection config = plugin.getConfiguration();

        veinGenerators = config.getList("vein-generators", GeneratorTemplate.class)
                .withDefault(List.of());

        virtualGeneratorsBarrierLayouts = new HashMap<>();
        for (KeyedScalar scalar : config.getSectionOrCreate("per-player-generators-barrier-layouts").getNestedScalars()) {
            GeneratorTemplate generatorTemplate = scalar.getKeyAs(GeneratorTemplate.class)
                    .withDefault(null);

            StructureTemplate barrierLayout;
            if (scalar.equalsIgnoreCase("none")) {
                barrierLayout = StructureTemplate.createEmpty(plugin);
            } else {
                barrierLayout = scalar.get(StructureTemplate.class)
                        .withDefault(null);
            }

            if (generatorTemplate != null && barrierLayout != null) {
                virtualGeneratorsBarrierLayouts.put(generatorTemplate, barrierLayout);
            }
        }

        return config;
    }

    public boolean isVeinGenerator(@NotNull Generator generator) {
        return veinGenerators.contains(generator.getTemplate());
    }

    public @Nullable StructureTemplate getVirtualGeneratorBarrierLayout(@NotNull GeneratorTemplate generatorTemplate) {
        return virtualGeneratorsBarrierLayouts.get(generatorTemplate);
    }

}
