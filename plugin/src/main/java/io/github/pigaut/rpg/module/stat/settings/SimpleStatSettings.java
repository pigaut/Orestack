package io.github.pigaut.rpg.module.stat.settings;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.module.stat.custom.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.module.stat.custom.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.delay.*;
import io.github.pigaut.yaml.node.*;
import io.github.pigaut.yaml.node.line.*;
import io.github.pigaut.yaml.node.scalar.*;
import io.github.pigaut.yaml.node.section.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SimpleStatSettings implements StatSettings {

    private final EnhancedPlugin plugin;

    private boolean showStatusBar;
    private boolean insertMessagesInStatusBar;
    private Delay statusBarMessageDuration;
    private int insertedMessageLength;
    private BarAlignment insertedMessageAlign;
    private String statusBar;
    private int baseDamage;
    private int baseDefense;
    private int baseMaxHealth;
    private int baseManaRegen;
    private int baseHealthRegen;
    private int baseMaxMana;
    private int healthRegenInterval;
    private int manaRegenInterval;
    private double baseCritDamage;
    private double baseCritChance;
    private int baseMiningFortune;
    private int baseFarmingFortune;
    private int baseForagingFortune;
    private int baseMovementSpeed;
    private int baseAttackSpeed;
    private int baseMiningSpeed;
    private Map<Stat, CustomStat> customStats;
    private Delay combatDuration;
    private boolean regenHealthDuringCombat;
    private boolean regenManaDuringCombat;

    private boolean sharpnessEnchantAsStat;
    private Map<Integer, Double> damageMultiplierByEnchantLevel;

    private boolean efficiencyEnchantAsStat;
    private Map<Integer, Integer> miningSpeedByEnchantLevel;

    private boolean fortuneEnchantAsStat;
    private Map<Integer, Integer> miningFortuneByEnchantLevel;

    public SimpleStatSettings(@NotNull EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfigurationData(@NotNull ConfigSection config) {
        showStatusBar = config.getBoolean("show-status-bar")
                .withDefault(true);

        insertMessagesInStatusBar = config.getBoolean("insert-messages-in-status-bar")
                .withDefault(true);

        statusBarMessageDuration = config.get("status-bar-messages.duration", Delay.class)
                .withDefault(Delay.fromSeconds(3));

        insertedMessageLength = config.getInteger("status-bar-messages.length")
                .require(Requirements.positive())
                .withDefault(18);

        insertedMessageAlign = config.get("status-bar-messages.align", BarAlignment.class)
                .withDefault(BarAlignment.CENTER);

        statusBar = config.getString("status-bar", ColorUtil.FORMATTER)
                .withDefault("&cStatus Bar Not Set");

        baseDamage = config.getInteger("base-stats.damage")
                .require(Requirements.positive())
                .withDefault(1);

        baseDefense = config.getInteger("base-stats.defense")
                .require(Requirements.positive())
                .withDefault(1);

        baseMaxHealth = config.getInteger("base-stats.max-health")
                .require(Requirements.positive())
                .withDefault(100);

        baseHealthRegen = config.getInteger("base-stats.health-regen")
                .require(Requirements.positive())
                .withDefault(3);

        healthRegenInterval = config.getString("health-regen-interval").test(s -> s.equals("none")) ? -1 :
                config.get("health-regen-interval", Delay.class)
                    .map(Delay::toTicks)
                    .withDefault(40);

        baseManaRegen = config.getInteger("base-stats.mana-regen")
                .require(Requirements.positive())
                .withDefault(2);

        baseMaxMana = config.getInteger("base-stats.max-mana")
                .require(Requirements.positive())
                .withDefault(100);

        manaRegenInterval = config.getString("mana-regen-interval").test(s -> s.equals("none")) ? -1 :
                config.get("mana-regen-interval", Delay.class)
                    .map(Delay::toTicks)
                    .withDefault(20);

        baseCritDamage = config.getDouble("base-stats.crit-damage")
                .require(Requirements.positive())
                .withDefault(0.5);

        baseCritChance = config.getDouble("base-stats.crit-chance")
                .require(Requirements.between(0, 1))
                .withDefault(0.3);

        baseMiningFortune = config.getInteger("base-stats.mining-fortune")
                .require(Requirements.positive())
                .withDefault(0);

        baseFarmingFortune = config.getInteger("base-stats.farming-fortune")
                .require(Requirements.positive())
                .withDefault(0);

        baseForagingFortune = config.getInteger("base-stats.foraging-fortune")
                .require(Requirements.positive())
                .withDefault(0);

        baseMovementSpeed = config.getInteger("base-stats.movement-speed")
                .require(Requirements.positive())
                .withDefault(0);

        baseAttackSpeed = config.getInteger("base-stats.attack-speed")
                .require(Requirements.positive())
                .withDefault(0);

        baseMiningSpeed = config.getInteger("base-stats.mining-speed")
                .require(Requirements.positive())
                .withDefault(0);

        // Pre-register stats for internal usage, they are later loaded again when manager boots
        StatManager stats = plugin.getStats();
        stats.clear();
        for (Stat baseStat : BaseStats.values()) {
            stats.register(baseStat.getName(), baseStat);
        }

        customStats = new HashMap<>();
        for (KeyedField field : config.getNestedFields("custom-stats")) {
            String name = CaseFormatter.toSnakeCase(field.getKey());
            if (stats.contains(name)) {
                config.collectError(new InvalidConfigException(field, name, "Stat name already in use: " + name));
                continue;
            }

            Stat stat = new Stat(name);
            stats.register(name, stat);

            field.get(CustomStat.class).ifValid(customStat -> customStats.put(stat, customStat));
        }

        combatDuration = config.get("in-combat-duration", Delay.class)
                .withDefault(Delay.fromSeconds(10));

        regenHealthDuringCombat = config.getBoolean("regen-health-during-combat")
                .withDefault(false);

        regenManaDuringCombat = config.getBoolean("regen-mana-during-combat")
                .withDefault(true);

        sharpnessEnchantAsStat = config.getBoolean("sharpness-enchant-as-stat")
                .withDefault(true);

        damageMultiplierByEnchantLevel = new HashMap<>();
        for (KeyedScalar scalar : config.getSectionOrCreate("damage-multiplier-by-sharpness-enchant-level").getNestedScalars()) {
            Integer level = scalar.getIntegerKey()
                    .require(Requirements.positive())
                    .withDefault(null);

            Double attackDamage = scalar.toDouble()
                    .map(value -> value + 1)
                    .withDefault(null);

            if (level != null && attackDamage != null) {
                damageMultiplierByEnchantLevel.put(level, attackDamage);
            }
        }

        efficiencyEnchantAsStat = config.getBoolean("efficiency-enchant-as-stat")
                .withDefault(true);

        miningSpeedByEnchantLevel = new HashMap<>();
        for (KeyedScalar scalar : config.getSectionOrCreate("mining-speed-by-efficiency-enchant-level").getNestedScalars()) {
            Integer level = scalar.getIntegerKey()
                    .require(Requirements.positive())
                    .withDefault(null);

            Integer miningSpeed = scalar.toInteger()
                    .withDefault(null);

            if (level != null && miningSpeed != null) {
                miningSpeedByEnchantLevel.put(level, miningSpeed);
            }
        }

        fortuneEnchantAsStat = config.getBoolean("fortune-enchant-as-stat")
                .withDefault(true);

        miningFortuneByEnchantLevel = new HashMap<>();
        for (KeyedScalar scalar : config.getSectionOrCreate("mining-fortune-by-fortune-enchant-level").getNestedScalars()) {
            Integer level = scalar.getIntegerKey()
                    .require(Requirements.positive())
                    .withDefault(null);

            Integer miningFortune = scalar.toInteger()
                    .withDefault(null);

            if (level != null && miningFortune != null) {
                miningFortuneByEnchantLevel.put(level, miningFortune);
            }
        }
    }

    @Override
    public boolean isShowStatusBar() {
        return showStatusBar;
    }

    @Override
    public boolean isInsertMessagesInStatusBar() {
        return insertMessagesInStatusBar;
    }

    @Override
    public @NotNull Delay getStatusBarMessageDuration() {
        return statusBarMessageDuration;
    }

    @Override
    public @NotNull BarAlignment getInsertedMessageAlign() {
        return insertedMessageAlign;
    }

    @Override
    public int getInsertedMessageLength() {
        return insertedMessageLength;
    }

    @Override
    public @NotNull String getStatusBar() {
        return statusBar;
    }

    @Override
    public int getBaseDamage() {
        return baseDamage;
    }

    @Override
    public int getBaseDefense() {
        return baseDefense;
    }

    @Override
    public int getBaseMaxHealth() {
        return baseMaxHealth;
    }

    @Override
    public int getBaseHealthRegen() {
        return baseHealthRegen;
    }

    @Override
    public int getBaseMaxMana() {
        return baseMaxMana;
    }

    @Override
    public int getBaseManaRegen() {
        return baseManaRegen;
    }

    @Override
    public double getBaseCritDamage() {
        return baseCritDamage;
    }

    @Override
    public double getBaseCritChance() {
        return baseCritChance;
    }

    @Override
    public int getBaseMiningFortune() {
        return baseMiningFortune;
    }

    @Override
    public int getBaseFarmingFortune() {
        return baseFarmingFortune;
    }

    @Override
    public int getBaseForagingFortune() {
        return baseForagingFortune;
    }

    @Override
    public int getBaseMovementSpeed() {
        return baseMovementSpeed;
    }

    @Override
    public int getBaseAttackSpeed() {
        return baseAttackSpeed;
    }

    @Override
    public int getBaseMiningSpeed() {
        return baseMiningSpeed;
    }

    @Override
    public @NotNull Map<Stat, CustomStat> getCustomStats() {
        return new HashMap<>(customStats);
    }

    @Override
    public @Nullable CustomStat getCustomStat(@NotNull Stat statType) {
        return customStats.get(statType);
    }

    @Override
    public @NotNull Delay getCombatDuration() {
        return combatDuration;
    }

    @Override
    public boolean isRegenHealthDuringCombat() {
        return regenHealthDuringCombat;
    }

    @Override
    public boolean isRegenManaDuringCombat() {
        return regenManaDuringCombat;
    }

    @Override
    public boolean isSharpnessEnchantAsStat() {
        return sharpnessEnchantAsStat;
    }

    @Override
    public double getDamageMultiplierFromSharpnessEnchant(@NotNull ItemStack item) {
        int sharpnessEnchantLevel = item.getEnchantmentLevel(Enchants.SHARPNESS);
        return damageMultiplierByEnchantLevel.getOrDefault(sharpnessEnchantLevel, 0d);
    }

    private static final double[] SHARPNESS_DEBUFFS = { -1.0, -1.5, -2.0, -2.5, -3.0, -3.5, -4.0, -4.5, -5.0, -5.5, -6.0, -6.5,
            -7.0, -7.5, -8.0, -8.5, -9.0, -9.5, -10.0, -10.5, -11.0 };

    @Override
    public double getSharpnessDamageDebuff(@NotNull ItemStack item) {
        int sharpnessEnchantLevel = item.getEnchantmentLevel(Enchants.SHARPNESS);
        if (sharpnessEnchantLevel > 20) return -11.0;
        return SHARPNESS_DEBUFFS[sharpnessEnchantLevel];
    }

    @Override
    public boolean isEfficiencyEnchantAsStat() {
        return efficiencyEnchantAsStat;
    }

    @Override
    public int getMiningSpeedFromEfficiencyEnchant(@NotNull ItemStack item) {
        int efficiencyEnchantLevel = item.getEnchantmentLevel(Enchants.EFFICIENCY);
        return miningSpeedByEnchantLevel.getOrDefault(efficiencyEnchantLevel, 0);
    }

    private static final double[] EFFICIENCY_DEBUFFS = { 0, -5, -10, -15, -20, -25, -30, -35, -40, -45, -50, -55, -60, -65,
            -70, -75, -80, -85, -90, -95, -100 };

    @Override
    public double getEfficiencyMiningSpeedDebuff(@NotNull ItemStack item) {
        int efficiencyEnchantLevel = item.getEnchantmentLevel(Enchants.EFFICIENCY);
        if (efficiencyEnchantLevel > 20) return -100;
        return EFFICIENCY_DEBUFFS[efficiencyEnchantLevel];
    }

    @Override
    public boolean isFortuneEnchantAsStat() {
        return fortuneEnchantAsStat;
    }

    @Override
    public int getMiningFortuneFromFortuneEnchant(@NotNull ItemStack item) {
        int fortuneEnchantLevel = item.getEnchantmentLevel(Enchants.FORTUNE);
        return miningFortuneByEnchantLevel.getOrDefault(fortuneEnchantLevel, 0);
    }

    @Override
    public int getHealthRegenInterval() {
        return healthRegenInterval;
    }

    @Override
    public int getManaRegenInterval() {
        return manaRegenInterval;
    }

}
