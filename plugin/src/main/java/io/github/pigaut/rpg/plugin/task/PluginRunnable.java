package io.github.pigaut.rpg.plugin.task;

import io.github.pigaut.rpg.plugin.*;
import org.bukkit.plugin.*;
import org.jetbrains.annotations.*;

public abstract class PluginRunnable implements Task, Runnable {

    protected final EnhancedPlugin plugin;
    private Task task;

    public PluginRunnable(@NotNull EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    public synchronized void cancel() throws IllegalStateException {
        if (task != null) {
            task.cancel();
        }
    }

    public boolean isScheduled() {
        return task != null && !task.isCancelled();
    }

    public synchronized boolean isCancelled() {
        return task == null || task.isCancelled();
    }

    @Override
    public Plugin getOwningPlugin() {
        return plugin;
    }

    public synchronized void runTask() throws IllegalStateException {
        checkNotYetScheduled();
        plugin.getScheduler().runTask(this);
    }

    public synchronized void runTaskAsync() throws IllegalStateException {
        checkNotYetScheduled();
        plugin.getScheduler().runTaskAsync(this);
    }

    public synchronized void runTaskLater(long delay) throws IllegalStateException {
        checkNotYetScheduled();
        task = plugin.getScheduler().runTaskLater(delay, this);
    }

    public synchronized void runTaskLaterAsync(long delay) throws IllegalStateException {
        checkNotYetScheduled();
        task = plugin.getScheduler().runTaskLaterAsync(delay, this);
    }

    public synchronized void runTaskTimer(long period) throws IllegalStateException {
        checkNotYetScheduled();
        task = plugin.getScheduler().runTaskTimer(period, this);
    }

    public synchronized void runTaskTimerAsync(long period) throws IllegalStateException {
        checkNotYetScheduled();
        task = plugin.getScheduler().runTaskTimerAsync(period, this);
    }

    public synchronized void runTaskTimer(long delay, long period) throws IllegalStateException {
        checkNotYetScheduled();
        task = plugin.getScheduler().runTaskTimer(delay, period, this);
    }

    public synchronized void runTaskTimerAsync(long delay, long period) throws IllegalStateException {
        checkNotYetScheduled();
        task = plugin.getScheduler().runTaskTimerAsync(delay, period, this);
    }

    private void checkNotYetScheduled() {
        if (task != null) {
            throw new IllegalStateException("Task is already scheduled");
        }
    }

}
