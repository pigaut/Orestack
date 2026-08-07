package io.github.pigaut.rpg.player;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class PlayerPlaceholders {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        PlaceholderRegistry placeholders = plugin.getPlaceholders();

        // Player placeholders
        placeholders.register("player", context -> {
            Player player = context.player();
            return player != null ? player.getName() : null;
        });

        placeholders.register("player_id", context -> {
            Player player = context.player();
            return player != null ? player.getUniqueId().toString() : null;
        });

        placeholders.register("player_hearts", context -> {
            Player player = context.player();
            return player != null ? player.getHealth() : null;
        });

        placeholders.register("player_max_hearts", context -> {
            Player player = context.player();
            return player != null ? PlayerUtil.getAttributeAmount(player, Attributes.MAX_HEALTH) : null;
        });

        placeholders.register("player_health", context -> {
            PlayerState playerState = context.playerState();
            return playerState != null ? playerState.getHealth() : null;
        });

        placeholders.register("player_max_health", context -> {
            PlayerState playerState = context.playerState();
            return playerState != null ? playerState.getMaxHealth() : null;
        });

        placeholders.register("player_health_regen", context -> {
            PlayerState playerState = context.playerState();
            return playerState != null ? playerState.getHealthRegen() : null;
        });

        placeholders.register("player_mana", context -> {
            PlayerState playerState = context.playerState();
            return playerState != null ? playerState.getMana() : null;
        });

        placeholders.register("player_max_mana", context -> {
            PlayerState playerState = context.playerState();
            return playerState != null ? playerState.getMaxMana() : null;
        });

        placeholders.register("player_mana_regen", context -> {
            PlayerState playerState = context.playerState();
            return playerState != null ? playerState.getManaRegen() : null;
        });

        placeholders.register("player_defense", context -> {
            PlayerState playerState = context.playerState();
            return playerState != null ? playerState.getDefense() : null;
        });

        placeholders.register("player_damage", context -> {
            PlayerState playerState = context.playerState();
            return playerState != null ? playerState.getAttackDamage() : null;
        });

        placeholders.register("player_crit_chance", context -> {
            PlayerState playerState = context.playerState();
            return playerState != null ? playerState.getCritChance() : null;
        });

        placeholders.register("player_crit_damage", context -> {
            PlayerState playerState = context.playerState();
            return playerState != null ? playerState.getCritDamageMultiplier() : null;
        });

        placeholders.register("player_mining_speed", context -> {
            PlayerState playerState = context.playerState();
            return playerState != null ? playerState.getRawMiningSpeed() : null;
        });

        placeholders.register("player_movement_speed", context -> {
            PlayerState playerState = context.playerState();
            return playerState != null ? playerState.getMovementSpeed() : null;
        });

        placeholders.register("player_mining_fortune", context -> {
            PlayerState playerState = context.playerState();
            return playerState != null ? playerState.getStatDisplayAmount(BaseStats.MINING_FORTUNE) : null;
        });

        placeholders.register("player_farming_fortune", context -> {
            PlayerState playerState = context.playerState();
            return playerState != null ? playerState.getStatDisplayAmount(BaseStats.FARMING_FORTUNE) : null;
        });

        placeholders.register("player_foraging_fortune", context -> {
            PlayerState playerState = context.playerState();
            return playerState != null ? playerState.getStatDisplayAmount(BaseStats.FORAGING_FORTUNE) : null;
        });

    }

}
