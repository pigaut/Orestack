package io.github.pigaut.orestack.generator.template;

import io.github.pigaut.orestack.stage.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.voxel.meta.placeholder.*;
import io.github.pigaut.voxel.util.Rotation;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.inventory.*;

import java.util.*;

public class GeneratorTemplate implements PlaceholderSupplier {

    private final String name;
    private final List<GeneratorStage> stages;
    private Material item = Material.PAPER;

    public GeneratorTemplate(String name, List<GeneratorStage> stages) {
        this.name = name;
        this.stages = stages;
    }

    public String getName() {
        return name;
    }

    public Material getItemType() {
        return item;
    }

    public void setItemType(Material item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return GeneratorItem.getItemFromGenerator(this);
    }

    public int getMaxStage() {
        return stages.size() - 1;
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
        return new Placeholder[]{Placeholder.of("%generator%", name), Placeholder.of("%generator_stages%", stages)};
    }

    @Override
    public String toString() {
        return "Generator{" + "name='" + name + '\'' + ", stages=" + stages + '}';
    }

}
