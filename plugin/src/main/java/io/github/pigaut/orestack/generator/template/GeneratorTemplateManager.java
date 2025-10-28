package io.github.pigaut.orestack.generator.template;

import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorTemplateManager extends ConfigBackedManager.Sequence<GeneratorTemplate> {

    public GeneratorTemplateManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, "generators");
    }

    @Override
    public String getPrefix() {
        return "Generator";
    }

    @Override
    public List<String> getLoadAfter() {
        return List.of("ItemsAdder");
    }

    @Override
    public void loadFromSequence(ConfigSequence sequence) throws InvalidConfigurationException {
        GeneratorTemplate template = sequence.loadRequired(GeneratorTemplate.class);
        try {
            add(template);
        }
        catch (DuplicateElementException e) {
            throw new InvalidConfigurationException(sequence, e.getMessage());
        }
    }

}
