package io.github.pigaut.orestack.generator.template;

import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.*;
import org.jetbrains.annotations.*;

public class GeneratorTemplateManager extends ConfigBackedManager.Sequence<GeneratorTemplate> {

    public GeneratorTemplateManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, "Generator", "generators");
    }

    @Override
    public void loadFromSequence(ConfigSequence sequence) throws InvalidConfigurationException {
        final GeneratorTemplate template = sequence.loadRequired(GeneratorTemplate.class);
        try {
            add(template);
        }
        catch (DuplicateElementException e) {
            throw new InvalidConfigurationException(sequence, e.getMessage());
        }
    }

}
