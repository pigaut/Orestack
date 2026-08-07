package io.github.pigaut.rpg.core.menu.button;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.enchant.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class IconBuilder {

    private Material type;
    private int amount = 1;
    private String display = "";
    private boolean enchanted = false;
    private final List<String> lore = new ArrayList<>();
    private @Nullable String headTexture = null;

    public IconBuilder() {
        this(Material.TERRACOTTA);
    }

    public IconBuilder(Material type) {
        this.type = type;
    }

    public static IconBuilder of(Material type) {
        return new IconBuilder(type);
    }

    public ItemStack buildIcon() {
        ItemStack icon = new ItemStack(type, amount);
        ItemMeta meta = icon.getItemMeta();
        if (meta != null) {
            if (display != null) {
                meta.setDisplayName(display);
            }
            if (!lore.isEmpty()) {
                meta.setLore(lore);
            }
            if (enchanted) {
                meta.addEnchant(Enchants.FORTUNE, 1, false);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE);
            icon.setItemMeta(meta);
        }
        return icon;
    }

    public IconBuilder type(Material type) {
        this.type = type;
        return this;
    }

    public IconBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public IconBuilder name(String display) {
        this.display = ColorUtil.translateColors("&f" + display);
        return this;
    }

    public IconBuilder addLeftClickLine(String action) {
        final String line = "&eLeft-Click: &f" + action;
        lore.add(ColorUtil.translateColors(line));
        return this;
    }

    public IconBuilder addRightClickLine(String action) {
        final String line = "&6Right-Click: &f" + action;
        lore.add(ColorUtil.translateColors(line));
        return this;
    }

    public IconBuilder addShiftLeftClickLine(String action) {
        final String line = "&cShift + Left-Click: &f" + action;
        lore.add(ColorUtil.translateColors(line));
        return this;
    }

    public IconBuilder addShiftRightClickLine(String action) {
        final String line = "&4Shift + Right-Click: &f" + action;
        lore.add(ColorUtil.translateColors(line));
        return this;
    }

    public IconBuilder addEmptyLine() {
        lore.add("");
        return this;
    }

    public IconBuilder addLine(@NotNull String line) {
        lore.add(ColorUtil.translateColors("&f" + line));
        return this;
    }

    public IconBuilder addLines(String... loreLines) {
        for (String loreLine : loreLines) {
            lore.add(ColorUtil.translateColors("&f" + loreLine));
        }
        return this;
    }

    public IconBuilder addLines(List<String> loreLines) {
        for (String loreLine : loreLines) {
            lore.add(ColorUtil.translateColors("&f" + loreLine));
        }
        return this;
    }

    public IconBuilder enchanted(boolean enchanted) {
        this.enchanted = enchanted;
        return this;
    }

}
