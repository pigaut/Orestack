package io.github.pigaut.orestack.generator.template;

import io.github.pigaut.orestack.generator.phase.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.bukkit.Rotation;
import io.github.pigaut.voxel.plugin.manager.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorTemplate implements Identifiable {

    private final String name;
    private final String group;
    private final List<GeneratorPhase> phases;
    private final boolean multiBlock;
    private final @Nullable Double maxHealth;
    private Material itemType;

    public GeneratorTemplate(@NotNull String name, @Nullable String group,
                             @NotNull List<GeneratorPhase> phases, Material itemType,
                             boolean multiBlock, @Nullable Double maxHealth) {
        this.name = name;
        this.group = group;
        this.phases = phases;
        setItemType(itemType);
        this.multiBlock = multiBlock;
        this.maxHealth = maxHealth;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @Nullable String getGroup() {
        return group;
    }

    public boolean isMultiBlock() {
        return multiBlock;
    }

    public boolean hasHealth() {
        return maxHealth != null;
    }

    public @Nullable Double getMaxHealth() {
        return maxHealth;
    }

    public @NotNull Material getItemType() {
        return itemType;
    }

    public void setItemType(Material itemType) {
        if (MaterialUtil.isAir(itemType)) {
            this.itemType = Material.TERRACOTTA;
        }
        else if (MaterialUtil.isCrop(itemType)) {
            this.itemType = MaterialUtil.getCropSeeds(itemType);
        }
        else {
            this.itemType = itemType;
        }
    }

    public int getMaxPhase() {
        return phases.size() - 1;
    }

    public List<GeneratorPhase> getPhases() {
        return new ArrayList<>(phases);
    }

    public GeneratorPhase getPhase(int phase) {
        return phases.get(phase);
    }

    public GeneratorPhase getLastPhase() {
        return phases.get(getMaxPhase());
    }

    public int indexOfPhase(GeneratorPhase phase) {
        final int index = phases.indexOf(phase);
        if (index == -1) {
            throw new IllegalArgumentException("Generator does not contain that phase");
        }
        return index;
    }

    public int getPhaseFromStructure(Location origin, Rotation rotation) {
        int currentPhase = getMaxPhase();
        for (int i = getMaxPhase(); i >= 0; i--) {
            final GeneratorPhase phase = getPhase(i);
            if (phase.getStructureTemplate().isPlaced(origin, rotation)) {
                currentPhase = i;
                break;
            }
        }
        return currentPhase;
    }

    public Set<Block> getOccupiedBlocks(@NotNull Location location, @NotNull Rotation rotation) {
        Set<Block> blocks = new HashSet<>();
        for (GeneratorPhase phase : phases) {
            blocks.addAll(phase.getStructureTemplate().getOccupiedBlocks(location, rotation));
        }
        return blocks;
    }

    @Override
    public String toString() {
        return "GeneratorTemplate{" +
                "name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", phases=" + phases +
                ", itemType=" + itemType +
                '}';
    }

}
