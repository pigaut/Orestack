package io.github.pigaut.rpg.module.menu.button.icon;

import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.core.item.options.*;
import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.core.item.options.*;
import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class IconOptions extends ItemOptions {

    public IconOptions(@Nullable Material material, @Nullable Integer amount,
                       @Nullable String displayName, @Nullable List<String> lore,
                       @Nullable Boolean hideTooltip, @Nullable List<ItemFlag> itemFlags, @Nullable Boolean glow,
                       @Nullable Integer customModelData, @Nullable NamespacedKey itemModel, @Nullable Boolean unbreakable,
                       @Nullable Integer repairCost, @Nullable Integer damage, @Nullable Integer maxDamage,
                       @Nullable Map<Enchantment, Integer> enchantments, @Nullable List<CustomAttribute> attributes,
                       @Nullable String headTexture) {
        super(material, amount, displayName, lore, hideTooltip, itemFlags, glow, customModelData, itemModel, unbreakable, repairCost, damage, maxDamage, enchantments, attributes, headTexture);
    }

}
