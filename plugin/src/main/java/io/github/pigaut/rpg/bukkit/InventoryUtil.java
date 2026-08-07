package io.github.pigaut.rpg.bukkit;

import org.bukkit.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class InventoryUtil {

    private InventoryUtil() {}

    public static boolean isValidChestSize(int size) {
        return isValidInventory(InventoryType.CHEST, size);
    }

    public static boolean isValidInventory(InventoryType storage, int size) {
        return storage == InventoryType.CHEST ? (size > 0 && size <= 54 && size % 9 == 0) : size == storage.getDefaultSize();
    }

    public static int getValidSizeOrDefault(InventoryType storage, int size) {
        if (isValidInventory(storage, size)) return size;
        return storage.getDefaultSize();
    }

    public static int getInventoryLength(InventoryType storage, int size) {
        switch (storage) {
            case CHEST, PLAYER, ENDER_CHEST, SHULKER_BOX, BARREL:
                return 9;
            case DISPENSER, DROPPER, WORKBENCH:
                return 3;
            default:
                return storage.getDefaultSize();
        }
    }

    public static int getInventoryHeight(InventoryType storage, int size) {
        switch (storage) {
            case ENDER_CHEST, SHULKER_BOX, BARREL, DISPENSER, DROPPER, WORKBENCH:
                return 3;
            case PLAYER:
                return 4;
            case CHEST:
                return size/9;
            default:
                return 1;
        }
    }

    public static Inventory createInventory(InventoryType storage) {
        return createInventory(storage.getDefaultTitle(), storage, storage.getDefaultSize());
    }

    public static Inventory createInventory(InventoryType storage, int size) {
        return createInventory(storage.getDefaultTitle(), storage, size);
    }

    public static Inventory createInventory(String title, InventoryType storage, int size) {
        if (storage == InventoryType.CHEST) {
            return Bukkit.createInventory(null, size, title);
        }
        return Bukkit.createInventory(null, storage, title);
    }

    public static boolean isHotbarSlot(int slot) {
        return slot >= 0 && slot <= 8;
    }

    public static int getFirstEmptyHotbarSlot(@NotNull PlayerInventory inventory) {
        for (int i = 0; i <= 8; i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null || item.getType() == Material.AIR) return i;
        }
        return -1;
    }

    public static boolean isPlacingItemIntoSlot(InventoryClickEvent event, int targetSlot) {
        Inventory topInventory = event.getInventory();
        int topSize = topInventory.getSize();
        int rawSlot = event.getRawSlot();

        if (rawSlot == targetSlot) {
            ItemStack cursor = event.getCursor();
            return cursor != null && cursor.getType() != Material.AIR;
        }

        if (event.isShiftClick() && rawSlot >= topSize) {
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) {
                return false;
            }

            ItemStack targetItem = topInventory.getItem(targetSlot);
            if (targetItem == null || targetItem.getType() == Material.AIR) {
                return true;
            }

            return targetItem.isSimilar(clickedItem) &&
                    targetItem.getAmount() < targetItem.getMaxStackSize();
        }

        return false;
    }

    public static boolean isDraggingItemIntoSlot(InventoryDragEvent event, int targetSlot) {
        Set<Integer> rawSlots = event.getRawSlots();

        if (rawSlots.contains(targetSlot)) {
            ItemStack cursor = event.getCursor();
            return cursor != null && cursor.getType() != Material.AIR;
        }

        return false;
    }

    public static @Nullable ItemStack getPlacedItem(@NotNull InventoryClickEvent event) {
        if (event.isShiftClick()) {
            return event.getCurrentItem();
        } else {
            return event.getCursor();
        }
    }

}
