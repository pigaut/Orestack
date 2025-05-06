package io.github.pigaut.orestack.generator.template;

import com.google.common.collect.*;
import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.node.sequence.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class GeneratorTemplateManager extends Manager {

    private final OrestackPlugin plugin;
    private final Map<String, GeneratorTemplate> generatorsByName = new ConcurrentHashMap<>();
    private final Multimap<String, GeneratorTemplate> generatorsByGroup = Multimaps.newListMultimap(new HashMap<>(), ArrayList::new);

    public GeneratorTemplateManager(OrestackPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public List<String> getGeneratorNames() {
        return new ArrayList<>(generatorsByName.keySet());
    }

    public List<String> getGeneratorGroups() {
        return new ArrayList<>(generatorsByGroup.keySet());
    }

    public Collection<GeneratorTemplate> getAllGeneratorTemplates() {
        return new ArrayList<>(generatorsByName.values());
    }

    public @Nullable GeneratorTemplate getGeneratorTemplate(String name) {
        return name != null ? generatorsByName.get(name) : null;
    }

    public @NotNull List<GeneratorTemplate> getGeneratorTemplates(String group) {
        return new ArrayList<>(generatorsByGroup.get(group));
    }

    public void addGenerator(@NotNull GeneratorTemplate generator) {
        generatorsByName.put(generator.getName(), generator);
        final String group = generator.getGroup();
        if (group != null) {
            generatorsByGroup.put(group, generator);
        }
    }

    @Override
    public void disable() {
        generatorsByName.clear();
        generatorsByGroup.clear();
    }

    @Override
    public void loadData() {
        for (File generatorFile : plugin.getFiles("generators")) {
            final RootSequence config = plugin.loadConfigSequence(generatorFile);
            this.addGenerator(config.load(GeneratorTemplate.class));
        }
    }

}
