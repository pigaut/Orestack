package io.github.pigaut.rpg.core.gameplay.chicken;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ChickenEggManager extends Manager {

    private final Set<UUID> chickenLaidEggs;
    private final int maxCapacity = 10000;

    public ChickenEggManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin);

        this.chickenLaidEggs = Collections.synchronizedSet(
            Collections.newSetFromMap(
                new LinkedHashMap<>(maxCapacity, 0.75f, false) {
                    @Override
                    protected boolean removeEldestEntry(Map.Entry<UUID, Boolean> eldest) {
                        return size() > maxCapacity;
                    }
                }
            )
        );
    }

    @Override
    public void enable() {
        Delay eggLayDelay = plugin.getSettings().getEggLayDelay();
        for (World world : Bukkit.getWorlds()) {
            for (Chicken chicken : world.getEntitiesByClass(Chicken.class)) {
                chicken.setEggLayTime(eggLayDelay.toTicks());
            }
        }
    }

    public boolean contains(@NotNull Item egg) {
        return chickenLaidEggs.contains(egg.getUniqueId());
    }

    public void add(@NotNull Item item) {
        chickenLaidEggs.add(item.getUniqueId());
    }

    public void remove(@NotNull Item item) {
        chickenLaidEggs.remove(item.getUniqueId());
    }

}