package io.github.pigaut.orestack.generator.template;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.config.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.node.sequence.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class GeneratorTemplateManager extends Manager {

    private final OrestackPlugin plugin;
    private final Map<String, GeneratorTemplate> generatorsByName = new ConcurrentHashMap<>();

    public GeneratorTemplateManager(OrestackPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public List<String> getGeneratorNames() {
        return new ArrayList<>(generatorsByName.keySet());
    }

    public Collection<GeneratorTemplate> getAllGeneratorTemplates() {
        return new ArrayList<>(generatorsByName.values());
    }

    public @Nullable GeneratorTemplate getGeneratorTemplate(String name) {
        return name != null ? generatorsByName.get(name) : null;
    }

    public void registerGenerator(@NotNull String name, @NotNull GeneratorTemplate generator) {
        generatorsByName.put(name, generator);
    }

    @Override
    public void loadData() {
        generatorsByName.clear();
        for (File generatorFile : plugin.getFiles("generators")) {
            final RootSequence config = plugin.loadConfigSequence(generatorFile);
            generatorsByName.put(config.getName(), config.load(GeneratorTemplate.class));
        }
    }

}
