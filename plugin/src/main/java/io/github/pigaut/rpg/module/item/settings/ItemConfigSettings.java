package io.github.pigaut.rpg.module.item.settings;

import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.item.power.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.module.structure.block.matcher.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.node.line.*;
import io.github.pigaut.yaml.node.scalar.*;
import io.github.pigaut.yaml.node.section.*;
import io.github.pigaut.yaml.node.sequence.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.enchantments.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ItemConfigSettings implements ItemSettings {

    private final EnhancedPlugin plugin;
    private final Settings settings;

    public ItemConfigSettings(@NotNull EnhancedPlugin plugin, @NotNull Settings settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    private Set<String> itemCategories;
    private Set<String> itemRarities;

    private Integer defaultBreakingPower;
    private @Nullable String defaultItemCategory;
    private @Nullable String defaultItemRarity;
    private @Nullable String defaultItemName;
    private Map<String, String> itemNameByRarity;
    private String defaultDescriptionColor;
    private @Nullable List<String> defaultItemLore;

    private Map<String, List<String>> breakingPowerLore;
    private List<String> categoryLore;
    private List<String> descriptionLore;
    private List<String> craftedByLore;
    private Map<String, List<String>> rarityLore;
    private List<String> abilitiesLore;
    private List<String> statsLore;
    private List<String> enchantsLore;

    private @Nullable String abilityLoreDivider;
    private Map<String, List<String>> abilityTemplates;

    private Boolean breakingPowerEnabled;
    private Integer playerBreakingPower;
    private Map<String, BreakingPower> breakingPowerByName;

    private List<String> defaultStatDescription;
    private Map<Stat, List<String>> statDescriptions;

    private Integer statLoreMaxLines;
    private @Nullable String statLoreDivider;
    private List<String> defaultEnchantDescription;

    private Integer enchantLoreMaxLines;
    private @Nullable String enchantLoreDivider;
    private Map<Enchantment, List<String>> enchantDescriptions;

    public void loadConfiguration(@NotNull ConfigSection config) {
        itemCategories = config.getStringList("item-categories")
                .mapIfValid(HashSet::new)
                .withDefault(new HashSet<>(0));

        itemRarities = config.getStringList("item-rarities")
                .mapIfValid(LinkedHashSet::new)
                .withDefault(new LinkedHashSet<>(0));

        ConfigSection itemDefaults = config.getSectionOrEmpty("item-defaults");

        defaultBreakingPower = itemDefaults.getInteger("breaking-power")
                .require(Requirements.min(0))
                .withDefault(1);

        defaultItemCategory = itemDefaults.getString("category")
                .mapIfValid(category -> category.equals("none") ? null : category)
                .withDefault(null);

        defaultItemRarity = itemDefaults.getString("rarity")
                .mapIfValid(rarity -> rarity.equals("none") ? null : rarity)
                .withDefault(null);

        defaultItemName = itemDefaults.getString("name")
                .mapIfValid(name -> name.equals("none") ? null : name)
                .withDefault(null);

        itemNameByRarity = new HashMap<>();
        for (KeyedScalar scalar : itemDefaults.getSectionOrEmpty("name-by-rarity").getNestedScalars()) {
            String rarity = scalar.getKey(CaseStyle.SNAKE);
            itemNameByRarity.put(rarity, scalar.toString());
        }

        defaultDescriptionColor = itemDefaults.getString("description-color")
                .mapIfValid(color -> color.equals("none") ? "" : color)
                .withDefault("");

        if (!itemDefaults.getString("lore").orElse("").equals("none")) {
            defaultItemLore = itemDefaults.getStringList("lore")
                    .withDefault(null);
        }

        ConfigSection lorePlaceholders = config.getSectionOrCreate("lore-placeholders");
        breakingPowerLore = new HashMap<>();
        for (KeyedSequence sequence : lorePlaceholders.getSectionOrEmpty("breaking-power").getNestedSequences()) {
            String name = sequence.getKey(CaseStyle.SNAKE);
            List<String> lines = sequence.toStringList()
                    .requireEach(s -> !PlaceholderUtil.containsPlaceholder(s, "{item_lore:}"),
                            "Lore placeholders cannot be nested inside another lore placeholder's lines")
                    .withDefault(List.of());
            breakingPowerLore.put(name, lines);
        }

        categoryLore = lorePlaceholders.getStringList("category")
                .requireEach(s -> !PlaceholderUtil.containsPlaceholder(s, "{item_lore:}"),
                        "Lore placeholders cannot be nested inside another lore placeholder's lines")
                .withDefault(List.of());

        descriptionLore = lorePlaceholders.getStringList("description")
                .requireEach(s -> !PlaceholderUtil.containsPlaceholder(s, "{item_lore:}"),
                        "Lore placeholders cannot be nested inside another lore placeholder's lines")
                .withDefault(List.of());

        craftedByLore = lorePlaceholders.getStringList("crafted-by")
                .requireEach(s -> !PlaceholderUtil.containsPlaceholder(s, "{item_lore:}"),
                        "Lore placeholders cannot be nested inside another lore placeholder's lines")
                .withDefault(List.of());

        rarityLore = new HashMap<>();
        for (KeyedSequence sequence : lorePlaceholders.getSectionOrEmpty("rarity").getNestedSequences()) {
            String rarity = sequence.getKey(CaseStyle.SNAKE);
            List<String> lines = sequence.toStringList()
                    .requireEach(s -> !PlaceholderUtil.containsPlaceholder(s, "{item_lore:}"),
                            "Lore placeholders cannot be nested inside another lore placeholder's lines")
                    .withDefault(List.of());
            rarityLore.put(rarity, lines);
        }

        abilitiesLore = lorePlaceholders.getStringList("abilities")
                .requireEach(s -> !PlaceholderUtil.containsPlaceholder(s, "{item_lore:}"),
                        "Lore placeholders cannot be nested inside another lore placeholder's lines")
                .withDefault(List.of());

        statsLore = lorePlaceholders.getStringList("stats")
                .requireEach(s -> !PlaceholderUtil.containsPlaceholder(s, "{item_lore:}"),
                        "Lore placeholders cannot be nested inside another lore placeholder's lines")
                .withDefault(List.of());

        enchantsLore = lorePlaceholders.getStringList("enchants")
                .requireEach(s -> !PlaceholderUtil.containsPlaceholder(s, "{item_lore:}"),
                        "Lore placeholders cannot be nested inside another lore placeholder's lines")
                .withDefault(List.of());

        abilityLoreDivider = config.getString("ability-templates.divider")
                .mapIfValid(divider -> divider.equals("none") ? null : divider)
                .withDefault(null);

        abilityTemplates = new LinkedHashMap<>();
        for (KeyedSequence sequence : config.getSectionOrEmpty("ability-templates").getNestedSequences()) {
            String name = sequence.getKey(CaseStyle.SNAKE);
            if (StringUtil.isAnyEqual(name, "divider")) {
                continue;
            }

            List<String> template = sequence.toStringList().withDefault(List.of());
            abilityTemplates.put(name, template);
        }

        // Breaking power
        breakingPowerEnabled = config.getBoolean("use-breaking-power")
                .withDefault(true);

        playerBreakingPower = config.getInteger("player-breaking-power")
                .require(Requirements.min(0))
                .withDefault(1);

        breakingPowerByName = new HashMap<>();
        if (breakingPowerEnabled) {
            for (KeyedSection section : config.getSectionOrEmpty("breaking-powers").getNestedSections()) {
                String name = section.getKey(CaseStyle.SNAKE);

                if (!name.endsWith("_power")) {
                    config.collectError(new InvalidConfigException(section, name, "Breaking power name must end with '-power' to avoid conflicts"));
                    continue;
                }

                BlockMatcherMap<Integer> breakingPowerByBlock = new BlockMatcherMap<>();
                for (KeyedScalar scalar : section.getSectionOrEmpty("block-requirements").getNestedScalars()) {
                    BlockMatcher blockMatcher = scalar.getKeyAs(BlockMatcher.class)
                            .withDefault(null);

                    ConfigLine line = scalar.toLine(LineStyle.SPACED, "<amount> power")
                            .withDefault(null);
                    if (line == null) {
                        continue;
                    }

                    Integer amount = line.getInteger(0)
                            .require(Requirements.positive())
                            .withDefault(null);

                    if (blockMatcher != null && amount != null) {
                        breakingPowerByBlock.put(blockMatcher, amount);
                    }
                }

                Function onWrongTool = section.get("on-wrong-tool", Function.class)
                        .withDefault(null);

                Function onInsufficientPower = section.get("on-insufficient-power", Function.class)
                        .withDefault(null);

                breakingPowerByName.put(name, new BreakingPower(name, breakingPowerByBlock, onWrongTool, onInsufficientPower));
            }
        }

        // Stat descriptions
        ConfigSection statDescriptionsSection = config.getSectionOrEmpty("stat-descriptions");
        statLoreMaxLines = statDescriptionsSection.getInteger("max-lines")
                .require(Requirements.positive())
                .withDefault(10);

        statLoreDivider = statDescriptionsSection.getString("divider")
                .mapIfValid(divider -> divider.equals("none") ? null : divider)
                .withDefault(null);

        defaultStatDescription = statDescriptionsSection.getStringList("default")
                .withDefault(List.of(ChatColor.GRAY + "{stat_name_tc}: +{stat_level}"));

        statDescriptions = new HashMap<>();
        for (KeyedSequence sequence : statDescriptionsSection.getNestedSequences()) {
            String key = sequence.getKey();
            if (StringUtil.isAnyEqual(key, "default", "max-lines", "divider")) {
                continue;
            }

            Stat stat = sequence.getKeyAs(Stat.class)
                    .withDefault(null);

            List<String> statDescription = sequence.toStringList()
                    .require(Requirements.minSize(1))
                    .withDefault(null);

            if (stat != null && statDescription != null) {
                statDescriptions.put(stat, statDescription);
            }
        }

        // Enchant descriptions
        ConfigSection enchantDescriptionsSection = config.getSectionOrEmpty("enchant-descriptions");
        enchantLoreMaxLines = enchantDescriptionsSection.getInteger("max-lines")
                .require(Requirements.positive())
                .withDefault(10);

        enchantLoreDivider = enchantDescriptionsSection.getString("divider")
                .mapIfValid(divider -> divider.equals("none") ? null : divider)
                .withDefault(null);

        defaultEnchantDescription = enchantDescriptionsSection.getStringList("default")
                .withDefault(List.of(ChatColor.BLUE + "{enchant_name_tc} {enchant_level_rm}"));

        enchantDescriptions = new HashMap<>();
        for (KeyedSequence sequence : enchantDescriptionsSection.getNestedSequences()) {
            String key = sequence.getKey();
            if (StringUtil.isAnyEqual(key, "default", "max-lines", "divider")) {
                continue;
            }

            Enchantment enchant = sequence.getKeyAs(Enchantment.class)
                    .withDefault(null);

            List<String> enchantDescription = sequence.toStringList()
                    .require(Requirements.minSize(1))
                    .withDefault(null);

            if (enchant != null && enchantDescription != null) {
                enchantDescriptions.put(enchant, enchantDescription);
            }
        }

    }

    @Override
    public boolean isItemCategory(@NotNull String category) {
        settings.checkLoaded(itemCategories);
        return itemCategories.contains(category);
    }

    @Override
    public boolean isItemRarity(@NotNull String rarity) {
        settings.checkLoaded(itemRarities);
        return itemRarities.contains(rarity);
    }

    @Override
    public @NotNull Set<String> getItemCategories() {
        settings.checkLoaded(itemCategories);
        return new HashSet<>(itemCategories);
    }

    @Override
    public @NotNull Set<String> getItemRarities() {
        settings.checkLoaded(itemRarities);
        return new LinkedHashSet<>(itemRarities);
    }

    @Override
    public int getDefaultBreakingPower() {
        settings.checkLoaded(defaultBreakingPower);
        return defaultBreakingPower;
    }

    @Override
    public @Nullable String getDefaultItemCategory() {
        return defaultItemCategory;
    }

    @Override
    public @Nullable String getDefaultItemRarity() {
        return defaultItemRarity;
    }

    @Override
    public @Nullable String getDefaultItemName() {
        return defaultItemName;
    }

    @Override
    public @Nullable String getItemNameByRarity(@NotNull String rarity) {
        settings.checkLoaded(itemNameByRarity);
        return itemNameByRarity.get(rarity);
    }

    @Override
    public @NotNull String getDefaultDescriptionColor() {
        settings.checkLoaded(defaultDescriptionColor);
        return defaultDescriptionColor;
    }

    @Override
    public @Nullable List<String> getDefaultItemLore() {
        return defaultItemLore != null ? new ArrayList<>(defaultItemLore) : null;
    }

    @Override
    public @NotNull List<String> getItemBreakingPowerLore(@NotNull String breakingPower) {
        settings.checkLoaded(breakingPowerLore);
        return new ArrayList<>(breakingPowerLore.getOrDefault(breakingPower, List.of()));
    }

    @Override
    public @NotNull List<String> getItemCategoryLore() {
        settings.checkLoaded(categoryLore);
        return new ArrayList<>(categoryLore);
    }

    @Override
    public @NotNull List<String> getItemDescriptionLore() {
        settings.checkLoaded(descriptionLore);
        return new ArrayList<>(descriptionLore);
    }

    @Override
    public @NotNull List<String> getItemCraftedByLore() {
        settings.checkLoaded(craftedByLore);
        return new ArrayList<>(craftedByLore);
    }

    @Override
    public @NotNull List<String> getItemRarityLore(@NotNull String rarity) {
        settings.checkLoaded(rarityLore);
        return new ArrayList<>(rarityLore.getOrDefault(rarity, List.of()));
    }

    @Override
    public @NotNull List<String> getItemAbilitiesLore() {
        settings.checkLoaded(abilitiesLore);
        return new ArrayList<>(abilitiesLore);
    }

    @Override
    public @NotNull List<String> getItemStatsLore() {
        settings.checkLoaded(statsLore);
        return new ArrayList<>(statsLore);
    }

    @Override
    public @NotNull List<String> getItemEnchantsLore() {
        settings.checkLoaded(enchantsLore);
        return new ArrayList<>(enchantsLore);
    }

    @Override
    public boolean isAbilityTemplate(@NotNull String name) {
        settings.checkLoaded(abilityTemplates);
        return abilityTemplates.containsKey(name);
    }

    @Override
    public @Nullable String getAbilityLoreDivider() {
        return abilityLoreDivider;
    }

    @Override
    public @NotNull Set<String> getAbilityTemplateNames() {
        settings.checkLoaded(abilityTemplates);
        return new HashSet<>(abilityTemplates.keySet());
    }

    @Override
    public @Nullable List<String> getAbilityTemplate(@NotNull String name) {
        settings.checkLoaded(abilityTemplates);
        List<String> abilityTemplate = abilityTemplates.get(name);
        return abilityTemplate != null ? new ArrayList<>(abilityTemplate) : null;
    }

    @Override
    public boolean isBreakingPower() {
        settings.checkLoaded(breakingPowerEnabled);
        return breakingPowerEnabled;
    }

    @Override
    public int getPlayerBreakingPower() {
        settings.checkLoaded(playerBreakingPower);
        return playerBreakingPower;
    }

    @Override
    public @NotNull Set<BreakingPower> getBreakingPowers() {
        settings.checkLoaded(breakingPowerByName);
        return new HashSet<>(breakingPowerByName.values());
    }

    @Override
    public @Nullable BlockBreakingPower getBlockBreakingPower(@NotNull Block block) {
        settings.checkLoaded(breakingPowerByName);
        for (BreakingPower breakingPower : breakingPowerByName.values()) {
            BlockBreakingPower blockBreakingPower = breakingPower.fromBlock(block);
            if (blockBreakingPower != null) {
                return blockBreakingPower;
            }
        }
        return null;
    }

    @Override
    public int getItemLoreMaxStats() {
        settings.checkLoaded(statLoreMaxLines);
        return statLoreMaxLines;
    }

    @Override
    public int getItemLoreMaxEnchants() {
        settings.checkLoaded(enchantLoreMaxLines);
        return enchantLoreMaxLines;
    }

    @Override
    public @Nullable String getItemLoreStatDivider() {
        return statLoreDivider;
    }

    @Override
    public @NotNull List<String> getItemStatDescription(@NotNull Stat stat) {
        settings.checkLoaded(statDescriptions);
        settings.checkLoaded(defaultStatDescription);
        return statDescriptions.getOrDefault(stat, defaultStatDescription);
    }

    @Override
    public @Nullable String getItemLoreEnchantDivider() {
        return enchantLoreDivider;
    }

    @Override
    public @NotNull List<String> getItemEnchantDescription(@NotNull Enchantment enchant) {
        settings.checkLoaded(enchantDescriptions);
        settings.checkLoaded(defaultEnchantDescription);
        return enchantDescriptions.getOrDefault(enchant, defaultEnchantDescription);
    }

}