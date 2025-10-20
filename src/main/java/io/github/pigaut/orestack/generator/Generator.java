package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.event.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.voxel.bukkit.Rotation;
import io.github.pigaut.voxel.core.function.*;
import io.github.pigaut.voxel.core.hologram.*;
import io.github.pigaut.voxel.core.structure.*;
import io.github.pigaut.voxel.placeholder.*;
import io.github.pigaut.voxel.player.*;
import io.github.pigaut.voxel.server.*;
import io.github.pigaut.voxel.util.*;
import io.github.pigaut.yaml.util.*;
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
    private final Block block;
    private final Rotation rotation;
    private int currentStage;
    private @Nullable BukkitTask growthTask = null;
    private @Nullable Instant growthStart = null;
    private @Nullable HologramDisplay currentHologram = null;
    private @Nullable Double health;
    private boolean updating = false;

    private Generator(GeneratorTemplate generator, Location origin, int currentStage, Rotation rotation) {
        this.template = generator;
        this.origin = origin.clone();
        this.block = origin.getBlock();
        this.currentStage = currentStage;
        this.rotation = rotation;
    }

    public static @NotNull Generator create(@NotNull GeneratorTemplate template, @NotNull Location origin, Rotation rotation, int stage) throws GeneratorOverlapException, GeneratorLimitException {
        final Generator blockGenerator = new Generator(template, origin, stage, rotation);
        plugin.getGenerators().registerGenerator(blockGenerator);
        blockGenerator.updateState();
        return blockGenerator;
    }

    public static @NotNull Generator create(@NotNull GeneratorTemplate template, @NotNull Location origin, Rotation rotation) throws GeneratorOverlapException, GeneratorLimitException {
        return create(template, origin, rotation, template.getMaxStage());
    }

    public static @NotNull Generator create(@NotNull GeneratorTemplate template, @NotNull Location origin) throws GeneratorOverlapException, GeneratorLimitException {
        return create(template, origin, Rotation.NONE);
    }

    public String getName() {
        return template.getName();
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

    public @Nullable Double getHealth() {
        return health;
    }

    public void damage(@Nullable PlayerState damageDealer, double amount) {
        if (health == null) {
            return;
        }
        health = Math.max(0, health - amount);
        if (health == 0) {
            health = null;
            GeneratorDestroyEvent event = new GeneratorDestroyEvent(damageDealer, this);
            SpigotServer.callEvent(event);
            if (!event.isCancelled()) {
                Function onDestroy = getCurrentStage().getDestroyFunction();
                if (onDestroy != null) {
                    onDestroy.dispatch(damageDealer, event, block, null);
                }
                previousStage();
            }
        }
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

    public int getTicksToNextStage() {
        if (growthStart == null) {
            return 0;
        }
        GeneratorStage stage = getCurrentStage();
        int timePassed = (int) (Duration.between(growthStart, Instant.now()).toMillis() / 50);
        return stage.getGrowthTime() - timePassed;
    }

    public int getTicksToRegrownStage() {
        int ticksToRegrown = getTicksToNextStage();
        for (int i = currentStage + 1; i < template.getMaxStage(); i++) {
            GeneratorStage stage = template.getStage(i);
            if (stage.getGrowthChance() == null && stage.getGrowthTime() != 0) {
                ticksToRegrown += stage.getGrowthTime();
            }
        }
        return ticksToRegrown;
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
        final BlockStructure nextStructure = template.getStage(stage).getStructure();
        for (Block previousBlock : template.getAllOccupiedBlocks(origin, rotation)) {
            final Block nextBlock = nextStructure.getBlockAt(origin, rotation, previousBlock.getLocation());
            if (nextBlock == null || nextBlock.getType() != previousBlock.getType()) {
                previousBlock.setType(Material.AIR, false);
            }
        }
        this.currentStage = stage;
        this.updateState();
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
            growthFunction.run(block);
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

        health = stage.getHealth();

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
        GeneratorStage stage = getCurrentStage();
        int ticksToNextStage = getTicksToNextStage();
        int ticksToRegrownStage = getTicksToRegrownStage();

        return new Placeholder[] {
                Placeholder.of("{generator}", template.getName()),
                Placeholder.of("{generator_stage}", currentStage),
                Placeholder.of("{generator_stages}", template.getMaxStage()),
                Placeholder.of("{generator_state}", stage.getState().toString().toLowerCase()),
                Placeholder.of("{generator_rotation}", rotation.toString().toLowerCase()),
                Placeholder.of("{generator_world}", origin.getWorld().getName()),
                Placeholder.of("{generator_x}", origin.getBlockX()),
                Placeholder.of("{generator_y}", origin.getBlockY()),
                Placeholder.of("{generator_z}", origin.getBlockZ()),

                Placeholder.of("{stage_timer}", Ticks.formatCompact(ticksToNextStage)),
                Placeholder.of("{stage_timer_full}", Ticks.formatFull(ticksToNextStage)),
                Placeholder.of("{stage_timer_hours}", Ticks.toHours(ticksToNextStage)),
                Placeholder.of("{stage_timer_minutes}", Ticks.toMinutes(ticksToNextStage)),
                Placeholder.of("{stage_timer_seconds}", Ticks.toSeconds(ticksToNextStage)),

                Placeholder.of("{generator_timer}", Ticks.formatCompact(ticksToRegrownStage)),
                Placeholder.of("{generator_timer_full}", Ticks.formatFull(ticksToRegrownStage)),
                Placeholder.of("{generator_timer_hours}", Ticks.toHours(ticksToRegrownStage)),
                Placeholder.of("{generator_timer_minutes}", Ticks.toMinutes(ticksToRegrownStage)),
                Placeholder.of("{generator_timer_seconds}", Ticks.toSeconds(ticksToRegrownStage))
        };
    }

}
