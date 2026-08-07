package io.github.pigaut.rpg.player.state;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PlayerStateManager<T extends PlayerState> extends Manager {

    private final PlayerStateFactory<T> playerStateFactory;
    private final Map<UUID, T> playerStateByUUID = new HashMap<>();
    private final Map<UUID, T> cachedPlayerStates = new HashMap<>();

    public PlayerStateManager(@NotNull EnhancedJavaPlugin plugin, @NotNull PlayerStateFactory<T> playerStateFactory) {
        super(plugin);
        this.playerStateFactory = playerStateFactory;
    }

    public @Nullable T getPlayerState(@NotNull String name) {
        Player player = Bukkit.getPlayer(name);
        return player != null ? getPlayerState(player) : null;
    }

    public @Nullable T getPlayerState(@NotNull UUID playerId) {
        return playerStateByUUID.get(playerId);
    }

    public @NotNull T getPlayerState(@NotNull Player player) {
        UUID playerId = player.getUniqueId();
        if (playerStateByUUID.containsKey(playerId)) {
            return playerStateByUUID.get(playerId);
        }

        T playerState = playerStateFactory.create(player);
        playerStateByUUID.put(playerState.getUniqueId(), playerState);
        return playerState;
    }

    public List<T> getAllPlayerStates() {
        return new ArrayList<>(playerStateByUUID.values());
    }

    @Override
    public void enable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            registerPlayer(player);
        }
        cachedPlayerStates.clear();
    }

    @Override
    public void disable() {
        cachedPlayerStates.putAll(playerStateByUUID);
        playerStateByUUID.clear();
    }

    public void registerPlayer(@NotNull Player player) {
        UUID playerId = player.getUniqueId();

        T cachedState = cachedPlayerStates.get(playerId);
        if (cachedState != null) {
            cachedPlayerStates.remove(playerId);
            playerStateByUUID.put(playerId, cachedState);
            return;
        }

        plugin.runWhenReady(() -> {
            if (playerStateByUUID.containsKey(playerId)) {
                return;
            }
            T newState = playerStateFactory.create(player);
            playerStateByUUID.put(playerId, newState);
        });
    }

    public void unregisterPlayer(@NotNull Player player) {
        UUID playerId = player.getUniqueId();
        T playerState = playerStateByUUID.remove(playerId);
        if (playerState != null) {
            cachedPlayerStates.put(playerId, playerState);
            int playerCacheDuration = plugin.getSettings().getPlayerCacheDuration().toTicks();
            plugin.getScheduler().runTaskLater(playerCacheDuration, () -> {
                cachedPlayerStates.remove(playerId);
            });
        }
    }

}
