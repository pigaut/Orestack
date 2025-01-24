package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.stage.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.voxel.meta.placeholder.*;
import io.github.pigaut.voxel.util.*;
import org.bukkit.inventory.*;

import java.util.*;

public class Generator implements PlaceholderSupplier {

    private final String name;
    private final List<GeneratorStage> stages;

    public Generator(String name, List<GeneratorStage> stages) {
        this.name = name;
        this.stages = stages;
    }

    public String getName() {
        return name;
    }

    public ItemStack getItem() {
        return GeneratorItem.getItemFromGenerator(this);
    }

    public boolean isCrop() {
        for (GeneratorStage stage : stages) {
            if (Crops.isCrop(stage.getBlockType())) {
                return true;
            }
        }
        return false;
    }

    public int getStages() {
        return stages.size() - 1;
    }

    public GeneratorStage getStage(int stage) {
        return stages.get(stage);
    }

    public int indexOf(GeneratorStage stage) {
        final int index = stages.indexOf(stage);
        if (index == -1) {
            throw new IllegalArgumentException("Generator does not contain that stage");
        }
        return index;
    }

    public GeneratorStage getLastStage() {
        return stages.get(stages.size() - 1);
    }

    public Placeholder[] getPlaceholders() {
        return new Placeholder[]{
                Placeholder.of("%generator%", name),
                Placeholder.of("%generator_stages%", stages)
        };
    }

    @Override
    public String toString() {
        return "Generator{" +
                "name='" + name + '\'' +
                ", stages=" + stages +
                '}';
    }

}
