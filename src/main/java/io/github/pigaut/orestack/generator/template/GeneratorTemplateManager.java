package io.github.pigaut.orestack.generator.template;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.node.sequence.*;
import org.jetbrains.annotations.*;

import java.io.*;

public class GeneratorTemplateManager extends ManagerContainer<GeneratorTemplate> {

    private final OrestackPlugin plugin;

    public GeneratorTemplateManager(OrestackPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getFilesDirectory() {
        return "generators";
    }

    @Override
    public void loadFile(@NotNull File file) {
        final RootSequence config = new RootSequence(file, plugin.getConfigurator());
        config.setPrefix("Generator");
        config.load();

        final GeneratorTemplate template = config.load(GeneratorTemplate.class);
        try {
            add(template);
        } catch (DuplicateElementException e) {
            throw new InvalidConfigurationException(config, e.getMessage());
        }
    }

}
