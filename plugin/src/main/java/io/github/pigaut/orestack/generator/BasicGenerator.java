package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.api.event.*;
import io.github.pigaut.orestack.generator.phase.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.core.transform.Rotation;
import io.github.pigaut.voxel.data.function.*;
import io.github.pigaut.voxel.util.*;
import io.github.pigaut.voxel.util.Server;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class BasicGenerator implements Generator {

    protected static final OrestackPlugin plugin = OrestackPlugin.getInstance();

    protected final GeneratorTemplate template;
    protected final String name;
    protected final Location origin;
    protected final Rotation rotation;
    protected final GeneratorState state;

    public BasicGenerator(GeneratorTemplate template, Location origin, Rotation rotation) {
        this.template = template;
        this.name = template.getName();
        this.origin = origin;
        this.rotation = rotation;
        this.state = new GeneratorState(this);
    }

    public void onStateChange() {
    }

    public boolean isFullyGrown() {
        return state.getCurrentPhase() >= template.getMaxPhase();
    }

    public boolean isDepleted() {
        return state.getCurrentPhase() <= 0;
    }

    public @NotNull String getName() {
        return template.getName();
    }

    public @NotNull GeneratorTemplate getTemplate() {
        return template;
    }

    public @NotNull Location getOrigin() {
        return origin.clone();
    }

    public @NotNull Rotation getRotation() {
        return rotation;
    }

    public int getMaxPhase() {
        return template.getMaxPhase();
    }

    public @NotNull GeneratorPhase getPhase(int phase) {
        return template.getPhase(phase);
    }

    public @NotNull GeneratorPhase getPhase() {
        return template.getPhase(state.getCurrentPhase());
    }

    @Override
    public @NotNull GeneratorState getState() {
        return state;
    }

    public @NotNull Set<Block> getOccupiedBlocks() {
        GeneratorPhase phase = getPhase();
        return phase.getStructureTemplate().getOccupiedBlocks(origin, rotation);
    }

    public @NotNull Set<Block> getAllOccupiedBlocks() {
        return template.getOccupiedBlocks(origin, rotation);
    }

    public @Nullable Integer getNextGrowthPhase(int targetPhase) {
        int maxPhase = getMaxPhase();
        if (targetPhase >= maxPhase) {
            return maxPhase;
        }

        GeneratorPhase phase = getPhase(targetPhase);
        for (int i = targetPhase; i < maxPhase; i++) {
            GeneratorPhase nextPhase = template.getPhase(i);
            if (nextPhase.growsInstantly()) {
                continue;
            }

            Double growthChance = nextPhase.getGrowthChance();
            if (growthChance != null && !Probability.test(growthChance)) {
                if (phase.getState().isHarvestable()) {
                    return null;
                }
                continue;
            }
            return i;
        }

        return maxPhase;
    }

    public int getNextHarvestingPhase(int currentPhase) {
        GeneratorPhase phase;
        do {
            currentPhase--;
            phase = template.getPhase(currentPhase);
        } while (phase.getState().isTransitional());

        return currentPhase;
    }

    public int getTicksToNextPhase() {
        return state.getTicksToNextPhase();
    }

    public int getTicksToRegrownPhase() {
        return state.getTicksToRegrownPhase();
    }

    public @Nullable Double getTotalHealth() {
        return state.getHealth();
    }

    public @Nullable Double getHealth() {
        return state.getPhaseHealth();
    }

    @Override
    public void cancelGrowth() {
        state.cancelGrowthTask();
    }

    public void mineBlock(@NotNull Player player, @NotNull Block block, int expToDrop) {
        if (!isValid()) {
            remove();
            return;
        }
        GeneratorUtil.callGeneratorMineEvent(this, state.getCurrentPhase(), player, block, expToDrop);
    }

    public void damage(@NotNull Player player, @NotNull Context context, double damageAmount) {
        Double health = state.getPhaseHealth();
        if (health == null) {
            return;
        }

        double newHealth = Math.max(0, health - damageAmount);
        if (newHealth > 0) {
            state.setHealth(newHealth);
            return;
        }

        GeneratorDestroyEvent event = new GeneratorDestroyEvent(player, origin, name, state.getCurrentPhase());
        Server.callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        GeneratorPhase phase = getPhase();
        Function onDestroy = phase.getDestroyFunction();
        if (onDestroy != null) {
            onDestroy.run(context);
        }

        harvest();
    }

    public void regrow() {
        setPhase(template.getMaxPhase(), true);
    }

    @Override
    public void grow() {
        grow(1);
    }

    @Override
    public void grow(int amount) {
        if (isFullyGrown()) {
            return;
        }

        GeneratorPhase currentPhase = getPhase();
        if (currentPhase.getGrowthTime() == 0) {
            return;
        }

        int currentPhaseIndex = state.getCurrentPhase();
        Integer nextPhaseIndex = getNextGrowthPhase(currentPhaseIndex + amount);
        if (nextPhaseIndex == null) {
            return;
        }

        GeneratorGrowthEvent growthEvent = new GeneratorGrowthEvent(origin, name, currentPhaseIndex);
        Server.callEvent(growthEvent);
        if (growthEvent.isCancelled()) {
            return;
        }

        setPhase(nextPhaseIndex, true);
    }

    public void harvest() {
        if (isDepleted()) {
            return;
        }

        int nextPhaseIndex = getNextHarvestingPhase(state.getCurrentPhase());
        setPhase(nextPhaseIndex, false);
    }

    @Override
    public void interact(@NotNull Player player, @NotNull Block block) {

    }

}
