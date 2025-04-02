package io.github.pigaut.orestack.config;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.structure.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.loader.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BlockStructureLoader implements ConfigLoader<BlockStructure> {

    private final OrestackPlugin plugin;

    public BlockStructureLoader(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getProblemDescription() {
        return "invalid structure";
    }

    @Override
    public @NotNull BlockStructure loadFromScalar(ConfigScalar scalar) throws InvalidConfigurationException {
        final String structureName = scalar.toString();
        final BlockStructure blockStructure = plugin.getBlockStructure(structureName);
        if (blockStructure == null) {
            throw new InvalidConfigurationException(scalar, "Could not find any block structure with name: " + structureName);
        }
        return blockStructure;
    }

    @Override
    public @NotNull BlockStructure loadFromSection(@NotNull ConfigSection config) throws InvalidConfigurationException {
        return new BlockStructure(List.of(config.load(BlockChange.class)));
    }

    @Override
    public @NotNull BlockStructure loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigurationException {
        final List<BlockChange> blockChanges = sequence.getAll(BlockChange.class);
        if (blockChanges.size() < 2) {
            throw new InvalidConfigurationException(sequence, "Structure must have at least two blocks in it");
        }
        if (!sequence.isRoot() && blockChanges.size() > 8) {
            throw new InvalidConfigurationException(sequence, "The free version supports unlimited inline structures but only up to 8 blocks");
        }
        return new BlockStructure(blockChanges);
    }

}
