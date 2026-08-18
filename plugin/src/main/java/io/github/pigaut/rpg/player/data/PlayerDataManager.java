package io.github.pigaut.rpg.player.data;

import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.*;

public class PlayerDataManager<T extends PlayerData> extends Manager {

    private final PlayerDataFactory<T> playerDataFactory;
    private final List<PlayerDataRepository<T>> playerDataRepositories = new ArrayList<>();

    private final Map<UUID, T> playerDataByUUID = new ConcurrentHashMap<>();

    public PlayerDataManager(@NotNull EnhancedJavaPlugin plugin, @NotNull PlayerDataFactory<T> playerDataFactory) {
        super(plugin);
        this.playerDataFactory = playerDataFactory;
    }

    public @Nullable T get(@NotNull String name) {
        Player player = Bukkit.getPlayer(name);
        return player != null ? get(player) : null;
    }

    public @Nullable T get(@NotNull UUID playerId) {
        Player player = Bukkit.getPlayer(playerId);
        return player != null ? get(player) : null;
    }

    @SuppressWarnings("unchecked")
    public @NotNull T get(@NotNull Player player) {
        UUID playerId = player.getUniqueId();
        if (playerDataByUUID.containsKey(playerId)) {
            return playerDataByUUID.get(playerId);
        }
        PlayerState playerState = plugin.getPlayerState(player);
        return (T) playerState.getPlayerData();
    }

    public @NotNull T load(@NotNull Player player) {
        UUID playerId = player.getUniqueId();
        if (playerDataByUUID.containsKey(playerId)) {
            return playerDataByUUID.get(playerId);
        }

        T playerData = playerDataFactory.create(player);
        playerDataByUUID.put(playerId, playerData);

        if (playerDataRepositories.isEmpty()) {
            playerData.setLoaded(true);
            return playerData;
        }

        plugin.runWhenReadyAsync(() -> {
            if (playerData.isLoaded()) {
                return;
            }
            for (PlayerDataRepository<T> dataRepository : playerDataRepositories) {
                dataRepository.loadData(playerData);
            }
            plugin.getScheduler().runTask(() -> {
                playerData.setLoaded(true);
                plugin.getColoredLogger().info("Loaded all player data for: " + player.getName());
            });
        });

        return playerData;
    }

    public void unload(@NotNull UUID playerId) {
        T playerData = get(playerId);
        if (playerData != null) {
            unload(playerData);
        }
    }

    public void unload(@NotNull T playerData) {
        if (playerData.isLoaded()) {
            for (PlayerDataRepository<T> dataRepository : playerDataRepositories) {
                dataRepository.saveData(playerData);
                dataRepository.clearData(playerData);
            }
            playerData.setLoaded(false);
        }
    }

    public void destroy(@NotNull UUID playerId) {
        T playerData = playerDataByUUID.get(playerId);
        if (playerData == null) {
            return;
        }

        PlayerState playerState = plugin.getPlayerState(playerId);
        if (playerState != null && playerState.getPlayerData() == playerData) {
            throw new IllegalStateException("Cannot destroy player data because owning player state still exists");
        }

        playerDataByUUID.remove(playerId);
        plugin.getScheduler().runTaskAsync(() -> unload(playerData));
    }

    public void save(@NotNull T playerData) {
        if (playerData.isLoaded()) {
            for (PlayerDataRepository<T> dataRepository : playerDataRepositories) {
                dataRepository.saveData(playerData);
            }
        }
    }

    @Override
    public boolean isAutoSave() {
        return true;
    }

    @Override
    public void loadData() {
        for (T playerData : playerDataByUUID.values()) {
            if (playerData.isLoaded()) {
                continue;
            }

            if (playerDataRepositories.isEmpty()) {
                playerData.setLoaded(true);
                continue;
            }

            for (PlayerDataRepository<T> dataRepository : playerDataRepositories) {
                dataRepository.loadData(playerData);
            }

            plugin.getScheduler().runTask(() -> playerData.setLoaded(true));
        }
    }

    @Override
    public void saveData() {
        for (T playerData : playerDataByUUID.values()) {
            unload(playerData);
        }
    }

    public void addDataRepository(@NotNull PlayerDataRepository<T> dataRepository) {
        playerDataRepositories.add(dataRepository);
    }

    public void removeDataRepository(@NotNull PlayerDataRepository<T> dataRepository) {
        playerDataRepositories.remove(dataRepository);
    }

}
