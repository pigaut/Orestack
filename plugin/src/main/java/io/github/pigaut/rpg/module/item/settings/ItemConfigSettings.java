package io.github.pigaut.rpg.module.item.settings;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.item.power.*;
import io.github.pigaut.rpg.module.item.rarity.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.module.structure.block.matcher.*;
import io.github.pigaut.rpg.plugin.*;
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

    private List<String> defaultItemLore;

    private Map<String, List<String>> placeholderHeaders;
    private Map<String, List<String>> placeholderDividers;
    private Map<String, List<String>> placeholderFooters;
    private Map<String, Integer> placeholderMaxLines;

    private String defaultItemName;
    private String defaultDescriptionColor;

    private List<String> defaultStatDescription;
    private Map<Stat, List<String>> statDescriptions;

    private List<String> defaultEnchantDescription;
    private Map<Enchantment, List<String>> enchantDescriptions;

    private List<String> abilityDescriptionTemplate;

    private String defaultItemRarity;
    private Map<String, ItemRarity> itemRarityByName;

    private boolean breakingPowerEnabled;
    private int defaultBreakingPower;
    private Map<String, BreakingPower> breakingPowerByName;

    public ItemConfigSettings(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfiguration(@NotNull ConfigSection config) {
        defaultItemLore = config.getStringList("default-item-lore")
                .withDefault(List.of());

        placeholderHeaders = loadPlaceholderMap(config.getSectionOrCreate("item-placeholder-headers"));
        placeholderDividers = loadPlaceholderMap(config.getSectionOrCreate("item-placeholder-dividers"));
        placeholderFooters = loadPlaceholderMap(config.getSectionOrCreate("item-placeholder-footers"));

        System.out.println(placeholderFooters);

        placeholderMaxLines = new HashMap<>();
        for (KeyedScalar scalar : config.getSectionOrEmpty("item-placeholder-max-lines").getNestedScalars()) {
            String placeholder = scalar.getKey(CaseStyle.SNAKE);
            Integer maxLines = scalar.toInteger()
                    .require(Requirements.positive())
                    .withDefault(null);

            if (maxLines != null) {
                placeholderMaxLines.put(placeholder, maxLines);
            }
        }

        defaultItemName = config.getString("default-item-name")
                .withDefault(ChatColor.WHITE + "{item_name}");

        defaultDescriptionColor = config.getString("default-description-color")
                .withDefault(ChatColor.GRAY.toString());

        // Stat descriptions
        defaultStatDescription = config.getStringList("stat-descriptions.default")
                .withDefault(List.of(ChatColor.GRAY + "{stat_name_tc}: +{stat_level}"));

        statDescriptions = new HashMap<>();
        for (KeyedSequence sequence : config.getSectionOrCreate("stat-descriptions").getNestedSequences()) {
            String key = sequence.getKey();
            if (key.equals("default")) {
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
        defaultEnchantDescription = config.getStringList("enchant-descriptions.default")
                .withDefault(List.of(ChatColor.BLUE + "{enchant_name_tc} {enchant_level_rm}"));

        enchantDescriptions = new HashMap<>();
        for (KeyedSequence sequence : config.getSectionOrCreate("enchant-descriptions").getNestedSequences()) {
            String key = sequence.getKey();
            if (key.equals("default")) {
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

        // Ability description template (was ability-descriptions.format)
        abilityDescriptionTemplate = config.getStringList("ability-description-template")
                .withDefault(List.of("Ability: {name} {trigger}", "{description}", "Mana Cost: {mana_cost}"));

        // Breaking power (was breaking-power.*, now use-breaking-power / default-breaking-power / breaking-powers)
        breakingPowerEnabled = config.getBoolean("use-breaking-power")
                .withDefault(true);

        defaultBreakingPower = config.getInteger("default-breaking-power")
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

                String display = section.getString("display")
                        .withDefault(null);

                if (display == null) {
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

                breakingPowerByName.put(name, new BreakingPower(name, display, breakingPowerByBlock, onWrongTool, onInsufficientPower));
            }
        }

        // Item rarities
        defaultItemRarity = config.getString("default-item-rarity")
                .withDefault("common");

        if (defaultItemRarity.equals("none")) {
            defaultItemRarity = null;
        }

        itemRarityByName = new HashMap<>();
        for (KeyedSection raritySection : config.getSectionOrEmpty("item-rarities").getNestedSections()) {
            String rarityName = raritySection.getKey(CaseStyle.SNAKE);
            Context baseContext = Context.fromPlugin(plugin).addPlaceholder("name", rarityName);

            String baseDisplay = raritySection.getString("display")
                    .mapIfValid(string -> PlaceholderUtil.parseAll(baseContext, string))
                    .withDefault(null);

            String baseItemName = raritySection.getString("item-name")
                    .withDefault(null);

            if (baseDisplay == null || baseItemName == null) {
                continue;
            }

            ItemRarity baseRarity = new ItemRarity(rarityName, baseDisplay, baseItemName);
            itemRarityByName.put(rarityName, baseRarity);

            for (KeyedScalar variantScalar : raritySection.getSectionOrEmpty("variants").getNestedScalars()) {
                String variantType = variantScalar.getKey(CaseStyle.SNAKE);
                String variantName = rarityName + "_" + variantType;

                String rawValue = variantScalar.toString();
                if (rawValue.equalsIgnoreCase("default")) {
                    itemRarityByName.put(variantName, baseRarity);
                    continue;
                }

                Context variantContext = Context.fromPlugin(plugin).addPlaceholder("name", variantName);

                ConfigLine line = variantScalar.toLine(LineStyle.LABELED);
                String variantDisplay = line.getString("display")
                        .mapIfValid(string -> PlaceholderUtil.parseAll(variantContext, string))
                        .withDefault(baseDisplay);

                String variantItemName = line.getString("itemName")
                        .withDefault(baseItemName);

                itemRarityByName.put(variantName, new ItemRarity(variantName, variantDisplay, variantItemName));
            }
        }
    }

    private Map<String, List<String>> loadPlaceholderMap(@NotNull ConfigSection section) {
        Map<String, List<String>> map = new HashMap<>();
        for (KeyedSequence sequence : section.getNestedSequences()) {
            String placeholder = sequence.getKey(CaseStyle.SNAKE);
            List<String> lines = sequence.toStringList()
                    .withDefault(List.of());
            map.put(placeholder, lines);
        }
        return map;
    }

    public @NotNull List<String> getPlaceholderHeader(@NotNull String placeholder) {
        return new ArrayList<>(placeholderHeaders.getOrDefault(placeholder, List.of()));
    }

    public @NotNull List<String> getPlaceholderDivider(@NotNull String placeholder) {
        return new ArrayList<>(placeholderDividers.getOrDefault(placeholder, List.of()));
    }

    public @NotNull List<String> getPlaceholderFooter(@NotNull String placeholder) {
        return new ArrayList<>(placeholderFooters.getOrDefault(placeholder, List.of()));
    }

    public int getPlaceholderMaxLines(@NotNull String placeholder, int defaultValue) {
        return placeholderMaxLines.getOrDefault(placeholder, defaultValue);
    }

    @Override
    public @NotNull List<String> getDefaultItemLore() {
        return new ArrayList<>(defaultItemLore);
    }

    @Override
    public @NotNull String getDefaultItemName() {
        return defaultItemName;
    }

    @Override
    public @NotNull String getDefaultDescriptionColor() {
        return defaultDescriptionColor;
    }

    @Override
    public @NotNull List<String> getDescriptionHeader() {
        return getPlaceholderHeader("item_description");
    }

    @Override
    public @NotNull List<String> getDescriptionFooter() {
        return getPlaceholderFooter("item_description");
    }

    @Override
    public int getStatsDescriptionMaxLines() {
        return getPlaceholderMaxLines("item_stats", 10);
    }

    @Override
    public @NotNull List<String> getStatsDescriptionHeader() {
        return getPlaceholderHeader("item_stats");
    }

    @Override
    public @NotNull List<String> getStatsDescriptionDivider() {
        return getPlaceholderDivider("item_stats");
    }

    @Override
    public @NotNull List<String> getStatsDescriptionFooter() {
        return getPlaceholderFooter("item_stats");
    }

    @Override
    public @NotNull List<String> getStatDescription(@NotNull Stat statType) {
        return statDescriptions.getOrDefault(statType, defaultStatDescription);
    }

    @Override
    public int getEnchantsDescriptionMaxLines() {
        return getPlaceholderMaxLines("item_enchants", 10);
    }

    @Override
    public @NotNull List<String> getEnchantsDescriptionHeader() {
        return getPlaceholderHeader("item_enchants");
    }

    @Override
    public @NotNull List<String> getEnchantsDescriptionDivider() {
        return getPlaceholderDivider("item_enchants");
    }

    @Override
    public @NotNull List<String> getEnchantsDescriptionFooter() {
        return getPlaceholderFooter("item_enchants");
    }

    @Override
    public @NotNull List<String> getEnchantDescription(@NotNull Enchantment enchant) {
        return enchantDescriptions.getOrDefault(enchant, defaultEnchantDescription);
    }

    @Override
    public @NotNull List<String> getAbilityDescriptionHeader() {
        return getPlaceholderHeader("item_abilities");
    }

    @Override
    public @NotNull List<String> getAbilityDescriptionDivider() {
        return getPlaceholderDivider("item_abilities");
    }

    @Override
    public @NotNull List<String> getAbilityDescriptionFooter() {
        return getPlaceholderFooter("item_abilities");
    }

    @Override
    public @NotNull List<String> getAbilityDescriptionTemplate() {
        return new ArrayList<>(abilityDescriptionTemplate);
    }

    @Override
    public boolean isItemRarity(@NotNull String rarity) {
        return itemRarityByName.containsKey(rarity);
    }

    @Override
    public @Nullable String getDefaultItemRarity() {
        return defaultItemRarity;
    }

    public @Nullable ItemRarity getItemRarity(@NotNull String name) {
        ItemRarity rarity = itemRarityByName.get(name);
        if (rarity != null) {
            return rarity;
        }
        String defaultRarity = getDefaultItemRarity();
        return defaultRarity != null ? itemRarityByName.get(defaultRarity) : null;
    }

    @Override
    public @Nullable String getItemRarityDisplay(@NotNull String name) {
        ItemRarity rarity = getItemRarity(name);
        return rarity != null ? rarity.getDisplay() : null;
    }

    @Override
    public @Nullable String getItemNameByRarity(@NotNull String name) {
        ItemRarity rarity = getItemRarity(name);
        return rarity != null ? rarity.getItemName() : null;
    }

    @Override
    public @NotNull List<String> getItemRarityDescription(@NotNull String name) {
        ItemRarity rarity = getItemRarity(name);
        if (rarity == null) {
            return List.of();
        }

        List<String> description = new ArrayList<>();
        description.addAll(getPlaceholderHeader("item_rarity"));
        description.add(rarity.getDisplay());
        description.addAll(getPlaceholderFooter("item_rarity"));

        return description;
    }

    @Override
    public @NotNull List<String> getRarityHeader() {
        return getPlaceholderHeader("item_rarity");
    }

    @Override
    public @NotNull List<String> getRarityFooter() {
        return getPlaceholderFooter("item_rarity");
    }

    @Override
    public boolean isBreakingPower() {
        return breakingPowerEnabled;
    }

    @Override
    public int getDefaultBreakingPower() {
        return defaultBreakingPower;
    }

    @Override
    public @NotNull Set<BreakingPower> getBreakingPowers() {
        return new HashSet<>(breakingPowerByName.values());
    }

    @Override
    public @Nullable BlockBreakingPower getBlockBreakingPower(@NotNull Block block) {
        for (BreakingPower breakingPower : breakingPowerByName.values()) {
            BlockBreakingPower blockBreakingPower = breakingPower.fromBlock(block);
            if (blockBreakingPower != null) {
                return blockBreakingPower;
            }
        }
        return null;
    }

    @Override
    public @NotNull List<String> getBreakingPowerHeader() {
        return getPlaceholderHeader("item_breaking_power");
    }

    @Override
    public @NotNull List<String> getBreakingPowerFooter() {
        return getPlaceholderFooter("item_breaking_power");
    }

}