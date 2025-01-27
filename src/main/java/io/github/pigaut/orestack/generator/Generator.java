package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.stage.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.voxel.function.*;
import io.github.pigaut.voxel.hologram.*;
import io.github.pigaut.voxel.hologram.display.*;
import io.github.pigaut.voxel.meta.placeholder.*;
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
    private int currentStage;
    private @Nullable BukkitTask growthTask = null;
    private Instant growthStart = null;
    private @Nullable HologramDisplay currentHologram = null;
    private boolean updating = false;

    private Generator(GeneratorTemplate generator, Location origin, int currentStage) {
        this.template = generator;
        this.origin = origin.clone();
        this.currentStage = currentStage;
    }

    public static @NotNull Generator create(@NotNull GeneratorTemplate template, @NotNull Location origin) throws GeneratorOverlapException {
        if (plugin.getGenerators().containsGenerator(template.getAllOccupiedBlocks(origin))) {
            throw new GeneratorOverlapException();
        }
        final Generator blockGenerator = new Generator(template, origin, template.getStageFromStructure(origin));
        plugin.getGenerators().registerGenerator(blockGenerator);
        blockGenerator.updateState();
        return blockGenerator;
    }

    public @NotNull GeneratorTemplate getTemplate() {
        return template;
    }

    public @NotNull Location getOrigin() {
        return origin.clone();
    }

    public Set<Block> getAllOccupiedBlocks() {
        return template.getAllOccupiedBlocks(origin);
    }

    public List<Block> getBlocks() {
        return getCurrentStage().getStructure().getBlocks(origin);
    }

    public boolean exists() {
        return plugin.getGenerators().isGenerator(origin);
    }

    public boolean isFirstStage() {
        return currentStage == 0;
    }

    public boolean isLastStage() {
        return currentStage >= template.getMaxStage();
    }

    public @Nullable Duration getTimeBeforeNextStage() {
        return growthStart != null ? Duration.between(growthStart, Instant.now()) : null;
    }

    public @NotNull GeneratorStage getCurrentStage() {
        return template.getStage(currentStage);
    }

    public @Nullable HologramDisplay getCurrentHologram() {
        return currentHologram;
    }

    public void setCurrentStage(int stage) {
        if (!exists()) {
            return;
        }
        final GeneratorStage currentStage = getCurrentStage();
        currentStage.getStructure().removeBlocks(origin);
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
        GeneratorStage nextStage = template.getStage(peekStage);
        while (!nextStage.shouldGrow()) {
            if (peekStage >= template.getMaxStage() || nextStage.getState() == GeneratorState.REPLENISHED) {
                return;
            }
            peekStage++;
            nextStage = template.getStage(peekStage);
        }

        final Function growthFunction = nextStage.getGrowthFunction();
        if (growthFunction != null) {
            growthFunction.run(origin.getBlock());
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
            previousStage = template.getStage(peekStage);
        }
        while (previousStage.getState() == GeneratorState.GROWING);
        setCurrentStage(peekStage);
    }

    private void updateState() {
        final GeneratorStage stage = getCurrentStage();
        updating = true;
        plugin.getScheduler().runTaskLater(1, () -> {
            stage.getStructure().createBlocks(origin);
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
            currentHologram = hologram.spawn(origin.clone().add(0.5, 0, 0.5), false, this);
        }
    }

    public boolean isUpdating() {
        return updating;
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
