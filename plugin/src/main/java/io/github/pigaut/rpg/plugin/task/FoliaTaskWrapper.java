package io.github.pigaut.rpg.plugin.task;

import io.papermc.paper.threadedregions.scheduler.*;
import org.bukkit.plugin.*;

public class FoliaTaskWrapper implements Task {

    private final ScheduledTask task;

    public FoliaTaskWrapper(ScheduledTask task) {
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
        return task.getOwningPlugin();
    }

}
