package io.github.pigaut.rpg.module.stat;

import io.github.pigaut.rpg.module.stat.tasks.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.plugin.registry.*;
import io.github.pigaut.yaml.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class StatManager extends Manager implements Registry<Stat> {

    private final Map<String, Stat> statsByName = new HashMap<>();

    private CombatStatusUpdateTask combatStatusUpdateTask;
    private StatusBarUpdateTask statusBarUpdateTask;
    private HealthRegenTask healthRegenTask;
    private ManaRegenTask manaRegenTask;

    public StatManager(EnhancedJavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void loadData() {
        for (Stat baseStat : BaseStats.values()) {
            register(baseStat.getName(), baseStat);
        }
        for (Stat customStat : plugin.getSettings().getCustomStats().keySet()) {
            register(customStat.getName(), customStat);
        }
    }

    @Override
    public void enable() {
        Settings settings = plugin.getSettings();
        if (!settings.isStats()) {
            return;
        }

        combatStatusUpdateTask = new CombatStatusUpdateTask(plugin);
        healthRegenTask = new HealthRegenTask(plugin);
        manaRegenTask = new ManaRegenTask(plugin);

        combatStatusUpdateTask.start();
        healthRegenTask.start();
        manaRegenTask.start();

        if (plugin.getSettings().isShowStatusBar()) {
            for (PlayerState playerState : plugin.getPlayerStates().getAll()) {
                playerState.reloadStats();
                playerState.updateStatusBar();
            }
            statusBarUpdateTask = new StatusBarUpdateTask(plugin);
            statusBarUpdateTask.start();
        }
        else {
            for (PlayerState playerState : plugin.getPlayerStates().getAll()) {
                playerState.reloadStats();
            }
        }
    }

    @Override
    public void disable() {
        if (combatStatusUpdateTask != null) {
            combatStatusUpdateTask.cancel();
            combatStatusUpdateTask = null;
        }

        if (statusBarUpdateTask != null) {
            statusBarUpdateTask.cancel();
            statusBarUpdateTask = null;
        }

        if (healthRegenTask != null) {
            healthRegenTask.cancel();
            healthRegenTask = null;
        }

        if (manaRegenTask != null) {
            manaRegenTask.cancel();
            manaRegenTask = null;
        }
    }

    @Override
    public boolean contains(@NotNull String name) {
        return statsByName.containsKey(name);
    }

    @Override
    public @Nullable Stat get(@NotNull String name) {
        return statsByName.get(name);
    }

    @Override
    public void register(@NotNull String name, @NotNull Stat stat) {
        Preconditions.checkArgument(!contains(name), "Stat is already registered");
        Preconditions.checkArgument(name.equals(stat.getName()), "Registry name does not match stat name");
        statsByName.put(name, stat);
    }

    @Override
    public void unregister(@NotNull String name) {
        statsByName.remove(name);
    }

    public @NotNull Collection<Stat> getAll() {
        return new ArrayList<>(statsByName.values());
    }

    @Override
    public @NotNull Collection<String> getAllNames() {
        return new ArrayList<>(statsByName.keySet());
    }

    @Override
    public void clear() {
        statsByName.clear();
    }

}
