package io.github.pigaut.rpg.plugin.task;

import org.bukkit.plugin.*;

public interface Task {

    void cancel();

    boolean isCancelled();

    Plugin getOwningPlugin();

}
