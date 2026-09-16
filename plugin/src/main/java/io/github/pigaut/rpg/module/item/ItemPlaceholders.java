package io.github.pigaut.rpg.module.item;

import com.willfp.eco.core.integrations.economy.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.item.power.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public class ItemPlaceholders {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        register(plugin, "item", Context::item);
        register(plugin, "tool", Context::tool);
        register(plugin, "off_hand", Context::offHand);
        register(plugin, "helmet", Context::helmet);
        register(plugin, "chestplate", Context::chestplate);
        register(plugin, "leggings", Context::leggings);
        register(plugin, "boots", Context::boots);

        PlaceholderRegistry placeholders = plugin.getPlaceholders();
        Settings settings = plugin.getSettings();

        placeholders.register("item_lore:breaking_power", context -> {
            ItemStack item = context.item();
            if (item == null) return List.of();

            ItemTemplate itemTemplate = plugin.getItemTemplate(item);
            if (itemTemplate == null) return List.of();

            ToolBreakingPower toolBreakingPower = itemTemplate.getBreakingPower();
            if (toolBreakingPower == null) return List.of();

            List<String> breakingPowerLore = settings.getItemBreakingPowerLore(toolBreakingPower.getName());
            return PlaceholderUtil.parseAll(context, breakingPowerLore);
        });

        placeholders.register("item_lore:category", context -> {
            ItemStack item = context.item();
            if (item == null) return List.of();

            ItemTemplate itemTemplate = plugin.getItemTemplate(item);
            if (itemTemplate == null) return List.of();

            String category = itemTemplate.getCategory();
            if (category == null) return List.of();

            List<String> categoryLore = settings.getItemCategoryLore();
            return PlaceholderUtil.parseAll(context, categoryLore);
        });

        placeholders.register("item_lore:rarity", context -> {
            ItemStack item = context.item();
            if (item == null) return List.of();

            ItemTemplate itemTemplate = plugin.getItemTemplate(item);
            if (itemTemplate == null) return List.of();

            String rarity = itemTemplate.getRarity();
            if (rarity == null) return List.of();

            List<String> rarityLore = settings.getItemRarityLore(rarity);
            return PlaceholderUtil.parseAll(context, rarityLore);
        });

        placeholders.register("item_lore:crafted_by", context -> {
            ItemStack item = context.item();
            if (item == null) return List.of();

            if (plugin.getItemTemplates().hasItemCreator(item)) {
                List<String> craftedByLore = settings.getItemCraftedByLore();
                return PlaceholderUtil.parseAll(context, craftedByLore);
            }
            return List.of();
        });

        placeholders.register("item_lore:description", context -> {
            ItemStack item = context.item();
            if (item == null) return List.of();

            ItemTemplate itemTemplate = plugin.getItemTemplate(item);
            if (itemTemplate == null) return List.of();

            if (!itemTemplate.getDescription().isEmpty()) {
                List<String> descriptionLore = settings.getItemDescriptionLore();
                return PlaceholderUtil.parseAll(context, descriptionLore);
            }
            return List.of();
        });

        placeholders.register("item_lore:abilities", context -> {
            ItemStack item = context.item();
            if (item == null) return List.of();

            ItemTemplate itemTemplate = plugin.getItemTemplate(item);
            if (itemTemplate == null) return List.of();

            if (!itemTemplate.getAbilitiesDescription().isEmpty()) {
                List<String> abilitiesLore = settings.getItemAbilitiesLore();
                return PlaceholderUtil.parseAll(context, abilitiesLore);
            }
            return List.of();
        });

        placeholders.register("item_lore:stats", context -> {
            ItemStack item = context.item();
            if (item == null) return List.of();

            if (plugin.getItemTemplates().hasStats(item)) {
                List<String> statsLore = settings.getItemStatsLore();
                return PlaceholderUtil.parseAll(context, statsLore);
            }
            return List.of();
        });

        placeholders.register("item_lore:enchants", context -> {
            ItemStack item = context.item();
            if (item.hasItemMeta() && item.getItemMeta().hasEnchants()) {
                List<String> enchantsLore = settings.getItemEnchantsLore();
                return PlaceholderUtil.parseAll(context, enchantsLore);
            }
            return List.of();
        });

    }

    private static void register(@NotNull EnhancedPlugin plugin, @NotNull String prefix, @NotNull Function<Context, ItemStack> resolver) {
        PlaceholderRegistry placeholders = plugin.getPlaceholders();
        Settings settings = plugin.getSettings();

        placeholders.register(prefix + "_name", context -> {
            ItemStack item = resolver.apply(context);
            if (item == null) {
                return null;
            }
            ItemTemplate itemTemplate = plugin.getItemTemplate(item);
            return itemTemplate != null ? itemTemplate.getName() : null;
        });

        placeholders.register(prefix + "_type", context -> {
            ItemStack item = resolver.apply(context);
            return item != null ? item.getType().toString() : null;
        });

        placeholders.register(prefix + "_display", context -> {
            ItemStack item = resolver.apply(context);
            if (item == null) {
                return null;
            }
            ItemMeta meta = item.getItemMeta();
            return meta != null ? meta.getDisplayName() : null;
        });

        placeholders.register(prefix + "_category", context -> {
            ItemStack item = resolver.apply(context);
            if (item == null) return List.of();

            ItemTemplate itemTemplate = plugin.getItemTemplate(item);
            if (itemTemplate == null) return List.of();

            return itemTemplate.getCategory();
        });

        placeholders.register(prefix + "_description", context -> {
            ItemStack item = resolver.apply(context);
            if (item == null) return List.of();

            ItemTemplate itemTemplate = plugin.getItemTemplate(item);
            if (itemTemplate == null) return List.of();

            return itemTemplate.getDescription();
        });

        placeholders.register(prefix + "_abilities", context -> {
            ItemStack item = resolver.apply(context);
            if (item == null) return List.of();

            ItemTemplate itemTemplate = plugin.getItemTemplate(item);
            if (itemTemplate == null) return List.of();

            return itemTemplate.getAbilitiesDescription();
        });

        placeholders.register(prefix + "_creator", context -> {
            ItemStack item = resolver.apply(context);
            if (item == null || !item.hasItemMeta()) {
                return null;
            }
            return PersistentData.getString(item.getItemMeta(), plugin.getItemTemplates().getCreatorKey());
        });

        placeholders.register(prefix + "_uses", context -> {
            ItemStack item = resolver.apply(context);
            if (item == null) {
                return null;
            }
            return plugin.getItemTemplates().getUsesLeft(item);
        });

        placeholders.register(prefix + "_max_uses", context -> {
            ItemStack item = resolver.apply(context);
            if (item == null) {
                return null;
            }
            ItemTemplate itemTemplate = plugin.getItemTemplate(item);
            return itemTemplate != null ? itemTemplate.getMaxUses() : null;
        });

        for (Stat stat : plugin.getStats().getAll()) {
            placeholders.register(prefix + "_stat:" + stat.getName(), context -> {
                ItemStack item = resolver.apply(context);
                return item != null ? plugin.getItemTemplates().getStatLevel(item, stat) : null;
            });

            placeholders.register(prefix + "_stat_total:" + stat.getName(), context -> {
                ItemStack item = resolver.apply(context);
                return item != null ? plugin.getItemTemplates().getStatTotalLevel(item, stat) : null;
            });
        }

        placeholders.register(prefix + "_enchant_damage", context -> {
            ItemStack item = resolver.apply(context);
            return item != null ? settings.getDamageMultiplierFromSharpnessEnchant(item) : null;
        });

        placeholders.register(prefix + "_enchant_mining_speed", context -> {
            ItemStack item = resolver.apply(context);
            return item != null ? settings.getMiningSpeedFromEfficiencyEnchant(item) : null;
        });

        placeholders.register(prefix + "_enchant_mining_fortune", context -> {
            ItemStack item = resolver.apply(context);
            return item != null ? plugin.getSettings().getMiningFortuneFromFortuneEnchant(item) : null;
        });

        for (BreakingPower breakingPower : settings.getBreakingPowers()) {
            placeholders.register(prefix + "_" + breakingPower.getName(), context -> {
                ItemStack item = resolver.apply(context);
                ItemTemplate itemTemplate = plugin.getItemTemplate(item);
                if (itemTemplate == null) {
                    return null;
                }
                ToolBreakingPower toolBreakingPower = itemTemplate.getBreakingPower();
                if (toolBreakingPower == null || !breakingPower.equals(toolBreakingPower.getType())) {
                    return null;
                }
                return toolBreakingPower.getAmount();
            });
        }

        placeholders.register(prefix + "_stats", context -> {
            ItemStack item = context.item();
            if (item == null) return List.of();

            String statsDivider = settings.getItemLoreStatDivider();
            int maxStatLines = settings.getItemLoreMaxStats();
            Map<Stat, Integer> itemStats = plugin.getItemTemplates().getStats(item);

            // full multi-line descriptions
            List<String> statLines = new ArrayList<>();
            itemStats.forEach((stat, level) -> {
                Context statContext = context.copy()
                        .addPlaceholder("stat_name", stat.getName())
                        .addPlaceholder("stat_level", level);

                List<String> statDescription = settings.getItemStatDescription(stat);
                if (statsDivider != null && !statLines.isEmpty()) {
                    statLines.add(statsDivider);
                }

                statLines.addAll(PlaceholderUtil.parseAll(statContext, statDescription));
            });

            // collapse to one line per stat
            if (statLines.size() > maxStatLines) {
                statLines.clear();
                itemStats.forEach((stat, level) -> {
                    Context statContext = context.copy()
                            .addPlaceholder("stat_name", stat.getName())
                            .addPlaceholder("stat_level", level);

                    String statDescription = settings.getItemStatDescription(stat).get(0);
                    if (statsDivider != null && !statLines.isEmpty()) {
                        statLines.add(statsDivider);
                    }

                    statLines.add(PlaceholderUtil.parseAll(statContext, statDescription));
                });
            }

            // inline up to 3 per line
            if (statLines.size() > maxStatLines) {
                statLines.clear();
                StringJoiner joiner = new StringJoiner(", ");
                int count = 0;

                for (Map.Entry<Stat, Integer> entry : itemStats.entrySet()) {
                    Stat stat = entry.getKey();
                    int level = entry.getValue();

                    Context statContext = context.copy()
                            .addPlaceholder("stat_name", stat.getName())
                            .addPlaceholder("stat_level", level);

                    String statDescription = settings.getItemStatDescription(stat).get(0);
                    String parsed = PlaceholderUtil.parseAll(statContext, statDescription);

                    joiner.add(parsed);
                    count++;

                    if (count == 3) {
                        statLines.add(joiner.toString());
                        joiner = new StringJoiner(", ");
                        count = 0;
                    }
                }
                if (count > 0) {
                    statLines.add(joiner.toString());
                }
            }

            return statLines;
        });

        placeholders.register(prefix + "_enchants", context -> {
            ItemStack item = context.item();
            if (item == null) return null;

            String enchantsDivider = settings.getItemLoreEnchantDivider();
            int maxEnchantLines = settings.getItemLoreMaxEnchants();
            Map<Enchantment, Integer> itemEnchants = item.getEnchantments();

            // full multi-line descriptions
            List<String> enchantLines = new ArrayList<>();
            itemEnchants.forEach((enchant, level) -> {
                Context enchantContext = context.copy()
                        .addPlaceholder("enchant_name", EnchantUtil.getEnchantName(enchant))
                        .addPlaceholder("enchant_level", level);

                List<String> enchantDescription = settings.getItemEnchantDescription(enchant);
                if (enchantsDivider != null && !enchantLines.isEmpty()) {
                    enchantLines.add(enchantsDivider);
                }

                enchantLines.addAll(PlaceholderUtil.parseAll(enchantContext, enchantDescription));
            });

            // collapse to one line per enchant
            if (enchantLines.size() > maxEnchantLines) {
                enchantLines.clear();
                itemEnchants.forEach((enchant, level) -> {
                    Context enchantContext = context.copy()
                            .addPlaceholder("enchant_name", EnchantUtil.getEnchantName(enchant))
                            .addPlaceholder("enchant_level", level);

                    String enchantDescription = settings.getItemEnchantDescription(enchant).get(0);
                    if (enchantsDivider != null && !enchantLines.isEmpty()) {
                        enchantLines.add(enchantsDivider);
                    }

                    enchantLines.add(PlaceholderUtil.parseAll(enchantContext, enchantDescription));
                });
            }

            // inline up to 3 per line
            if (enchantLines.size() > maxEnchantLines) {
                enchantLines.clear();
                StringJoiner joiner = new StringJoiner(", ");
                int count = 0;

                for (Map.Entry<Enchantment, Integer> entry : itemEnchants.entrySet()) {
                    Enchantment enchant = entry.getKey();
                    int level = entry.getValue();

                    Context enchantContext = context.copy()
                            .addPlaceholder("enchant_name", EnchantUtil.getEnchantName(enchant))
                            .addPlaceholder("enchant_level", level);

                    String enchantDescription = settings.getItemEnchantDescription(enchant).get(0);
                    String parsed = PlaceholderUtil.parseAll(enchantContext, enchantDescription);

                    joiner.add(parsed);
                    count++;

                    if (count == 3) {
                        enchantLines.add(joiner.toString());
                        joiner = new StringJoiner(", ");
                        count = 0;
                    }
                }
                if (count > 0) {
                    enchantLines.add(joiner.toString());
                }
            }

            return enchantLines;
        });

    }

}
