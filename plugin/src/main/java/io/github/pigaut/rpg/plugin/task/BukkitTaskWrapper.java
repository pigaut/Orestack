package io.github.pigaut.rpg.plugin.task;

import org.bukkit.plugin.*;
import org.bukkit.scheduler.*;
import org.jetbrains.annotations.*;

public class BukkitTaskWrapper implements Task {

    private final BukkitTask task;

    public BukkitTaskWrapper(@NotNull BukkitTask task) {
        this.task = task;
    }

    @Override
    public void cancel() {
        task.cancel();
    }

    @Override
    public boolean isCancelled() {
        return task.isCancelled();
    }

    @Override
    public Plugin getOwningPlugin() {
        return task.getOwner();
    }

}
