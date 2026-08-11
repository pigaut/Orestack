package io.github.pigaut.rpg.module.item;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.item.power.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ItemTemplate implements Identifiable {

    private final EnhancedPlugin plugin;
    private final String name;
    private final String group;

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    private final List<String> description;
    private final List<String> abilities;

    private final @Nullable ToolBreakingPower breakingPower;
    private final @Nullable String rarity;

    private final boolean unplaceable;

    private final Integer maxUses;
    private final Amount uses;
    private final Map<Stat, Amount> stats;

    private final Function onBlockBreak;
    private final Function onLeftClick;
    private final Function onRightClick;
    private final Function onLeftClickBlock;
    private final Function onLeftClickAir;
    private final Function onRightClickBlock;
    private final Function onRightClickAir;
    private final Function onSwapHand;
    private final Function onDrop;

    public ItemTemplate(EnhancedPlugin plugin, String name, @Nullable String group,
                        ItemStack itemStack, ItemMeta itemMeta,
                        @NotNull List<String> description, @NotNull List<String> abilities,
                        @Nullable ToolBreakingPower breakingPower, @Nullable String rarity,
                        boolean unplaceable, Integer maxUses, Amount uses,
                        Map<Stat, Amount> stats,
                        Function onBlockBreak, Function onLeftClick, Function onRightClick,
                        Function onLeftClickBlock, Function onLeftClickAir,
                        Function onRightClickBlock, Function onRightClickAir,
                        Function onSwapHand, Function onDrop) {
        this.plugin = plugin;
        this.name = name;
        this.group = group;
        this.itemStack = itemStack;
        this.itemMeta = itemMeta;
        this.description = description;
        this.abilities = abilities;
        this.breakingPower = breakingPower;
        this.rarity = rarity;
        this.unplaceable = unplaceable;
        this.maxUses = maxUses;
        this.uses = uses;
        this.stats = Map.copyOf(stats);
        this.onBlockBreak = onBlockBreak;
        this.onLeftClick = onLeftClick;
        this.onRightClick = onRightClick;
        this.onLeftClickBlock = onLeftClickBlock;
        this.onLeftClickAir = onLeftClickAir;
        this.onRightClickBlock = onRightClickBlock;
        this.onRightClickAir = onRightClickAir;
        this.onSwapHand = onSwapHand;
        this.onDrop = onDrop;
    }

    public @NotNull String getName() {
        return name;
    }

    public @Nullable String getGroup() {
        return group;
    }

    @Override
    public @NotNull ItemStack getIcon() {
        return itemStack.clone();
    }

    public @NotNull ItemStack createItemStack() {
        return createItemStack(null);
    }

    public @NotNull ItemStack createItemStack(@Nullable Player player) {
        ItemStack item = itemStack.clone();
        ItemMeta meta = itemMeta.clone();

        // Item template name
        PersistentData.setString(meta, plugin.getItems().getItemKey(), name);

        // Item uses
        if (uses != null) {
            PersistentData.setInteger(meta, plugin.getItems().getUsesKey(), uses.intValue());
        }

        // Item stats
        stats.forEach((stat, amount) -> {
            NamespacedKey statKey = plugin.getItems().getStatKey(stat);
            PersistentData.setInteger(meta, statKey, amount.intValue());
        });

        item.setItemMeta(meta);

        Context context = Context.builder(plugin)
                .withPlayer(player)
                .withPlayerState(player != null ? plugin.getPlayerState(player) : null)
                .withItem(item)
                .build();

        return PlaceholderUtil.parseAll(context, item);
    }

    public void updateItemMeta(@NotNull ItemStack item, @Nullable Player player) {
        Context context = Context.builder(plugin)
                .withPlayer(player)
                .withPlayerState(player != null ? plugin.getPlayerState(player) : null)
                .withItem(item)
                .build();

        ItemMeta metaTemplate = itemMeta.clone();
        metaTemplate.removeEnchantments();

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.getEnchants().forEach((enchant, level) -> {
            metaTemplate.addEnchant(enchant, level, true);
        });

        ItemMeta parsedMeta = PlaceholderUtil.parseAll(context, metaTemplate);
        item.setItemMeta(parsedMeta);
    }

    public boolean isUnplaceable() {
        return unplaceable;
    }

    public boolean hasUses() {
        return maxUses != null;
    }

    public @Nullable Integer getMaxUses() {
        return maxUses;
    }

    public @Nullable Amount getUses() {
        return uses;
    }

    public @Nullable Function getOnRightClickAir() {
        return onRightClickAir;
    }

    public @Nullable Function getOnRightClickBlock() {
        return onRightClickBlock;
    }

    public @Nullable Function getOnLeftClickAir() {
        return onLeftClickAir;
    }

    public @Nullable Function getOnLeftClickBlock() {
        return onLeftClickBlock;
    }

    public @Nullable Function getOnRightClick() {
        return onRightClick;
    }

    public @Nullable Function getOnLeftClick() {
        return onLeftClick;
    }

    public @Nullable Function getOnBlockBreak() {
        return onBlockBreak;
    }

    public @Nullable Function getOnSwapHand() {
        return onSwapHand;
    }

    public @Nullable Function getOnDrop() {
        return onDrop;
    }

    public @NotNull List<String> getDescription() {
        return new ArrayList<>(description);
    }

    public @NotNull List<String> getAbilitiesDescription() {
        return new ArrayList<>(abilities);
    }

    public @Nullable ToolBreakingPower getBreakingPower() {
        return breakingPower;
    }

    public int getBreakingPowerAmount() {
        return breakingPower != null ? breakingPower.getAmount() : plugin.getSettings().getDefaultBreakingPower();
    }

    public @Nullable String getRarity() {
        return rarity;
    }

}
