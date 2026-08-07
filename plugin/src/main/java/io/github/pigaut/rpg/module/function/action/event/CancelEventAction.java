package io.github.pigaut.rpg.module.function.action.event;

import org.bukkit.event.*;
import org.jetbrains.annotations.*;

public class CancelEventAction implements EventAction {

    private final boolean cancel;

    public CancelEventAction(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public void execute(@NotNull Event event) {
        if (event instanceof Cancellable cancellable) {
            cancellable.setCancelled(cancel);
        }
    }

}
