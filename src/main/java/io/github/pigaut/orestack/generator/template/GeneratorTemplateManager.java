package io.github.pigaut.orestack.generator.template;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.node.sequence.*;

import java.io.*;

public class GeneratorTemplateManager extends ManagerContainer<GeneratorTemplate> {

    private final OrestackPlugin plugin;

    public GeneratorTemplateManager(OrestackPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public void disable() {
        this.clear();
    }

    @Override
    public void loadData() {
        for (File generatorFile : plugin.getFiles("generators")) {
            final RootSequence config = plugin.loadConfigSequence(generatorFile);
            this.add(config.load(GeneratorTemplate.class));
        }
    }

}
