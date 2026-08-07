package io.github.pigaut.rpg.module.function.condition.player;

import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PlayerHasPermission implements PlayerCondition {

    private final List<String> permissions;

    public PlayerHasPermission(String permission) {
        this.permissions = List.of(permission);
    }

    public PlayerHasPermission(List<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public Boolean evaluate(@NotNull Player player) {
        for (String permission : permissions) {
            if (!player.hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }

}
