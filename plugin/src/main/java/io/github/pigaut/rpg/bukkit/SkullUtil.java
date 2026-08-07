package io.github.pigaut.rpg.bukkit;

import com.destroystokyo.paper.profile.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.util.*;

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
        profile.setProperty(new ProfileProperty("textures", base64Texture));
        skullMeta.setPlayerProfile(profile);
        item.setItemMeta(skullMeta);
    }

    public static void setSkullTexture(@NotNull ItemStack item, @NotNull Player player) {
        if (item.getType() != Material.PLAYER_HEAD) {
            item.setType(Material.PLAYER_HEAD);
        }

        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        if (skullMeta != null) {
            skullMeta.setOwningPlayer(player);
            item.setItemMeta(skullMeta);
        }
    }

}
