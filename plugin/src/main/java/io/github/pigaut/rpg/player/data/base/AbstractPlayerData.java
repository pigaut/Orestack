package io.github.pigaut.rpg.player.data.base;

import org.jetbrains.annotations.*;

import java.util.*;

public abstract class AbstractPlayerData implements EnhancedPlayerData {

    private final UUID playerId;

    private boolean loaded = false;
    private final List<Runnable> pendingTasks = new ArrayList<>();

    public AbstractPlayerData(@NotNull UUID playerId) {
        this.playerId = playerId;
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return playerId;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
        if (loaded) {
            for (Runnable pendingTask : pendingTasks) {
                pendingTask.run();
            }
            pendingTasks.clear();
        }
    }

    @Override
    public void runWhenLoaded(@NotNull Runnable task) {
        if (loaded) {
            task.run();
        } else {
            pendingTasks.add(task);
        }
    }

}
