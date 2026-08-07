package io.github.pigaut.rpg.plugin;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.drop.settings.*;
import io.github.pigaut.rpg.core.gameplay.settings.*;
import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.core.hologram.style.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.core.placeholder.settings.*;
import io.github.pigaut.rpg.core.progressbar.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.item.power.*;
import io.github.pigaut.rpg.module.item.settings.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.module.stat.custom.*;
import io.github.pigaut.rpg.module.stat.settings.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.manager.module.Module;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.delay.*;
import io.github.pigaut.yaml.node.*;
import io.github.pigaut.yaml.node.section.*;
import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

public class Settings implements ConfigBacked, GameplaySettings, DropSettings, StatSettings, ItemSettings, PlaceholderSettings {

    protected final EnhancedPlugin plugin;

    private final SimpleGameplaySettings gameplaySettings;
    private final SimpleDropSettings dropSettings;
    private final SimpleItemSettings itemSettings;
    private final SimpleStatSettings statSettings;
    private final SimplePlaceholderSettings placeholderSettings;

    // Shortcuts
    private boolean shortcuts;
    private Map<String, String> configShortcuts;

    // Generic settings
    private boolean keepConfigUpToDate;
    private boolean debug;
    private boolean showReloadErrors;
    private boolean generateLanguageFiles;
    private boolean coloredConsole;
    private boolean generateExamples;
    private boolean checkForUpdates;
    private boolean metrics;
    private boolean dumpLogo;
    private String languageFileName;
    private Delay autoSave;
    private Delay worldLoadTimeout;
    private Delay playerCacheDuration;

    // Modules
    private Set<Module> disabledModules;

    // Mobs
    private String playerSlainMessage;

    // Holograms
    private int hologramViewDistance;
    private HologramProvider preferredHologramProvider;
    private HologramStyle defaultHologramStyle;
    private Map<String, HologramStyle> hologramStyles;

    // Noise
    private final NamespacedKey wandKey;
    public int guiReopenDelay = 40;
    private Set<Material> structureBlacklist;
    private ItemStack structureWand;
    private Set<String> functionNames;

    public Settings(EnhancedPlugin plugin) {
        this.plugin = plugin;
        this.wandKey = plugin.getNamespacedKey("wand");
        this.gameplaySettings = new SimpleGameplaySettings();
        this.dropSettings = new SimpleDropSettings();
        this.itemSettings = new SimpleItemSettings();
        this.statSettings = new SimpleStatSettings(plugin);
        this.placeholderSettings = new SimplePlaceholderSettings();
    }

    @Override
    public @NotNull ErrorCollector loadConfigurationData() {
        RootSection config = plugin.getConfiguration();

        shortcuts = config.getBoolean("shortcuts")
                .withDefault(true);

        configShortcuts = new HashMap<>();
        if (shortcuts) {
            for (String key : config.getKeys()) {
                if (!key.endsWith("-config-shortcuts")) {
                    continue;
                }

                ConfigSection argSection = config.getSection(key).withDefault(null);
                if (argSection == null) {
                    continue;
                }

                for (String shortcut : argSection.getKeys()) {
                    String value = argSection.getString(shortcut).withDefault(null);
                    if (value != null) {
                        configShortcuts.put(shortcut, value);
                    }
                }
            }
            applyConfigShortcuts(config);
        }

        keepConfigUpToDate = config.getBoolean("keep-file-up-to-date")
                .withDefault(true);

        debug = config.getBoolean("debug")
                .withDefault(false);

        showReloadErrors = config.getBoolean("show-reload-errors")
                .withDefault(true);

        generateLanguageFiles = config.getBoolean("generate-language-files")
                .withDefault(false);

        coloredConsole = config.getBoolean("colored-console")
                .withDefault(true);

        generateExamples = config.getBoolean("generate-examples")
                .withDefault(true);

        checkForUpdates = config.getBoolean("check-for-updates")
                .withDefault(true);

        metrics = config.getBoolean("metrics")
                .withDefault(true);

        dumpLogo = config.getBoolean("dump-logo")
                .withDefault(true);

        languageFileName = config.getString("language")
                .require(Requirements.minLength(1))
                .map(fileName -> fileName + ".yml")
                .withDefault("languages/en.yml");

        autoSave = config.get("auto-save", Delay.class)
                .withDefault(Delay.fromSeconds(0));

        worldLoadTimeout = config.get("world-load-timeout", Delay.class)
                .withDefault(Delay.fromSeconds(3));

        playerCacheDuration = config.get("player-cache-duration", Delay.class)
                .withDefault(Delay.fromSeconds(30));

        // Modules settings
        disabledModules = new HashSet<>(config.getList("disabled-modules", Module.class)
                .withDefault(List.of()));

        // Mob settings
        playerSlainMessage = config.getString("player-slain-message", ColorUtil.FORMATTER)
                .withDefault(ChatColor.RED + "{player_tc} was killed by a {mob_tc}");

        hologramViewDistance = config.getInteger("hologram-view-distance")
                .withDefault(15);

        preferredHologramProvider = config.get("preferred-hologram-provider", HologramProvider.class)
                .withDefault(HologramProvider.FANCY_HOLOGRAMS);

        if (Server.getVersion() > Version.V1_19_4) {
            hologramStyles = new HashMap<>();
            for (KeyedField field : config.getSectionOrCreate("hologram-styles").getNestedFields()) {
                String name = field.getKey();
                HologramStyle hologramStyle = field.get(HologramStyle.class)
                        .withDefault(null);
                if (hologramStyle != null) {
                    hologramStyles.put(name, hologramStyle);
                }
            }

            defaultHologramStyle = hologramStyles.getOrDefault("default", HologramStyle.createDefaultStyle());
        }

        structureBlacklist = config.getList("structure-blacklist", Material.class)
                .map(HashSet::new)
                .withDefault(new HashSet<>());

        {
            structureWand = config.get("structure-wand", ItemStack.class)
                    .withDefault(new ItemStack(Material.GOLDEN_PICKAXE));

            ItemMeta meta = structureWand.getItemMeta();
            if (meta != null) {
                PersistentData.setString(meta, wandKey, "true");
                structureWand.setItemMeta(meta);
            } else {
                config.collectError(new InvalidConfigException(config, "structure-wand.type", "item does not support item meta"));
            }
        }

        functionNames = new HashSet<>();
        for (File file : plugin.getFiles("functions")) {
            ConfigSection functionConfig = YamlConfig.loadSectionOrEmpty(file);
            for (String key : functionConfig.getKeys()) {
                functionNames.add(CaseFormatter.toSnakeCase(key));
            }
        }

        gameplaySettings.loadConfigurationData(config);
        dropSettings.loadConfigurationData(config);
        statSettings.loadConfigurationData(config);
        itemSettings.loadConfigurationData(config);
        placeholderSettings.loadConfigurationData(config);

        return config;
    }

    public boolean isStats() {
        return !disabledModules.contains(Module.STATS);
    }

    public boolean isKeepConfigUpToDate() {
        return keepConfigUpToDate;
    }

    public boolean isDebug() {
        return debug;
    }

    public boolean isShowReloadErrors() {
        return showReloadErrors;
    }

    public boolean isGenerateLanguageFiles() {
        return generateLanguageFiles;
    }

    public boolean isColoredConsole() {
        return coloredConsole;
    }

    public boolean isGenerateExamples() {
        return generateExamples;
    }

    public boolean isCheckForUpdates() {
        return checkForUpdates;
    }

    public boolean isMetrics() {
        return metrics;
    }

    public boolean isDumpLogo() {
        return dumpLogo;
    }

    public @NotNull String getLanguageFileName() {
        return languageFileName;
    }

    public @NotNull File getLanguageFile() {
        return plugin.getFile(languageFileName);
    }

    public Delay getAutoSave() {
        return autoSave;
    }

    public Delay getWorldLoadTimeout() {
        return worldLoadTimeout;
    }

    public Delay getPlayerCacheDuration() {
        return playerCacheDuration;
    }

    public boolean isShortcuts() {
        return shortcuts;
    }

    public boolean isModuleEnabled(@NotNull Module module) {
        return !disabledModules.contains(module);
    }

    public void addDisabledModule(@NotNull Module module) {
        disabledModules.add(module);
    }

    public @NotNull Set<Module> getEnabledModules() {
        Set<Module> modules = new HashSet<>(Arrays.asList(Module.values()));
        modules.removeAll(disabledModules);
        return modules;
    }

    public @NotNull List<Module> getDisabledModules() {
        return new ArrayList<>(disabledModules);
    }

    public @NotNull String getPlayerSlainMessage() {
        return playerSlainMessage;
    }

    public int getHologramViewDistance() {
        return hologramViewDistance;
    }

    public @NotNull HologramProvider getPreferredHologramProvider() {
        return preferredHologramProvider;
    }

    public @NotNull HologramStyle getDefaultHologramStyle() {
        if (Server.getVersion() < Version.V1_19_4) {
            throw new UnsupportedOperationException("Hologram styles are only available in 1.19.4+");
        }
        return defaultHologramStyle.copy();
    }

    public @Nullable HologramStyle getHologramStyle(@NotNull String name) {
        if (Server.getVersion() < Version.V1_19_4) {
            throw new UnsupportedOperationException("Hologram styles are only available in 1.19.4+");
        }

        HologramStyle hologramStyle = hologramStyles.get(name);
        return hologramStyle != null ? hologramStyle.copy() : null;
    }

    public Set<Material> getStructureBlacklist() {
        return new HashSet<>(structureBlacklist);
    }

    public boolean isStructureWand(@NotNull ItemStack item) {
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }
        return PersistentData.hasString(meta, wandKey);
    }

    public ItemStack getStructureWand() {
        return PlaceholderUtil.parseAll(plugin, structureWand.clone());
    }

    public void applyConfigShortcuts(@NotNull ConfigField field) {
        configShortcuts.forEach(field::replaceAll);
    }

    public @NotNull Set<String> getFunctionNames() {
        return functionNames;
    }

    @Override
    public boolean isShowDeathMessages() {
        return gameplaySettings.isShowDeathMessages();
    }

    @Override
    public boolean isShowSlainMessages() {
        return gameplaySettings.isShowSlainMessages();
    }

    @Override
    public boolean isCustomCowMilking() {
        return gameplaySettings.isCustomCowMilking();
    }

    @Override
    public boolean isHungerDepletion() {
        return gameplaySettings.isHungerDepletion();
    }

    @Override
    public boolean isHungerHealthRegen() {
        return gameplaySettings.isHungerHealthRegen();
    }

    @Override
    public float getExhaustionMultiplier() {
        return gameplaySettings.getExhaustionMultiplier();
    }

    @Override
    public int getHungerCap() {
        return gameplaySettings.getHungerCap();
    }

    @Override
    public int getSaturationCap() {
        return gameplaySettings.getSaturationCap();
    }

    @Override
    public int getCowMaxMilk() {
        return gameplaySettings.getCowMaxMilk();
    }

    @Override
    public @NotNull Amount getMilkProduceAmount() {
        return gameplaySettings.getMilkProduceAmount();
    }

    @Override
    public @NotNull Delay getMilkProduceDelay() {
        return gameplaySettings.getMilkProduceDelay();
    }

    @Override
    public boolean isCustomEggLaying() {
        return gameplaySettings.isCustomEggLaying();
    }

    @Override
    public @NotNull Amount getEggLayAmount() {
        return gameplaySettings.getEggLayAmount();
    }

    @Override
    public @NotNull Delay getEggLayDelay() {
        return gameplaySettings.getEggLayDelay();
    }

    @Override
    public @NotNull ItemDropTarget getDefaultItemDropTarget() {
        return dropSettings.getDefaultItemDropTarget();
    }

    @Override
    public boolean isFortuneDrop(@NotNull Material material) {
        return dropSettings.isFortuneDrop(material);
    }

    @Override
    public boolean isMiningFortuneDrop(@NotNull Material material) {
        return dropSettings.isMiningFortuneDrop(material);
    }

    @Override
    public boolean isLootingDrop(@NotNull Material material) {
        return dropSettings.isLootingDrop(material);
    }

    @Override
    public boolean isAutoSmelt() {
        return dropSettings.isAutoSmelt();
    }

    @Override
    public boolean isTelepathy() {
        return dropSettings.isTelepathy();
    }

    @Override
    public boolean isExperience() {
        return dropSettings.isExperience();
    }

    @Override
    public @Nullable Material getSilkDrop(@NotNull Material originalDrop) {
        return dropSettings.getSilkDrop(originalDrop);
    }

    @Override
    public @Nullable Material getSmeltedDrop(@NotNull Material originalDrop) {
        return dropSettings.getSmeltedDrop(originalDrop);
    }

    @Override
    public double getSmeltChance(@NotNull ItemStack tool) {
        return dropSettings.getSmeltChance(tool);
    }

    @Override
    public double getTelepathyChance(@NotNull ItemStack tool) {
        return dropSettings.getTelepathyChance(tool);
    }

    @Override
    public @NotNull Amount getExperienceMultiplier(@NotNull ItemStack tool) {
        return dropSettings.getExperienceMultiplier(tool);
    }

    @Override
    public boolean isShowStatusBar() {
        return statSettings.isShowStatusBar();
    }

    @Override
    public boolean isInsertMessagesInStatusBar() {
        return statSettings.isInsertMessagesInStatusBar();
    }

    @Override
    public @NotNull Delay getStatusBarMessageDuration() {
        return statSettings.getStatusBarMessageDuration();
    }

    @Override
    public @NotNull BarAlignment getInsertedMessageAlign() {
        return statSettings.getInsertedMessageAlign();
    }

    @Override
    public int getInsertedMessageLength() {
        return statSettings.getInsertedMessageLength();
    }

    @Override
    public @NotNull String getStatusBar() {
        return statSettings.getStatusBar();
    }

    @Override
    public int getBaseDamage() {
        return statSettings.getBaseDamage();
    }

    @Override
    public int getBaseDefense() {
        return statSettings.getBaseDefense();
    }

    @Override
    public int getBaseMaxHealth() {
        return statSettings.getBaseMaxHealth();
    }

    @Override
    public int getBaseHealthRegen() {
        return statSettings.getBaseHealthRegen();
    }

    @Override
    public int getBaseMaxMana() {
        return statSettings.getBaseMaxMana();
    }

    @Override
    public int getBaseManaRegen() {
        return statSettings.getBaseManaRegen();
    }

    @Override
    public double getBaseCritDamage() {
        return statSettings.getBaseCritDamage();
    }

    @Override
    public double getBaseCritChance() {
        return statSettings.getBaseCritChance();
    }

    @Override
    public int getBaseMiningFortune() {
        return statSettings.getBaseMiningFortune();
    }

    @Override
    public int getBaseFarmingFortune() {
        return statSettings.getBaseFarmingFortune();
    }

    @Override
    public int getBaseForagingFortune() {
        return statSettings.getBaseForagingFortune();
    }

    @Override
    public int getBaseMovementSpeed() {
        return statSettings.getBaseMovementSpeed();
    }

    @Override
    public int getBaseAttackSpeed() {
        return statSettings.getBaseAttackSpeed();
    }

    @Override
    public int getBaseMiningSpeed() {
        return statSettings.getBaseMiningSpeed();
    }

    @Override
    public @NotNull Map<Stat, CustomStat> getCustomStats() {
        return statSettings.getCustomStats();
    }

    @Override
    public @Nullable CustomStat getCustomStat(@NotNull Stat statType) {
        return statSettings.getCustomStat(statType);
    }

    @Override
    public @NotNull Delay getCombatDuration() {
        return statSettings.getCombatDuration();
    }

    @Override
    public boolean isRegenHealthDuringCombat() {
        return statSettings.isRegenHealthDuringCombat();
    }

    @Override
    public boolean isRegenManaDuringCombat() {
        return statSettings.isRegenManaDuringCombat();
    }

    @Override
    public boolean isSharpnessEnchantAsStat() {
        return statSettings.isSharpnessEnchantAsStat();
    }

    @Override
    public double getDamageMultiplierFromSharpnessEnchant(@NotNull ItemStack item) {
        return statSettings.getDamageMultiplierFromSharpnessEnchant(item);
    }

    @Override
    public double getSharpnessDamageDebuff(@NotNull ItemStack item) {
        return statSettings.getSharpnessDamageDebuff(item);
    }

    @Override
    public boolean isEfficiencyEnchantAsStat() {
        return statSettings.isEfficiencyEnchantAsStat();
    }

    @Override
    public int getMiningSpeedFromEfficiencyEnchant(@NotNull ItemStack item) {
        return statSettings.getMiningSpeedFromEfficiencyEnchant(item);
    }

    @Override
    public double getEfficiencyMiningSpeedDebuff(@NotNull ItemStack item) {
        return statSettings.getEfficiencyMiningSpeedDebuff(item);
    }

    @Override
    public boolean isFortuneEnchantAsStat() {
        return statSettings.isFortuneEnchantAsStat();
    }

    @Override
    public int getMiningFortuneFromFortuneEnchant(@NotNull ItemStack item) {
        return statSettings.getMiningFortuneFromFortuneEnchant(item);
    }

    @Override
    public int getHealthRegenInterval() {
        return statSettings.getHealthRegenInterval();
    }

    @Override
    public int getManaRegenInterval() {
        return statSettings.getManaRegenInterval();
    }

    @Override
    public boolean isItemRarity(@NotNull String rarity) {
        return itemSettings.isItemRarity(rarity);
    }

    @Override
    public @Nullable String getDefaultItemRarity() {
        return itemSettings.getDefaultItemRarity();
    }

    @Override
    public @Nullable String getItemRarityDisplay(@NotNull String name) {
        return itemSettings.getItemRarityDisplay(name);
    }

    @Override
    public int getStatsDescriptionMaxLines() {
        return itemSettings.getStatsDescriptionMaxLines();
    }

    @Override
    public @NotNull List<String> getStatsDescriptionHeader() {
        return itemSettings.getStatsDescriptionHeader();
    }

    @Override
    public @NotNull List<String> getStatsDescriptionDivider() {
        return itemSettings.getStatsDescriptionDivider();
    }

    @Override
    public @NotNull List<String> getStatsDescriptionFooter() {
        return itemSettings.getStatsDescriptionFooter();
    }

    @Override
    public @NotNull List<String> getStatDescription(@NotNull Stat statType) {
        return itemSettings.getStatDescription(statType);
    }

    @Override
    public int getEnchantsDescriptionMaxLines() {
        return itemSettings.getEnchantsDescriptionMaxLines();
    }

    @Override
    public @NotNull List<String> getEnchantsDescriptionHeader() {
        return itemSettings.getEnchantsDescriptionHeader();
    }

    @Override
    public @NotNull List<String> getEnchantsDescriptionDivider() {
        return itemSettings.getEnchantsDescriptionDivider();
    }

    @Override
    public @NotNull List<String> getEnchantsDescriptionFooter() {
        return itemSettings.getEnchantsDescriptionFooter();
    }

    @Override
    public @NotNull List<String> getEnchantDescription(@NotNull Enchantment enchant) {
        return itemSettings.getEnchantDescription(enchant);
    }

    @Override
    public @NotNull List<String> getAbilityDescriptionHeader() {
        return itemSettings.getAbilityDescriptionHeader();
    }

    @Override
    public @NotNull List<String> getAbilityDescriptionDivider() {
        return itemSettings.getAbilityDescriptionDivider();
    }

    @Override
    public @NotNull List<String> getAbilityDescriptionFooter() {
        return itemSettings.getAbilityDescriptionFooter();
    }

    @Override
    public @NotNull List<String> getAbilityDescriptionFormat() {
        return itemSettings.getAbilityDescriptionFormat();
    }

    @Override
    public @NotNull List<String> getLorePartsHeader() {
        return itemSettings.getLorePartsHeader();
    }

    @Override
    public @NotNull List<String> getLorePartsFooter() {
        return itemSettings.getLorePartsFooter();
    }

    @Override
    public @NotNull String getLoreFormat() {
        return itemSettings.getLoreFormat();
    }

    @Override
    public @NotNull List<String> getDefaultItemLore() {
        return itemSettings.getDefaultItemLore();
    }

    @Override
    public boolean isBreakingPower() {
        return itemSettings.isBreakingPower();
    }

    @Override
    public @Nullable BreakingPower getBlockBreakingPower(@NotNull Material block) {
        return itemSettings.getBlockBreakingPower(block);
    }

    @Override
    public @NotNull Set<BreakingPower> getBreakingPowers() {
        return itemSettings.getBreakingPowers();
    }

    @Override
    public @Nullable Object getPlaceholderFallback(@NotNull String placeholder) {
        return placeholderSettings.getPlaceholderFallback(placeholder);
    }

    @Override
    public void registerPlaceholderFallback(@NotNull String placeholder, @NotNull Object fallback) {
        placeholderSettings.registerPlaceholderFallback(placeholder, fallback);
    }

    @Override
    public @NotNull List<ProgressBar> getProgressBars() {
        return placeholderSettings.getProgressBars();
    }

    @Override
    public @NotNull List<ProgressBar> getCountdownBars() {
        return placeholderSettings.getCountdownBars();
    }

}
