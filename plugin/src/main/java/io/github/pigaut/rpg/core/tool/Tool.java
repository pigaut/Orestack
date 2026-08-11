package io.github.pigaut.rpg.core.tool;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class Tool {

    private final NamespacedKey namespacedKey;
    private final String permission;

    private ItemStack itemTemplate = new ItemStack(Material.DIRT);
    private Consumer<ItemMeta> metaTemplate = meta -> {};

    private Consumer<PlayerInteractEvent> onLeftClickBlock;
    private Consumer<PlayerInteractEvent> onRightClickBlock;
    private Consumer<PlayerInteractEvent> onLeftClickAir;
    private Consumer<PlayerInteractEvent> onRightClickAir;

    private BiConsumer<Player, Entity> onLeftClickEntity;
    private BiConsumer<Player, Entity> onRightClickEntity;

    private BiConsumer<Player, ItemStack> onSwapHand;
    private BiConsumer<Player, ItemStack> onDropItem;

    public Tool(@NotNull EnhancedPlugin plugin, @NotNull String name) {
        this.namespacedKey = plugin.getNamespacedKey(name);
        this.permission = plugin.getPermission(name);
    }

    public @NotNull String getId() {
        return namespacedKey.toString();
    }

    public @NotNull String getName() {
        return namespacedKey.getKey();
    }

    public @NotNull NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    public @NotNull String getPermission() {
        return permission;
    }

    public @NotNull ItemStack getItemTemplate() {
        return itemTemplate.clone();
    }

    public void setItemTemplate(@NotNull ItemStack itemTemplate) {
        Preconditions.checkArgument(ItemUtil.isNotAir(itemTemplate), "Item type cannot be air");
        this.itemTemplate = itemTemplate;
    }

    public ItemMeta getItemMetaTemplate(@NotNull ItemStack item) {
        ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(item.getType());
        PersistentData.setString(itemMeta, namespacedKey, namespacedKey.getKey());
        metaTemplate.accept(itemMeta);
        return itemMeta;
    }

    public void setItemMetaTemplate(@NotNull Consumer<ItemMeta> metaTemplate) {
        this.metaTemplate = metaTemplate;
    }

    public Tool onLeftClickBlock(Consumer<PlayerInteractEvent> action) {
        this.onLeftClickBlock = action;
        return this;
    }

    public Tool onRightClickBlock(Consumer<PlayerInteractEvent> action) {
        this.onRightClickBlock = action;
        return this;
    }

    public Tool onLeftClickAir(Consumer<PlayerInteractEvent> action) {
        this.onLeftClickAir = action;
        return this;
    }

    public Tool onRightClickAir(Consumer<PlayerInteractEvent> action) {
        this.onRightClickAir = action;
        return this;
    }

    public Tool onLeftClickEntity(BiConsumer<Player, Entity> action) {
        this.onLeftClickEntity = action;
        return this;
    }

    public Tool onRightClickEntity(BiConsumer<Player, Entity> action) {
        this.onRightClickEntity = action;
        return this;
    }

    public Tool onSwapHand(BiConsumer<Player, ItemStack> action) {
        this.onSwapHand = action;
        return this;
    }

    public Tool onDropItem(BiConsumer<Player, ItemStack> action) {
        this.onDropItem = action;
        return this;
    }

    public @Nullable Consumer<PlayerInteractEvent> getOnLeftClickBlock() { return onLeftClickBlock; }

    public @Nullable Consumer<PlayerInteractEvent> getOnRightClickBlock() { return onRightClickBlock; }

    public @Nullable Consumer<PlayerInteractEvent> getOnLeftClickAir() { return onLeftClickAir; }

    public @Nullable Consumer<PlayerInteractEvent> getOnRightClickAir() { return onRightClickAir; }

    public @Nullable BiConsumer<Player, Entity> getOnLeftClickEntity() { return onLeftClickEntity; }

    public @Nullable BiConsumer<Player, Entity> getOnRightClickEntity() { return onRightClickEntity; }

    public @Nullable BiConsumer<Player, ItemStack> getOnSwapHand() { return onSwapHand; }

    public @Nullable BiConsumer<Player, ItemStack> getOnDropItem() { return onDropItem; }

}
