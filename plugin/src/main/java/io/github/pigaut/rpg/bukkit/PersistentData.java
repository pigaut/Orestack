package io.github.pigaut.rpg.bukkit;

import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.*;
import org.jetbrains.annotations.*;

public class PersistentData {

    private PersistentData() {}

    public static boolean hasString(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        return holder.getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }

    public static boolean hasString(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key, @NotNull String value) {
        String foundValue = getString(holder, key);
        return value.equals(foundValue);
    }

    public static @Nullable String getString(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        return holder.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    public static String getStringOrDefault(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key, @NotNull String defValue) {
        String foundValue = getString(holder, key);
        return foundValue != null ? foundValue : defValue;
    }

    public static void setString(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key, @NotNull String value) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
    }

    public static void setString(@NotNull ItemStack item, @NotNull NamespacedKey key, @NotNull String value) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        setString(meta, key, value);
        item.setItemMeta(meta);
    }

    public static boolean hasInteger(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        Integer foundValue = getInteger(holder, key);
        return foundValue != null;
    }

    public static boolean hasInteger(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key, int value) {
        Integer foundValue = getInteger(holder, key);
        return foundValue != null && foundValue == value;
    }

    public static @Nullable Integer getInteger(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        return holder.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
    }

    public static int getIntegerOrDefault(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key, int defValue) {
        Integer foundValue = getInteger(holder, key);
        return foundValue != null ? foundValue : defValue;
    }

    public static void setInteger(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key, int value) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
    }

    public static void setInteger(@NotNull ItemStack item, @NotNull NamespacedKey key, int value) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        setInteger(meta, key, value);
        item.setItemMeta(meta);
    }

    public static @Nullable Double getDouble(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        return holder.getPersistentDataContainer().get(key, PersistentDataType.DOUBLE);
    }

    public static double getDoubleOrDefault(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key, double defValue) {
        Double foundValue = getDouble(holder, key);
        return foundValue != null ? foundValue : defValue;
    }

    public static void setDouble(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key, double value) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
    }

    public static boolean hasTag(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        PersistentDataContainer container = holder.getPersistentDataContainer();
        if (!container.has(key, PersistentDataType.BYTE)) {
            return false;
        }

        return container.get(key, PersistentDataType.BYTE) == 1;
    }

    public static void setTag(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
    }

    public static void remove(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        holder.getPersistentDataContainer().remove(key);
    }

    public static @Nullable PersistentDataType<?, ?> getDataType(@NotNull PersistentDataContainer container, @NotNull NamespacedKey key) {
        if (container.has(key, PersistentDataType.STRING)) {
            return PersistentDataType.STRING;
        } else if (container.has(key, PersistentDataType.BYTE)) {
            return PersistentDataType.BYTE;
        } else if (container.has(key, PersistentDataType.SHORT)) {
            return PersistentDataType.SHORT;
        } else if (container.has(key, PersistentDataType.INTEGER)) {
            return PersistentDataType.INTEGER;
        } else if (container.has(key, PersistentDataType.LONG)) {
            return PersistentDataType.LONG;
        } else if (container.has(key, PersistentDataType.FLOAT)) {
            return PersistentDataType.FLOAT;
        } else if (container.has(key, PersistentDataType.DOUBLE)) {
            return PersistentDataType.DOUBLE;
        } else if (container.has(key, PersistentDataType.BYTE_ARRAY)) {
            return PersistentDataType.BYTE_ARRAY;
        } else if (container.has(key, PersistentDataType.INTEGER_ARRAY)) {
            return PersistentDataType.INTEGER_ARRAY;
        } else if (container.has(key, PersistentDataType.LONG_ARRAY)) {
            return PersistentDataType.LONG_ARRAY;
        } else if (container.has(key, PersistentDataType.TAG_CONTAINER)) {
            return PersistentDataType.TAG_CONTAINER;
        } else if (container.has(key, PersistentDataType.TAG_CONTAINER_ARRAY)) {
            return PersistentDataType.TAG_CONTAINER_ARRAY;
        }

        return null;
    }

    public static @Nullable PersistentDataType<?, ?> getDataType(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
        return getDataType(holder.getPersistentDataContainer(), key);
    }

}
