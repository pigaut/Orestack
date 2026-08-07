package io.github.pigaut.rpg.core.item;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MetaEditor {

    private final ItemMeta meta;

    protected MetaEditor(ItemMeta meta) {
        this.meta = meta;
    }

    public static MetaEditor of(@NotNull ItemMeta meta) {
        return new MetaEditor(meta);
    }

    public MetaEditor withAllFlags() {
        meta.addItemFlags(ItemFlag.values());
        return this;
    }

    public MetaEditor clearFlags() {
        meta.removeItemFlags(ItemFlag.values());
        return this;
    }

    public MetaEditor withName(@Nullable String name) {
        if (name != null) {
            meta.setDisplayName(ColorUtil.translateColors(name));
        } else {
            meta.setDisplayName(null);
        }
        return this;
    }

    public MetaEditor addEmptyLine() {
        return addLine("");
    }

    public MetaEditor addLine(@NotNull String loreLine) {
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        lore.add(ColorUtil.translateColors(loreLine));
        meta.setLore(lore);
        return this;
    }

    public MetaEditor withLore(@Nullable List<String> lore) {
        if (lore == null) {
            meta.setLore(null);
            return this;
        }

        List<String> colored = new ArrayList<>(lore.size());
        for (String line : lore) {
            colored.add(ColorUtil.translateColors(line));
        }
        meta.setLore(colored);
        return this;
    }

    public MetaEditor withLore(@NotNull String... lines) {
        return withLore(Arrays.asList(lines));
    }

    public MetaEditor withEnchant(@NotNull Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    public MetaEditor withEnchants(@NotNull Map<Enchantment, Integer> enchants) {
        enchants.forEach((ench, lvl) -> meta.addEnchant(ench, lvl, true));
        return this;
    }

    public MetaEditor unbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return this;
    }

    public MetaEditor withFlags(@NotNull ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    public MetaEditor withCustomModel(@Nullable Integer data) {
        meta.setCustomModelData(data);
        return this;
    }

}
