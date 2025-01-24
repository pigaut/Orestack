package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.stage.*;
import io.github.pigaut.voxel.function.*;
import io.github.pigaut.voxel.hologram.*;
import io.github.pigaut.voxel.hologram.display.*;
import io.github.pigaut.voxel.meta.placeholder.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.bukkit.scheduler.*;
import org.jetbrains.annotations.*;

import java.time.*;

public class BlockGenerator implements PlaceholderSupplier {

    private final OrestackPlugin plugin = OrestackPlugin.getPlugin();

    private final Generator generator;
    private final Location location;
    private int currentStage;
    private @Nullable BukkitTask growthTask = null;
    private Instant growthStart = null;
    private @Nullable HologramDisplay currentHologram = null;
    private boolean updating = false;

    private BlockGenerator(Generator generator, Location location, int currentStage) {
        this.generator = generator;
        this.location = location;
        this.currentStage = currentStage;
    }

    public static @NotNull BlockGenerator create(@NotNull Generator generator, @NotNull Location location) {
        final BlockData blockData = location.getBlock().getBlockData();
        int currentStage = generator.getStages();
        for (int i = generator.getStages() - 1; i >= 0; i--) {
            if (generator.getStage(i).matchBlock(blockData)) {
                currentStage = i;
            }
        }
        final BlockGenerator blockGenerator = new BlockGenerator(generator, location.clone(), currentStage);
        blockGenerator.plugin.getGenerators().addBlockGenerator(blockGenerator);
        blockGenerator.updateState();
        return blockGenerator;
    }

    public @NotNull Generator getGenerator() {
        return generator;
    }

    public @NotNull Location getLocation() {
        return location.clone();
    }

    public @NotNull Block getBlock() {
        return location.getBlock();
    }

    public boolean exists() {
        return plugin.getBlockGenerator(location) != null;
    }

    public boolean isFirstStage() {
        return currentStage == 0;
    }

    public boolean isLastStage() {
        return currentStage >= generator.getStages();
    }

    public @Nullable Duration getTimeBeforeNextStage() {
        return growthStart != null ? Duration.between(growthStart, Instant.now()) : null;
    }

    public @NotNull GeneratorStage getCurrentStage() {
        return generator.getStage(currentStage);
    }

    public @Nullable HologramDisplay getCurrentHologram() {
        return currentHologram;
    }

    public void setCurrentStage(int stage) {
        if (!exists()) {
            return;
        }
        final GeneratorStage currentStage = getCurrentStage();
        if (!currentStage.matchBlock(getBlock().getBlockData())) {
            plugin.getGenerators().removeBlockGenerator(location);
            return;
        }
        this.currentStage = stage;
        updateState();
    }

    public void cancelGrowth() {
        if (growthTask != null) {
            growthTask.cancel();
        }
        growthTask = null;
        growthStart = null;
    }

    public void nextStage() {
        if (isLastStage()) {
            throw new IllegalStateException("Block generator does not have a next stage");
        }

        int peekStage = this.currentStage + 1;
        GeneratorStage nextStage = generator.getStage(peekStage);
        while (!nextStage.shouldGrow()) {
            if (peekStage >= generator.getStages() || nextStage.getState() == GeneratorState.REPLENISHED) {
                return;
            }
            peekStage++;
            nextStage = generator.getStage(peekStage);
        }

        final Function growthFunction = nextStage.getGrowthFunction();
        if (growthFunction != null) {
            growthFunction.run(this.getBlock());
        }

        setCurrentStage(peekStage);
    }

    public void previousStage() {
        if (isFirstStage()) {
            throw new IllegalStateException("Block generator does not have a previous stage");
        }

        int peekStage = this.currentStage;
        GeneratorStage previousStage;
        do {
            peekStage--;
            previousStage = generator.getStage(peekStage);
        }
        while (previousStage.getState() == GeneratorState.GROWING);
        setCurrentStage(peekStage);
    }

    private void updateState() {
        final GeneratorStage stage = getCurrentStage();
        updating = true;
        plugin.getScheduler().runTaskLater(1, () -> {
            stage.updateBlock(this.getBlock());
            updating = false;
        });

        if (currentHologram != null) {
            currentHologram.despawn();
            currentHologram = null;
        }

        if (!isLastStage() && stage.getState() != GeneratorState.REPLENISHED) {
            if (growthTask != null && !growthTask.isCancelled()) {
                growthTask.cancel();
                growthStart = null;
            }
            growthTask = plugin.getScheduler().runTaskLater(stage.getGrowthTime(), () -> {
                growthTask = null;
                this.nextStage();
            });
            growthStart = Instant.now();
        }

        final Hologram hologram = stage.getHologram();
        if (hologram != null) {
            currentHologram = hologram.spawn(location.clone().add(0.5, 0, 0.5), false, this);
        }
    }

    public boolean isUpdating() {
        return updating;
    }

    @Override
    public String toString() {
        return "BlockGenerator{" +
                "generator=" + generator.getName() +
                ", location=" + location +
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
                Placeholder.of("%generator_world%", location.getWorld().getName()),
                Placeholder.of("%generator_x%", location.getBlockX()),
                Placeholder.of("%generator_y%", location.getBlockY()),
                Placeholder.of("%generator_z%", location.getBlockZ()),
                Placeholder.of("%generator_timer_seconds%", remainingSeconds),
                Placeholder.of("%generator_timer_minutes%", remainingSeconds / 60)
        );
    }

}
