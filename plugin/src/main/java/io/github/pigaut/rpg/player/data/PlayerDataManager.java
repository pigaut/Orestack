package io.github.pigaut.rpg.player.data;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.*;

public class PlayerDataManager<T extends PlayerData> extends Manager {

    private final PlayerDataFactory<T> playerDataFactory;

    private final Map<UUID, T> playerDataByUUID = new ConcurrentHashMap<>();
    private final Map<UUID, T> cachedPlayerData = new ConcurrentHashMap<>();
    private final List<PlayerDataRepository<T>> playerDataRepositories = new ArrayList<>();

    public PlayerDataManager(@NotNull EnhancedJavaPlugin plugin, @NotNull PlayerDataFactory<T> playerDataFactory) {
        super(plugin);
        this.playerDataFactory = playerDataFactory;
    }

    public @Nullable T getPlayerData(@NotNull String name) {
        Player player = Bukkit.getPlayer(name);
        return player != null ? getPlayerData(player) : null;
    }

    public @Nullable T getPlayerData(@NotNull UUID playerId) {
        return playerDataByUUID.get(playerId);
    }

    public @NotNull T getPlayerData(@NotNull Player player) {
        UUID playerId = player.getUniqueId();

        T playerData = playerDataByUUID.get(playerId);
        if (playerData != null) {
            return playerData;
        }

        playerData = cachedPlayerData.remove(playerId);
        if (playerData == null) {
            playerData = playerDataFactory.create(player);
            playerDataByUUID.put(playerId, playerData);

            if (playerDataRepositories.isEmpty()) {
                playerData.setLoaded(true);
                return playerData;
            }

            T finalPlayerData = playerData;
            plugin.runWhenReadyAsync(() -> {
                if (finalPlayerData.isLoaded()) {
                    return;
                }
                for (PlayerDataRepository<T> dataRepository : playerDataRepositories) {
                    dataRepository.loadData(finalPlayerData);
                }
                plugin.getScheduler().runTask(() -> {
                    finalPlayerData.setLoaded(true);
                    plugin.getColoredLogger().info("Loaded all player data for: " + player.getName());
                });
            });
        }

        return playerData;
    }

    public List<T> getAll() {
        return new ArrayList<>(playerDataByUUID.values());
    }

    @Override
    public boolean isAutoSave() {
        return true;
    }

    @Override
    public void clear() {
        playerDataByUUID.clear();
        cachedPlayerData.clear();
    }

    @Override
    public void loadData() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            T playerData = playerDataFactory.create(player);
            playerDataByUUID.put(player.getUniqueId(), playerData);

            if (playerDataRepositories.isEmpty()) {
                playerData.setLoaded(true);
                continue;
            }

            plugin.runWhenReady(() -> {
                if (playerData.isLoaded()) {
                    return;
                }
                for (PlayerDataRepository<T> dataRepository : playerDataRepositories) {
                    dataRepository.loadData(playerData);
                }
                plugin.getScheduler().runTask(() -> {
                    playerData.setLoaded(true);
                });
            });
        }
    }

    @Override
    public void enable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            registerPlayer(player);
        }
    }

    @Override
    public void saveData() {
        for (T playerData : playerDataByUUID.values()) {
            if (playerData.isLoaded()) {
                for (PlayerDataRepository<T> dataRepository : playerDataRepositories) {
                    dataRepository.saveData(playerData);
                }
            }
        }
        for (T playerData : cachedPlayerData.values()) {
            if (playerData.isLoaded()) {
                for (PlayerDataRepository<T> dataRepository : playerDataRepositories) {
                    dataRepository.saveData(playerData);
                }
            }
        }
    }

    public void addDataRepository(@NotNull PlayerDataRepository<T> dataRepository) {
        playerDataRepositories.add(dataRepository);
    }

    public void removeDataRepository(@NotNull PlayerDataRepository<T> dataRepository) {
        playerDataRepositories.remove(dataRepository);
    }

    public void registerPlayer(@NotNull Player player) {
        getPlayerData(player);
    }

    public void unregisterPlayer(@NotNull Player player) {
        UUID playerId = player.getUniqueId();
        T playerData = playerDataByUUID.remove(playerId);

        if (playerData == null) {
            playerData = cachedPlayerData.get(playerId);
        }

        if (playerData != null) {
            if (playerData.isLoaded()) {
                T finalPlayerData = playerData;
                plugin.getScheduler().runTaskAsync(() -> {
                    for (PlayerDataRepository<T> repo : playerDataRepositories) {
                        repo.saveData(finalPlayerData);
                    }
                });
            }

            cachedPlayerData.put(playerId, playerData);
            int playerCacheDuration = plugin.getSettings().getPlayerCacheDuration().toTicks();
            plugin.getScheduler().runTaskLater(playerCacheDuration, () -> {
                cachedPlayerData.remove(playerId);
            });
        }
    }
}
