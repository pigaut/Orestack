package io.github.pigaut.rpg.module.stat;

import io.github.pigaut.yaml.convert.format.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BaseStats {

    public static final Stat DAMAGE = new Stat("damage");
    public static final Stat DEFENSE = new Stat("defense");
    public static final Stat MAX_HEALTH = new Stat("max_health");
    public static final Stat MAX_MANA = new Stat("max_mana");
    public static final Stat HEALTH_REGEN = new Stat("health_regen");
    public static final Stat MANA_REGEN = new Stat("mana_regen");
    public static final Stat CRIT_DAMAGE = new Stat("crit_damage");
    public static final Stat CRIT_CHANCE = new Stat("crit_chance");
    public static final Stat EXP_GAIN = new Stat("exp_gain");
    public static final Stat MINING_FORTUNE = new Stat("mining_fortune");
    public static final Stat FARMING_FORTUNE = new Stat("farming_fortune");
    public static final Stat FORAGING_FORTUNE = new Stat("foraging_fortune");
    public static final Stat MOVEMENT_SPEED = new Stat("movement_speed");
    public static final Stat ATTACK_SPEED = new Stat("attack_speed");
    public static final Stat MINING_SPEED = new Stat("mining_speed");

    private static final Map<String, Stat> BASE_STATS_BY_NAME = new HashMap<>();

    static {
        BASE_STATS_BY_NAME.put(DAMAGE.getName(), DAMAGE);
        BASE_STATS_BY_NAME.put(DEFENSE.getName(), DEFENSE);
        BASE_STATS_BY_NAME.put(MAX_HEALTH.getName(), MAX_HEALTH);
        BASE_STATS_BY_NAME.put(MAX_MANA.getName(), MAX_MANA);
        BASE_STATS_BY_NAME.put(HEALTH_REGEN.getName(), HEALTH_REGEN);
        BASE_STATS_BY_NAME.put(MANA_REGEN.getName(), MANA_REGEN);
        BASE_STATS_BY_NAME.put(CRIT_DAMAGE.getName(), CRIT_DAMAGE);
        BASE_STATS_BY_NAME.put(CRIT_CHANCE.getName(), CRIT_CHANCE);
        BASE_STATS_BY_NAME.put(EXP_GAIN.getName(), EXP_GAIN);
        BASE_STATS_BY_NAME.put(MINING_FORTUNE.getName(), MINING_FORTUNE);
        BASE_STATS_BY_NAME.put(FARMING_FORTUNE.getName(), FARMING_FORTUNE);
        BASE_STATS_BY_NAME.put(FORAGING_FORTUNE.getName(), FORAGING_FORTUNE);
        BASE_STATS_BY_NAME.put(MOVEMENT_SPEED.getName(), MOVEMENT_SPEED);
        BASE_STATS_BY_NAME.put(ATTACK_SPEED.getName(), ATTACK_SPEED);
        BASE_STATS_BY_NAME.put(MINING_SPEED.getName(), MINING_SPEED);
    }

    public static boolean contains(@NotNull String name) {
        return BASE_STATS_BY_NAME.containsKey(CaseFormatter.toSnakeCase(name));
    }

    public static boolean contains(@NotNull Stat statType) {
        return BASE_STATS_BY_NAME.containsValue(statType);
    }

    public static @Nullable Stat get(@NotNull String name) {
        return BASE_STATS_BY_NAME.get(CaseFormatter.toSnakeCase(name));
    }

    public static @NotNull Collection<Stat> values() {
        return new ArrayList<>(BASE_STATS_BY_NAME.values());
    }

}
