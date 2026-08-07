package io.github.pigaut.rpg.plugin.boot;

import io.github.pigaut.rpg.plugin.boot.phase.*;
import io.github.pigaut.rpg.server.*;
import io.github.pigaut.yaml.util.*;
import org.bukkit.plugin.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class StartupTask {

    private Runnable task = () -> {};
    private final Set<BootPhase> taskRequirements = new HashSet<>();
    private boolean init = false;
    private boolean executed = false;

    public static StartupTask create() {
        return new StartupTask();
    }

    public StartupTask onReady(@NotNull Runnable task) {
        Preconditions.checkState(!init, "Cannot change the runnable after task has been initialized.");
        this.task = task;
        return this;
    }

    public StartupTask require(@NotNull BootPhase taskRequirement) {
        Preconditions.checkState(!init, "Cannot add requirement after task has been initialized.");
        taskRequirements.add(taskRequirement);
        return this;
    }

    public void init() {
        Preconditions.checkState(!init, "Task has already been initialized.");
        init = true;

        for (BootPhase bootPhase : taskRequirements) {
            if (bootPhase instanceof PluginBootPhase) {
                Plugin plugin = Server.getPlugin(bootPhase.getNamespace());
                if (plugin == null) {
                    taskRequirements.remove(bootPhase);
                    continue;
                }

                String pluginPhase = bootPhase.getKey();
                if (pluginPhase == null || (pluginPhase.equals("enabled") && plugin.isEnabled())) {
                    taskRequirements.remove(bootPhase);
                }
            }
        }

        if (taskRequirements.isEmpty()) {
            executed = true;
            task.run();
        }
    }

    public void markReady(@NotNull BootPhase bootPhase) {
        if (executed) {
            return;
        }
        taskRequirements.remove(bootPhase);
        if (taskRequirements.isEmpty()) {
            executed = true;
            task.run();
        }
    }

}
