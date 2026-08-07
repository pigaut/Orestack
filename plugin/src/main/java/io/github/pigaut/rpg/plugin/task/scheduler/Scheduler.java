package io.github.pigaut.rpg.plugin.task.scheduler;

import io.github.pigaut.rpg.plugin.task.*;
import org.jetbrains.annotations.*;

public interface Scheduler {

    void runTask(@NotNull Runnable runnable);

    void runTaskAsync(@NotNull Runnable runnable);

    @Nullable
    Task runTaskLater(long delay, @NotNull Runnable runnable);

    @Nullable
    Task runTaskLaterAsync(long delay, @NotNull Runnable runnable);

    @NotNull
    Task runTaskTimer(long period, @NotNull Runnable runnable);

    @NotNull
    Task runTaskTimerAsync(long period, @NotNull Runnable runnable);

    @NotNull
    Task runTaskTimer(long delay, long period, @NotNull Runnable runnable);

    @NotNull
    Task runTaskTimerAsync(long delay, long period, @NotNull Runnable runnable);

}
