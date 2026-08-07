package io.github.pigaut.rpg.player.input;

import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class ChatInput<T> extends GenericInputCollector<T> {

    private final EnhancedPlugin plugin;
    private final PlayerState playerState;
    private String description = "Enter a value in chat";

    private MenuView previousView;

    public ChatInput(@NotNull EnhancedPlugin plugin, @NotNull PlayerState player, @NotNull Parser<T> parser) {
        super(player, parser);
        this.plugin = plugin;
        this.playerState = player;
    }

    @Override
    public @NotNull InputSource getInputSource() {
        return InputSource.CHAT;
    }

    @Override
    public void onStart() {
        previousView = playerState.getOpenMenu();
        if (previousView != null) {
            previousView.close();
        }
        Player player = playerState.asPlayer();
        player.sendTitle(description, "Type ESC to cancel", 10, Short.MAX_VALUE, 0);
    }

    @Override
    public void onSuccess(@NotNull T input) {
        playerState.asPlayer().resetTitle();
        if (previousView != null && playerState.getOpenMenu() == null) {
            previousView.open();
        }
    }

    @Override
    public void onCancel() {
        playerState.asPlayer().resetTitle();
        if (previousView != null) {
            previousView.open();
        }
    }

    @Override
    public void onInputRejected(@NotNull String errorMessage) {
        Player player = playerState.asPlayer();
        player.sendTitle(ChatColor.RED + errorMessage, "Type ESC to cancel", 10, 70, 0);
        plugin.getScheduler().runTaskLater(60, () -> {
            if (isCollecting()) {
                player.sendTitle(description, "Type ESC to cancel", 10, Short.MAX_VALUE, 0);
            }
        });
    }

    public ChatInput<T> description(@NotNull String description) {
        this.description = description;
        return this;
    }

    public ChatInput<T> onInput(@NotNull Consumer<T> inputCollector) {
        return (ChatInput<T>) super.onInput(inputCollector);
    }

    public ChatInput<T> onCancel(@NotNull Runnable cancelAction) {
        return (ChatInput<T>) super.onCancel(cancelAction);
    }

}
