package io.github.pigaut.rpg.module.stat.settings;

import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.module.stat.custom.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.delay.*;
import io.github.pigaut.yaml.node.*;
import io.github.pigaut.yaml.node.scalar.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;
import static org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.*;

public class StatConfigSettings implements StatSettings {

    private final EnhancedPlugin plugin;
    private final Settings settings;

    public StatConfigSettings(@NotNull EnhancedPlugin plugin, @NotNull Settings settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    private Boolean showStatusBar;
    private Boolean insertMessagesInStatusBar;
    private Delay statusBarMessageDuration;
    private Integer insertedMessageLength;
    private BarAlignment insertedMessageAlign;
    private String statusBar;
    private Integer baseDamage;
    private Integer baseDefense;
    private Integer baseMaxHealth;
    private Integer baseManaRegen;
    private Integer baseHealthRegen;
    private Integer baseMaxMana;
    private Integer healthRegenInterval;
    private Integer manaRegenInterval;
    private Double baseCritDamage;
    private Double baseCritChance;
    private Integer baseMiningFortune;
    private Integer baseFarmingFortune;
    private Integer baseForagingFortune;
    private Integer baseMovementSpeed;
    private Integer baseAttackSpeed;
    private Integer baseMiningSpeed;
    private Map<Stat, CustomStat> customStats;
    private Delay combatDuration;
    private Boolean regenHealthDuringCombat;
    private Boolean regenManaDuringCombat;

    private Boolean sharpnessEnchantAsStat;
    private Map<Integer, Double> damageMultiplierByEnchantLevel;

    private Boolean efficiencyEnchantAsStat;
    private Map<Integer, Integer> miningSpeedByEnchantLevel;

    private Boolean fortuneEnchantAsStat;
    private Map<Integer, Integer> miningFortuneByEnchantLevel;

    private Map<DamageCause, Double> damageMultiplierByCause;
    private Double defaultDamageMultiplier = 1.0;

    public void loadConfiguration(@NotNull ConfigSection config) {
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

        statusBar = config.getString("status-bar")
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
                    .mapIfValid(Delay::toTicks)
                    .withDefault(40);

        baseManaRegen = config.getInteger("base-stats.mana-regen")
                .require(Requirements.positive())
                .withDefault(2);

        baseMaxMana = config.getInteger("base-stats.max-mana")
                .require(Requirements.positive())
                .withDefault(100);

        manaRegenInterval = config.getString("mana-regen-interval").test(s -> s.equals("none")) ? -1 :
                config.get("mana-regen-interval", Delay.class)
                    .mapIfValid(Delay::toTicks)
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
        for (KeyedField field : config.getSectionOrEmpty("custom-stats").getNestedFields()) {
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

            Double damageMultiplier = scalar.toDouble()
                    .withDefault(null);

            if (level != null && damageMultiplier != null) {
                damageMultiplierByEnchantLevel.put(level, damageMultiplier);
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

        defaultDamageMultiplier = config.getDouble("default-damage-multiplier")
                .require(Requirements.positive())
                .withDefault(1.0);

        damageMultiplierByCause = new HashMap<>();
        for (KeyedScalar scalar : config.getSectionOrEmpty("damage-multipliers").getNestedScalars()) {
            DamageCause damageCause = scalar.getKeyAs(DamageCause.class)
                    .withDefault(null);

            Double damageMultiplier = scalar.toDouble()
                    .withDefault(null);

            if (damageCause != null && damageMultiplier != null) {
                damageMultiplierByCause.put(damageCause, damageMultiplier);
            }
        }
    }

    @Override
    public boolean isShowStatusBar() {
        settings.checkLoaded(showStatusBar);
        return showStatusBar;
    }

    @Override
    public boolean isInsertMessagesInStatusBar() {
        settings.checkLoaded(insertMessagesInStatusBar);
        return insertMessagesInStatusBar;
    }

    @Override
    public @NotNull Delay getStatusBarMessageDuration() {
        settings.checkLoaded(statusBarMessageDuration);
        return statusBarMessageDuration;
    }

    @Override
    public @NotNull BarAlignment getInsertedMessageAlign() {
        settings.checkLoaded(insertedMessageAlign);
        return insertedMessageAlign;
    }

    @Override
    public int getInsertedMessageLength() {
        settings.checkLoaded(insertedMessageLength);
        return insertedMessageLength;
    }

    @Override
    public @NotNull String getStatusBar() {
        settings.checkLoaded(statusBar);
        return statusBar;
    }

    @Override
    public int getBaseDamage() {
        settings.checkLoaded(baseDamage);
        return baseDamage;
    }

    @Override
    public int getBaseDefense() {
        settings.checkLoaded(baseDefense);
        return baseDefense;
    }

    @Override
    public int getBaseMaxHealth() {
        settings.checkLoaded(baseMaxHealth);
        return baseMaxHealth;
    }

    @Override
    public int getBaseHealthRegen() {
        settings.checkLoaded(baseHealthRegen);
        return baseHealthRegen;
    }

    @Override
    public int getBaseMaxMana() {
        settings.checkLoaded(baseMaxMana);
        return baseMaxMana;
    }

    @Override
    public int getBaseManaRegen() {
        settings.checkLoaded(baseManaRegen);
        return baseManaRegen;
    }

    @Override
    public double getBaseCritDamage() {
        settings.checkLoaded(baseCritDamage);
        return baseCritDamage;
    }

    @Override
    public double getBaseCritChance() {
        settings.checkLoaded(baseCritChance);
        return baseCritChance;
    }

    @Override
    public int getBaseMiningFortune() {
        settings.checkLoaded(baseMiningFortune);
        return baseMiningFortune;
    }

    @Override
    public int getBaseFarmingFortune() {
        settings.checkLoaded(baseFarmingFortune);
        return baseFarmingFortune;
    }

    @Override
    public int getBaseForagingFortune() {
        settings.checkLoaded(baseForagingFortune);
        return baseForagingFortune;
    }

    @Override
    public int getBaseMovementSpeed() {
        settings.checkLoaded(baseMovementSpeed);
        return baseMovementSpeed;
    }

    @Override
    public int getBaseAttackSpeed() {
        settings.checkLoaded(baseAttackSpeed);
        return baseAttackSpeed;
    }

    @Override
    public int getBaseMiningSpeed() {
        settings.checkLoaded(baseMiningSpeed);
        return baseMiningSpeed;
    }

    @Override
    public @NotNull Map<Stat, CustomStat> getCustomStats() {
        settings.checkLoaded(customStats);
        return new HashMap<>(customStats);
    }

    @Override
    public @Nullable CustomStat getCustomStat(@NotNull Stat statType) {
        settings.checkLoaded(customStats);
        return customStats.get(statType);
    }

    @Override
    public @NotNull Delay getCombatDuration() {
        settings.checkLoaded(combatDuration);
        return combatDuration;
    }

    @Override
    public boolean isRegenHealthDuringCombat() {
        settings.checkLoaded(regenHealthDuringCombat);
        return regenHealthDuringCombat;
    }

    @Override
    public boolean isRegenManaDuringCombat() {
        settings.checkLoaded(regenManaDuringCombat);
        return regenManaDuringCombat;
    }

    @Override
    public boolean isSharpnessEnchantAsStat() {
        settings.checkLoaded(sharpnessEnchantAsStat);
        return sharpnessEnchantAsStat;
    }

    @Override
    public double getDamageMultiplierFromSharpnessEnchant(@NotNull ItemStack item) {
        settings.checkLoaded(damageMultiplierByEnchantLevel);
        int sharpnessEnchantLevel = item.getEnchantmentLevel(Enchants.SHARPNESS);
        return damageMultiplierByEnchantLevel.getOrDefault(sharpnessEnchantLevel, 1d);
    }

    @Override
    public boolean isEfficiencyEnchantAsStat() {
        settings.checkLoaded(efficiencyEnchantAsStat);
        return efficiencyEnchantAsStat;
    }

    @Override
    public int getMiningSpeedFromEfficiencyEnchant(@NotNull ItemStack item) {
        settings.checkLoaded(miningSpeedByEnchantLevel);
        int efficiencyEnchantLevel = item.getEnchantmentLevel(Enchants.EFFICIENCY);
        return miningSpeedByEnchantLevel.getOrDefault(efficiencyEnchantLevel, 0);
    }

    @Override
    public boolean isFortuneEnchantAsStat() {
        settings.checkLoaded(fortuneEnchantAsStat);
        return fortuneEnchantAsStat;
    }

    @Override
    public int getMiningFortuneFromFortuneEnchant(@NotNull ItemStack item) {
        settings.checkLoaded(miningFortuneByEnchantLevel);
        int fortuneEnchantLevel = item.getEnchantmentLevel(Enchants.FORTUNE);
        return miningFortuneByEnchantLevel.getOrDefault(fortuneEnchantLevel, 0);
    }

    @Override
    public int getHealthRegenInterval() {
        settings.checkLoaded(healthRegenInterval);
        return healthRegenInterval;
    }

    @Override
    public int getManaRegenInterval() {
        settings.checkLoaded(manaRegenInterval);
        return manaRegenInterval;
    }

    @Override
    public double getDamageMultiplier(@NotNull DamageCause cause) {
        settings.checkLoaded(damageMultiplierByCause);
        settings.checkLoaded(defaultDamageMultiplier);
        return damageMultiplierByCause.getOrDefault(cause, defaultDamageMultiplier);
    }

}
