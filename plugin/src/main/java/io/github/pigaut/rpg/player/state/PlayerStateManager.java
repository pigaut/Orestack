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

    public @Nullable T get(@NotNull String name) {
        Player player = Bukkit.getPlayer(name);
        return player != null ? get(player) : null;
    }

    public @Nullable T get(@NotNull UUID playerId) {
        return playerStateByUUID.get(playerId);
    }

    public @NotNull T get(@NotNull Player player) {
        UUID playerId = player.getUniqueId();
        if (playerStateByUUID.containsKey(playerId)) {
            return playerStateByUUID.get(playerId);
        }

        T playerState = playerStateFactory.create(player);
        playerStateByUUID.put(playerState.getUniqueId(), playerState);
        return playerState;
    }

    public List<T> getAll() {
        return new ArrayList<>(playerStateByUUID.values());
    }

    @Override
    public void enable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            register(player);
        }
        cachedPlayerStates.clear();
    }

    @Override
    public void disable() {
        cachedPlayerStates.putAll(playerStateByUUID);
        playerStateByUUID.clear();
    }

    public void register(@NotNull Player player) {
        UUID playerId = player.getUniqueId();

        T cachedState = cachedPlayerStates.remove(playerId);
        if (cachedState != null) {
            playerStateByUUID.put(playerId, cachedState);
            return;
        }

        plugin.runWhenReady(() -> {
            if (!playerStateByUUID.containsKey(playerId)) {
                T newState = playerStateFactory.create(player);
                playerStateByUUID.put(playerId, newState);
            }
        });
    }

    public void unregister(@NotNull Player player) {
        UUID playerId = player.getUniqueId();
        T playerState = playerStateByUUID.remove(playerId);
        if (playerState != null) {
            cachedPlayerStates.put(playerId, playerState);
            int playerCacheDuration = plugin.getSettings().getPlayerCacheDuration().toTicks();
            plugin.getScheduler().runTaskLater(playerCacheDuration, () -> {
                if (playerStateByUUID.containsKey(playerId)) {
                    return;
                }
                cachedPlayerStates.remove(playerId);
                plugin.getPlayerData().unload(playerId);
            });
        }
    }

}
