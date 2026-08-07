package io.github.pigaut.rpg.plugin.task.scheduler;

import com.google.common.base.*;
import io.github.pigaut.rpg.plugin.task.*;
import io.github.pigaut.rpg.util.reflection.*;
import io.papermc.paper.threadedregions.scheduler.*;
import org.bukkit.*;
import org.bukkit.plugin.*;
import org.jetbrains.annotations.*;

public class FoliaScheduler implements Scheduler {

    private final Plugin plugin;
    private final GlobalRegionScheduler regionScheduler;

    public FoliaScheduler(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.regionScheduler = Reflect.onClass(Bukkit.class)
                .call("getGlobalRegionScheduler")
                .get();
    }

    public void runTask(@NotNull Runnable runnable) {
        regionScheduler.execute(plugin, runnable);
    }

    @Override
    public void runTaskAsync(@NotNull Runnable runnable) {
        regionScheduler.execute(plugin, runnable);
    }

    public Task runTaskLater(long delay, @NotNull Runnable runnable) {
        if (delay < 1) {
            runTask(runnable);
            return null;
        }

        return new FoliaTaskWrapper(regionScheduler
                .runDelayed(plugin, t -> runnable.run(), delay));
    }

    @Override
    public @Nullable Task runTaskLaterAsync(long delay, @NotNull Runnable runnable) {
        if (delay < 1) {
            runTaskAsync(runnable);
            return null;
        }

        return new FoliaTaskWrapper(regionScheduler
                .runDelayed(plugin, t -> runnable.run(), delay));
    }

    public @NotNull Task runTaskTimer(long period, @NotNull Runnable runnable) {
        Preconditions.checkArgument(period > 0, "Period must be greater than 0");
        return new FoliaTaskWrapper(regionScheduler
                .runAtFixedRate(plugin, t -> runnable.run(), period, period));
    }

    @Override
    public @NotNull Task runTaskTimerAsync(long period, @NotNull Runnable runnable) {
        Preconditions.checkArgument(period > 0, "Period must be greater than 0");
        return new FoliaTaskWrapper(regionScheduler
                .runAtFixedRate(plugin, t -> runnable.run(), period, period));
    }

    public @NotNull Task runTaskTimer(long delay, long period, @NotNull Runnable runnable) {
        Preconditions.checkArgument(delay > 0, "Delay must be greater than 0");
        Preconditions.checkArgument(period > 0, "Period must be greater than 0");
        return new FoliaTaskWrapper(regionScheduler
                .runAtFixedRate(plugin, t -> runnable.run(), delay, period));
    }

    @Override
    public @NotNull Task runTaskTimerAsync(long delay, long period, @NotNull Runnable runnable) {
        Preconditions.checkArgument(delay > 0, "Delay must be greater than 0");
        Preconditions.checkArgument(period > 0, "Period must be greater than 0");
        return new FoliaTaskWrapper(regionScheduler
                .runAtFixedRate(plugin, t -> runnable.run(), delay, period));
    }

}
