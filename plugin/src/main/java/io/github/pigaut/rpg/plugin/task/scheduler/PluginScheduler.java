package io.github.pigaut.rpg.plugin.task.scheduler;

import com.google.common.base.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.task.*;
import io.papermc.paper.threadedregions.scheduler.*;
import org.bukkit.*;
import org.bukkit.scheduler.*;
import org.jetbrains.annotations.*;

public class PluginScheduler implements Scheduler {

    private final EnhancedPlugin plugin;
    private final BukkitScheduler bukkitScheduler;

    public PluginScheduler(@NotNull EnhancedPlugin plugin) {
        this.plugin = plugin;
        this.bukkitScheduler = Bukkit.getScheduler();
    }

    public void runTask(@NotNull Runnable runnable) {
        bukkitScheduler.runTask(plugin, runnable);
    }

    public void runTaskAsync(@NotNull Runnable runnable) {
        bukkitScheduler.runTaskAsynchronously(plugin, runnable);
    }

    public @Nullable Task runTaskLater(long delay, @NotNull Runnable runnable) {
        if (delay < 1) {
            runTask(runnable);
            return null;
        }

        return new BukkitTaskWrapper(bukkitScheduler.runTaskLater(plugin, runnable, delay));
    }

    public @Nullable Task runTaskLaterAsync(long delay, @NotNull Runnable runnable) {
        if (delay < 1) {
            runTaskAsync(runnable);
            return null;
        }
        return new BukkitTaskWrapper(bukkitScheduler.runTaskLaterAsynchronously(plugin, runnable, delay));
    }

    public @NotNull Task runTaskTimer(long period, @NotNull Runnable runnable) {
        Preconditions.checkArgument(period > 0, "Period must be greater than 0");
        return new BukkitTaskWrapper(bukkitScheduler.runTaskTimer(plugin, runnable, period, period));
    }

    public @NotNull Task runTaskTimerAsync(long period, @NotNull Runnable runnable) {
        Preconditions.checkArgument(period > 0, "Period must be greater than 0");
        return new BukkitTaskWrapper(bukkitScheduler.runTaskTimerAsynchronously(plugin, runnable, period, period));
    }

    public @NotNull Task runTaskTimer(long delay, long period, @NotNull Runnable runnable) {
        Preconditions.checkArgument(delay > 0, "Delay must be greater than 0");
        Preconditions.checkArgument(period > 0, "Period must be greater than 0");
        return new BukkitTaskWrapper(bukkitScheduler.runTaskTimer(plugin, runnable, delay, period));
    }

    public @NotNull Task runTaskTimerAsync(long delay, long period, @NotNull Runnable runnable) {
        Preconditions.checkArgument(delay > 0, "Delay must be greater than 0");
        Preconditions.checkArgument(period > 0, "Period must be greater than 0");
        return new BukkitTaskWrapper(bukkitScheduler.runTaskTimerAsynchronously(plugin, runnable, delay, period));
    }

}
