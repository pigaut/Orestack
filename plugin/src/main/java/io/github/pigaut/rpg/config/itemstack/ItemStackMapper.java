package io.github.pigaut.rpg.config.itemstack;

import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.map.*;
import org.bukkit.*;
import org.bukkit.attribute.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.*;

import java.util.*;

public class ItemStackMapper implements ConfigMapper.Section<ItemStack> {

    @Override
    public void mapToSection(ConfigSection section, ItemStack item) {
        Material type = item.getType();
        section.set("material|type", type);

        int amount = item.getAmount();
        if (amount != 1) {
            section.set("amount", item.getAmount());
        }

        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();

            if (meta.hasDisplayName()) {
                section.set("name|display", meta.getDisplayName());
            }

            if (meta.hasLore()) {
                section.set("lore", meta.getLore());
            }

            if (meta instanceof Damageable damageable && damageable.hasDamage()) {
                section.set("damage", damageable.getDamage());
            }

            if (meta.isUnbreakable()) {
                section.set("unbreakable", true);
            }

            if (meta.hasCustomModelData()) {
                section.set("model-data", meta.getCustomModelData());
            }

            if (meta instanceof Repairable repairable) {
                if (repairable.hasRepairCost()) {
                    section.set("repair-cost", repairable.getRepairCost());
                }
            }

            Set<ItemFlag> itemFlags = meta.getItemFlags();
            if (!itemFlags.isEmpty()) {
                section.set("flags", itemFlags);
            }

            meta.getEnchants().forEach((enchant, level) -> {
                section.set("enchants." + enchant.getKey().getKey(), level);
            });

            final PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            if (!dataContainer.isEmpty()) {
                section.set("persistent-data", dataContainer);
            }

//            if (meta instanceof SkullMeta skullMeta) {
//                final String headTexture = XSkull.of(skullMeta).getProfileValue();
//                if (headTexture != null) {
//                    section.set("head-texture|head", headTexture);
//                }
//            }

            if (meta.hasAttributeModifiers()) {
                final ConfigSequence attributeSequence = section.getSequenceOrCreate("attributes");
                for (Map.Entry<Attribute, AttributeModifier> entry : meta.getAttributeModifiers().entries()) {
                    attributeSequence.add(new CustomAttribute(entry.getKey(), entry.getValue()));
                }
            }

        }

    }

}