package io.github.pigaut.rpg.core.gameplay.brew;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BrewedPotionManager extends Manager {

    private final Set<BrewingSlot> brewedSlots;
    private final int maxCapacity = 5000;

    public BrewedPotionManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin);
        Map<BrewingSlot, Boolean> map = new LinkedHashMap<>(maxCapacity, 0.75f, false) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<BrewingSlot, Boolean> eldest) {
                return size() > maxCapacity;
            }
        };
        this.brewedSlots = Collections.synchronizedSet(Collections.newSetFromMap(map));
    }

    public boolean isBrewedSlot(@NotNull Block brewingStand, int slot) {
        return brewedSlots.contains(new BrewingSlot(BlockPosition.fromBlock(brewingStand), slot));
    }

    public void add(@NotNull Block brewingStand, int slot) {
        brewedSlots.add(new BrewingSlot(BlockPosition.fromBlock(brewingStand), slot));
    }

    public void remove(@NotNull Block brewingStand, int slot) {
        brewedSlots.remove(new BrewingSlot(BlockPosition.fromBlock(brewingStand), slot));
    }

    public void removeAll(@NotNull Block brewingStand) {
        BlockPosition pos = BlockPosition.fromBlock(brewingStand);
        brewedSlots.removeIf(slot -> slot.position().equals(pos));
    }

    private record BrewingSlot(@NotNull BlockPosition position, int slot) {
    }
}