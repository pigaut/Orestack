package io.github.pigaut.rpg.hook;

import me.clip.placeholderapi.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class PlaceholdersHook {

    public String setPlaceholders(@Nullable OfflinePlayer offlinePlayer, @NotNull String value) {
        return PlaceholderAPI.setPlaceholders(offlinePlayer, value);
    }

}
