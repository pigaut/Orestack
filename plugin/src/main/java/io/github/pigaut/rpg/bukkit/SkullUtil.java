package io.github.pigaut.rpg.bukkit;

import com.destroystokyo.paper.profile.*;
import io.github.pigaut.rpg.server.version.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.*;

// Paper 1.18.1+ only
public class SkullUtil {

    public static void setSkullTexture(@NotNull ItemStack item, @NotNull String base64Texture) {
        if (item.getType() != Material.PLAYER_HEAD) {
            item.setType(Material.PLAYER_HEAD);
        }

        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        if (skullMeta == null) {
            return;
        }

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.getProperties().add(new ProfileProperty("textures", base64Texture));
        skullMeta.setPlayerProfile(profile);
        item.setItemMeta(skullMeta);
    }

    public static void setSkullTexture(@NotNull ItemStack item, @NotNull Player player) {
        if (item.getType() != Material.PLAYER_HEAD) {
            item.setType(Material.PLAYER_HEAD);
        }

        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        if (skullMeta == null) {
            return;
        }

        skullMeta.setOwningPlayer(player);
        item.setItemMeta(skullMeta);
    }

    @Nullable
    public static String getSkullTexture(@NotNull Block block) {
        if (block.getType() != Material.PLAYER_HEAD && block.getType() != Material.PLAYER_WALL_HEAD) {
            return null;
        }

        Skull skull = (Skull) block.getState();
        return getSkullTexture(skull);
    }

    @Nullable
    public static String getSkullTexture(@NotNull Skull skull) {
        PlayerProfile profile = skull.getPlayerProfile();
        if (profile == null) {
            return null;
        }

        for (ProfileProperty property : profile.getProperties()) {
            if (property.getName().equals("textures")) {
                return property.getValue();
            }
        }

        return null;
    }

    public static void setSkullTexture(@NotNull Block block, @NotNull String base64Texture) {
        if (block.getType() != Material.PLAYER_HEAD && block.getType() != Material.PLAYER_WALL_HEAD) {
            block.setType(Material.PLAYER_HEAD);
        }

        Skull skull = (Skull) block.getState();
        setSkullTexture(skull, base64Texture);
    }

    public static void setSkullTexture(@NotNull Skull skull, @NotNull String base64Texture) {
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.getProperties().add(new ProfileProperty("textures", base64Texture));
        skull.setOwnerProfile(profile);
    }

    public static void setSkullTexture(@NotNull Block block, @NotNull Player player) {
        if (block.getType() != Material.PLAYER_HEAD && block.getType() != Material.PLAYER_WALL_HEAD) {
            block.setType(Material.PLAYER_HEAD);
        }

        Skull skull = (Skull) block.getState();
        skull.setOwningPlayer(player);
        skull.update();
    }

}

