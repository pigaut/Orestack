package io.github.pigaut.orestack.structure;

import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.voxel.yaml.*;
import io.github.pigaut.voxel.yaml.node.sequence.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class StructureManager extends Manager {

    private final Map<String, BlockStructure> structuresByName = new ConcurrentHashMap<>();

    public StructureManager(EnhancedPlugin plugin) {
        super(plugin);
    }

    public List<String> getStructureNames() {
        return new ArrayList<>(structuresByName.keySet());
    }

    public @Nullable BlockStructure getBlockStructure(String name) {
        return structuresByName.get(name);
    }

    public Collection<BlockStructure> getAllBlockStructures() {
        return new ArrayList<>(structuresByName.values());
    }

    public void registerBlockStructure(String name, BlockStructure structure) {
        structuresByName.put(name, structure);
    }

    public BlockStructure removeBlockStructure(String name) {
        return structuresByName.remove(name);
    }

    @Override
    public void loadData() {
        structuresByName.clear();
        for (File structureFile : plugin.getFiles("structures")) {
            final RootSequence config = plugin.loadConfigSequence(structureFile);
            if (structuresByName.size() >= 10) {
                throw new InvalidConfigurationException(config, "The free version allows only up to 10 structures");
            }
            registerBlockStructure(config.getName(), config.load(BlockStructure.class));
        }
    }

}
