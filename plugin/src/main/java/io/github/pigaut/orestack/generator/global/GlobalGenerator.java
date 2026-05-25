package io.github.pigaut.orestack.generator.global;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.exception.*;
import io.github.pigaut.orestack.generator.phase.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.core.hologram.*;
import io.github.pigaut.voxel.core.transform.Rotation;
import io.github.pigaut.voxel.data.function.*;
import io.github.pigaut.voxel.data.structure.*;
import io.github.pigaut.voxel.data.structure.global.Structure;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class GlobalGenerator extends BasicGenerator {

    private static final OrestackPlugin plugin = OrestackPlugin.getInstance();

    private Structure structure;
    private @Nullable Hologram hologram = null;

    private GlobalGenerator(GeneratorTemplate template, Location origin, Rotation rotation) {
        super(template, origin, rotation);
    }

    public static @NotNull GlobalGenerator create(@NotNull GeneratorTemplate template, @NotNull Location origin, @NotNull Rotation rotation, int phase) throws GeneratorOverlapException, GeneratorLimitException {
        GlobalGenerator generator = new GlobalGenerator(template, origin, rotation);
        plugin.getGenerators().registerGenerator(generator);
        generator.setPhase(phase, true);
        return generator;
    }

    public static @NotNull GlobalGenerator create(@NotNull GeneratorTemplate template, @NotNull Location origin, @NotNull Rotation rotation) throws GeneratorOverlapException, GeneratorLimitException {
        return create(template, origin, rotation, template.getMaxPhase());
    }

    public static @NotNull GlobalGenerator create(@NotNull GeneratorTemplate template, @NotNull Location origin) throws GeneratorOverlapException, GeneratorLimitException {
        return create(template, origin, Rotation.NONE);
    }

    @Override
    public void onStateChange() {
        updateHologram();
    }

    public boolean isValid() {
        return structure.isPlaced();
    }

    public void cleanup() {
        cancelGrowth();
        removeBlocks();
        removeHologram();
    }

    public void remove() {
        cleanup();

        if (plugin.getSettings().isKeepBlocksOnRemove()) {
            template.getLastPhase().getStructureTemplate().place(origin, rotation);
        }

        plugin.getGenerators().unregisterGenerator(this);
    }

    public @NotNull Structure getStructure() {
        return structure;
    }

    public void setStructure(@NotNull StructureTemplate newStructure) {
        if (structure == null) {
            structure = newStructure.place(origin, rotation);
        } else {
            structure = structure.replace(newStructure);
        }
    }

    public @Nullable Hologram getHologram() {
        return hologram;
    }

    public void setHologram(@Nullable HologramTemplate newHologram, @NotNull Context context) {
        removeHologram();
        if (newHologram != null) {
            Location offsetLocation = origin.clone().add(0.5, 0.5, 0.5);
            hologram = newHologram.spawn(offsetLocation, rotation, context);
        }
    }

    public void removeBlocks() {
        structure.remove();
    }

    public void updateHologram() {
        if (hologram != null && hologram.exists()) {
            hologram.update();
        }
    }

    public void removeHologram() {
        if (hologram != null) {
            hologram.remove();
            hologram = null;
        }
    }

    public void setPhase(int phaseIndex, boolean growing) {
        if (phaseIndex < 0 || phaseIndex > getMaxPhase()) {
            return;
        }

        GeneratorPhase newPhase = getPhase(phaseIndex);
        Context context = Context.builder(plugin)
                .withBlock(origin.getBlock())
                .with(Generator.class, this)
                .build();

        state.setCurrentPhase(phaseIndex);
        state.setHealth(newPhase.getMaxHealth());
        setStructure(newPhase.getStructureTemplate());
        setHologram(newPhase.getHologramTemplate(), context);

        if (growing) {
            Function growthFunction = newPhase.getGrowthFunction();
            if (growthFunction != null) {
                growthFunction.run(context);
            }
        }
    }

}
