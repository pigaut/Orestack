package io.github.pigaut.rpg.core.item.options;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.util.reflection.*;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.util.reflection.*;
import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static io.github.pigaut.rpg.server.version.Version.V1_20_5;

public class ItemOptions {

    private final @Nullable Material material;
    private final @Nullable Integer amount;
    private final @Nullable Boolean hideTooltip;
    private final @Nullable String displayName;
    private final @Nullable List<String> lore;
    private final @Nullable List<ItemFlag> itemFlags;
    private final @Nullable Boolean glow;
    private final @Nullable Integer customModelData;
    private final @Nullable NamespacedKey itemModel;
    private final @Nullable Boolean unbreakable;
    private final @Nullable Integer repairCost;
    private final @Nullable Integer damage;
    private final @Nullable Integer maxDamage;
    private final @Nullable Map<Enchantment, Integer> enchantments;
    private final @Nullable List<CustomAttribute> attributes;
    private final @Nullable String headTexture;

    public ItemOptions(@Nullable Material material, @Nullable Integer amount,
                       @Nullable String displayName, @Nullable List<String> lore,
                       @Nullable Boolean hideTooltip, @Nullable List<ItemFlag> itemFlags, @Nullable Boolean glow,
                       @Nullable Integer customModelData, @Nullable NamespacedKey itemModel, @Nullable Boolean unbreakable,
                       @Nullable Integer repairCost, @Nullable Integer damage, @Nullable Integer maxDamage,
                       @Nullable Map<Enchantment, Integer> enchantments, @Nullable List<CustomAttribute> attributes,
                       @Nullable String headTexture) {
        this.material = material;
        this.amount = amount;
        this.displayName = displayName;
        this.lore = lore;
        this.hideTooltip = hideTooltip;
        this.itemFlags = itemFlags;
        this.glow = glow;
        this.customModelData = customModelData;
        this.itemModel = itemModel;
        this.unbreakable = unbreakable;
        this.repairCost = repairCost;
        this.damage = damage;
        this.maxDamage = maxDamage;
        this.enchantments = enchantments;
        this.attributes = attributes;
        this.headTexture = headTexture;
    }

    public void applyTo(@NotNull ItemStack item) {
        if (material != null) {
            item.setType(material);
        }

        if (amount != null) {
            item.setAmount(amount);
        }

        if (headTexture != null && Server.isPaper()) {
            SkullUtil.setSkullTexture(item, headTexture);
        }

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (displayName != null) {
                meta.setDisplayName(displayName);
            }

            if (lore != null) {
                meta.setLore(lore);
            }

            if (itemFlags != null) {
                meta.addItemFlags(itemFlags.toArray(new ItemFlag[0]));
            }

            if (hideTooltip != null) {
                if (Server.getVersion() >= V1_20_5) {
                    meta.setHideTooltip(hideTooltip);
                } else {
                    meta.setDisplayName(ChatColor.RESET.toString());
                    meta.setLore(null);
                    meta.addItemFlags(ItemFlag.values());
                }
            }

            if (glow != null) {
                if (Server.getVersion() >= V1_20_5) {
                    meta.setEnchantmentGlintOverride(glow);
                    item.setItemMeta(meta);
                } else if (glow) {
                    if (!meta.hasEnchants()) {
                        meta.addEnchant(Enchants.LUCK, 1, true);
                    }
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    item.setItemMeta(meta);
                }
            }

            if (customModelData != null) {
                meta.setCustomModelData(customModelData);
            }

            if (itemModel != null && Server.getVersion() >= Version.V1_21_4) {
                meta.setItemModel(itemModel);
            }

            if (unbreakable != null) {
                meta.setUnbreakable(unbreakable);
            }

            if (repairCost != null && meta instanceof Repairable repairable) {
                repairable.setRepairCost(repairCost);
            }

            if (damage != null && meta instanceof Damageable damageable) {
                damageable.setDamage(damage);
            }

            if (maxDamage != null && meta instanceof Damageable) {
                if (Server.getVersion() >= Version.V1_20_5) {
                    Reflect.on(meta).call("setMaxDamage", maxDamage);
                }
            }

            if (enchantments != null) {
                enchantments.forEach((enchantment, level) -> meta.addEnchant(enchantment, level, true));
            }

            if (attributes != null) {
                attributes.forEach(attribute -> meta.addAttributeModifier(attribute.type(), attribute.modifier()));
            }

            item.setItemMeta(meta);
        }
    }

}