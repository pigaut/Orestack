package io.github.pigaut.rpg.module.item;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.item.power.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.convert.format.*;
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

        placeholders.register(prefix + "_uses", context -> {
            ItemStack item = resolver.apply(context);
            if (item == null) {
                return null;
            }
            return plugin.getItems().getUsesLeft(item);
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
                return item != null ? plugin.getItems().getStatLevel(item, stat) : null;
            });

            placeholders.register(prefix + "_stat_total:" + stat.getName(), context -> {
                ItemStack item = resolver.apply(context);
                return item != null ? plugin.getItems().getStatTotalLevel(item, stat) : null;
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

        placeholders.register(prefix + "_stats", context -> {
            ItemStack item = resolver.apply(context);
            if (item == null) {
                return null;
            }

            List<String> statsHeader = settings.getStatsDescriptionHeader();
            List<String> statsDivider = settings.getStatsDescriptionDivider();
            List<String> statsFooter = settings.getStatsDescriptionFooter();
            int maxStatLines = settings.getStatsDescriptionMaxLines();

            Map<Stat, Integer> stats = plugin.getItems().getStats(item);

            // full multi-line descriptions
            List<String> statLines = new ArrayList<>();
            stats.forEach((stat, level) -> {
                Context statContext = context.copy()
                        .addPlaceholder("stat_name", stat.getName())
                        .addPlaceholder("stat_level", level);

                List<String> statDescription = settings.getStatDescription(stat);
                if (!statLines.isEmpty() && !statsDivider.isEmpty()) {
                    statLines.addAll(statsDivider);
                }

                statLines.addAll(PlaceholderUtil.parseAll(statContext, statDescription));
            });

            // collapse to one line per enchant
            if (statLines.size() > maxStatLines) {
                statLines.clear();
                stats.forEach((stat, level) -> {
                    Context statContext = context.copy()
                            .addPlaceholder("enchant_name", stat.getName())
                            .addPlaceholder("enchant_level", level);

                    String statDescription = settings.getStatDescription(stat).get(0);
                    if (!statLines.isEmpty() && !statsDivider.isEmpty()) {
                        statLines.addAll(statsDivider);
                    }

                    statLines.add(PlaceholderUtil.parseAll(statContext, statDescription));
                });
            }

            // inline up to 3 per line
            if (statLines.size() > maxStatLines) {
                statLines.clear();
                StringJoiner joiner = new StringJoiner(", ");
                int count = 0;

                for (Map.Entry<Stat, Integer> entry : stats.entrySet()) {
                    Stat stat = entry.getKey();
                    int level = entry.getValue();

                    Context statContext = context.copy()
                            .addPlaceholder("stat_name", stat.getName())
                            .addPlaceholder("stat_level", level);

                    String statDescription = settings.getStatDescription(stat).get(0);
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

            // Header/footer
            if (!statLines.isEmpty()) {
                if (!statsHeader.isEmpty()) {
                    statLines.addAll(0, statsHeader);
                }
                if (!statsFooter.isEmpty()) {
                    statLines.addAll(statsFooter);
                }
            }

            return statLines;
        });

        placeholders.register(prefix + "_enchants", context -> {
            ItemStack item = resolver.apply(context);
            if (item == null) {
                return null;
            }

            List<String> enchantsHeader = settings.getEnchantsDescriptionHeader();
            List<String> enchantsDivider = settings.getEnchantsDescriptionDivider();
            List<String> enchantsFooter = settings.getEnchantsDescriptionFooter();
            int maxEnchantLines = settings.getEnchantsDescriptionMaxLines();

            Map<Enchantment, Integer> enchants = item.getEnchantments();

            // full multi-line descriptions
            List<String> enchantLines = new ArrayList<>();
            enchants.forEach((enchant, level) -> {
                Context enchantContext = context.copy()
                        .addPlaceholder("enchant_name", EnchantUtil.getEnchantName(enchant))
                        .addPlaceholder("enchant_level", level);

                List<String> enchantDescription = settings.getEnchantDescription(enchant);
                if (!enchantLines.isEmpty() && !enchantsDivider.isEmpty()) {
                    enchantLines.addAll(enchantsDivider);
                }

                enchantLines.addAll(PlaceholderUtil.parseAll(enchantContext, enchantDescription));
            });

            // collapse to one line per enchant
            if (enchantLines.size() > maxEnchantLines) {
                enchantLines.clear();
                enchants.forEach((enchant, level) -> {
                    Context enchantContext = context.copy()
                            .addPlaceholder("enchant_name", EnchantUtil.getEnchantName(enchant))
                            .addPlaceholder("enchant_level", level);

                    String enchantDescription = settings.getEnchantDescription(enchant).get(0);
                    if (!enchantLines.isEmpty() && !enchantsDivider.isEmpty()) {
                        enchantLines.addAll(enchantsDivider);
                    }

                    enchantLines.add(PlaceholderUtil.parseAll(enchantContext, enchantDescription));
                });
            }

            // inline up to 3 per line
            if (enchantLines.size() > maxEnchantLines) {
                enchantLines.clear();
                StringJoiner joiner = new StringJoiner(", ");
                int count = 0;

                for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                    Enchantment enchant = entry.getKey();
                    int level = entry.getValue();

                    Context enchantContext = context.copy()
                            .addPlaceholder("enchant_name", EnchantUtil.getEnchantName(enchant))
                            .addPlaceholder("enchant_level", level);

                    String enchantDescription = settings.getEnchantDescription(enchant).get(0);
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

            // Header/footer
            if (!enchantLines.isEmpty()) {
                if (!enchantsHeader.isEmpty()) {
                    enchantLines.addAll(0, enchantsHeader);
                }
                if (!enchantsFooter.isEmpty()) {
                    enchantLines.addAll(enchantsFooter);
                }
            }

            return enchantLines;
        });


        // Custom item placeholders (description, stats, abilities, rarity)
        placeholders.register(prefix + "_description", context -> {
            ItemStack item = resolver.apply(context);
            return item != null ? plugin.getItems().getDescription(item) : null;
        });

        placeholders.register(prefix + "_abilities", context -> {
            ItemStack item = resolver.apply(context);
            return item != null ? plugin.getItems().getAbilitiesDescription(item) : null;
        });

        placeholders.register(prefix + "_breaking_power", context -> {
            ItemStack item = resolver.apply(context);
            ItemTemplate itemTemplate = plugin.getItemTemplate(item);
            if (itemTemplate == null) {
                return List.of();
            }
            ToolBreakingPower toolBreakingPower = itemTemplate.getBreakingPower();
            if (toolBreakingPower == null) {
                return List.of();
            }

            List<String> description = new ArrayList<>();
            description.addAll(settings.getBreakingPowerHeader());
            description.add(toolBreakingPower.getDisplay());
            description.addAll(settings.getBreakingPowerFooter());

            return description;
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

        placeholders.register(prefix + "_rarity", context -> {
            ItemStack item = resolver.apply(context);
            String rarity = plugin.getItems().getRarity(item);
            return rarity != null ? settings.getItemRarityDescription(rarity) : List.of();
        });

    }

}
