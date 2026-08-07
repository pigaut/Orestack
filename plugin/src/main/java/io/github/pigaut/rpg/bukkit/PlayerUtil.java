package io.github.pigaut.rpg.bukkit;

import com.google.gson.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.event.item.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.event.item.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.util.*;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.*;
import org.bukkit.*;
import org.bukkit.attribute.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PlayerUtil {

    public static final EquipmentSlot[] EQUIPMENT_SLOTS = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET,
            EquipmentSlot.HAND,
            EquipmentSlot.OFF_HAND
    };

    public static void sendChat(@NotNull Player player, @NotNull String message) {
        if (message.startsWith("[json]")) {
            message = StringUtil.removeTag(message, "[json]");

            BaseComponent[] components;
            try {
                components = ComponentSerializer.parse(message);
            } catch (JsonParseException e) {
                player.sendMessage(message);
                return;
            }

            player.spigot().sendMessage(components);
            return;
        }

        player.sendMessage(message);
    }

    public static void sendChat(@NotNull Player player, @NotNull Context context, @NotNull String message) {
        String parsedMessage = PlaceholderUtil.parseAll(context, message);
        sendChat(player, parsedMessage);
    }

    public static void sendActionBar(@NotNull Player player, @NotNull String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(message));
    }

    public static void sendActionBar(@NotNull Player player, @NotNull Context context, @NotNull String message) {
        String parsedMessage = PlaceholderUtil.parseAll(context, message);
        sendActionBar(player, parsedMessage);
    }

    public static void giveItemsOrDrop(@NotNull Player player, @NotNull ItemStack... items) {
        PlayerGiveItemEvent giveEvent = new PlayerGiveItemEvent(player, items);
        Server.callEvent(giveEvent);
        if (giveEvent.isCancelled()) {
            return;
        }

        Map<Integer, ItemStack> leftoverItems = player.getInventory().addItem(items);
        if (!leftoverItems.isEmpty()) {
            World world = player.getWorld();
            Location playerLocation = player.getLocation();
            leftoverItems.values().forEach(item -> {
                world.dropItemNaturally(playerLocation, item);
            });
        }
    }

    public static void takeItems(@NotNull Player player, @NotNull ItemStack item, int amount) {
        PlayerTakeItemEvent itemTakeEvent = new PlayerTakeItemEvent(player, item, amount);
        Server.callEvent(itemTakeEvent);
        if (itemTakeEvent.isCancelled()) {
            return;
        }

        PlayerInventory inventory = player.getInventory();
        int remaining = amount;

        for (ItemStack foundItem : inventory.getContents()) {
            if (foundItem == null || !foundItem.isSimilar(item)) {
                continue;
            }

            int stackAmount = foundItem.getAmount();

            if (stackAmount <= remaining) {
                remaining -= stackAmount;
                inventory.remove(foundItem);
            } else {
                foundItem.setAmount(stackAmount - remaining);
                remaining = 0;
            }

            if (remaining <= 0) {
                break;
            }
        }
    }

    public static void heal(@NotNull Player player, double amount) {
        AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.MAX_HEALTH);
        double maxHealth = maxHealthAttribute != null ? maxHealthAttribute.getValue() : 20.0;
        double newHealth = Math.min(player.getHealth() + amount, maxHealth);
        player.setHealth(newHealth);
    }

    public static void openEnderChest(@NotNull Player player) {
        player.openInventory(player.getEnderChest());
    }

    public static int getTargetBlockX(@NotNull Player player) {
        final Block targetBlock = player.getTargetBlockExact(6);
        if (targetBlock != null) {
            return targetBlock.getX();
        }
        return 0;
    }

    public static int getTargetBlockY(@NotNull Player player) {
        final Block targetBlock = player.getTargetBlockExact(6);
        if (targetBlock != null) {
            return targetBlock.getY();
        }
        return 0;
    }

    public static int getTargetBlockZ(@NotNull Player player) {
        final Block targetBlock = player.getTargetBlockExact(6);
        if (targetBlock != null) {
            return targetBlock.getZ();
        }
        return 0;
    }

    public static boolean isInRange(@NotNull Player player, @NotNull Location target, double range) {
        if (!player.getWorld().equals(target.getWorld())) return false;
        return player.getLocation().distanceSquared(target) <= (range * range);
    }

    public static boolean canInstaMine(@NotNull Player player, @NotNull Block block) {
        return canInstaMine(player, block.getType());
    }

    public static boolean canInstaMine(@NotNull Player player, @NotNull Material blockType) {
        if (player.getGameMode() == GameMode.CREATIVE) return true;
        return blockType.getHardness() <= 0.0;
    }

    public static int getEffectLevel(@NotNull Player player, @NotNull PotionEffectType potionEffect) {
        PotionEffect effect = player.getPotionEffect(potionEffect);
        return effect != null ? effect.getAmplifier() + 1 : 0;
    }

    public static @Nullable Double getAttributeAmount(@NotNull Player player, @NotNull Attribute attribute) {
        AttributeInstance attributeInstance = player.getAttribute(attribute);
        if (attributeInstance != null) {
            return attributeInstance.getValue();
        }
        return null;
    }

    public static boolean isOnGround(@NotNull Player player) {
        Block block = player.getLocation().subtract(0, 0.1, 0).getBlock();
        return !block.isEmpty() && block.getType().isSolid();
    }

    public static boolean isStandingOn(@NotNull Player player, @NotNull Material material) {
        Block block = player.getLocation().subtract(0, 0.1, 0).getBlock();
        return block.getType() == material;
    }

    public static @NotNull ItemStack getTool(@NotNull Player player) {
        return player.getInventory().getItemInMainHand();
    }

    public static void setTool(@NotNull Player player, @Nullable ItemStack item) {
        player.getInventory().setItemInMainHand(item);
    }

    public static @NotNull ItemStack getOffHand(@NotNull Player player) {
        return player.getInventory().getItem(EquipmentSlot.OFF_HAND);
    }

    public static @NotNull ItemStack getHelmet(@NotNull Player player) {
        return player.getInventory().getItem(EquipmentSlot.HEAD);
    }

    public static @NotNull ItemStack getChestplate(@NotNull Player player) {
        return player.getInventory().getItem(EquipmentSlot.CHEST);
    }

    public static @NotNull ItemStack getLeggings(@NotNull Player player) {
        return player.getInventory().getItem(EquipmentSlot.LEGS);
    }

    public static @NotNull ItemStack getBoots(@NotNull Player player) {
        return player.getInventory().getItem(EquipmentSlot.FEET);
    }

}
