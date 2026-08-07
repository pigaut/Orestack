package io.github.pigaut.rpg.player.data;

import org.jetbrains.annotations.*;

public interface PlayerDataRepository<T extends PlayerData> {

    void loadData(@NotNull T playerData);

    void saveData(@NotNull T playerData);

}
