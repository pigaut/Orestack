package io.github.pigaut.rpg.plugin.manager;

import io.github.pigaut.rpg.plugin.*;

public abstract class Manager {

    protected final EnhancedJavaPlugin plugin;
    protected final PluginLogger logger;

    protected Manager(EnhancedJavaPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getColoredLogger();
    }

    public boolean isAutoSave() {
        return false;
    }

    public void clear() {

    }

    public void loadData() {

    }

    public void enable() {

    }

    public void disable() {

    }

    public void saveData() {

    }

    public void reload() {
        disable();
        plugin.getScheduler().runTaskAsync(() -> {
            saveData();
            loadData();
            plugin.getScheduler().runTask(this::enable);
        });
    }

}
