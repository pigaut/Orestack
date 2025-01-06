package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.stage.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.scheduler.*;

public class BlockGenerator {

    private final OrestackPlugin plugin = OrestackPlugin.getPlugin();

    private final Generator generator;
    private final Location location;
    private int currentStage;
    private BukkitTask growthTask = null;

    public BlockGenerator(Generator generator, Location location) {
        this.generator = generator;
        this.location = location;
        this.currentStage = generator.getStages();
    }

    public Generator getGenerator() {
        return generator;
    }

    public Location getLocation() {
        return location;
    }

    public Block getBlock() {
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

    public GeneratorStage getCurrentStage() {
        return generator.getStage(currentStage);
    }

    public void setCurrentStage(int stage) {
        this.currentStage = stage;
        updateState();
    }

    public void nextStage() {
        if (isLastStage()) {
            throw new IllegalStateException("Block generator does not have a next stage");
        }

        if (!getCurrentStage().matchBlock(getBlock().getBlockData())) {
            plugin.getGenerators().removeBlockGenerator(location);
            return;
        }

        int peekStage = currentStage + 1;
        GeneratorStage nextStage = generator.getStage(peekStage);
        while (!nextStage.shouldGrow()) {
            if (peekStage >= generator.getStages() || nextStage.getState() == GeneratorState.REPLENISHED) {
                return;
            }
            peekStage++;
            nextStage = generator.getStage(peekStage);
        }

        nextStage.getOnGrowth().forEach(function -> function.run(this.getBlock()));
        setCurrentStage(peekStage);
    }

    public void previousStage() {
        if (isFirstStage()) {
            throw new IllegalStateException("Block generator does not have a previous stage");
        }

        if (!getCurrentStage().matchBlock(getBlock().getBlockData())) {
            plugin.getGenerators().removeBlockGenerator(location);
            return;
        }

        int peekStage = currentStage;
        GeneratorStage previousStage;
        do {
            peekStage--;
            previousStage = generator.getStage(peekStage);
        }
        while (previousStage.getState() == GeneratorState.GROWING);
        setCurrentStage(peekStage);
    }

    public void updateState() {
        if (!exists()) {
            return;
        }
        final GeneratorStage stage = getCurrentStage();
        plugin.getScheduler().runTaskLater(1, () -> stage.updateBlock(this.getBlock()));
        if (!isLastStage() && stage.getState() != GeneratorState.REPLENISHED) {
            if (growthTask != null) {
                growthTask.cancel();
            }
            this.growthTask = plugin.getScheduler().runTaskLater(stage.getGrowthTime(), () -> {
                growthTask = null;
                this.nextStage();
            });
        }
    }

    @Override
    public String toString() {
        return "BlockGenerator{" +
                "generator=" + generator.getName() +
                ", location=" + location +
                ", currentStage=" + currentStage +
                '}';
    }

}
