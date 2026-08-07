package io.github.pigaut.rpg.module.item.settings;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.tag.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.item.power.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.node.scalar.*;
import io.github.pigaut.yaml.node.section.*;
import io.github.pigaut.yaml.node.sequence.*;
import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SimpleItemSettings implements ItemSettings {

    private List<String> lorePartsHeader;
    private List<String> lorePartsFooter;
    private String loreFormat;
    private List<String> defaultItemLore;

    private List<String> abilityHeader;
    private List<String> abilityDivider;
    private List<String> abilityFooter;
    private List<String> abilityFormat;

    private int maxStatLines;
    private List<String> statsHeader;
    private List<String> statsDivider;
    private List<String> statsFooter;
    private List<String> defaultStatDescription;
    private Map<Stat, List<String>> statDescriptions;

    private int maxEnchantLines;
    private List<String> enchantsHeader;
    private List<String> enchantsDivider;
    private List<String> enchantsFooter;
    private List<String> defaultEnchantDescription;
    private Map<Enchantment, List<String>> enchantDescriptions;

    private String defaultItemRarity;
    private Map<String, String> itemRarityByName;

    private boolean breakingPower;
    private Map<String, BreakingPower> breakingPowerByName;

    public void loadConfigurationData(@NotNull RootSection config) {
        // Default lore
        lorePartsHeader = config.getStringList("default-item-lore.header", ColorUtil.FORMATTER)
                .withDefault(List.of());

        lorePartsFooter = config.getStringList("default-item-lore.footer", ColorUtil.FORMATTER)
                .withDefault(List.of(""));

        loreFormat = config.getString("default-item-lore.color|format", ColorUtil.FORMATTER)
                .withDefault(ChatColor.GRAY.toString());

        defaultItemLore = new ArrayList<>();
        List<String> loreParts = config.getStringList("default-item-lore.parts", ColorUtil.FORMATTER)
                .withDefault(List.of());

        for (int i = 0; i < loreParts.size(); i++) {
            String lorePart = loreParts.get(i);

            // Skip header/footer for placeholders that support their own
            if (lorePart.contains("{item_stats}") || lorePart.contains("{item_enchants}") || lorePart.contains("{item_abilities}")) {
                defaultItemLore.add(lorePart);
                continue;
            }

            defaultItemLore.addAll(lorePartsHeader);
            defaultItemLore.add(ColorUtil.startsWithColor(lorePart) ? lorePart : (loreFormat + lorePart));
            if (i != loreParts.size() - 1) {
                defaultItemLore.addAll(lorePartsFooter);
            }
        }

        // Ability descriptions
        abilityHeader = config.getStringList("ability-descriptions.header", ColorUtil.FORMATTER)
                .withDefault(List.of());

        abilityDivider = config.getStringList("ability-descriptions.divider", ColorUtil.FORMATTER)
                .withDefault(List.of());

        abilityFooter = config.getStringList("ability-descriptions.footer", ColorUtil.FORMATTER)
                .withDefault(List.of());

        abilityFormat = config.getStringList("ability-descriptions.format", ColorUtil.FORMATTER)
                .withDefault(List.of("Ability: {name} {trigger}", "{description}", "Mana Cost: {mana_cost}"));

        // Stat descriptions
        maxStatLines = config.getInteger("stat-descriptions.max-lines")
                .withDefault(10);

        statsHeader = config.getStringList("stat-descriptions.header", ColorUtil.FORMATTER)
                .withDefault(List.of());

        statsDivider = config.getStringList("stat-descriptions.divider", ColorUtil.FORMATTER)
                .withDefault(List.of());

        statsFooter = config.getStringList("stat-descriptions.footer", ColorUtil.FORMATTER)
                .withDefault(List.of());

        defaultStatDescription = config.getStringList("stat-descriptions.default", ColorUtil.FORMATTER)
                .withDefault(List.of(ChatColor.GRAY + "{stat_name_tc} {stat_level_rm}"));

        statDescriptions = new HashMap<>();
        for (KeyedSequence sequence : config.getSectionOrCreate("stat-descriptions").getNestedSequences()) {
            String key = sequence.getKey();
            if (ObjectUtil.isAnyEqual(key, "header", "divider", "footer", "max-lines", "default")) {
                continue;
            }

            Stat stat = sequence.getKeyAs(Stat.class)
                    .withDefault(null);

            List<String> statDescription = sequence.toStringList(ColorUtil.FORMATTER)
                    .require(Requirements.minSize(1))
                    .withDefault(null);

            if (stat != null && statDescription != null) {
                statDescriptions.put(stat, statDescription);
            }
        }

        // Enchant descriptions
        maxEnchantLines = config.getInteger("enchant-descriptions.max-lines")
                .withDefault(10);

        enchantsHeader = config.getStringList("enchant-descriptions.header", ColorUtil.FORMATTER)
                .withDefault(List.of());

        enchantsDivider = config.getStringList("enchant-descriptions.divider", ColorUtil.FORMATTER)
                .withDefault(List.of());

        enchantsFooter = config.getStringList("enchant-descriptions.footer", ColorUtil.FORMATTER)
                .withDefault(List.of());

        defaultEnchantDescription = config.getStringList("enchant-descriptions.default", ColorUtil.FORMATTER)
                .withDefault(List.of(ChatColor.GRAY + "{enchant_name} {enchant_level_rm}"));

        enchantDescriptions = new HashMap<>();
        for (KeyedSequence sequence : config.getSectionOrCreate("enchant-descriptions").getNestedSequences()) {
            String key = sequence.getKey();
            if (ObjectUtil.isAnyEqual(key, "header", "divider", "footer", "max-lines", "default")) {
                continue;
            }

            Enchantment enchant = sequence.getKeyAs(Enchantment.class)
                    .withDefault(null);

            List<String> enchantDescription = sequence.toStringList(ColorUtil.FORMATTER)
                    .require(Requirements.minSize(1))
                    .withDefault(null);

            if (enchant != null && enchantDescription != null) {
                enchantDescriptions.put(enchant, enchantDescription);
            }
        }

        // Item rarity
        defaultItemRarity = config.getString("default-item-rarity")
                .withDefault("common");

        if (defaultItemRarity.equals("none")) {
            defaultItemRarity = null;
        }

        itemRarityByName = new HashMap<>();
        for (KeyedScalar scalar : config.getNestedScalars("item-rarities")) {
            String name = scalar.getKey(CaseStyle.SNAKE);
            String rarity = scalar.toString(ColorUtil.FORMATTER);
            itemRarityByName.put(name, rarity);
        }

        breakingPower = config.getBoolean("breaking-power.enabled")
                .withDefault(true);

        breakingPowerByName = new HashMap<>();
        if (breakingPower) {
            for (KeyedSection section : config.getNestedSections("breaking-power")) {
                String name = section.getKey(CaseStyle.SNAKE);
                if (name.equals("enabled")) {
                    continue;
                }

                if (!name.endsWith("-power")) {
                    config.collectError(new InvalidConfigException(section, name, "Breaking power name must end with '-power' to avoid conflicts"));
                }

                String display = section.getString("display", ColorUtil.FORMATTER)
                        .withDefault(null);

                if (display == null) {
                    continue;
                }

                Map<Material, Integer> breakingPowersByBlock = new HashMap<>();
                outer:
                for (KeyedScalar scalar : section.getNestedScalars("blocks")) {
                    MaterialTag tag = scalar.getKeyAs(MaterialTag.class)
                            .withDefault(null);

                    Integer amount = scalar.toInteger()
                            .require(Requirements.positive())
                            .withDefault(null);

                    if (tag != null && amount != null) {
                        for (Material material : tag.getMaterials()) {
                            if (!material.isBlock()) {
                                config.collectError(new InvalidConfigException(scalar, "Expected a block but found: " + material));
                                break outer;
                            }
                            breakingPowersByBlock.put(material, amount);
                        }
                    }
                }

                Function onInsufficientPower = section.get("on-insufficient-power", Function.class)
                                .withDefault(null);

                breakingPowerByName.put(name, new BreakingPower(name, display, breakingPowersByBlock, onInsufficientPower));
            }
        }

    }

    @Override
    public @NotNull List<String> getAbilityDescriptionHeader() {
        return new ArrayList<>(abilityHeader);
    }

    @Override
    public @NotNull List<String> getAbilityDescriptionDivider() {
        return new ArrayList<>(abilityDivider);
    }

    @Override
    public @NotNull List<String> getAbilityDescriptionFooter() {
        return new ArrayList<>(abilityFooter);
    }

    @Override
    public @NotNull List<String> getAbilityDescriptionFormat() {
        return new ArrayList<>(abilityFormat);
    }

    @Override
    public @Nullable String getDefaultItemRarity() {
        return defaultItemRarity;
    }

    @Override
    public boolean isItemRarity(@NotNull String rarity) {
        return itemRarityByName.containsKey(rarity);
    }

    @Override
    public @Nullable String getItemRarityDisplay(@NotNull String name) {
        String foundRarity = itemRarityByName.get(name);
        if (foundRarity != null) {
            return foundRarity;
        }
        String defaultRarity = getDefaultItemRarity();
        return defaultRarity != null ? itemRarityByName.get(defaultRarity) : null;
    }

    @Override
    public int getStatsDescriptionMaxLines() {
        return maxStatLines;
    }

    @Override
    public @NotNull List<String> getStatsDescriptionHeader() {
        return new ArrayList<>(statsHeader);
    }

    @Override
    public @NotNull List<String> getStatsDescriptionDivider() {
        return new ArrayList<>(statsDivider);
    }

    @Override
    public @NotNull List<String> getStatsDescriptionFooter() {
        return new ArrayList<>(statsFooter);
    }

    @Override
    public @NotNull List<String> getStatDescription(@NotNull Stat statType) {
        return statDescriptions.getOrDefault(statType, defaultStatDescription);
    }

    @Override
    public int getEnchantsDescriptionMaxLines() {
        return maxEnchantLines;
    }

    @Override
    public @NotNull List<String> getEnchantsDescriptionHeader() {
        return new ArrayList<>(enchantsHeader);
    }

    @Override
    public @NotNull List<String> getEnchantsDescriptionDivider() {
        return new ArrayList<>(enchantsDivider);
    }

    @Override
    public @NotNull List<String> getEnchantsDescriptionFooter() {
        return new ArrayList<>(enchantsFooter);
    }

    @Override
    public @NotNull List<String> getEnchantDescription(@NotNull Enchantment enchant) {
        return enchantDescriptions.getOrDefault(enchant, defaultEnchantDescription);
    }

    @Override
    public @NotNull List<String> getLorePartsHeader() {
        return new ArrayList<>(lorePartsHeader);
    }

    @Override
    public @NotNull List<String> getLorePartsFooter() {
        return new ArrayList<>(lorePartsFooter);
    }

    @Override
    public @NotNull String getLoreFormat() {
        return loreFormat;
    }

    @Override
    public @NotNull List<String> getDefaultItemLore() {
        return new ArrayList<>(defaultItemLore);
    }

    @Override
    public boolean isBreakingPower() {
        return breakingPower;
    }

    @Override
    public int getBlockBreakingPowerAmount(@NotNull Material block) {
        for (BreakingPower breakingPower : breakingPowerByName.values()) {
            Integer amount = breakingPower.getAmount(block);
            if (amount != null) {
                return amount;
            }
        }
        return 1;
    }

    @Override
    public @Nullable BreakingPower getBlockBreakingPower(@NotNull Material block) {
        for (BreakingPower breakingPower : breakingPowerByName.values()) {
            if (breakingPower.containsBlock(block)) {
                return breakingPower;
            }
        }
        return null;
    }

    @Override
    public @NotNull Set<String> getBreakingPowerNames() {
        return new HashSet<>(breakingPowerByName.keySet());
    }

    @Override
    public @NotNull Set<BreakingPower> getBreakingPowers() {
        return new HashSet<>(breakingPowerByName.values());
    }

}
