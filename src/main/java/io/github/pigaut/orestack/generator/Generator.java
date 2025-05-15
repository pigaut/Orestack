package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.event.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.stage.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.voxel.core.function.*;
import io.github.pigaut.voxel.core.structure.*;
import io.github.pigaut.voxel.hologram.*;
import io.github.pigaut.voxel.placeholder.*;
import io.github.pigaut.voxel.server.*;
import io.github.pigaut.voxel.util.Rotation;
import io.github.pigaut.voxel.util.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.scheduler.*;
import org.jetbrains.annotations.*;

import java.time.*;
import java.util.*;

public class Generator implements PlaceholderSupplier {

    private static final OrestackPlugin plugin = OrestackPlugin.getPlugin();

    private final GeneratorTemplate template;
    private final Location origin;
    private final Rotation rotation;
    private int currentStage;
    private @Nullable BukkitTask growthTask = null;
    private @Nullable Instant growthStart = null;
    private @Nullable HologramDisplay currentHologram = null;
    private boolean updating = false;
    private boolean harvesting = false;

    private Generator(GeneratorTemplate generator, Location origin, int currentStage, Rotation rotation) {
        this.template = generator;
        this.origin = origin.clone();
        this.currentStage = currentStage;
        this.rotation = rotation;
    }

    public static @NotNull Generator create(@NotNull GeneratorTemplate template, @NotNull Location origin, Rotation rotation, int stage) throws GeneratorOverlapException {
        for (Block block : template.getAllOccupiedBlocks(origin, rotation)) {
            if (plugin.getGenerators().isGenerator(block.getLocation())) {
                throw new GeneratorOverlapException();
            }
        }
        final Generator blockGenerator = new Generator(template, origin, stage, rotation);
        plugin.getGenerators().registerGenerator(blockGenerator);
        blockGenerator.updateState();
        return blockGenerator;
    }

    public static @NotNull Generator create(@NotNull GeneratorTemplate template, @NotNull Location origin, Rotation rotation) throws GeneratorOverlapException {
        return create(template, origin, rotation, template.getMaxStage());
    }

    public static @NotNull Generator create(@NotNull GeneratorTemplate template, @NotNull Location origin) throws GeneratorOverlapException {
        return create(template, origin, Rotation.NONE);
    }

    public boolean exists() {
        for (Block block : getBlocks()) {
            if (!plugin.getGenerators().isGenerator(block.getLocation())) {
                return false;
            }
        }
        return true;
    }

    public boolean isFirstStage() {
        return currentStage == 0;
    }

    public boolean isLastStage() {
        return currentStage >= template.getMaxStage();
    }

    public boolean isUpdating() {
        return updating;
    }

    public boolean isHarvesting() {
        return harvesting;
    }

    public void setHarvesting(boolean harvesting) {
        this.harvesting = harvesting;
    }

    public boolean matchBlocks() {
        return getCurrentStage().getStructure().matchBlocks(origin, rotation);
    }

    public @NotNull GeneratorTemplate getTemplate() {
        return template;
    }

    public @NotNull Location getOrigin() {
        return origin.clone();
    }

    public Rotation getRotation() {
        return rotation;
    }

    public List<Block> getBlocks() {
        return getCurrentStage().getStructure().getBlocks(origin, rotation);
    }

    public Set<Block> getAllOccupiedBlocks() {
        return template.getAllOccupiedBlocks(origin, rotation);
    }

    public @Nullable Duration getTimeBeforeNextStage() {
        return growthStart != null ? Duration.between(growthStart, Instant.now()) : null;
    }

    public int getCurrentStageId() {
        return currentStage;
    }

    public @NotNull GeneratorStage getCurrentStage() {
        return template.getStage(currentStage);
    }

    public @Nullable HologramDisplay getCurrentHologram() {
        return currentHologram;
    }

    public void cancelGrowth() {
        if (growthTask != null) {
            growthTask.cancel();
        }
        growthTask = null;
        growthStart = null;
    }

    public void reset() {
        this.setCurrentStage(currentStage);
    }

    public void setCurrentStage(int stage) {
        if (!this.exists()) {
            return;
        }
        this.updating = true;
        plugin.getScheduler().runTaskLater(1, () -> {
            final BlockStructure nextStructure = template.getStage(stage).getStructure();
            for (Block previousBlock : template.getAllOccupiedBlocks(origin, rotation)) {
                final Block nextBlock = nextStructure.getBlockAt(origin, rotation, previousBlock.getLocation());
                if (nextBlock == null || nextBlock.getType() != previousBlock.getType()) {
                    previousBlock.setType(Material.AIR, false);
                }
            }
            this.currentStage = stage;
            this.updateState();
        });
    }

    public void nextStage() {
        if (this.isLastStage()) {
            return;
        }

        final GeneratorStage currentStage = this.getCurrentStage();
        if (currentStage.getGrowthTime() == 0) {
            return;
        }

        int peekStage = this.currentStage + 1;
        GeneratorStage nextStage = template.getStage(peekStage);
        while (nextStage.getGrowthTime() == 0) {
            if (peekStage >= template.getMaxStage()) {
                break;
            }
            peekStage++;
            nextStage = template.getStage(peekStage);
        }

        boolean shouldGrow = false;
        while (!shouldGrow) {
            final Double growthChance = nextStage.getGrowthChance();
            if (growthChance != null && !Probability.test(growthChance)) {
                if (currentStage.getState().isHarvestable()) {
                    return;
                }
                peekStage++;
                nextStage = template.getStage(peekStage);
                continue;
            }
            shouldGrow = true;
        }

        final Function growthFunction = nextStage.getGrowthFunction();
        if (growthFunction != null) {
            growthFunction.run(origin.getBlock());
        }

        this.setCurrentStage(peekStage);
    }

    public void previousStage() {
        if (isFirstStage()) {
            return;
        }

        int peekStage = currentStage;
        GeneratorStage previousStage;
        do {
            peekStage--;
            previousStage = template.getStage(peekStage);
        }
        while (previousStage.getState() == GeneratorState.GROWING);

        setCurrentStage(peekStage);
    }

    private void updateState() {
        final GeneratorStage stage = getCurrentStage();
        stage.getStructure().updateBlocks(origin, rotation);
        updating = false;
        harvesting = false;

        if (currentHologram != null) {
            currentHologram.despawn();
            currentHologram = null;
        }

        if (stage.getState() != GeneratorState.REPLENISHED) {
            if (growthTask != null && !growthTask.isCancelled()) {
                growthTask.cancel();
                growthStart = null;
            }
            growthTask = plugin.getScheduler().runTaskLater(stage.getGrowthTime(), () -> {
                growthTask = null;
                if (this.isLastStage()) {
                    return;
                }

                final GeneratorGrowthEvent growthEvent = new GeneratorGrowthEvent(this);
                SpigotServer.callEvent(growthEvent);

                if (!growthEvent.isCancelled()) {
                    this.nextStage();
                }
            });
            growthStart = Instant.now();
        }

        final Hologram hologram = stage.getHologram();
        if (hologram != null) {
            final Location offsetLocation = origin.clone().add(0.5, 0.5, 0.5);
            currentHologram = hologram.spawn(offsetLocation, rotation, false, this);
        }
    }

    @Override
    public String toString() {
        return "BlockGenerator{" +
                "generator=" + template.getName() +
                ", location=" + origin +
                ", currentStage=" + currentStage +
                '}';
    }

    @Override
    public @NotNull Placeholder[] getPlaceholders() {
        final GeneratorStage currentStage = getCurrentStage();
        final Duration timer = getTimeBeforeNextStage();

        long remainingSeconds = (timer != null)
                ? Math.max(0, (currentStage.getGrowthTime() / 20) - timer.getSeconds())
                : 0;

        return Placeholder.mergeAll(
                currentStage.getPlaceholders(),
                Placeholder.of("%generator_world%", origin.getWorld().getName()),
                Placeholder.of("%generator_x%", origin.getBlockX()),
                Placeholder.of("%generator_y%", origin.getBlockY()),
                Placeholder.of("%generator_z%", origin.getBlockZ()),
                Placeholder.of("%generator_timer_seconds%", remainingSeconds),
                Placeholder.of("%generator_timer_minutes%", remainingSeconds / 60)
        );
    }

}
