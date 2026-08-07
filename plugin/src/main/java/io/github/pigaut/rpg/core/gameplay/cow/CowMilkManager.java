package io.github.pigaut.rpg.core.gameplay.cow;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.plugin.task.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.plugin.task.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CowMilkManager extends Manager {

    private final Map<UUID, CowMilk> milkLeftByCow = new HashMap<>();
    private Task milkRegenTask;

    public CowMilkManager(EnhancedJavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void enable() {
        milkRegenTask = plugin.getScheduler().runTaskTimer(200, () -> {
            for (CowMilk cowMilk : milkLeftByCow.values()) {
                regenerate(cowMilk);
            }
        });
    }

    @Override
    public void disable() {
        if (milkRegenTask != null) {
            milkRegenTask.cancel();
        }
        milkLeftByCow.clear();
    }

    public @NotNull CowMilk getCowMilk(@NotNull Cow cow) {
        return milkLeftByCow.computeIfAbsent(cow.getUniqueId(),
                uuid -> new CowMilk(plugin.getSettings().getCowMaxMilk()));
    }

    public boolean hasMilkLeft(@NotNull Cow cow) {
        if (plugin.getSettings().isCustomCowMilking()) {
            return getCowMilk(cow).getMilk() > 0;
        }
        return true;
    }

    public void milk(@NotNull Cow cow) {
        CowMilk cowMilk = getCowMilk(cow);

        int milkAvailable = cowMilk.getMilk();
        if (milkAvailable <= 0) {
            return;
        }

        cowMilk.setMilk(milkAvailable - 1);
        cowMilk.setLastMilkChange(System.currentTimeMillis());
    }

    private void regenerate(@NotNull CowMilk cowMilk) {
        int milk = cowMilk.getMilk();
        int maxMilk = plugin.getSettings().getCowMaxMilk();
        if (milk >= maxMilk) {
            return;
        }

        Long lastMilkProduced = cowMilk.getLastMilkChange();
        if (lastMilkProduced == null) {
            cowMilk.setMilk(maxMilk);
            return;
        }

        long regenDelayMillis = plugin.getSettings().getMilkProduceDelay().toMillis();
        long elapsed = System.currentTimeMillis() - lastMilkProduced;

        int milkToRegen = plugin.getSettings().getMilkProduceAmount().intValue();
        if (elapsed >= regenDelayMillis) {
            cowMilk.setMilk(Math.min(maxMilk, milk + milkToRegen));
            cowMilk.setLastMilkChange(System.currentTimeMillis());
        }
    }

    public void remove(@NotNull Cow cow) {
        milkLeftByCow.remove(cow.getUniqueId());
    }

}
