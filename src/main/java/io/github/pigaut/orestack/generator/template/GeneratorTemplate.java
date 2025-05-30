package io.github.pigaut.orestack.generator.template;

import io.github.pigaut.orestack.stage.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.voxel.placeholder.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.voxel.util.*;
import io.github.pigaut.voxel.util.Rotation;
import io.github.pigaut.yaml.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorTemplate implements Identifiable, PlaceholderSupplier {

    private final String name;
    private final String group;
    private final ConfigSequence sequence;
    private final List<GeneratorStage> stages;
    private Rotation rotation = Rotation.NONE;
    private Material itemType = Material.TERRACOTTA;

    public GeneratorTemplate(String name, @Nullable String group, ConfigSequence sequence, List<GeneratorStage> stages) {
        this.name = name;
        this.group = group;
        this.sequence = sequence;
        this.stages = stages;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @Nullable String getGroup() {
        return group;
    }

    @Override
    public @NotNull ConfigSequence getField() {
        return sequence;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public ItemStack getItem() {
        return GeneratorTools.getGeneratorTool(this);
    }

    public Material getItemType() {
        return itemType;
    }

    public void setItemType(Material itemType) {
        this.itemType = itemType;
    }

    public int getMaxStage() {
        return stages.size() - 1;
    }

    public List<GeneratorStage> getStages() {
        return new ArrayList<>(stages);
    }

    public GeneratorStage getStage(int stage) {
        return stages.get(stage);
    }

    public GeneratorStage getLastStage() {
        return stages.get(getMaxStage());
    }

    public int indexOfStage(GeneratorStage stage) {
        final int index = stages.indexOf(stage);
        if (index == -1) {
            throw new IllegalArgumentException("Generator does not contain that stage");
        }
        return index;
    }

    public int getStageFromStructure(Location origin, Rotation rotation) {
        int currentStage = getMaxStage();
        for (int i = getMaxStage(); i >= 0; i--) {
            final GeneratorStage stage = getStage(i);
            if (stage.getStructure().matchBlocks(origin, rotation)) {
                currentStage = i;
                break;
            }
        }
        return currentStage;
    }

    public Set<Block> getAllOccupiedBlocks(Location location, Rotation rotation) {
        final Set<Block> blocks = new HashSet<>();
        for (GeneratorStage stage : stages) {
            blocks.addAll(stage.getStructure().getBlocks(location, rotation));
        }
        return blocks;
    }

    public Placeholder[] getPlaceholders() {
        return new Placeholder[]{
                Placeholder.of("{generator}", name),
                Placeholder.of("{generator_stages}", stages),
                Placeholder.of("{generator_rotation}", rotation)
        };
    }

    @Override
    public String toString() {
        return "Generator{" + "name='" + name + '\'' + ", stages=" + stages + '}';
    }

}
