package io.github.pigaut.rpg.module.gate;

import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.structure.*;
import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.structure.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GatePhase {

    private final StructureTemplate structureTemplate;
    private final List<Material> decorativeBlocks;
    private final int openingDelay;
    private final int closingDelay;
    private final Double maxHealth;
    private final Delay clickCooldown;
    private final @Nullable HologramTemplate openingHologram;
    private final @Nullable HologramTemplate closingHologram;
    private final @Nullable Function onBreak;
    private final @Nullable Function onTransition;
    private final @Nullable Function onOpening;
    private final @Nullable Function onClosing;
    private final @Nullable Function onClick;
    private final @Nullable Function onLeftClick;
    private final @Nullable Function onRightClick;
    private final @Nullable Function onDestroy;

    public GatePhase(@NotNull StructureTemplate structureTemplate, List<Material> decorativeBlocks,
                     int openingDelay, int closingDelay, Double health, Delay clickCooldown,
                     @Nullable HologramTemplate openingHologram, @Nullable HologramTemplate closingHologram, @Nullable Function onBreak,
                     @Nullable Function onTransition, @Nullable Function onOpening, @Nullable Function onClosing,
                     @Nullable Function onClick, @Nullable Function onLeftClick, @Nullable Function onRightClick,
                     @Nullable Function onDestroy) {
        this.structureTemplate = structureTemplate;
        this.decorativeBlocks = decorativeBlocks;
        this.openingDelay = openingDelay;
        this.closingDelay = closingDelay;
        this.maxHealth = health;
        this.clickCooldown = clickCooldown;
        this.closingHologram = closingHologram;
        this.onBreak = onBreak;
        this.onTransition = onTransition;
        this.onOpening = onOpening;
        this.onClosing = onClosing;
        this.onClick = onClick;
        this.onLeftClick = onLeftClick;
        this.onRightClick = onRightClick;
        this.openingHologram = openingHologram;
        this.onDestroy = onDestroy;
    }

    public @NotNull StructureTemplate getStructureTemplate() {
        return structureTemplate;
    }

    public List<Material> getDecorativeBlocks() {
        return new ArrayList<>(decorativeBlocks);
    }

    public @NotNull Delay getClickCooldown() {
        return clickCooldown;
    }

    public int getOpeningDelay() {
        return openingDelay;
    }

    public int getClosingDelay() {
        return closingDelay;
    }

    public @Nullable Function getBreakFunction() {
        return onBreak;
    }

    public @Nullable HologramTemplate getOpeningHologram() {
        return openingHologram;
    }

    public @Nullable HologramTemplate getClosingHologram() {
        return closingHologram;
    }

    public @Nullable Function getTransitionFunction() {
        return onTransition;
    }

    public @Nullable Function getOpeningFunction() {
        return onOpening;
    }

    public @Nullable Function getClosingFunction() {
        return onClosing;
    }

    public @Nullable Function getClickFunction() {
        return onClick;
    }

    public @Nullable Function getLeftClickFunction() {
        return onLeftClick;
    }

    public @Nullable Function getRightClickFunction() {
        return onRightClick;
    }

    public Function getDestroyFunction() {
        return onDestroy;
    }

    public @Nullable Double getMaxHealth() {
        return maxHealth;
    }

    @Override
    public String toString() {
        return "GatePhase{" +
                "structureTemplate=" + structureTemplate +
                ", decorativeBlocks=" + decorativeBlocks +
                ", openingDelay=" + openingDelay +
                ", closingDelay=" + closingDelay +
                ", health=" + maxHealth +
                ", clickCooldown=" + clickCooldown +
                ", openingHologram=" + openingHologram +
                ", closingHologram=" + closingHologram +
                ", onBreak=" + onBreak +
                ", onTransition=" + onTransition +
                ", onOpening=" + onOpening +
                ", onClosing=" + onClosing +
                ", onClick=" + onClick +
                ", onLeftClick=" + onLeftClick +
                ", onRightClick=" + onRightClick +
                '}';
    }

}
