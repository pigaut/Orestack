package io.github.pigaut.rpg.core.item.options;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.node.scalar.*;
import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ItemOptionsLoader implements ConfigLoader.Section<ItemOptions> {

    @Override
    public @NotNull String getErrorDescription() {
        return "invalid item";
    }

    @Override
    public @NotNull ItemOptions loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        Material material = section.get("type|material", Material.class)
                .require(MaterialUtil::isNotAir, "item type must not be air")
                .withDefault(null);

        Integer amount = section.getInteger("amount").withDefault(null);

        String displayName = section.getString("name|display", ColorUtil.FORMATTER).withDefault(null);

        List<String> lore = section.isSet("lore")
                ? section.getStringList("lore", ColorUtil.FORMATTER).orEmpty()
                : null;

        Boolean hideTooltip = section.getBoolean("hide-tooltip")
                .withDefault(null);

        if (section.getString("name|display").orElse("").equals("&r")) {
            hideTooltip = true;
        }

        boolean hideFlags = section.getBoolean("hide-flags")
                .withDefault(true);

        List<ItemFlag> itemFlags = section.getAll("flags", ItemFlag.class)
                .withDefault(hideFlags ? Arrays.asList(ItemFlag.values()) : List.of());

        Boolean glow = section.getBoolean("glow").withDefault(null);

        Integer customModelData = section.getInteger("model-data|custom-model-data").withDefault(null);

        NamespacedKey itemModel = section.get("model|custom-model", NamespacedKey.class)
                .check(Server.getVersion() >= Version.V1_21_4, "item model is only available in version 1.21.4+")
                .withDefault(null);

        Boolean unbreakable = section.getBoolean("unbreakable").withDefault(null);

        Integer repairCost = section.getInteger("repair-cost").withDefault(null);

        Integer damage = section.getInteger("durability").withDefault(null);

        Integer maxDamage = section.getInteger("max-durability")
                .check(Server.getVersion() >= Version.V1_20_5, "max durability is only available in version 1.20.5+")
                .withDefault(null);

        Map<Enchantment, Integer> enchantments = null;
        if (section.isSet("enchantments|enchants")) {
            enchantments = new HashMap<>();
            ConfigSection enchantsSection = section.getRequiredSection("enchantments|enchants");
            for (KeyedScalar field : enchantsSection.getNestedScalars()) {
                Enchantment enchantment = field.getKeyAs(Enchantment.class).orThrow();
                int level = field.toInteger().orThrow();
                enchantments.put(enchantment, level);
            }
        }

        List<CustomAttribute> attributes = section.isSet("attributes")
                ? section.getAll("attributes", CustomAttribute.class).orEmpty()
                : null;

        String headTexture = section.getString("head-texture|head-data|head")
                .check(Server.isPaper(), "Head textures require a paper server")
                .withDefault(null);

        return new ItemOptions(material, amount, displayName, lore, hideTooltip, itemFlags, glow, customModelData, itemModel,
                unbreakable, repairCost, damage, maxDamage, enchantments, attributes, headTexture);
    }

}
