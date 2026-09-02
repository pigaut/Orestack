package io.github.pigaut.rpg.module.function.condition.player;

import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PlayerHasPermission implements PlayerCondition.Predicate {

    private final List<String> permissions;

    public PlayerHasPermission(String permission) {
        this.permissions = List.of(permission);
    }

    public PlayerHasPermission(List<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean test(@NotNull Player player) {
        for (String permission : permissions) {
            if (!player.hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }

}
