package io.github.pigaut.rpg.player.data;

import io.github.pigaut.rpg.player.data.base.*;
import org.jetbrains.annotations.*;

public interface PlayerDataRepository<T extends EnhancedPlayerData> {

    void loadData(@NotNull T playerData);

    void saveData(@NotNull T playerData);

    void clearData(@NotNull T playerData);

}
