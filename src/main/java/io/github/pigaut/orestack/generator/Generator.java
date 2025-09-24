package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.event.*;
import io.github.pigaut.orestack.generator.template.*;
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

    private Generator(GeneratorTemplate generator, Location origin, int currentStage, Rotation rotation) {
        this.template = generator;
        this.origin = origin.clone();
        this.currentStage = currentStage;
        this.rotation = rotation;
    }

    public static @NotNull Generator create(@NotNull GeneratorTemplate template, @NotNull Location origin, Rotation rotation, int stage) throws GeneratorOverlapException {
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

    public boolean isValid() {
        for (Block block : getBlocks()) {
            final Generator generator = plugin.getGenerator(block.getLocation());
            if (!this.equals(generator)) {
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

    public void setCurrentStage(int stage) {
        if (stage < 0 || stage > template.getMaxStage()) {
            return;
        }

        if (!this.isValid()) {
            return;
        }
        this.updating = true;
        this.cancelGrowth();
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

    public @Nullable HologramDisplay getCurrentHologram() {
        return currentHologram;
    }

    public void cancelGrowth() {
        if (growthTask != null) {
            if (!growthTask.isCancelled()) {
                growthTask.cancel();
            }
            growthTask = null;
        }
        growthStart = null;
    }

    public void reset() {
        this.setCurrentStage(currentStage);
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
        while (previousStage.getState().isTransitional());

        setCurrentStage(peekStage);
    }

    private void updateState() {
        final GeneratorStage stage = getCurrentStage();
        stage.getStructure().updateBlocks(origin, rotation);
        updating = false;

        if (currentHologram != null) {
            currentHologram.destroy();
        }

        final Hologram hologram = stage.getHologram();
        if (hologram != null) {
            final Location offsetLocation = origin.clone().add(0.5, 0.5, 0.5);
            currentHologram = hologram.spawn(offsetLocation, rotation, this);
        }

        if (stage.getState() == GeneratorState.REGROWN) {
            return;
        }

        growthStart = Instant.now();
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
        final GeneratorStage stage = getCurrentStage();
        final Duration timer = getTimeBeforeNextStage();

        long secondsToNextStage = (timer != null)
                ? Math.max(0, (stage.getGrowthTime() / 20) - timer.getSeconds())
                : 0;

        long secondsToReplenished = secondsToNextStage;
        final List<GeneratorStage> stages = template.getStages();
        for (int i = currentStage; i < template.getMaxStage(); i++) {
            GeneratorStage nextStage = stages.get(i);
            if (nextStage.getGrowthChance() != null || nextStage.getGrowthTime() == 0) {
                continue;
            }
            secondsToReplenished += (nextStage.getGrowthTime() / 20);
        }

        return new Placeholder[]{
                Placeholder.of("{generator}", template.getName()),
                Placeholder.of("{generator_stage}", currentStage),
                Placeholder.of("{generator_stages}", template.getMaxStage()),
                Placeholder.of("{generator_state}", stage.getState().toString().toLowerCase()),
                Placeholder.of("{generator_world}", origin.getWorld().getName()),
                Placeholder.of("{generator_x}", origin.getBlockX()),
                Placeholder.of("{generator_y}", origin.getBlockY()),
                Placeholder.of("{generator_z}", origin.getBlockZ()),

                //Deprecated
                Placeholder.of("{generator_timer_seconds}", secondsToNextStage),
                Placeholder.of("{generator_timer_minutes}", secondsToNextStage / 60),

                Placeholder.of("{generator_next_timer}", formatTimer(secondsToNextStage)),
                Placeholder.of("{generator_next_seconds}", secondsToNextStage),
                Placeholder.of("{generator_next_minutes}", secondsToNextStage / 60),
                Placeholder.of("{generator_replenish_timer}", formatTimer(secondsToReplenished)),
                Placeholder.of("{generator_replenish_seconds}", secondsToReplenished),
                Placeholder.of("{generator_replenish_minutes}", secondsToReplenished / 60)
        };
    }

    private String formatTimer(long seconds) {
        if (seconds < 60) {
            return seconds + "s";
        }

        long minutes = seconds / 60;
        long leftOverSeconds = seconds % 60;
        if (leftOverSeconds == 0) {
            return minutes + "m";
        }

        return minutes + "m " + leftOverSeconds + "s";
    }

}
