package io.github.pigaut.rpg.plugin.task.scheduler;

import com.google.common.base.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.task.*;
import io.github.pigaut.rpg.util.reflection.*;
import io.papermc.paper.threadedregions.scheduler.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class FoliaRegionScheduler implements Scheduler {

    private final EnhancedPlugin plugin;
    private final Location location;
    private final RegionScheduler regionScheduler;

    public FoliaRegionScheduler(@NotNull EnhancedPlugin plugin, @NotNull Location location) {
        this.plugin = plugin;
        this.location = location;
        regionScheduler = Reflect.onClass(Bukkit.class)
                .call("getRegionScheduler")
                .get();
    }

    @Override
    public void runTask(@NotNull Runnable runnable) {
        regionScheduler.execute(plugin, location, runnable);
    }

    @Override
    public void runTaskAsync(@NotNull Runnable runnable) {
        regionScheduler.execute(plugin, location, runnable);
    }

    @Override
    public Task runTaskLater(long delay, @NotNull Runnable runnable) {
        if (delay < 1) {
            runTask(runnable);
            return null;
        }
        return new FoliaTaskWrapper(regionScheduler
                .runDelayed(plugin, location, t -> runnable.run(), delay));
    }

    @Override
    public @Nullable Task runTaskLaterAsync(long delay, @NotNull Runnable runnable) {
        if (delay < 1) {
            runTask(runnable);
            return null;
        }
        return new FoliaTaskWrapper(regionScheduler
                .runDelayed(plugin, location, t -> runnable.run(), delay));
    }

    @Override
    public @NotNull Task runTaskTimer(long period, @NotNull Runnable runnable) {
        Preconditions.checkArgument(period > 0, "Period must be greater than 0");
        return new FoliaTaskWrapper(regionScheduler
                .runAtFixedRate(plugin, location, t -> runnable.run(), period, period));
    }

    @Override
    public @NotNull Task runTaskTimerAsync(long period, @NotNull Runnable runnable) {
        Preconditions.checkArgument(period > 0, "Period must be greater than 0");
        return new FoliaTaskWrapper(regionScheduler
                .runAtFixedRate(plugin, location, t -> runnable.run(), period, period));
    }

    @Override
    public @NotNull Task runTaskTimer(long delay, long period, @NotNull Runnable runnable) {
        Preconditions.checkArgument(delay > 0, "Delay must be greater than 0");
        Preconditions.checkArgument(period > 0, "Period must be greater than 0");
        return new FoliaTaskWrapper(regionScheduler
                .runAtFixedRate(plugin, location, t -> runnable.run(), delay, period));
    }

    @Override
    public @NotNull Task runTaskTimerAsync(long delay, long period, @NotNull Runnable runnable) {
        Preconditions.checkArgument(delay > 0, "Delay must be greater than 0");
        Preconditions.checkArgument(period > 0, "Period must be greater than 0");
        return new FoliaTaskWrapper(regionScheduler
                .runAtFixedRate(plugin, location, t -> runnable.run(), delay, period));
    }

}
